package org.example.project.UI

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.automirrored.filled.Message
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Message
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Subscriptions
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CustomSideDrawer(
    drawerState: DrawerState,
    drawerWidth: Dp = 260.dp,
    peekWidth: Dp = 36.dp,
    drawerContent: @Composable ColumnScope.() -> Unit,
    content: @Composable () -> Unit
) {
    val offsetX by animateDpAsState(
        targetValue = if (drawerState.isOpen) 0.dp else -(drawerWidth - peekWidth),
        label = "drawer_offset"
    )

    Box(Modifier.fillMaxSize()) {

        // Основной контент
        Box(
            Modifier
                .fillMaxSize()
                .offset(x = if (drawerState.isOpen) drawerWidth else 0.dp)
        ) {
            content()
        }

        // Drawer
        Row(
            Modifier
                .fillMaxHeight()
                .width(drawerWidth)
                .offset(x = offsetX)
                .background(Color(0xFF2C3E91)) // синий фон как на скрине
        ) {

            // Торчащая кнопка
            DrawerToggleHandle(
                isOpen = drawerState.isOpen,
                onClick = { drawerState.toggle() }
            )

            // Контент Drawer
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(1f)
                    .padding(top = 32.dp, start = 16.dp, end = 16.dp, bottom = 16.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp),
                content = drawerContent
            )
        }
    }
}

@Stable
class DrawerState(
    initialOpen: Boolean = false
) {
    var isOpen by mutableStateOf(initialOpen)
        private set

    fun open() { isOpen = true }
    fun close() { isOpen = false }
    fun toggle() { isOpen = !isOpen }
}

@Composable
fun rememberDrawerState(initialOpen: Boolean = false) = remember { DrawerState(initialOpen) }


@Composable
fun DrawerToggleHandle(
    isOpen: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxHeight()
            .width(36.dp)
            .clickable { onClick() }
            .background(
                color = Color(0xFF1F2A70), // чуть темнее, чем основной фон
                shape = RoundedCornerShape(topEnd = 12.dp, bottomEnd = 12.dp)
            ),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = if (isOpen) Icons.Default.ArrowBack else Icons.Default.ArrowForward,
            contentDescription = "Toggle drawer",
            tint = Color.White
        )
    }
}

@Composable
fun DrawerHandle(
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxHeight()
            .width(36.dp)
            .clickable { onClick() }
            .background(
                MaterialTheme.colorScheme.primary,
                RoundedCornerShape(topEnd = 16.dp, bottomEnd = 16.dp)
            ),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Default.Menu,
            contentDescription = "Open drawer",
            tint = MaterialTheme.colorScheme.onPrimary
        )
    }
}



@Composable
fun DrawerContent(drawerState: DrawerState) {
    Row(Modifier.fillMaxHeight()) {

        // 👈 УХО
        DrawerHandle {
            drawerState.toggle()
        }

        // 📦 Сам Drawer
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text("Меню", style = MaterialTheme.typography.titleLarge)

            Text("Главная")
            Text("Профиль")
            Text("Настройки")
            Text("Выход")
        }
    }
}


@Composable
fun DrawerContentSample() {
    Column(verticalArrangement = Arrangement.spacedBy(20.dp)) {
        // Верхний блок с профилем
        Row(verticalAlignment = Alignment.CenterVertically) {
            // Здесь можно вставить Image(profilePicture)
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = "Profile",
                tint = Color.White,
                modifier = Modifier.size(40.dp)
            )
            Spacer(Modifier.width(12.dp))
            Column {
                Text("Rlexandra", color = Color.White, fontSize = 18.sp)
                Text("rlexandra@gmail.com", color = Color.White.copy(alpha = 0.7f), fontSize = 12.sp)
            }
        }

        Spacer(Modifier.height(24.dp))

        // Список пунктов с иконками
        DrawerItem(Icons.Default.Home, "Home")
        DrawerItem(Icons.Default.Person, "My Profile")
        DrawerItem(Icons.Default.Favorite, "My Vacancy")
        DrawerItem(Icons.Default.Message, "Message")
        DrawerItem(Icons.Default.Subscriptions, "Subscription")
        DrawerItem(Icons.Default.Notifications, "Notification")
        DrawerItem(Icons.Default.Settings, "Setting")

        Spacer(Modifier.weight(1f)) // Push Log Out вниз

        DrawerItem(Icons.Default.ExitToApp, "Log Out")
    }
}

@Composable
fun DrawerItem(icon: ImageVector, text: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { /* handle click */ }
    ) {
        Icon(icon, contentDescription = text, tint = Color.White)
        Spacer(Modifier.width(12.dp))
        Text(text, color = Color.White, fontSize = 16.sp)
    }
}

