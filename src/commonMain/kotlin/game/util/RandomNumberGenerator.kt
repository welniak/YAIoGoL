package game.util

import kotlin.random.Random

interface RandomNumberGenerator {
    fun nextDouble(): Double
}

class RandomNumberGeneratorImpl : RandomNumberGenerator {

    override fun nextDouble() = Random.nextDouble()
}
