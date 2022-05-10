package jpabook.jpashop.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jpabook.jpashop.Repository.MemberRepository;
import jpabook.jpashop.domain.Member;
import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true) // 읽기 전용을 명시해주면 jpa가 최적화 해줌.
@RequiredArgsConstructor // final 잡은 레포지토리를 생성자로 생성해줌.
public class MemberService {

	private final MemberRepository memberRepository;

	/** 
	 * 회원 가입
	 * @param member
	 * @return member_id
	 */
	@Transactional // 디폴트가 readOnly가 false임.
	public Long join(Member member) {
		
		validateDuplicateMember(member); // 중복 회원 검증
		memberRepository.save(member);
		return member.getId();
	}

	/**
	 * 회원 중복 체크
	 * @param member
	 */
	private void validateDuplicateMember(Member member) {
		List<Member> findMembers = memberRepository.findByName(member.getName()); // 최후의 방어로 unique 제약조건을 해야함.
		if(!findMembers.isEmpty()) {
			throw new IllegalStateException("이미 존재하는 회원입니다.");
		}
	}
	
	/**
	 * 회원 전체 조회
	 * @return List<Member>
	 */
	public List<Member> findMembers(){
		return memberRepository.findAll();
	}
	
	/**
	 * 회원 조회
	 * @param id
	 * @return member
	 */
	public Member findOne(Long id) {
		return memberRepository.findOne(id);
	}

	@Transactional
	public void update(Long id, String name) {
		Member member = memberRepository.findOne(id);
		member.setName(name);
	}
}
