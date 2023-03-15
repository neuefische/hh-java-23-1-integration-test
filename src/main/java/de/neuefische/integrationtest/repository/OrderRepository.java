package de.neuefische.integrationtest.repository;

import de.neuefische.integrationtest.model.Order;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class OrderRepository {

    private List<Order> orders;

    public OrderRepository(List<Order> orders) {
        this.orders = orders;
    }

    public List<Order> list() {
        return orders;
    }

    public Order get(String id) {
        for (Order order : orders) {
            if (order.getId().equals(id)) {
                return order;
            }
        }
        return null;
    }

    public Order add(Order orderToAdd) {
        orders.add(orderToAdd);
        return orderToAdd;
    }
}
