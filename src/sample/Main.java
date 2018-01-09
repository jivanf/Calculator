package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class Main extends Application {



    @Override
    public void start(Stage stage) throws Exception{
        Font.loadFont(
                Algelator.class.getResource("Roboto-Thin.ttf").toExternalForm(),
                24
        );


        Parent root = FXMLLoader.load(Main.class.getResource("/sample/Calculator2.fxml"));

        Scene scene1 = new Scene(root);

        stage.setScene(scene1);

        stage.setMinWidth(700);
        stage.setMinHeight(520);

        stage.setMaxWidth(700);
        stage.setMaxHeight(520);


        stage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
