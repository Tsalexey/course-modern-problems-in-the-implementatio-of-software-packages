package rudn.science.belov_course.task6

import kotlin.math.pow

fun task6() {
    val N = 10
    val M = 10

    val xRange = 0..1
    val tRange = 0..1

    explicit(xRange, tRange, N, M)
}

fun explicit(x: IntRange, t: IntRange, N: Int, M: Int) {

    val h: Double = ((x.last - x.first) / N).toDouble()
    val tau: Double = ((t.last - t.first) / M).toDouble()

    val u = hashMapOf<Int, MutableMap<Int, Double>>()

    // init u
    (1 until N + 1).forEach { n -> u.put(n, HashMap()) }

    (1 until N + 1).forEach { n ->
        (1 until M + 1).forEach { m ->
            when (n) {
                1 -> (u.getValue(m).set(n, 1.0))
                N -> (u.getValue(m).set(n, N.toDouble()))
                else -> (u.getValue(m).set(n, 0.0))
            }
            if (n == 1) {
                u.getValue(n).set(m, m.toDouble().pow(2))
            }
        }
    }

    for (i in 1..N) {
        for (j in 1..M) {
            print("${u.getValue(i).get(j)} ")
        }
        println()
    }
}

