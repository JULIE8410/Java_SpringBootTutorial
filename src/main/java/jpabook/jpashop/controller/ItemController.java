package jpabook.jpashop.controller;

import jpabook.jpashop.domain.item.Album;
import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Locale;

@Controller
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @GetMapping("/items/new")
    public String createForm(Model model){   // 상품 등록 관련 폼 제공 목적
        model.addAttribute("form", new BookForm());
        return "items/createItemForm";
    }

    @PostMapping("/items/new")
    public String create(BookForm form){  // Post 통해서 넘어올때는 작성한 form 이 함께 전송

        // Order 에서 했던 것처럼 setter 들을 날릴 수 있는 방법으로 코드 수정해보기
        Book book = new Book();
        book.setName(form.getName());
        book.setPrice(form.getPrice());
        book.setStockQuantity(form.getStockQuantity());
        book.setAuthor(form.getAuthor());
        book.setIsbn(form.getIsbn());
        itemService.saveItem(book);

        return "redirect:/items";
    }

    @GetMapping("/items")
    public String list(Model model){
        List<Item> items = itemService.findItems();
        model.addAttribute("items", items);
        return "items/itemList";
    }

    @GetMapping("/items/{itemId}/edit")
    public String updateItemForm(@PathVariable("itemId") Long itemId, Model model){

        // 클릭한 아이템 id 가 넘어옴, id 를 가지고 해당되는 item 을 찾아옴
        Book item = (Book) itemService.findOne(itemId);

        // 기존의 item 을 수정해야 하는 경우이기 때문에, BookForm 에 기존 자료 넣어줌
        BookForm form = new BookForm();
        form.setId(item.getId());
        form.setName(item.getName());
        form.setPrice(item.getPrice());
        form.setStockQuantity(item.getStockQuantity());
        form.setAuthor(item.getAuthor());
        form.setIsbn(item.getIsbn());

        model.addAttribute("form", form);

        return "items/updateItemForm";   // 수정대상 상품 조회 결과를 모델 객체에 담아서 뷰(items/updateItemForm) 에 전달
    }

    @PostMapping("/items/{itemId}/edit")
    public String updateItem(@ModelAttribute("form") BookForm form, @PathVariable String itemId){ //@ModelAttribute("form"): updateItemForm 에서 넘어올때의 object 이름

        Book book = new Book();

        // form 에서 넘어온 데이터를 book 에 조립
        book.setId(form.getId());
        book.setName(form.getName());
        book.setPrice(form.getPrice());
        book.setStockQuantity(form.getStockQuantity());
        book.setAuthor(form.getAuthor());
        book.setIsbn(form.getIsbn());

        itemService.saveItem(book);

        // 생각해 볼 부분: url 에 itemId 가 조작되서 넘어올 수도 있다는 점 (다른 사람 데이터 수정될 수 있음) 취약점
        // 서비스 계층(뒷단) 이던 앞단이던 요청하는 유저가 해당요청에 대한 권한이 있는지 체크해보는 로직 필요할 수 있음
        // 혹은 업데이트 할 객체를 세션에 담아놓고 풀어내는 방법도 있을 듯 (요즘에는 세션객체는 잘 쓰지않음)

        return "redirect:/items";

    }




}
