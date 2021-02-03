package com.i.findmythings

class imagesRecyclerModel {

    lateinit var imageUri : String
     lateinit var imageName : String
    constructor(){}

    constructor( imageUri : String,imageName : String){
        this.imageUri = imageUri
        this.imageName = imageName
    }
}