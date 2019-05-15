/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.demo.data_transfer_objects;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author STEFAN94
 */

public class WatchDTO {
    
    private Long id;
    private boolean active = true;
    private String brand;
    private String category;
    private String name;
    private String description;
    private int inStockNumber;
    private String dailColor;
    private double caseSize;
    private String caseShape;
    private String caseMaterial;
    private double caseThikness;
    private String crystalType;
    private String mechanism;
    private double strapWidth;
    private String waterResistance;
    private double shippingWeight;
    private double listPrice;
    private double ourPrice;
    private List<WatchToCartItemDTO> watchToCardItemList;
    private Set<UserCommentDTO> watchComments = new HashSet<>();
    private List<UserRatingDTO> userRating;
    private MultipartFile[] albums;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getInStockNumber() {
        return inStockNumber;
    }

    public void setInStockNumber(int inStockNumber) {
        this.inStockNumber = inStockNumber;
    }

    public String getDailColor() {
        return dailColor;
    }

    public void setDailColor(String dailColor) {
        this.dailColor = dailColor;
    }

    public double getCaseSize() {
        return caseSize;
    }

    public void setCaseSize(double caseSize) {
        this.caseSize = caseSize;
    }

    public String getCaseShape() {
        return caseShape;
    }

    public void setCaseShape(String caseShape) {
        this.caseShape = caseShape;
    }

    public String getCaseMaterial() {
        return caseMaterial;
    }

    public void setCaseMaterial(String caseMaterial) {
        this.caseMaterial = caseMaterial;
    }

    public double getCaseThikness() {
        return caseThikness;
    }

    public void setCaseThikness(double caseThikness) {
        this.caseThikness = caseThikness;
    }

    public String getCrystalType() {
        return crystalType;
    }

    public void setCrystalType(String crystalType) {
        this.crystalType = crystalType;
    }

    public String getMechanism() {
        return mechanism;
    }

    public void setMechanism(String mechanism) {
        this.mechanism = mechanism;
    }

    public double getStrapWidth() {
        return strapWidth;
    }

    public void setStrapWidth(double strapWidth) {
        this.strapWidth = strapWidth;
    }

    public String getWaterResistance() {
        return waterResistance;
    }

    public void setWaterResistance(String waterResistance) {
        this.waterResistance = waterResistance;
    }

    public double getShippingWeight() {
        return shippingWeight;
    }

    public void setShippingWeight(double shippingWeight) {
        this.shippingWeight = shippingWeight;
    }

    public double getListPrice() {
        return listPrice;
    }

    public void setListPrice(double listPrice) {
        this.listPrice = listPrice;
    }

    public double getOurPrice() {
        return ourPrice;
    }

    public void setOurPrice(double ourPrice) {
        this.ourPrice = ourPrice;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Watch{" + "id=" + id + ", active=" + active + ", brand=" + brand + ", category=" + category + ", name=" + name + ", description=" + description + ", inStockNumber=" + inStockNumber + ", dailColor=" + dailColor + ", caseSize=" + caseSize + ", caseShape=" + caseShape + ", caseMaterial=" + caseMaterial + ", caseThikness=" + caseThikness + ", crystalType=" + crystalType + ", mechanism=" + mechanism + ", strapWidth=" + strapWidth + ", waterResistance=" + waterResistance + ", shippingWeight=" + shippingWeight + ", listPrice=" + listPrice + ", ourPrice=" + ourPrice + '}';
    }

    public List<WatchToCartItemDTO> getWatchToCardItemList() {
        return watchToCardItemList;
    }

    public void setWatchToCardItemList(List<WatchToCartItemDTO> watchToCardItemList) {
        this.watchToCardItemList = watchToCardItemList;
    }

    public Set<UserCommentDTO> getWatchComments() {
        return watchComments;
    }

    public void setWatchComments(Set<UserCommentDTO> watchComments) {
        this.watchComments = watchComments;
    }

    public List<UserRatingDTO> getUserRating() {
        return userRating;
    }

    public void setUserRating(List<UserRatingDTO> userRating) {
        this.userRating = userRating;
    }

    public MultipartFile[] getAlbums() {
        return albums;
    }

    public void setAlbums(MultipartFile[] albums) {
        this.albums = albums;
    }

}
