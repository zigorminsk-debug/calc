package com.example.calc

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.calc.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    // Состояние калькулятора
    private var currentInput = ""
    private var previousInput = ""
    private var operation: String? = null
    private var memory: Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupNumberButtons()
        setupOperationButtons()
        setupFunctionButtons()
    }

    private fun setupNumberButtons() {
        val numberButtons = listOf(
            binding.btn0 to "0",
            binding.btn1 to "1",
            binding.btn2 to "2",
            binding.btn3 to "3",
            binding.btn4 to "4",
            binding.btn5 to "5",
            binding.btn6 to "6",
            binding.btn7 to "7",
            binding.btn8 to "8",
            binding.btn9 to "9",
            binding.btnDot to "."
        )
        numberButtons.forEach { (button, value) ->
            button.setOnClickListener { onDigit(value) }
        }
    }

    private fun setupOperationButtons() {
        binding.btnAdd.setOnClickListener { onOperation("+") }
        binding.btnSub.setOnClickListener { onOperation("-") }
        binding.btnMul.setOnClickListener { onOperation("*") }
        binding.btnDiv.setOnClickListener { onOperation("/") }
        binding.btnPow.setOnClickListener { onOperation("^") }
        binding.btnEquals.setOnClickListener { onEquals() }
    }

    private fun setupFunctionButtons() {
        binding.btnClear.setOnClickListener { onClear() }
        binding.btnDelete.setOnClickListener { onDelete() }
        binding.btnSign.setOnClickListener { onSignToggle() }
        binding.btnPercent.setOnClickListener { onPercent() }
        binding.btnSqrt.setOnClickListener { onSqrt() }
        binding.btnSqr.setOnClickListener { onSquare() }
        binding.btnSin.setOnClickListener { onTrig("sin") }
        binding.btnCos.setOnClickListener { onTrig("cos") }
        binding.btnTan.setOnClickListener { onTrig("tan") }
        binding.btnLog.setOnClickListener { onLog("log10") }
        binding.btnLn.setOnClickListener { onLog("ln") }
        binding.btnFactorial.setOnClickListener { onFactorial() }
        binding.btnMemoryAdd.setOnClickListener { onMemoryAdd() }
        binding.btnMemoryRead.setOnClickListener { onMemoryRead() }
        binding.btnMemoryClear.setOnClickListener { onMemoryClear() }
    }

    private fun onDigit(digit: String) {
        if (digit == "." && currentInput.contains(".")) return
        currentInput += digit
        updateDisplay(currentInput)
    }

    private fun onOperation(op: String) {
        if (currentInput.isNotEmpty()) {
            if (previousInput.isNotEmpty() && operation != null) {
                onEquals()
            }
            previousInput = currentInput
            currentInput = ""
        }
        operation = op
        binding.tvExpression.text = "${formatNumber(previousInput.toDoubleOrNull() ?: 0.0)} $op"
    }

    private fun onEquals() {
        if (previousInput.isEmpty() || operation == null || currentInput.isEmpty()) return
        val a = previousInput.toDoubleOrNull() ?: 0.0
        val b = currentInput.toDoubleOrNull() ?: 0.0
        val result = when (operation) {
            "+" -> a + b
            "-" -> a - b
            "*" -> a * b
            "/" -> {
                if (b == 0.0) {
                    binding.tvResult.text = "Ошибка: деление на 0"
                    currentInput = ""
                    previousInput = ""
                    operation = null
                    binding.tvExpression.text = ""
                    return
                }
                a / b
            }
            "^" -> Math.pow(a, b)
            else -> return
        }
        currentInput = formatNumber(result)
        previousInput = ""
        operation = null
        binding.tvExpression.text = ""
        updateDisplay(currentInput)
    }

    private fun onClear() {
        currentInput = ""
        previousInput = ""
        operation = null
        binding.tvExpression.text = ""
        binding.tvResult.text = "0"
    }

    private fun onDelete() {
        if (currentInput.isNotEmpty()) {
            currentInput = currentInput.dropLast(1)
            updateDisplay(if (currentInput.isEmpty()) "0" else currentInput)
        }
    }

    private fun onSignToggle() {
        val value = currentInput.toDoubleOrNull()
        if (value != null) {
            currentInput = formatNumber(-value)
            updateDisplay(currentInput)
        }
    }

    private fun onPercent() {
        val value = currentInput.toDoubleOrNull() ?: 0.0
        currentInput = formatNumber(value / 100.0)
        updateDisplay(currentInput)
    }

    private fun onSqrt() {
        val value = currentInput.toDoubleOrNull() ?: 0.0
        if (value < 0) {
            binding.tvResult.text = "Ошибка"
            return
        }
        currentInput = formatNumber(Math.sqrt(value))
        updateDisplay(currentInput)
    }

    private fun onSquare() {
        val value = currentInput.toDoubleOrNull() ?: 0.0
        currentInput = formatNumber(value * value)
        updateDisplay(currentInput)
    }

    private fun onTrig(func: String) {
        val value = currentInput.toDoubleOrNull() ?: 0.0
        val radians = Math.toRadians(value)
        val result = when (func) {
            "sin" -> Math.sin(radians)
            "cos" -> Math.cos(radians)
            "tan" -> Math.tan(radians)
            else -> 0.0
        }
        currentInput = formatNumber(result)
        updateDisplay(currentInput)
    }

    private fun onLog(func: String) {
        val value = currentInput.toDoubleOrNull() ?: 0.0
        if (value <= 0) {
            binding.tvResult.text = "Ошибка"
            return
        }
        val result = when (func) {
            "log10" -> Math.log10(value)
            "ln" -> Math.log(value)
            else -> 0.0
        }
        currentInput = formatNumber(result)
        updateDisplay(currentInput)
    }

    private fun onFactorial() {
        val value = currentInput.toDoubleOrNull() ?: 0.0
        if (value < 0 || value != value.toInt().toDouble() || value > 20) {
            binding.tvResult.text = "Ошибка"
            return
        }
        var result = 1L
        for (i in 2..value.toInt()) {
            result *= i
        }
        currentInput = formatNumber(result.toDouble())
        updateDisplay(currentInput)
    }

    private fun onMemoryAdd() {
        val value = currentInput.toDoubleOrNull() ?: 0.0
        memory += value
        binding.tvMemory.text = "M: ${formatNumber(memory)}"
    }

    private fun onMemoryRead() {
        currentInput = formatNumber(memory)
        updateDisplay(currentInput)
    }

    private fun onMemoryClear() {
        memory = 0.0
        binding.tvMemory.text = "M: 0"
    }

    private fun updateDisplay(text: String) {
        binding.tvResult.text = text
    }

    private fun formatNumber(value: Double): String {
        return if (value == value.toInt().toDouble() && Math.abs(value) < 1e12) {
            value.toInt().toString()
        } else {
            java.text.DecimalFormat("#.########").format(value)
        }
    }
}
