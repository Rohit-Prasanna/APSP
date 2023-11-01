package com.example.apap_admin;

import android.graphics.Bitmap;

import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

public class MyViewModel extends ViewModel {
    private ArrayList<Bitmap> bitmapArray;

    public ArrayList<Bitmap> getBitmapArray() {
        return bitmapArray;
    }

    public void setBitmapArray(ArrayList<Bitmap> bitmapArray) {
        this.bitmapArray = bitmapArray;
    }
}
