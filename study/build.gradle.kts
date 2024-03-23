import com.github.gradle.node.NodeExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.springframework.boot.gradle.tasks.bundling.BootJar
import org.springframework.boot.gradle.tasks.run.BootRun

plugins {
    id("org.springframework.boot") version "3.2.3"
    id("io.spring.dependency-management") version "1.1.4"
    id("com.github.node-gradle.node") version "7.0.1"
    kotlin("jvm") version "1.9.22"
    kotlin("plugin.spring") version "1.9.22"
}

allprojects {
    group = "org.book.app"
    version = "0.0.1-SNAPSHOT"
    description = "study"

    repositories {
        mavenCentral()
    }
}

project(":backend") {
    apply(plugin = "kotlin")
    apply(plugin = "org.jetbrains.kotlin.plugin.spring")
    apply(plugin = "org.springframework.boot")
    apply(plugin = "io.spring.dependency-management")

    java {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }

    tasks.withType<JavaCompile> {
        options.encoding = "UTF-8"
    }

    tasks.withType<JavaCompile>().named("compileTestJava") {
        options.compilerArgs = listOf("-proc:none")
    }

    tasks.register<Delete>("cleanGen") {
        val dirBaseName = "src/main/resources/static/"
        val dirNames = listOf("css", "res", "js")
        val gitKeep = ".gitkeep"
        dirNames.forEach { dirName ->
            val targetDir = file("$dirBaseName$dirName")
            targetDir.listFiles()?.forEach { file ->
                if (file.name != gitKeep) {
                    delete(file)
                }
            }
        }
    }

    tasks.named<BootJar>("bootJar") {
        duplicatesStrategy = DuplicatesStrategy.INCLUDE
    }

    tasks.named<Jar>("jar") {
        enabled = false
    }

    tasks.named<BootRun>("bootRun") {
        sourceResources(sourceSets["main"])
        // jvmArgsの設定
        jvmArgs = listOf("-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=7778")
    }

    tasks.withType<KotlinCompile> {
        kotlinOptions {
            freeCompilerArgs += "-Xjsr305=strict"
            jvmTarget = "21"
        }
    }

    tasks.withType<Test> {
        useJUnitPlatform()
    }

//    あとでfrontendにもってきたい
    tasks.register("webpack", Task::class) {
        dependsOn("cleanGen")
        mustRunAfter("cleanGen")
    }

    tasks.named("build") {
        dependsOn("webpack")
        mustRunAfter("webpack")
    }
//あとでfrontendにもってきたい

    dependencyManagement {
        dependencies {
            dependencySet(mapOf("group" to "org.mybatis.spring.boot", "version" to "3.0.3")) {
                entry("mybatis-spring-boot-starter")
            }
            dependencySet(mapOf("group" to "commons-io", "version" to "2.15.1")) {
                entry("commons-io")
            }
            dependencySet(mapOf("group" to "org.apache.pdfbox", "version" to "3.0.1")) {
                entry("pdfbox")
            }
            dependencySet(mapOf("group" to "com.googlecode.juniversalchardet", "version" to "1.0.3")) {
                entry("juniversalchardet")
            }
            dependencySet(mapOf("group" to "io.mockk", "version" to "1.13.10")) {
                entry("mockk")
            }
        }
    }

    dependencies {
        implementation("org.springframework.boot:spring-boot-starter-web")
        implementation("org.springframework.boot:spring-boot-starter-data-jpa")
        implementation("org.springframework.boot:spring-boot-starter-validation")
        implementation("org.springframework.boot:spring-boot-starter-security")
        implementation("org.springframework.boot:spring-boot-starter-oauth2-resource-server")
        implementation("org.springframework.security:spring-security-config")
        implementation("org.mybatis.spring.boot:mybatis-spring-boot-starter")
        implementation("com.googlecode.juniversalchardet:juniversalchardet")
        implementation("org.postgresql:postgresql")
        implementation("commons-io:commons-io")
        implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-csv")
        implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310")
        implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
        implementation("org.apache.pdfbox:pdfbox")
        implementation("org.jetbrains.kotlin:kotlin-reflect")
        testImplementation("io.mockk:mockk")
        testImplementation("org.springframework.boot:spring-boot-starter-test")
        testImplementation("org.springframework.security:spring-security-test")
        compileOnly("org.projectlombok:lombok")
        annotationProcessor("org.projectlombok:lombok")
        developmentOnly("org.springframework.boot:spring-boot-devtools")
    }
}

project(":frontend") {
    apply(plugin = "com.github.node-gradle.node")

    configure<NodeExtension> {
        version = "18.15.0"
        download = true
    }
}








