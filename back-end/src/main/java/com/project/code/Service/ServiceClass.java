package com.project.code.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.code.Model.Inventory;
import com.project.code.Model.Product;
import com.project.code.Repo.InventoryRepository;
import com.project.code.Repo.ProductRepository;

@Service
public class ServiceClass {

    @Autowired
    private InventoryRepository inventoryRepository;

    @Autowired
    private ProductRepository productRepository;
    
    public boolean validateInventory(Inventory inventory){
        return inventoryRepository.findByProductIdandStoreId(
            inventory.getProduct().getId(), inventory.getStore().getId()) != null;
    }

    public boolean validateProduct(Product product){
        return productRepository.findByName(product.getName()) != null;
    }


    public boolean validateProductId(long id){
        return productRepository.findById(id) != null;
    }

    public Inventory getInventory(Inventory inventory){
        Inventory record = inventoryRepository
        .findByProductIdandStoreId(inventory.getProduct().getId(), 
        inventory.getStore().getId());

        return record;
    }

}
