package jpabook.jpashop.api;

import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderItem;
import jpabook.jpashop.repository.OrderRepository;
import jpabook.jpashop.repository.OrderSearch;
import jpabook.jpashop.vo.OrderDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class OrderApiController {
    
    private final OrderRepository orderRepository;
    
    @GetMapping("/api/v1/orders")
    public List<Order> odersV1(){
        List<Order> all = orderRepository.findAllByString(new OrderSearch());

        // intelliJ에서 제공해주는 자동완성기능을 활용합시다.  -> iter
        for (Order order : all) {
            order.getMember().getName();
            order.getDelivery().getAddress();
            List<OrderItem> orderItems = order.getOrderItems();
            orderItems.stream().forEach(o -> o.getItems().getName());
        }
        
        return all;
    }

    @GetMapping("/api/v2/orders")
    public List<OrderDto> odersV2(){
        List<Order> orders = orderRepository.findAllByString(new OrderSearch());
        List<OrderDto> collect = orders.stream()
                .map(o -> new OrderDto(o))
                .collect(Collectors.toList());

        return  collect;

    }


    // v2와 사실 코드가 바뀐게 없지만 repositoty의 JPQL 메소드를 어떻게 구현하느냐에 따라 데이터베이스에 전송되는 쿼리수를 줄이므로써 성능을 높일 수 있습니다.
    @GetMapping("/api/v3/orders")
    public List<OrderDto> odersV3() {
        List<Order> orders = orderRepository.findAllWithItem();
        List<OrderDto> collect = orders.stream()
                .map(o -> new OrderDto(o))
                .collect(Collectors.toList());

        return  collect;
    }

    
}
