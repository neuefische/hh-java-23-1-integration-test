package de.neuefische.integrationtest.controller;

import de.neuefische.integrationtest.model.Product;
import de.neuefische.integrationtest.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// Wir schreiben einen Integrationstest und stellen sicher dass
// - die HTTP Methoden ausgeführt werden
// - der Service läuft
// - das Repo die Daten hergibt

// @SpringBootTest = Fährt den Spring Kontext hoch (Server, Datenbankverbindung, erzeugt Objekte, etc.)
// @AutoConfigureMockMvc = Konfiguriert MockMvc nach unseren Testbedürfnissen
@SpringBootTest
@AutoConfigureMockMvc
class ShopIntegrationTest {

    // Mock = So was wie Nachmachen
    // MockMvc ermöglicht uns Anfragen nachzumachen
    // Damit mockMvc NICHT null ist, müssen wir die Abhängigkeit, d.h. die DEPENDENCY bereitstellen...
    @Autowired
    MockMvc mockMvc;

    @Autowired
    ProductRepository productRepository;

    // 1. Wurde die Anfrage verstanden und angenommen? -> Status 200 = OK
    // 2. Bekommen wir alle Produkte zurück?
    // Wenn die Datenbank leer ist -> bekommen wir eine leere Liste []

    // Wenn wir alle Produkte abrufen
    // Sollen alle Produkte zurück kommen
    // Testmethode + sollte ...
    // PS: Es könnte vllllt eine Exception entstehen, das ist uns dann aber egal, weil dann ist der Test eben fehlgeschlagen
    @Test
    void getAllProducts_shouldReturnEmptyList_whenRepositoryIsEmpty() throws Exception {
        // mockMvc.perform -> wie bei Postman, verschickt eine Anfrage
        // GET = HTTP Verb um etwas anzufragen
        mockMvc.perform(get("/api/shop/products"))
                .andExpect(status().isOk())
                .andExpect(content().json(
                        """
                                []
                                """));
    }

    @Test
    // @DirtiesContext = Putzkraft => Wenn du etwas eingefügt hast ins Repo, lösche es danach wieder!
    @DirtiesContext
    void getAllProducts_shouldReturnListWithOneProduct_whenRepositoryHasOneProduct() throws Exception {
        // Wie kriegen wir etwas ins Repo?
        // zB per POST .. oder?
        // einfach direkt mit einem Repositoryaufruf!
        Product product = new Product("Georgischer Rotwein", "1");
        productRepository.add(product);

        // mockMvc.perform -> wie bei Postman, verschickt eine Anfrage
        // GET = HTTP Verb um etwas anzufragen
        mockMvc.perform(get("/api/shop/products"))
                .andExpect(status().isOk())
                .andExpect(content().json(
                        """
                                [
                                   {
                                    "name": "Georgischer Rotwein",
                                    "id": "1"
                                  }
                                ]
                                """));
    }

    // Wenn wir ein Produkt an den Controller senden, wird es erstellt.
    // Dieses neu erstellte Produkt erwarten wir als Antwort
    @Test
    @DirtiesContext
    void addProduct_shouldReturnCreatedProduct() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/shop/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(
                                """
                                         {
                                             "name": "Mayo",
                                             "id": "1"
                                         }
                                        """
                        ))
                .andExpect(status().isOk())
                .andExpect(content().json(
                        """
                                 {
                                     "name": "Mayo",
                                     "id": "1"
                                 }
                                """
                ));
    }
}