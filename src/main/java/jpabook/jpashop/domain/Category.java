package jpabook.jpashop.domain;

import jpabook.jpashop.domain.item.Item;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class Category {

    @Id @GeneratedValue
    @Column(name = "category_id")
    private Long id;

    private String name;

    @ManyToMany
    @JoinTable(name = "category_item",  //테이블 자동생성함?
               joinColumns = @JoinColumn(name = "category_id"),
               inverseJoinColumns = @JoinColumn(name = "item_id"))  //ITEM 테이블 명시안해줘도 됨?
    private List<Item> items = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)  // 이 부분은 왜 연결하는지 잘 모르겠음, OneToMany 되야한다고 생각했는데...
    @JoinColumn(name = "parent_id")
    private Category parent;  // Category 내에 Category 가지는 이유

    @OneToMany(mappedBy = "parent")
    private List<Category> child = new ArrayList<>();


    // 연관관계 메서드
    public void addChildCategory(Category child){
        this.child.add(child);
        child.setParent(this);
    }



}
