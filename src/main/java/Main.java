import algorithm.Brucker;
import controller.LoadData;
import controller.MainController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.Task;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("LayoutFXML/enter_page.fxml"));
        Parent root = loader.load();
        primaryStage.setTitle("Brucker's Algorithm");
        primaryStage.setScene(new Scene(root));
        Task rootNode = LoadData.loadFromFile();
        MainController controller = loader.getController();
        controller.setRoot(rootNode);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
