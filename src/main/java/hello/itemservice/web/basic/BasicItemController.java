package hello.itemservice.web.basic;

import hello.itemservice.domain.item.Item;
import hello.itemservice.domain.item.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.PostConstruct;
import java.util.List;

@Controller
@RequestMapping("/basic/items")
@RequiredArgsConstructor
public class BasicItemController {
    private final ItemRepository itemRepository;

    // lombok의 `@RequiredArgsConstructor` 애노테이션 때문에 자동으로 생기므로 딱히 작성해줄 필요 없다.
    /*
    @Autowired
    public BasicItemController(ItemRepository itemRepository) {
        // `itemRepository`는 `@Repository`로 스프링 컨테이너에 등록되어 있기 때문에, 자동으로 주입될 수 있다.
        this.itemRepository = itemRepository;
    }
     */

    @GetMapping
    public String items(Model model) {
        List<Item> items = itemRepository.findAll();
        model.addAttribute("items", items);
        return "basic/items";
    }

    // 테스트용 데이터 추가
    @PostConstruct
    public void init() {
        itemRepository.save(new Item("itemA", 10000, 10));
        itemRepository.save(new Item("itemB", 20000, 20));
    }

    @GetMapping("/{itemId}")
    public String item(@PathVariable Long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "basic/item";
    }

    @GetMapping("/add")
    public String addForm() {
        return "basic/addForm";
    }

    // 같은 URL이지만 HTTP 메소드로 기능을 구분해주기
    // @PostMapping("/add")
    public String addItemV1(
            @RequestParam String itemName,
            @RequestParam Integer price,
            @RequestParam Integer quantity,
            Model model
    ) {
        Item item = new Item();

        item.setItemName(itemName);
        item.setPrice(price);
        item.setQuantity(quantity);

        itemRepository.save(item);

        model.addAttribute("item", item);

        // 기존 상품 상세 페이지 활용
        return "basic/item";
    }

    // 같은 URL이지만 HTTP 메소드로 기능을 구분해주기
    // @PostMapping("/add")
    public String addItemV2(@ModelAttribute("item") Item item) { // 파라미터로 Model도 받을 필요 없어짐
        itemRepository.save(item);
        // model.addAttribute("item", item); // 자동 추가 생략 가능

        // 기존 상품 상세 페이지 활용
        return "basic/item";
    }

    // @PostMapping("/add")
    public String addItemV3(@ModelAttribute Item item) {
        // `name`을 지우면, `Item` 이라는 클래스명의 첫 글자만 소문자로 바꾼 것이 `name`이 된다.
        itemRepository.save(item);

        // 기존 상품 상세 페이지 활용
        return "basic/item";
    }

    // @PostMapping("/add")
    public String addItemV4(Item item) {
        itemRepository.save(item);
        return "basic/item";
    }

    // @PostMapping("/add")
    public String addItemV5(Item item) {
        itemRepository.save(item);
        return "redirect:/basic/items/" + item.getId();
    }

    @PostMapping("/add")
    public String addItemV6(Item item, RedirectAttributes redirectAttributes) {
        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);

        return "redirect:/basic/items/{itemId}";
    }

    @GetMapping("/{itemId}/edit")
    public String editForm(@PathVariable Long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "basic/editForm";
    }

    @PostMapping("/{itemId}/edit")
    public String edit(@PathVariable Long itemId, @ModelAttribute Item item) {
        itemRepository.update(itemId, item);

        return "redirect:/basic/items/{itemId}";
    }
}
