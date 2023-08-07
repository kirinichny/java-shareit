package ru.practicum.shareit.item.storage.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.UnauthorizedAccessException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemStorage;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
@Slf4j
public class InMemoryItemStorageImpl implements ItemStorage {
    private final Map<Long, Item> itemsData = new HashMap<>();
    private Long lastItemId = 0L;

    private Long generateNewItemId() {
        return ++lastItemId;
    }

    @Override
    public Item getItemById(Long itemId) {
        verifyItemExists(itemId);
        return itemsData.get(itemId);
    }

    @Override
    public List<Item> getItemsByOwnerId(Long ownerId) {
        return itemsData.values().stream()
                .filter(item -> item.getOwner().getId().equals(ownerId))
                .collect(Collectors.toList());
    }

    @Override
    public List<Item> searchItems(String searchText) {
        if (searchText.isEmpty()) {
            return Collections.emptyList();
        }

        final String lowercaseSearchText = searchText.toLowerCase();

        return itemsData.values().stream()
                .filter(Item::getAvailable)
                .filter(item -> item.getName().toLowerCase().contains(lowercaseSearchText)
                        || item.getDescription().toLowerCase().contains(lowercaseSearchText))
                .collect(Collectors.toList());
    }

    @Override
    public Long createItem(Item item) {
        final Long id = generateNewItemId();
        item.setId(id);

        itemsData.put(id, item);

        return item.getId();
    }

    @Override
    public Long updateItem(Item updatedItem) {
        final Long itemId = updatedItem.getId();

        verifyItemExists(itemId);

        Item item = itemsData.get(itemId);

        if (!Objects.equals(item.getOwner().getId(), updatedItem.getOwner().getId())) {
            final String errorMessage = "Недостаточно прав доступа для изменения данных вещи #" + itemId + ".";
            log.error(errorMessage);
            throw new UnauthorizedAccessException(errorMessage);
        }

        item.setName(updatedItem.getName());
        item.setDescription(updatedItem.getDescription());
        item.setAvailable(updatedItem.getAvailable());

        return itemId;
    }

    @Override
    public boolean isItemExists(Long itemId) {
        return itemsData.containsKey(itemId);
    }

    @Override
    public void verifyItemExists(Long itemId) throws NotFoundException {
        final String errorMessage = "Вещь #" + itemId + " не найдена.";

        if (!isItemExists(itemId)) {
            log.error(errorMessage);
            throw new NotFoundException(errorMessage);
        }
    }
}