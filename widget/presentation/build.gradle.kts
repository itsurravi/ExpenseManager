plugins {
    alias(libs.plugins.expensemanager.android.feature.ui)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.ravikantsharma.widget.presentation"
}

dependencies {
    implementation(libs.androidx.navigation.compose)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.koin.androidx.compose)
    implementation(libs.androidx.glance.appwidget)
    implementation(libs.androidx.glance.material3)

    // Project dependencies
    implementation(projects.core.domain)
    implementation(projects.widget.domain)
}