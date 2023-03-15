package de.neuefische.integrationtest.controller;

import de.neuefische.integrationtest.model.Product;
import de.neuefische.integrationtest.service.ShopService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/shop")
public class ShopController {

    private final ShopService shopService;

    /*
     * Dependency Injection = Wofür genau?
     *
     * Gibt Verantwortung an Spring ab
     * + Weniger Code schreiben
     *
     * Wir verwenden keine leeren Konstruktoren
     * -> Spring verknüpft unsere Abhängigkeiten automatisch
     *
     * -> Testen wird einfacher
     * */
    public ShopController(ShopService shopService) {
        this.shopService = shopService;
    }

    // Das ist ein Endpunkt
    @GetMapping("/products")
    public List<Product> getAllProducts() {
        return shopService.listProducts();
    }

    @PostMapping("/products")
    public Product addProduct(@RequestBody Product product) {
        return shopService.addProduct(product);
    }
}
