/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.demo.repository;

import com.example.demo.domain.CartItem;
import com.example.demo.domain.Order;
import com.example.demo.domain.ShoppingCart;
import java.util.List;
import javax.transaction.Transactional;
import org.springframework.data.repository.CrudRepository;

/**
 *
 * @author STEFAN94
 */

@Transactional
public interface CartItemRepository extends CrudRepository<CartItem, Long>{
    
    List<CartItem> findByShoppingCart(ShoppingCart shoppingCart);

    public List<CartItem> findByOrder(Order order);
     
}
