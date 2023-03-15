package de.neuefische.integrationtest.service;

import de.neuefische.integrationtest.model.Product;
import de.neuefische.integrationtest.model.Order;
import de.neuefische.integrationtest.repository.OrderRepository;
import de.neuefische.integrationtest.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ShopService {

    private OrderRepository orderRepository;
    private ProductRepository productRepository;

    public ShopService(OrderRepository orderRepository,
                       ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
    }

    public Product getProduct(String id) {
        // cmd alt v oder ctrl alt v
        Product product = productRepository.get(id);
        return product;
    }

    public List<Product> listProducts() {
        return productRepository.list();
    }

    public List<Order> listOrders() {
        return orderRepository.list();
    }

    public Order getOrder(String id) {
        return orderRepository.get(id);
    }

    public Order addOrder(String orderId, List<String> ids) {

        List<Product> products = new ArrayList<>();

        for (String id : ids) {
            Product product = productRepository.get(id);
            products.add(product);
        }

        Order newOrder = new Order(orderId, products);
        return orderRepository.add(newOrder);
    }

    public Product addProduct(Product product) {
        productRepository.add(product);
        return product;
    }

    public Product updateProduct(Product product) {
        return productRepository.updateProduct(product);
    }

    public void deleteProduct(String id) {
        productRepository.removeProduct(id);
    }
}
