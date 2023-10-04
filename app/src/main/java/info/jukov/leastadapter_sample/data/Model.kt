package info.jukov.leastadapter_sample.data

import info.jukov.leastadapter.StableId

sealed class Model {

    data class Item(
        val id: Int,
        val text: String
    ) : Model()

    data class Header(
        val id: Int,
        val text: String
    ) : Model(), StableId {
        override val stableId: Long = id.toLong()
    }
}