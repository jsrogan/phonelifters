package com.phonelifters.armenu

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val armCurl = Exercise("Arm Curl", "bigtest")
            val squat = Exercise("Squat", "squat3")
            val swing = Exercise("Kettlebell Swing", "kbswing4")
            val exerciseMenu = ExerciseMenu(armCurl)
            exerciseMenu.addExercise(squat)
            exerciseMenu.addExercise(swing)
            val navController = rememberNavController()
            val context = LocalContext.current
            NavHost(navController, startDestination = "ExerciseMenu") {
                composable("ExerciseMenu") {
                    MainView(exerciseMenu, exerciseMenu.exercises, navController = navController)
                
                }

                composable("DisplayExercise/{modelId}",
                    arguments = listOf(navArgument("modelId") {

                    }))
                {
                        backStackEntry ->
                    ARScreen(backStackEntry.arguments?.getString("modelId"), navController, context)
                    
                }



                composable("PoseView"){
                    PoseView(navController)
                }
            }
        }
    }
}

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
https://stackoverflow.com/a/54469912,
https://www.youtube.com/watch?v=rb5m0Py8y1s
 */
