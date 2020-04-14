package sample.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import sample.LoginModel;
;


import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class LoginController implements Initializable  {

    public LoginModel loginModel = new LoginModel();

    @FXML
    private Label isConnected;

    @FXML
    private TextField txtUsername;

    @FXML
    private TextField txtPassword;

    public void initialize(URL location, ResourceBundle resources ){
        if(loginModel.isDbConnected()){
            isConnected.setText("Connected!");
        } else {
            isConnected.setText("NOT  Connected!");
        }
    }


    public void Login (ActionEvent event){
        try {
            if(loginModel.isLogin(txtUsername.getText(), txtPassword.getText())) {
                isConnected.setText("username and password is correct!");

                ((Node)event.getSource()).getScene().getWindow().hide();

                Stage primaryStage = new Stage();
                FXMLLoader loader = new FXMLLoader();

                Pane root = null;

                try {
                    root = loader.load(getClass().getResource("/sample/views/User.fxml").openStream());
                } catch (IOException e) {
                    e.printStackTrace();
                }

                UserController userController = (UserController)loader.getController();
                userController.getUser(txtUsername.getText());

                Scene scene = new Scene(root);

                primaryStage.setScene(scene);
                primaryStage.show();



            } else {
                isConnected.setText("username or password is not correct!");
            }

        } catch (SQLException e) {
            isConnected.setText("username or password is not correct!");
            e.printStackTrace();
        }
    }


}
