/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.demo.repository;

import com.example.demo.domain.ShoppingCart;
import org.springframework.data.repository.CrudRepository;

/**
 *
 * @author STEFAN94
 */
public interface ShoppingCartRepository extends CrudRepository<ShoppingCart, Long>{
    
}
