package sample;

import java.math.*;
import java.text.DecimalFormat;
import java.util.*;
import java.util.regex.*;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import javafx.scene.control.*;
import javafx.scene.control.Dialog;
import javafx.scene.control.Alert.AlertType;

import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;
import org.apache.commons.lang3.StringUtils;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;


/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author jivan108
 */
public class Calculator {

    private double ans;
    public boolean clearNext = false;
    private final Pattern funcSavePattern = Pattern.compile("\\w+\\([a-zA-Z,]+\\)=[A-Za-z0-9-!$%^&*()+ {}|/~=`:'÷<>?,.]+");
    final Pattern funcEvalPattern = Pattern.compile("\\w+\\([A-Za-z0-9-!$%^&*()÷+ {}|/~=`:'<>?,.]+\\)");
    private ArrayList<String> functions = new ArrayList<String>();
    public TextField expField;

    Calculator(TextField aExpField) {
        expField = aExpField;
    }

    public void initializeFunctions() {
        functions.add("sin");
        functions.add("sinh");
        functions.add("cos");
        functions.add("cosh");
        functions.add("tan");
        functions.add("tanh");
        functions.add("log");
        functions.add("ln");
        functions.add("√");
    }

    private Set<String> functionArray = new HashSet<>();

    public ArrayList<String> getFunctions() {
        return functions;
    }


    public String filterExpression(String exp) {
        final Pattern sciFunc = Pattern.compile("([(]+?[a-zA-z]+\\([a-zA-Z0-9^*+/#÷[(][)]]+[)]?)");

        String filteredExp = exp.replaceAll("÷", "/");
        filteredExp = exp.replaceAll("Ans", Double.toString(ans));
        filteredExp = filteredExp.replaceAll("Ran#", Double.toString(Math.random()));
        filteredExp = filteredExp.replaceAll("√",   "sqrt");
        filteredExp = filteredExp.replaceAll("log", "log10");
        filteredExp = filteredExp.replaceAll("ln", "log");
        filteredExp = filteredExp.replaceAll("÷", "/");

        String splitExp[] = filteredExp.split("[+*/-]");
            for (String e : splitExp) {
                if (e.matches("\\w+\\([A-Za-z0-9-!$%^&*()÷+ {}|/~=`:'<>?,.]+\\)")) {
                String functionName = e.substring(0, e.indexOf("("));
                for (String function : functionArray) {
                    if (function.substring(0, function.indexOf("(")).equals(functionName)) {
                        System.out.println("About to replace..." + e);
                        System.out.println("filteredExp before replace: " + filteredExp);
                        filteredExp = filteredExp.replace(e, Double.toString(evaluateFunction(expField, e)));
                    }
                }


            }
        }

//        while (sciFuncMatcher.find()) {
//            String group = sciFuncMatcher.group();
//
//
////            if (!")".equals(Character.toString(group.charAt(group.length() - 1)))) {
////                group.insert(group.length(), ")");
////            }
//
//            System.out.println("Original group: " + group);
//
//            String filteredGroup = group.replaceAll("\\(", "");
//            filteredGroup = filteredGroup.replaceAll("\\)", "");
//
//            System.out.println("Filtered group: " + filteredGroup);
//            System.out.println("Pattern: " + Pattern.quote(group));
//
//            filteredExp = filteredExp.replace(group, filteredGroup);
//
//        }


        System.out.println("filteredExp is: " + filteredExp);
        return filteredExp;
    }


    private void saveFunction(TextField expField) {
        String exp = expField.getText();
        String splitFunction[] = exp.split("=");
        for (String i : this.functionArray) {
            String splitI[] = i.split("=");
            if (splitFunction[0].equals(splitI[0])) {
                this.functionArray.remove(i);
                System.out.println(i + " was overridden with " + exp);
            }
        }
        this.functionArray.add(exp.trim());
        System.out.println(expField.getText());
        expField.setText("");
    }

    private Double evaluateFunction(TextField expField, String functionEval) {
        int expOpenBraceIndex = functionEval.indexOf("(");
        int expCloseBraceIndex = functionEval.indexOf(")");
        Object rawVariableNumbers[] = functionEval.substring(expOpenBraceIndex + 1, expCloseBraceIndex ).split(",");
        System.out.println(functionEval.substring(expOpenBraceIndex + 1, expCloseBraceIndex));
        ArrayList variableNumbers = new ArrayList<Double>();
        Set variableNames;
        Map variables = new HashMap<String, Double>();

        for (Object variableNumber : rawVariableNumbers) {
            System.out.println(variableNumber);
            Expression e = new ExpressionBuilder(variableNumber.toString())
                    .build();
            variableNumbers.add(e.evaluate());
        }
        rawVariableNumbers = variableNumbers.toArray();


        for (String function : functionArray) {

                if (function.substring(0, 1).equals(function.substring(0, 1))) {
                    String functionSplit[] = function.split("=");
                    String rawVariableNames[] = functionSplit[0].trim().substring(functionSplit[0].indexOf("(") + 1, functionSplit[0].indexOf(")")).split(",");
                    variableNames = new HashSet<String>(Arrays.asList(rawVariableNames));
                    for(String variableName : rawVariableNames) {
                        System.out.println(variableName);
                    }


                    String expInput = functionSplit[1];
                    expInput = expInput.replaceAll("÷", "/");
                    Expression e = new ExpressionBuilder(expInput)
                            .variables(variableNames)
                            .build();
                    for(int i = 0; i < rawVariableNames.length; i++) {
                        System.out.println("rawVariableNames length is: " + rawVariableNames.length + ". rawVariableNumbers length is: " + rawVariableNumbers.length);
                        variables.put(rawVariableNames[i], rawVariableNumbers[i]);
                    }
                    e.setVariables(variables);

                    //expField.setText(Double.toString(e.evaluate()));
                    clearNext = true;
                    ans = e.evaluate();


                }
            }

            return ans;




    }

