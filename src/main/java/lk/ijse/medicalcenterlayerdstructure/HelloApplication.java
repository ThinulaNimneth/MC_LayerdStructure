package lk.ijse.medicalcenterlayerdstructure;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class HelloApplication extends Application {

    @Override
    public void start(Stage stage) throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(
                getClass().getResource("/lk/ijse/medicalcenterlayerdstructure/login.fxml")
        );

        Scene scene = new Scene(fxmlLoader.load(), 520, 400);
        stage.setTitle("MediCare System");
        stage.setScene(scene);
        stage.setResizable(false); //cant resize


        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}