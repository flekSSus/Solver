import javafx.application.Application;                                                                              
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import javafx.scene.layout.VBox;

public class SolverAppMain extends Application
{
    @Override
    public void start(Stage primaryStage) throws Exception
    {

        Parent root = FXMLLoader.load(getClass().getResource("Main.fxml"));
 
        Scene scene = new Scene(root,600,600);
        primaryStage.setTitle("JavaFX Scene Builder Example");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args)
    {
        launch(args);
    }
}

