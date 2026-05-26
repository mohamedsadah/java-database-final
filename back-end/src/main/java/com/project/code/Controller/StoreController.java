package com.project.code.Controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.code.Model.PlaceOrderRequestDTO;
import com.project.code.Model.Store;
import com.project.code.Repo.StoreRepository;
import com.project.code.Service.OrderService;

@RestController
@RequestMapping("/store")
public class StoreController {

    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private OrderService orderService;


    @PostMapping
    public Map<String, String> addStore(@RequestBody Store store){
        Map<String, String> messageMap = new HashMap<>();
        Store storeSaved = storeRepository.save(store);

        if(storeSaved != null){
            messageMap.put("message", "store with id: "+storeSaved.getId()+" saved Successfully");
            return messageMap;
        }
        
        messageMap.put("message", "Error: store with id: "+store.getId()+" not saved");
        
        return messageMap;
        
    }
 
    @GetMapping("validate/{storeId}")
    public boolean validateStore(@PathVariable Long storeId){
        return storeRepository.existsById(storeId);
    }


    @PostMapping("/placeOrder")
    public Map<String, String> placeOrder(@RequestBody PlaceOrderRequestDTO order){
        Map<String,String> message = new HashMap<>();

        if(order.getStoreId() == null){
            message.put("Error", "Store is required");
            return message;
        }
        try{
            orderService.saveOrder(order);
            message.put("message", "Order placed Successfully");
        }catch(Exception e){
            String err = e.getCause() != null ? e.getCause().getMessage() : e.getMessage();
            message.put("Error", err == null ?
                "Cannot place Order please try again" : err
            );
        }

        return message;

    }
   
}
