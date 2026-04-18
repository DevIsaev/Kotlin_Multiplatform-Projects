package org.example.project.PostgreSQL

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.sql.Connection
import java.sql.DriverManager

class JvmHrRepository(private val config: DatabaseConfig) : HrRepository {

    private var connection: Connection? = null

    // ──────────────────────────────────────────────
    //  Подключение
    // ──────────────────────────────────────────────

    private fun getConnection(): Connection {
        val conn = connection
        if (conn != null && !conn.isClosed) return conn

        // Загружаем драйвер явно (нужно для старых JVM)
        Class.forName("org.postgresql.Driver")

        return DriverManager.getConnection(
            config.jdbcUrl,
            config.user,
            config.password
        ).also { connection = it }
    }

    // ──────────────────────────────────────────────
    //  Запросы
    // ──────────────────────────────────────────────

    override suspend fun getEmployees(): List<Employee> = withContext(Dispatchers.IO) {
        val sql = """
            SELECT employee_id, first_name, last_name, email,
                   job_id, salary, department_id,
                   TO_CHAR(hire_date, 'YYYY-MM-DD') AS hire_date
            FROM employees
            ORDER BY last_name, first_name
        """.trimIndent()

        buildList {
            getConnection().prepareStatement(sql).use { stmt ->
                stmt.executeQuery().use { rs ->
                    while (rs.next()) {
                        add(
                            Employee(
                                employeeId  = rs.getInt("employee_id"),
                                firstName   = rs.getString("first_name"),
                                lastName    = rs.getString("last_name"),
                                email       = rs.getString("email"),
                                jobId       = rs.getString("job_id"),
                                salary      = rs.getDouble("salary")
                                    .takeIf { !rs.wasNull() },
                                departmentId = rs.getInt("department_id")
                                    .takeIf { !rs.wasNull() },
                                hireDate    = rs.getString("hire_date") ?: ""
                            )
                        )
                    }
                }
            }
        }
    }

    override suspend fun getDepartments(): List<Department> = withContext(Dispatchers.IO) {
        val sql = "SELECT department_id, department_name, location_id FROM departments ORDER BY department_name"

        buildList {
            getConnection().prepareStatement(sql).use { stmt ->
                stmt.executeQuery().use { rs ->
                    while (rs.next()) {
                        add(
                            Department(
                                departmentId   = rs.getInt("department_id"),
                                departmentName = rs.getString("department_name"),
                                locationId     = rs.getInt("location_id").takeIf { !rs.wasNull() }
                            )
                        )
                    }
                }
            }
        }
    }

    override suspend fun getJobs(): List<Job> = withContext(Dispatchers.IO) {
        val sql = "SELECT job_id, job_title, min_salary, max_salary FROM jobs ORDER BY job_title"

        buildList {
            getConnection().prepareStatement(sql).use { stmt ->
                stmt.executeQuery().use { rs ->
                    while (rs.next()) {
                        add(
                            Job(
                                jobId     = rs.getString("job_id"),
                                jobTitle  = rs.getString("job_title"),
                                minSalary = rs.getDouble("min_salary").takeIf { !rs.wasNull() },
                                maxSalary = rs.getDouble("max_salary").takeIf { !rs.wasNull() }
                            )
                        )
                    }
                }
            }
        }
    }

    override suspend fun getEmployeesByDepartment(departmentId: Int): List<Employee> =
        withContext(Dispatchers.IO) {
            val sql = """
                SELECT employee_id, first_name, last_name, email,
                       job_id, salary, department_id,
                       TO_CHAR(hire_date, 'YYYY-MM-DD') AS hire_date
                FROM employees
                WHERE department_id = ?
                ORDER BY last_name
            """.trimIndent()

            buildList {
                getConnection().prepareStatement(sql).use { stmt ->
                    stmt.setInt(1, departmentId)
                    stmt.executeQuery().use { rs ->
                        while (rs.next()) {
                            add(
                                Employee(
                                    employeeId   = rs.getInt("employee_id"),
                                    firstName    = rs.getString("first_name"),
                                    lastName     = rs.getString("last_name"),
                                    email        = rs.getString("email"),
                                    jobId        = rs.getString("job_id"),
                                    salary       = rs.getDouble("salary").takeIf { !rs.wasNull() },
                                    departmentId = rs.getInt("department_id").takeIf { !rs.wasNull() },
                                    hireDate     = rs.getString("hire_date") ?: ""
                                )
                            )
                        }
                    }
                }
            }
        }

    override fun close() {
        connection?.close()
        connection = null
    }
}

// actual-реализация expect-функции
actual fun createHrRepository(config: DatabaseConfig): HrRepository =
    JvmHrRepository(config)