/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.stage.Stage;

/**
 *
 * @author jivan108
 */
public class Algelator extends Application {
    
    @Override
    public void start(Stage stage) throws Exception {
        Font.loadFont(
                Algelator.class.getResource("algelator/Roboto-Thin.ttf").toExternalForm(),
                24
        );

        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("Calculator2.fxml"));

        Scene scene1 = new Scene(root);

        stage.setScene(scene1);

        stage.setMinWidth(700);
        stage.setMinHeight(520);

        stage.setMaxWidth(700);
        stage.setMaxHeight(520);

        stage.show();

    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        launch(args);
        
    }
    
}
