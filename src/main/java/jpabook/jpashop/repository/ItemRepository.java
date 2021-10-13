package jpabook.jpashop.repository;

import jpabook.jpashop.domain.item.Item;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ItemRepository {

    private final EntityManager em;

    public void save(Item item){
        // id 가 있으면 신규로 보고 persist() 진행
        // id 가 없으면 이미 DB에 저장된 엔티티를 수정한다고 보고 merge() 실행
        if(item.getId() == null){
            em.persist(item);
        }else{
            em.merge(item);
        }

    }

    // 1개 찾는거는 쿼리 안써도 됨?
    public Item findOne(Long id){
        return em.find(Item.class, id);
    }

    public List<Item> findAll(){
        return em.createQuery("select i from Item i", Item.class)
                .getResultList();
    }

}
