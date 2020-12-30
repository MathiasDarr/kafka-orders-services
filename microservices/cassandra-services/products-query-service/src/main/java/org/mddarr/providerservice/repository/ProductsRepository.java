package org.mddarr.providerservice.repository;

import org.mddarr.providerservice.models.Product;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductsRepository extends CassandraRepository<Product, Long> {


}