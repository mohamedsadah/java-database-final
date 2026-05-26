package com.project.code.Controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.project.code.Model.CombinedRequest;
import com.project.code.Model.Inventory;
import com.project.code.Model.Product;
import com.project.code.Repo.InventoryRepository;
import com.project.code.Repo.ProductRepository;
import com.project.code.Repo.StoreRepository;
import com.project.code.Service.ServiceClass;

@RestController
@RequestMapping("/inventory")
public class InventoryController {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private InventoryRepository inventoryRepository;

    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private ServiceClass serviceClass;

    @PutMapping("/updateInventory")
    public Map<String, String> updateInventory(@RequestBody CombinedRequest request){
        Map<String, String> message = new HashMap<>();

        Product product = request.getProduct();
        Inventory inventoryOBJ = request.getInventory();

        if(serviceClass.validateProductId(product.getId())){
            Inventory inventory = inventoryRepository
            .findByProductIdandStoreId(product.getId(), inventoryOBJ.getStore().getId());

            if(inventory != null){
                product.setId(inventory.getProduct().getId());
                inventory.setProduct(product);
                inventoryRepository.save(inventory);
                message.put("Success","Successfully updated product");
                return message;
            }else message.put("Error", "No data Available");
        }else message.put("Error", "Product does not exist");

        return message;

    }


    @PostMapping("/saveInventory")
    public Map<String, String> saveInventory(@RequestBody Inventory inventory){
        Map<String,String> message = new HashMap<>();

        if(serviceClass.validateInventory(inventory)) {
            message.put("Error", "Data already exists");
            return message;
        }

        if(inventory.getProduct() == null || inventory.getStore() == null) {
            message.put("Error", 
                inventory.getProduct() == null 
                ? "Product is missing" : 
                inventory.getStore() == null 
                ? "Store is missing" : "product and Store are missing");
            return message;
        }

        Long productId = inventory.getProduct().getId();
        Long storeId = inventory.getStore().getId();

        if(productId == null || !productRepository.findById(productId).isPresent()){
            message.put("Error", "Product does not exist in database");
            return message;
        }

        if(storeId == null || !storeRepository.findById(storeId).isPresent()){
            message.put("Error", "Store does not exist in database");
            return message;
        }

        if(inventory.getStockLevel() < 0){
            message.put("Error", "Invalid stock level");
            return message;
        }

        try{
            inventoryRepository.save(inventory);
            message.put("Success", "Data saved Successfully");
        }catch(DataIntegrityViolationException e){
            String err = e.getRootCause() != null ? e.getRootCause().getMessage() : e.getMessage();
            message.put("Error", 
                err == null ? 
                "Data integrity violation" : err);
        }catch(Exception ex){
                message.put("Error", "An error occurred: "+ex.getMessage());
        }

        return message;
    }


    @GetMapping("/getAllProducts/{storeId}")
    public Map<String, Object> getAllProducts(@PathVariable long storeId){
        Map<String, Object> productsMap = new HashMap<>();

        List<Product> products = productRepository.findByStoreId(storeId);

        productsMap.put("products", products);

        return productsMap;
         
    }


    @GetMapping("/getProductName/filter")
    public Map<String, Object> getProductName(
            @RequestParam(required = false) String category, 
            @RequestParam(required = false) String name, 
            @RequestParam Long storeId){

        Map<String, Object> productMap = new HashMap<>();

        if(storeId == null){
            productMap.put("Error", "StoreId is Required");
            return productMap;
        }

        if(name == null && category == null){
            productMap.put("Error", "Atleat a name or category is required");
            return productMap;
        }

        List<Product> products;

        if(name == null && category != null) products = productRepository.findByCategoryAndStoreId(storeId.longValue(), category);
        else if(name != null && category == null) products = productRepository.findByNameLike(storeId.longValue(), name);
        else products = productRepository.findByNameAndCategory(storeId.longValue(), name, category);

        productMap.put("product", products);

        return productMap;
    }


    @GetMapping("/search/{name}/{storeId}")
    public Map<String, Object> searchProduct(@PathVariable String name, @PathVariable Long storeId){
        Map<String, Object> productMap = new HashMap<>();
        List<Product> products = productRepository.findByNameLike(storeId.longValue(), name);
        productMap.put("product", products);

        return productMap;
    }


    @DeleteMapping("/removeProduct/{id}")
    public Map<String,String> removeProduct(@PathVariable("id") Long productId){
        Map<String,String> message = new HashMap<>();

        if(!serviceClass.validateProductId(productId.longValue())){
            message.put("message","Product with id: "+productId+" does not exist in database");
            return message;
        }

        inventoryRepository.deleteByProductId(productId);
        message.put("message", "Product deleted successfully");
        return message;

    }


    @GetMapping("/validate/{quantity}/{storeId}/{productId}")
    public boolean validateQuantity(@PathVariable int quantity, @PathVariable Long storeId,
            @PathVariable Long productId){

        Inventory inventory = inventoryRepository.findByProductIdandStoreId(productId, storeId);
        
        if(inventory == null) return false;

        int availableQuantity = inventory.getStockLevel();

        if(availableQuantity >= quantity) return true;

        return false;
    }

}
