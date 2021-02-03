package com.i.findmythings.home.model

class MainCategoryModel() {
    lateinit var categoryName: String
     var CategoryImage : Int = 0

    constructor(categoryName: String, CategoryImage : Int) : this() {
        this.categoryName =categoryName
        this.CategoryImage = CategoryImage
    }

}