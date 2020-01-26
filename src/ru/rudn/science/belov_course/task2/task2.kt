package ru.rudn.science.belov_course.task2

import ru.rudn.science.belov_course.task1.f
import java.io.File
import kotlin.math.abs
import kotlin.math.pow

fun main(args: Array<String>) {
    val segmentStart = 0.0
    val segmentEnd = 1.0
    val nodes = listOf(10, 20, 40, 80, 160, 320, 640, 1280)
    val exactIntegral = Math.E - 1

    calculate(segmentStart, segmentEnd, nodes, exactIntegral, ::f, ::trapeziumRule)
    calculate(segmentStart, segmentEnd, nodes, exactIntegral, ::f, ::midpointRule)
}

fun f(x: Double): Double = Math.exp(x)

fun trapeziumRule(range: IntRange, xPoint: Double, h: Double, f: (x: Double) -> Double): Double {
    var integral = 0.0
    var x = xPoint
    range.forEach { integral += (h / 2) * (f(x + h) + f(x)); x += h }
    return integral
}

fun midpointRule(range: IntRange, xPoint: Double, h: Double, f: (x: Double) -> Double): Double {
    var integral = 0.0
    var x = xPoint
    range.forEach { integral += h * f(x + h / 2); x += h }
    return integral
}

fun richardson(h: Double,
               t: Double,
               n: Int,
               startPoint: Double,
               endPoint: Double,
               range: IntRange,
               integratedFunction: (x: Double) -> Double,
               A: (range: IntRange, xPoint: Double, h: Double, f: (x: Double) -> Double) -> Double): Double {
    val halfRange = 1..((endPoint - startPoint) / (h / t)).toInt() - 1
    var coef = t.pow(n)
    var halfInt = A(halfRange, startPoint, h / t, integratedFunction)
    var int = A(range, startPoint, h, integratedFunction)
    return (coef * halfInt - int) / (coef - 1)
}

fun calculate(startPoint: Double,
              endPoint: Double,
              nodes: List<Int>,
              exactIntegral: Double,
              integratedFunction: (x: Double) -> Double,
              integrateNumerically: (range: IntRange, xPoint: Double, h: Double, f: (x: Double) -> Double) -> Double) {
    val file = "task2_data/${integrateNumerically.javaClass.simpleName}.txt"

    File(file).printWriter().use { out ->
        for (N in nodes) {
            val h = (endPoint - startPoint) / N
            val range = 1 until N - 1
            var integral = integrateNumerically(range, startPoint, h, integratedFunction)
            var DIntegral = richardson(h, 2.0, 2, startPoint, endPoint, range, integratedFunction, integrateNumerically)
            val D = abs(exactIntegral - integral)
            val DRich = abs(exactIntegral - DIntegral)
            out.println("${N};${exactIntegral};${integral};${DIntegral};${D};${DRich};${Math.log(N.toDouble())};${Math.log(D)};${Math.log(DRich)}")
        }
    }
}
