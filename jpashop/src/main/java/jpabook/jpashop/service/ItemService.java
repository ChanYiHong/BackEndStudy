package jpabook.jpashop.service;

import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;

    // read only로 하면 안됨
    @Transactional
    public void saveItem(Item item){
        itemRepository.save(item);
    }

    @Transactional                      // 준영속 상태의 파라미터 엔티티.
    public void updateItem(Long itemId, String name, int price, int stockQuantity){
        // 영속 상태의 entity. 변경감지가 이루어짐.
        Item findItem = itemRepository.findOne(itemId);
        findItem.setPrice(price);
        findItem.setName(name);
        findItem.setStockQuantity(stockQuantity);

    }

    // 상품 여러개 찾기.
    public List<Item> findItems() {
        return itemRepository.findAll();
    }

    // 상품 한개 찾기.
    public Item findOne(Long itemId) {
        return itemRepository.findOne(itemId);
    }

}
