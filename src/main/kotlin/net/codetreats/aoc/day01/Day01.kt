package net.codetreats.aoc.day01

import net.codetreats.aoc.Day
import net.codetreats.aoc.util.Logger

class Day01 : Day<List<Int>>(1) {
    override val logger: Logger = Logger.forDay(dayOfMonth)

    override val useDummy = false

    override fun convert(input: List<String>): List<Int> {
        throw NotImplementedError()
    }

    override fun convert1(input: List<String>): List<Int> = input.map { line ->
        val firstDigit = line.find { char -> char.isDigit() }!!
        val lastDigit = line.findLast { char -> char.isDigit() }!!

        "${firstDigit}${lastDigit}".toInt()
    }

    override fun run1(data: List<Int>): String = data.sum().toString()

    override fun convert2(input: List<String>): List<Int> = input.map { line ->
        val regex = """(\d|one|two|three|four|five|six|seven|eight|nine)""".toRegex()

        // In order to correctly match overlapping digits like "eighthree" as 83:
        val matches = line.mapIndexed { index, _ ->
            val substring = line.substring(index)
            regex.find(substring)?.value
        }.filterNotNull()

        val firstDigit = spelledOutDigitToNumericDigit(matches.first())
        val lastDigit = spelledOutDigitToNumericDigit(matches.last())

        "${firstDigit}${lastDigit}".toInt()
    }

    private fun spelledOutDigitToNumericDigit(digit: String): String {
        val spelledOutDigits = listOf("one", "two", "three", "four", "five", "six", "seven", "eight", "nine")

        val indexOfInputDigit = spelledOutDigits.indexOf(digit)
        if (indexOfInputDigit == -1) {
            return digit
        } else {
            return "${indexOfInputDigit + 1}"
        }
    }

    override fun run2(data: List<Int>): String = data.sum().toString()
}