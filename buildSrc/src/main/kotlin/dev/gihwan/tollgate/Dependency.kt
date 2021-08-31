/*
 * MIT License
 *
 * Copyright (c) 2020 - 2021 Gihwan Kim
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package dev.gihwan.tollgate

object Dependency {
    const val armeria = "com.linecorp.armeria:armeria:${Version.armeria}"
    const val armeriaJunit = "com.linecorp.armeria:armeria-junit5:${Version.armeria}"

    const val commonsLang3 = "org.apache.commons:commons-lang3:${Version.commonsLang3}"
    const val config = "com.typesafe:config:${Version.config}"
    const val guava = "com.google.guava:guava:${Version.guava}"
    const val jsr305 = "com.google.code.findbugs:jsr305:${Version.jsr305}"
    const val logback = "ch.qos.logback:logback-classic:${Version.logback}"
    const val slf4j = "org.slf4j:slf4j-api:${Version.slf4j}"

    const val springBootAutoConfigure =
        "org.springframework.boot:spring-boot-autoconfigure:${Version.springBoot}"
    const val springBootStarter =
        "org.springframework.boot:spring-boot-starter:${Version.springBoot}"
    const val springBootStarterTest =
        "org.springframework.boot:spring-boot-starter-test:${Version.springBoot}"

    const val assertj = "org.assertj:assertj-core:${Version.assertj}"
    const val awaitility = "org.awaitility:awaitility:${Version.awaitility}"
    const val junitApi = "org.junit.jupiter:junit-jupiter-api:${Version.junit}"
    const val junitEngine = "org.junit.jupiter:junit-jupiter-engine:${Version.junit}"
    const val mockito = "org.mockito:mockito-core:${Version.mockito}"
}
