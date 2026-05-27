package com.project.code.Model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "customer")
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotNull(message = "Customer name cannot be null")
    @NotBlank(message = "Customer name cannot be empty")
    private String name;

    @NotNull(message = "Customer email cannot be null")
    @NotBlank(message = "Customer email cannot be empty")
    private String email;

    @NotNull(message = "Customer Phone number cannot be null")
    @NotBlank(message = "Customer Phone number cannot be empty")
    private String phone;

    @OneToMany(mappedBy = "customer", fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<OrderDetails> orders;

    public Customer() {}

    public Customer(String name, String email, String phone){
        this.name = name;
        this.email = email;
        this.phone = phone;
    }


    public long getId() { return this.id;}
    public String getName() { return this.name; }
    public String getEmail() { return this.email; }
    public String getPhone() { return this.phone; }
    public List<OrderDetails> getOrders() { return this.orders; }

    public void setId(long id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setEmail(String email) {this.email = email; }
    public void setPhone(String phone) {this.phone = phone; }
    public void setOrders(List<OrderDetails> orders) {
        this.orders = orders;
    }

}

