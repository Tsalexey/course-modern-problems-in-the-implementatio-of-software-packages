package ru.rudn.science.belov_course

import java.io.File
import kotlin.math.abs
import kotlin.reflect.KFunction3

object Utils {

    fun point(range: IntRange, nodeNumber: Int, step: Double): Double = range.first.toDouble() + step * nodeNumber

    fun thomasMethod(A: Double,
                     B: Double,
                     C: Double,
                     calculateD: (u: Map<Int, MutableMap<Int, Double>>,
                                  n: Int,
                                  m: Int,
                                  x: IntRange,
                                  t: IntRange,
                                  h: Double,
                                  tau: Double,
                                  sigma: Double) -> Double,
                     u: Map<Int, MutableMap<Int, Double>>,
                     m: Int,
                     N: Int,
                     x: IntRange,
                     t: IntRange,
                     h: Double,
                     tau: Double,
                     sigma: Double) {

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
            u.getValue(m + 1).put(i, V.getValue(i))
        }
    }

    fun init(x: IntRange,
             t: IntRange,
             applyCondition: KFunction3<@ParameterName(name = "u") Map<Int, MutableMap<Int, Double>>, @ParameterName(name = "m") Int, @ParameterName(name = "n") Int, Unit>): Map<Int, MutableMap<Int, Double>> {
        val u = hashMapOf<Int, MutableMap<Int, Double>>()

        t.forEach { m -> u.put(m, HashMap()) }

        t.forEach { m ->
            x.forEach { n ->
                applyCondition(u, m, n)
            }
        }
        return u
    }

    fun debugMap(map: Map<Int, Double>, N: Int, name: String) {
        println()
        println(name)
        (0..N).forEach { i -> println("${i} : ${map.getOrDefault(i, Double.NaN)} ") }
    }

    fun saveU(result: Map<Int, Map<Int, Double>>,
              dirName: String,
              filename: String,
              xRange: IntRange,
              tRange: IntRange,
              x: IntRange,
              h: Double,
              t: IntRange,
              tau: Double,
              times: List<Double>) {
        println("saving ${dirName}/${filename}")

        File("${dirName}/${filename}.txt").printWriter().use { out ->
            (tRange).forEach { m ->
                (xRange).forEach { n ->
                    out.println("${point(t, m, tau)} ${point(x, n, h)} ${result.getValue(m).getValue(n)} ${result.getValue(m).getValue(n)}")
                }
                out.println()
            }
        }

        for (time in times) {
            val file = "${dirName}/${filename}_time${time}.txt"
            File(file).printWriter().use { out ->
                File(file).printWriter().use { out ->
                    (tRange).forEach { m ->
                        (xRange).forEach { n ->
                            if (abs(point(t, m, tau) - time) < 0.00001) {
                                out.println("${point(x, n, h)} ${result.getValue(m).getValue(n)}")
                            }
                        }
                    }
                }
            }
        }
    }
}
