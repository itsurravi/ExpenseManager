plugins {
//    alias(libs.plugins.android.application)
//    alias(libs.plugins.jetbrainsKotlinAndroid) apply false
//    alias(libs.plugins.compose.compiler) apply false
    alias(libs.plugins.expensemanager.android.application.compose)
}

android {
    namespace = "com.ravikantsharma.expensemanager"

    defaultConfig {
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
}

dependencies {
    // Use the Compose bundle
    implementation(libs.bundles.compose)
    implementation(libs.androidx.navigation.compose)

    // Core
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.material.icons.extended)

    // Crypto
    implementation(libs.androidx.security.crypto.ktx)

    // Testing dependencies
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.bundles.compose.test)

    // Debug dependencies
    debugImplementation(libs.bundles.compose.debug)

    // Timber
    implementation(libs.timber)

    implementation(libs.bundles.koin)

    // Implement local modules
    implementation(projects.auth.domain)
    implementation(projects.auth.data)
    implementation(projects.auth.presentation)

    implementation(projects.core.domain)
    implementation(projects.core.database)
    implementation(projects.core.data)
    implementation(projects.core.presentation.designsystem)
    implementation(projects.core.presentation.ui)
}