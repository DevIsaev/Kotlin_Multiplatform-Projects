package org.example.project.PostgreSQL

data class Employee(
    val employeeId: Int,
    val firstName: String?,
    val lastName: String,
    val email: String,
    val jobId: String,
    val salary: Double?,
    val departmentId: Int?,
    val hireDate: String
)

data class Department(
    val departmentId: Int,
    val departmentName: String,
    val locationId: Int?
)

data class Job(
    val jobId: String,
    val jobTitle: String,
    val minSalary: Double?,
    val maxSalary: Double?
)