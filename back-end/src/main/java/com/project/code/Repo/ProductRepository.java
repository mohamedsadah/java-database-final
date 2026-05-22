package com.project.code.Repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.project.code.Model.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {

    public List<Product> findAll();

    public List<Product> findByCategory(String category);

    @Query("SELECT p FROM Product p WHERE p.price BETWEEN :minPrice AND :maxPrice")
    public List<Product> findByPriceBetween(@Param("minPrice") double minPrice, @Param("maxPrice") double maxPrice);

    public List<Product> findBySku(String sku);

    public Product findByName(String name);

    public Product findById(long id);

    @Query("SELECT i.product FROM Inventory i WHERE i.store.id = :storeId AND LOWER(i.product.name) LIKE LOWER(CONCAT('%', :pName, '%'))")
    public List<Product> findByNameLike(@Param("storeId") long storeId, @Param("pName") String pName);

    @Query("SELECT i.product FROM Inventory i WHERE i.store.id = :storeId AND LOWER(i.product.name) LIKE LOWER(CONCAT('%', :pName, '%')) AND i.product.category = :category ")
    public List<Product> findByNameAndCategory(@Param("storeId") long id, @Param("pName") String pName, @Param("category") String category);

    @Query("SELECT i.product FROM Inventory i WHERE i.store.id = :storeId AND i.product.category = :category")
    public List<Product> findByCategoryAndStoreId(@Param("storeId") long id, @Param("category") String category);

    @Query("SELECT p FROM Product p WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :pName, '%'))")
    public List<Product> findBySubName(@Param("pName") String pName);

    @Query("SELECT i.product FROM Inventory i WHERE i.store.id = :storeId")
    public List<Product> findByStoreId(@Param("storeId") long storeId);

    @Query("SELECT i.product FROM Inventory i WHERE i.store.id = :storeId AND i.product.category = :category")
    public List<Product> findByCategory(@Param("storeId") long id, @Param("category") String cat);

    @Query("SELECT i.product FROM Inventory i WHERE LOWER(i.product.name) LIKE LOWER(CONCAT('%', :pName, '%')) AND i.product.category = :category")
    public List<Product> findBySubNameAndCategory(@Param("pName") String pName, @Param("category") String cat);



}
