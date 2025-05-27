plugins {
    alias(libs.plugins.expensemanager.android.library)
    alias(libs.plugins.expensemanager.android.room)
}

android {
    namespace = "com.ravikantsharma.core.database"
}

dependencies {
    implementation(libs.bundles.koin)
    implementation(libs.sqlcipher)
    implementation(libs.androidx.security.crypto)

    implementation(projects.core.domain)
}