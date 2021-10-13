package jpabook.jpashop.domain.item;


import jpabook.jpashop.domain.Category;
import jpabook.jpashop.exception.NotEnoughStockException;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)  // InheritanceType.SINGLE_TABLE: A single table per class hierarchy
@DiscriminatorColumn(name = "dtype")  // 뜻 공부하기, 이거 사용하는 이유?
@Getter @Setter
public abstract class Item {

    @Id @GeneratedValue
    @Column(name = "item_id")
    private Long id;

    private String name;

    private int price;
    private int stockQuantity;

    @ManyToMany(mappedBy = "items")
    private List<Category> categories = new ArrayList<>();

    // 비즈니스 로직
    public void addStock(int quantity){
        // 파라미터로 넘어온 수만큼 재고를 늘림,
        // 재고가 증가하거나 상품주문을 취소해서 재고를 다시 늘려야 할때 사용됨
        this.stockQuantity = quantity;
    }

    public void removeStock(int quantity){

        // 파라미터로 넘어온 수만큼 재고를 줄임
        // 재고가 부족하면 예외가 발생함, 주로 상품을 주문할때 사용함
         int restStock = this.stockQuantity - quantity;
         if(restStock < 0){
             throw new NotEnoughStockException("Need more stock");
         }
         this.stockQuantity = restStock;
    }

}
