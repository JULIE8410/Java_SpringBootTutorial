package jpabook.jpashop.service;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.exception.NotEnoughStockException;
import jpabook.jpashop.repository.OrderRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.junit.Assert.*;


@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class OrderServiceTest {

    @Autowired EntityManager em;
    @Autowired OrderService orderService;
    @Autowired OrderRepository orderRepository;

    @Test
    public void orderItemTest () throws Exception {

        //Given
        Member member = createMember();

        Item book = createBook("Spring Boot with JPA", 10000, 10);


        int orderCount = 2;

        //When
        Long orderId = orderService.order(member.getId(), book.getId(), orderCount);

        //Then
        Order getOrder = orderRepository.findOne(orderId);

        // import static org.junit.Assert.*;
        assertEquals("When ordering products, the status is ORDER", OrderStatus.ORDER, getOrder.getStatus());
        assertEquals("The number of products you ordered must be exact", 1, getOrder.getOrderItems().size());
        assertEquals("Order price is price * quantity", 10000*2, getOrder.getTotalPrice());
        assertEquals("Stock should be reduced by the amount ordered", 8, book.getStockQuantity());

    }



    @Test(expected = NotEnoughStockException.class)
    public void orderItem_overStockQuantityTest () throws Exception {

        //Given
        Member member = createMember();
        Item item = createBook("Spring Boot with JPA", 10000, 10);

        int orderCount = 11;

        //When
        orderService.order(member.getId(), item.getId(), 11 );

        //Then
        fail("Should throw Exception - NotEnoughStockException");

    }

    @Test
    public void cancelItemTest () throws Exception {

        //Given
        Member member = createMember();
        Item item = createBook("Spring Boot with JPA", 10000, 10);

        int orderCount = 2;

        Long orderId = orderService.order(member.getId(), item.getId(), orderCount);

        //When
        orderService.cancelOrder(orderId);

        //Then
        Order getOrder = orderRepository.findOne(orderId);
        assertEquals("Order status is CANCEL when you cancel order", OrderStatus.CANCEL, getOrder.getStatus());
        assertEquals("Products whose orders have been cancelled must increase in stock by the number of canceled items", 10, item.getStockQuantity());




    }

    private Item createBook(String name, int price, int stockQuantity) {
        Item item = new Book();
        item.setName(name);
        item.setPrice(price);
        item.setStockQuantity(stockQuantity);
        em.persist(item);
        return item;
    }

    private Member createMember() {
        Member member = new Member();
        member.setName("Julie");
        member.setAddress(new Address("Vancouver", "14th Ave", "V3N 2A9"));
        em.persist(member);
        return member;
    }
}
