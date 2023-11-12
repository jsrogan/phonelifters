//package de.yanneckreiss.cameraxtutorial.ui.features.camera.photo_capture needed?
package com.xperiencelabs.armenu

import android.Manifest
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.ImageCapture
import androidx.camera.video.Recorder
import androidx.camera.video.Recording
import androidx.camera.video.VideoCapture
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.core.Preview
import androidx.camera.core.CameraSelector
import android.util.Log
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.video.FallbackStrategy
import androidx.camera.video.MediaStoreOutputOptions
import androidx.camera.video.Quality
import androidx.camera.video.QualitySelector
import androidx.camera.video.VideoRecordEvent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
//import androidx.compose.foundation.layout.RowScopeInstance.align
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.PermissionChecker
import androidx.navigation.NavHostController
import com.google.ar.core.Config
import com.xperiencelabs.armenu.ui.theme.HeavenWhite
import com.xperiencelabs.armenu.ui.theme.arsenic
import io.github.sceneview.ar.ARScene
import io.github.sceneview.ar.node.ArModelNode
import io.github.sceneview.ar.node.ArNode
import io.github.sceneview.ar.node.PlacementMode
import java.nio.ByteBuffer
import java.text.SimpleDateFormat
import java.util.Locale


@Composable
fun PoseView(navHostController: NavHostController) {
    val nodes = remember {
        mutableListOf<ArNode>()
    }
    val modelNode = remember {
        mutableStateOf<ArModelNode?>(null)
    }
    val placeModelButton = remember {
        mutableStateOf(false)
    }
    Box(modifier = Modifier.fillMaxSize()){
        ARScene(
            modifier = Modifier.fillMaxSize(),
            nodes = nodes,
            planeRenderer = true,
            onCreate = {arSceneView ->
                arSceneView.lightEstimationMode = Config.LightEstimationMode.DISABLED
                arSceneView.planeRenderer.isShadowReceiver = false
                modelNode.value = ArModelNode(arSceneView.engine, PlacementMode.INSTANT).apply {
                    loadModelGlbAsync(
                        glbFileLocation = "models/${model}.glb",
                        scaleToUnits = 0.8f,
                        autoAnimate = true,
                        //CHECK
                        //Rotation needs to be purposeful, not random, research anchor
                    ){

                    }
                    onAnchorChanged = {
                        placeModelButton.value = !isAnchored
                    }
                    onHitResult = {node, hitResult ->
                        placeModelButton.value = node.isTracking
                    }

                }
                nodes.add(modelNode.value!!)
            },
            onSessionCreate = {
                planeRenderer.isVisible = false
            }
        )

        if(placeModelButton.value){
            Column(modifier = Modifier
                .padding(8.dp, 0.dp, 8.dp, 0.dp)
                .background(color = HeavenWhite)) {
                Row(horizontalArrangement = Arrangement.SpaceBetween, modifier= Modifier.fillMaxWidth(1f)) {
                    IconButton(onClick = { navHostController.popBackStack() }) {
                        Icon(imageVector = ImageVector.vectorResource(R.drawable.back_button),
                            contentDescription = stringResource(R.string.exercise_select), //CHECK change to actual exercise name
                            modifier = Modifier.scale(1.4f),
                        )
                    }
                }
                Text("Try Exercise Yourself", color = arsenic, fontSize = 30.sp)
            }

            Button(onClick = {
                //val intent = Intent(context, PoseActivity::class.java)
                //context.startActivity(intent)
                navHostController.navigate("ExerciseMenu")
            }, modifier = Modifier.align(Alignment.BottomCenter)) {
                Text(text = "Done")
            }

        }

    }

}

/*
sources: https://www.youtube.com/watch?v=pPVZambOuG8,
https://github.com/YanneckReiss/JetpackComposeCameraXShowcase/blob/master/app/src/main/kotlin/de/yanneckreiss/cameraxtutorial/MainActivity.kt,
https://developer.android.com/codelabs/camerax-getting-started#1,
https://github.com/Pawandeep-prog/realtime_pose_detection_android/tree/main,
https://www.geeksforgeeks.org/imageview-in-android-using-jetpack-compose/,
https://developer.android.com/training/sharing/send#:~:text=Android%20uses%20the%20action%20ACTION_SEND,displays%20them%20to%20the%20user.,
https://developer.android.com/codelabs/camerax-getting-started#1,
https://stackoverflow.com/a/13977619,
https://stackoverflow.com/a/14779939,
https://developer.android.com/training/camerax/take-photo#kotlin,
https://stackoverflow.com/a/73752855
 */