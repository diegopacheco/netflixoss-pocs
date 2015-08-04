package com.github.diegopacheco.graldeplugin.simpleplugin;

import org.gradle.api.Plugin;
import org.gradle.api.Project;

public class JavaGradleSimplePlugin  implements Plugin<Project> {
	@Override
	public void apply(Project arg0) {
		System.out.println("Gradle plugin in Action by Diego Pacheco: " + arg0);
	}
}