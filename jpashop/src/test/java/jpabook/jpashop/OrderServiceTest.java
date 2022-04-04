package jpabook.jpashop;

import static org.junit.Assert.assertEquals;

import javax.persistence.EntityManager;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import jpabook.jpashop.Repository.OrderRepository;
import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Item;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.exception.NotEnoughStockException;
import jpabook.jpashop.service.OrderService;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional // 테스트케이스에 있을때 디폴트로 롤백
class OrderServiceTest {
	
	@Autowired EntityManager em;
	@Autowired OrderService orderService;
	@Autowired OrderRepository orderRepository;

	@Test
	public void 상품주문() throws Exception{
		Member member = createMember();
		
		Book book = createBook("시골 JPA", 10000, 10);
		
		int orderCount = 2;
		
		Long orderId = orderService.order(member.getId(), book.getId(), orderCount);
			
		Order order = orderRepository.findOne(orderId);
		
		assertEquals("상품주문시 상태는 ORDER", OrderStatus.ORDER, order.getStatus());
		assertEquals("주문한 상픔 종류수가 정확해야 한다.", 1, order.getOrderItems().size());
		assertEquals("주문 가격은 가격 * 수량이다.", 10000*orderCount, order.getTotalPrice());
		assertEquals("주문 수량만큼 재고가 줄어야 한다.", 8, book.getStockQuantity());
	}
	
	@Test
	public void 주문취소() throws Exception{
		Member member = createMember();
		Item item = createBook("시골 JPA", 10000, 10);
		
		int orderCount = 2;
		Long orderId = orderService.order(member.getId(), item.getId(), orderCount);
		orderService.cancelOrder(orderId);
		
		Order getOrder = orderRepository.findOne(orderId);
		
		assertEquals("주문 취소시 상태는 CANCEL", OrderStatus.CANCEL, getOrder.getStatus());
		assertEquals("주문이 취소된 상품은 재고가 증가해야한다.", 10, item.getStockQuantity());
	}
	
	@Test
	public void 재고수량초과() throws Exception{
		Member member = createMember();
		Item item = createBook("시골 JPA", 10000, 10);
		
		int orderCount = 11;
		
		try {
			orderService.order(member.getId(), item.getId(), orderCount);
		} catch (NotEnoughStockException e) {
			System.out.println("재고가 초과되었습니다.");
		}
		
	}
	
	public Member createMember() {
		Member member = new Member();
		member.setName("회원1");
		member.setAddress(new Address("서울", "강가", "123-121"));
		em.persist(member);
		return member;
	}
	
	public Book createBook(String name, int price, int quantity) {
		Book book = new Book();
		book.setName(name);
		book.setPrice(price);
		book.setStockQuantity(quantity);
		em.persist(book);
		return book;
	}
}
