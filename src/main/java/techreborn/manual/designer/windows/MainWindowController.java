package techreborn.manual.designer.windows;

import javafx.event.Event;
import javafx.scene.control.Button;
import javafx.scene.control.MenuBar;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

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

    public void newItem(Event event) {
        if(treeList.getSelectionModel().getSelectedItem() instanceof TreeItem){
            TreeItem item = (TreeItem) treeList.getSelectionModel().getSelectedItem();
            item.getChildren().add(new TreeItem<String>("hi"));
            item.setExpanded(true);
        }

    }
}
