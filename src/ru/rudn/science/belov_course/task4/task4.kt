package ru.rudn.science.belov_course.task4

import ru.rudn.science.belov_course.Utils
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

    for (nodes in Conditions.nodesCountList) {
        val N = nodes
        val M = nodes

        val h: Double = (Conditions.x.last - Conditions.x.first).toDouble() / N
        val tau: Double = (Conditions.t.last - Conditions.t.first).toDouble() / M

        Conditions.h = h
        Conditions.tau = tau

        val u = Utils.init(0..N, 0..M, ::applyCondition)

        (0..M - 1).forEach { m ->
            (0..N - 1).forEach { n ->
                val u_np1_m = u.getValue(m).getValue(n + 1)
                val u_n_mp1 = u.getValue(m + 1).getValue(n)
                u.getValue(m + 1).put(n + 1, (h * u_np1_m + tau * u_n_mp1) / (h + tau))
            }
        }
        Utils.saveU(u, "task4_data", "task4_u_${nodes}", 0..N, 0..M, Conditions.x, h, Conditions.t, tau, Conditions.times)

        // todo: find estimation by richardson
    }
}

fun applyCondition(u: Map<Int, MutableMap<Int, Double>>, m: Int, n: Int) {
    u.getValue(m).set(n, Double.NaN)
    if (n == 0) u.getValue(m).put(n, 0.0)
    if (m == 0) u.getValue(m).put(n, Utils.point(Conditions.x, n, Conditions.h).pow(2))
    return;
}
