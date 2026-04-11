package org.example.project.Demo

import SimpleProject.icons.Icons
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import org.example.project.resources.colors.Success


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
        //кнопки
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

        //текстовые поля

        var login by remember { mutableStateOf("") }
        var email by remember { mutableStateOf("") }
        var phone by remember { mutableStateOf("") }
        var password by remember { mutableStateOf("") }

        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp).background(Success),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(){
                Column(Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    // Поле логина
                    DemoTextField(
                        placeholderText = "Логин*",
                        cornerRadius = 28.dp,
                        height = 50.dp,
                        width = 250.dp
                    ).Create(
                        value = login,
                        onValueChange = { login = it }
                    )

                    // Поле почты
                    DemoTextField(
                        placeholderText = "Почта*",
                        cornerRadius = 28.dp,
                        height = 50.dp,
                        width = 250.dp,
                        keyboardType = KeyboardType.Email
                    ).Create(
                        value = email,
                        onValueChange = { email = it }
                    )

                    // Поле телефона
                    DemoTextField(
                        placeholderText = "Номер телефона*",
                        cornerRadius = 28.dp,
                        height = 50.dp,
                        width = 250.dp,
                        keyboardType = KeyboardType.Phone
                    ).Create(
                        value = phone,
                        onValueChange = { phone = it }
                    )

                    // Поле пароля
                    DemoTextField(
                        placeholderText = "Пароль*",
                        cornerRadius = 28.dp,
                        height = 50.dp,
                        width = 250.dp,
                        isPassword = true
                    ).Create(
                        value = password,
                        onValueChange = { password = it }
                    )
                    // Использование с Builder-паттерном
                    var password1 by remember { mutableStateOf("") }

                    CustomTextFieldBuilder()
                        .placeholder("Пароль*")
                        .cornerRadius(28.dp)
                        .height(50.dp)
                        .width(250.dp)
                        .asPassword()
                        .build()
                        .Create(
                            value = password1,
                            onValueChange = { password1 = it }
                        )
                }

                //стикер

            }
        }
    }
}
