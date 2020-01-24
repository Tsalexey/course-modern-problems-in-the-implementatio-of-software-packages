package ru.rudn.science.belov_course.task4

import java.io.File
import kotlin.math.pow

fun task4(buildDir: File) {
    val nodesCountList = arrayListOf<Int>(100, 200, 400, 800)
    val xRange = 0..10 // x stands for space
    val tRange = 0..10 // t stands for time


    for (nodes in nodesCountList) {
        val N = nodes // space nodes count
        val M = nodes // time nodes count

        val h: Double = (xRange.last - xRange.first).toDouble() / N
        val tau: Double = (tRange.last - tRange.first).toDouble() / M

        var u = hashMapOf<Int, ArrayList<Double>>()

        var xInitlist = mutableListOf<Double>()
        var tInitList = mutableListOf<Double>()

        (0..N).forEach { xInitlist.add(it.toDouble().pow(2)) }
        (0..M).forEach { tInitList.add(0.0)}

        u.put(0, xInitlist.toMutableList() as ArrayList<Double>)
        (1..M).forEach { u.put(it, tInitList.toMutableList() as ArrayList<Double>) }

        for (n in 1..N) {
            for (m in 1..M) {
                val derrivative = ((tau / h) * u.get(n - 1)!!.get(m) + u.get(n)!!.get(m - 1)) / (1 + tau / h)
                u.get(n)?.set(m, derrivative)
            }
        }

        // todo: find estimation by richardson

        val file = "task4_data/task4_u_${nodes}nodes.txt"
        File(file).printWriter().use { out ->
            for(n in 0..N){
                for(m in 0..M){
                    out.println("${0 + h * n};${0 + tau * m};${u.get(n)!!.get(m)}")
                }
            }
        }
    }
}