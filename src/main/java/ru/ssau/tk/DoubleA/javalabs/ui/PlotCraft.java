package ru.ssau.tk.DoubleA.javalabs.ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import java.io.IOException;

public class PlotCraft extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/ui/fxml/main.fxml"));
        Image icon = new Image("file:src/main/resources/ui/icons/function.png");
        stage.getIcons().add(icon);
        stage.setTitle("PlotCraft");
        stage.setMinWidth(800);
        stage.setMinHeight(600);

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
