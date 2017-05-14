package frame.functions.realizations.Factories;

import frame.functions.Function;
import frame.functions.realizations.*;
import frame.functions.realizations.operations.Operation;
import frame.functions.realizations.operations.realizations.*;

import java.util.*;

/**
 * Created by Денис on 09.06.2016.
 */
public class MVFunctionFactory extends FunctionFactory {

    //origins
    private String interpretation;
    private HashMap<String, Var> vars = new HashMap<>();
    private Operation func;

    //to build
    private HashSet<String> standardOperationSet
            = new HashSet<>(Arrays.asList(StandardOperationsSRC.literals));


    @Override
    public Function buildFunction(String interpretation) throws Exception {
        this.interpretation = interpretation;
        Stack<Operation> varStack = new Stack<Operation>();
        Stack<String> operationsStack = new Stack<String>();

        String literal = "";
        boolean inLiteralStatus = false;

        //to execute from cast to cast
        String interpretationInCasts = "(" + interpretation + ")";
        if (interpretationInCasts.contains(">=")) {
            interpretationInCasts = interpretationInCasts.replaceAll(">=", ")>=(");
            interpretationInCasts = "(" + interpretationInCasts + ")";
        }
        if (interpretationInCasts.contains("<=")) {
            interpretationInCasts = interpretationInCasts.replaceAll("<=", ")<=(");
            interpretationInCasts = "(" + interpretationInCasts + ")";
        }
        if (interpretationInCasts.contains("==")) {
            interpretationInCasts = interpretationInCasts.replaceAll("==", ")=(");
            interpretationInCasts = "(" + interpretationInCasts + ")";
        }
        char previousChar = '(';
        for (char c : interpretationInCasts.toCharArray()) {
            if (c == '(') {
                if (inLiteralStatus) {
                    inLiteralStatus = false;
                    if (standardOperationSet.contains(literal)) {
                        operationsStack.push(literal);
                    } else if (vars.containsKey(literal)) {
                        varStack.push(vars.get(literal));
                    } else {
                        try {
                            double con = Double.parseDouble(literal);
                            varStack.push(new Const(con));
                        } catch (NumberFormatException nfe) {
                            vars.put(literal, new Var(literal));
                            varStack.push(vars.get(literal));
                        }
                    }
                }
                operationsStack.add(String.valueOf(c));
                literal = "";
            } else if (c == ')') {
                if (inLiteralStatus) {
                    inLiteralStatus = false;
                    if (standardOperationSet.contains(literal)) {
                        operationsStack.push(literal);
                    } else if (vars.containsKey(literal)) {
                        varStack.push(vars.get(literal));
                    } else {
                        try {
                            double con = Double.parseDouble(literal);
                            varStack.push(new Const(con));
                        } catch (NumberFormatException nfe) {
                            vars.put(literal, new Var(literal));
                            varStack.push(vars.get(literal));
                        }
                    }
                }
                String op;
                try {
                    while (!(op = operationsStack.pop()).equals("(")) {
                        executeOperation(op, varStack, operationsStack);
                    }
                } catch (EmptyStackException e) {
                    System.out.println("Empty stack");
                }
                literal = "";
            } else if (c == '+') {
                String currentOperation = String.valueOf(c);
                if (inLiteralStatus) {
                    if (standardOperationSet.contains(literal)) {
                        operationsStack.push(literal);
                    } else if (vars.containsKey(literal)) {
                        varStack.push(vars.get(literal));
                    } else {
                        try {
                            double con = Double.parseDouble(literal);
                            varStack.push(new Const(con));
                        } catch (NumberFormatException nfe) {
                            vars.put(literal, new Var(literal));
                            varStack.push(vars.get(literal));
                        }
                    }
                    inLiteralStatus = false;
                }
                int currentPriority = priority(currentOperation);
                String op;
                while (priority(op = operationsStack.pop()) < currentPriority) {
                    executeOperation(op, varStack, operationsStack);
                }
                operationsStack.push(op);
                operationsStack.push(currentOperation);
                literal = "";
            } else if (c == '-') {//FIXME
                String currentOperation = String.valueOf(c);
                if (inLiteralStatus) {
                    if (standardOperationSet.contains(literal)) {
                        operationsStack.push(literal);
                    } else if (vars.containsKey(literal)) {
                        varStack.push(vars.get(literal));
                    } else {
                        try {
                            double con = Double.parseDouble(literal);
                            varStack.push(new Const(con));
                        } catch (NumberFormatException nfe) {
                            vars.put(literal, new Var(literal));
                            varStack.push(vars.get(literal));
                        }
                    }
                    inLiteralStatus = false;
                    //executeOperation("-", varStack, operationsStack);
                }

                if (previousChar == '(') {
                    operationsStack.push("neg");
                } else {
                    int currentPriority = priority("+");
                    String op;
                    while (priority(op = operationsStack.pop()) < currentPriority) {
                        executeOperation(op, varStack, operationsStack);
                    }
                    operationsStack.push(op);
                    operationsStack.push("+");
                    operationsStack.push("neg");
                }
                literal = "";
//                if (previousChar != '(') {
//                    String currentOperation = "+";
//                    int currentPriority = priority(currentOperation);
//                    String op;
//                    while (priority(op = operationsStack.pop()) < currentPriority) {
//                        executeOperation(op, varStack, operationsStack);
//                    }
//                    //operationsStack.push(op);
//                    operationsStack.push("+");
//                    //operationsStack.push(String.valueOf(c));
//                    operationsStack.push("neg");
//                } else {
//                    //trying to fix
//                    operationsStack.push("+");
//                    //
//
//                    operationsStack.push("neg");
//                }

                literal = "";
            } else if (c == '*') {
                String currentOperation = String.valueOf(c);
                if (inLiteralStatus) {
                    if (standardOperationSet.contains(literal)) {
                        operationsStack.push(literal);
                    } else if (vars.containsKey(literal)) {
                        varStack.push(vars.get(literal));
                    } else {
                        try {
                            double con = Double.parseDouble(literal);
                            varStack.push(new Const(con));
                        } catch (NumberFormatException nfe) {
                            vars.put(literal, new Var(literal));
                            varStack.push(vars.get(literal));
                        }
                    }
                    inLiteralStatus = false;
                }
                int currentPriority = priority(currentOperation);
                String op;
                while (priority(op = operationsStack.pop()) < currentPriority) {
                    executeOperation(op, varStack, operationsStack);
                }
                operationsStack.push(op);
                operationsStack.push(currentOperation);
                literal = "";
            } else if (c == '/') {
                String currentOperation = String.valueOf(c);
                if (inLiteralStatus) {
                    if (standardOperationSet.contains(literal)) {
                        operationsStack.push(literal);
                    } else if (vars.containsKey(literal)) {
                        varStack.push(vars.get(literal));
                    } else {
                        try {
                            double con = Double.parseDouble(literal);
                            varStack.push(new Const(con));
                        } catch (NumberFormatException nfe) {
                            vars.put(literal, new Var(literal));
                            varStack.push(vars.get(literal));
                        }
                    }
                    inLiteralStatus = false;
                }
                int currentPriority = priority(currentOperation);
                String op;
                while (priority(op = operationsStack.pop()) < currentPriority) {
                    executeOperation(op, varStack, operationsStack);
                }
                operationsStack.push(op);
                operationsStack.push(String.valueOf(c));
                literal = "";
            } else if (c == '^') {
                String currentOperation = String.valueOf(c);
                if (inLiteralStatus) {
                    if (standardOperationSet.contains(literal)) {
                        operationsStack.push(literal);
                    } else if (vars.containsKey(literal)) {
                        varStack.push(vars.get(literal));
                    } else {
                        try {
                            double con = Double.parseDouble(literal);
                            varStack.push(new Const(con));
                        } catch (NumberFormatException nfe) {
                            vars.put(literal, new Var(literal));
                            varStack.push(vars.get(literal));
                        }
                    }
                    inLiteralStatus = false;
                }
                int currentPriority = priority(currentOperation);
                String op;
                while (priority(op = operationsStack.pop()) < currentPriority) {
                    executeOperation(op, varStack, operationsStack);
                }
                operationsStack.push(op);
                operationsStack.push(String.valueOf(c));
                literal = "";
            } else if (c == 'd') {
                if (inLiteralStatus) {
                    literal += "d";
                    if (standardOperationSet.contains(literal)) {
                        operationsStack.push(literal);
                    } else if (vars.containsKey(literal)) {
                        varStack.push(vars.get(literal));
                    } else {
                        try {
                            double con = Double.parseDouble(literal);
                            varStack.push(new Const(con));
                        } catch (NumberFormatException nfe) {
                            vars.put(literal, new Var(literal));
                            varStack.push(vars.get(literal));
                        }
                    }
                    inLiteralStatus = false;
                } else {
                    operationsStack.add(String.valueOf(c));
                    literal = "";
                }
            } else if (c == ' ') {
                if (inLiteralStatus) {
                    if (standardOperationSet.contains(literal)) {
                        operationsStack.add(literal);
                    } else if (vars.containsKey(literal)) {
                        varStack.push(vars.get(literal));
                    } else {
                        try {
                            double con = Double.parseDouble(literal);
                            varStack.push(new Const(con));
                        } catch (NumberFormatException nfe) {
                            vars.put(literal, new Var(literal));
                            varStack.push(vars.get(literal));
                        }
                    }
                    inLiteralStatus = false;
                }
                literal = "";
            } else {
                literal += c;
                inLiteralStatus = true;
            }
            previousChar = c;
        }

        //FIXME to logger plz
        //System.out.println("Function created successfully.");

        func = varStack.pop();

        return new FunctionMV(
                  func
                , vars
        );

    }

