package com.project.code.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;

import com.project.code.Model.Customer;
import com.project.code.Model.Inventory;
import com.project.code.Model.OrderDetails;
import com.project.code.Model.OrderItem;
import com.project.code.Model.PlaceOrderRequestDTO;
import com.project.code.Model.Product;
import com.project.code.Model.PurchaseProductDTO;
import com.project.code.Model.Store;
import com.project.code.Repo.CustomerRepository;
import com.project.code.Repo.InventoryRepository;
import com.project.code.Repo.OrderDetailsRepository;
import com.project.code.Repo.OrderItemRepository;
import com.project.code.Repo.ProductRepository;
import com.project.code.Repo.StoreRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class OrderService {

    @Autowired
    private ProductRepository productRepo;

    @Autowired
    private InventoryRepository inventoryRepo;

    @Autowired
    private CustomerRepository customerRepo;

    @Autowired
    private StoreRepository storeRepo;

    @Autowired
    private OrderDetailsRepository orderDetailsRepo;

    @Autowired
    private OrderItemRepository orderItemRepo;



    @Transactional
    public void saveOrder(PlaceOrderRequestDTO placedOrder) {
        Customer customer = retrieveCustomer(
            placedOrder.getCustomerName(),
            placedOrder.getCustomerEmail(),
            placedOrder.getCustomerPhone());

        Store store = findStoreById(placedOrder.getStoreId());

        OrderDetails order = new OrderDetails(
            customer,
            store,
            placedOrder.getTotalPrice(),
            LocalDateTime.now());

        order = orderDetailsRepo.save(order);

        updateInventory(placedOrder);

        List<OrderItem> orderItems = createOrderItems(order, placedOrder.getPurchaseProduct());
        orderItemRepo.saveAll(orderItems);
    }

    public Customer retrieveCustomer(String Name, String email, String phone) {
        Customer customer = customerRepo.findByEmail(email);
        if (customer == null) {
            customer = new Customer(Name, email, phone);
            customerRepo.save(customer);
        }

        return customer;
    }

    public Store findStoreById(Long id){
        Optional<Store> store = storeRepo.findById(id);
        return store.orElseThrow(() -> new EntityNotFoundException("store with id: "+id+" not found"));
    }

    public OrderDetails orderdetails(PlaceOrderRequestDTO porder) {
        Customer customer = retrieveCustomer(
            porder.getCustomerName(),
            porder.getCustomerEmail(),
            porder.getCustomerPhone());

        Store store = findStoreById(porder.getStoreId());

        OrderDetails order = new OrderDetails(
            customer,
            store,
            porder.getTotalPrice(),
            LocalDateTime.now());

        return orderDetailsRepo.save(order);
    }

    private List<OrderItem> createOrderItems(OrderDetails order, List<PurchaseProductDTO> purchaseProductDTOs) {
        List<OrderItem> orderItems = new ArrayList<>();

        for (PurchaseProductDTO productDTO : purchaseProductDTOs) {
            Product product = productRepo.findById(productDTO.getId())
                .orElseThrow(() -> new EntityNotFoundException("product not found. id: " + productDTO.getId()));

            OrderItem orderItem = new OrderItem(
                order,
                product,
                productDTO.getQuantity(),
                productDTO.getPrice());

            orderItems.add(orderItem);
            orderItemRepo.save(orderItem);
        }

        return orderItems;
    }

    public void updateInventory(PlaceOrderRequestDTO porder) {
        Store store = findStoreById(porder.getStoreId());
        List<PurchaseProductDTO> purchasedProducts = porder.getPurchaseProduct();

        for (PurchaseProductDTO prods : purchasedProducts) {
            Product product = productRepo.findById(prods.getId())
                .orElseThrow(() -> new EntityNotFoundException("product not found. id: " + prods.getId()));

            Inventory inventory = inventoryRepo.findByProductIdandStoreId(product.getId(), store.getId());
            if (inventory == null) {
                throw new EntityNotFoundException("Inventory not found for product id: " + product.getId() + " in store id: " + store.getId());
            }

            int currentStock = inventory.getStockLevel();
            if (currentStock < prods.getQuantity()) {
                throw new IllegalArgumentException("only " + currentStock + " of " + product.getName() + " remains");
            }

            inventory.setStockLevel(currentStock - prods.getQuantity());
            inventoryRepo.save(inventory);
        }
    }

   
}
