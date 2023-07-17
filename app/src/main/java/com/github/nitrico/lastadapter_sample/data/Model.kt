package com.github.nitrico.lastadapter_sample.data

sealed class Model {

    data class Item(
        val id: Int,
        val text: String
    ) : Model()

    data class Header(
        val id: Int,
        val text: String
    ) : Model()
}