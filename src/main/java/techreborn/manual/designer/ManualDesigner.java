package techreborn.manual.designer;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TreeItem;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import techreborn.manual.designer.fileUtils.SaveSystem;
import techreborn.manual.designer.windows.MainWindowController;
import techreborn.manual.saveFormat.Entry;

import java.io.File;
import java.net.URL;

/**
 * Created by Mark on 05/04/2016.
 */
public class ManualDesigner extends Application {

    public static Stage stage;

    public static void main(String[] args) {
            launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        stage = primaryStage;
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
                boolean isValid = !SaveSystem.entries.containsKey(selectedItem);
                controller.textInput.setDisable(isValid);
                controller.nameTextArea.setDisable(isValid);
                if(!isValid){
                    if(SaveSystem.entries.containsKey(selectedItem)){
                        Entry entry = SaveSystem.entries.get(selectedItem);
                        controller.pageimage.setVisible(false);
                        if(entry.data != null && entry.data.data != null){
                            if(entry.data.data.containsKey("text")){
                                controller.textInput.setText(entry.data.data.get("text"));
                            }
                            if(entry.data.data.containsKey("image")){
                                controller.imageTextArea.setText(entry.data.data.get("image"));
                                if(SaveSystem.lastSave != null){
                                    File imagesDir = new File(SaveSystem.lastSave, "images");
                                    if(imagesDir.exists()){
                                        File image = new File(imagesDir, entry.data.data.get("image") + ".png");
                                        if(image.exists()){
                                            controller.pageimage.setImage(new Image(image.toURI().toString()));
                                            controller.pageimage.setVisible(true);
                                        }
                                    }
                                }
                            } else {
                                controller.imageTextArea.setText("");
                            }
                            //TODO Improve this
                        } else  {
                            controller.textInput.setText("");
                            controller.imageTextArea.setText("");
                        }
                        if(entry.type.equalsIgnoreCase("image")){
                            controller.imageTextArea.setDisable(false);
                        } else {
                            controller.imageTextArea.setDisable(true);
                        }
                        controller.infoLabel.setText("Settings: " + entry.type);
                        controller.nameTextArea.setText(entry.registryName);
                    }
                }

            }
        });

        controller.textInput.setDisable(false);
        controller.nameTextArea.setDisable(false);

        controller.image.setImage(new Image("assets/techreborn/textures/manual/gui/manual.png"));
        controller.image.setPreserveRatio(true);
        controller.image.setSmooth(true);
        controller.image.setCache(true);
        controller.image.setFitHeight(1000);
        controller.image.setFitWidth(1000);
        controller.image.fitWidthProperty().bind(controller.renderPane.widthProperty());
        controller.image.fitHeightProperty().bind(controller.renderPane.heightProperty());

        controller.pageimage.setPreserveRatio(true);
        controller.pageimage.setSmooth(true);
        controller.pageimage.setCache(true);
        controller.pageimage.setFitHeight(1000);
        controller.pageimage.setFitWidth(1000);
        controller.pageimage.fitWidthProperty().bind(controller.renderPane.widthProperty());
        controller.pageimage.fitHeightProperty().bind(controller.renderPane.heightProperty());

        controller.load();

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Info Dialog");
        alert.setHeaderText("This is not how to run the mod!");
        alert.setContentText("If you are trying to play with TechReborn in you minecraft world, this is NOT how you do it. You have the wrong jar file or you are doing something very wrong. Go and download the universal jar file and put it in the mods folder");
        alert.show();
    }
}
