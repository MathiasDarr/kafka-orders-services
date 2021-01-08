package org.mddarr.orders.processing.service;

import static org.assertj.core.api.Assertions.assertThat;


public class InventoryViewTest {

//    @Test
//    void shouldCreateMaterializedView() {
//
//        final StreamsBuilder builder = new StreamsBuilder();
//
//        new Views.InventoryView().buildInventoryView(builder);
//        Properties config = new Properties();
//        config.putAll(Map.of(APPLICATION_ID_CONFIG, "test-app",
//                BOOTSTRAP_SERVERS_CONFIG, "dummy:9092"));
//
//        try (TopologyTestDriver topologyTestDriver = new TopologyTestDriver(builder.build(), config)) {
//            final TestInputTopic<Integer, String> orders =
//                    topologyTestDriver.createInputTopic(Constants.PRODUCT_INVENTORY_TOPIC,
//                            Serdes.Integer().serializer(),
//                            Serdes.String().serializer());
//
//            orders.pipeInput(1, "iPhone");
//            orders.pipeInput(2, "iPad");
//            orders.pipeInput(1, "iPhone, AirPods");
//            orders.pipeInput(2, "HomePod");
//
//            final KeyValueStore<Integer, String> keyValueStore =
//                    topologyTestDriver.getKeyValueStore(Constants.PRODUCT_INVENTORY_STORE);
//
//            assertThat(keyValueStore.get(1)).isEqualTo("iPhone, AirPods");
//            assertThat(keyValueStore.get(2)).isEqualTo("HomePod");
//        }
//    }
//
//
//    @Test
//    void shouldCreateInventoryView() {
//
//        final StreamsBuilder builder = new StreamsBuilder();
//
//        new Views.InventoryView().buildInventoryView(builder);
//        Properties config = new Properties();
//        config.putAll(Map.of(APPLICATION_ID_CONFIG, "test-app",
//                BOOTSTRAP_SERVERS_CONFIG, "dummy:9092"));
//
//
//
//
//
//        try (TopologyTestDriver topologyTestDriver = new TopologyTestDriver(builder.build(), config)) {
//            final TestInputTopic<Integer, String> orders =
//                    topologyTestDriver.createInputTopic(Constants.PRODUCT_INVENTORY_TOPIC,
//                            Serdes.Integer().serializer(),
//                            Serdes.String().serializer());
//
//            orders.pipeInput(1, "iPhone");
//            orders.pipeInput(2, "iPad");
//            orders.pipeInput(1, "iPhone, AirPods");
//            orders.pipeInput(2, "HomePod");
//
//            final KeyValueStore<Integer, String> keyValueStore =
//                    topologyTestDriver.getKeyValueStore(Constants.PRODUCT_INVENTORY_STORE);
//
//            assertThat(keyValueStore.get(1)).isEqualTo("iPhone, AirPods");
//            assertThat(keyValueStore.get(2)).isEqualTo("HomePod");
//        }
//    }















}