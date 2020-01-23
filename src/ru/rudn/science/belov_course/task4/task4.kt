package ru.rudn.science.belov_course.task4

import kotlin.math.pow

fun main(args: Array<String>) {

    val xRange = 0..10 // x stands for space
    val tRange = 0..10 // t stands for time

    val N: Int // space nodes count
    val M: Int // time nodes count

    val nodesCountList = arrayListOf<Int>(100, 200, 400, 800)

    val nodesCount = 100

    N = nodesCount
    M = nodesCount

    val stepSpace = (xRange.last - xRange.first) / N
    val stepTime = (tRange.last - tRange.first) / M

    // u_time_init = x^2
    // u_space_init = 0
    
    // u_time + u_space = 0


}