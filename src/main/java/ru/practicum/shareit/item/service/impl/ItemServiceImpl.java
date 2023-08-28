package ru.practicum.shareit.item.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.UnauthorizedAccessException;
import ru.practicum.shareit.item.dto.CommentDetailsInfoDto;
import ru.practicum.shareit.item.mapper.CommentMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validation;
import javax.validation.ValidationException;
import javax.validation.Validator;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;
    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Override
    public Item getItemById(Long itemId, Long userId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Вещь #" + itemId + " не найдена."));
        Long itemOwnerId = item.getOwner().getId();

        setComments(item);

        return itemOwnerId.equals(userId) ? setLastAndNextBookingData(item) : item;
    }

    @Override
    public List<Item> getItemsByOwnerId(Long ownerId) {
        return itemRepository.findItemsByOwnerIdOrderById(ownerId).stream()
                .map(this::setLastAndNextBookingData)
                .map(this::setComments)
                .collect(Collectors.toList());
    }

    @Override
    public List<Item> searchItems(String searchText) {
        return (!searchText.isBlank()) ? itemRepository.search(searchText) : Collections.emptyList();
    }

    @Override
    public Item createItem(Item item, Long ownerId) {
        User owner = userRepository.findById(ownerId)
                .orElseThrow(() -> new NotFoundException("Пользователь #" + ownerId + " не найден."));

        item.setOwner(owner);
        return itemRepository.save(item);
    }

    @Override
    public Item updateItem(Item item, Long ownerId) {
        Long itemId = item.getId();
        Item currentItem = getItemById(itemId, ownerId);

        User owner = userRepository.findById(ownerId)
                .orElseThrow(() -> new NotFoundException("Пользователь #" + ownerId + " не найден."));

        if (!Objects.equals(currentItem.getOwner().getId(), ownerId)) {
            final String errorMessage = "Недостаточно прав доступа для изменения данных вещи #" + itemId + ".";
            throw new UnauthorizedAccessException(errorMessage);
        }

        item.setOwner(owner);

        if (Objects.isNull(item.getName())) {
            item.setName(currentItem.getName());
        }

        if (Objects.isNull(item.getDescription())) {
            item.setDescription(currentItem.getDescription());
        }

        if (Objects.isNull(item.getAvailable())) {
            item.setAvailable(currentItem.getAvailable());
        }

        Set<ConstraintViolation<Item>> violations = validator.validate(item);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }

        return itemRepository.save(item);
    }

    @Override
    public Comment createComment(Long itemId, Comment comment, Long authorId) {
        LocalDateTime currentDateTime = LocalDateTime.now();

        User author = userRepository.findById(authorId)
                .orElseThrow(() -> new NotFoundException("Пользователь #" + authorId + " не найден."));

        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Вещь #" + itemId + " не найдена."));

        boolean isAvailableForComment = bookingRepository.existsByBookerIdAndItemIdAndEndBeforeAndStatus(authorId,
                itemId, currentDateTime, BookingStatus.APPROVED);

        if (!isAvailableForComment) {
            throw new ValidationException("Пользователь #" + authorId + " не брал в аренду вещь #" + authorId + " " +
                    "или срок аренды еще не завершен.");
        }

        comment.setItem(item);
        comment.setAuthor(author);
        comment.setCreated(currentDateTime);
        return commentRepository.save(comment);
    }

    private Item setLastAndNextBookingData(Item item) {
        Long itemId = item.getId();
        LocalDateTime currentDateTime = LocalDateTime.now();

        Optional<Booking> lastBooking = bookingRepository
                .findFirstBookingByItemIdAndStartBeforeAndStatusNotOrderByStartDesc(itemId,
                        currentDateTime,
                        BookingStatus.REJECTED);

        Optional<Booking> nextBooking = bookingRepository
                .findFirstBookingByItemIdAndStartAfterAndStatusNotOrderByStart(itemId, currentDateTime,
                        BookingStatus.REJECTED);

        lastBooking.ifPresent(booking -> item.setLastBooking(BookingMapper.toBookingDatesDto(booking)));
        nextBooking.ifPresent(booking -> item.setNextBooking(BookingMapper.toBookingDatesDto(booking)));

        return item;
    }

    private Item setComments(Item item) {
        Long itemId = item.getId();

        List<CommentDetailsInfoDto> comments = commentRepository.findAllByItemId(itemId).stream()
                .map(CommentMapper::toCommentDetailsInfoDto)
                .collect(Collectors.toList());

        item.setComments(comments);
        return item;
    }
}