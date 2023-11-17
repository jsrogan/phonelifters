package com.programminghut.pose_detection

import android.annotation.SuppressLint
import androidx.activity.ComponentActivity
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
import androidx.core.app.ActivityCompat.requestPermissions
import com.github.doyaaaaaken.kotlincsv.dsl.csvWriter
import com.programminghut.pose_detection.ml.LiteModelMovenetSingleposeLightningTfliteFloat164
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.image.ImageProcessor
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.image.ops.ResizeOp
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import java.io.FileWriter

class MainActivity : AppCompatActivity() {

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
        setContentView(R.layout.activity_main)
        get_permissions()

        imageProcessor = ImageProcessor.Builder().add(ResizeOp(192, 192, ResizeOp.ResizeMethod.BILINEAR)).build()
        model = LiteModelMovenetSingleposeLightningTfliteFloat164.newInstance(this)
        imageView = findViewById(R.id.imageView)
        textureView = findViewById(R.id.textureView)
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

                //Log.d("output__", outputFeature0.size.toString())
                var currentRow = mutableListOf<Float>()
                var a = 0f
                var b = 0f
                while(x <= 49){
                    if(outputFeature0.get(x+2) > 0.45){
                        canvas.drawCircle(outputFeature0.get(x+1)*w, outputFeature0.get(x)*h, 10f, paint)
                        //append to CSV file
                        a = outputFeature0.get(x+1)*w
                        b = outputFeature0.get(x)*h

                    }

                    else
                    {
                        a = 0f
                        b = 0f
                    }

                    /*
                    val a = outputFeature0.get(x+1)*w
                    val b = outputFeature0.get(x)*h
                    currentRow.add(a)
                    currentRow.add(b)
                     */
                    currentRow.add(a)
                    currentRow.add(b)
                    x+=3
                }

                //for (row in currentRow)
                //{
                    Log.d("coordinates", currentRow.toString())
                //}

                /*
                csvWriter().open("training.csv") {
                    writeRow(currentRow)
                }

                 */



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

/*
sources: https://github.com/Pawandeep-prog/realtime_pose_detection_android,
https://github.com/doyaaaaaken/kotlin-csv,
https://www.javatpoint.com/kotlin-android-read-and-write-internal-storage,
https://www.educba.com/kotlin-empty-list/,
https://developer.android.com/training/data-storage/shared/documents-files,
https://www.baeldung.com/kotlin/csv-files,
https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.io/java.io.-file/buffered-writer.html,
https://support.google.com/android/answer/9431959?hl=en,
https://medium.com/@sergei.rybalkin/upload-file-to-google-drive-with-kotlin-931cec5252c1,
https://github.com/rybalkinsd/kohttp,
https://developers.google.com/identity/protocols/oauth2,
https://stackoverflow.com/a/72481029,
https://proandroiddev.com/my-confusion-related-to-android-storage-management-related-apis-ed59c69b567b,
https://proandroiddev.com/everything-about-storage-on-android-2e9154882414,
https://developers.google.com/android/reference/com/google/android/gms/ads/nativead/MediaView,
https://tdcolvin.medium.com/demystifying-internal-vs-external-storage-in-modern-android-c9c31cb8eeec,
https://medium.com/swlh/sample-for-android-storage-access-framework-aka-scoped-storage-for-basic-use-cases-3ee4fee404fc,
https://www.makeuseof.com/android-grant-permissions-with-adb/,
https://www.programiz.com/kotlin-programming/for-loop,
https://stackoverflow.com/a/7718219,
https://support.google.com/docs/answer/6325535?hl=en&co=GENIE.Platform%3DDesktop
 */