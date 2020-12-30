package org.mddarr.patientservice.repository;

import org.mddarr.patientservice.models.Order;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends CassandraRepository<Order, Long> {




}