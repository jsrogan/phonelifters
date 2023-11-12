package com.xperiencelabs.armenu

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.ar.core.dependencies.i
import com.xperiencelabs.armenu.ui.theme.Gray88
import com.xperiencelabs.armenu.ui.theme.HeavenWhite
import androidx.navigation.NavHostController
import com.xperiencelabs.armenu.ui.theme.arsenic
import com.xperiencelabs.armenu.ui.theme.lightBlue

class Exercise(name: String, model: String)
{
    var exerciseName = ""
    var demoModel = ""
    //var id = 0
    init
    {
        exerciseName = name
        demoModel = model
        //id = ID
    }

    fun getName(): String
    {
        return exerciseName
    }

    fun getModel(): String
    {
        return demoModel
    }

    /*
    fun getId(): Int
    {
        return id
    }
     */
}

class ExerciseMenu(e: Exercise)
{
    var exercises = arrayOf(e)

    /*
    fun getExercise(name: String) : Exercise?
    {
        if (exercises.contains(name))
        {
            return exercises[name]
        }

        return Exercise("exercise not found", "", -1)
        //CHECK
    }
     */

    @Composable
    fun ShowExercises(index: Int, exerciseList: Array<Exercise>, navController: NavHostController) {
        Column(modifier = Modifier
            .padding(8.dp, 0.dp, 8.dp, 0.dp)
            .background(color = if (index % 2 == 0) Gray88 else HeavenWhite)) {
            Row(horizontalArrangement = Arrangement.SpaceBetween, modifier= Modifier.fillMaxWidth(1f).background(color = lightBlue)) {
                Text("Pick an Exercise", color = arsenic, fontSize = 30.sp, textAlign = TextAlign.Center)
            }

            Row(horizontalArrangement = Arrangement.SpaceBetween, modifier= Modifier.fillMaxWidth(1f)) {
                for (exercise: Exercise in exerciseList)
                {
                    //Text(exercise.exerciseName, color = lightBlue, fontSize = 30.sp, textAlign = TextAlign.Center)
                    exercise.exerciseName?.let { Text(it, fontSize = 17.sp, modifier = Modifier.padding(4.dp, 8.dp, 4.dp, 0.dp)) }

                    IconButton(onClick = { navController.navigate("DisplayExercise") }) {
                        Icon(imageVector = ImageVector.vectorResource(R.drawable.start_exercise_button),
                            contentDescription = stringResource(R.string.exercise_select), //CHECK change to actual exercise name
                            modifier = Modifier.scale(1.4f),
                            //tint = Color.Green
                        )
                    }

                }
            }
        }
    }

}

@Composable
fun MainView(exerciseMenu: ExerciseMenu, list: Array<Exercise>, navController: NavHostController)
{
    exerciseMenu.ShowExercises(index = 0, exerciseList = list, navController = navController)
}

/*
sources:
https://developers.google.com/ml-kit/vision/pose-detection/android,
https://kotlinlang.org/docs/classes.html#constructors,
https://kotlinlang.org/docs/properties.html#getters-and-setters,
https://www.simplilearn.com/tutorials/kotlin-tutorial/guide-to-kotlin-constructors,
https://kotlinlang.org/docs/arrays.html#when-to-use-arrays,
https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-map/,
https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-hash-map/,
https://stackoverflow.com/questions/44905012/method-hashmapof-in-kotlin,
https://www.baeldung.com/kotlin/initialize-map,
https://www.geeksforgeeks.org/searchview-in-android-with-kotlin/,
https://kotlinlang.org/docs/control-flow.html#for-loops,
https://developer.android.com/develop/ui/views/components/menus
 */
