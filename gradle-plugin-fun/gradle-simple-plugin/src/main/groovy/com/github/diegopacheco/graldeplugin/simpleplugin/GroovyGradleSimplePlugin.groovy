package com.github.diegopacheco.graldeplugin.simpleplugin

import org.gradle.api.*

class GroovyGradleSimplePlugin implements Plugin<Project> {
    def void apply(Project project) {
        project.task('hello') << {
            println 'Hi from GroovyGradleSimplePlugin plugin!'
        }
    }
}