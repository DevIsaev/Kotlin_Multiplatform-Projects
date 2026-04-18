package org.example.project.PostgreSQL

data class DatabaseConfig(
    val host: String = "localhost",
    val port: Int = 5432,
    val database: String = "mydb",
    val user: String = "admin",
    val password: String = "admin123"
) {
    val jdbcUrl: String
        get() = "jdbc:postgresql://$host:$port/$database"
}

interface HrRepository {
    suspend fun getEmployees(): List<Employee>
    suspend fun getDepartments(): List<Department>
    suspend fun getJobs(): List<Job>
    suspend fun getEmployeesByDepartment(departmentId: Int): List<Employee>
    fun close()
}

expect fun createHrRepository(config: DatabaseConfig): HrRepository