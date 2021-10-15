package jpabook.jpashop.service;

import jpabook.jpashop.domain.*;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.repository.ItemRepository;
import jpabook.jpashop.repository.MemberRepository;
import jpabook.jpashop.repository.OrderRepository;
import jpabook.jpashop.repository.OrderSearch;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderService {

    private final MemberRepository memberRepository;
    private final OrderRepository orderRepository;
    private final ItemRepository itemRepository;


    // 서비스 계층은 단순히 엔티티에 필요한 요청을 위임하고 있음 (도메인 모델 패턴)  <-> 트랜잭션 스크립트 패턴


    // 주문
    @Transactional
    public Long order(Long memberId, Long itemId, int count){

        // controller 에서는 식별자 들만 넘기고, Transaction 이 일어나는 service 계층에서 엔티티 접근하는게 바람직할 것
        // 핵심비즈니스 로직을 service 계층에서 하게 되면, 트랜잭션 내에서 엔티티 다루면 영속성 관련 처리가 가능함
        // 트랜잭션 없이

        // 엔티티 조회
        Member member = memberRepository.findOne(memberId);
        Item item = itemRepository.findOne(itemId);

        // 배송정보 생성
        Delivery delivery = new Delivery();
        delivery.setAddress(member.getAddress());
        delivery.setStatus(DeliveryStatus.READY);

        // 주문상품 생성
        OrderItem orderItem = OrderItem.createOrderItem(item, item.getPrice(), count);

        // 주문생성
        Order order = Order.createOrder(member, delivery, orderItem);

        // 주문 저장
        orderRepository.save(order);
        // Order 클래스내에 OrderItem 과 Delivery CascadeType.ALL 설정해 놓음 - order persist 될때 다 같이 됨
        return order.getId();
    }

    // 주문 취소
    @Transactional
    public void cancelOrder(Long orderId){

        // 주문엔티티 조회
        Order order = orderRepository.findOne(orderId);

        // 주문 취소
        order.cancel();

        // ** JPA 는 dirty checking 통해서 스스로 데이터 변경내역을 감지하고 변경내역을 DB 에 업데이트(업데이트 쿼리 날림)

    }

    // 검색
    public List<Order> findOrders(OrderSearch orderSearch){
        return orderRepository.findAll(orderSearch);
    }

}
