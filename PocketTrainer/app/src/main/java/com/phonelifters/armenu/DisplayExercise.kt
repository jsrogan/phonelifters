package com.phonelifters.armenu

import android.app.PendingIntent.getActivity
import android.content.ActivityNotFoundException
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat.startActivity
import androidx.navigation.NavHostController
import com.google.ar.core.Config
import com.phonelifters.armenu.ui.theme.ARMenuTheme
import com.phonelifters.armenu.ui.theme.HeavenWhite
import com.phonelifters.armenu.ui.theme.Translucent
import com.phonelifters.armenu.ui.theme.arsenic
import io.github.sceneview.ar.ARScene
import io.github.sceneview.ar.node.ArModelNode
import io.github.sceneview.ar.node.ArNode
import io.github.sceneview.ar.node.PlacementMode
import android.content.pm.PackageManager

class DisplayExercise : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            ARMenuTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Box(modifier = Modifier.fillMaxSize()){
                        val currentModel = remember {
                            mutableStateOf("")
                        }
                        
                        Menu(modifier = Modifier.align(Alignment.BottomCenter)){
                            currentModel.value = it
                        }

                    }
                }
            }
        }
    }
}



@Composable
fun Menu(modifier: Modifier,onClick:(String)->Unit) {
    var currentIndex by remember {
        mutableStateOf(0)
    }


    val itemsList = listOf(
        Models("demo", R.drawable.demo)

    )
    fun updateIndex(offset:Int){
        currentIndex = (currentIndex+offset + itemsList.size) % itemsList.size
        onClick(itemsList[currentIndex].name)
    }
    Row(modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        IconButton(onClick = {
            updateIndex(-1)
        }) {
            Icon(painter = painterResource(id = R.drawable.baseline_arrow_back_ios_24), contentDescription ="previous" )
        }

        CircularImage(imageId = itemsList[currentIndex].imageId )

        IconButton(onClick = {
            updateIndex(1)
        }) {
            Icon(painter = painterResource(id = R.drawable.baseline_arrow_forward_ios_24), contentDescription ="next")
        }
    }

}

@Composable
fun CircularImage(
    modifier: Modifier=Modifier,
    imageId: Int
) {
    Box(modifier = modifier
        .size(140.dp)
        .clip(CircleShape)
        .border(width = 3.dp, Translucent, CircleShape)
    ){
        Image(painter = painterResource(id = imageId), contentDescription = null, modifier = Modifier.size(140.dp), contentScale = ContentScale.FillBounds)
    }
}

@Composable
fun ARScreen(model:String?, navHostController: NavHostController, context: Context) {
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
                modelNode.value = ArModelNode(arSceneView.engine,PlacementMode.INSTANT).apply {
                    loadModelGlbAsync(
                        glbFileLocation = "models/${model}.glb",
                        scaleToUnits = 0.8f,
                        autoAnimate = true,
                       
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
                                contentDescription = stringResource(R.string.exercise_select), 
                                modifier = Modifier.scale(1.4f),
                            )
                    }
                }
                Text("AR Exercise Demonstration", color = arsenic, fontSize = 30.sp)
            }

            Button(onClick = {

                Log.d("packages", context.packageManager.getInstalledPackages(0).toString())
                val poseIntent = context.packageManager.getLaunchIntentForPackage("com.programminghut.pose_detection")

                if (poseIntent != null)
                {
                    startActivity(context, poseIntent, null)
                }

            }, modifier = Modifier.align(Alignment.BottomCenter)) {
                Text(text = "Try exercise myself")
            }
        }

    }


    LaunchedEffect(key1 = model){
        modelNode.value?.loadModelGlbAsync(
            glbFileLocation = "models/${model}.glb",
            scaleToUnits = 0.8f
        )
        Log.e("errorloading","ERROR LOADING MODEL")
    }

}


data class Models(var name:String,var imageId:Int)


/*
sources: https://stackoverflow.com/a/72898717,
https://developer.android.com/reference/android/text/Layout.Alignment,
https://stackoverflow.com/a/22966322,
https://stackoverflow.com/a/64470532,
https://stackoverflow.com/a/4186097,
https://developer.android.com/training/basics/intents/filters,
https://developer.android.com/guide/app-actions/action-schema,
https://developer.android.com/training/basics/intents/sending,
https://developer.android.com/training/basics/intents/filters,
https://stackoverflow.com/a/76680021,
https://www.youtube.com/watch?v=tTnDCq2_kgg,
https://stackoverflow.com/a/11008893,
https://stackoverflow.com/a/12315939,
https://developer.android.com/reference/android/content/Intent.html,
https://developer.android.com/guide/app-actions/custom-intents,
https://stackoverflow.com/a/22396628,
https://developer.android.com/reference/kotlin/android/content/Context#getpackagemanager,
https://developer.android.com/reference/kotlin/android/content/Context#getPackageManager(),
https://developer.android.com/reference/android/content/Intent#CATEGORY_SELECTED_ALTERNATIVE,
https://stackoverflow.com/a/6758962,
https://stackoverflow.com/a/76680021,
https://stackoverflow.com/a/64479595,
https://developer.android.com/reference/android/content/pm/PackageManager#getInstalledApplications(int),
https://developer.android.com/reference/android/content/pm/PackageManager#getLaunchIntentForPackage(java.lang.String)
 */
