import kotlin.math.min
import kotlin.math.max

private data class Range(
    val startInitial: Long,
    val endInitial: Long,
    val startMapped: Long,
    val endMapped: Long
)

private fun mergeRanges(range1: Range, range2: Range): Range {
    val commonRangeLeft = max(range1.startMapped, range2.startInitial)
    val commonRangeRight = min(range1.endMapped, range2.endInitial)
    
    val startInitial = commonRangeLeft - range1.startMapped + range1.startInitial
    val endInitial = startInitial + (commonRangeRight - commonRangeLeft)
    val startMapped = commonRangeLeft - range2.startInitial + range2.startMapped
    val endMapped = startMapped + (commonRangeRight - commonRangeLeft)
    
    return Range(startInitial, endInitial, startMapped, endMapped)
}


private class RangeMap {
    private var rangeList: MutableList<Range> = ArrayList()
    
    fun add(range: Range) {
        rangeList.add(range)
    }
    
    fun sort() {
        rangeList.sortWith(compareBy {
            it.startInitial
        })
    }
    
    fun binarySearch(value: Long): Long {
        val maxPower = 20
        var res = 0
        for (i in maxPower downTo  0) {
            if ((res xor (1 shl i) < rangeList.size) &&
                rangeList[res xor (1 shl i)].startInitial <= value) {
                res = res xor (1 shl i)
            }
        }

        if (value < rangeList[res].startInitial || value > rangeList[res].endInitial) {
            return value
        }

        return (value - rangeList[res].startInitial) + rangeList[res].startMapped
    }
    
    fun getPosition(value: Long): Int {
        val maxPower = 20
        var res = 0
        for (i in maxPower downTo  0) {
            if ((res xor (1 shl i) < rangeList.size) &&
                rangeList[res xor (1 shl i)].startInitial <= value) {
                res = res xor (1 shl i)
            }
        }
        
        return res
    }
    
    fun getMinimumInInterval(left: Long, right: Long): Long {
        val leftPos = getPosition(left)
        val rightPos = getPosition(right)
        
        var res = (left - rangeList[leftPos].startInitial) + rangeList[leftPos].startMapped
        for (i in leftPos + 1..rightPos) {
            res = min(res, rangeList[i].startMapped)
        }
        
        return res
    }
    
    fun mergeRangeMap(other: RangeMap): RangeMap {
        val result = RangeMap()
        
        rangeList.sortWith(compareBy{
            it.startMapped
        })
        other.sort()
        
        var j = 0
        for (i in 0 until rangeList.size) {
            while (j < other.rangeList.size && other.rangeList[j].endInitial < rangeList[i].startMapped) {
                j++
            }
            
            while (j < other.rangeList.size && other.rangeList[j].startInitial <= rangeList[i].endMapped) {
                val range = mergeRanges(rangeList[i], other.rangeList[j])
                result.add(range)
                j++
            }
            j--
        }
        
        return result
    }
    
    fun fillEmpty() {
        val result = ArrayList<Range>()
        
        sort()
        var prv = -1L;
        for (range in rangeList) {
            if (prv < range.startInitial - 1) {
                result.add(Range(prv + 1, range.startInitial - 1, prv + 1, range.startInitial - 1))
            }
            
            result.add(range)
            prv = range.endInitial
        }
        
        result.add(Range(prv + 1, Long.MAX_VALUE, prv + 1, Long.MAX_VALUE))
        
        rangeList = result
    }
}

private fun readMap(map: RangeMap, index: Long, input: List<String>): Long {
    var index = index.toInt()
    index++
    
    while(index < input.size && input[index].isNotBlank()) {
        val (startMappingString, startRangeString, lengthString) = input[index].split(" ")
        val startRange = startRangeString.toLong()
        val startMapping = startMappingString.toLong()
        val length = lengthString.toLong()
        
        map.add(Range(startRange, startRange + length - 1, startMapping, startMapping + length - 1))
        index++
    }
    
    return index + 1L
}

