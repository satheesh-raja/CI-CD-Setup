// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version "8.2.2" apply false
    id("org.jetbrains.kotlin.android") version "1.9.0" apply false
}

tasks.register("copyGitHooks", Copy::class.java) {
    description = "Copies the git hooks from /git-hooks to the .git folder."
    group = "git hooks"
    from("$rootDir/scripts/pre-commit")
    into("$rootDir/.git/hooks/")
}
tasks.register("installGitHooks", Exec::class.java) {
    description = "Installs the pre-commit git hooks from /git-hooks."
    group = "git hooks"
    workingDir = rootDir
    commandLine = listOf("chmod")
    args("-R", "+x", ".git/hooks/")
    dependsOn("copyGitHooks")
    doLast {
        logger.info("Git hook installed successfully.")
    }
}

afterEvaluate {
    tasks.getByPath(":app:preBuild").dependsOn(":installGitHooks")
}