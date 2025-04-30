plugins {
    alias(libs.plugins.expensemanager.jvm.library)
}
dependencies {
    implementation(libs.kotlinx.coroutines.core)
    implementation(projects.core.domain)
}