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
import org.mddarr.ridereceiver.MaterializedViewApplication;
import org.mddarr.ridereceiver.TopFiveProducts;
import org.mddarr.ridereceiver.models.ProductBean;
import org.mddarr.ridereceiver.models.ProductPurchaseCountBean;
import org.mddarr.ridereceiver.queries.PurchaseEventBean;
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


    @RequestMapping("/charts/top-five")
    @SuppressWarnings("unchecked")
//    public List<ProductPurchaseCountBean> topFive() {

    public String topFive() {

        List<HostInfo> infos = interactiveQueryService.getAllHostsInfo(Constants.TOP_FIVE_PRODUCTS_STORE);
        System.out.println("THE INFOS ARE " + infos.toString());
//        HostInfo hostInfo = interactiveQueryService.getHostInfo(Constants.TOP_FIVE_PRODUCTS_STORE,
//                Constants.TOP_FIVE_KEY, new StringSerializer());
////        String a =interactiveQueryService.getCurrentHostInfo();
//        return hostInfo.toString();
        return infos.toString();
//        if (interactiveQueryService.getCurrentHostInfo().equals(hostInfo)) {
//            logger.info("Top Five products request served from same host: " + hostInfo);
//            return topFiveSongs(Constants.TOP_FIVE_KEY, Constants.TOP_FIVE_PRODUCTS_STORE);
//        }
//        else {
//            //find the store from the proper instance.
//            logger.info("Top Five songs request served from different host: " + hostInfo);
//            RestTemplate restTemplate = new RestTemplate();
//            return restTemplate.postForObject(String.format("http://%s:%d/%s", hostInfo.host(),
//                            hostInfo.port(), "charts/top-five?genre=Punk"), "punk", List.class);
//        }
    }

    private List<ProductPurchaseCountBean> topFiveSongs(final String key, final String storeName) {
        final ReadOnlyKeyValueStore<String, TopFiveProducts> topFiveStore =
                interactiveQueryService.getQueryableStore(storeName, QueryableStoreTypes.<String, TopFiveProducts>keyValueStore());

        // Get the value from the store
        final TopFiveProducts value = topFiveStore.get(key);
        if (value == null) {
            throw new IllegalArgumentException(String.format("Unable to find value in %s for key %s", storeName, key));
        }
        final List<ProductPurchaseCountBean> results = new ArrayList<>();
        value.forEach(productPurchaseCount -> {

            HostInfo hostInfo = interactiveQueryService.getHostInfo(Constants.ALL_PRODUCTS, productPurchaseCount.getProductId(), new StringSerializer());

            if (interactiveQueryService.getCurrentHostInfo().equals(hostInfo)) {
                logger.info("Song info request served from same host: " + hostInfo);

                final ReadOnlyKeyValueStore<String, AvroProduct> songStore =
                        interactiveQueryService.getQueryableStore(Constants.ALL_PRODUCTS, QueryableStoreTypes.<String, AvroProduct>keyValueStore());

                final AvroProduct product = songStore.get(productPurchaseCount.getProductId());
                results.add(new ProductPurchaseCountBean(product.getVendor(),product.getProduct(),productPurchaseCount.getCount()));
            }
            else {
                logger.info("Song info request served from different host: " + hostInfo);
                RestTemplate restTemplate = new RestTemplate();
                ProductBean productBean = restTemplate.postForObject(String.format("http://%s:%d/%s", hostInfo.host(),hostInfo.port(), "product/idx?id=" + productPurchaseCount.getProductId()),  "id", ProductBean.class);
                results.add(new ProductPurchaseCountBean(productBean.getVendor(),productBean.getProduct(), productPurchaseCount.getCount()));
            }
        });
        return results;
    }
}






