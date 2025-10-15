package com.example.lib

class Manufacturer(
    val name: String,
    val country: String,
    val foundedYear: Int
) {
    override fun toString(): String {
        return " (name='$name', country='$country', foundedYear=$foundedYear)"
    }
}