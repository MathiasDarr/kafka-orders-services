package org.mddarr.inventoryservice.controllers;


import org.mddarr.inventoryservice.dto.Category;
import org.mddarr.inventoryservice.dto.Product;
import org.mddarr.inventoryservice.services.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
public class ProductsController {

    private final ProductService productService;

    public ProductsController(ProductService productService){
        this.productService = productService;
    }

    @GetMapping("categories")
    public List<Category> allCategories() {
        return productService.fetchCategories();
    }

    @GetMapping("products")
    public List<Product> allProducts() {
        return productService.fetchAllProducts();
    }

    @GetMapping("products/vendor/{brandID}")
    public List<Product> allVendorProducts(@PathVariable String brandID) {
        return productService.fetchAllProductsByBrand(brandID);
    }

    @GetMapping("products/category/{category}")
    public List<Product> allCategoryProducts(@PathVariable String category) {
        return productService.fetchAllProductsByCategory(category);
    }

    @GetMapping("products/{brand}/{productName}")
    public ResponseEntity<Product> getProduct(@PathVariable String brand, @PathVariable String productName,
                                              @RequestParam(name="isCacheable") boolean isCacheable ){
        return ResponseEntity.of(productService.getProduct(brand, productName, isCacheable));
    }

//    @GetMapping
//    public List<Product> allBrandsProducts(){
//
//    }

}
