package sample;

import java.text.DecimalFormat;
import java.util.*;
import java.util.regex.*;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;

import javafx.scene.layout.GridPane;
import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;
import org.apache.commons.lang3.StringUtils;



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
        if (exp.isEmpty()) {
            exp = "0";
        }


        String filteredExp = exp.replaceAll("Ans", Double.toString(ans));
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

                            filteredExp = filteredExp.replace(e, Double.toString(evaluateFunction(expField, e)));
                    }
                }


            }
        }


        return filteredExp;
    }


    private void saveFunction(TextField expField) {
        String exp = expField.getText();
        String splitFunction[] = exp.split("=");
        for (String i : this.functionArray) {
            String splitI[] = i.split("=");
            if (splitFunction[0].substring(0, splitFunction[0].indexOf("(")).equals(splitI[0].substring(0, splitI[0].indexOf("(")))) {
                Alert alert = new Alert(AlertType.CONFIRMATION);
                alert.setTitle("Formulas");
                alert.setHeaderText("Sobrescribir formula");
                alert.setContentText("¿Estas seguro que quieres sobrescribir " + i + " con " + exp + " ?");

                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.OK){
                    this.functionArray.remove(i);
                }

                else {
                    break;
                }



            }
        }
        this.functionArray.add(exp.trim());
        expField.setText("");
    }

    public void showFunctions() {
        TextArea textArea = new TextArea();
        textArea.setEditable(false);
        textArea.setWrapText(true);
        GridPane gridPane = new GridPane();
        gridPane.setMaxWidth(Double.MAX_VALUE);
        gridPane.add(textArea, 0, 0);

        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Formulas");
        alert.setHeaderText("Tus formulas");
        alert.getDialogPane().setContent(gridPane);
        String contentText = "";
        if (!functionArray.isEmpty()) {
            for (String function : functionArray) {
                contentText = contentText + function + "\n";
            }
            textArea.setText(contentText);
        }

        else {
            textArea.setText("No tienes formulas.");
        }
        alert.showAndWait();
    }

    private void deleteFunction(String input) {
        int openBraceIndex = input.indexOf("(") + 1;
        String function = input.substring(openBraceIndex, input.length() - 1);
        if (functionArray.contains(function)) {
            functionArray.remove(function);
        }

        else {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Formula no encontrada");
            alert.setContentText("La formula " + function + " no se pudo borrar. Esto puede ser porque la formula no existe.");

            alert.showAndWait();
        }


    }

    private Double evaluateFunction(TextField expField, String functionEval) {
        int expOpenBraceIndex = functionEval.indexOf("(");
        int expCloseBraceIndex = functionEval.indexOf(")");
        Object rawVariableNumbers[] = functionEval.substring(expOpenBraceIndex + 1, expCloseBraceIndex ).split(",");
        ArrayList variableNumbers = new ArrayList<Double>();
        Set variableNames;
        Map variables = new HashMap<String, Double>();

        for (Object variableNumber : rawVariableNumbers) {
            Expression e = new ExpressionBuilder(variableNumber.toString())
                    .build();
            variableNumbers.add(e.evaluate());
        }
        rawVariableNumbers = variableNumbers.toArray();


        for (String function : functionArray) {


                if (function.substring(0, expOpenBraceIndex).equals(functionEval.substring(0, expOpenBraceIndex))) {
                    String functionSplit[] = function.split("=");
                    String rawVariableNames[] = functionSplit[0].trim().substring(functionSplit[0].indexOf("(") + 1, functionSplit[0].indexOf(")")).split(",");
                    variableNames = new HashSet<>(Arrays.asList(rawVariableNames));

                    String expInput = functionSplit[1];
                    expInput = expInput.replaceAll("÷", "/");
                    System.out.println("expInput is: " + expInput);
                    Expression e = new ExpressionBuilder(expInput)
                            .variables(variableNames)
                            .build();
                    for(int i = 0; i < rawVariableNames.length; i++) {

                        System.out.println("Var Name: " + rawVariableNames[i] + "Value: " + rawVariableNumbers[i]);
                        variables.put(rawVariableNames[i], rawVariableNumbers[i]);
                    }
                    e.setVariables(variables);

                    //expField.setText(Double.toString(e.evaluate()));
                    clearNext = true;
                    ans = e.evaluate();


                }
            }


        System.out.println(ans);
            return ans;

    }

    public void calculateExpressionWithMonos(TextField expField, String monoExpression) {
        DecimalFormat df = new DecimalFormat("#.#####");
        Pattern letterPattern = Pattern.compile("[a-zA-Z]+[^[\\d]+]?");
        Pattern numberPattern = Pattern.compile("[+-[*/]]?[0-9]");

        // key: monomial letter. value: monomial number
        Multimap<String, String> monoMap = ArrayListMultimap.create();
        String result = "";

        String monos[] = monoExpression.split("(?=[+-])");


        for (String mono : monos) {

            if (Character.toString(mono.charAt(0)).equals("-") && Character.toString(mono.charAt(1)).equals("(")) {
                break;

            }

            Matcher letterMatcher = letterPattern.matcher(mono);
            Matcher numberMatcher = numberPattern.matcher(mono);

            if (letterMatcher.find()) {

                Pattern digitPattern = Pattern.compile("\\d");
                Matcher digitMatcher = digitPattern.matcher(mono);

                if (!digitMatcher.find()) {

                    StringBuilder monoBuilder = new StringBuilder(mono);


                    monoBuilder.insert(letterMatcher.start(), "1");
                    mono = monoBuilder.toString();
                }


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

                mono = StringUtils.replaceEach(mono, new String[]{"+", "-"}, new String[]{"-", "+"});
                if (!Character.toString(mono.charAt(0)).equals("+") && !Character.toString(mono.charAt(0)).equals("-")) {
                    mono = "-" + mono;
                }


                Matcher letterMatcher = letterPattern.matcher(mono);
                Matcher numberMatcher = numberPattern.matcher(mono);


                if (letterMatcher.find() && numberMatcher.find()) {
                    monoMap.put(mono.substring(letterMatcher.start(), mono.length()), mono.substring(numberMatcher.start(), letterMatcher.start()));
                }

                else {
                    monoMap.put("", mono);
                }


            }
        }


        for (String monoLetter : monoMap.keySet()) {
            Expression e = new ExpressionBuilder(monoMap.get(monoLetter).toString().replace(",", ""))
                    .build();
            if (e.evaluate() > 0) {
                result = result + "+" + df.format(e.evaluate()) + monoLetter;
            } else {

                result = result + df.format(e.evaluate()) + monoLetter;
            }
        }

        expField.setText(result);
    }

    public void calculateExpression(TextField expField, String exp) {

        final Pattern funcSavePattern = Pattern.compile("\\w+\\([a-zA-Z,]+\\)=[A-Za-z0-9-!$%^&*()+ {}|/~=`:'÷<>?,.]+");
        final Pattern funcDeletePattern = Pattern.compile("del\\(.*\\)");


        Matcher funcSaveMatcher = funcSavePattern.matcher(exp);
        Matcher funcDeleteMatcher = funcDeletePattern.matcher(exp);

        // Check if user selected function notation
        if (funcSaveMatcher.matches()) {
            saveFunction(expField);
        } else if (funcDeleteMatcher.matches()) {
            deleteFunction(exp);
            expField.setText("");

        } else {


            Expression e = new ExpressionBuilder(exp)
                    .build();
            double result = e.evaluate();

            String newResult = "";

            if (result % 1 == 0) {
                DecimalFormat decimalFormat = new DecimalFormat("0.#####");
                newResult = decimalFormat.format(result);
                expField.setText(newResult);

            }

            else {
                expField.setText(Double.toString(result));
            }

            ans = e.evaluate();
        }

    }

}
