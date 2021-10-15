package jpabook.jpashop.controller;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/members/new")
    public String createForm(Model model){
        // 컨트롤러에서 뷰로 넘어갈때 key-value 실어서 보냄, 현재는 MemberForm 은 빈 껍데기 멤버객체 가지고감(이유: validation 해줌)
        model.addAttribute("memberForm", new MemberForm());
        return "members/createMemberForm";
    }

    @PostMapping("/members/new")
    public String create(@Valid MemberForm form, BindingResult result){
        //@Valid: MemberForm 에 @NotEmpty 설정한 name 에 대해서 validation 할 수 있음

        // @Valid MemberForm form 뒤에 BindingResult result 를 파라미터로 적으면 만약 validation 에서 에러나는 경우
        // 그 에러를 가지고 result 에 저장한 채로 아래의 코드가 실행됨
        if(result.hasErrors()){
            return "members/createMemberForm";  // 회원가입 폼이 다시 보여지지만 name 관련된 에러메시지를 함께 보여줌
        }

        Address address = new Address(form.getCity(), form.getStreet(), form.getZipcode());

        Member member = new Member();
        member.setName(form.getName());
        member.setAddress(address);

        memberService.join(member);
        return "redirect:/";
    }

    @GetMapping("/members")
    public String list(Model model){

        // 나중에 생각해야 할 부분: API 를 만들 때 나중에 절때 Entity 를 넘기면 안됨! (Entity 에 로직 추가했는데 그것으로 인해서 API 스펙이 변할수있음)
        // 예) Member 에 field 를 추가하게 될 경우 API 스펙이 변경되는 문제 발생 (불완전해짐)
        //     Member 에 password field 를 추가했다고 가정할 경우, 비번 노출 우려
        // MemberDTO 사용하는 것 or 화면에 맞는 Data Transfer Object 사용 고려해 볼 것
        List<Member> members = memberService.findMembers();
        model.addAttribute("members", members);

        // model.addAttribute("members", memberService.findMembers());

        return "members/memberList";
    }
}
