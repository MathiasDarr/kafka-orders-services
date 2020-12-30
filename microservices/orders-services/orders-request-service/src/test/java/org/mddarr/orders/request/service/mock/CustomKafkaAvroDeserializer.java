package org.mddarr.orders.request.service.mock;

import org.mddarr.orders.event.dto.AvroOrder;
import org.mddarr.orders.request.service.Constants;

import io.confluent.kafka.schemaregistry.client.MockSchemaRegistryClient;
import io.confluent.kafka.schemaregistry.client.SchemaRegistryClient;
import io.confluent.kafka.serializers.KafkaAvroDeserializer;
import org.apache.avro.Schema;



/**
 * This code is not thread safe and should not be used in production environment
 */
public class CustomKafkaAvroDeserializer extends KafkaAvroDeserializer {
    @Override
    public Object deserialize(String topic, byte[] bytes) {
        if (topic.equals(Constants.ORDER_TOPIC)) {
            this.schemaRegistry = getMockClient(AvroOrder.SCHEMA$);
        }

        return super.deserialize(topic, bytes);
    }

    private static SchemaRegistryClient getMockClient(final Schema schema$) {
        return new MockSchemaRegistryClient() {
            @Override
            public synchronized Schema getById(int id) {
                return schema$;
            }
        };
    }
}
