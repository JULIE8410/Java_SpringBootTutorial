package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)  // 트랜잭션, 영속성 컨테스트, 이거는 service 랑 어울리는 것인가?
@RequiredArgsConstructor //lombok 사용해서 생성자 주입 설정대신에 간편하게 할 수 있음
public class MemberService {

    //final 키워드 추가해놓으면 컴파일 시점에 memberRepository 를 설정하지 않는 오류를 체크할 수 있음
    private final MemberRepository memberRepository;

    /* lombok @RequiredArgsConstructor 사용해서 생성자 작성할 필요없음.
    @Autowired  //생성자 1개일때 생략가능함,
    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

     */

    /**
     * 회원가입
     */
    @Transactional //변경
    public Long join(Member member){
        validateDuplicateMember(member);  // 중복회원검증
        memberRepository.save(member);  //save 될 때 db 에서 자동으로 번호를 받아서 id에 저장됨?
        return member.getId();  //그래서 여기서 id 를 불러올수 있는건가?
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
}
