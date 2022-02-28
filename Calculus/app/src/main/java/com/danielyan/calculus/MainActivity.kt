package com.danielyan.calculus

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import android.widget.TextView

class MainActivity : AppCompatActivity() {

    lateinit var currentNumView: TextView

    var current: Double = .0 // текущее значение
    var temp: Double = .0 // вспомогательное значение для положительного и отрицательного сложения
    var point: Long = 10 // вспомогательное значение для ввода вещественной части
    var operation: Int = 0 // селектор текущей операции
    var multitemp: Double = 1.0 // вспомогательное значение для умножения
    var divtemp: Double = .0 // вспомогательное значение для деления

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        currentNumView = findViewById(R.id.textView)
        currentNumView.text = "0"
    }

    // Сохранение состояния
    override fun onSaveInstanceState(outState: Bundle) {

        outState?.run {
            putDouble("CURRENT", current)
            putDouble("TEMP", temp)
            putLong("POINT", point)
            putInt("OPERATION", operation)
            putDouble("MULTITEMP", multitemp)
            putDouble("DIVTEMP", divtemp)
            putString("TEXT", currentNumView.text.toString())
        }

        super.onSaveInstanceState(outState)
    }

    // Восстановление состояния при наличии
    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)

        current = savedInstanceState.getDouble("CURRENT")
        temp = savedInstanceState.getDouble("TEMP")
        point = savedInstanceState.getLong("POINT")
        operation = savedInstanceState.getInt("OPERATION")
        multitemp = savedInstanceState.getDouble("MULTITEMP")
        divtemp = savedInstanceState.getDouble("DIVTEMP")
        currentNumView.text = savedInstanceState.getString("TEXT")

    }

    // Кнопки для ввода цифр 0-9

    fun button0(view: View) = number(0)

    fun button1(view: View) = number(1)

    fun button2(view: View) = number(2)

    fun button3(view: View) = number(3)

    fun button4(view: View) = number(4)

    fun button5(view: View) = number(5)

    fun button6(view: View) = number(6)

    fun button7(view: View) = number(7)

    fun button8(view: View) = number(8)

    fun button9(view: View) = number(9)

    // Функция обработки цифр после их ввода с учетом выбранного режима операции,
    // 0 - по умолчанию, 1 - после окончания вычислений, 2 - деление,
    // 3 - резерв, 4 - для ввода вещественной части, 5 - отрицательное сложение
    fun number (number: Byte) {
        if (operation == 0 || operation == 2 || operation == 3 || operation == 5) {
            current *= 10
            current += number
            printValue()
        }
        if (operation == 1) {
            current = number.toDouble()
            operation = 0
            printValue()
        }
        if (operation == 4) {
            //pointValue *= 10
            //pointValue += number
            current = current + number.toDouble() / point
            point *= 10
            printValue()
        }
    }

    // Кнопка сложения
    fun buttonPlus(view: View) {
        if (operation == 2)
            current = divtemp/current
        pointValue()
        if (operation == 5) {
            current = temp - current
            operation = 1
            temp = .0
        }
        if (multitemp != 1.toDouble()) {
            current *= multitemp
            multitemp = 1.0
        }
        temp += current
        current = .0
        printValue()
    }

    // Кнопка отрицательного сложения
    fun buttonMinus(view: View) {
        pointValue()
        if (multitemp != 1.toDouble()) {
            current *= multitemp
            multitemp = 1.0
        }
        if (operation != 5) {
            operation = 5
            temp = current
        }
        else {
            current = temp - current
            temp = current
        }
        current = .0
        point = 10
        printValue()
    }

    // Кнопка умножения
    fun buttonMultiplication(view: View) {
        if (operation == 2)
            current = divtemp/current
        pointValue()
        //if (operatiion == 4)
        //    operatiion = 0
        if (temp != 0.toDouble()) {
            current += temp
            temp = .0
        }
        multitemp *= current
        current = .0
        point = 10
        printValue()
    }

    // Кнопка деления
    fun buttonDivision(view: View) {
        if (operation == 2)
            current = divtemp/current
        pointValue()
        if (multitemp != 1.toDouble()) {
            current *= multitemp
            multitemp = 1.0
        }
        if (temp != 0.toDouble()) {
            current += temp
            temp = .0
        }
        divtemp = current
        current = .0
        point = 10
        operation = 2
        printValue()
    }

    // Кнопка завершения вычисления (=)
    fun buttonEquals(view: View) {
        if (operation == 5) {
            current = temp - current
        }
        else
            current += temp
        temp = .0
        current *= multitemp
        multitemp = 1.0
        if (operation == 2)
            current = divtemp/current
        pointValue()
        printValue()
        operation = 1
    }
    // Кнопка обнуления
    fun buttonDelete(view: View) {
        current = .0
        temp = .0
        point = 10
        operation = 0
        multitemp = 1.0
        divtemp = .0
        currentNumView.text = "0"
    }

    // Кнопка для ввода вещественной части числа
    fun buttonPoint(view: View) {
        if (operation != 4 && current * 10 % 10 == .0) {
            currentNumView.text = "${currentNumView.text}" + "."
            operation = 4
        }
    }

    // Функция для завершения рассчета вещественной части введенного числа
    fun pointValue() {
        point = 10
        operation = 0
    }

    // Функция вывода текущего значения/результата
    fun printValue() {
        if (current*10 % 10 == 0.0)
            currentNumView.text = current.toLong().toString()
        else
            currentNumView.text = current.toString()
    }
}