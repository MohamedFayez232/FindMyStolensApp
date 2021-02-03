package com.i.findmythings.categories.model

class CategoriesrecyclerModel {
    lateinit var name_of_lost : String
    lateinit var date_of_stolen : String
    lateinit var serial_number_of_lost : String
    lateinit var place_of_stolen : String
    lateinit var posting_date : String
    lateinit var lost_color : String
    lateinit var vin_number : String
    lateinit var lost_logo_image_uri : String
    lateinit var publisher_id : String
    lateinit var category : String

    constructor(){}

    constructor(date_of_stolen : String,serial_number_of_lost : String,place_of_stolen : String
    ,posting_date : String,lost_color : String,vin_number : String,lost_logo_image_uri : String,
                publisher_id : String){

        this.date_of_stolen = date_of_stolen
        this.lost_color = lost_color
        this.lost_logo_image_uri =lost_logo_image_uri
        this.name_of_lost = name_of_lost
        this.place_of_stolen =place_of_stolen
        this.posting_date = posting_date
        this.serial_number_of_lost = serial_number_of_lost
        this.vin_number = vin_number
        this.publisher_id = publisher_id
    }

}