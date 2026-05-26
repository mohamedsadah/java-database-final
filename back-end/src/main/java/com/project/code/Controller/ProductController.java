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

import com.project.code.Model.Product;
import com.project.code.Repo.InventoryRepository;
import com.project.code.Repo.ProductRepository;
import com.project.code.Service.ServiceClass;

@RestController
@RequestMapping("/product")
public class ProductController {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ServiceClass serviceClass;

    @Autowired
    private InventoryRepository inventoryRepository;


    @PostMapping("/addProduct")
    public Map<String,String> addProduct(@RequestBody Product product){
        Map<String,String> message = new HashMap<>();

        if(!serviceClass.validateProduct(product)){
            try {
                productRepository.save(product);
                message.put("Success", "Product added successfully");
            }
            catch(DataIntegrityViolationException e){
                String err = e.getRootCause() != null ? 
                            e.getRootCause().getMessage() : 
                            e.getMessage();
                message.put("Error", err == null ?
                    "Data Integrity Violated" : err);
            }catch(Exception ex){
                message.put("Error", "An error occurred: "+ex.getMessage());
            }
        }
        else message.put("Error", "Product already exists");

        return message;
    }


    @GetMapping("/product/{id}")
    public Map<String, Object> getProductbyId(@PathVariable long id){
        Map<String, Object> product = new HashMap<>();
        product.put("products", productRepository.findById(id));
        return product;
    }


    @PutMapping("/updateProduct")
    public Map<String, String> updateProduct(@RequestBody Product productOBJ){
        Map<String, String> message = new HashMap<>();

        try{
            if(productRepository.findById(productOBJ.getId()) != null){
                productRepository.save(productOBJ);
                message.put("message", "Successfully updated product");
            }else
                message.put("message", "cannot update a non-existing record");
        }catch(Exception e){
            message.put("message", "Error occurred: "+e.getMessage());
        }

        return message;
    }


    @GetMapping("/category")
    public Map<String, Object> filterbyCategoryProduct(
        @RequestParam(required = false) String name,
        @RequestParam(required = false) String category
    ){
        Map<String, Object> productsMap = new HashMap<>();
        List<Product> products;

        if(name == null && category != null) 
            products = productRepository.findByCategory(category);
        else if(category == null && name != null) 
            products = productRepository.findBySubName(name);
        else
            products = productRepository.findBySubNameAndCategory(name, category);

        productsMap.put("products", products);

        return productsMap;
    }

    @GetMapping("/list")
    public Map<String,Object> listProducts(){
        Map<String, Object> productsMap = new HashMap<>();
        List<Product> products = productRepository.findAll();
        productsMap.put("products", products);
        return productsMap;
    }

    @GetMapping("filter/{category}/{storeId}")
    public Map<String, Object> getProductbyCategoryAndStoreId(
        @PathVariable String category,
        @PathVariable long storeId
    ){
        Map<String, Object> productsMap = new HashMap<>();
        List<Product> products = productRepository
            .findByCategoryAndStoreId(storeId, category);
        
        productsMap.put("product", products);

        return productsMap;
    }
     


    @DeleteMapping("/deleteProduct/{id}")
    public Map<String,String> deleteProduct(@PathVariable Long id){
        Map<String,String> message = new HashMap<>();
        if(serviceClass.validateProductId(id.longValue())){
            inventoryRepository.deleteByProductId(id);
            productRepository.deleteById(id);
            message.put("message", "Product Deleted Successfully");
        }else 
            message.put("message", "no record found for thhis product");

        return message;
    }

    @GetMapping("/searchProduct/{name}")
    public Map<String,Object> searchProduct(@PathVariable String name){
        Map<String, Object> productsMap = new HashMap<>();
        List<Product> products = productRepository.findBySubName(name);

        productsMap.put("products", products);

        return productsMap;
    }


  
    
}
