package org.example.project.Screens.HTMLparser

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mohamedrejeb.ksoup.html.parser.KsoupHtmlHandler
import com.mohamedrejeb.ksoup.html.parser.KsoupHtmlOptions
import com.mohamedrejeb.ksoup.html.parser.KsoupHtmlParser
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.client.statement.bodyAsText
import kotlinx.coroutines.launch


@Composable
fun HTMLparser(){
    val scope = rememberCoroutineScope()

    var url by remember { mutableStateOf("https://en.m.wikipedia.org/wiki/Main_Page") }
    var page by remember { mutableStateOf<ParsedPage?>(null) }
    var isLoading by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf<String?>(null) }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {

        // --- Строка ввода URL + кнопка ---
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedTextField(
                value = url,
                onValueChange = { url = it },
                label = { Text("URL") },
                modifier = Modifier.weight(1f),
                singleLine = true
            )
            Button(
                onClick = {
                    scope.launch {
                        isLoading = true
                        error = null
                        page = null
                        try {
                            page = loadAndParsePage(url)
                        } catch (e: Exception) {
                            error = e.message ?: "Неизвестная ошибка"
                        } finally {
                            isLoading = false
                        }
                    }
                },
                enabled = !isLoading
            ) {
                Text(if (isLoading) "..." else "Загрузить")
            }
        }

        Spacer(Modifier.height(12.dp))

        // --- Состояния ---
        when {
            isLoading -> {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        CircularProgressIndicator()
                        Spacer(Modifier.height(8.dp))
                        Text("Загружаем и парсим...")
                    }
                }
            }

            error != null -> {
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Ошибка: $error",
                        modifier = Modifier.padding(16.dp),
                        color = MaterialTheme.colorScheme.onErrorContainer
                    )
                }
            }

            page != null -> {
                // Заголовок страницы
                Text(
                    text = page!!.title,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "${page!!.elements.size} элементов",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.outline
                )

                Spacer(Modifier.height(8.dp))
                HorizontalDivider()
                Spacer(Modifier.height(8.dp))

                // Список элементов
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(page!!.elements) { element ->
                        PageElementItem(element)
                    }
                }
            }

            else -> {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(
                        "Введите URL и нажмите «Загрузить»",
                        color = MaterialTheme.colorScheme.outline
                    )
                }
            }
        }
    }
}

fun firstExample(){
    // --- 1. Исходный HTML ---
    val html = """
        <html>
        <head><title>Мой сайт</title></head>
        <body>
            <h1 id="main-title">Привет, KMP!</h1>
            <p class="intro">Это первый параграф.</p>
            <p class="intro">Это второй параграф.</p>
            <a href="https://example.com" class="link">Ссылка 1</a>
            <a href="https://kotlin.org" class="link">Kotlin</a>
            <ul>
                <li>Элемент 1</li>
                <li>Элемент 2</li>
                <li>Элемент 3</li>
            </ul>
        </body>
        </html>
    """.trimIndent()

    // --- 2. Результаты ---
    val title = StringBuilder()
    val h1Text = StringBuilder()
    val paragraphs = mutableListOf<String>()
    val links = mutableListOf<Pair<String, String>>() // (href, text)
    val listItems = mutableListOf<String>()

    // Вспомогательный стек для отслеживания текущего контекста
    val tagStack = ArrayDeque<String>()
    var currentText = StringBuilder()

    // --- 3. Парсер ---
    val parser = KsoupHtmlParser(
        options = KsoupHtmlOptions(
            xmlMode = false,
            decodeEntities = true,
        ),
        handler = object : KsoupHtmlHandler {

            override fun onOpenTag(
                name: String,
                attributes: Map<String, String>,
                isImplied: Boolean
            ) {
                tagStack.addLast(name)
                currentText = StringBuilder()

                // Сохраняем href при открытии <a>
                if (name == "a") {
                    val href = attributes["href"] ?: ""
                    tagStack.addLast("__href__$href") // временно храним href
                }
            }

            override fun onText(text: String) {
                currentText.append(text)
            }

            override fun onCloseTag(name: String, isImplied: Boolean) {
                val text = currentText.toString().trim()

                when (name) {
                    "title" -> title.append(text)
                    "h1"    -> h1Text.append(text)
                    "p"     -> if (text.isNotEmpty()) paragraphs.add(text)
                    "li"    -> if (text.isNotEmpty()) listItems.add(text)
                    "a"     -> {
                        // Извлекаем сохранённый href
                        val hrefEntry = tagStack.lastOrNull { it.startsWith("__href__") }
                        val href = hrefEntry?.removePrefix("__href__") ?: ""
                        if (text.isNotEmpty()) links.add(href to text)
                        tagStack.removeAll { it.startsWith("__href__") }
                    }
                }

                // Убираем тег из стека
                if (tagStack.lastOrNull() == name) tagStack.removeLast()
                currentText = StringBuilder()
            }
        }
    )

    parser.write(html)
    parser.end()

    // --- 4. Вывод результатов ---
    println("=== Результаты парсинга ===")
    println()

    println("📄 Title: $title")
    println("📌 H1: $h1Text")
    println()

    println("📝 Параграфы (${paragraphs.size}):")
    paragraphs.forEachIndexed { i, p -> println("  $i. $p") }
    println()

    println("🔗 Ссылки (${links.size}):")
    links.forEachIndexed { i, (href, text) ->
        println("  $i. '$text' → $href")
    }
    println()

    println("📋 Список (${listItems.size}):")
    listItems.forEachIndexed { i, item -> println("  $i. $item") }
}

