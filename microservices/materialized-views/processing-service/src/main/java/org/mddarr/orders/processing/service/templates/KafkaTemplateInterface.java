package org.mddarr.orders.processing.service.templates;

import org.springframework.kafka.core.KafkaTemplate;

public interface KafkaTemplateInterface<T> {
    KafkaTemplate<String, T> getKafkaTemplate();
}
