import kotlin.math.min

private fun part1(input: List<String>): Int {
    var result = 0
    for (line in input) {
        val (prefix, winning, actual) = line.split(":", "|")
        
        val winningNumbers = winning.trim()
            .split(" ")
            .asSequence()
            .filter(String::isNotEmpty)
            .map(String::toInt)
            .toSet()
        
        val winningCountInActual = actual.trim()
            .split(" ")
            .asSequence()
            .filter(String::isNotEmpty)
            .map(String::toInt)
            .filter { winningNumbers.contains(it) }
            .count()
        
        result += (1 shl (winningCountInActual - 1))
    }
    
    return result
}

private fun part2(input: List<String>): Int {
    val copies =  Array(input.size) { 1 }
    var result = 0
    for ((index, line) in input.withIndex()) {
        val (prefix, winning, actual) = line.split(":", "|")

        val winningNumbers = winning.trim()
            .split(" ")
            .asSequence()
            .filter(String::isNotEmpty)
            .map(String::toInt)
            .toSet()

        val winningCountInActual = actual.trim()
            .split(" ")
            .asSequence()
            .filter(String::isNotEmpty)
            .map(String::toInt)
            .filter { winningNumbers.contains(it) }
            .count()

        for (i in (index + 1) until min(index + winningCountInActual + 1, input.size)) {
            copies[i] += copies[index]
        }
        
        result += copies[index]
    }

    return result
}

fun main() {
    // test if implementation meets criteria from the description, like:
    val testInput = readInput("input/day04/test")
    check(part1(testInput) == 13)
    check(part2(testInput) == 30)

    val input = readInput("input/day04/input")
    part1(input).println()
    part2(input).println()
}
