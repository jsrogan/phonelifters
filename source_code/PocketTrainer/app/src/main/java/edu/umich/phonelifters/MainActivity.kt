package edu.umich.phonelifters

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.filament.View
import com.google.ar.core.ArCoreApk
import com.google.ar.core.dependencies.e
import com.google.ar.core.exceptions.UnavailableUserDeclinedInstallationException
import edu.umich.phonelifters.ui.theme.PocketTrainerTheme

//import com.google.ar.core

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Enable AR-related functionality on ARCore supported devices only.
        maybeEnableArButton()
    }

    fun maybeEnableArButton() {
        ArCoreApk.getInstance().checkAvailabilityAsync(this) { availability ->
            if (availability.isSupported) {
                mArButton.visibility = View.VISIBLE
                mArButton.isEnabled = true
            } else { // The device is unsupported or unknown.
                mArButton.visibility = View.INVISIBLE
                mArButton.isEnabled = false
            }
        }
    }

    var mUserRequestedInstall = true

    override fun onResume() {
        super.onResume()

        // Check camera permission.
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            if (!granted) {
                toast("Camera access denied") // Updated toast message
                finish()
            }
        }.launch(android.Manifest.permission.CAMERA) // Request CAMERA permission instead of RECORD_AUDIO #OUR CODE


        // Request the camera permission, if necessary.
        if (!CameraPermissionHelper.hasCameraPermission(this)) {
            CameraPermissionHelper.requestCameraPermission(this)
        }

        // Ensure that Google Play Services for AR and ARCore device profile data are
        // installed and up to date.
        try {
            if (mSession == null) {
                when (ArCoreApk.getInstance().requestInstall(this, mUserRequestedInstall)) {
                    ArCoreApk.InstallStatus.INSTALLED -> {
                        // Success: Safe to create the AR session.
                        mSession = Session(this)
                    }
                    ArCoreApk.InstallStatus.INSTALL_REQUESTED -> {
                        // When this method returns `INSTALL_REQUESTED`:
                        // 1. ARCore pauses this activity.
                        // 2. ARCore prompts the user to install or update Google Play
                        //    Services for AR (market://details?id=com.google.ar.core).
                        // 3. ARCore downloads the latest device profile data.
                        // 4. ARCore resumes this activity. The next invocation of
                        //    requestInstall() will either return `INSTALLED` or throw an
                        //    exception if the installation or update did not succeed.
                        mUserRequestedInstall = false
                        return
                    }
                }
            }
        } catch (e: UnavailableUserDeclinedInstallationException) {
            // Display an appropriate message to the user and return gracefully.
            Toast.makeText(this, "TODO: handle exception " + e, Toast.LENGTH_LONG)
                .show()
            return
        } catch (…) {
            return  // mSession remains null, since session creation has failed.
        }
    }

    /*
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        /*
        setContent {
            PocketTrainerTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Greeting("Android")
                }
            }
        }
        */

        registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            if (!granted) {
                toast("Camera access denied") // Updated toast message
                finish()
            }
        }.launch(android.Manifest.permission.CAMERA) // Request CAMERA permission instead of RECORD_AUDIO #OUR CODE
    }
    fun onCreate() {
        // Request the camera permission, if necessary.
        if (!CameraPermissionHelper.hasCameraPermission(this)) {
            CameraPermissionHelper.requestCameraPermission(this)
        }
    }
*/

}
/*
@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    PocketTrainerTheme {
        Greeting("Android")
    }
}
*/
/*
sources:
*https://stackoverflow.com/a/37834539,
https://stackoverflow.com/questions/7609270/not-able-to-access-adb-in-os-x-through-terminal-command-not-found,
https://stackoverflow.com/questions/10303639/adb-command-not-found,
https://developers.google.com/ar/develop/java/quickstart,
https://developers.google.com/ar/develop/java/emulator,
https://github.com/google-ar/arcore-android-sdk/releases,
https://stackoverflow.com/a/68255789,
https://github.com/google-ar/arcore-android-sdk,
https://developers.google.com/ar/devices,
https://developers.google.com/ar/develop/java/emulator#create_avd,
https://developers.google.com/ar/devices#emulators,
https://developer.android.com/reference/android/hardware/camera2/package-summary,
https://github.com/googlearchive/android-Camera2Basic
https://developers.google.com/ar/develop/java/camera-sharing#kotlin,
https://github.com/google-ar/arcore-android-sdk/blob/master/samples/shared_camera_java/app/src/main/java/com/google/ar/core/examples/java/common/helpers/CameraPermissionHelper.java,
https://github.com/google-ar/arcore-android-sdk/tree/master,
https://github.com/google-ar/arcore-android-sdk/tree/master/samples/shared_camera_java/app/src/main/java/com/google/ar/core/examples/java/common/helpers,
https://stackoverflow.com/a/18556886,
https://www.geeksforgeeks.org/how-to-add-external-library-in-android-studio/,
https://www.xda-developers.com/install-adb-windows-macos-linux/#what-is-android-debug-bridge-adb,
https://developer.android.com/tools/adb,
https://gitlab.eecs.umich.edu/sugih/graphics/-/wikis/Installing-ARCore-on-Android-Emulator#install,
https://support.google.com/pixelphone/thread/5184892/google-play-services-keeps-stopping-constant-pop-up?hl=en,
https://developers.google.com/ar/develop/java/emulator,
https://www.google.com/search?q=chip+info+mac+command+line&sca_esv=573232648&ei=8HYpZcTWBIiJptQPitWgkA8&ved=0ahUKEwjEjuOYv_OBAxWIhIkEHYoqCPIQ4dUDCBA&uact=5&oq=chip+info+mac+command+line&gs_lp=Egxnd3Mtd2l6LXNlcnAiGmNoaXAgaW5mbyBtYWMgY29tbWFuZCBsaW5lMgUQIRigATIFECEYqwIyBRAhGKsCMggQIRgWGB4YHUjADVDqAVigDHABeAGQAQCYAZIBoAHOCaoBBDEwLjO4AQPIAQD4AQHCAgoQABhHGNYEGLADwgIHECEYoAEYCuIDBBgAIEGIBgGQBgg&sclient=gws-wiz-serp,
https://www.computerworld.com/article/2726136/use-the-mac-s-command-line-to-get-detailed-cpu-information.html#:~:text=Type%20in%20the%20following%20command,article%20at%20the%20link%20below.,
https://developers.google.com/ar/develop/java/camera-sharing#kotlin,
https://kotlinlang.org/docs/coding-conventions.html#names-for-test-methods,
https://developers.google.com/ar/reference/java/com/google/ar/core/Session,
https://developers.google.com/ar/develop/java/quickstart,
https://github.com/google-ar/arcore-android-sdk/blob/master/samples/hello_ar_kotlin/build.gradle,
https://developers.google.com/ar/develop/java/enable-arcore,
https://developers.google.com/ar/develop/java/quickstart,
https://github.com/SceneView/sceneview-android,
https://developers.google.com/ar/develop/java/enable-arcore#kotlin,
https://www.youtube.com/watch?app=desktop&v=rb5m0Py8y1s,
https://github.com/SceneView/sceneview-android,
https://www.youtube.com/watch?v=EWXGaypl2ms&t=2s,
https://www.youtube.com/watch?v=MM786WD-Gv8
 */