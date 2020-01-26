package ru.rudn.science.belov_course.task1.task6

import java.io.File
import kotlin.math.abs
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

fun f(x: Double, t: Double): Double = 0.0

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

    (0..M - 1).forEach { m ->
        (0..N - 2).forEach { n ->
            val xPoint = point(x, n, h)
            val tPoint = point(t, m, tau)
            val phi = f(xPoint, tPoint)

            val u_np1_m = u.getValue(m).getValue(m + 1)
            val u_n_m = u.getValue(m).getValue(n)
            val u_np2_m = u.getValue(m).getValue(n + 2)

            u.getValue(m + 1).put(n + 1, u_np1_m + (2 * tau / h.pow(2)) * u_np1_m + (tau / h.pow(2)) * u_n_m + (tau / h.pow(2)) * u_np2_m + tau * phi)
        }
    }

    save(u, "explicit", 0..M, 0..N, x, h, t, tau, times)
}

fun calculateD(u: Map<Int, MutableMap<Int, Double>>, n: Int, m: Int, x: IntRange, t: IntRange, h: Double, tau: Double): Double {
    return -tau * h.pow(2) * f(point(x, n, h), point(t, m - 1, tau)) - h.pow(2) * u.getValue(m - 1).getValue(n)
}

fun implicit(x: IntRange, t: IntRange, N: Int, M: Int, times: List<Double>) {
    val h: Double = (x.last.toDouble() - x.first.toDouble()) / N
    val tau: Double = (t.last.toDouble() - t.first.toDouble()) / M

    val u = init(0..N, 0..N, N, h)

    (1..M).forEach { m ->
        var A: Double = tau
        var B: Double = -(h.pow(2) + 2 * tau)
        var C: Double = tau

        val V = mutableMapOf<Int, Double>()
        val D = mutableMapOf<Int, Double>()
        val F = mutableMapOf<Int, Double>()

        V.put(0, u.getValue(m).getValue(0))
        V.put(N, u.getValue(m).getValue(N))

        (1..N - 1).forEach { n ->
            V.put(n, u.getValue(m).getValue(n))
            D.put(n, calculateD(u, n, m, x, t, h, tau))
            F.put(n, D.getValue(n))
        }

        F.set(1, F.getValue(1) - A * V.getValue(0))
        F.set(N - 1, F.getValue(N - 1) - C * V.getValue(N))

        val alpha = mutableMapOf<Int, Double>()
        val beta = mutableMapOf<Int, Double>()

        alpha.put(1, C / B)
        beta.put(1, F.getValue(1) / B)

        (2..N - 1).forEach { i ->
            alpha.put(i, C / (B - A * alpha.getValue(i - 1)))
            beta.put(i, (F.getValue(i) - A * beta.getValue(i - 1)) / (B - A * alpha.getValue(i - 1)))
        }

        V.put(N - 1, beta.getValue(N - 1))
        for (i in N - 2 downTo 1) {
            V.put(i, beta.getValue(i) - alpha.getValue(i) * V.getValue(i + 1))
        }

        (1..N - 1).forEach { i ->
            u.getValue(m).put(i, V.getValue(i))
        }

    }

    save(u, "implicit", 0..M, 0..N, x, h, t, tau, times)
}

fun debug(map: Map<Int, Double>, N: Int, s: String) {
    println()
    println(s)

    (0..N).forEach { i ->
        println("${i} : ${map.getOrDefault(i, Double.NaN)} ")
    }
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
        val file = "task6_data/task6_${filename}_time${time}.txt"
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

