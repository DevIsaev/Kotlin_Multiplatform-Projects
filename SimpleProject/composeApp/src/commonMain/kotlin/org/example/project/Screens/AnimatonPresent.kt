package org.example.project.Screens
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColor
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.EaseInOutCubic
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateIntOffsetAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.snap
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.layout
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextMotion
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import kotlin.math.roundToInt


enum class UiState{Loading,Loaded,Error}
enum class BoxState {Collapsed,  Expanded}

@Composable
fun AnimationsScreen(){
    LazyColumn(modifier = Modifier.fillMaxSize().padding(15.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(16.dp)
    ) {
        item {
            Text(text = "Краткое руководство по анимации в Compose", style = MaterialTheme.typography.headlineMedium)
        }
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = "Compose имеет множество встроенных механизмов анимации, и выбор одного из них может быть сложным. Ниже представлен список распространённых вариантов использования анимации.",
                        style = MaterialTheme.typography.bodyMedium)
                }
            }
        }
        item { LaunchAnimation() }
        item { AnimatedVisibility() }
//        item {AlphaAnimation()}
        item { AnimatedBackgroundColor() }
        item { AnimatedSize() }
        item { AnimatedPosition() }
        item { LayoutModifierAnimation() }
        item { AnimatedPadding() }
//        item { AnimatedElevation() }
        item { AnimatedText() }
        item { AnimatedTextColor() }
        item { AnimatedContent() }
        item { RepeatAnimation() }
        item { SequentialAnimation() }
//        item { ParallelAnimation() }
        item { TransitionApi() }
//        item { AnimationSpec() }
    }
}

// Обертка в карточке
@Composable
fun CardExample(title: String, description: String, content: @Composable () -> Unit) {
    Card(modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)) {
        Column(modifier = Modifier.fillMaxWidth().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text(text = title, style = MaterialTheme.typography.titleLarge, color = MaterialTheme.colorScheme.primary)

            Text(text = description, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)

            Divider()

            content()
        }
    }
}

// Анимированное появление/исчезновение
@Composable
fun AnimatedVisibility(){
    var visible by remember { mutableStateOf(true) }

    CardExample("Анимированное появление/исчезновение","AnimatedVisibility для плавного появления/исчезновения"){
        Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(8.dp)){
            Button(onClick = {visible=!visible }){Text(if (visible) "скрыть" else "показать")}

            AnimatedVisibility(visible, enter = fadeIn()+ expandVertically(), exit= fadeOut()+ shrinkVertically()){
                Box(modifier = Modifier.size(100.dp).background(Color.Green, RoundedCornerShape(8.dp))){
                    Text("Появление/Исчезновение", modifier = Modifier.align(Alignment.Center), color = Color.White)
                }
            }
        }
    }
}

// Анимация альфа-канала
@Composable
fun AlphaAnimation(){
    var visible by remember { mutableStateOf(true) }
    val animatedAlpha by animateFloatAsState(targetValue = if (visible) 1f else 0f, label = "alpha")

    CardExample("Анимация альфа-канала", "animateFloatAsState для анимации прозрачности"){
        Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(8.dp)){
            Button(onClick = {visible=!visible }){Text(if (visible) "скрыть" else "показать")}

            Box( modifier = Modifier.size(100.dp)
                .graphicsLayer { alpha = animatedAlpha }
                .clip(RoundedCornerShape(8.dp))
                .background(Color.Green)
                .align(Alignment.CenterHorizontally)){
                Text("Alpha: ${animatedAlpha.toString()}", modifier = Modifier.align(Alignment.Center), color = Color.White)
            }
        }
    }
}

// Анимированный цвет фона
@Composable
fun AnimatedBackgroundColor() {
    var animateBackgroundColor by remember { mutableStateOf(false) }

    val animatedColor by animateColorAsState(targetValue = if (animateBackgroundColor) Color.Green else Color.Blue, label = "color")

    CardExample(title = "Анимированный цвет фона", description = "animateColorAsState для плавной смены цвета") {
        Column(modifier = Modifier.fillMaxWidth().height(150.dp).background(animatedColor)
                .clickable { animateBackgroundColor = !animateBackgroundColor }
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center) {
            Text("Нажмите для смены цвета", color = Color.White, style = MaterialTheme.typography.bodyLarge)
            Text("Текущий: ${if (animateBackgroundColor) "Зеленый" else "Синий"}", color = Color.White.copy(alpha = 0.9f))
        }
    }
}

