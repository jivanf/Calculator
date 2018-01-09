/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sample;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.*;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.scene.layout.GridPane;

/**
 * FXML Controller class
 * 
* @author jivan108
 */
public class CalculatorController implements Initializable {

    @FXML
    GridPane calculator;

    @FXML
    ToggleButton monoToggle;

    @FXML
    TextField input;

    @FXML
    Button one;

    @FXML
    Button two;

    @FXML
    Button three;

    @FXML
    Button four;

    @FXML
    Button five;

    @FXML
    Button six;

    @FXML
    Button seven;

    @FXML
    Button eight;

    @FXML
    Button nine;

    private Calculator calculatorObj = new Calculator(input);

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    public void displayTextOnAction(ActionEvent e) {
        Calculator calculator = new Calculator(input);
        calculator.initializeFunctions();
        if(calculator.getFunctions().contains(((Button) e.getSource()).getText())) {

            input.setText(input.getText() + ((Button) e.getSource()).getText() + "()");
        }



    }

    public void delete() {
        if (!input.getText().isEmpty()) {
            StringBuilder s = new StringBuilder(input.getText());
            s.deleteCharAt(s.length() - 1);
            input.setText(s.toString());
        }
    }

    public void list() {
        calculatorObj.showFunctions();
    }

    public void handle(KeyEvent event) {

        switch (event.getCode()) {
            case ENTER:
                calculateOnAction();
                break;
        }

        if (input.getSelection().getStart() == 0 && input.getSelection().getEnd() == 0 && event.getCode() == KeyCode.SHIFT) {
            System.out.println("gesfji");
            input.setText(input.getText() + event.getText());
            switch (event.getCode()) {
                case BACK_SPACE:
                    delete();
                    break;
            }

        }
    }



    public void clearTextOnAction() {
        input.setText("");
    }

    public void calculateOnAction() {
        String filteredExp = null;
        try {
            filteredExp = calculatorObj.filterExpression(input.getText());
        } catch (Exception e) {

        }
        if (monoToggle.isSelected()) {
            calculatorObj.calculateExpressionWithMonos(input, filteredExp);
        }

        else {
            try {
                calculatorObj.calculateExpression(input, filteredExp);
            } catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Expresion invalida");
                alert.setContentText("Hubo un error con la expresión dada. Favor de intentar de nuevo.");
                alert.showAndWait();
            }
        }

    }

}
