/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sample;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.animation.FadeTransition;
import javafx.event.*;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.Node;
import javafx.scene.input.*;
import javafx.scene.layout.Background;
import javafx.scene.layout.GridPane;
import javafx.animation.PauseTransition;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.util.Duration;

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

    public void makeFadeTransition() {
        FadeTransition transition = new FadeTransition(Duration.millis(500));
        transition.setNode(calculator);
        transition.setFromValue(0);
        transition.setToValue(1);
        transition.play();
    }

    public void buttonAnimation(Button btn) {
        
        PauseTransition pause = new PauseTransition(Duration.seconds(0.1));
        btn.arm();
        pause.setOnFinished(e -> btn.disarm());
        pause.play();
        btn.fire();
    }

    public void displayTextOnAction(ActionEvent e) {
        Calculator calculator = new Calculator(input);
        calculator.initializeFunctions();
        if(calculator.getFunctions().contains(((Button) e.getSource()).getText())) {

            input.setText(input.getText() + ((Button) e.getSource()).getText() + "()");
            int inputIndex = input.getText().indexOf(input.getText() + ((Button) e.getSource()).getText());
            if(input.getText().isEmpty()) {

            }

            else {

            }


            System.out.println(inputIndex);

            System.out.println("Caret position: " + input.getCaretPosition());
        }

        else {
            input.setText(input.getText() + ((Button) e.getSource()).getText());
        }



    }

    public void delete() {
        if (!input.getText().isEmpty()) {
            StringBuilder s = new StringBuilder(input.getText());
            s.deleteCharAt(s.length() - 1);
            input.setText(s.toString());
        }
    }

    public void handle(KeyEvent event) {

        //final KeyCombination kb = new KeyCodeCombination(KeyCode.SHIFT, KeyCombination.SHIFT_DOWN);

        if (input.getCaretPosition() == 0) {
            input.setText(input.getText() + event.getText());
            System.out.println("Entering switch.");
            System.out.println(event.getCode());
            switch (event.getCode()) {
                case BACK_SPACE:
                    System.out.println("Tfak");
                    delete();
                    break;
            }
        }


//        switch (event.getCode()) {
//            case DIGIT1:
//                if(event.isShiftDown()) {
//                    buttonAnimation(one);
//                }
//                break;
//
//            case DIGIT2:
//                buttonAnimation(two);
//                break;
//
//            case DIGIT3:
//                buttonAnimation(three);
//                break;
//
//            case DIGIT4:
//                buttonAnimation(four);
//                break;
//
//            case DIGIT5:
//                buttonAnimation(five);
//                break;
//
//            case DIGIT6:
//                buttonAnimation(six);
//                break;
//
//            case DIGIT7:
//                buttonAnimation(seven);
//                break;
//
//            case DIGIT8:
//                buttonAnimation(eight);
//                break;
//
//            case DIGIT9:
//                //System.out.println(event.getText());
//                buttonAnimation(nine);
//                break;
//        }

    }

    public void clearTextOnAction() {
        input.setText("");
    }

    public void calculateOnAction() {
        String filteredExp = calculatorObj.filterExpression(input.getText());
        if (monoToggle.isSelected()) {
            calculatorObj.calculateExpressionWithMonos(input, filteredExp);
        }

        else {
            calculatorObj.calculateExpression(input, filteredExp);
        }

    }

}
