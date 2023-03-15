package de.neuefische.integrationtest.repository;

import de.neuefische.integrationtest.model.Product;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ProductRepository {

    private List<Product> products;

    public ProductRepository(List<Product> products) {
        this.products = products;
    }

    public List<Product> list() {
        return products;
    }

    public Product get(String id) {
        for (Product product : products) {
            if (product.id().equals(id)) {
                return product;
            }
        }
        return null;
    }

    public Product add(Product product) {
        products.add(product);
        return product;
    }
}
