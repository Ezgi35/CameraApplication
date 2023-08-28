package com.example.cameraapplication;

import android.graphics.Bitmap;

import java.io.File;

public class InputParameters {

    public File getImageName() {
        return imageName;
    }

    public void setImageName(File imageName) {
        this.imageName = imageName;
    }

    public Bitmap getImageData() {
        return imageData;
    }

    public void setImageData(Bitmap imageData) {
        this.imageData = imageData;
    }

    File imageName;
    Bitmap imageData;



}
