/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.demo.data_transfer_objects;

import java.util.Date;

/**
 *
 * @author STEFAN94
 */

public class UserRatingDTO {

    private long id;
    private int rating;
    private Date date;
    private UserDTO user;
    private WatchDTO watch;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    public WatchDTO getWatch() {
        return watch;
    }

    public void setWatch(WatchDTO watch) {
        this.watch = watch;
    }
    
    
    
}
