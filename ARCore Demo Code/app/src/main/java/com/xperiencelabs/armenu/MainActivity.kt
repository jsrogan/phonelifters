package com.xperiencelabs.armenu


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.xperiencelabs.armenu.ExerciseMenu
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val navController = rememberNavController()
            val exercise = Exercise("arm curl", "bigtest.glb")
            val exerciseMenu = ExerciseMenu(exercise)
            exerciseMenu.ShowExercises(index = 0, exerciseList = exerciseMenu.exercises, navController = navController)
        }
    }
}

/*
class MainActivity : ComponentActivity() {
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
                          mutableStateOf("bigtest")
                      }
                      ARScreen(currentModel.value)
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

    /*

    Food("burger",R.drawable.burger),
        Food("instant",R.drawable.instant),
        Food("momos",R.drawable.momos),
        Food("pizza",R.drawable.pizza),
        Food("ramen",R.drawable.ramen),
     */

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
fun ARScreen(model:String) {
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

        /*
        if(placeModelButton.value){
            Button(onClick = {
                modelNode.value?.anchor()
            }, modifier = Modifier.align(Alignment.Center)) {
                Text(text = "Anchor")
            }
        }
        */

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
*/

/*
sources: https://github.com/princeku07/AR-Menu-App---Android-Jetpack-Compose-/tree/main,
https://github.com/SceneView/sceneview-android/tree/main,
https://www.mixamo.com/#/?page=1&query=bicep+curl,
https://products.aspose.app/3d/conversion/dae-to-glb,
https://github.com/SceneView/sceneform-android/blob/master/samples/ar-model-viewer/build.gradle,
https://blender.stackexchange.com/questions/68001/working-with-very-large-objects,
https://github.com/SceneView/sceneview-android,
https://docs.blender.org/manual/en/latest/scene_layout/object/editing/transform/scale.html,
https://stackoverflow.com/questions/67577120/conversion-of-dae-to-glb-gltf,
https://stackoverflow.com/a/54469912
 */