// Анимировать размер компонуемого объекта
@Composable
fun AnimatedSize() {
    var expanded by remember { mutableStateOf(false) }

    CardExample(title = "Анимировать размер компонуемого объекта", description = "Modifier.animateContentSize() для плавного изменения размера") {
        Box(modifier = Modifier.fillMaxWidth()
                .animateContentSize()
                .height(if (expanded) 200.dp else 100.dp)
                .background(Color.Blue).clickable { expanded = !expanded }
                .padding(16.dp)
        ) {
            Text(if (expanded) "Нажмите для уменьшения\n(Развернуто)" else "Нажмите для увеличения\n(Свернуто)", color = Color.White,  modifier = Modifier.align(Alignment.Center))
        }
    }
}

// Пример 5: Анимировать позицию компонуемого объекта
@Composable
fun AnimatedPosition() {
    var moved by remember { mutableStateOf(false) }
    val pxToMove = with(LocalDensity.current) { 100.dp.toPx().roundToInt() }

    val offset by animateIntOffsetAsState(targetValue = if (moved) IntOffset(pxToMove, pxToMove) else IntOffset.Zero, label = "offset")

    CardExample(title = "Анимировать позицию компонуемого объекта", description = "animateIntOffsetAsState + Modifier.offset") {
        Box(modifier = Modifier.fillMaxWidth().height(200.dp).background(Color.LightGray.copy(alpha = 0.3f)).padding(16.dp)) {
            Box(modifier = Modifier.offset { offset }.size(100.dp).background(Color.Blue, CircleShape).clickable { moved = !moved }) {
                Text("Нажмите\nдля\nдвижения", modifier = Modifier.align(Alignment.Center), color = Color.White, fontSize = 12.sp)
            }
        }
    }
}

// Анимация с Modifier.layout{}
@Composable
fun LayoutModifierAnimation() {
    var toggled by remember { mutableStateOf(false) }

    CardExample(title = "Анимация с Modifier.layout{}", description = "Влияет на расположение соседних элементов") {
        Column(modifier = Modifier.fillMaxWidth().padding(16.dp).clickable { toggled = !toggled }) {
            val offsetTarget = if (toggled) IntOffset(150, 50) else IntOffset.Zero
            val offset = animateIntOffsetAsState(targetValue = offsetTarget, label = "offset")

            Box(modifier = Modifier.size(80.dp).background(Color.Blue))

            Box(modifier = Modifier
                    .layout { measurable, constraints ->
                        val offsetValue = offset.value
                        val placeable = measurable.measure(constraints)
                        layout(
                            placeable.width + offsetValue.x,
                            placeable.height + offsetValue.y
                        ) {
                            placeable.placeRelative(offsetValue)
                        }
                    }
                    .size(80.dp)
                    .background(Color.Green)
            )

            Box(modifier = Modifier.size(80.dp).background(Color.Blue))

            Text("Нажмите в любом месте для анимации", modifier = Modifier.padding(top = 8.dp), fontSize = 12.sp)
        }
    }
}

// Анимированное заполнение
@Composable
fun AnimatedPadding() {
    var toggled by remember { mutableStateOf(false) }

    val animatedPadding by animateDpAsState(
        targetValue = if (toggled) 0.dp else 20.dp,
        label = "padding"
    )

    CardExample(title = "Анимированное заполнение компонуемого объекта", description = "animateDpAsState + Modifier.padding()") {
        Box(modifier = Modifier.fillMaxWidth().height(200.dp)
                .background(Color.LightGray.copy(alpha = 0.3f)).padding(16.dp)
        ) {
            Box(modifier = Modifier.fillMaxSize()
                    .padding(animatedPadding)
                    .background(Color(0xFF53D9A1))
                    .clickable { toggled = !toggled }
            ) {
                Text("Нажмите\nPadding: ${animatedPadding.value}dp",
                    modifier = Modifier.align(Alignment.Center), color = Color.White)
            }
        }
    }
}

