package jpabook.jpashop.domain.item;



import jpabook.jpashop.domain.Category;
import jpabook.jpashop.exception.NotEnoughStockException;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "dtype")  // 타입 구분
@Getter @Setter
public abstract class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ITEM_SEQ_GENERATOR")
    @Column(name = "item_id")
    private Long id;

    private String name;

    private int price;
    private int stockQuantity;

    @ManyToMany(mappedBy = "items")  // Category 테이블의 items
    private List<Category> categories = new ArrayList<>();

    // 비즈니스 로직
    // stockQuantity 를 Item 엔티티가 가지고 있으므로 여기에 비즈니스 로직을 넣는 것이 좋음(관리 수월)
    /**
     * stock 증가
     */
    public void addStock(int quantity){
        // 재고가 증가하거나 상품주문을 취소해서 재고를 다시 늘려야 할때 사용됨
        this.stockQuantity += quantity;
    }

    /**
     * stock 감소
     */
    public void removeStock(int quantity){

        // 재고가 부족하면 예외가 발생함, 주로 상품을 주문할때 사용함
        int restStock = this.stockQuantity - quantity;

        if(restStock < 0){
            throw new NotEnoughStockException("Need more stock");
        }

        this.stockQuantity = restStock;
    }

}
