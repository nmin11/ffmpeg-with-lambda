import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import com.github.jengelman.gradle.plugins.shadow.transformers.*

plugins {
	id("org.springframework.boot") version "2.7.6"
	id("io.spring.dependency-management") version "1.0.15.RELEASE"
	id("com.github.johnrengelman.shadow") version "7.1.2"
	id("org.springframework.boot.experimental.thin-launcher") version "1.0.28.RELEASE"
	kotlin("jvm") version "1.6.21"
	kotlin("plugin.spring") version "1.6.21"
	application
}

group = "video-handler"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_11

repositories {
	mavenCentral()
}

extra["springCloudVersion"] = "2021.0.5"

dependencies {
	implementation("org.springframework.boot:spring-boot-starter")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
	implementation("org.springframework.cloud:spring-cloud-function-context")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
}

dependencyManagement {
	imports {
		mavenBom("org.springframework.cloud:spring-cloud-dependencies:${property("springCloudVersion")}")
	}
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs = listOf("-Xjsr305=strict")
		jvmTarget = "11"
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}

tasks {
	named<ShadowJar>("shadowJar") {
		mergeServiceFiles()
		append("META-INF/spring.handlers")
		append("META-INF/spring.schemas")
		append("META-INF/spring.tooling")
		transform(PropertiesFileTransformer::class.java) {
			paths = mutableListOf("META-INF/spring.factories")
			mergeStrategy = "append"
		}
	}
}

application {
	mainClass.set("videohandler.lambda.LambdaApplicationKt")
}
