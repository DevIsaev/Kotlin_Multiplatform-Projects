package org.example.project.Screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person3
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.painterResource

//функция элемента из списка
@Composable
fun listItem(name:String,status:Boolean){
    Card(modifier = Modifier.fillMaxWidth().padding(15.dp).pointerInput(UInt){

        detectDragGesturesAfterLongPress { change, dragAmount ->
//            Log.d("log","drag after long press $dragAmount")
        }
    },
        shape = RoundedCornerShape(15.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp)) {
        Box(modifier = Modifier.fillMaxWidth().background(Color.Green).padding(10.dp), contentAlignment = Alignment.TopStart
        ){

            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.background(Color.Red)) {
                Icons.Default.Person3
                Column (Modifier.padding(horizontal = 25.dp)){
                    Text(text = name)
                    Text(text = status.toString())
                }
            }
        }
    }
}



@Composable
fun listScreen(){
    Column (modifier = Modifier.verticalScroll(rememberScrollState())){
        listItem("Bob",true)
        listItem("Bob",true)
        listItem("Bob",true)
        listItem("Bob",true)
        listItem("Bob",true)
        listItem("Bob",true)
        listItem("Bob",true)
        listItem("Bob",true)
        listItem("Bob",true)
    }
}