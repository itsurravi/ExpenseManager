plugins {
    alias(libs.plugins.expensemanager.android.feature.ui)
}

android {
    namespace = "com.ravikantsharma.auth.presentation"
}

dependencies {
    implementation(projects.core.domain)
    implementation(projects.auth.domain)
}