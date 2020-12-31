package org.mddarr.producer.repository;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
import org.mddarr.producer.domain.Book;
import org.mddarr.producer.domain.Product;

public class ProductCassandraRepository {

    private static final String TABLE_NAME = "products";

    private static final String TABLE_NAME_BY_TITLE = TABLE_NAME + "ByTitle";

    private Session session;

    public ProductCassandraRepository(Session session) {
        this.session = session;
    }

    public List<Product> selectAll() {
        StringBuilder sb = new StringBuilder("SELECT * FROM products");

        final String query = sb.toString();
        ResultSet rs = session.execute(query);

        List<Product> products = new ArrayList<>();

        for (Row r : rs.all()) {
            Product product = new Product(r.getString("vendor"), r.getString("name"), r.getString("category"), r.getString("image_url"), r.getFloat("price"), r.getLong("inventory") );
            products.add(product);
        }
        return products;
    }

}
