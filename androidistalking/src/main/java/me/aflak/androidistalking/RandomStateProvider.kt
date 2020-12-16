package me.aflak.androidistalking

import kotlin.random.Random

object RandomStateProvider : StateProvider {
    private const val STATE_COUNT = 50

    override fun getStates(size: Int): List<List<Float>> {
        return List(STATE_COUNT) {
            val states = MutableList(size / 2) {
                Random.nextFloat()
            }
            val reversed = states.reversed()
            if (size % 2 != 0) {
                states.add(Random.nextFloat())
            }
            states.addAll(reversed)
            states
        }
    }
}
