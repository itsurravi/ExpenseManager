plugins {
    alias(libs.plugins.expensemanager.android.library)
}

android {
    namespace = "com.ravikantsharma.auth.data"
}

dependencies {
    implementation(libs.bundles.koin)

    implementation(projects.core.domain)
    implementation(projects.auth.domain)
    api(projects.sessionManagement.data)
}