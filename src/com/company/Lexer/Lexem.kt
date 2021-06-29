package com.company.Lexer

import java.util.regex.Pattern

enum class Lexem(pattern: String) {
    NUM("0|[1-9]{1}[0-9]*"),
    LCB("\\{"),
    RCB("\\}"),
    LB("\\("),
    RB("\\)"),
    C_OP(";"),
    GOTO(""),
    GOTO_INDEX(""),
    ASSIGN_OP("="),
    METHOD("\\."),
    FUNC_OP("add|remove|get|contains"),
    TYPE_W("new"),
    TYPE("Set|List"),
    WHILE("while"),
    FOR("for"),
    IF("if"),
    ELSE("else"),
    PRINT("print"),
    BOOL_OP("<|>|<=|>=|==|!="),
    BOOL("true|false"),
    VAR("[a-zA-Z][a-zA-Z0-9_]*"),
    INC("\\+\\+"),
    DEC("\\--"),
    OP("[*|/|+|-]");

    val pattern: Pattern = Pattern.compile(pattern)

}