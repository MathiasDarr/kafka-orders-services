package org.mddarr.providerservice.controllers;

import org.mddarr.providerservice.models.Product;
import org.mddarr.providerservice.services.ProductService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ProductsController {

    private final ProductService productService;

    public ProductsController(ProductService productService){
        this.productService = productService;
    }

    @GetMapping(value="products")
    public List<Product> getPatients(){
        return productService.getProducts();
    }


}
