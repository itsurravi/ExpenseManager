plugins {
    alias(libs.plugins.expensemanager.android.feature.ui)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.ravikantsharma.auth.presentation"
}

dependencies {
    implementation(libs.androidx.navigation.compose)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.koin.androidx.compose)

    implementation(projects.core.domain)
    implementation(projects.auth.domain)
    api(projects.sessionManagement.presentation)
}