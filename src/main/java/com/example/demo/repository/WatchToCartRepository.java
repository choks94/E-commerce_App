/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.demo.repository;

import com.example.demo.domain.CartItem;
import com.example.demo.domain.WatchToCartItem;
import javax.transaction.Transactional;
import org.springframework.data.repository.CrudRepository;

/**
 *
 * @author STEFAN94
 */

@Transactional
public interface WatchToCartRepository extends CrudRepository<WatchToCartItem, Long>{

    public void deleteByCartItem(CartItem cartItem);
    
}
