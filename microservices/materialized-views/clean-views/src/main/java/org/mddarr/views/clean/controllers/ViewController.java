package org.mddarr.views.clean.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.config.StreamsBuilderFactoryBean;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ViewController {

    private final StreamsBuilderFactoryBean streamsBuilderFactoryBean;


}
