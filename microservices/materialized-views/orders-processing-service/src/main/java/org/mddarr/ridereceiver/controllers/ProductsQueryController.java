package org.mddarr.ridereceiver.controllers;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.kafka.common.serialization.LongSerializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.kafka.streams.state.HostInfo;
import org.apache.kafka.streams.state.QueryableStoreTypes;
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;
import org.mddarr.products.AvroProduct;
import org.mddarr.ridereceiver.Constants;
import org.mddarr.ridereceiver.models.ProductBean;
import org.mddarr.ridereceiver.models.SongBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.binder.kafka.streams.InteractiveQueryService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;


@RestController
public class ProductsQueryController {

    private final Log logger = LogFactory.getLog(getClass());

    @Autowired
    private InteractiveQueryService interactiveQueryService;


    @RequestMapping("/product/idx")
    public ProductBean product(@RequestParam(value="id") String id) {
        final ReadOnlyKeyValueStore<String, AvroProduct> productStore =
                interactiveQueryService.getQueryableStore(Constants.ALL_PRODUCTS, QueryableStoreTypes.<String, AvroProduct>keyValueStore());

        final AvroProduct product = productStore.get(id);
        if (product == null) {
            throw new IllegalArgumentException("hi");
        }
        return new ProductBean(product.getVendor(), product.getProduct());
    }


}

