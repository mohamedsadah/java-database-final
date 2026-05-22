package com.project.code.Repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.project.code.Model.Store;

public interface StoreRepository extends JpaRepository<Store, Long>{

    public Store findById(long id);

    @Query("SELECT s FROM Store s WHERE LOWER(s.name) LIKE LOWER(CONCAT('%', :sName, '%'))")
    public List<Store> findBySubName(@Param("sName") String storeName);

   

}