// Анимированное возвышение
@Composable
fun AnimatedElevation() {
    val interactionSource = remember { MutableInteractionSource() }
    val pressed = interactionSource.collectIsPressedAsState()

    val elevation by animateDpAsState(
        targetValue = if (pressed.value) 32.dp else 8.dp,
        label = "elevation"
    )

    CardExample(title = "Анимированное возвышение составного объекта", description = "animateDpAsState + Modifier.graphicsLayer для тени") {
        Box(modifier = Modifier.fillMaxWidth().height(150.dp)
                .background(Color.LightGray.copy(alpha = 0.3f)).padding(16.dp)) {
            Box(modifier = Modifier.size(100.dp)
                    .align(Alignment.Center)
                    .graphicsLayer {
                        shadowElevation = elevation.toPx()
                    }
                    .clickable(interactionSource = interactionSource, indication = null) {}
                    .background(Color.Green, CircleShape)
            ) {
                Text("Нажмите\nи удерживайте", modifier = Modifier.align(Alignment.Center), color = Color.White, fontSize = 12.sp)
            }
        }
    }
}

// Анимация масштабирования текста
@Composable
fun AnimatedText() {
    val infiniteTransition = rememberInfiniteTransition(label = "infinite transition")
    val scale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.5f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000),
            repeatMode = RepeatMode.Reverse),
        label = "scale"
    )

    CardExample(title = "Анимация масштабирования текста", description = "TextMotion.Animated + graphicsLayer для плавной анимации") {
        Box(modifier = Modifier.fillMaxWidth().height(150.dp).background(Color.LightGray.copy(alpha = 0.3f))) {
            Text(text = "Пусть будет!",
                modifier = Modifier
                    .graphicsLayer {
                        scaleX = scale
                        scaleY = scale
                        transformOrigin = TransformOrigin.Center
                    }
                    .align(Alignment.Center),
                style = androidx.compose.material3.LocalTextStyle.current.copy(
                    textMotion = TextMotion.Animated
                ),
                fontSize = 24.sp,
                color = Color.Blue
            )
        }
    }
}

// Анимированный цвет текста
@Composable
fun AnimatedTextColor() {
    val infiniteTransition = rememberInfiniteTransition(label = "infinite transition")
    val animatedColor by infiniteTransition.animateColor(
        initialValue = Color(0xFF60DDAD),
        targetValue = Color(0xFF4285F4),
        animationSpec = infiniteRepeatable(
            animation = tween(1000),
            repeatMode = RepeatMode.Reverse
        ),
        label = "color"
    )

    CardExample(title = "Анимированный цвет текста", description = "rememberInfiniteTransition + animateColor для бесконечной анимации") {
        Box(modifier = Modifier.fillMaxWidth().height(150.dp)
                .background(Color.LightGray.copy(alpha = 0.3f))) {
            androidx.compose.foundation.text.BasicText(text = "Hello Compose!",
                modifier = Modifier.align(Alignment.Center),
                style = MaterialTheme.typography.headlineMedium.copy(color = animatedColor)
            )
        }
    }
}

