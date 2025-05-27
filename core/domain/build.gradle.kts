plugins {
    alias(libs.plugins.expensemanager.jvm.library)
}
dependencies {
    implementation(libs.kotlinx.coroutines.core)
    testImplementation(libs.junit.junit)
}
