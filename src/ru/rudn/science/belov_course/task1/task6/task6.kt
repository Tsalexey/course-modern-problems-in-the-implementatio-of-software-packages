package ru.rudn.science.belov_course.task1.task6

import java.io.File
import kotlin.math.abs
import kotlin.math.exp
import kotlin.math.pow

fun main(args: Array<String>) {
    val N = 100
    val M = 100

    val xRange = 0..1
    val tRange = 0..1

    val times = listOf<Double>(0.0, 0.25, 0.5, 0.75, 1.0)

    explicit(xRange, tRange, N, M, times)
    implicit(xRange, tRange, N, M, times)
}

fun f(x: Double, t: Double): Double = exp(x * t)

fun point(range: IntRange, nodeNumber: Int, step: Double): Double = range.first.toDouble() + step * nodeNumber

fun init(x: IntRange, t: IntRange, N: Int, h: Double): Map<Int, MutableMap<Int, Double>> {
    val u = hashMapOf<Int, MutableMap<Int, Double>>()

    t.forEach { m -> u.put(m, HashMap()) }

    t.forEach { m ->
        x.forEach { n ->
            u.getValue(m).set(n, Double.NaN)
            if (n == 0) (u.getValue(m).set(n, 0.0))
            if (n == N) (u.getValue(m).set(n, 1.0))
            if (m == 0) u.getValue(m).set(n, point(x, n, h).pow(2))
        }
    }
    return u
}

fun explicit(x: IntRange, t: IntRange, N: Int, M: Int, times: List<Double>) {
    val h: Double = (x.last.toDouble() - x.first.toDouble()) / N
    val tau: Double = (t.last.toDouble() - t.first.toDouble()) / M

    val u = init(0..N, 0..N, N, h)

    var xPoint: Double
    var tPoint: Double
    var phi: Double
    var uCurent: Double
    var uPrev: Double
    var uNext: Double

    (0..M - 1).forEach { m ->
        (1..N - 1).forEach { n ->
            xPoint = point(x, n, h)
            tPoint = point(t, m, tau)
            phi = f(xPoint, tPoint)
            uCurent = u.getValue(m).getValue(n)
            uPrev = u.getValue(m).getValue(n - 1)
            uNext = u.getValue(m).getValue(n + 1)
            u.getValue(m + 1).set(n, tau * phi + uCurent + (tau / h.pow(2)) * (uNext - 2 * uCurent + uPrev))
        }
    }

    save(u, "explicit", 0..M, 0..N, x, h, t, tau, times)
}

fun implicit(x: IntRange, t: IntRange, N: Int, M: Int, times: List<Double>) {
//    val h: Double = (x.last.toDouble() - x.first.toDouble()) / N
//    val tau: Double = (t.last.toDouble() - t.first.toDouble()) / M
//
//    val u = init(0..N, 0..M, N, h)
//
//    var xPoint: Double
//    var tPoint: Double
//    var phi: Double
//    var uCurent: Double
//    var uPrev: Double
//    var uNext: Double
//
//    (0..M - 1).forEach { m ->
//        (1..N - 1).forEach { n ->
//            xPoint = point(x, n, h)
//            tPoint = point(t, m, tau)
//            phi = f(xPoint, tPoint)
//            uCurent = u.getValue(m).getValue(n)
//            uPrev = u.getValue(m).getValue(n - 1)
//            uNext = u.getValue(m).getValue(n + 1)
//            u.getValue(m + 1).set(n, tau * phi + uCurent + (tau / h.pow(2)) * (uNext - 2 * uCurent + uPrev))
//        }
//    }
//
//    save(u, "implicit", 0..M, 0..N, x, h, t, tau, times)
}

fun save(result: Map<Int, Map<Int, Double>>,
         filename: String,
         xRange: IntRange,
         tRange: IntRange,
         x: IntRange,
         h: Double,
         t: IntRange,
         tau: Double,
         times: List<Double>) {
    val file = "task6_data/task6_${filename}.txt"
    File(file).printWriter().use { out ->
        (tRange).forEach { m ->
            (xRange).forEach { n ->
                out.println("${point(x, n, h)};${point(t, m, tau)};${result.getValue(m).getValue(n)}")
            }
        }
    }

    for (time in times) {
        val file = "task6_data/task6_explicit_time${time}.txt"
        File(file).printWriter().use { out ->
            File(file).printWriter().use { out ->
                (tRange).forEach { m ->
                    (xRange).forEach { n ->
                        if (abs(point(x, m, tau) - time) < 0.00001) {
                            out.println("${point(x, n, h)};${point(t, m, tau)};${result.getValue(m).getValue(n)}")
                        }
                    }
                }
            }
        }
    }
}

