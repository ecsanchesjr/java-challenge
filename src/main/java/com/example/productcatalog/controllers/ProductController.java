package com.example.productcatalog.controllers;

import com.example.productcatalog.entities.Product;
import com.example.productcatalog.specs.ProductSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.productcatalog.repositories.ProductRepository;

import java.util.*;

@RestController
@RequestMapping("/")
public class ProductController {

    @Autowired
    ProductRepository productRepository;

    @GetMapping("/products")
    public ResponseEntity<List<Product>> findAll() {
        List<?> products = productRepository.findAll();
        return new ResponseEntity(products, HttpStatus.OK);
    }

    @GetMapping("/products/{id}")
    public ResponseEntity<Product> findById(@PathVariable("id") long id) {
        Optional<Product> product = productRepository.findById(id);
        if(product.isPresent()) return new ResponseEntity(product.get(), HttpStatus.OK);
        else return new ResponseEntity(null, HttpStatus.NOT_FOUND);
    }

    @GetMapping("/products/search")
    public ResponseEntity<List<Product>> findAllByParams(@RequestParam Map<String, String> searchParams) {
        List<Product> products = productRepository.findAll(new ProductSpecification(searchParams));
        return new ResponseEntity(products, HttpStatus.OK);
    }

    @PostMapping("/products")
    public ResponseEntity<Product> create(@RequestBody Product product){
        try {
            if(product.getPrice() < 0) throw new NumberFormatException();
            Product savedProduct = productRepository.save(new Product(product.getName(), product.getDescription(), product.getPrice()));
            return new ResponseEntity(savedProduct, HttpStatus.CREATED);
        } catch (Exception e) {
            Map<String,String> body = new HashMap<>();
            body.put("message", "Invalid data for product: the name, description and price fields are REQUIRED and price must be greater than zero.");
            body.put("status_code", String.valueOf(HttpStatus.BAD_REQUEST.value()));
            return new ResponseEntity(body, HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/products/{id}")
    public ResponseEntity<Product> update(@PathVariable("id") long id, @RequestBody Product product) {
        try {
            Optional<Product> tmpProduct = productRepository.findById(id);
            if(!tmpProduct.isPresent()) return new ResponseEntity(null, HttpStatus.NOT_FOUND);

            if(product.getPrice() < 0) throw new NumberFormatException();
            tmpProduct.get().setName(product.getName());
            tmpProduct.get().setDescription(product.getDescription());
            tmpProduct.get().setPrice(product.getPrice());
            Product savedProduct = productRepository.save(tmpProduct.get());
            return new ResponseEntity(savedProduct, HttpStatus.CREATED);
        } catch (Exception e) {
            Map<String,String> body = new HashMap<>();
            body.put("message", "Invalid data for product: the name, description and price fields are REQUIRED and price must be greater than zero.");
            body.put("status_code", String.valueOf(HttpStatus.BAD_REQUEST.value()));
            return new ResponseEntity(body, HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("products/{id}")
    public ResponseEntity<Product> delete(@PathVariable("id") long id) {
        try {
            productRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
