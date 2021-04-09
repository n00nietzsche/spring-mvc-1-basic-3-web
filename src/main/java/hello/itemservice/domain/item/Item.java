package hello.itemservice.domain.item;

import lombok.Getter;
import lombok.Setter;

// @Data 는 @Getter @Setter @RequiredArgsConstructor @ToString @EqualsAndHashCode.를 다 만들어주기 때문에 위험하다.
@Getter @Setter
public class Item {

    private Long id;
    private String itemName;
    private Integer price;
    private Integer quantity;

    public Item() {
    }

    public Item(String itemName, Integer price, Integer quantity) {
        this.itemName = itemName;
        this.price = price;
        this.quantity = quantity;
    }
}
