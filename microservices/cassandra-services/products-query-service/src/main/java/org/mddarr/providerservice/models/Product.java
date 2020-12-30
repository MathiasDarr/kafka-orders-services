package org.mddarr.providerservice.models;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;

import java.util.List;

@Table("products")
@Data
@NoArgsConstructor
public class Product {

    @PrimaryKeyColumn(name="vendor", ordinal = 0, type = PrimaryKeyType.PARTITIONED )
    private String provider_id;

    @PrimaryKeyColumn(name="name", ordinal = 1, type = PrimaryKeyType.PARTITIONED)
    private String department_name;

    @Column("category") private String category;
    @Column("price") private Double price;
    @Column("image_url") private String image_url;
    @Column("inventory") private Integer inventory;

    public String getProvider_id() {
        return provider_id;
    }

    public void setProvider_id(String provider_id) {
        this.provider_id = provider_id;
    }

    public String getDepartment_name() {
        return department_name;
    }

    public void setDepartment_name(String department_name) {
        this.department_name = department_name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public Integer getInventory() {
        return inventory;
    }

    public void setInventory(Integer inventory) {
        this.inventory = inventory;
    }
}

