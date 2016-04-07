package techreborn.manual.designer.windows;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.MenuBar;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import techreborn.manual.designer.ManualCatergories;
import techreborn.manual.designer.fileUtils.LoadSystem;
import techreborn.manual.designer.fileUtils.SaveSystem;
import techreborn.manual.saveFormat.Entry;

import java.awt.*;
import java.util.ArrayList;
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

    public void newItem(Event event) {
        if(treeList.getSelectionModel().getSelectedItem() instanceof TreeItem){
            TreeItem item = (TreeItem) treeList.getSelectionModel().getSelectedItem();
            if(item == ManualCatergories.blocks || item == ManualCatergories.items){
                TextInputDialog dialog = new TextInputDialog("");
                dialog.setTitle("Name?");
                dialog.setContentText("Name of item:");
                Optional<String> result = dialog.showAndWait();
                if(result.isPresent()){
                    TreeItem<String> newItem = new TreeItem<>(result.get());
                    Entry entry = new Entry();
                    entry.name = result.get();
                    ArrayList<String> choices = new ArrayList<>();
                        choices.add("Text");
                        choices.add("Image");
                        choices.add("Recipe");

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
                    item.getChildren().add(newItem);
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
        LoadSystem.load();
    }

    public void close(ActionEvent actionEvent) {
        //TODO checks to make sure its being saved
        System.exit(0);
    }
}
