package ru.rudn.science.belov_course.task1.task4

import java.io.File
import kotlin.math.pow

fun main(args: Array<String>) {
    val nodesCountList = arrayListOf<Int>(100, 200, 400, 800)
    val xRange = 0..10
    val tRange = 0..10

    for (nodes in nodesCountList) {
        val N = nodes // space nodes count
        val M = nodes // time nodes count

        val h: Double = (xRange.last - xRange.first).toDouble() / N
        val tau: Double = (tRange.last - tRange.first).toDouble() / M

        var u = init(0..N + 1, 0..M + 1)

        (0..M - 1).forEach { m ->
            (0..N - 1).forEach { n ->
                val u_np1_m = u.getValue(m).getValue(n + 1)
                val u_n_mp1 = u.getValue(m + 1).getValue(n)
                u.getValue(m + 1).put(n + 1, (h * u_np1_m + tau * u_n_mp1) / (h + tau))
            }
        }

        // todo: find estimation by richardson

        val file = "task4_data/task4_u_${nodes}nodes.txt"
        File(file).printWriter().use { out ->
            for (n in 0..N) {
                for (m in 0..M) {
                    out.println("${0 + h * n};${0 + tau * m};${u.get(n)!!.get(m)}")
                }
            }
        }
    }
}


fun init(x: IntRange, t: IntRange): Map<Int, MutableMap<Int, Double>> {
    val u = hashMapOf<Int, MutableMap<Int, Double>>()

    t.forEach { m -> u.put(m, HashMap()) }

    t.forEach { m ->
        x.forEach { n ->
            u.getValue(m).set(n, Double.NaN)
            if (n == 0) u.getValue(m).put(n, 0.0)
            if (m == 0) u.getValue(m).put(n, n.toDouble().pow(2))
        }
    }
    return u
}
