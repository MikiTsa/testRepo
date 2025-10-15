package com.example.lib

class Brand(
    val name: String,
    val country: String,
    val foundedYear: Int
) {
    override fun toString(): String {
        return "$name (Founded: $foundedYear, Country: $country)"
    }
}