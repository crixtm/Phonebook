/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package phonebook;

import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.FileOutputStream;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

/**
 *
 * @author crix
 */
public class ViewController implements Initializable {
    
    @FXML
    TableView table;
    @FXML
    TextField inputLastName;
    @FXML
    TextField inputFirstName;
    @FXML
    TextField inputEmail;
    @FXML
    Button saveButton;
    @FXML
    StackPane menuPane;
    @FXML
    Pane contactPane;
    @FXML
    Pane exportPane;
    @FXML
    TextField exportFileNameInput;
    @FXML
    Button exportButton;
    
    private static final String MENU_CONTACTS = "Contacts";
    private static final String MENU_EXIT = "Exit";
    private static final String MENU_EXPORT = "Export";
    private static final String MENU_LIST = "List";
    
    private final ObservableList<Person> data
            = FXCollections.observableArrayList(
                    new Person("Gipsz", "Jakab", "jakab@gipsz.com"),
                    new Person("Gipsz2", "Jakab2", "jakab2@gipsz.com"),
                    new Person("Gipsz3", "Jakab3", "jakab3@gipsz.com"));
    
    private void setTableData() {
        TableColumn lastNameColumn = new TableColumn("LastName");
        lastNameColumn.setMinWidth(100);
        lastNameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        lastNameColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        
        lastNameColumn.setOnEditCommit(
                new EventHandler<TableColumn.CellEditEvent<Person, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<Person, String> t) {
                ((Person) t.getTableView().getItems().get(
                        t.getTablePosition().getRow())).setLastName(t.getNewValue());
            }
        });
        
        TableColumn firstNameColumn = new TableColumn("FirstName");
        firstNameColumn.setMinWidth(100);
        firstNameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        firstNameColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        
        firstNameColumn.setOnEditCommit(
                new EventHandler<TableColumn.CellEditEvent<Person, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<Person, String> t) {
                ((Person) t.getTableView().getItems().get(
                        t.getTablePosition().getRow())).setFirstName(t.getNewValue());
            }
        });
        
        TableColumn emailColumn = new TableColumn("Email");
        emailColumn.setMinWidth(200);
        emailColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        emailColumn.setOnEditCommit(
                new EventHandler<TableColumn.CellEditEvent<Person, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<Person, String> t) {
                ((Person) t.getTableView().getItems().get(
                        t.getTablePosition().getRow())).setEmail(t.getNewValue());
                
                table.getColumns().addAll(lastNameColumn, firstNameColumn, emailColumn);
                table.setItems(data);
            }
            
        });
        table.getColumns().addAll(firstNameColumn, lastNameColumn, emailColumn);
        table.setItems(data);
    }
    
    private void setMenuData() {
        
        TreeItem<String> treeItemRoot1 = new TreeItem<>("Menu");
        TreeView<String> treeView = new TreeView<>(treeItemRoot1);
        treeView.setShowRoot(false);
        TreeItem<String> contactsNode = new TreeItem<>(MENU_CONTACTS);
        contactsNode.setExpanded(true);
        TreeItem<String> exitNode = new TreeItem<>(MENU_EXIT);
        //this block should add images to menu
        Node contactImageNode = new ImageView(new Image(getClass().getResourceAsStream("/contacts.png")));
        Node exportImageNode = new ImageView(new Image(getClass().getResourceAsStream("/export.png")));
        TreeItem<String> listContactsNode = new TreeItem<>(MENU_LIST, contactImageNode);
        TreeItem<String> exportContactsNode = new TreeItem<>(MENU_EXPORT, exportImageNode);
        //but nothing happen
        contactsNode.getChildren().addAll(listContactsNode, exportContactsNode);
        treeItemRoot1.getChildren().addAll(contactsNode, exitNode);
        menuPane.getChildren().add(treeView);
        treeView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                TreeItem<String> selectedItem = (TreeItem<String>) newValue;
                String selectedMenu;
                selectedMenu = selectedItem.getValue();
                if (null != selectedMenu) {
                    switch (selectedMenu) {
                        case MENU_CONTACTS: {
                            try {
                                if (selectedItem.isExpanded()) {
                                    selectedItem.setExpanded(false);
                                } else {
                                    selectedItem.setExpanded(true);
                                }
                                //selectedItem.setExpanded(!selectedItem.isExpanded());
                            } catch (Exception e) {
                            }
                            break;
                        }
                        case MENU_LIST: {
                            exportPane.setVisible(false);
                            contactPane.setVisible(true);
                            break;
                        }
                        case MENU_EXPORT: {
                            exportPane.setVisible(true);
                            contactPane.setVisible(false);
                            break;
                        }
                        case MENU_EXIT: {
                            System.exit(0);
                            break;
                        }
                        
                    }
                }
            }
            
        });
        
    }

    @FXML
    private void addContact(ActionEvent event) {
        String email = inputEmail.getText();
        if (email.contains("@") && email.length() > 3 && email.contains(".")) {
            data.add(new Person(inputLastName.getText(), inputFirstName.getText(), inputEmail.getText()));
            inputLastName.clear();
            inputFirstName.clear();
            inputEmail.clear();
        } else {
            
        }
        
    }
    
    @FXML
    private void exportPDF(ActionEvent event) {
        PDFGenerator generator = new PDFGenerator();
        boolean success = generator.export(exportFileNameInput.getText(), collectContacts());
        
        if (success) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "PDF exported", ButtonType.OK);            
            alert.showAndWait();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR, "PDF export failed, please see log for details.", ButtonType.OK);
            alert.showAndWait();
        }
    }
    
    final private String LB = "\n";
    
    private String collectContacts() {
        StringBuilder sb = new StringBuilder("Contacts" + LB);
        data.stream().forEach(person -> {
            sb.append(person.toString());
            sb.append(LB);
        });
        
        return sb.toString();
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setTableData();
        setMenuData();       
    }

    
}
