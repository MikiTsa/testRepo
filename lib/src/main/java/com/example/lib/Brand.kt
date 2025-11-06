package com.example.lib

data class Brand(
    val name: String,
    val country: String,
    val foundedYear: Int
) : Comparable<Brand> {

    override fun compareTo(other: Brand): Int {
        return name.compareTo(other.name)
    }

    override fun toString(): String {
        return "$name (Founded: $foundedYear, Country: $country)"
    }
}