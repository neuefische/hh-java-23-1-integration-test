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

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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
    // Immer über den Methoden, die den Speicher verändern
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
        // Wir verschicken eine POST Anfrage an die URL
        mockMvc.perform(
                        // WAS WIR VERSCHICKEN
                        post("/api/shop/products")
                                // Das ist unser Format - wir verschicken fast immer JSON
                                .contentType(MediaType.APPLICATION_JSON)
                                // Was im Body beim POST-Request verschickt wird
                                .content(
                                        """
                                                 {
                                                     "name": "Primitivo Wein",
                                                     "id": "1"
                                                 }
                                                """
                                ))
                // VERGLEICH - IST UNSER ERGEBNIS RICHTIG?
                .andExpect(
                        // Der Status den wir zurück bekommen
                        status().isOk()
                )
                .andExpect(
                        // Das ist der Request Body - Der Inhalt den wir bekommen
                        content().json(
                                """
                                         {
                                             "name": "Primitivo Wein",
                                             "id": "1"
                                         }
                                        """
                        ));
    }

    @Test
    @DirtiesContext
    void putProduct_shouldReturnChangedProduct() throws Exception {
        productRepository.add(new Product("Tetrapack Wein", "1"));

        mockMvc.perform(put("/api/shop/products/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(
                                """
                                         {
                                             "name": "Primitivo Wein",
                                             "id": "1"
                                         }
                                        """
                        ))
                .andExpect(
                        status().isOk()
                )
                .andExpect(
                        content().json(
                                """
                                         {
                                             "name": "Primitivo Wein",
                                             "id": "1"
                                         }
                                        """
                        ));
    }

    @Test
    @DirtiesContext
    void deleteProduct_shouldReturnEmptyBody() throws Exception {
        productRepository.add(new Product("Tetrapack Wein", "1"));

        mockMvc.perform(delete("/api/shop/products/1"))
                .andExpect(
                        status().isOk()
                );
    }
}
