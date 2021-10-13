package jpabook.jpashop.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class Member {

    @Id @GeneratedValue
    private Long id;

    private String name;

    @Embedded // Specifies a persistent field or property of an entity whose value is an instance of an embeddable class.
    private Address address;  //??

    @OneToMany
    private List<Order> orders = new ArrayList<>();



}
