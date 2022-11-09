import algorithm.Brucker;
import controller.LoadData;
import controller.MainController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import model.Task;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.Objects;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Task rootNode = null;
        try {
            rootNode = LoadData.loadFromFile();
        } catch (ParseException e)  {
            System.err.println("Problem with parse file. Please check JSON format");
            System.err.println("Please insert data manually");
        } catch (IOException e){
            System.err.println("Problem with access to file. Check file location");
            System.err.println("Please insert data manually");
        }
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("LayoutFXML/enter_page.fxml"));
        Parent root = loader.load();
        primaryStage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("icon_clock.png"))));
        primaryStage.setTitle("Brucker's Algorithm");
        primaryStage.setScene(new Scene(root));
        MainController controller = loader.getController();
        controller.setRoot(rootNode);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
