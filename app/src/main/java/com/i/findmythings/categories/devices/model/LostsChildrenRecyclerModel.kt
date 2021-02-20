package com.i.findmythings.categories.devices.model


import java.io.Serializable


class LostsChildrenRecyclerModel : Serializable{

    public lateinit var name_of_children : String
    public lateinit var children_nationa_id : String
    public lateinit var place_of_hidden : String
    public  lateinit var date_of_hidden : String
    public lateinit var posting_date : String
    public lateinit var children_pirthday : String
    public lateinit var type : String
    public lateinit var lost_logo_image_uri : String
    public lateinit var publisher_id : String
    public lateinit var category : String

    constructor(){}

     constructor(name_of_children : String,children_nationa_id : String,place_of_hidden : String
     ,date_of_hidden : String, posting_date : String,children_pirthday : String,type : String
     ,lost_logo_image_uri : String, publisher_id : String,category : String) : this(){

      this.category = category
      this.children_nationa_id = children_nationa_id
      this.children_pirthday = children_pirthday
      this.date_of_hidden = date_of_hidden
      this.lost_logo_image_uri = lost_logo_image_uri
      this.name_of_children = name_of_children
      this.place_of_hidden = place_of_hidden
      this.posting_date = posting_date
      this.publisher_id = publisher_id
      this.type = type

     }

}