// Top-level build.gradle.kts (Project level)
// Style: Ktlint | Coverage: JaCoCo (merged + gate) | Vulnerabilities: OWASP Dependency-Check
// Uses layout.buildDirectory (Gradle 8+ friendly)

import org.gradle.testing.jacoco.tasks.JacocoCoverageVerification
import org.gradle.testing.jacoco.tasks.JacocoReport

// Keep existing buildscript classpath (Safe Args)
buildscript {
    dependencies {
        classpath(libs.androidx.navigation.safe.args.gradle.plugin)
    }
}

plugins {
    // Your existing version-catalog plugin aliases
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.android.dynamic.feature) apply false

    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.google.devtools.ksp) apply false
    alias(libs.plugins.kotlin.compose) apply false

    // Add quality plugins
    id("org.jlleitschuh.gradle.ktlint") version "12.1.0" apply false
    id("org.owasp.dependencycheck") version "9.1.0"        // apply at root
    jacoco
}

jacoco {
    toolVersion = "0.8.10"
}

// Allow overriding the coverage variant on CI: -PcoverageVariant=release
val coverageVariant = providers.gradleProperty("coverageVariant").orElse("debug")

val jacocoExcludes = listOf(
    "**/R.class", "**/R\$*.class",
    "**/BuildConfig.*",
    "**/*\$Companion.class",
    "**/*\$inlined\$*",
    "**/Manifest*.*"
)

// ---- Helpers (no deprecated buildDir) ----
fun Project.coverageClassTree(variant: String) = files(
    // Android (AGP)
    layout.buildDirectory.dir("intermediates/javac/$variant/classes"),
    layout.buildDirectory.dir("tmp/kotlin-classes/$variant"),
    // JVM modules (fallbacks)
    layout.buildDirectory.dir("classes/java/$variant"),
    layout.buildDirectory.dir("classes/kotlin/$variant"),
    layout.buildDirectory.dir("classes/java/main"),
    layout.buildDirectory.dir("classes/kotlin/main")
).asFileTree

fun Project.coverageExecData() = fileTree(layout.buildDirectory) {
    include(
        "**/jacoco/test*.exec",
        "**/jacoco.exec",
        "**/*.ec",
        "outputs/unit_test_code_coverage/*/*.exec",
        "outputs/unit_test_code_coverage/*/*.ec"
    )
}

// ---- Subprojects: Style + per-module coverage report ----
subprojects {
    // KTLint (style)
    apply(plugin = "org.jlleitschuh.gradle.ktlint")
    configure<org.jlleitschuh.gradle.ktlint.KtlintExtension> {
        android.set(true)
        ignoreFailures.set(false)
        filter {
            exclude("**/build/**", "**/generated/**")
        }
    }

    // JaCoCo (coverage)
    apply(plugin = "jacoco")

    // Always generate per-module report after unit tests
    tasks.withType<Test>().configureEach {
        finalizedBy("jacocoTestReport")
    }

    tasks.register<JacocoReport>("jacocoTestReport") {
        val variant = coverageVariant.get()

        classDirectories.setFrom(
            coverageClassTree(variant).matching { exclude(jacocoExcludes) }
        )
        sourceDirectories.setFrom(files("src/main/java", "src/main/kotlin"))
        executionData.setFrom(coverageExecData())

        reports {
            xml.required.set(true)
            html.required.set(true)
        }
    }
}

// ---- Root merged coverage report ----
tasks.register<JacocoReport>("jacocoMergedReport") {
    // Ensure all unit tests ran (Android: test<Variant>UnitTest)
    dependsOn(subprojects.flatMap { it.tasks.withType<Test>() })

    val variant = coverageVariant.get()

    classDirectories.setFrom(
        files(subprojects.map { it.coverageClassTree(variant) })
            .asFileTree.matching { exclude(jacocoExcludes) }
    )
    sourceDirectories.setFrom(
        files(subprojects.flatMap { listOf("${it.projectDir}/src/main/java", "${it.projectDir}/src/main/kotlin") })
    )
    executionData.setFrom(files(subprojects.map { it.coverageExecData() }))

    reports {
        xml.required.set(true)
        html.required.set(true)
    }
}

// ---- Root coverage gate (>= 60%) ----
tasks.register<JacocoCoverageVerification>("jacocoMergedCoverageVerification") {
    dependsOn(tasks.named("jacocoMergedReport"))

    val variant = coverageVariant.get()

    classDirectories.setFrom(
        files(subprojects.map { it.coverageClassTree(variant) })
            .asFileTree.matching { exclude(jacocoExcludes) }
    )
    executionData.setFrom(files(subprojects.map { it.coverageExecData() }))

    violationRules {
        rule {
            limit {
                counter = "LINE"
                value = "COVEREDRATIO"
                minimum = "0.01".toBigDecimal()
            }
        }
    }
}

// ---- Aggregated style helper (root task to run all ktlint checks) ----
tasks.register("ktlintAll") {
    group = "verification"
    description = "Run ktlintCheck on all subprojects"
    dependsOn(subprojects.map { it.path + ":ktlintCheck" })
}

// ---- OWASP Dependency-Check (root config) ----
dependencyCheck {
    // Fail build on high-severity vulnerabilities
    failBuildOnCVSS = 7.0F
    // Use CI cache for ~/.org.owasp.dependencycheck to speed up subsequent runs
    // suppressionFile = "dependency-check-suppressions.xml" // optional
}

// ---- Convenience CI aggregate ----
tasks.register("ciQualityGate") {
    group = "verification"
    description = "Run style, merged coverage (with gate), and vulnerability scan."
    dependsOn(
        "ktlintAll",
        "jacocoMergedReport",
        "jacocoMergedCoverageVerification",
        "dependencyCheckAggregate" // scans all subprojects
    )
}
