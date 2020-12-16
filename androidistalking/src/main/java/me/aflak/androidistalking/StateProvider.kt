package me.aflak.androidistalking

interface StateProvider {
    fun getStates(size: Int): List<List<Float>>
}
