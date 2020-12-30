package org.mddarr.providerservice.services;

import org.mddarr.providerservice.models.Product;
import org.mddarr.providerservice.repository.ProductsRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService implements ProductServiceInterface {

    private final ProductsRepository productsRepository;

    public ProductService(ProductsRepository patientRepository){
        this.productsRepository = patientRepository;
    }

    @Override
    public List<Product> getProducts() {
        return productsRepository.findAll();
    }
}
