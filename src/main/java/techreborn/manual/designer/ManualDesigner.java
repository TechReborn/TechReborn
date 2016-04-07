package techreborn.manual.designer;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TreeItem;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import techreborn.manual.designer.windows.MainWindowController;

import java.net.URL;

/**
 * Created by Mark on 05/04/2016.
 */
public class ManualDesigner extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        URL fxmlUrl = classLoader.getResource("assets/techreborn/designer/mainWindow.fxml");

        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(fxmlUrl);
        fxmlLoader.setBuilderFactory(new JavaFXBuilderFactory());

        Parent root = fxmlLoader.load(fxmlUrl.openStream());
        Scene scene = new Scene(root,900, 550);
        primaryStage.setTitle("TechReborn Manual Designer");
        primaryStage.setScene(scene);
        primaryStage.show();
        MainWindowController controller = fxmlLoader.getController();

        ManualCatergories.contents = new TreeItem<>("Contents");
        ManualCatergories.contents.setExpanded(true);

        ManualCatergories.blocks = new TreeItem<>("Blocks");

        ManualCatergories.contents.getChildren().add(ManualCatergories.blocks);

        ManualCatergories.items = new TreeItem<>("Items");
        ManualCatergories.contents.getChildren().add(ManualCatergories.items);

        controller.treeList.setRoot(ManualCatergories.contents);

        controller.treeList.getSelectionModel().selectedItemProperty().addListener( new ChangeListener() {

            @Override
            public void changed(ObservableValue observable, Object oldValue,
                                Object newValue) {
                TreeItem<String> selectedItem = (TreeItem<String>) newValue;
                //TODO things if needed
            }
        });

        controller.image.setImage(new Image("assets/techreborn/textures/manual/gui/manual.png"));
        controller.image.setPreserveRatio(true);
        controller.image.setSmooth(true);
        controller.image.setCache(true);
        controller.image.setFitHeight(1000);
        controller.image.setFitWidth(1000);
        controller.image.fitWidthProperty().bind(controller.renderPane.widthProperty());
        controller.image.fitHeightProperty().bind(controller.renderPane.heightProperty());
    }
}
