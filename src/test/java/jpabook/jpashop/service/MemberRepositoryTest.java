package jpabook.jpashop.service;

import com.fasterxml.jackson.databind.deser.std.StdKeyDeserializer;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;

import static org.assertj.core.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;

    @Test
    @Transactional  // EntityManager 를 통한 모든 데이터 변경은 Transactional 안에서 이뤄져야 함, @Transactional 어노테이션이 테스트 케이스 안에 있으면 테스트 실행 후 롤백
//    @Rollback(false)
    public void testMember(){
        Member member = new Member();
        member.setName("Julie");
//        Long saveId = memberRepository.save(member);
        memberRepository.save(member);
        Member findMember = memberRepository.findOne(1L);

        assertThat(findMember.getId()).isEqualTo(member.getId());

        assertThat(findMember.getName()).isEqualTo(member.getName());

        assertThat(findMember).isEqualTo(member);  // JPA 엔티티 동일성 보장

        System.out.println("findMember == member: " + (findMember == member) );
    }



}
