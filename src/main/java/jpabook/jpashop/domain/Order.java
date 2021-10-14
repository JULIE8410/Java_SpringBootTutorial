package jpabook.jpashop.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")  // 테이블명, 그렇지 않으면 Order 클래스명따라 관례적으로 Order 로 표시될수 있으므로
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order {

    @Id @GeneratedValue
    @Column(name = "order_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)  // Order 의 입장에서는 1개의 Member 를 가짐
    @JoinColumn(name = "member_id")     // 맵핑, 연관관계의 주인
    private Member member;  // 주문회원

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)  // CascadeType.ALL:  Order 를 persist 하게되면 OrderItem 도 같이 persist 됨
    private List<OrderItem> orderItems = new ArrayList<>();

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)  // Order persist 하게 되면 Delivery 도 persist 같이 됨
    @JoinColumn(name = "delivery_id")  // 연관관계 주인
    private Delivery delivery;  // 배송정보

    private LocalDateTime orderDate; // 주문시간, Java 8 에서 LocalDateTime 을 Hibernate 가 지원

    /*
     * SpringPhysicalNamingStrategy 에 따라서
        1. camel case -> underscore(orderDate -> order_date)
        2. .(점) -> _(언더스코어)
        3. 대문자 -> 소문자
     */

    @Enumerated(EnumType.STRING)
    private OrderStatus status;  // 주문상태[ORDER, CANCEL]

    //연관관계 메서드 - 양방향일때 사용하면 좋음
    public void setMember(Member member){
        this.member = member;
        member.getOrders().add(this);  // Member 테이블과 연관관계 묶어버림
    }

    public void addOrderItem(OrderItem orderItem){
        orderItems.add(orderItem);
        orderItem.setOrder(this);
    }

    public void setDelivery(Delivery delivery){
        this.delivery = delivery;
        delivery.setOrder(this);

    }

    // 주문엔티티 생성할 때 사용, 주문회원, 배송정보, 주문상품들(...) 의 정보를 받아서 실제 주문 엔티티를 생성
    public static Order createOrder(Member member, Delivery delivery, OrderItem... orderItems){

        Order order = new Order();
        order.setMember(member);
        order.setDelivery(delivery);

        for(OrderItem orderItem : orderItems){
            order.addOrderItem(orderItem);  // 리스트에 추가
        }

        order.setStatus(OrderStatus.ORDER);       // 주문 상태 변경
        order.setOrderDate(LocalDateTime.now());

        return order;
    }

    // 비즈니스 로직

    /**
     * 주문취소
     */
    public void cancel() {
        // 주문상태를 취소로 변경하고, 주문상품에 주문취소를 알림

        if (delivery.getStatus() == DeliveryStatus.COMP) {
            throw new IllegalStateException("Products that have already been delivered cannot be canceled.");
        }

        this.setStatus(OrderStatus.CANCEL);

        // 루프를 돌면서 주문한 아이템들 하나씩 원복시킴
        for (OrderItem orderItem : orderItems) {
            orderItem.cancel();
        }


    }

    // 조회 로직
    /**
     * 전체 주문 가격 조회
     */
    public int getTotalPrice(){
        int totalPrice = 0;

        for(OrderItem orderItem : orderItems){
            totalPrice += orderItem.getTotalPrice();
        }
        return totalPrice;

        // return orderItems.stream().mapToInt(OrderItem::getTotalPrice).sum();
    }




}
