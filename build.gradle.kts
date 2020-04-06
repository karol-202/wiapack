plugins {
    kotlin("jvm") version "1.3.71"
    application
}

application.mainClassName = "pl.karol202.chatsci.MainKt"

tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
}

repositories {
    jcenter()
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))

    implementation("com.github.ajalt:clikt:2.6.0")
    implementation("org.jsoup:jsoup:1.13.1")
}
