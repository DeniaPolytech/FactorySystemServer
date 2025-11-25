plugins {
	java
	id("org.springframework.boot") version "3.5.6"
	id("io.spring.dependency-management") version "1.1.7"
}

group = "com.deniapolytech"
version = "0.0.1-SNAPSHOT"
description = "Сервер для корпоративного мессенджера"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(23)
	}
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation ("org.springframework.boot:spring-boot-starter-data-jpa")

	implementation ("io.jsonwebtoken:jjwt-api:0.11.5")
	implementation ("io.jsonwebtoken:jjwt-impl:0.11.5")
	implementation ("io.jsonwebtoken:jjwt-jackson:0.11.5")

	implementation ("org.springframework.security:spring-security-crypto:6.2.1")

	implementation("org.postgresql:postgresql:42.6.0")


	compileOnly("org.projectlombok:lombok:1.18.30")
	annotationProcessor("org.projectlombok:lombok:1.18.30")

	runtimeOnly("com.mysql:mysql-connector-j")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")


}

tasks.withType<Test> {
	useJUnitPlatform()
}

tasks.withType<JavaCompile> {
	options.compilerArgs.add("-Xlint:unchecked")
	options.compilerArgs.add("-Xlint:deprecation")
}
