package org.joenjogu.bookstore.catalog.web.controllers;

import org.joenjogu.bookstore.catalog.domain.PagedResult;
import org.joenjogu.bookstore.catalog.domain.Product;
import org.joenjogu.bookstore.catalog.domain.ProductNotFoundException;
import org.joenjogu.bookstore.catalog.domain.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productsService;

    public ProductController(ProductService productsService) {
        this.productsService = productsService;
    }

    @GetMapping
    public PagedResult<Product> getProducts(@RequestParam(name = "page", defaultValue = "1") int pageNumber) {
        return productsService.getProducts(pageNumber);
    }

    @GetMapping("/{code}")
    public ResponseEntity<Product> getProductByCode(@PathVariable(name = "code") String code) {
        return productsService
                .findByCode(code)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> ProductNotFoundException.forCode(code));
    }
}
