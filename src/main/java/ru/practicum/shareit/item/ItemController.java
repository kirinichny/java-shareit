package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.handlers.HeaderConstants;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;

import javax.validation.Valid;
import java.util.List;

import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
@Slf4j
public class ItemController {
    private final ItemService itemService;

    @GetMapping
    public List<ItemDto> getItemsByOwnerId(@RequestHeader(HeaderConstants.X_SHARER_USER_ID) long ownerId) {
        log.debug("+ getItemsByOwnerId: ownerId={}", ownerId);
        List<ItemDto> items = itemService.getItemsByOwnerId(ownerId).stream()
                .map(ItemMapper::toItemDto)
                .collect(toList());
        log.debug("- getItemsByOwnerId: {}", items);
        return items;
    }

    @GetMapping("/{itemId}")
    public ItemDto getItemById(@PathVariable Long itemId) {
        log.debug("+ getItemById: itemId={}", itemId);
        ItemDto item = ItemMapper.toItemDto(itemService.getItemById(itemId));
        log.debug("- getItemById: {}", item);
        return item;
    }

    @GetMapping("/search")
    public List<ItemDto> searchItems(@RequestParam String text) {
        log.debug("+ searchItems: text={}", text);
        List<ItemDto> items = itemService.searchItems(text).stream()
                .map(ItemMapper::toItemDto)
                .collect(toList());
        log.debug("- searchItems: {}", items);
        return items;
    }

    @PostMapping
    public ItemDto createItem(@RequestBody @Valid ItemDto item,
                              @RequestHeader(HeaderConstants.X_SHARER_USER_ID) long userId) {
        log.debug("+ createItem: item={}, userId={}", item, userId);

        Item createdItem = itemService.createItem(ItemMapper.toItem(item), userId);
        ItemDto createdItemDto = ItemMapper.toItemDto(createdItem);

        log.debug("- createItem: {}", createdItemDto);

        return createdItemDto;
    }

    @PatchMapping("/{itemId}")
    public ItemDto updateItem(@PathVariable Long itemId,
                              @RequestBody ItemDto item,
                              @RequestHeader(HeaderConstants.X_SHARER_USER_ID) long userId) {
        log.debug("+ updateItem: itemId={}, item={}, userId={}", itemId, item, userId);

        item.setId(itemId);
        Item updatedItem = itemService.updateItem(ItemMapper.toItem(item), userId);
        ItemDto updatedItemDto = ItemMapper.toItemDto(updatedItem);

        log.debug("- updateItem: {}", updatedItemDto);

        return updatedItemDto;
    }
}