// Переключение между различными типами контента
@Composable
fun AnimatedContent() {
    var state by remember { mutableStateOf(UiState.Loading) }

    CardExample(title = "Переключение между различными типами контента", description = "AnimatedContent для переходов между состояниями") {
        Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
            Button(
                onClick = {
                    state = when (state) {
                        UiState.Loading -> UiState.Loaded
                        UiState.Loaded -> UiState.Error
                        UiState.Error -> UiState.Loading
                    }
                }
            ) {
                Text("Сменить состояние")
            }

            Spacer(modifier = Modifier.height(16.dp))

            AnimatedContent(targetState = state,
                transitionSpec = {
                    fadeIn(animationSpec = tween(300)) togetherWith
                            fadeOut(animationSpec = tween(300))
                },
                label = "Animated Content"
            ) { targetState ->
                when (targetState) {
                    UiState.Loading -> {
                        Box(modifier = Modifier.size(100.dp).background(Color.Blue, CircleShape)) {
                            Text("Загрузка...", modifier = Modifier.align(Alignment.Center), color = Color.White)
                        }
                    }
                    UiState.Loaded -> {
                        Box(modifier = Modifier.size(100.dp).background(Color.Green, CircleShape)) {
                            Text("Готово!", modifier = Modifier.align(Alignment.Center), color = Color.White)
                        }
                    }
                    UiState.Error -> {
                        Box(modifier = Modifier.size(100.dp).background(Color.Red, CircleShape)) {
                            Text("Ошибка!", modifier = Modifier.align(Alignment.Center), color = Color.White)
                        }
                    }
                }
            }
        }
    }
}

// Повторяющаяся анимация
@Composable
fun RepeatAnimation() {
    val infiniteTransition = rememberInfiniteTransition(label = "infinite")
    val color by infiniteTransition.animateColor(
        initialValue = Color.Green,
        targetValue = Color.Blue,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "color"
    )

    CardExample(title = "Повторить анимацию", description = "infiniteRepeatable для бесконечной анимации") {
        Column(modifier = Modifier.fillMaxWidth().height(150.dp).background(color).padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("Бесконечная анимация цвета", color = Color.White, style = MaterialTheme.typography.headlineSmall)
            Text("Цвет меняется каждую секунду", color = Color.White.copy(alpha = 0.9f))
        }
    }
}

// Запуск анимации при появлении
@Composable
fun LaunchAnimation() {
    val alphaAnimation = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        alphaAnimation.animateTo(1f, animationSpec = tween(1000))
    }

    CardExample(title = "Запустить анимацию при запуске компонуемого объекта", description = "LaunchedEffect + Animatable для анимации при создании") {
        Box(modifier = Modifier.fillMaxWidth().height(100.dp)
                .graphicsLayer { alpha = alphaAnimation.value }
                .background(Color.Green, RoundedCornerShape(8.dp))
        ) {
            Text("Появился с анимацией!", modifier = Modifier.align(Alignment.Center), color = Color.White)
        }
    }
}

// Последовательные анимации
@Composable
fun SequentialAnimation() {
    val alphaAnimation = remember { Animatable(0f) }
    val yAnimation = remember { Animatable(0f) }
    var isAnimating by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    CardExample(title = "Создание последовательных анимаций", description = "Последовательный вызов animateTo в корутине") {
        Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
            Button(
                onClick = {
                    isAnimating = true
                    coroutineScope.launch {
                        alphaAnimation.animateTo(1f, animationSpec = tween(500))
                        yAnimation.animateTo(100f, animationSpec = tween(500))
                        yAnimation.animateTo(0f, animationSpec = tween(500))
                        alphaAnimation.animateTo(0.5f, animationSpec = tween(500))
                        isAnimating = false
                    }
                },
                enabled = !isAnimating
            ) {
                Text(if (isAnimating) "Анимация..." else "Запустить последовательно")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Box(
                modifier = Modifier
                    .size(100.dp)
                    .graphicsLayer {
                        alpha = alphaAnimation.value
                        translationY = yAnimation.value
                    }
                    .background(Color.Blue, CircleShape)
            ) {
                Text("Последовательно", modifier = Modifier.align(Alignment.Center), color = Color.White, fontSize = 10.sp)
            }
        }
    }
}

