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
public class ItemService {  //상품 서비스는 상품 리포지토리에 단순히 위임만 하는 클래스, 테스트작성해보기

    private final ItemRepository itemRepository;

    @Transactional
    public void saveItem(Item item){
        itemRepository.save(item);
    }

    @Transactional    // merge()랑 동일하게 작용함
    public void updateItem(Long itemId, String name, int price, int stockQuantity){
        // itemId 를 통해서 repository 에서 해당하는 Item 찾아옴 - 이것은 영속상태의 것을 가지고 있음
        // 따라서 트랜잭션이 끝나고 나면 flush 후 변경상태 감지하고 자동으로 DB 업데이트 (변경감지기능 사용)
        Item findItem = itemRepository.findOne(itemId);


        // 아래와 같이 setter 를 사용하는 방법보다는 change(, , , ) 같은 메소드를 이용하여 의미있는 메소드를 활용하는 것 추천
//        findItem.change(param.getPrice(), param.getName(), param.getStockQuantity());

        findItem.setName(name);
        findItem.setPrice(price);
        findItem.setStockQuantity(stockQuantity);  // 예를 들어서 3개만 수정한다고 가정

    }

    public List<Item> findItems(){
        return itemRepository.findAll();
    }

    public Item findOne(Long itemId){
        return itemRepository.findOne(itemId);
    }
}
