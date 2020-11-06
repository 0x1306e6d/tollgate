/*
 * MIT License
 *
 * Copyright (c) 2020 Gihwan Kim
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

plugins {
    id("java-library")
}

dependencies {
    api("com.linecorp.armeria:armeria:1.1.0")
    implementation("com.google.guava:guava:29.0-jre")
    implementation("com.google.code.findbugs:jsr305:3.0.2")
    implementation("org.apache.commons:commons-lang3:3.11")
    implementation("org.slf4j:slf4j-api:1.7.30")

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.7.0")
    testImplementation("org.assertj:assertj-core:3.17.2")
    testImplementation("org.mockito:mockito-core:3.5.13")
    testImplementation("org.awaitility:awaitility:4.0.3")
    testImplementation("com.linecorp.armeria:armeria-junit5:1.1.0")

    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.7.0")
    testRuntimeOnly("ch.qos.logback:logback-classic:1.2.3")
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

tasks.test {
    useJUnitPlatform()
}
