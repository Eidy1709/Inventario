// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    kotlin ("jvm") version "2.4.21"
    id("com.google.devtools.ksp") version "2.3.12"
    alias(libs.plugins.android.application) apply false
}