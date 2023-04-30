plugins {
    id("testng.java-library")
}

java {
    registerFeature("guice") {
        usingSourceSet(sourceSets["main"])
    }
}

dependencies {
    api(projects.testngCollections)
    compileOnly("com.github.spotbugs:spotbugs:_")
    "guiceApi"(platform("com.google.inject:guice-bom:_"))
    "guiceApi"("com.google.inject:guice") {
        exclude("javax.inject", "javax.inject")
        exclude("aopalliance", "aopalliance")
        exclude("com.google.guava", "listenablefuture")
    }

    implementation(projects.testngReflectionUtils)
    api("org.slf4j:slf4j-api:_")
    testImplementation("org.slf4j:slf4j-simple:_")
}
