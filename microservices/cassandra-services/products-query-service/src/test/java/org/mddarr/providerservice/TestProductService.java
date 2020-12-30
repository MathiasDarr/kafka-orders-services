package org.mddarr.providerservice;

import com.datastax.driver.core.Cluster;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.jupiter.api.Test;

import org.testcontainers.containers.GenericContainer;

public class TestProductService {

    @ClassRule
    public static GenericContainer cassandra = new GenericContainer("cassandra:3")
                    .withExposedPorts(9042);

    @Test
    public void test() {
    }


}
