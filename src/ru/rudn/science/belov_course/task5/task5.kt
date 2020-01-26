package ru.rudn.science.belov_course.task5

import ru.rudn.science.belov_course.Utils
import java.io.File
import kotlin.math.abs
import kotlin.math.exp
import kotlin.math.pow
import kotlin.math.sqrt

object Conditions {
    val N = 100
    val M = 100

    val xRange = 0..10
    val tRange = 0..10

    val h: Double = (xRange.last - xRange.first).toDouble() / N
    val tau: Double = (tRange.last - tRange.first).toDouble() / M

    val times = listOf<Double>(0.0, 0.25, 0.5, 0.75, 1.0)
}

fun main(args: Array<String>) {
    val M = Conditions.M
    val N = Conditions.N
    val tau = Conditions.tau
    val h = Conditions.h

    var u = init(0..N + 1, 0..M + 1)

    // не используем решение в лоб, (Калиткин, стр. 164 (Ложная сходимость)
    // нужно использовать неявную консервативную схему
    (0..M - 1).forEach { m ->
        println("m=${m}/${M - 1}")
        (1..N - 1).forEach { n ->
            val u_n_m = u.getValue(m).getValue(n)
            val u_nm1_mp1 = u.getValue(m + 1).getValue(n - 1)
            u.getValue(m + 1).put(n, sqrt(h.pow(2) / tau.pow(2) + (2 * h / tau) * u_n_m + u_nm1_mp1.pow(2)) - h / tau)
        }
    }

    println("saving results")
    val file = "task5_data/task5_${N}_${M}.txt"
    File(file).printWriter().use { out ->
        (0..M).forEach { m ->
            (0..N).forEach { n ->
                out.println("${0 + tau * m} ${0 + h * n} ${u.getValue(m).getValue(n)} ${u.getValue(m).getValue(n)}")
            }
            out.println()
        }
    }

    for (t in listOf(0, 5, 10)) {
        val file2 = "task5_data/task5_${N}_${M}_xu_t=${t}.txt"
        File(file2).printWriter().use { out ->
            (0..M).forEach { m ->
                (0..N).forEach { n ->
                    if (abs((0 + tau * m) - t.toDouble()) < 0.00001) out.println("${0 + tau * m} ${0 + h * n} ${u.getValue(m).getValue(n)}")
                }
                out.println()
            }
        }
    }
}

fun init(x: IntRange, t: IntRange): Map<Int, MutableMap<Int, Double>> {
    val u = hashMapOf<Int, MutableMap<Int, Double>>()

    t.forEach { m -> u.put(m, HashMap()) }

    t.forEach { m ->
        x.forEach { n ->
            u.getValue(m).put(n, Double.NaN)
            if (n == 0) u.getValue(m).put(n, 1 / ((1 + Utils.point(t, m, Conditions.tau).pow(2))))
            if (m == 0) u.getValue(m).put(n, exp(Utils.point(x, n, Conditions.h)))
        }
    }
    return u
}
