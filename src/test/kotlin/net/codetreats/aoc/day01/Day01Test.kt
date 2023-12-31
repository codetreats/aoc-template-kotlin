package net.codetreats.aoc.day01

import net.codetreats.aoc.util.Level
import net.codetreats.aoc.util.Logger
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class Day01Test {
    private lateinit var classUnderTest : Day01

    @BeforeEach
    fun setup() {
        Logger.level = Level.ERROR
        classUnderTest = Day01()
    }

    @Test
    fun testPart01() {
        assertEquals("", classUnderTest.run1(false))
    }

    @Test
    fun testPart01_dummyData() {
        assertEquals("", classUnderTest.run1(true))
    }

    @Test
    fun testPart02() {
        assertEquals("", classUnderTest.run2(false))
    }

    @Test
    fun testPart02_dummyData() {
        assertEquals("", classUnderTest.run2(true))
    }
}
