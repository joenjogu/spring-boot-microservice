package org.joenjogu.bookstore.orderservice.testdata;

import static org.instancio.Select.field;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import org.instancio.Instancio;
import org.joenjogu.bookstore.orderservice.domain.model.Address;
import org.joenjogu.bookstore.orderservice.domain.model.CreateOrderRequest;
import org.joenjogu.bookstore.orderservice.domain.model.Customer;
import org.joenjogu.bookstore.orderservice.domain.model.OrderItem;

public class TestDataFactory {

    static final List<String> VALID_COUNTRIES = List.of("India", "Kenya", "Germany");
    static final Set<OrderItem> VALID_ORDER_ITEMS =
            Set.of(new OrderItem("P100", "Plastic", BigDecimal.valueOf(34.00), 11));
    static final Set<OrderItem> INVALID_ORDER_ITEMS = Set.of(new OrderItem("", "", BigDecimal.TEN, 11));

    public static CreateOrderRequest createValidOrderRequest() {
        return Instancio.of(CreateOrderRequest.class)
                .set(field(CreateOrderRequest::items), VALID_ORDER_ITEMS)
                .generate(field(Customer::email), gen -> gen.text().pattern("#a#a#a#a#a#a@mail.com"))
                .generate(field(Address::country), gen -> gen.oneOf(VALID_COUNTRIES))
                .create();
    }

    public static CreateOrderRequest createOrderRequestWithInvalidCustomer() {
        return Instancio.of(CreateOrderRequest.class)
                .set(field(CreateOrderRequest::items), VALID_ORDER_ITEMS)
                .set(field(Customer::name), "")
                .generate(field(Customer::email), gen -> gen.text().pattern("#a#a#a#a#a#a@mail.com"))
                .generate(field(Address::country), gen -> gen.oneOf(VALID_COUNTRIES))
                .create();
    }

    public static CreateOrderRequest createOrderRequestWithInvalidAddress() {
        return Instancio.of(CreateOrderRequest.class)
                .set(field(CreateOrderRequest::items), VALID_ORDER_ITEMS)
                .generate(field(Customer::email), gen -> gen.text().pattern("#a#a#a#a#a#a@mail.com"))
                .set(field(Address::city), "")
                .create();
    }

    public static CreateOrderRequest createOrderRequestWithNoItems() {
        return Instancio.of(CreateOrderRequest.class)
                .set(field(CreateOrderRequest::items), Set.of())
                .generate(field(Customer::email), gen -> gen.text().pattern("#a#a#a#a#a#a@mail.com"))
                .generate(field(Address::country), gen -> gen.oneOf(VALID_COUNTRIES))
                .create();
    }
}