// Параллельные анимации
@Composable
fun ParallelAnimation() {
    val alphaAnimation = remember { Animatable(0f) }
    val yAnimation = remember { Animatable(0f) }
    var isAnimating by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    CardExample(title = "Создавать параллельные анимации", description = "Несколько launch блоков для параллельных анимаций") {
        Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
            Button(
                onClick = {
                    isAnimating = true
                    coroutineScope.launch {
                        launch {
                            alphaAnimation.animateTo(1f, animationSpec = tween(1000))
                        }
                        launch {
                            yAnimation.animateTo(100f, animationSpec = tween(1000))
                        }
                        // Ждем завершения обеих анимаций
                        kotlinx.coroutines.delay(1100)
                        isAnimating = false
                    }
                },
                enabled = !isAnimating
            ) {
                Text(if (isAnimating) "Анимация..." else "Запустить параллельно")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Box(
                modifier = Modifier
                    .size(100.dp)
                    .graphicsLayer {
                        alpha = alphaAnimation.value
                        translationY = yAnimation.value
                    }
                    .background(Color.Green, CircleShape)
            ) {
                Text(
                    "Параллельно",
                    modifier = Modifier.align(Alignment.Center),
                    color = Color.White,
                    fontSize = 10.sp
                )
            }
        }
    }
}

// Transition API
@Composable
fun TransitionApi() {
    var currentState by remember { mutableStateOf(BoxState.Collapsed) }
    val transition = updateTransition(currentState, label = "transition")

    val size by transition.animateDp(label = "size") { state ->
        when (state) {
            BoxState.Collapsed -> 100.dp
            BoxState.Expanded -> 200.dp
        }
    }

    val borderRadius by transition.animateDp(label = "borderRadius") { state ->
        when (state) {
            BoxState.Collapsed -> 8.dp
            BoxState.Expanded -> 100.dp
        }
    }

    CardExample(title = "Transition API", description = "updateTransition для управления несколькими анимациями одним состоянием") {
        Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
            Button(
                onClick = {
                    currentState = if (currentState == BoxState.Collapsed)
                        BoxState.Expanded
                    else
                        BoxState.Collapsed
                }
            ) {
                Text(if (currentState == BoxState.Collapsed) "Расширить" else "Свернуть")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Box(modifier = Modifier.size(size).background(color = Color.Blue, shape = RoundedCornerShape(borderRadius))
                    .clickable {
                        currentState = if (currentState == BoxState.Collapsed)
                            BoxState.Expanded
                        else
                            BoxState.Collapsed
                    }
            ) {
                Text("Нажмите\nРазмер: ${size.value.toInt()}dp",
                    modifier = Modifier.align(Alignment.Center),
                    color = Color.White,
                    fontSize = 12.sp
                )
            }
        }
    }
}

// Разные animationSpec
@Composable
fun AnimationSpec() {
    var selectedSpec by remember { mutableStateOf(0) }
    val specs = listOf("spring", "tween", "keyframes", "snap")

    val animatedValue by animateFloatAsState(
        targetValue = if (selectedSpec == 0) 0f else 300f,
        animationSpec = when (specs[selectedSpec]) {
            "spring" -> spring(dampingRatio = 0.5f, stiffness = 200f)
            "tween" -> tween(durationMillis = 1000, easing = EaseInOutCubic)
            "keyframes" -> keyframes {
                durationMillis = 1000
                0f at 0 with LinearEasing
                400f at 500 with FastOutLinearInEasing
                300f at 1000
            }
            else -> snap()
        },
        label = "animationSpec"
    )

    CardExample(title = "Различные animationSpec", description = "Разные спецификации анимации: spring, tween, keyframes, snap") {
        Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
            // Переключатели для выбора типа анимации
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                specs.forEachIndexed { index, spec ->
                    FilterChip(selected = selectedSpec == index,
                        onClick = {
                            selectedSpec = index
                            // Сброс для повторной анимации
                            if (selectedSpec == 0) selectedSpec = 1 else selectedSpec = 0
                        },
                        label = { Text(spec) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Box(modifier = Modifier.fillMaxWidth().height(150.dp)
                    .background(Color.LightGray.copy(alpha = 0.3f))) {
                Box(modifier = Modifier
                        .offset(x = animatedValue.dp)
                        .size(50.dp)
                        .background(Color.Green, CircleShape)
                ) {
                    Text(specs[selectedSpec], modifier = Modifier.align(Alignment.Center), color = Color.White, fontSize = 10.sp)
                }
            }

            Text("Текущий animationSpec: ${specs[selectedSpec]}", modifier = Modifier.padding(top = 8.dp))
        }
    }
}