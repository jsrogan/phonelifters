package edu.umich.pockettrainer

import android.graphics.Paint
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material3.Button
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.Placeable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.google.ar.core.Config
import edu.umich.pockettrainer.ui.theme.PocketTrainerTheme
import io.github.sceneview.ar.ARScene
import io.github.sceneview.ar.node.ArModelNode
import io.github.sceneview.ar.node.ArNode
import io.github.sceneview.ar.node.PlacementMode

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
    }
}
/*
@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}
*/

@Composable
fun Exercises(modifier: Modifier, onClick: (Int) -> Unit)
{
    val exercisesList = listOf(Exercise("arm curl", 1))
    var currentExercise by remember { mutableStateOf(1) }

    fun updateExercise(offset: Int)
    {
        currentExercise = (currentExercise + exercisesList.size) % exercisesList.size
        onClick(exercisesList[currentExercise].exeriseID)
    }

    Row(modifier = modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceAround)
    {
        IconButton(onClick = ({/*do later*/})) //Try It Myself button
    }
}

@Composable
fun ExerciseScreen(model: Int)
{
    val nodes = remember { mutableListOf<ArNode>() }
    val modelNode = remember { mutableStateOf<ArModelNode?>(null) }
    val placeAvatar = remember {
        mutableStateOf(false)
}

    Box(modifier = Modifier.fillMaxSize()) {
        ARScene(
            modifier = Modifier.fillMaxSize(),
            nodes = nodes,
            planeRenderer = true,
            onCreate = {arSceneView -> arSceneView.lightEstimationMode = Config.LightEstimationMode.DISABLED
                arSceneView.planeRenderer.isShadowReceiver = false
                modelNode.value = ArModelNode(arSceneView.engine, PlacementMode.INSTANT).apply {
                    loadModelGlbAsync(glbFileLocation = "Avatar file location here") {

                    }

                    onAnchorChanged = {
                        placeAvatar.value = !isAnchored
                    }

                    onHitResult = { node, hitResult ->
                        placeAvatar.value = node.isTracking
                    }
                    nodes.add(modelNode.value!!)
                }
            },
            onSessionCreate = {
                planeRenderer.isVisible = false
            }
        )

        if (placeAvatar.value)
        {
            Button(onClick = {
                modelNode.value?.anchor()
            }, modifier = Modifier.align(Alignment.Center)) {
                Text(text = "Show Avatar Demonstrating Exercise")
            }
        }
    }

    LaunchedEffect(key1 = model){
        modelNode.value?.loadModelGlbAsync(glbFileLocation = "AR Avatar directory here")
        Log.e("error", "Couldn't load exercise.")
    }
    
}

@Composable
fun DisplayExercise()
{
    Box(modifier = Modifier.fillMaxSize()) {
        val currentExercise = remember {
            mutableStateOf(1)
        }
        ExerciseScreen(currentExercise.value)
        Exercises(modifier = Modifier.align(Alignment.BottomCenter)) {
            currentExercise.value = it
        }
    }
}

data class Exercise(var name: String, var exeriseID: Int)
{

}



/*
@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    PocketTrainerTheme {
        Greeting("Android")
    }
}
*/

/*
sources: https://github.com/princeku07/AR-Menu-App---Android-Jetpack-Compose-/blob/main/app/src/main/java/com/xperiencelabs/armenu/MainActivity.kt,
https://www.youtube.com/watch?app=desktop&v=rb5m0Py8y1s,
https://github.com/SceneView/sceneview-android/tree/main
 */