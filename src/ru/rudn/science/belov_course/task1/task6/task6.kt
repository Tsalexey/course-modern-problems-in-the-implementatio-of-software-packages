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

    explicit(xRange, tRange, N, M)
}

fun f(x: Double, t: Double): Double = exp(x * t)

fun point(range: IntRange, nodeNumber: Int, step: Double): Double = range.first.toDouble() + step * nodeNumber

fun explicit(x: IntRange, t: IntRange, N: Int, M: Int) {
    val h: Double = (x.last.toDouble() - x.first.toDouble()) / N
    val tau: Double = (t.last.toDouble() - t.first.toDouble()) / M

    val u = hashMapOf<Int, MutableMap<Int, Double>>()

    // init u
    (0..M).forEach { m -> u.put(m, HashMap()) }

    (0..M).forEach { m ->
        (0..N).forEach { n ->
            (u.getValue(m).set(n, Double.NaN))
            if (n == 0) (u.getValue(m).set(n, 0.0))
            if (n == N) (u.getValue(m).set(n, 1.0))
            if (m == 0) u.getValue(m).set(n, point(x, n, h).pow(2))
        }
    }

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
//            println("m=${m}, n=${n}, x=${xPoint}, t=${tPoint}, f=${phi}, uC=${uCurent}, uP=${uPrev}, uN=${uNext}")
            u.getValue(m + 1).set(n, tau * phi + uCurent + (tau / h.pow(2)) * (uNext - 2 * uCurent + uPrev))
        }
    }

    val file = "task6_data/task6_explicit.txt"
    File(file).printWriter().use { out ->
        (0..M).forEach { m ->
            (0..N).forEach { n ->
                out.println("${point(x, n, h)};${point(t, m, tau)};${u.getValue(m).getValue(n)}")
            }
        }
    }

    for (time in listOf<Double>(0.0, 0.25, 0.5, 0.75, 1.0)) {
        val file = "task6_data/task6_explicit_time${time}.txt"
        File(file).printWriter().use { out ->
            File(file).printWriter().use { out ->
                (0..M).forEach { m ->
                    (0..N).forEach { n ->
                        if (abs(point(x, m, tau) - time) < 0.00001) {
                            out.println("${point(x, n, h)};${point(t, m, tau)};${u.getValue(m).getValue(n)}")
                        }
                    }
                }
            }
        }
    }
}

