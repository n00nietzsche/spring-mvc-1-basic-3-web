package hello.itemservice.domain.item;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository // 컴포넌트 스캔의 대상으로 만들어줌
public class ItemRepository {

    // static 사용한 것에 주의
    // 실제 환경에서는 MultiThread 환경에서 HashMap을 쓰지말고, ConcurrentHashMap 을 써야 한다.
    private static final Map<Long, Item> store = new HashMap<>();
    // Long 도 실무에서는 atomic 등을 써야 함
    private static long sequence = 0L;

    public Item save(Item item) {
        item.setId(++sequence);
        store.put(item.getId(), item);
        return item;
    }

    public Item findById(Long id) {
        return store.get(id);
    }

    public List<Item> findAll() {
        // 감싸서 반환하는 이유는 다른 개발자가 store에 무언가 추가해도 안전하도록 감싸놓은 것
        return new ArrayList<>(store.values());
    }

    public void update(Long itemId, Item updateParam) {
        // 사실 정석으로 하려면 Dto 등의 객체를 만들고 바뀌는 것들만 넣어놓는 것이 맞음
        // 중복 vs 명확성에서는 항상 명확성을 따르는 게 낫다
        Item findItem = findById(itemId);
        findItem.setItemName(updateParam.getItemName());
        findItem.setPrice(updateParam.getPrice());
        findItem.setQuantity(updateParam.getQuantity());
    }

    public void clearStore() {
        // hashMap 데이터 날리기
        store.clear();
    }
}
