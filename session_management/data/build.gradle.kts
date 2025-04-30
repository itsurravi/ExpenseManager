plugins {
    alias(libs.plugins.expensemanager.android.library)
}

android {
    namespace = "com.ravikantsharma.session_management.data"
}

dependencies {
    implementation(libs.bundles.koin)
    implementation(libs.androidx.datastore.preference)
    implementation(libs.kotlinx.coroutines.core)

    implementation(projects.core.domain)
    implementation(projects.sessionManagement.domain)
}