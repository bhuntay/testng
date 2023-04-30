import buildlogic.filterEolSimple

plugins {
    `java-library`
    id("org.gradlex.extra-java-module-info")
    id("testng.java")
    id("testng.testing")
}

tasks.withType<Javadoc>().configureEach {
    excludes.add("org/testng/internal/**")
}

tasks.withType<JavaCompile>().configureEach {
    inputs.property("java.version", System.getProperty("java.version"))
    inputs.property("java.vm.version", System.getProperty("java.vm.version"))
    options.apply {
        encoding = "UTF-8"
        //We still are NOT compatible with JDK17 in terms of deprecation.
        // But let's not fail builds for that reason
        if (JavaVersion.current() != JavaVersion.VERSION_17) {
            compilerArgs.add("-Xlint:deprecation")
            //If we have deprecation warnings, the build fails
            //which should not happen
            //compilerArgs.add("-Werror")
        }
    }
}

tasks.withType<Jar>().configureEach {
    into("META-INF") {
        filterEolSimple("crlf")
        from("$rootDir/LICENSE.txt")
        from("$rootDir/NOTICE")
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    }
    manifest {
        // providers.gradleProperty does not work
        // see https://github.com/gradle/gradle/issues/14972
        val name = rootProject.findProperty("project.name")
        val vendor = rootProject.findProperty("project.vendor.name")
        attributes(mapOf(
            "Specification-Title" to name,
            "Specification-Version" to project.version,
            "Specification-Vendor" to vendor,
            "Implementation-Title" to name,
            "Implementation-Version" to project.version,
            "Implementation-Vendor" to vendor,
            "Implementation-Vendor-Id" to rootProject.findProperty("project.vendor.id"),
            "Implementation-Url" to rootProject.findProperty("project.url"),
        ))
    }
}

@Suppress("unused")
val transitiveSourcesElements by configurations.creating {
    description = "Share sources folder with other projects for aggregation (e.g. sources, javadocs, etc)"
    isVisible = false
    isCanBeResolved = false
    isCanBeConsumed = true
    extendsFrom(configurations.implementation.get())
    attributes {
        attribute(Usage.USAGE_ATTRIBUTE, objects.named(Usage.JAVA_RUNTIME))
        attribute(Category.CATEGORY_ATTRIBUTE, objects.named(Category.DOCUMENTATION))
        attribute(DocsType.DOCS_TYPE_ATTRIBUTE, objects.named("source-folders"))
    }
    // afterEvaluate is to allow creation of the new source sets
    afterEvaluate {
        sourceSets.main.get().java.srcDirs.forEach { outgoing.artifact(it) }
    }
}

extraJavaModuleInfo {
    automaticModule("dom4j-2.1.3.jar", "dom4j2")
    automaticModule("failureaccess-1.0.1.jar", "failureaccess")
    automaticModule("j2objc-annotations-1.3.jar", "j2objc.annotations")
    automaticModule("jcip-annotations-1.0.jar", "jcip.annotations")
    automaticModule("jsr305-3.0.2.jar", "javax.inject")
}
