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

    // не используем решение в ло, (Калиткин, стр. 164 (Ложная сходимость)
    // нужно использовать консервативную схему
    (0..M - 1).forEach { m ->
        println("m=${m}/${M - 1}")
        (1..N).forEach { n ->
            val u_n_m = u.getValue(m).getValue(n)
            val u_nm1_m = u.getValue(m).getValue(n - 1)
            val u_n_mp1 = u.getValue(m + 1).getValue(n)

            u.getValue(m + 1).put(n, (u_n_mp1 - u_n_m) / BigDecimal.valueOf(tau) + (u_n_m.pow(2) - u_nm1_m.pow(2)) / (BigDecimal.valueOf(2) * BigDecimal.valueOf(h)))
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

fun init(x: IntRange, t: IntRange): Map<Int, MutableMap<Int, BigDecimal>> {
    val u = hashMapOf<Int, MutableMap<Int, BigDecimal>>()

    t.forEach { m -> u.put(m, HashMap()) }

    t.forEach { m ->
        x.forEach { n ->
            u.getValue(m).set(n, BigDecimal.ZERO)
            if (n == 0) u.getValue(m).put(n, BigDecimal.valueOf(Math.log10(1 / ((1 + m.toDouble().pow(2))))))
            if (m == 0) u.getValue(m).put(n, BigDecimal.valueOf(Math.log10(Math.exp(n.toDouble()))))
        }
    }
    return u
}
