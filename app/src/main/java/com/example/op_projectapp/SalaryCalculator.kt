package com.example.op_projectapp

// SalaryCalculator.kt
object SalaryCalculator {
    fun calculateSalary(wage: String, hours: Int, days: Int, rest: String, tax: String): Int {
        val wage = wage.toInt()
        val monthlySalary = wage * hours * days * 4
        val restAmount = if (rest == "주휴수당 포함" && hours * days >= 15) {
            (hours.coerceAtMost(8) * days.coerceAtMost(5) * wage).toDouble()
        } else 0.0
        val taxRate = when (tax) {
            "4대보험 적용 (9.32%)" -> 0.0932
            "소득세 적용 (3.3%)" -> 0.033
            else -> 0.0
        }
        val taxAmount = (monthlySalary + restAmount) * taxRate
        return (monthlySalary + restAmount - taxAmount).toInt()
    }
}
