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

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.testing.Test
import org.gradle.testing.jacoco.plugins.JacocoPlugin
import org.gradle.testing.jacoco.plugins.JacocoPluginExtension
import org.gradle.testing.jacoco.tasks.JacocoReport

class CoveragePlugin : Plugin<Project> {
    override fun apply(project: Project) {
        project.plugins.apply(JacocoPlugin::class.java)

        val jacoco = project.extensions.getByType(JacocoPluginExtension::class.java)
        jacoco.toolVersion = "0.8.7"

        val test = project.tasks.withType(Test::class.java)
        val jacocoReport = project.tasks.withType(JacocoReport::class.java) {
            dependsOn(test)

            reports {
                xml.required.set(true)
                xml.outputLocation.set(project.layout.buildDirectory.file("jacoco/jacocoTestReport.xml"))
                html.required.set(true)
                html.outputLocation.set(project.layout.buildDirectory.dir("jacoco/html"))
            }
        }
        test.all { finalizedBy(jacocoReport) }
    }
}
