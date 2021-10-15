package jpabook.jpashop.controller;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter @Setter
public class MemberForm {

    @NotEmpty(message = "Name is required")
    private String name;   // 이름은 필수항목

    private String city;
    private String street;
    private String zipcode;

}
