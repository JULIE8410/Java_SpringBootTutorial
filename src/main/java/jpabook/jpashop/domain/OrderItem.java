package jpabook.jpashop.domain;

import jpabook.jpashop.domain.item.Item;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "order_item")
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)  //protected default 생성자
public class OrderItem {

    @Id @GeneratedValue
    @Column(name = "order_item_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)  // 지연로딩
    @JoinColumn(name = "item_id")
    private Item item; // 주문상품

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order; // 주문

    private int orderPrice; // 주문가격

    private int count; // 주문수량

    // protected OrderItem() {}

    // 생성메서드
    // 주문상품, 가격, 수량정보를 사용해서 주문상품 엔티티를 생성
    public static OrderItem createOrderItem(Item item, int orderPrice, int count){
        OrderItem orderItem = new OrderItem();
        orderItem.setItem(item);
        orderItem.setOrderPrice(orderPrice);
        orderItem.setCount(count);

        item.removeStock(count);  // 주문 생성 후 item 에 주문한 수량만큼 상품의 재고를 줄임

        return orderItem;
    }


    // 비즈니스 로직
    /**
     * 주문취소
     */
    public void cancel() {
        // 아이템을 가져와서 취소 주문한 수량만큼 상품의 재고를 증가시킴
        getItem().addStock(count);
    }

    // 조회 로직
    /**
     * 주문상품 전체 가격 조회
     */
    public int getTotalPrice() {
        return getOrderPrice() * getCount();  // 주문가격에 수량을 곱한 값을 반환
    }
}
