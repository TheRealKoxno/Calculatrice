package com.koxno.calculatrice.domain

import org.junit.Assert
import org.junit.Test

class CalculateExpressionKtTest {

    @Test
    fun testPlus() {
        val expression = "2+2"
        val result = "4"

        Assert.assertEquals(result, calculateExpression(expression))
    }

    @Test
    fun testSubtraction() {
        val expression = "4-2"
        val result = "2"

        Assert.assertEquals(result, calculateExpression(expression))
    }

    @Test
    fun testExpression() {
        val expression = "4-2*2"
        val result = "0"

        Assert.assertEquals(result, calculateExpression(expression))
    }

    @Test
    fun test() {
        val expression = "999999990/10"
        val result = "99999999"

        Assert.assertEquals(result, calculateExpression(expression))
    }

    @Test
    fun testInput() {
        testCalculation("", "")
        testCalculation("2", "2")
        testCalculation("2+", "2")
        testCalculation("2+2", "4")
    }

    private fun testCalculation(
        expression: String,
        result: String,
    ) {
        Assert.assertEquals(result, calculateExpression(expression))
    }
}