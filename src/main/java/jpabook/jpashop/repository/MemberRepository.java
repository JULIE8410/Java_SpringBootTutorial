package jpabook.jpashop.repository;

import jpabook.jpashop.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository  //@Repository: 스프링빈으로 등록, JPA 예외를 스프링 기반 예외로 예외 변환
@RequiredArgsConstructor
public class MemberRepository {

    //@PersistenceContext    EntityManager 주입
    //private EntityManager em;

    // @RequiredArgsConstructor 선언
    private final EntityManager em;

    public void save(Member member){
        em.persist(member);
    }

    public Member findOne(Long id){
        return em.find(Member.class, id);
    }

    public List<Member> findAll(){
        //Jakarta Persistence Query Language, 결과클래스
        return em.createQuery("select m from Member m", Member.class)
                 .getResultList();  //쿼리결과 return
    }

    public List<Member> findByName(String name){
        //where 절에서 m.name 은 Member 클래스 내에 있는 name data field 랑 같아야 함(이름 다르면 오류 남)
        return em.createQuery("select m from Member m where m.name = :name", Member.class)
                .setParameter("name", name)
                .getResultList();
    }
}
