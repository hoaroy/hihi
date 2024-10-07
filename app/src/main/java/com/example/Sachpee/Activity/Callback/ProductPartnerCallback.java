package com.example.Sachpee.Activity.Callback;

import com.example.Sachpee.Model.Product;

import java.util.List;

public interface ProductPartnerCallback {
    void onProductsLoaded(List<Product> products);
}
