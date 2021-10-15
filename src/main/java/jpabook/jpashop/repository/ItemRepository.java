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

        // id 가 없으면 신규로 보고 persist() 진행 (JPA 에 저장하기 전까지는 Id 없음)
        // id 가 있으면 이미 JPA 를 통해서 DB에 저장된 적 있음, 따라서 엔티티를 수정한다고 보고 merge() 실행 (Update 의 느낌)
        if(item.getId() == null){
            // item 신규 등록
            em.persist(item);

        }else{
            // item 업데이트
            em.merge(item);
            // 주의!! 변경감지 기능을 사용하면 원하는 속성만 선택해서 변경할 수 있지만,
            //        병합을 사용하면 모든 속성이 변경됨! 병합시 값이 없으면 null 로 업데이트 될 위험 있음 (병합은 모든 필드를 교체)
            // merge 보다는 변경감지 기능 사용을 권장함

        }

    }

    public Item findOne(Long id){
        return em.find(Item.class, id);
    }

    public List<Item> findAll(){
        // 여러건 조회의 경우에는 Jpql 작성해야 함
        return em.createQuery("select i from Item i", Item.class)
                .getResultList();
    }

}
