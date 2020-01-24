package ru.rudn.science.belov_course.task3

import java.io.File
import kotlin.math.abs
import kotlin.math.pow
import kotlin.math.sqrt

fun f(x: Double): Double = Math.exp(-x.pow(2))
//fun xi(ksi: Double): Double = ksi / sqrt(1+ksi.pow(2)) // TODO: probably wrong defined xi function
fun xi(ksi: Double): Double = ksi / sqrt(1 - ksi.pow(2))

fun task3() {
    val nodes = listOf(10, 20, 40, 80, 160, 320, 640, 1280)
    val alpha = 0.0
    val beta = 1.0

    val integralExactValue = sqrt(Math.PI) / 2

    val file = "task3_data/task3.txt"
    File(file).printWriter().use { out ->
        for (N in nodes) {

            var integral = 0.0

            var hKsi = (beta - alpha) / N

            for (i in 0..N - 2) {
                val ksiCurrent = alpha + i * hKsi
                val ksiNext = alpha + (i + 1) * hKsi
                val xKsiCurrent = xi(ksiCurrent)
                val xKsiNext = xi(ksiNext)
                val h = xKsiNext - xKsiCurrent
                val xKsiMiddle = xi(alpha + (ksiNext + ksiCurrent) / 2)
                integral += h * f(xKsiMiddle)
            }
            val d = abs(integralExactValue - integral)

            out.println("${N};${integral};${integralExactValue};${d};${Math.log(N.toDouble())};${Math.log(d)}")
            println("N = ${N}, integral=${integral}, exact value = ${integralExactValue}, diff = ${d}")
        }
    }
}