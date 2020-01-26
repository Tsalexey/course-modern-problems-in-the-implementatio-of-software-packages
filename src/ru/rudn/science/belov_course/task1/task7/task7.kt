package ru.rudn.science.belov_course.task1.task7

import java.io.File
import kotlin.math.abs
import kotlin.math.exp
import kotlin.math.pow

fun main(args: Array<String>) {
    val N = 100
    val M = 100

    val xRange = -1..1
    val tRange = 0..2

    val times = listOf<Double>(0.0, 0.5, 1.0, 1.5, 2.0)

    implicit(xRange, tRange, N, M, times)
}


fun point(range: IntRange, nodeNumber: Int, step: Double): Double = range.first.toDouble() + step * nodeNumber

fun init(x: IntRange, t: IntRange, N: Int, h: Double): Map<Int, MutableMap<Int, Double>> {
    val u = hashMapOf<Int, MutableMap<Int, Double>>()

    t.forEach { m -> u.put(m, HashMap()) }

    t.forEach { m ->
        x.forEach { n ->
            u.getValue(m).set(n, Double.NaN)
            if (n == 0) (u.getValue(m).put(n, exp(-1.0)))
            if (n == N) (u.getValue(m).put(n, exp(-1.0)))
            if (m == 0) u.getValue(m).put(n, exp(2.0))
            if (m == 1) u.getValue(m).put(n, u.getValue(0).getValue(n))
        }
    }
    return u
}

fun calculateD(u: Map<Int, MutableMap<Int, Double>>, n: Int, m: Int, x: IntRange, t: IntRange, h: Double, tau: Double, sigma: Double): Double {
    val u_nm1_mm1 = u.getValue(m - 1).getValue(n - 1)
    val u_n_mm1 = u.getValue(m - 1).getValue(n)
    val u_np1_mm1 = u.getValue(m - 1).getValue(n + 1)
    val u_nm1_m = u.getValue(m).getValue(n - 1)
    val u_n_m = u.getValue(m).getValue(n)
    val u_np1_m = u.getValue(m).getValue(n + 1)

    val G = sigma * (u_nm1_mm1 - 2 * u_n_mm1 + u_np1_mm1) / (h.pow(2)) + ((1 - 2 * sigma) * u_nm1_m - 2 * u_n_m + u_np1_m) / (h.pow(2))
    return -(tau.pow(2) * G + 2 * u_n_m - u_n_mm1)
}

fun implicit(x: IntRange, t: IntRange, N: Int, M: Int, times: List<Double>) {
    println("implicit");

    val h: Double = (x.last.toDouble() - x.first.toDouble()) / N
    val tau: Double = (t.last.toDouble() - t.first.toDouble()) / M

    val u = init(0..N, 0..M, N, h)

    val sigma = 0.05

    (1..M - 1).forEach { m ->
        println("m=${m}/${M}");
        var A: Double = sigma * tau.pow(2) / h.pow(2)
        var B: Double = -(1 + (2 * sigma * tau.pow(2)) / (h.pow(2)))
        var C: Double = (sigma * tau.pow(2)) / h.pow(2)

        val V = mutableMapOf<Int, Double>()
        val D = mutableMapOf<Int, Double>()
        val F = mutableMapOf<Int, Double>()

        V.put(0, u.getValue(m).getValue(0))
        V.put(N, u.getValue(m).getValue(N))

        (1..N - 1).forEach { n ->
            V.put(n, u.getValue(m).getValue(n))
            D.put(n, calculateD(u, n, m, x, t, h, tau, sigma))
            F.put(n, D.getValue(n))
        }

        debug(V, N, "V")
        debug(D, N, "D")

        F.set(1, F.getValue(1) - A * V.getValue(0))
        F.set(N - 1, F.getValue(N - 1) - C * V.getValue(N))

        debug(F, N, "F")

        val alpha = mutableMapOf<Int, Double>()
        val beta = mutableMapOf<Int, Double>()

        alpha.put(1, C / B)
        beta.put(1, F.getValue(1) / B)

        (2..N - 1).forEach { i ->
            alpha.put(i, C / (B - A * alpha.getValue(i - 1)))
            beta.put(i, (F.getValue(i) - A * beta.getValue(i - 1)) / (B - A * alpha.getValue(i - 1)))
        }

        debug(alpha, N, "alpha")
        debug(beta, N, "beta")

        V.put(N - 1, beta.getValue(N - 1))
        for (i in N - 2 downTo 1) {
            V.put(i, beta.getValue(i) - alpha.getValue(i) * V.getValue(i + 1))
        }


        debug(V, N, "V")

        (1..N - 1).forEach { i ->
            u.getValue(m + 1).put(i, V.getValue(i))
        }

    }

    save(u, "implicit", 0..N, 0..M, x, h, t, tau, times)
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
    println("saving ${filename}")

    val max: Double
    val min: Double

    val file = "task7_data/task7_${filename}.txt"
    File(file).printWriter().use { out ->
        (tRange).forEach { m ->
            (xRange).forEach { n ->
                out.println("${point(t, m, tau)} ${point(x, n, h)} ${result.getValue(m).getValue(n)} ${result.getValue(m).getValue(n)}")
            }
            out.println()
        }
    }

    for (time in times) {
        val file = "task7_data/task7_${filename}_time${time}.txt"
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

