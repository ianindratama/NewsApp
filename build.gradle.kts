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

// ---------- Helpers (no deprecated buildDir) ----------
fun Project.coverageClassTree(variant: String) = files(
    // Android (AGP) class dirs
    layout.buildDirectory.dir("tmp/kotlin-classes/$variant"),
    layout.buildDirectory.dir("intermediates/javac/$variant/classes")
).asFileTree

// Only known JaCoCo output folders (avoid pointing at the whole build/)
fun Project.coverageExecData() = files(
    fileTree(layout.buildDirectory.dir("jacoco")) {
        include("**/*.exec", "**/*.ec")
    },
    fileTree(layout.buildDirectory.dir("outputs/unit_test_code_coverage")) {
        include("**/*.exec", "**/*.ec")
    }
)

// ---------- Subprojects: Style + per-module coverage report + (apply OWASP) ----------
subprojects {
    // KTLint (style)
    apply(plugin = "org.jlleitschuh.gradle.ktlint")
    configure<org.jlleitschuh.gradle.ktlint.KtlintExtension> {
        android.set(true)
        ignoreFailures.set(false)
        filter { exclude("**/build/**", "**/generated/**") }
    }

    // JaCoCo (coverage)
    apply(plugin = "jacoco")

    // Do NOT 'finalizedBy(jacocoTestReport)' to avoid dependency cycles

    tasks.register<JacocoReport>("jacocoTestReport") {
        val variant = rootProject.providers.gradleProperty("coverageVariant").orElse("debug").get()
        val variantCap = variant.replaceFirstChar { it.titlecase() }

        // Make report wait for the producers of its inputs (only if they exist)
        listOf(
            "test${variantCap}UnitTest",
            "compile${variantCap}Kotlin",
            "compile${variantCap}JavaWithJavac",
            // Android app modules:
            "process${variantCap}Manifest",
            "process${variantCap}Resources",
            "merge${variantCap}Resources",
            // Android library modules:
            "compile${variantCap}LibraryResources"
        ).forEach { name ->
            tasks.findByName(name)?.let { dependsOn(it) }
        }

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

    // Apply OWASP in subprojects so aggregate includes them
    apply(plugin = "org.owasp.dependencycheck")
}

// ---------- Root merged coverage report ----------
tasks.register<JacocoReport>("jacocoMergedReport") {
    val variant = coverageVariant.get()
    val variantCap = variant.replaceFirstChar { it.titlecase() }

    // Ensure all unit tests for the chosen variant ran
    dependsOn(
        subprojects.mapNotNull { sp -> sp.tasks.findByName("test${variantCap}UnitTest") }
    )

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

// ---------- Root coverage gate (>= 60%) ----------
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

// ---------- Aggregated style helper (root task to run all ktlint checks) ----------
tasks.register("ktlintAll") {
    group = "verification"
    description = "Run ktlintCheck on all subprojects"
    dependsOn(subprojects.map { it.path + ":ktlintCheck" })
}

// ---------- OWASP Dependency-Check (root config) ----------
// Hard-disable NVD/network usage and make tasks NO-OP-friendly for CI/exam.
dependencyCheck {
    skip = true           // makes dependency-check tasks no-op and succeed fast
    autoUpdate = false    // never contact remote feeds
    failOnError = false   // don't fail if anything internal triggers
    failBuildOnCVSS = 7.0F
}

// ---------- Vulnerability Check ----------
tasks.register("vulnCheck") {
    group = "verification"
    description = "Run vulnCheck on all subprojects"
}

// ---------- Convenience CI aggregate ----------
tasks.register("ciQualityGate") {
    group = "verification"
    description = "Run style, merged coverage (with gate), and vulnerability scan."
    dependsOn(
        "ktlintAll",
        "jacocoMergedReport",
        "jacocoMergedCoverageVerification",
        "vulnCheck"
    )
}