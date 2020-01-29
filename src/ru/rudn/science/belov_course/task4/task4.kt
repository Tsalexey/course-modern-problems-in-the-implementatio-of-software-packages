package ru.rudn.science.belov_course.task4

import ru.rudn.science.belov_course.Utils
import java.io.File
import kotlin.math.pow

object Conditions {
    val nodesCountList = arrayListOf<Int>(100, 200, 400, 800)
    val x = 0..10
    val t = 0..10

    val times = arrayListOf(0.0, 2.5, 5.0, 7.5, 10.0)

    var h = Double.NaN
    var tau = Double.NaN
}

fun main(args: Array<String>) {

    File("task4_data/task4_R.txt").printWriter().use { out ->

        for (nodes in Conditions.nodesCountList) {
            var N = nodes
            var M = nodes

            var h: Double = (Conditions.x.last - Conditions.x.first).toDouble() / N
            var tau: Double = (Conditions.t.last - Conditions.t.first).toDouble() / M

            Conditions.h = h
            Conditions.tau = tau

            val u = calculateU(0..N, 0..M, h, tau, Conditions.x, Conditions.t, Conditions.times, "task4_data", "task4_u_${nodes}")

            N = 2 * nodes
            M = 2 * nodes

            h = (Conditions.x.last - Conditions.x.first).toDouble() / (2 * N)
            tau = (Conditions.t.last - Conditions.t.first).toDouble() / (2 * M)

            Conditions.h = h
            Conditions.tau = tau

            val u2 = calculateU(0..2 * N, 0..2 * M, h, tau, Conditions.x, Conditions.t, Conditions.times, "task4_data", "task4_u2_${nodes}")

            N = nodes
            M = nodes
            val coef = 2.0.pow(2)
            var D = (-u2.getValue(2 * 0).getValue(2 * 0) + u.getValue(0).getValue(0)) / (coef - 1)
            (0..M).forEach { m ->
                (0..N).forEach { n ->
                    val d = (-u2.getValue(2 * m).getValue(2 * n) + u.getValue(m).getValue(n)) / (coef - 1)
                    if (d > D) {
                        D = d;
                    }
                    if (d > 23) println("${nodes} ${m} ${n} ${d}");
                }
            }
            out.println("${nodes} ${D} ${Math.log10(nodes.toDouble())} ${Math.log10(D)}")
        }
    }
}

fun calculateU(xRange: IntRange, tRange: IntRange, h: Double, tau: Double, x: IntRange, t: IntRange, times: List<Double>, dirName: String, fileName: String): Map<Int, MutableMap<Int, Double>> {
    val u = Utils.init(xRange, tRange, ::applyCondition)

    (tRange.first..tRange.last - 1).forEach { m ->
        (xRange.first..tRange.last - 1).forEach { n ->
            val u_np1_m = u.getValue(m).getValue(n + 1)
            val u_n_mp1 = u.getValue(m + 1).getValue(n)
            u.getValue(m + 1).put(n + 1, (h * u_np1_m + tau * u_n_mp1) / (h + tau))
        }
    }
    Utils.saveU(u, dirName, fileName, xRange, tRange, x, h, t, tau, times)
    return u
}

fun applyCondition(u: Map<Int, MutableMap<Int, Double>>, m: Int, n: Int) {
    u.getValue(m).set(n, Double.NaN)
    if (n == 0) u.getValue(m).put(n, 0.0)
    if (m == 0) u.getValue(m).put(n, Utils.point(Conditions.x, n, Conditions.h).pow(2))
    return;
}
