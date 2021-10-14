package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service  // 컴포넌트 스캔대상 - 자동으로 스프링 빈으로 등록됨
@Transactional(readOnly = true)  // 데이터 변경시에는 @Transactional 안에서, readOnly = true 옵션주면 JPA 가 조회하는 곳에서 성능 최적화 시킴
@RequiredArgsConstructor //lombok 사용해서 생성자 주입, final 키워드 가진 것들 활용해서 생성자 만들어줌
public class MemberService {

    //final 키워드 추가해놓으면 컴파일 시점에 memberRepository 를 설정하지 않는 오류를 컴파일 시에 체크할 수 있음
    private final MemberRepository memberRepository;

    /*
    lombok @RequiredArgsConstructor 사용(생성자 별도 작성할 필요없음)
    @Autowired  //생성자 1개일때 생략가능
    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

     */

    /**
     * 회원가입
     */
    @Transactional   // 데이터 변경이 일어나는 메소드 이므로 옵션[readOnly = true] 없이 사용(default false)
    public Long join(Member member){
        validateDuplicateMember(member);
        memberRepository.save(member); // 향후 (실무) 동시성 문제 고려하여 해당 컬럼을 unique 제약 조건 두는 것 추천
        return member.getId();
    }

    private void validateDuplicateMember(Member member) {
        List<Member> findMembers = memberRepository.findByName(member.getName());
        if(!findMembers.isEmpty()){
            throw new IllegalStateException("Name already exists");
        }
    }

    /**
     * 전체 회원 조회
     */
    public List<Member> findMembers(){
        return memberRepository.findAll();
    }

    /**
     * 회원 조회
     */
    public Member findOne(Long memberId){
        return memberRepository.findOne(memberId);
    }
}
