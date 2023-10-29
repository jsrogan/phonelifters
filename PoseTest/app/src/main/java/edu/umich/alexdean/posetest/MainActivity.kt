package edu.umich.alexdean.posetest

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.hardware.camera2.CameraCaptureSession
import android.hardware.camera2.CameraDevice
import android.hardware.camera2.CameraManager
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import android.view.Surface
import android.view.TextureView
import android.hardware.camera2.CaptureRequest
import android.os.Handler
import android.os.HandlerThread
import android.os.Parcel
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.google.android.gms.tasks.Task
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.pose.Pose
import com.google.mlkit.vision.pose.PoseDetection
import com.google.mlkit.vision.pose.PoseDetector
import com.google.mlkit.vision.pose.PoseLandmark
import com.google.mlkit.vision.pose.accurate.AccuratePoseDetectorOptions
import com.google.mlkit.vision.pose.defaults.PoseDetectorOptions
import edu.umich.alexdean.posetest.ui.theme.PoseTestTheme

class MainActivity : ComponentActivity() {
    lateinit var cameraManager: CameraManager
    lateinit var textureView: TextureView
    lateinit var handler: Handler
    lateinit var handlerThread: HandlerThread
    lateinit var imageAnalyzer: YourImageAnalyzer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)
        get_permissions()

    }

    @SuppressLint("MissingPermission")
    fun open_camera(){

        cameraManager.openCamera(cameraManager.cameraIdList[0], object: CameraDevice.StateCallback(){
            override fun onOpened(p0: CameraDevice) {
                var captureRequest = p0.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW)
                var surface = Surface(textureView.surfaceTexture)
                captureRequest.addTarget(surface)
                p0.createCaptureSession(listOf(surface), object: CameraCaptureSession.StateCallback(){
                    override fun onConfigured(p0: CameraCaptureSession) {
                        p0.setRepeatingRequest(captureRequest.build(), null, null)
                    }
                    override fun onConfigureFailed(p0: CameraCaptureSession) {

                    }
                }, handler)
                val parcel = Parcel.obtain()
                surface.writeToParcel(parcel, 0)
                val myBitmap = Bitmap.CREATOR.createFromParcel(parcel)
                imageAnalyzer.analyze(myBitmap)
            }

            override fun onDisconnected(p0: CameraDevice) {

            }

            override fun onError(p0: CameraDevice, p1: Int) {

            }
        }, handler)
    }

    fun get_permissions(){
        if(checkSelfPermission(android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            requestPermissions(arrayOf(android.Manifest.permission.CAMERA), 101)
        }
    }
    override fun onRequestPermissionsResult(  requestCode: Int, permissions: Array<out String>, grantResults: IntArray  ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(grantResults[0] != PackageManager.PERMISSION_GRANTED) get_permissions()
    }
}

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
    PoseTestTheme {
        Greeting("Android")
    }
}
class YourImageAnalyzer {

    fun analyze(bitmap: Bitmap) {
//        val mediaImage = imageProxy.image
//        if (mediaImage != null) {
        val image = InputImage.fromBitmap(bitmap, 0)
        getPoseDetection().process(image)
            .addOnSuccessListener { pose ->
                val builder = java.lang.StringBuilder()
                pose.allPoseLandmarks.forEach{
                    builder.appendLine("Landmark Type: "+it.landmarkType+ " Landmark Position "+it.position)
                }
                Log.e("Tag", builder.toString())

            }
            .addOnFailureListener{
                Log.e("Tag", it.message.toString())
            }
//                .addOnCompleteListener{
//                }
//        }
    }
    private fun getPoseDetection(): PoseDetector{
        val option = PoseDetectorOptions.Builder()
            .setDetectorMode(PoseDetectorOptions.STREAM_MODE)
            .build()
        return PoseDetection.getClient(option)
    }
}