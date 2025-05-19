package com.example.dacs1;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class mainchay extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/dacs1/Dangnhap/dangnhap.fxml"));
            Parent root = loader.load();

            primaryStage.setTitle("Quản Lý Kho");
            primaryStage.setScene(new Scene(root, 747, 557));
            primaryStage.show();
        } catch (IOException e) {
            System.err.println("Không thể tải main.fxml");
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}