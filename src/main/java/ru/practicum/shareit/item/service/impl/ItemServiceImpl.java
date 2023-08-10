package ru.practicum.shareit.item.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.item.storage.ItemStorage;
import ru.practicum.shareit.user.storage.UserStorage;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemStorage itemStorage;
    private final UserStorage userStorage;
    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Override
    public Item getItemById(Long itemId) {
        return itemStorage.getItemById(itemId);
    }

    @Override
    public List<Item> getItemsByOwnerId(Long ownerId) {
        return itemStorage.getItemsByOwnerId(ownerId);
    }

    @Override
    public List<Item> searchItems(String searchText) {
        return (!searchText.isBlank()) ? itemStorage.searchItems(searchText) : Collections.emptyList();
    }

    @Override
    public Item createItem(Item item, Long ownerId) {
        item.setOwner(userStorage.getUserById(ownerId));
        itemStorage.createItem(item);
        return item;
    }

    @Override
    public Item updateItem(Item item, Long ownerId) {

        Item currentItem = getItemById(item.getId());

        item.setOwner(userStorage.getUserById(ownerId));

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

        itemStorage.updateItem(item);

        return item;
    }
}