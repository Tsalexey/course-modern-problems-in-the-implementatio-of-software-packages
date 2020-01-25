package ru.rudn.science.belov_course.task1

import java.io.File

fun task1() {
    val segmentStart: Double = 0.0
    val segmentEnd: Double = 1.0

    val nodes = listOf(10, 20, 40, 80, 160, 320, 640, 1280)

    for (N in nodes) {
        calculate(segmentStart, segmentEnd, N, 1..N - 1, ::method1)
        calculate(segmentStart, segmentEnd, N, 2..N, ::method2)
        calculate(segmentStart, segmentEnd, N, 2..N - 1, ::method3)
    }
    calculateError(nodes, "task1_data/task1$1")
    calculateError(nodes, "task1_data/task1$2")
    calculateError(nodes, "task1_data/task1$3")
}

fun f(x: Double): Double = Math.exp(x)
fun fDerrivativeExact(x: Double) = Math.exp(x)
fun method1(x: Double, h: Double) = (f(x + h) - f(x)) / h
fun method2(x: Double, h: Double) = (f(x) - f(x - h)) / h
fun method3(x: Double, h: Double) = (f(x + h) - f(x - h)) / (2 * h)

fun calculate(segmentStart: Double, segmentEnd: Double, N: Int, range: IntRange, derrivative: (x: Double, h: Double) -> Double) {
    val h = (segmentEnd - segmentStart) / N

    var x: Double = segmentStart + range.first * h
    var d: Double
    var fDerrivative: Double
    var exactResult: Double

    val file = "task1_data/${derrivative.javaClass.simpleName}_${N}nodes.txt"
    File(file).printWriter().use { out ->
        for (i in range) {
            fDerrivative = derrivative(x, h)
            exactResult = fDerrivativeExact(x)
            d = Math.abs(fDerrivative - exactResult)
            out.println("${x};${fDerrivative};${exactResult};${d}")
            x += h
        }
    }
}

fun calculateError(nodes: List<Int>, inputFileNamePrefix: String) {
    var map = LinkedHashMap<Int, Double>()

    for (N in nodes) {
        var d: Double
        var inputFileName = "${inputFileNamePrefix}_${N}nodes.txt"
        File(inputFileName).inputStream().bufferedReader().useLines { lines ->
            lines.forEach {
                d = it.split(";").last().toDouble()
                if (map.get(N) == null) map.put(N, d)
                map.get(N)?.apply {
                    if (map.get(N)!! < d)
                        map.put(N, d)
                }
            }
        }
    }

    val outputFile = "${inputFileNamePrefix}_error.txt"
    File(outputFile).printWriter().use { out ->
        map.forEach { (out.println("${Math.log(it.key.toDouble())};${Math.log(it.value)}")) }
    }
}
