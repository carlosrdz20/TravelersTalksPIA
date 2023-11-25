package com.example.travelers_talks.data

class CountryData {
}
class Country(var idCountry:Int? = null, var nameCountry:String? = null, var imageCountry:ByteArray? = null)

data class CountryId(var id: Int, var name: String){
    override fun toString(): String{
        return name
    }
}