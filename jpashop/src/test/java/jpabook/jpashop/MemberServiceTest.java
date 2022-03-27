package jpabook.jpashop;

import static org.junit.Assert.fail;
import static org.junit.jupiter.api.Assertions.assertEquals;

import javax.persistence.EntityManager;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import jpabook.jpashop.Repository.MemberRepository;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.service.MemberService;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional // 테스트케이스에 있을때 디폴트로 롤백
class MemberServiceTest {
	
	@Autowired MemberService memberService;
	@Autowired MemberRepository memberRepository;
	@Autowired EntityManager em;

	@Test
	public void 회원가입() throws Exception{
		
		Member member = new Member();
		member.setName("KIM");
		
		Long id = memberService.join(member);
		
		assertEquals(member, memberRepository.findOne(id));
	}
	
	@Test
	public void 중복_회원_예외() throws Exception{
		
		Member member1 = new Member();
		member1.setName("KIM");
		
		Member member2 = new Member();
		member2.setName("KIM");
		
		memberService.join(member1);
		try {
			memberService.join(member2); // 예외 발생
		} catch (IllegalStateException e) {
			return;
		}
		
		fail("예외가 발생해야 한다.");
	}
}
