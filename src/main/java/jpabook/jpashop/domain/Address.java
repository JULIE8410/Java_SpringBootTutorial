package jpabook.jpashop.domain;

import lombok.Getter;

import javax.persistence.Embeddable;

@Embeddable
@Getter
public class Address {

    //@Setter 없음, 생성자에서 값을 모두 초기화하고, 이후에는 변경이 불가능함

    private String city;
    private String street;
    private String zipcode;

    protected Address(){
        // JPA 스펙상 엔티티나 임베디드 타입은 자바 기본 생성자를 public 또는 protected 로 설정
        // protected 가 더 안전 => JPA 구현 라이브러리가 객체 생성할때 리플랙션 같은 기술을 사용할 수 있도록 지원해야 하기 때문문
    }

    public Address(String city, String street, String zipcode) {
        this.city = city;
        this.street = street;
        this.zipcode = zipcode;
    }
}
