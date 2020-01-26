package ru.rudn.science.belov_course.task5

import java.io.File
import java.math.BigDecimal
import kotlin.math.abs
import kotlin.math.pow

fun main(args: Array<String>) {
    val N = 10
    val M = 10

    val xRange = 0..10
    val tRange = 0..10

    val h: Double = (xRange.last - xRange.first).toDouble() / N
    val tau: Double = (tRange.last - tRange.first).toDouble() / M

    println("h=${h}, tau=${tau}, R=${tau / h.pow(2)}")

    var u = init(0..N + 1, 0..M + 1)

    (0..M - 1).forEach { m ->
        println("m=${m}/${M - 1}")
        (0..N - 1).forEach { n ->
            val u_np1_m = u.getValue(m).getValue(n + 1)
            val u_n_m = u.getValue(m).getValue(n)
            u.getValue(m + 1).put(n + 1, (BigDecimal.valueOf(h) * u_np1_m - u_n_m * BigDecimal.valueOf(tau) * u_np1_m + BigDecimal.valueOf(tau) * u_n_m.pow(2)) / BigDecimal.valueOf(h))
        }
    }

//    (0..M - 1).forEach { m ->
//        println("m=${m}/${M - 1}")
//        (0..N - 1).forEach { n ->
//            val u_np1_m = u.getValue(m).getValue(n + 1)
//            val u_n_m = u.getValue(m).getValue(n)
//            u.getValue(m + 1).put(n + 1, (BigDecimal.valueOf(h) * u_np1_m - u_n_m * BigDecimal.valueOf(tau) * u_np1_m + BigDecimal.valueOf(tau) * u_n_m.pow(2)) / BigDecimal.valueOf(h))
//        }
//    }
//
    println("saving results")
    val file = "task5_data/task5_${N}_${M}.txt"
    File(file).printWriter().use { out ->
        (0..M).forEach { m ->
            (0..N).forEach { n ->
                out.println("${0 + h * n};${0 + tau * m};${u.getValue(m).getValue(n)}")
            }
        }
    }

    for (t in listOf(0, 5, 10)) {
        val file2 = "task5_data/task5_${N}_${M}_xu_t=${t}.txt"
        File(file2).printWriter().use { out ->
            (0..M).forEach { m ->
                (0..N).forEach { n ->
                    if (abs((0 + tau * m) - t.toDouble()) < 0.00001) out.println("${0 + h * n};${0 + tau * m};${u.getValue(m).getValue(n)}")
                }
            }
        }
    }
}

fun init(x: IntRange, t: IntRange): Map<Int, MutableMap<Int, BigDecimal>> {
    val u = hashMapOf<Int, MutableMap<Int, BigDecimal>>()

    t.forEach { m -> u.put(m, HashMap()) }

    t.forEach { m ->
        x.forEach { n ->
            u.getValue(m).set(n, BigDecimal.ZERO)
            if (n == 0) u.getValue(m).put(n, BigDecimal.valueOf((1 / ((1 + m.toDouble().pow(2))))))
            if (m == 0) u.getValue(m).put(n, BigDecimal.valueOf((Math.exp(n.toDouble()))))
        }
    }
    return u
}
