import kotlin.math.min

private fun part1(input: List<String>): Int {
    val (_, timeString) = input[0].split(':')
    val (_, distanceString) = input[1].split(':');
    
    val timeList = timeString.split(' ')
        .asSequence()
        .filter { it.isNotBlank() }
        .map { it.toInt() }
        .toList();
    
    val distanceList = distanceString.split(' ')
        .asSequence()
        .filter { it.isNotBlank() }
        .map { it.toInt() }
        .toList();
    
    var res = 1
    for (i in timeList.indices) {
        var posib = 0
        for (velocity in 0 .. timeList[i]) {
            val distance = velocity * (timeList[i] - velocity)
            
            if (distance > distanceList[i]) {
                posib++
            }
        }
        
        res *= posib
    }
    
    return res
}

private fun part2(input: List<String>): Int {
    val (_, timeString) = input[0].split(':')
    val (_, distanceString) = input[1].split(':');

    val time = timeString.split(' ')
        .asSequence()
        .filter { it.isNotBlank() }
        .fold("") { acc, s ->
            acc + s
        }
        .toInt();

    val distance = distanceString.split(' ')
        .asSequence()
        .filter { it.isNotBlank() }
        .fold("") { acc, s ->
            acc + s
        }
        .toLong();

    var res = 0
    for (velocity in 0 .. time) {
        val crDistance = velocity.toLong() * (time - velocity)

        if (crDistance > distance) {
            res++
        }
    }

    return res
}

fun main() {
    // test if implementation meets criteria from the description, like:
    val testInput = readInput("input/day06/test")
    check(part1(testInput) == 288)
    check(part2(testInput) == 71503)

    val input = readInput("input/day06/input")
    part1(input).println()
    part2(input).println()
}
