package ru.practicum.shareit.item.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.itemRequest.model.ItemRequest;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {
    List<Item> findItemsByOwnerIdOrderById(Long itemId, Pageable pageable);

    List<Item> findByRequestIn(List<ItemRequest> itemRequests);

    List<Item> findAllByRequestId(Long itemRequestId);

    @Query(" SELECT i FROM Item i " +
            "WHERE (UPPER(i.name) LIKE UPPER(CONCAT('%', ?1, '%')) " +
            " OR UPPER(i.description) LIKE UPPER(CONCAT('%', ?1, '%'))) " +
            " AND i.available = true")
    List<Item> search(String searchText, Pageable pageable);
}