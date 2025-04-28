plugins {
    alias(libs.plugins.expensemanager.jvm.library)
}
dependencies {
    implementation(projects.core.domain)
}