import kotlin.math.min

private enum class HandType2(val rank: Int) {
    FIVE_OF_A_KIND(1),
    FOUR_OF_A_KIND(2),
    FULL_HOUSE(3),
    THREE_OF_A_KIND(4),
    TWO_PAIR(5),
    ONE_PAIR(6),
    HIGH_CARD(7)
}


fun main() {
    fun fromSymbolToValue(ch: Char): Int {
        return if (ch.isDigit()) {
            ch.code - '0'.code;
        } else if (ch == 'T') {
            10
        } else if (ch == 'J') {
            0
        } else if (ch == 'Q') {
            12
        } else if (ch == 'K') {
            13
        } else {
            14
        }
    }

    data class Hand(val hand: String, val bid: Int) {

        fun getType(): HandType2 {
            val frequency = Array<Int>(15) { 0 }
            for (ch in hand) {
                frequency[fromSymbolToValue(ch)]++
            }

            var maximum = 0;
            var secondMaximum = 0;
            for (i in 1 until 15) {
                if (frequency[i] > maximum) {
                    secondMaximum = maximum
                    maximum = frequency[i]
                }
                else if (frequency[i] > secondMaximum) {
                    secondMaximum = frequency[i]
                }
            }
            
            maximum += frequency[0]

            return if (maximum == 5) {
                HandType2.FIVE_OF_A_KIND
            }
            else if (maximum == 4) {
                HandType2.FOUR_OF_A_KIND
            }
            else if (maximum == 3 && secondMaximum == 2) {
                HandType2.FULL_HOUSE
            }
            else if (maximum == 3) {
                HandType2.THREE_OF_A_KIND
            }
            else if (maximum == 2 && secondMaximum == 2) {
                HandType2.TWO_PAIR
            }
            else if (maximum == 2) {
                HandType2.ONE_PAIR
            }
            else {
                HandType2.HIGH_CARD
            }
        }

        fun compare(other: Hand): Int {
            val type = getType()
            val otherType = other.getType()

            if (type.rank < otherType.rank) {
                return 1
            }
            else if (type.rank > otherType.rank) {
                return -1
            }
            else {
                for (i in 0 until 5) {
                    if (fromSymbolToValue(hand[i]) > fromSymbolToValue(other.hand[i])) {
                        return 1
                    }
                    else if (fromSymbolToValue(hand[i]) < fromSymbolToValue(other.hand[i])) {
                        return -1
                    }
                }
                return 0
            }
        }
    }

    fun part2(input: List<String>): Int {
        val hands = arrayListOf<Hand>();
        for (handString in input) {
            val (cards, bidString) = handString.trim().split(" ")
            hands.add(Hand(cards, bidString.toInt()))
        }

        val rankComparator = { o1: Hand, o2: Hand -> o1.compare(o2) }
        hands.sortWith(rankComparator)

        var result = 0
        for ((index, hand) in hands.withIndex()) {
            result += hand.bid * (index + 1)
        }
        return result
    }
    
    // test if implementation meets criteria from the description, like:
    val testInput = readInput("input/day07/test")
    //check(part1(testInput) == 6440)
    check(part2(testInput) == 5905)

    val input = readInput("input/day07/input")
    part2(input).println()
}
