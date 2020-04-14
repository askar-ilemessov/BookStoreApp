package sample.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import sample.Dialogs.AlertDialog;
import sample.ModelTable;
import sample.SqliteConnection;

import java.io.IOException;
import java.net.URL;
import java.sql.*;

import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UserController implements Initializable {

    @FXML
    private Label userLbl;

    @FXML
    private TableView<ModelTable> table;
    @FXML
    private TableColumn<ModelTable, Integer> col_id;
    @FXML
    private TableColumn<ModelTable, String> col_title;
    @FXML
    private TableColumn<ModelTable, String> col_author;
    @FXML
    private TableColumn<ModelTable, String> col_genre;
    @FXML
    private TableColumn<ModelTable, String> col_isbn;
    @FXML
    private TableColumn<ModelTable, Integer> col_year;

    ObservableList<ModelTable> oblist = FXCollections.observableArrayList();





    @FXML
    private TextField txt_id;

    @FXML
    private TextField txt_title;

    @FXML
    private TextField txt_author;

    @FXML
    private TextField txt_genre;

    @FXML
    private TextField txt_isbn;

    @FXML
    private TextField txt_year;

    @FXML
    private Button button_addBook;

    @FXML
    private Button btn_updateBook;

    @FXML
    private Button btn_delete;




    @FXML
    private TextField txt_search;

    //Connection
    private Connection conn =null;
    private PreparedStatement pst = null;
    private PreparedStatement pst2 = null;

    private SqliteConnection dc;


    private ResultSet rs;




    @Override
    public void initialize(URL location, ResourceBundle resources) {

        dc = new SqliteConnection();
        loadTable();
        setCellValueFromTable();
        searchBook();
    }



    public void handleAddBook (ActionEvent event) throws SQLException {

        String query = "INSERT INTO books (book_id, title, author, genre,  ISBN, year) " +
                        "VALUES (?, ?, ?, ?, ?, ?)";

        String book_id = txt_id.getText();
        String title = txt_title.getText();
        String author = txt_author.getText();
        String genre = txt_genre.getText();
        String isbn = txt_isbn.getText();
        String year = txt_year.getText();

        try {
            conn = dc.Connector();

            pst = conn.prepareStatement(query);

/*          auto generated id

            pst = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            rs = pst.getGeneratedKeys();
            rs.next();
            Object key = rs.getObject(1);

            pst.setInt(1,Integer.parseInt(String.valueOf(key)));
*/

            pst.setInt(1,Integer.parseInt(String.valueOf(book_id)));
            pst.setString(2,title);
            pst.setString(3,author);
            pst.setString(4,genre);
            pst.setString(5,isbn);
            pst.setString(6,year);

            boolean b;
            if(txt_id.getText().matches("([0-9]+(\\[0-9]?)?)+")  && txt_year.getText().matches("([0-9]+(\\[0-9]?)?)+")){
                b = true;
            } else {
                b = false;
            }

            if (    b && txt_id.getText().length() != 0 && !txt_id.getText().isEmpty() &&
                    txt_title.getText().length() !=0 && !txt_title.getText().isEmpty() &&
                    txt_author.getText().length() !=0 && !txt_author.getText().isEmpty() ) {

                int i = pst.executeUpdate();



                if (i == 1){
                    AlertDialog.display("Info", "Data Inserted Successfully");
                    loadTable();
                    clearTextFields();
                }

            } else {
                AlertDialog.display("Error", "\t Data was not Inserted \n" + "\t Id, Title and Author should not be empty!" + "\n \t Id And Year fileds should be digits");
            }




        } catch (SQLException e) {
            Logger.getLogger(UserController.class.getName()).log(Level.SEVERE, null, e);
            e.printStackTrace();
        } finally {
            pst.close();
        }
    }


    public void handleUpdateBook(ActionEvent event) throws SQLException{
        //UPDATE books SET year = 2001 WHERE book_id = 223

        String query = "UPDATE books SET title = ? , author = ?, genre = ?, ISBN = ? , year = ?  WHERE book_id = ?";

        String book_id = txt_id.getText();
        String title = txt_title.getText();
        String author = txt_author.getText();
        String genre = txt_genre.getText();
        String isbn = txt_isbn.getText();
        String year = txt_year.getText();

        boolean b;
        if(txt_id.getText().length() != 0 && !txt_id.getText().isEmpty() &&
                txt_title.getText().length() !=0 && !txt_title.getText().isEmpty() &&
                txt_author.getText().length() !=0 && !txt_author.getText().isEmpty() ) {
            b = true;
        } else {
            b = false;
        }


        try {
            conn = dc.Connector();

            pst = conn.prepareStatement(query);

            pst.setString(1,title);
            pst.setString(2,author);
            pst.setString(3,genre);
            pst.setString(4,isbn);
            pst.setString(5,year);
            pst.setString(6,book_id);


            if (b){
                int i = pst.executeUpdate();
                if (i == 1){
                    AlertDialog.display("Info", "Data Updated Successfully");
                    loadTable();
                    clearTextFields();
                }
            } else{
                AlertDialog.display("Error", "\t Data was not Updated \n" + "\t Id, Title and Author should not be empty!" + "\n \t Id And Year fileds should be digits");

            }



        } catch (SQLException e) {
            Logger.getLogger(UserController.class.getName()).log(Level.SEVERE, null, e);
            e.printStackTrace();
        }


    }


    private void clearTextFields(){
        txt_id.clear();
        txt_title.clear();
        txt_author.clear();
        txt_genre.clear();
        txt_isbn.clear();
        txt_year.clear();
    }




    public void  handleDeleteButton(ActionEvent event) {

        String query = "DELETE FROM books  WHERE book_id = ?";


        try {
            conn = dc.Connector();
            pst = conn.prepareStatement(query);

            pst.setString(1, txt_id.getText());

            if(txt_id.getText().length() != 0 && !txt_id.getText().isEmpty()){
                int i = pst.executeUpdate();

                if (i == 1){
                    AlertDialog.display("Info", "Data Deleted Successfully");
                    loadTable();
                    clearTextFields();
                }
            } else {
                AlertDialog.display("Error", "\t Data was not Deleted \n" + "\t Id required to delete!");

            }









        } catch (SQLException e) {
            e.printStackTrace();
        }

    }



    private void searchBook(){
        txt_search.setOnKeyReleased(e -> {

            if(txt_search.getText() == ""){
                loadTable();
            }

            else {
                oblist.clear();

                String query = "Select * from books where title LIKE '%" + txt_search.getText() + "%' "
                        + "UNION Select * from books where author LIKE '%" + txt_search.getText() + "%' "
                        + "UNION Select * from books where genre LIKE '%" + txt_search.getText() + "%' "
                        + "UNION Select * from books where year LIKE '%" + txt_search.getText() + "%' "
                        + "UNION Select * from books where isbn LIKE '%" + txt_search.getText() + "%' ";

                conn = dc.Connector();


                try {
                    pst = conn.prepareStatement(query);

                    //pst.setString(1,txt_search.getText());

                    ResultSet rs;
                    rs = pst.executeQuery();


                    while (rs.next()) {
                        oblist.add(new ModelTable(rs.getString(1), rs.getString(2),
                                rs.getString(3), rs.getString(4),
                                rs.getString(5), rs.getString(6)));

                    }

                    table.setItems(oblist);


                } catch (SQLException e1) {
                    e1.printStackTrace();
                }

            }


        });
    }

    private void setCellValueFromTable(){
        table.setOnMouseClicked(e ->  {
            ModelTable modelTable = table.getItems().get(table.getSelectionModel().getSelectedIndex());
            txt_id.setText(modelTable.getId());
            txt_title.setText(modelTable.getTitle());
            txt_author.setText(modelTable.getAuthor());
            txt_genre.setText(modelTable.getGenre());
            txt_isbn.setText(modelTable.getIsbn());
            txt_year.setText(modelTable.getYear());

        });
    }


    public void getUser (String user) {
        userLbl.setText(user);
    }


    public void signOut (ActionEvent event) {

        try {

            ((Node)event.getSource()).getScene().getWindow().hide();

            Stage primaryStage = new Stage();
            FXMLLoader loader = new FXMLLoader();

            Pane root = null;
            root = loader.load(getClass().getResource("/sample/views/Login.fxml").openStream());

            Scene scene = new Scene(root);

            primaryStage.setScene(scene);
            primaryStage.show();


        } catch (IOException e) {

            e.printStackTrace();
        }
    }


    public void loadBtn (ActionEvent event) {

        String query = "select * from books";
        oblist.clear();

        try {
            Connection conn = dc.Connector();

            ResultSet rs = conn.createStatement().executeQuery(query);


            while(rs.next()){
                oblist.add(new ModelTable(rs.getString(1), rs.getString(2),
                        rs.getString(3), rs.getString(4),
                        rs.getString(5), rs.getString(6)));

            }

        } catch (SQLException e) {
            Logger.getLogger(UserController.class.getName()).log(Level.SEVERE, null, e);
            e.printStackTrace();
        }


        col_id.setCellValueFactory(new PropertyValueFactory<>("id"));
        col_title.setCellValueFactory(new PropertyValueFactory<>("title"));
        col_author.setCellValueFactory(new PropertyValueFactory<>("author"));
        col_genre.setCellValueFactory(new PropertyValueFactory<>("genre"));
        col_isbn.setCellValueFactory(new PropertyValueFactory<>("isbn"));
        col_year.setCellValueFactory(new PropertyValueFactory<>("year"));


        table.setItems(null);
        table.setItems(oblist);

    }


    private void loadTable () {

        String query = "select * from books";
        oblist.clear();

        try {
            Connection conn = dc.Connector();

            ResultSet rs = conn.createStatement().executeQuery(query);


            while(rs.next()){
                oblist.add(new ModelTable(rs.getString(1), rs.getString(2),
                        rs.getString(3), rs.getString(4),
                        rs.getString(5), rs.getString(6)));

            }

        } catch (SQLException e) {
            Logger.getLogger(UserController.class.getName()).log(Level.SEVERE, null, e);
            e.printStackTrace();
        }


        col_id.setCellValueFactory(new PropertyValueFactory<>("id"));
        col_title.setCellValueFactory(new PropertyValueFactory<>("title"));
        col_author.setCellValueFactory(new PropertyValueFactory<>("author"));
        col_genre.setCellValueFactory(new PropertyValueFactory<>("genre"));
        col_isbn.setCellValueFactory(new PropertyValueFactory<>("isbn"));
        col_year.setCellValueFactory(new PropertyValueFactory<>("year"));


        table.setItems(null);
        table.setItems(oblist);

    }






}
