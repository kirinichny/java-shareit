package ru.practicum.shareit.item.storage;

import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemStorage {
    Item getItemById(Long itemId);

    List<Item> getItemsByOwnerId(Long ownerId);

    List<Item> searchItems(String searchText);

    Long createItem(Item item);

    Long updateItem(Item item);

    boolean isItemExists(Long itemId);

    void verifyItemExists(Long itemId);
}