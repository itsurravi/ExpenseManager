plugins {
    alias(libs.plugins.expensemanager.android.library)
}

android {
    namespace = "com.ravikantsharma.data"
}

dependencies {
    implementation(libs.timber)
    implementation(libs.bundles.koin)
    implementation(libs.androidx.security.crypto)
    implementation(libs.play.services.time)

    implementation(projects.core.domain)
    implementation(projects.core.database)
}