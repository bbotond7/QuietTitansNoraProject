package hu.unideb.inf;

import hu.unideb.inf.dao.ContactDAO;
import hu.unideb.inf.dao.ContactsDAOImpl;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;


public class MainApp extends Application {

    @Override
    public void start(Stage stage) throws Exception {

        ContactDAO dao = new ContactsDAOImpl();
        dao.findAll().forEach(System.out::println);

        var scene = new Scene(new StackPane(new Label("sffsg")), 640, 480);
        stage.setScene(scene);
        stage.show();

        /* ez volt benne eredetileg
        FXMLLoader loader = new FXMLLoader(MainApp.class.getResource("/fxml/FXMLStudentsScene.fxml"));
        Scene scene = new Scene(loader.load());
        stage.setTitle("Students Register");
        stage.setScene(scene);
        stage.show();*/

        /*Parent root = FXMLLoader.load(getClass().getResource("/fxml/Scene.fxml"));
        
        Scene scene = new Scene(root);
        scene.getStylesheets().add("/styles/Styles.css");
        
        stage.setTitle("JavaFX and Maven");
        stage.setScene(scene);
        stage.show();*/
    }

    /**
     * The main() method is ignored in correctly deployed JavaFX application.
     * main() serves only as fallback in case the application can not be
     * launched through deployment artifacts, e.g., in IDEs with limited FX
     * support. NetBeans ignores main().
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
