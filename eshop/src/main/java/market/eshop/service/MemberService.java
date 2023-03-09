package market.eshop.service;

import lombok.RequiredArgsConstructor;
import market.eshop.domain.Member;
import market.eshop.domain.form.LoginForm;
import market.eshop.repository.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final CartService cartService;

    public Long join(Member member) {
        validateDuplicateMember(member);
        Member saveMember = memberRepository.save(member);
        cartService.createCart(saveMember.getId());
        return member.getId();
    }

    private void validateDuplicateMember(Member member) {
        List<Member> result =
                memberRepository.findByEmail(member.getEmail());

        if(!result.isEmpty()) {
            throw new IllegalStateException("중복된 이메일입니다.");
        }
    }

    public List<Member> login(LoginForm form) {
        List<Member> result = memberRepository.findByEmailAndPassword(form);
        if(result.isEmpty()) {
            throw new IllegalStateException("아이디나 비밀번호를 확인해주세요.");
        }

        return result;
    }

    public Member findMember(Long orderId) {
        return memberRepository.findById(orderId).get();
    }
}
