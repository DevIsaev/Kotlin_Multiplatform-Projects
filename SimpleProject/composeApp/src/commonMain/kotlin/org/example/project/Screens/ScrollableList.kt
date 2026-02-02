package org.example.project.Screens

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Person3
import androidx.compose.material.icons.filled.RemoveCircle
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.painterResource


data class User(var id: Int, var name: String, var status: Boolean, var role: String = "User", var lastSeen: String = "2 hours ago")

// wвета для градиентов
val gradientBlue = Brush.horizontalGradient(
    colors = listOf(Color(0xFF6A11CB), Color(0xFF2575FC))
)

val gradientGreen = Brush.horizontalGradient(
    colors = listOf(Color(0xFF00B09B), Color(0xFF96C93D))
)

val gradientRed = Brush.horizontalGradient(
    colors = listOf(Color(0xFFFF416C), Color(0xFFFF4B2B))
)

val gradientPurple = Brush.horizontalGradient(
    colors = listOf(Color(0xFF8A2387), Color(0xFFF27121))
)

//функция элемента из списка
@Composable
fun listItem( user: User, onClick: () -> Unit = {}, onLongPress: () -> Unit = {}){
    val backgroundColor by animateColorAsState(
        targetValue = if (user.status) Color(0xFFE8F5E9) else Color(0xFFF5F5F5),
        animationSpec = tween(durationMillis = 300)
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .shadow(
                elevation = 4.dp,
                shape = RoundedCornerShape(16.dp),
                clip = true
            )
            .pointerInput(Unit) {
                detectDragGesturesAfterLongPress { change, dragAmount ->
                    // Обработка перетаскивания
                }
            }
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = backgroundColor,
            contentColor = MaterialTheme.colorScheme.onSurface
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp,
            pressedElevation = 8.dp
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Аватар пользователя
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape)
                    .background(
                        brush = if (user.status) gradientGreen else gradientRed,
                        alpha = 0.9f
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "User Avatar",
                    modifier = Modifier.size(30.dp),
                    tint = Color.White
                )
            }

            // Информация о пользователе
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = user.name,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF333333)
                    )

                    // Бейдж роли
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(
                                brush = gradientPurple,
                                alpha = 0.8f
                            )
                            .padding(horizontal = 8.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = user.role,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(
                        imageVector = if (user.status)
                            Icons.Default.CheckCircle
                        else
                            Icons.Default.RemoveCircle,
                        contentDescription = "Status",
                        modifier = Modifier.size(16.dp),
                        tint = if (user.status) Color(0xFF4CAF50) else Color(0xFF757575)
                    )

                    Text(
                        text = if (user.status) "Online" else "Offline",
                        fontSize = 14.sp,
                        color = if (user.status) Color(0xFF4CAF50) else Color(0xFF757575)
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        text = "•",
                        fontSize = 14.sp,
                        color = Color(0xFFBDBDBD)
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        text = user.lastSeen,
                        fontSize = 14.sp,
                        color = Color(0xFF757575)
                    )
                }
            }
        }
    }
}

// Заголовок секции
@Composable
fun SectionHeader(title: String, count: Int) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = title,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF333333)
            )

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(
                        brush = gradientBlue,
                        alpha = 0.1f
                    )
                    .padding(horizontal = 12.dp, vertical = 4.dp)
            ) {
                Text(
                    text = "$count",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF2575FC)
                )
            }
        }

        Spacer(modifier = Modifier.height(4.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            Color(0xFF2575FC).copy(alpha = 0.3f),
                            Color(0xFFFFFFFF),
                            Color(0xFF2575FC).copy(alpha = 0.3f)
                        )
                    )
                )
        )
    }
}

@Composable
fun listScreen(){
    val users = listOf(
        User(1, "Alex Johnson", true, "Admin", "Active now"),
        User(2, "Sam Wilson", true, "Moderator", "5 min ago"),
        User(3, "Taylor Swift", false, "VIP User", "1 day ago"),
        User(4, "John Doe", true, "User", "30 min ago"),
        User(5, "Jane Smith", false, "User", "2 days ago"),
        User(6, "Robert Brown", true, "Moderator", "Active now"),
        User(7, "Emily Davis", true, "User", "10 min ago"),
        User(8, "Michael Lee", false, "User", "3 days ago"),
        User(9, "Sarah Connor", true, "VIP User", "1 hour ago"),
        User(10, "David Miller", false, "User", "5 days ago")
    )
    val onlineUsers = users.filter { it.status }
    val offlineUsers = users.filter { !it.status }

    Box(
        modifier = Modifier.fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFFF8F9FF),
                        Color(0xFFF0F2FF)
                    )
                )
            )
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            // Секция онлайн пользователей
            item {
                SectionHeader(
                    title = "Online Users",
                    count = onlineUsers.size
                )
            }

            items(onlineUsers, key = { it.id }) { user ->
                listItem(
                    user = user,
                    onClick = {
                        // Обработка клика
                        println("Clicked on ${user.name}")
                    },
                    onLongPress = {
                        // Обработка долгого нажатия
                        println("Long pressed ${user.name}")
                    }
                )
            }

            // Секция офлайн пользователей
            item {
                SectionHeader(
                    title = "Offline Users",
                    count = offlineUsers.size
                )
            }

            items(offlineUsers, key = { it.id }) { user ->
                listItem(
                    user = user,
                    onClick = {
                        // Обработка клика
                        println("Clicked on ${user.name}")
                    },
                    onLongPress = {
                        // Обработка долгого нажатия
                        println("Long pressed ${user.name}")
                    }
                )
            }
        }
    }
}

// Пустой список с красивым сообщением
@Composable
fun EmptyListState() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            Color(0xFF6A11CB).copy(alpha = 0.1f),
                            Color(0xFF2575FC).copy(alpha = 0.05f)
                        )
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = "No users",
                modifier = Modifier.size(48.dp),
                tint = Color(0xFF6A11CB).copy(alpha = 0.5f)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "No users found",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF333333)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Add new users or check your connection",
            fontSize = 16.sp,
            color = Color(0xFF757575),
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )
    }
}