    private void executeOperation(String op, Stack<Operation> varStack, Stack<String> operationsStack) {
        if (op.equals("+")) {
            /*String operation_will_destroyed;
            if (!operationsStack.empty()) {
                while (!(operation_will_destroyed = operationsStack.pop()).equals("(")) {
                    executeOperation(operation_will_destroyed, varStack, operationsStack);
                }
            }*/
            Operation arg1 = varStack.pop();
            Operation arg2 = varStack.pop();
            varStack.push(new Sum(arg2, arg1));
        } else if (op.equals("-")) {
            Operation arg = varStack.pop();
            varStack.push(new Neg(arg));
            //if (!operationsStack.peek().equals("(")) {
            operationsStack.push("+");
            //}
        } else if (op.equals("neg")) {
            Operation arg = varStack.pop();
            varStack.push(new Neg(arg));
        } else if (op.equals("*")) {
            Operation arg1 = varStack.pop();
            Operation arg2 = varStack.pop();
            varStack.push(new Mult(arg2, arg1));

        } else if (op.equals("/")) {
            Operation arg1 = varStack.pop();
            Operation arg2 = varStack.pop();
            varStack.push(new Div(arg2, arg1));

        } else if (op.equals("d")) {
            Operation arg = varStack.pop();
            varStack.push(new Dif(arg));

        } else if (op.equals("^")) {
            Operation arg1 = varStack.pop();
            Operation arg2 = varStack.pop();
            varStack.push(new Pow(arg2, arg1));
        } else if (op.equals("ln")) {
            Operation arg1 = varStack.pop();
            varStack.push(new Ln(arg1));
        } else if (op.equals("exp")) {
            Operation arg1 = varStack.pop();
            varStack.push(new Exp(arg1));
        } else if (op.equals("sin")) {
            Operation arg1 = varStack.pop();
            varStack.push(new Sin(arg1));
        } else if (op.equals("cos")) {
            Operation arg1 = varStack.pop();
            varStack.push(new Cos(arg1));
        } else if (op.equals("<=")) {
            Operation arg1 = varStack.pop();
            Operation arg2 = varStack.pop();
            varStack.push(new LessOrEqual(arg2, arg1));
        } else if (op.equals(">=")) {
            Operation arg1 = varStack.pop();
            Operation arg2 = varStack.pop();
            varStack.push(new GraterOrEqual(arg2, arg1));
        }
    }

    private int priority(String s) {
        if (s.equals("-") || s.equals("neg") || s.equals("d")
                || s.equals("ln") || s.equals("exp")
                || s.equals("sin") || s.equals("cos"))
            return 1;
        else if (s.equals("^")) return 2;
        else if (s.equals("*") || s.equals("/")) return 3;
        else if (s.equals("(") || (s.equals("<=") || (s.equals(">=")))) return 5;
        else return 4;
    }
}
