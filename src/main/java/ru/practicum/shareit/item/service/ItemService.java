package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemService {
    Item getItemById(Long itemId);

    List<Item> getItemsByOwnerId(Long ownerId);

    List<Item> searchItems(String searchText);

    Item createItem(Item item, Long ownerId);

    Item updateItem(Item item, Long ownerId);
}