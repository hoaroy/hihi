package com.example.Sachpee.Model;

import java.util.HashMap;
import java.util.Map;

public class Product {
    private int codeProduct,codeCategory,priceProduct;
        private String nameProduct,imgProduct,userPartner;

    public Product() {
    }

    public int getCodeProduct() {
        return codeProduct;
    }

    public void setCodeProduct(int codeProduct) {
        this.codeProduct = codeProduct;
    }

    public int getCodeCategory() {
        return codeCategory;
    }

    public void setCodeCategory(int codeCategory) {
        this.codeCategory = codeCategory;
    }


    public String getUserPartner() {
        return userPartner;
    }

    public void setUserPartner(String userPartner) {
        this.userPartner = userPartner;
    }

    public int getPriceProduct() {
        return priceProduct;
    }

    public void setPriceProduct(int priceProduct) {
        this.priceProduct = priceProduct;
    }

    public String getNameProduct() {
        return nameProduct;
    }

    public void setNameProduct(String nameeProduct) {
        this.nameProduct = nameeProduct;
    }

    public String getImgProduct() {
        return imgProduct;
    }

    public void setImgProduct(String imgProduct) {
        this.imgProduct = imgProduct;
    }
    public Map<String,Object> toMap(){
        HashMap<String,Object> result = new HashMap<>();
        result.put("codeCategory",getCodeCategory());
        result.put("userPartner",getUserPartner());
        result.put("nameProduct",getNameProduct());
        result.put("priceProduct",getPriceProduct());
        result.put("imgProduct",getImgProduct());
        return result;
    }
}
