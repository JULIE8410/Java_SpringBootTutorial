package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest  // 스프링부트 띄운 상태에서 테스트
@Transactional  //@Transactional 넣어서 테스트 실행, 테스트 끝나면 트랜젝션 롤백
public class MemberServiceTest {

    @Autowired MemberService memberService;
    @Autowired MemberRepository memberRepository;

//    @Autowired EntityManager em;

    @Test
//    @Rollback(false) // 롤백 안시키고 커밋시킴
    public void join() throws Exception {

        //Given
        Member member = new Member();
        member.setName("Julie");

        //When
        Long saveId = memberService.join(member);

        //Then
//        em.flush(); // DB 데이터 변경
        assertEquals(member, memberRepository.findOne(saveId)); // 같은 트랜잭션 안에서 PK 값 같으면 같은 영속성 컨텍스트
    }

    @Test(expected = IllegalStateException.class)
    public void duplicateMemberException () throws Exception {

        //Given
        Member member1 = new Member();
        member1.setName("Julie");

        Member member2 = new Member();
        member2.setName("Julie");

        //When
        memberService.join(member1);
        memberService.join(member1);

        //Then
        Assert.fail("Exception should be occurred");

    }
}
