package org.mddarr.patientservice.services;

import org.mddarr.patientservice.models.Order;
import org.mddarr.patientservice.repository.OrderRepository;
import org.springframework.data.cassandra.core.CassandraOperations;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrdersService implements OrderServiceInterface {

    private final CassandraOperations cassandraTemplate;
    private final OrderRepository orderRepository;

    public OrdersService(OrderRepository orderRepository, CassandraOperations cassandraTemplate){
        this.orderRepository = orderRepository;
        this.cassandraTemplate = cassandraTemplate;
    }

    @Override
    public List<Order> getOrders() {
        return orderRepository.findAll();
    }
}
