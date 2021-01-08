package org.mddarr.store.processing.service.controllers;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.streams.state.HostInfo;
import org.apache.kafka.streams.state.QueryableStoreTypes;
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;
import org.mddarr.store.processing.service.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.binder.kafka.streams.InteractiveQueryService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequiredArgsConstructor
public class QueryController {

    @Autowired
    private InteractiveQueryService interactiveQueryService;

    @GetMapping(value="state")
    public long getState(){

        final ReadOnlyKeyValueStore<String, Long> songStore =
                interactiveQueryService.getQueryableStore(Constants.PURCHASE_COUNT_STORE, QueryableStoreTypes.<String, Long>keyValueStore());
        return songStore.approximateNumEntries();
    }

}
