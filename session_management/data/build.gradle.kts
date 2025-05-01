plugins {
    alias(libs.plugins.expensemanager.android.library)
    alias(libs.plugins.protobuf)
}

android {
    namespace = "com.ravikantsharma.session_management.data"
}

protobuf {
    protoc {
        artifact = libs.protobuf.protoc.get().toString()
    }
    generateProtoTasks {
        all().forEach { task ->
            task.builtins {
                register("java") {
                    option("lite")
                }
                register("kotlin") {
                    option("lite")
                }
            }
        }
    }
}

dependencies {
    implementation(libs.bundles.koin)
    implementation(libs.androidx.datastore.preference)
    implementation(libs.kotlinx.coroutines.core)
    api(libs.protobuf.kotlin.lite)

    implementation(projects.core.domain)
    implementation(projects.sessionManagement.domain)
}