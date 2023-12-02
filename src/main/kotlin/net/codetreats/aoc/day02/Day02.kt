package net.codetreats.aoc.day02

import net.codetreats.aoc.Day
import net.codetreats.aoc.util.Logger

class Day02 : Day<List<Game>>(2) {
    override val logger: Logger = Logger.forDay(dayOfMonth)

    override val useDummy = false

    override fun convert(input: List<String>): List<Game> = input.map { line ->
        val gameRegex = """^Game\s(\d*):(.*)$""".toRegex()

        val (gameId, roundsString) = gameRegex.find(line)!!.destructured

        val rounds = roundsString.split(";").map { roundString ->
            val roundEntries = roundString.split(",").map { roundEntryString ->
                val roundEntryRegex = """^\s*(\d*)\s*(red|green|blue)\s*$""".toRegex()
                val (amount, color) = roundEntryRegex.find(roundEntryString)!!.destructured

                amount.toInt() to color
            }

            val greenAmount = roundEntries.find { (_, color) -> color == "green" }?.first ?: 0
            val redAmount = roundEntries.find { (_, color) -> color == "red" }?.first ?: 0
            val blueAmount = roundEntries.find { (_, color) -> color == "blue" }?.first ?: 0

            Round(red = redAmount, green = greenAmount, blue = blueAmount)
        }

        Game(rounds, gameId.toInt())
    }

    override fun run1(data: List<Game>): String {
        val totalRed = 12
        val totalGreen = 13
        val totalBlue = 14

        return data.filter { game ->
            game.rounds.all { round ->
                round.red <= totalRed && round.green <= totalGreen && round.blue <= totalBlue
            }
        }.sumOf { it.id }.toString()
    }

    override fun run2(data: List<Game>): String {
        return data.sumOf { game ->
            val atLeastRed = game.rounds.maxOf { round -> round.red }
            val atLeastGreen = game.rounds.maxOf { round -> round.green }
            val atLeastBlue = game.rounds.maxOf { round -> round.blue }

            val power = atLeastRed * atLeastGreen * atLeastBlue

            power
        }.toString()
    }
}

data class Game(val rounds: List<Round>, val id: Int)

data class Round(val red: Int, val green: Int, val blue: Int)