    public void calculateExpressionWithMonos(TextField expField, String exp) {
        DecimalFormat df = new DecimalFormat("#.#####");
        Scanner s = new Scanner(System.in);
        Pattern letterPattern = Pattern.compile("[a-zA-Z]+[^[\\d]+]?");
        Pattern numberPattern = Pattern.compile("[+-[*/]]?[0-9]");

        // key: monomial letter. value: monomial number
        Multimap<String, String> monoMap = ArrayListMultimap.create();
        Hashtable<String, String> results = new Hashtable<String, String>();
        String result = "";

        String monoExpression = exp;

        String monos[] = monoExpression.split("(?=[+-[*/]])");



        for (String mono : monos) {

            if (Character.toString(mono.charAt(0)).equals("-") && Character.toString(mono.charAt(1)).equals("(")) {
                System.out.println("About to break with the following monomial: " + mono);
                break;

            }

            Matcher letterMatcher = letterPattern.matcher(mono);
            Matcher numberMatcher = numberPattern.matcher(mono);

            if (letterMatcher.find()) {

                System.out.println("Original Monomial: " + mono);
                Pattern digitPattern = Pattern.compile("\\d");
                Matcher digitMatcher = digitPattern.matcher(mono);

                if (!digitMatcher.find()) {
                    System.out.println("Digit not found.");

                    StringBuilder monoBuilder = new StringBuilder(mono);


                    monoBuilder.insert(letterMatcher.start(), "1");
                    mono = monoBuilder.toString();
                }

                String monoLetter = mono.substring(letterMatcher.start(), mono.length());

                System.out.println("Letter of monomial: " + monoLetter);
            }


            if (numberMatcher.find() && letterMatcher.find()) {
                String monoNumber = mono.substring(numberMatcher.start(), letterMatcher.start());

                System.out.println("Number of monomial: " + monoNumber + "\n");
            }

            letterMatcher = letterPattern.matcher(mono);
            numberMatcher = numberPattern.matcher(mono);

            if (letterMatcher.find() && numberMatcher.find()) {
                monoMap.put(mono.substring(letterMatcher.start(), mono.length()), mono.substring(numberMatcher.start(), letterMatcher.start()));
            }

            else {
                monoMap.put("", mono);
            }
        }

        if (monoExpression.contains("-(")) {
            String subtraction = monoExpression.substring(monoExpression.indexOf("-(") + 2, monoExpression.length() - 1);

            String monosInSubtraction[] = subtraction.split("(?=[+-[*/]])");

            for (String mono : monosInSubtraction) {

                System.out.println("Original mono in subtraction: " + mono);
                mono = StringUtils.replaceEach(mono, new String[]{"+", "-"}, new String[]{"-", "+"});
                if (!Character.toString(mono.charAt(0)).equals("+") && !Character.toString(mono.charAt(0)).equals("-")) {
                    mono = "-" + mono;
                }

                System.out.println("String in subtraction is: " + subtraction);

                Matcher letterMatcher = letterPattern.matcher(mono);
                Matcher numberMatcher = numberPattern.matcher(mono);

                if (letterMatcher.find()) {
                    String monoLetter = mono.substring(letterMatcher.start(), mono.length());
                    System.out.println("Monomial: " + mono);

                    System.out.println("Letter of monomial: " + monoLetter);
                }

                if (numberMatcher.find()) {
                    String monoNumber = mono.substring(numberMatcher.start(), letterMatcher.start());
                    System.out.println("Number of monomial: " + monoNumber + "\n");
                }

                monoMap.put(mono.substring(letterMatcher.start(), mono.length()), mono.substring(numberMatcher.start(), letterMatcher.start()));
            }
        }


        for (String monoLetter : monoMap.keySet()) {
            System.out.println("Letter: " + monoLetter + " has numbers: " + monoMap.get(monoLetter).toString());
            Expression e = new ExpressionBuilder(monoMap.get(monoLetter).toString().replace(",", ""))
                    .build();
            if (e.evaluate() > 0) {
                results.put((int) e.evaluate() + monoLetter, "+");
                result = result + "+" + df.format(e.evaluate()) + monoLetter;
            } else {
                results.put((int) e.evaluate() + monoLetter, "");
                result = result + df.format(e.evaluate()) + monoLetter;
            }
        }

        expField.setText(result);
    }

    public void calculateExpression(TextField expField, String exp) {
        ArrayList<Integer> xValues = new ArrayList<Integer>();

        final Pattern varPattern = Pattern.compile("[a-zA-Z]");

        Matcher funcSaveMatcher = funcSavePattern.matcher(exp);
        Matcher funcEvalMatcher = funcEvalPattern.matcher(exp);

        Matcher varMatcher = varPattern.matcher(exp);

        Map<String, Integer> vars = new HashMap<String, Integer>();

        // Check if user selected function notation
        if (funcSaveMatcher.matches()) {
            // funcNotationInput = funcField.getText().replaceAll("\\s+", "").trim();
            saveFunction(expField);
        }


        else {


            Expression e = new ExpressionBuilder(exp)
                    .build();
            double result = e.evaluate();

            DecimalFormat df = new DecimalFormat("###.#######");
            expField.setText(Double.toString(result));
            ans = e.evaluate();
        }

    }

}
