package com.project.code.Model;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "inventory")
public class Inventory {

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private long id;

   @ManyToOne
   @JoinColumn(name = "product_id")
   @JsonBackReference("inventory-product")
   private Product product;

   @ManyToOne
   @JoinColumn(name = "store_id")
   @JsonBackReference("inventory-store")
   private Store store;

   private int stocklevel;

   public Inventory() {}

   public Inventory(Product product, Store store, int stocklevel){
      this.product = product;
      this.store = store;
      this.stocklevel = stocklevel;
   }

   public Product getProduct() {
      return this.product;
   }

   public Store getStore() {
      return this.store;
   }

   public int getStockLevel() {
      return this.stocklevel;
   }

   public void setProduct(Product product){
      this.product = product;
   }

   public void setStore(Store store){
      this.store = store;
   }

   public void setStockLevel(int stocklevel){
      this.stocklevel = stocklevel;
   }

}

