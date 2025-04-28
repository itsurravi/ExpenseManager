plugins {
    alias(libs.plugins.expensemanager.android.library)
}

android {
    namespace = "com.ravikantsharma.dashboard.data"
}

dependencies {
    implementation(libs.bundles.koin)

    implementation(projects.core.domain)
    implementation(projects.dashboard.domain)
}