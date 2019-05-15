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
public class UserCommentDTO {

    private long id;
    private String comment;
    private Date date;
    private UserDTO user;
    private WatchDTO watch;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

}
