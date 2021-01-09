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

import dev.gihwan.tollgate.Dependency

plugins {
    java
    application
    id("com.google.cloud.tools.jib")
}

dependencies {
    implementation(project(":core"))

    implementation(Dependency.config)
    implementation(Dependency.jsr305)
    implementation(Dependency.slf4j)

    runtimeOnly(Dependency.logback)

    testImplementation(Dependency.junitApi)
    testImplementation(Dependency.assertj)
    testImplementation(Dependency.awaitility)
    testImplementation(Dependency.mockito)
    testImplementation(Dependency.armeriaJunit)

    testRuntimeOnly(Dependency.junitEngine)
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

tasks.test {
    useJUnitPlatform()
}

jib {
    from {
        image = "openjdk:11-jre-slim"
    }
    to {
        image = "ghkim3221/tollgate-standalone"
    }
    container {
        ports = listOf("8080")
    }
}
