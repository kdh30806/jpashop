package jpabook.jpashop;

import javax.transaction.Transactional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
class MemberRepositoryTest {
	
	@Autowired MemberRepository memberRepository;

	@Test
	@Transactional
	@Rollback(false)
	void contextLoads() {
		Member member = new Member();
		member.setUsername("MEMBERA");
		
		Long saveId = memberRepository.save(member);
		
		Member findMember = memberRepository.find(saveId);
		
		Assertions.assertThat(findMember.getId()).isEqualTo(member.getId());
		Assertions.assertThat(findMember).isEqualTo(member);
	}

}
