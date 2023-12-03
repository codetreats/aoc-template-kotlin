package net.codetreats.aoc.day03

import net.codetreats.aoc.Day
import net.codetreats.aoc.common.Board
import net.codetreats.aoc.common.Point
import net.codetreats.aoc.util.Logger

class Day03 : Day<Board<Char>>(3) {
    override val logger: Logger = Logger.forDay(dayOfMonth)

    override val useDummy = false

    companion object {
        private const val NO_SYMBOL = '.'
        private const val GEAR_SYMBOL = '*'
    }

    override fun convert(input: List<String>): Board<Char> {
        val height = input.size
        val width = input[0].length

        require(input.all { line -> line.length == width })

        val result = Board(width, height, NO_SYMBOL)

        input.forEachIndexed { rowIndex, line ->
            line.forEachIndexed { columnIndex, char ->
                result.set(rowIndex, columnIndex, char)
            }
        }

        return result
    }

    override fun run1(data: Board<Char>): String {
        val numbersInBoard = extractNumbers(data)

        val filteredNumbersInBoard = numbersInBoard.filter { numberInBoard ->
            val neighborPoints = numberInBoard.enumerateNeighbors(data)

            neighborPoints.any {
                val char = data.get(it.x, it.y).value
                !char.isDigit() && char != NO_SYMBOL
            }
        }

        return filteredNumbersInBoard.sumOf {
            it.toInt(data)
        }.toString()
    }

    override fun run2(data: Board<Char>): String {
        val numbersInBoard = extractNumbers(data)
        val gearsInBoard = extractGears(data)

        val numbersConnectedByGears = gearsInBoard.map { gear ->
            numbersInBoard.filter { numberInBoard ->
                val neighborPoints = numberInBoard.enumerateNeighbors(data).map { Point.from(it.x, it.y) }

                gear in neighborPoints
            }
        }.filter { it.size == 2 }

        return numbersConnectedByGears.sumOf { (first, second) ->
            first.toInt(data) * second.toInt(data)
        }.toString()
    }

    private fun extractNumbers(
        data: Board<Char>,
    ): List<NumberInBoard> {
        val numbersInBoard = mutableListOf<NumberInBoard>()
        var columnIndexNumberStart: Int? = null

        for (rowIndex in 0 until data.height) {
            for (columnIndex in 0 until data.width) {
                val currentChar = data.get(rowIndex, columnIndex).value
                if (!currentChar.isDigit()) {
                    if (columnIndexNumberStart != null) {
                        numbersInBoard.add(NumberInBoard(rowIndex, columnIndexNumberStart, columnIndex - 1))
                        columnIndexNumberStart = null
                    }
                } else {
                    if (columnIndexNumberStart == null) {
                        columnIndexNumberStart = columnIndex
                    } else {
                        if (columnIndex == data.width - 1) {
                            numbersInBoard.add(NumberInBoard(rowIndex, columnIndexNumberStart, columnIndex))
                            columnIndexNumberStart = null
                        }
                    }
                }
            }
            assert(columnIndexNumberStart == null)
        }

        return numbersInBoard
    }

    private fun extractGears(
        data: Board<Char>,
    ): List<Point> {
        val gearsInBoard = mutableListOf<Point>()

        for (rowIndex in 0 until data.height) {
            for (columnIndex in 0 until data.width) {
                val currentChar = data.get(rowIndex, columnIndex).value
                if (currentChar == GEAR_SYMBOL) {
                    gearsInBoard.add(Point.from(rowIndex, columnIndex))
                }
            }
        }

        return gearsInBoard
    }

    data class NumberInBoard(val row: Int, val columnFrom: Int, val columnTo: Int) {
        fun enumeratePoints(): List<Point> = buildList {
            for (column in columnFrom..columnTo) {
                add(Point.from(row, column))
            }
        }

        fun enumerateNeighbors(board: Board<*>): List<Point> {
            val ownPoints = enumeratePoints()
            val neighborPoints = ownPoints.flatMap {
                board.neighbors(it.x, it.y, withDiag = true)
            }.filter { !ownPoints.contains(it) }.filter {
                it.x in 0 until board.width && it.y in 0 until board.height
            }
            return neighborPoints
        }

        fun toInt(board: Board<Char>): Int {
            return enumeratePoints().map { point ->
                board.get(point.x, point.y).value
            }.joinToString("").toInt()
        }
    }
}