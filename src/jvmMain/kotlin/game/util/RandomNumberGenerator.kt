package game.util

interface RandomNumberGenerator {
    fun nextDouble(): Double
}

class RandomNumberGeneratorImpl : RandomNumberGenerator {

    override fun nextDouble() = Math.random()
}
