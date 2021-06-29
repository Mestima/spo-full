package com.company

import com.company.Lexer.Lexer.lex
import com.company.Parser.Parser.parse
import kotlin.jvm.JvmStatic
import com.company.Parser.Calc
import com.company.StackMachine.Executor
import java.lang.Exception

object Main {
    @JvmStatic
    fun main(args: Array<String>) {
        val tokens = lex("g = 12; i = 0; array new List; array.add(3+5); array.add(12); array.add(3*12); array.add(12/3); arrayNew new Set; arrayNew.add(3); arrayNew.add(4* 8 / 2); c = array.get(2); while(i < 12){i = i + 1;} g--; g = g - 10;")
        println("--------- Доступные токены: ------------")
        for (value in tokens) {
            println(value)
        }
        try {
            parse(tokens)
        } catch (ex: Exception) {
            System.err.println(ex)
            System.exit(1)
        }
        println("-------------- ОПЗ: ------------")
        val testCalc = Calc.makePoliz(tokens)
        var i = 1
        for (token in testCalc) {
            println("$i $token")
            i++
        }
        println("--------- Таблица переменных: -----------")
        Executor.exec(testCalc)
    }
}