package jpabook.jpashop.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "MEMBER_SEQ_GENERATOR")
    @Column(name = "member_id")
    private Long id;

    private String name;

    @Embedded // 내장타입,Specifies a persistent field or property of an entity whose value is an instance of an embeddable class.
    private Address address;

    // Member 는 여러개의 Order 를 가질 수 있음
    @OneToMany(mappedBy = "member")  //Order 테이블의 member 에 맵핑되었음
    private List<Order> orders = new ArrayList<>();  // 컬렉션은 필드에서 초기화 하는 것이 안전, null 문제에서 안전,

    // hibernate 는 엔티티를 영속화 할 때  컬렉션을 감싸서 hibernate 가 제공하는 내장 컬렉션으로 변경




}
