package jpabook.jpashop.repository;

import jpabook.jpashop.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository  //@Repository: 컴포넌트 스캔에 의해 스프링빈으로 등록, JPA 예외를 스프링 기반 예외로 예외 변환
@RequiredArgsConstructor
public class MemberRepository {

    //@PersistenceContext    스프링이 EntityManager 생성해서 주입
    //private EntityManager em;

    // @RequiredArgsConstructor 선언해서 생성자 주입
    private final EntityManager em;

    public void save(Member member){
        em.persist(member);  // 영속성 컨텍스트에 값을 넣음, key-value 에서 key 에는 id 값을 넣음
    }

    public Member findOne(Long id){
        // 단건조회, 파라미터 (타입, PK)
        return em.find(Member.class, id);
    }

    public List<Member> findAll(){
        // Jakarta Persistence Query Language
        // 파라미터 (Jpql, 반환타입)  *Jpql 은 엔티티 객체를 대상으로 쿼리 <-> SQL 은 테이블을 대상으로 쿼리
        return em.createQuery("select m from Member m", Member.class)
                .getResultList();
    }

    public List<Member> findByName(String name){
        //where 절에서 m.name 은 Member 클래스 내에 있는 name data field
        return em.createQuery("select m from Member m where m.name = :name", Member.class)
                .setParameter("name", name)
                .getResultList();
    }
}
