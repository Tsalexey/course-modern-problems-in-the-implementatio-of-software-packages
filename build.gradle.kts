import ru.rudn.science.belov_course.task1.task1
import ru.rudn.science.belov_course.task2.task2
import ru.rudn.science.belov_course.task3.task3
import ru.rudn.science.belov_course.task4.task4

repositories {
    jcenter()
}

tasks {
    val build by registering {
        doLast {
            task1(project.buildDir)
            task2(project.buildDir)
            task3(project.buildDir)
            task4(project.buildDir)
        }
    }

    wrapper {
        gradleVersion = "6.1"
        distributionType = Wrapper.DistributionType.ALL
    }
}

