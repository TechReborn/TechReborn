package techreborn.manual.designer.windows;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import techreborn.manual.designer.ManualCatergories;
import techreborn.manual.designer.fileUtils.LoadSystem;
import techreborn.manual.designer.fileUtils.SaveSystem;
import techreborn.manual.saveFormat.Entry;
import techreborn.manual.saveFormat.EntryData;

import java.awt.*;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;

/**
 * Created by Mark on 05/04/2016.
 */
public class MainWindowController {
    public MenuBar menuBar;
    public SplitPane splitPane;
    public TreeView treeList;
    public ImageView image;
    public AnchorPane renderPane;
    public Button buttonNew;
    public TextArea textInput;
    public Button buttonDelete;
    public TextField nameTextArea;
    public TextField imageTextArea;
    public ImageView pageimage;
    public Label infoLabel;

    public void newItem(Event event) {
        if(treeList.getSelectionModel().getSelectedItem() instanceof TreeItem){
            TreeItem item = (TreeItem) treeList.getSelectionModel().getSelectedItem();
            if(item == ManualCatergories.blocks || item == ManualCatergories.items){
                TextInputDialog dialog = new TextInputDialog("");
                dialog.setTitle("Name?");
                dialog.setContentText("Name of item:");
                Optional<String> result = dialog.showAndWait();
                if(result.isPresent() && !result.get().isEmpty()){
                    TreeItem<String> newItem = new TreeItem<>(result.get());
                    Entry entry = new Entry();
                    entry.name = result.get();
                    ArrayList<String> choices = new ArrayList<>();
                        choices.add("Text");
                        choices.add("Image");

                    ChoiceDialog<String> choiceDialog = new ChoiceDialog<>("Text", choices);
                    choiceDialog.setTitle("Type?");
                    Optional<String> choiceResult = choiceDialog.showAndWait();
                    if(choiceResult.isPresent()){
                        System.out.println(choiceResult.get());
                        entry.type = choiceResult.get();
                    } else {
                        Toolkit.getDefaultToolkit().beep();
                        return;
                    }
                    entry.category = (String) item.getValue();
                    item.getChildren().add(newItem);
                    item.setExpanded(true);
                    SaveSystem.entries.put(newItem, entry);
                } else {
                    Toolkit.getDefaultToolkit().beep();
                }
            } else {
                Toolkit.getDefaultToolkit().beep();
            }
        }
    }

    public void deleteItem(Event event) {
        if(treeList.getSelectionModel().getSelectedItem() instanceof TreeItem){
            TreeItem item = (TreeItem) treeList.getSelectionModel().getSelectedItem();
            TreeItem parent = item.getParent();
            if(parent != null){
                if(parent == ManualCatergories.blocks || parent == ManualCatergories.items){
                    parent.getChildren().remove(item);
                } else {
                    Toolkit.getDefaultToolkit().beep();
                }
            }

        }
    }

    public void save(ActionEvent actionEvent) {
        SaveSystem.export();
    }

    public void open(ActionEvent actionEvent) {
        try {
            LoadSystem.load();
        } catch (FileNotFoundException e) {
            System.out.println("bad things happened");
            e.printStackTrace();
        }
    }

    public void close(ActionEvent actionEvent) {
        //TODO checks to make sure its being saved
        System.exit(0);
    }

    public void load(){
        textInput.textProperty().addListener((observable, oldValue, newValue) -> {
            textAreaChanged();
        });
        nameTextArea.textProperty().addListener((observable, oldValue, newValue) -> {
            nameChange();
        });
        imageTextArea.textProperty().addListener((observable, oldValue, newValue) -> {
            imageAreaChanged();
        });
    }



    public void textAreaChanged() {
        if(!treeList.getSelectionModel().isEmpty()){
            if(SaveSystem.entries.containsKey(treeList.getSelectionModel().getSelectedItem())){
                Entry entry = SaveSystem.entries.get(treeList.getSelectionModel().getSelectedItem());
                if(entry != null){
                    if(entry.data == null){
                        entry.data = new EntryData();
                    }
                    if(entry.data.data == null){
                        entry.data.data = new HashMap<>();
                    }
                    if(entry.data.data.containsKey("text")){
                        entry.data.data.replace("text", textInput.getText());
                    } else {
                        entry.data.data.put("text", textInput.getText());
                    }
                }
                SaveSystem.entries.replace((TreeItem) treeList.getSelectionModel().getSelectedItem(), entry);
            }
        }
    }

    public void imageAreaChanged() {
        if(!treeList.getSelectionModel().isEmpty()){
            if(SaveSystem.entries.containsKey(treeList.getSelectionModel().getSelectedItem())){
                Entry entry = SaveSystem.entries.get(treeList.getSelectionModel().getSelectedItem());
                if(entry != null){
                    if(entry.data == null){
                        entry.data = new EntryData();
                    }
                    if(entry.data.data == null){
                        entry.data.data = new HashMap<>();
                    }
                    if(entry.data.data.containsKey("image")){
                        entry.data.data.replace("image", imageTextArea.getText());
                    } else {
                        entry.data.data.put("image", imageTextArea.getText());
                    }
                }
                SaveSystem.entries.replace((TreeItem) treeList.getSelectionModel().getSelectedItem(), entry);
            }
        }
    }

    public void nameChange() {
        if(!treeList.getSelectionModel().isEmpty()){
            if(SaveSystem.entries.containsKey(treeList.getSelectionModel().getSelectedItem())){
                Entry entry = SaveSystem.entries.get(treeList.getSelectionModel().getSelectedItem());
                if(entry != null){
                    entry.registryName = nameTextArea.getText();
                    SaveSystem.entries.replace((TreeItem) treeList.getSelectionModel().getSelectedItem(), entry);
                }
            }
        }
    }
}
