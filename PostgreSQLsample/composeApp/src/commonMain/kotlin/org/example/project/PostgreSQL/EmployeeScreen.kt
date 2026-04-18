package org.example.project.PostgreSQL

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch

// ──────────────────────────────────────────────────────────────────
//  ViewModel-like state holder
// ──────────────────────────────────────────────────────────────────

class EmployeeViewModel(private val repo: HrRepository) {

    suspend fun loadEmployees(): Result<List<Employee>> = runCatching {
        repo.getEmployees()
    }

    suspend fun loadDepartments(): Result<List<Department>> = runCatching {
        repo.getDepartments()
    }

    suspend fun loadByDepartment(deptId: Int): Result<List<Employee>> = runCatching {
        repo.getEmployeesByDepartment(deptId)
    }
}

// ──────────────────────────────────────────────────────────────────
//  Главный экран
// ──────────────────────────────────────────────────────────────────

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HrApp() {
    val config = DatabaseConfig(
        host     = "localhost",
        port     = 5432,
        database = "mydb",
        user     = "admin",
        password = "admin123"
    )

    val repo = remember { createHrRepository(config) }
    val vm   = remember { EmployeeViewModel(repo) }

    DisposableEffect(Unit) {
        onDispose { repo.close() }
    }

    MaterialTheme {
        EmployeeScreen(vm)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmployeeScreen(vm: EmployeeViewModel) {
    val scope = rememberCoroutineScope()

    var employees    by remember { mutableStateOf<List<Employee>>(emptyList()) }
    var departments  by remember { mutableStateOf<List<Department>>(emptyList()) }
    var isLoading    by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var selectedTab  by remember { mutableIntStateOf(0) }

    // Загрузка данных при старте
    LaunchedEffect(Unit) {
        isLoading = true
        errorMessage = null

        vm.loadEmployees()
            .onSuccess { employees = it }
            .onFailure { errorMessage = it.message }

        vm.loadDepartments()
            .onSuccess { departments = it }
            .onFailure { /* уже показали ошибку */ }

        isLoading = false
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("HR Database", fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // Вкладки
            TabRow(selectedTabIndex = selectedTab) {
                Tab(
                    selected = selectedTab == 0,
                    onClick  = { selectedTab = 0 },
                    text     = { Text("Сотрудники (${employees.size})") }
                )
                Tab(
                    selected = selectedTab == 1,
                    onClick  = { selectedTab = 1 },
                    text     = { Text("Отделы (${departments.size})") }
                )
            }

            when {
                isLoading -> {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            CircularProgressIndicator()
                            Spacer(Modifier.height(12.dp))
                            Text("Подключение к PostgreSQL...")
                        }
                    }
                }

                errorMessage != null -> {
                    ErrorView(errorMessage!!) {
                        scope.launch {
                            isLoading = true
                            errorMessage = null
                            vm.loadEmployees()
                                .onSuccess { employees = it }
                                .onFailure { errorMessage = it.message }
                            isLoading = false
                        }
                    }
                }

                selectedTab == 0 -> EmployeeList(employees)
                selectedTab == 1 -> DepartmentList(departments)
            }
        }
    }
}

// ──────────────────────────────────────────────────────────────────
//  Список сотрудников
// ──────────────────────────────────────────────────────────────────

@Composable
fun EmployeeList(employees: List<Employee>) {
    LazyColumn(
        contentPadding = PaddingValues(12.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(employees, key = { it.employeeId }) { emp ->
            EmployeeCard(emp)
        }
    }
}

@Composable
fun EmployeeCard(emp: Employee) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape    = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Аватар-заглушка
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(
                        MaterialTheme.colorScheme.primaryContainer,
                        RoundedCornerShape(24.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text  = emp.lastName.take(1).uppercase(),
                    fontWeight = FontWeight.Bold,
                    fontSize   = 20.sp,
                    color      = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }

            Spacer(Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text       = "${emp.firstName ?: ""} ${emp.lastName}".trim(),
                    fontWeight = FontWeight.SemiBold,
                    fontSize   = 16.sp
                )
                Text(
                    text  = emp.email,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 13.sp
                )
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.padding(top = 4.dp)
                ) {
                    Chip(emp.jobId)
                    emp.salary?.let { Chip("$${it.toInt()}") }
                }
            }

            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text     = "#${emp.employeeId}",
                    fontSize = 12.sp,
                    color    = MaterialTheme.colorScheme.outline
                )
                Text(
                    text     = emp.hireDate,
                    fontSize = 11.sp,
                    color    = MaterialTheme.colorScheme.outline
                )
            }
        }
    }
}

// ──────────────────────────────────────────────────────────────────
//  Список отделов
// ──────────────────────────────────────────────────────────────────

@Composable
fun DepartmentList(departments: List<Department>) {
    LazyColumn(
        contentPadding = PaddingValues(12.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(departments, key = { it.departmentId }) { dept ->
            Card(
                modifier  = Modifier.fillMaxWidth(),
                shape     = RoundedCornerShape(12.dp),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text       = dept.departmentName,
                        fontWeight = FontWeight.Medium,
                        modifier   = Modifier.weight(1f)
                    )
                    Text(
                        text     = "ID: ${dept.departmentId}",
                        fontSize = 13.sp,
                        color    = MaterialTheme.colorScheme.outline
                    )
                }
            }
        }
    }
}

// ──────────────────────────────────────────────────────────────────
//  Утилиты
// ──────────────────────────────────────────────────────────────────

@Composable
fun Chip(text: String) {
    Surface(
        color  = MaterialTheme.colorScheme.secondaryContainer,
        shape  = RoundedCornerShape(8.dp)
    ) {
        Text(
            text     = text,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
            fontSize = 11.sp,
            color    = MaterialTheme.colorScheme.onSecondaryContainer
        )
    }
}

@Composable
fun ErrorView(message: String, onRetry: () -> Unit) {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(24.dp)) {
            Text("Ошибка подключения", fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Spacer(Modifier.height(8.dp))
            Text(message, color = MaterialTheme.colorScheme.error, fontSize = 13.sp)
            Spacer(Modifier.height(16.dp))
            Button(onClick = onRetry) {
                Text("Повторить")
            }
        }
    }
}