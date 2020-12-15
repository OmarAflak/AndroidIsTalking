package me.aflak.libraries

import kotlin.math.PI
import kotlin.math.sin

object SinStateProvider : StateProvider {
    private const val STATE_COUNT = 10

    override fun getStates(size: Int): List<List<Float>> {
        val dx = 2 * PI / STATE_COUNT
        return List(STATE_COUNT) { i ->
            List(size) { j ->
                sin(dx * (i + j)).toFloat()
            }
        }
    }
}