private fun part1(input: List<String>): Long {
    val (prefix, seeds) = input[0].split(':')
    
    val seedList = seeds.trim()
        .split(" ")
        .asSequence()
        .filter(String::isNotEmpty)
        .map(String::toLong)
        .toList();
    
    val seedToSoil = RangeMap()
    val soilToFertilizer = RangeMap()
    val fertilizerToWater = RangeMap()
    val waterToLight = RangeMap()
    val lightToTemperature = RangeMap()
    val temperatureToHumidity = RangeMap()
    val humidityToLocation = RangeMap()
    
    var index = 2L
    index = readMap(seedToSoil, index, input)
    index = readMap(soilToFertilizer, index, input)
    index = readMap(fertilizerToWater, index, input)
    index = readMap(waterToLight, index, input)
    index = readMap(lightToTemperature, index, input)
    index = readMap(temperatureToHumidity, index, input)
    readMap(humidityToLocation, index, input)
    
    seedToSoil.sort()
    soilToFertilizer.sort()
    fertilizerToWater.sort()
    waterToLight.sort()
    lightToTemperature.sort()
    temperatureToHumidity.sort()
    humidityToLocation.sort()
    
    var res = Long.MAX_VALUE
    for (seed in seedList) {
        var loc = seed
        loc = seedToSoil.binarySearch(loc)
        loc = soilToFertilizer.binarySearch(loc)
        loc = fertilizerToWater.binarySearch(loc)
        loc = waterToLight.binarySearch(loc)
        loc = lightToTemperature.binarySearch(loc)
        loc = temperatureToHumidity.binarySearch(loc)
        loc = humidityToLocation.binarySearch(loc)
        
        res = min(res, loc)
    }
    
    return res
}

private fun part2(input: List<String>): Long {
    val (prefix, seeds) = input[0].split(':')

    val seedList = seeds.trim()
        .split(" ")
        .asSequence()
        .filter(String::isNotEmpty)
        .map(String::toLong)
        .toList();

    val seedToSoil = RangeMap()
    val soilToFertilizer = RangeMap()
    val fertilizerToWater = RangeMap()
    val waterToLight = RangeMap()
    val lightToTemperature = RangeMap()
    val temperatureToHumidity = RangeMap()
    val humidityToLocation = RangeMap()

    var index = 2L
    index = readMap(seedToSoil, index, input)
    index = readMap(soilToFertilizer, index, input)
    index = readMap(fertilizerToWater, index, input)
    index = readMap(waterToLight, index, input)
    index = readMap(lightToTemperature, index, input)
    index = readMap(temperatureToHumidity, index, input)
    readMap(humidityToLocation, index, input)
    
    seedToSoil.fillEmpty()
    soilToFertilizer.fillEmpty()
    fertilizerToWater.fillEmpty()
    waterToLight.fillEmpty()
    lightToTemperature.fillEmpty()
    temperatureToHumidity.fillEmpty()
    humidityToLocation.fillEmpty()

    val seedToFertilizer = seedToSoil.mergeRangeMap(soilToFertilizer)
    val seedToWater = seedToFertilizer.mergeRangeMap(fertilizerToWater)
    val seedToLight = seedToWater.mergeRangeMap(waterToLight)
    val seedToTemperature = seedToLight.mergeRangeMap(lightToTemperature)
    val seedToHumidity = seedToTemperature.mergeRangeMap(temperatureToHumidity)
    val seedToLocation = seedToHumidity.mergeRangeMap(humidityToLocation)

    var res = Long.MAX_VALUE
    seedToLocation.sort()
    for (i in 0 until seedList.size step 2) {
        val loc = seedToLocation.getMinimumInInterval(seedList[i], seedList[i] + seedList[i + 1] - 1)

        res = min(res, loc)
    }

    return res
}

fun main() {
    // test if implementation meets criteria from the description, like:
    val testInput = readInput("input/day05/test")
    check(part1(testInput) == 35L)
    check(part2(testInput) == 46L)

    val input = readInput("input/day05/input")
    part1(input).println()
    part2(input).println()
}
