package com.isidroid.b21

import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test",  which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    fun twoSum(nums: IntArray, target: Int): IntArray {
        for ((index, number) in nums.withIndex()) {
            val subArray = nums.take(index + 1)
            if (subArray.sum() >= target)
                return (0..index).toList().toIntArray()
        }

        return intArrayOf()
    }

    fun isPalindrome(x: Int): Boolean {
        if (x in 0..10) return false

        val string = "$x".toCharArray()
        val steps = string.size / 2

        for (i in 0 until steps) {
            val rightPosition = string.size - (i + 1)

            if (string[i] != string[rightPosition])
                return false
        }

        return true
    }

    @Test
    fun testTwoSum() {
        val expected = intArrayOf(0, 1)
        val actual = twoSum(intArrayOf(2, 7, 11, 15), 9)

        println("${actual.joinToString()}")
        assertEquals(expected.toList(), actual.toList())
    }

    @Test
    fun testPalindrome() {
        val expected = true
        val actual = isPalindrome(-1234554321)

        println("actual=$actual")
        assertEquals(expected, actual)
    }
}