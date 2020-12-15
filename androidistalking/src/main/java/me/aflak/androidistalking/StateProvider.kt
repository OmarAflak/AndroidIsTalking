package me.aflak.libraries

interface StateProvider {
    fun getStates(size: Int): List<List<Float>>
}