data class PageElement(
    val type: ElementType,
    val text: String,
    val href: String? = null,
    val src: String? = null,
    val level: Int = 0
)

enum class ElementType {
    HEADING, PARAGRAPH, LINK, IMAGE, LIST_ITEM, CODE, BLOCKQUOTE, OTHER
}

data class ParsedPage(
    val title: String,
    val elements: List<PageElement>
)


// Загрузить HTML по URL
// ========== Отрисовка одного элемента ==========

@Composable
fun PageElementItem(element: PageElement) {
    SelectionContainer {
        when (element.type) {

            ElementType.HEADING -> {
                val fontSize = when (element.level) {
                    1 -> 24.sp
                    2 -> 20.sp
                    3 -> 18.sp
                    else -> 16.sp
                }
                Text(
                    text = element.text,
                    fontSize = fontSize,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = if (element.level <= 2) 12.dp else 6.dp)
                )
            }

            ElementType.PARAGRAPH -> {
                Text(
                    text = element.text,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            ElementType.LINK -> {
                Text(
                    text = "🔗 ${element.text}",
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            ElementType.LIST_ITEM -> {
                Row(modifier = Modifier.fillMaxWidth()) {
                    Text("• ", fontWeight = FontWeight.Bold)
                    Text(element.text, style = MaterialTheme.typography.bodyMedium)
                }
            }

            ElementType.IMAGE -> {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text("🖼")
                    Text(
                        text = element.text.ifEmpty { element.src ?: "" },
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.outline,
                        fontStyle = FontStyle.Italic
                    )
                }
            }

            ElementType.CODE -> {
                Surface(
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    shape = MaterialTheme.shapes.small,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = element.text,
                        fontFamily = FontFamily.Monospace,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(8.dp)
                    )
                }
            }

            ElementType.BLOCKQUOTE -> {
                Surface(
                    color = MaterialTheme.colorScheme.secondaryContainer,
                    shape = MaterialTheme.shapes.small,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = element.text,
                        fontStyle = FontStyle.Italic,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                    )
                }
            }

            ElementType.OTHER -> {}
        }
    }
}

// ========== Сетевой слой (без изменений) ==========

suspend fun fetchHtml(url: String): String {
    val client = HttpClient()
    return try {
        client.get(url) {
            headers {
                append("User-Agent", "Mozilla/5.0 (compatible; KMP-Parser/1.0)")
                append("Accept", "text/html,application/xhtml+xml")
                append("Accept-Language", "ru,en;q=0.9")
            }
        }.bodyAsText()
    } finally {
        client.close()
    }
}

fun parseHtml(html: String): ParsedPage {
    val elements = mutableListOf<PageElement>()
    var pageTitle = ""
    val tagStack = ArrayDeque<Pair<String, Map<String, String>>>()
    val textBuffer = StringBuilder()
    val headingTags = setOf("h1", "h2", "h3", "h4", "h5", "h6")
    val skipTags = setOf("script", "style", "nav", "footer", "head", "noscript", "iframe", "svg", "button")
    var skipDepth = 0

    val parser = KsoupHtmlParser(
        options = KsoupHtmlOptions(decodeEntities = true),
        handler = object : KsoupHtmlHandler {
            override fun onOpenTag(name: String, attributes: Map<String, String>, isImplied: Boolean) {
                if (skipDepth > 0 || name in skipTags) { skipDepth++; return }
                if (name == "img") {
                    val src = attributes["src"] ?: return
                    val alt = attributes["alt"] ?: ""
                    if (src.isNotBlank()) elements.add(PageElement(ElementType.IMAGE, alt, src = src))
                    return
                }
                tagStack.addLast(name to attributes)
                textBuffer.clear()
            }

            override fun onText(text: String) {
                if (skipDepth > 0) return
                val cleaned = text.replace("\n", " ").replace(Regex("\\s+"), " ")
                if (cleaned.isNotBlank()) textBuffer.append(cleaned)
            }

            override fun onCloseTag(name: String, isImplied: Boolean) {
                if (skipDepth > 0) { skipDepth--; return }
                val text = textBuffer.toString().trim()
                val attrs = tagStack.lastOrNull { it.first == name }?.second ?: emptyMap()
                when {
                    name == "title"       -> pageTitle = text
                    name in headingTags   -> if (text.isNotEmpty()) elements.add(PageElement(ElementType.HEADING, text, level = name[1].digitToInt()))
                    name == "p"           -> if (text.isNotEmpty()) elements.add(PageElement(ElementType.PARAGRAPH, text))
                    name == "a"           -> { val href = attrs["href"]; if (text.isNotEmpty() && href != null) elements.add(PageElement(ElementType.LINK, text, href = href)) }
                    name == "li"          -> if (text.isNotEmpty()) elements.add(PageElement(ElementType.LIST_ITEM, text))
                    name == "code" || name == "pre" -> if (text.isNotEmpty()) elements.add(PageElement(ElementType.CODE, text))
                    name == "blockquote"  -> if (text.isNotEmpty()) elements.add(PageElement(ElementType.BLOCKQUOTE, text))
                }
                val idx = tagStack.indexOfLast { it.first == name }
                if (idx >= 0) tagStack.removeAt(idx)
                textBuffer.clear()
            }
        }
    )
    parser.write(html)
    parser.end()
    return ParsedPage(title = pageTitle, elements = elements)
}

suspend fun loadAndParsePage(url: String): ParsedPage {
    val html = fetchHtml(url)
    return parseHtml(html)
}