package com.project.code.Model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "product", uniqueConstraints = @UniqueConstraint(columnNames = "sku"))
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotNull(message = "Product name cannot be null")
    @NotBlank(message = "Product name cannot be empty")
    private String name;

    @NotNull(message = "Product price cannot be null")
    @DecimalMin("1.0")
    private Double price;

    @NotNull(message = "Product sku cannot be null")
    @NotBlank(message = "Product sku cannot be empty")
    private String sku;

    @NotNull(message = "Product category cannot be null")
    @NotBlank(message = "Product category cannot be empty")
    private String category;

    @OneToMany(mappedBy = "product")
    @JsonManagedReference("inventory-product")
    private List<Inventory> inventories;

    public Product() {}

    public Product(String name, double price, String sku){
        this.name = name;
        this.price = price;
        this.sku = sku;
    }

    public Product(String name, double price, String sku, String category) {
        this.name = name;
        this.price = price;
        this.sku = sku;
        this.category = category;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public List<Inventory> getInventories() {
        return inventories;
    }

    public void setInventories(List<Inventory> inventories) {
        this.inventories = inventories;
    }

}


