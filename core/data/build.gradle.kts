plugins {
    alias(libs.plugins.expensemanager.android.library)
}

android {
    namespace = "com.ravikantsharma.data"
}

dependencies {
    implementation(libs.timber)
    implementation(libs.bundles.koin)
    implementation(projects.core.domain)
}