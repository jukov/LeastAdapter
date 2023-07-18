package info.jukov.leastadapter_sample.data

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