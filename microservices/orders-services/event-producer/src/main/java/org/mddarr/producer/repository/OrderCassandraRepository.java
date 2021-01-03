package org.mddarr.producer.repository;

import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
import org.mddarr.producer.models.Order;

import java.util.ArrayList;
import java.util.List;

public class OrderCassandraRepository {

    private static final String TABLE_NAME = "orders";

    private Session session;

    public OrderCassandraRepository(Session session) {
        this.session = session;
    }

    public List<Order> selectAll() {
        StringBuilder sb = new StringBuilder("SELECT * FROM orders;");

        final String query = sb.toString();
        ResultSet rs = session.execute(query);
        String customerID;
        List<String> vendors;
        List<String> products;
        List<Long> quantities;
        List<Order> orders = new ArrayList<>();

        for (Row r : rs.all()) {
            Order product = new Order(r.getString("customerID"), r.getList("vendors", String.class),r.getList("products", String.class), r.getList("quantities", Long.class));
            orders.add(product);
        }
        return orders;
    }


}
