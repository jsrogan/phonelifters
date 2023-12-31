package com.phonelifters.armenu

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.phonelifters.armenu.ui.theme.Gray88
import com.phonelifters.armenu.ui.theme.HeavenWhite
import androidx.navigation.NavHostController
import com.phonelifters.armenu.ui.theme.arsenic
import com.phonelifters.armenu.ui.theme.lightBlue

class Exercise(name: String, model: String)
{
    var exerciseName = ""
    var demoModel = ""
    
    init
    {
        exerciseName = name
        demoModel = model
        
    }

    fun getName(): String
    {
        return exerciseName
    }

    fun getModel(): String
    {
        return demoModel
    }


}

class ExerciseMenu(e: Exercise)
{
    var exercises = arrayOf(e)

    fun addExercise(e: Exercise){
        exercises = exercises.plus(e)
    }



    @Composable
    fun ShowExercises(index: Int, exerciseList: Array<Exercise>, navController: NavHostController) {
        Column(modifier = Modifier
            .padding(8.dp, 0.dp, 8.dp, 0.dp)
            .background(color = if (index % 2 == 0) Gray88 else HeavenWhite)) {
            Row(horizontalArrangement = Arrangement.SpaceBetween, modifier= Modifier.fillMaxWidth(1f).background(color = lightBlue)) {
                Text("Pick an Exercise", color = arsenic, fontSize = 30.sp, textAlign = TextAlign.Center)
            }

            for (exercise: Exercise in exerciseList)
            {
                Row(horizontalArrangement = Arrangement.SpaceBetween, modifier= Modifier.fillMaxWidth(1f)) {
                    exercise.exerciseName?.let { Text(it, fontSize = 17.sp, modifier = Modifier.padding(4.dp, 8.dp, 4.dp, 0.dp)) }
                    val currentExercise = exercise.getModel()
                    val navString = "DisplayExercise/$currentExercise"
                    IconButton(onClick = { navController.navigate(navString) }) {
                        Icon(imageVector = ImageVector.vectorResource(R.drawable.start_exercise_button),
                            contentDescription = stringResource(R.string.exercise_select), 
                            modifier = Modifier.scale(1.4f),
                            
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
