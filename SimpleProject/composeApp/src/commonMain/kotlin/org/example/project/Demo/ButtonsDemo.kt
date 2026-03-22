package org.example.project.Demo

import SimpleProject.icons.Icons
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.fuyuz.svgicon.SvgIcon
import org.example.project.resources.fonts.Fonts.Zapussans

@Composable
fun buttonDemo(){
//    SvgIcon(
//        svg = SimpleProject.icons.Icons.Person,
//        contentDescription = "SVG ICON",
//        tint = Color.Gray,
//        modifier = Modifier.size(96.dp),
//    )
//    Text("TEXT", fontFamily = Zapussans())

    Column(
        modifier = Modifier.padding(20.dp).verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Большие кнопки
        Text("LARGE размер")
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            ButtonStyles.DarkBlueButton(
                text = "кнопка",
                onClick = { },
                size = ButtonSize.LARGE
            )

            ButtonStyles.OrangeButton(
                text = "кнопка",
                onClick = { },
                size = ButtonSize.LARGE
            )
        }

        // Средние кнопки
        Text("MEDIUM размер")
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            ButtonStyles.BlueButton(
                text = "кнопка",
                onClick = { },
                size = ButtonSize.MEDIUM
            )

            ButtonStyles.BrownButton(
                text = "кнопка",
                onClick = { },
                size = ButtonSize.MEDIUM
            )
        }

        // Обычные кнопки
        Text("NORMAL размер (по умолчанию)")
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            ButtonStyles.OrangeButton(
                text = "кнопка",
                onClick = { }
            )

            ButtonStyles.BrownButton(
                text = "кнопка",
                onClick = { }
            )
        }

        // Маленькие кнопки
        Text("SMALL размер")
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            ButtonStyles.BlueButton(
                text = "кнопка",
                onClick = { },
                size = ButtonSize.SMALL
            )

            ButtonStyles.OrangeButton(
                text = "кнопка",
                onClick = { },
                size = ButtonSize.SMALL
            )
        }

        // Очень маленькие кнопки
        Text("TINY размер")
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            ButtonStyles.BlueButton(
                text = "кнопка",
                onClick = { },
                size = ButtonSize.TINY
            )

            ButtonStyles.BrownButton(
                text = "кнопка",
                onClick = { },
                size = ButtonSize.TINY
            )
        }

        // Кнопки с иконками разных размеров
        Text("С иконками")
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            ButtonStyles.DarkBlueButton(
                text = "",
                onClick = { },
                icon = Icons.Back,
                iconOnly = true,
                size = ButtonSize.LARGE
            )

            ButtonStyles.BlueButton(
                text = "",
                onClick = { },
                icon = Icons.Back,
                iconOnly = true,
                size = ButtonSize.MEDIUM
            )

            ButtonStyles.OrangeButton(
                text = "",
                onClick = { },
                icon = Icons.Back,
                iconOnly = true,
                size = ButtonSize.NORMAL
            )

            ButtonStyles.BrownButton(
                text = "",
                onClick = { },
                icon = Icons.Back,
                iconOnly = true,
                size = ButtonSize.SMALL
            )
        }

        // Можно также переопределить отдельные параметры
//        Text("Кастомные параметры")
//        ButtonStyles.DarkBlueButton(
//            text = "Своя высота",
//            onClick = { },
//            size = ButtonSize.NORMAL,
//            height = 60.dp, // Переопределяем высоту
//            width = 200.dp,  // Задаем ширину
//            fontSize = 18.sp // Переопределяем размер шрифта
//        )
    }
}