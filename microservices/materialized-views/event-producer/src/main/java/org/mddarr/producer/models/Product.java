package org.mddarr.producer.models;

public class Product {

    private String vendor;
    private String product;
    private String category;
    private String image_url;
    private Long inventory;
    private Float price;

    Product() {
    }

    public Product(String vendor, String product, String category, String image_url, Float price, Long inventory) {
        this.vendor = vendor;
        this.product = product;
        this.image_url = image_url;
        this.price = price;
        this.inventory = inventory;
        this.category = category;
    }

    public String getVendor() {
        return vendor;
    }

    public void setVendor(String vendor) {
        this.vendor = vendor;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public Long getInventory() {
        return inventory;
    }

    public void setInventory(Long inventory) {
        this.inventory = inventory;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }
}
