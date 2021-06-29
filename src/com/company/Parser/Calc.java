package com.company.Parser;

import com.company.Lexer.Lexem;
import com.company.Lexer.Token;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

public class Calc {
    public static LinkedList<Token> calc = new LinkedList<>();

    public static LinkedList<Token> makePoliz(Queue<Token> input) {
        while (!input.isEmpty()) {
            Token token = input.peek();
            if (!(token.type == Lexem.WHILE || token.type == Lexem.IF)) {
                makePolizFromExpr(input);
            }
            else {
                makePolizFromWhile(input, token);
            }
        }

        return calc;
    }

    private static void makePolizFromWhile(Queue<Token> input, Token tmp) {
        Queue<Token> boolExpr = new LinkedList<>();
        input.poll();
        Token token = input.poll();
        int index = calc.size();
        while (true) {
            assert token != null;
            if (token.type == Lexem.LCB) break;
            boolExpr.add(token);
            token = input.poll();
        }

        makePolizFromExpr(boolExpr);
        if (tmp.type == Lexem.WHILE) {
            calc.add(new Token(Lexem.GOTO_INDEX, Integer.toString(p(calc.size(), input))));
        }
        String p = Integer.toString(p(calc.size(), input));
        if (tmp.type == Lexem.IF) {
            calc.add(new Token(Lexem.GOTO_INDEX, p));
        }
        calc.add(new Token(Lexem.GOTO, "!F"));

        Queue<Token> expr = new LinkedList<>();
        token = input.poll();
        while (true) {
            assert token != null;
            if (token.type == Lexem.RCB) break;
            if (token.type == Lexem.WHILE || token.type == Lexem.IF) {
                makePolizFromExpr(expr);
                makePolizFromWhile(input, token);
            }
            if (!(token.type == Lexem.WHILE || token.type == Lexem.IF))
                expr.add(token);
            token = input.poll();
        }
        makePolizFromExpr(expr);
        if (tmp.type != Lexem.IF)
            calc.add(new Token(Lexem.GOTO_INDEX, Integer.toString(index)));
        if (tmp.type != Lexem.WHILE)
            calc.add(new Token(Lexem.GOTO_INDEX, p));
        calc.add(new Token(Lexem.GOTO, "!"));
    }

    private static int p(int size, Queue<Token> tokens) {
        int p = size;
        int i = 1;

        Queue<Token> newtokens = new LinkedList<>(tokens);
        newtokens.poll();
        Token newtoken;

        while (i > 0){
            if (newtokens.isEmpty())
            {
                break;
            }
            else
            {
                newtoken = newtokens.poll();
            }
            if (newtoken.type == Lexem.WHILE || newtoken.type == Lexem.IF) {
                i++;
                p--;
            }
            if (newtoken.type == Lexem.RCB) {
                i--;
            }
            if (newtoken.type != Lexem.C_OP) {
                if(!(newtoken.type == Lexem.INC || newtoken.type == Lexem.DEC)){
                    p++;
                }
                else p = p + 4;
            }
        }
        p+=4;

        return p;
    }


    private static void makePolizFromExpr(Queue<Token> input) {
        Stack<Token> stack = new Stack<>();

        while (!input.isEmpty()) {
            Token token = input.peek();

            if (token.type == Lexem.WHILE || token.type == Lexem.IF) {
                break;
            }

            if (token.type == Lexem.TYPE_W) {
                stack.add(token);
            }

            if (token.type == Lexem.TYPE) {
                calc.add(token);
                calc.add(stack.pop());
            }

            token = input.poll();
            assert token != null;
            if (token.type == Lexem.INC || token.type == Lexem.DEC)
            {
                calc.add(calc.getLast());
                Token tmpToken = new Token(Lexem.NUM, "1");
                calc.add(tmpToken);
                if (token.type == Lexem.INC)
                {
                    tmpToken = new Token(Lexem.OP, "+");
                }
                else {tmpToken = new Token(Lexem.OP, "-");}
                calc.add(tmpToken);
                calc.add(new Token(Lexem.ASSIGN_OP, "="));
            }

            //Если лексема является числом или переменной, добавляем ее в ПОЛИЗ-массив.
            if (token.type == Lexem.VAR || token.type == Lexem.NUM) {
                calc.add(token);
            }

            //Если лексема является бинарной операцией, тогда:
            if (token.type == Lexem.OP || token.type == Lexem.BOOL_OP || token.type == Lexem.ASSIGN_OP || token.type == Lexem.FUNC_OP) {
                if (!stack.empty()) {
                    while (getPriorOfOp(token.data) >= getPriorOfOp(stack.peek().data)) {
                        calc.add(stack.pop());
                        if (stack.empty()){
                            break;
                        }
                    }
                }
                stack.push(token);
            }

            //Если лексема является открывающей скобкой, помещаем ее в стек.
            if (token.type == Lexem.LB) {
                stack.push(token);
            }

            if (token.type == Lexem.RB) {
                if (!stack.empty()) {
                    while (!stack.empty() && stack.peek().type != Lexem.LB) {
                        calc.add(stack.pop());
                    }
                    if (!stack.empty() && stack.peek().type == Lexem.LB) {
                        stack.pop();
                    }
                }
            }

            if (token.type == Lexem.C_OP) {
                while (!stack.empty()) {
                    calc.add(stack.pop());
                }
            }
        }

        while (!stack.empty()) {
            calc.add(stack.pop());
        }
    }

    private static int getPriorOfOp(String op) {
        return switch (op) {
            case "*", "/" -> 0;
            case "+", "-" -> 2;
            case ">", ">=", "<", "<=", "==", "!=" -> 3;
            case "=" -> 5;
            default -> 4;
        };
    }

}
