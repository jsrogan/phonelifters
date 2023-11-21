package com.phonelifters.armenu

//package de.yanneckreiss.cameraxtutorial.ui.features.camera.photo_capture needed?
/*
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.*
import android.hardware.camera2.CameraCaptureSession
import android.hardware.camera2.CameraDevice
import android.hardware.camera2.CameraManager
import android.hardware.camera2.CaptureRequest
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.util.Log
import android.view.Surface
import android.view.TextureView
import android.widget.ImageView
import com.xperiencelabs.armenu.ml.LiteModelMovenetSingleposeLightningTfliteFloat164
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.image.ImageProcessor
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.image.ops.ResizeOp
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
class PoseActivity : AppCompatActivity() {

    val paint = Paint()
    lateinit var imageProcessor: ImageProcessor
    lateinit var model: LiteModelMovenetSingleposeLightningTfliteFloat164
    lateinit var bitmap: Bitmap
    lateinit var imageView: ImageView
    lateinit var handler:Handler
    lateinit var handlerThread: HandlerThread
    lateinit var textureView: TextureView
    lateinit var cameraManager: CameraManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pose) //put Compose here
        get_permissions()
        imageProcessor = ImageProcessor.Builder().add(ResizeOp(192, 192, ResizeOp.ResizeMethod.BILINEAR)).build()
        model = LiteModelMovenetSingleposeLightningTfliteFloat164.newInstance(this)
        //imageView = findViewById(R.id.imageView) //separate composable, replace with Image or AsyncImage
        imageView = findViewById(R.id.imageView)
        textureView = findViewById(R.id.textureView) //separate composable, need SurfaceTexture to put in an AndroidView
        cameraManager = getSystemService(Context.CAMERA_SERVICE) as CameraManager
        handlerThread = HandlerThread("videoThread")
        handlerThread.start()
        handler = Handler(handlerThread.looper)

        paint.setColor(Color.YELLOW)

        textureView.surfaceTextureListener = object:TextureView.SurfaceTextureListener{
            override fun onSurfaceTextureAvailable(p0: SurfaceTexture, p1: Int, p2: Int) {
                open_camera()
            }

            override fun onSurfaceTextureSizeChanged(p0: SurfaceTexture, p1: Int, p2: Int) {

            }

            override fun onSurfaceTextureDestroyed(p0: SurfaceTexture): Boolean {
                return false
            }

            override fun onSurfaceTextureUpdated(p0: SurfaceTexture) {
                bitmap = textureView.bitmap!!
                var tensorImage = TensorImage(DataType.UINT8)
                tensorImage.load(bitmap)
                tensorImage = imageProcessor.process(tensorImage)

                val inputFeature0 = TensorBuffer.createFixedSize(intArrayOf(1, 192, 192, 3), DataType.UINT8)
                inputFeature0.loadBuffer(tensorImage.buffer)

                val outputs = model.process(inputFeature0)
                val outputFeature0 = outputs.outputFeature0AsTensorBuffer.floatArray

                var mutable = bitmap.copy(Bitmap.Config.ARGB_8888, true)
                var canvas = Canvas(mutable)
                var h = bitmap.height
                var w = bitmap.width
                var x = 0

                Log.d("output__", outputFeature0.size.toString())
                while(x <= 49){
                    if(outputFeature0.get(x+2) > 0.45){
                        canvas.drawCircle(outputFeature0.get(x+1)*w, outputFeature0.get(x)*h, 10f, paint)
                    }
                    x+=3
                }

                imageView.setImageBitmap(mutable)
            }
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        model.close()
    }

    @SuppressLint("MissingPermission")
    fun open_camera(){
        cameraManager.openCamera(cameraManager.cameraIdList[0], object:CameraDevice.StateCallback(){
            override fun onOpened(p0: CameraDevice) {
                var captureRequest = p0.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW)
                var surface = Surface(textureView.surfaceTexture)
                captureRequest.addTarget(surface)
                p0.createCaptureSession(listOf(surface), object:CameraCaptureSession.StateCallback(){
                    override fun onConfigured(p0: CameraCaptureSession) {
                        p0.setRepeatingRequest(captureRequest.build(), null, null)
                    }
                    override fun onConfigureFailed(p0: CameraCaptureSession) {

                    }
                }, handler)
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

sources: https://www.youtube.com/watch?v=pPVZambOuG8,
https://github.com/YanneckReiss/JetpackComposeCameraXShowcase/blob/master/app/src/main/kotlin/de/yanneckreiss/cameraxtutorial/MainActivity.kt,
https://developer.android.com/codelabs/camerax-getting-started#1,
https://github.com/Pawandeep-prog/realtime_pose_detection_android/tree/main,
https://www.geeksforgeeks.org/imageview-in-android-using-jetpack-compose/,
https://developer.android.com/training/sharing/send#:~:text=Android%20uses%20the%20action%20ACTION_SEND,displays%20them%20to%20the%20user.,
https://stackoverflow.com/questions/36990054/android-studio-resolving-duplicate-classes,
https://developer.android.com/build/dependencies#resolution_errors,
https://github.com/Pawandeep-prog/realtime_pose_detection_android,
https://github.com/tensorflow/tensorflow/issues/46356,
https://stackoverflow.com/a/68312016,
https://firebase.google.com/docs/ml/android/use-custom-models,
https://stackoverflow.com/a/7574735,
https://stackoverflow.com/a/69846812,
https://stackoverflow.com/a/66942101,
https://stackoverflow.com/a/69846812,
https://stackoverflow.com/a/62891524,
https://www.javatpoint.com/kotlin-android-explicit-intent,
https://www.diffchecker.com/,
https://www.geeksforgeeks.org/implicit-and-explicit-intents-in-android-with-examples/,
https://stackoverflow.com/a/69846812,
https://github.com/Pawandeep-prog/realtime_pose_detection_android,
https://developer.android.com/jetpack/androidx/releases/compose-kotlin,
https://github.com/tensorflow/tensorflow/issues/46356,
https://docs.gradle.org/current/dsl/org.gradle.api.artifacts.ResolutionStrategy.html#org.gradle.api.artifacts.ResolutionStrategy:preferProjectModules(),
https://docs.gradle.org/current/dsl/org.gradle.api.artifacts.ResolutionStrategy.html#org.gradle.api.artifacts.ResolutionStrategy,
https://mvnrepository.com/artifact/org.tensorflow/tensorflow-lite-metadata,
https://tfhub.dev/google/movenet/singlepose/lightning/4,
https://developer.android.com/topic/libraries/support-library/packages,
https://lightrun.com/answers/objectbox-objectbox-java-flatbuffers-version-conflicts-with-other-library,
https://developers.google.com/ml-kit/vision/pose-detection/android,
https://github.com/googlesamples/mlkit/tree/master/android/vision-quickstart,
https://stackoverflow.com/a/56029604,
 */