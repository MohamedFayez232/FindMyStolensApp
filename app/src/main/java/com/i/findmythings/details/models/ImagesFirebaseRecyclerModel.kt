package com.i.findmythings.details.models

class imagesFirebaseRecyclerModel {

    lateinit var image_uri : String

    constructor(){}

    constructor( imageUri : String){
        this.image_uri = imageUri

    }
}