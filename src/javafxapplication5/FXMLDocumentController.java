/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXML2.java to edit this template
 */
package javafxapplication5;

import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.animation.TranslateTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;
import java.util.Date;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author hkha9
 */
public class FXMLDocumentController implements Initializable {

    @FXML
    private AnchorPane si_loginForm;

    @FXML
    private TextField si_username;

    @FXML
    private PasswordField si_password;

    @FXML
    private Hyperlink si_forgotPass;

    @FXML
    private Button si_loginBtn;

    @FXML
    private AnchorPane su_signupForm;

    @FXML
    private TextField su_username;

    @FXML
    private PasswordField su_password;

    @FXML
    private PasswordField su_confirmPass;

    @FXML
    private ComboBox<?> su_question;

    @FXML
    private TextField su_answer;

    @FXML
    private Button su_signupBtn;

    @FXML
    private AnchorPane fp_questionForm;

    @FXML
    private ComboBox<?> fp_question;

    @FXML
    private TextField fp_answer;

    @FXML
    private Button fp_proceedBtn;

    @FXML
    private Button fp_backBtn;

    @FXML
    private TextField fp_username;

    @FXML
    private AnchorPane np_newPassForm;

    @FXML
    private PasswordField np_newPassword;

    @FXML
    private PasswordField np_confirmPassword;

    @FXML
    private Button np_changePassBtn;

    @FXML
    private Button np_backBtn;

    @FXML
    private AnchorPane side_form;

    @FXML
    private Button side_createBtn;

    @FXML
    private Button side_alreadyHave;

    private final MyConnection con = new MyConnection();
    private PreparedStatement ps;
    private ResultSet rs;

    private Alert alert;

    public void registerGetClear() {
        su_username.setText("");
        su_password.setText("");
        su_confirmPass.setText("");
        su_question.getSelectionModel().clearSelection();
        su_answer.setText("");
    }

    public void forgotPassGetClear() {
        fp_username.setText("");
        fp_question.getSelectionModel().clearSelection();
        fp_answer.setText("");
        np_newPassword.setText("");
        np_confirmPassword.setText("");
    }

    public void loginBtn() {
        if (si_username.getText().isEmpty()
                || si_password.getText().isEmpty()) {
            alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Please fill all blank fields");
            alert.showAndWait();
        } else {
            String username = si_username.getText();
            String password = String.valueOf(si_password.getText());
            String querySelect = "select username, password from EMPLOYEE where username=? and password=?";
            try {
                ps = con.getConnection().prepareStatement(querySelect);
                ps.setString(1, username);
                ps.setString(2, password);

                rs = ps.executeQuery();

                if (rs.next()) {
                    // lấy tên username đang sử dụng
                    data.username = username;

                    // nếu username và password đúng sẽ chuyển sang trang chính
                    alert = new Alert(AlertType.INFORMATION);
                    alert.setTitle("Info Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Successfully Login!");
                    alert.showAndWait();

                    Parent root = FXMLLoader.load(getClass().getResource("mainForm.fxml"));

                    Stage stage = new Stage();
                    Scene scene = new Scene(root);

                    stage.setTitle("Clothing Management System");
                    stage.setMinWidth(1100);
                    stage.setMinHeight(600);

                    stage.setScene(scene);
                    stage.show();

                    si_loginBtn.getScene().getWindow().hide();
                } else {
                    alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Error Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Incorrect username or password");
                    alert.showAndWait();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public void registerBtn() {
        // kiểm tra có bỏ trống field không
        if (su_username.getText().isEmpty()
                || su_password.getText().isEmpty()
                || su_confirmPass.getText().isEmpty()
                || su_question.getSelectionModel().getSelectedItem() == null
                || su_answer.getText().isEmpty()) {
            alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Please fill all blank fields");
            alert.showAndWait();
        } else {
            String username = su_username.getText();
            String password = String.valueOf(su_password.getText());
            String rePassword = String.valueOf(su_confirmPass.getText());
            String question = (String) su_question.getSelectionModel().getSelectedItem();
            String answer = su_answer.getText();

            Date currentDate = new Date();
            java.sql.Date sqlDate = new java.sql.Date(currentDate.getTime());
            String date = String.valueOf(sqlDate);

            String query = "insert into EMPLOYEE(username, password, question, answer, date) values(?,?,?,?,?)";

            try {
                // kiểm tra đã có username tồn tại
                String queryCheckUsername = "select username from EMPLOYEE where username='" + username + "'";
                ps = con.getConnection().prepareStatement(queryCheckUsername);
                rs = ps.executeQuery();

                if (rs.next()) {
                    alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Error Message");
                    alert.setHeaderText(null);
                    alert.setContentText(username + " is already existed");
                    alert.showAndWait();
                } else if (su_password.getText().length() < 3) { // kiểm tra mật khẩu không được ít hơn 3 ký tự
                    alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Error Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Password must be at least 3 characters long");
                    alert.showAndWait();
                } else {
                    ps = con.getConnection().prepareStatement(query);
                    ps.setString(1, username);
                    ps.setString(2, password);
                    ps.setString(3, question);
                    ps.setString(4, answer);
                    ps.setString(5, date);
                    //kiểm tra pass giống với confirm pass
                    if (password.equals(rePassword)) {
                        ps.executeUpdate();

                        alert = new Alert(AlertType.INFORMATION);
                        alert.setTitle("Info Message");
                        alert.setHeaderText(null);
                        alert.setContentText("Successfully registered Account!");
                        alert.showAndWait();

                        registerGetClear();

                        TranslateTransition slider = new TranslateTransition();

                        slider.setNode(side_form);
                        slider.setToX(0);
                        slider.setDuration(Duration.seconds(.5));

                        slider.setOnFinished((ActionEvent e) -> {
                            side_alreadyHave.setVisible(false);
                            side_createBtn.setVisible(true);
                        });
                        slider.play();
                    } else {
                        alert = new Alert(AlertType.ERROR);
                        alert.setTitle("Error Message");
                        alert.setHeaderText(null);
                        alert.setContentText("Password doesnot match RePassword");
                        alert.showAndWait();
                    }
                }

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    private String[] questionList = {"What is your favorite color?", "What is your favorite food?", "What is your date of birth?"};

    public void registerQuestionList() {
        List<String> listQuestion = new ArrayList<>();

        if (questionList != null) {
            for (String data : questionList) {
                listQuestion.add(data);
            }
        }
        //ObservableList thường là để cập nhật giao diện người dùng tự động khi dữ liệu cơ bản thay đổi.
        ObservableList listData = FXCollections.observableArrayList(listQuestion);

        su_question.setItems(listData);
    }

    // chuyển sang form quên mật khẩu
    public void switchForgotPassword() {
        fp_questionForm.setVisible(true);
        si_loginForm.setVisible(false);

        forgotPassQuestionList();
    }

    public void proceedBtn() {
        if (fp_username.getText().isEmpty()
                || fp_question.getSelectionModel().getSelectedItem() == null
                || fp_answer.getText().isEmpty()) {
            alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Please fill all blank fields");
            alert.showAndWait();
        } else {
            String username = fp_username.getText();
            String question = (String) fp_question.getSelectionModel().getSelectedItem();
            String answer = fp_answer.getText();
            String querySelect = "select username, question, answer from EMPLOYEE where username=? and question=? and answer=?";
            try {
                ps = con.getConnection().prepareStatement(querySelect);
                ps.setString(1, username);
                ps.setString(2, question);
                ps.setString(3, answer);

                rs = ps.executeQuery();

                if (rs.next()) {
                    np_newPassForm.setVisible(true);
                    fp_questionForm.setVisible(false);
                } else {
                    alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Error Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Incorrect Info");
                    alert.showAndWait();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public void changePassBtn() {
        if (np_newPassword.getText().isEmpty()
                || np_confirmPassword.getText().isEmpty()) {
            alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Please fill all blank fields");
            alert.showAndWait();
        } else {
            String username = fp_username.getText();
            String newPass = np_newPassword.getText();
            String confirmPass = np_confirmPassword.getText();
            String query = "update EMPLOYEE set password='" + newPass + "' where username='" + username + "'";
            try {
                ps = con.getConnection().prepareStatement(query);
                if (newPass.equals(confirmPass)) {
                    if (newPass.length() < 3) { // kiểm tra mật khẩu không được ít hơn 3 ký tự
                        alert = new Alert(AlertType.ERROR);
                        alert.setTitle("Error Message");
                        alert.setHeaderText(null);
                        alert.setContentText("Password must be at least 3 characters long");
                        alert.showAndWait();
                    } else {
                        ps.executeUpdate();

                        alert = new Alert(AlertType.INFORMATION);
                        alert.setTitle("Info Message");
                        alert.setHeaderText(null);
                        alert.setContentText("Successfully changed Password!");
                        alert.showAndWait();

                        si_loginForm.setVisible(true);
                        np_newPassForm.setVisible(false);

                        forgotPassGetClear();
                    }
                } else {
                    alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Error Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Password doesnot match RePassword");
                    alert.showAndWait();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public void forgotPassQuestionList() {
        List<String> listQuestion = new ArrayList<>();

        if (questionList != null) {
            for (String data : questionList) {
                listQuestion.add(data);
            }
        }
        //ObservableList thường là để cập nhật giao diện người dùng tự động khi dữ liệu cơ bản thay đổi.
        ObservableList listData = FXCollections.observableArrayList(listQuestion);

        fp_question.setItems(listData);
    }

    public void backToLoginForm() {
        si_loginForm.setVisible(true);
        fp_questionForm.setVisible(false);
    }

    public void backToQuestionForm() {
        fp_questionForm.setVisible(true);
        np_newPassForm.setVisible(false);
    }

    // đổi form đăng nhập sang form đăng ký
    public void switchForm(ActionEvent event) {

        TranslateTransition slider = new TranslateTransition();

        if (event.getSource() == side_createBtn) {
            slider.setNode(side_form);
            slider.setToX(300);
            slider.setDuration(Duration.seconds(.5));

            slider.setOnFinished((ActionEvent e) -> {
                side_alreadyHave.setVisible(true);
                side_createBtn.setVisible(false);

                fp_questionForm.setVisible(false);
                si_loginForm.setVisible(true);
                np_newPassForm.setVisible(false);

                fp_username.setText("");
                fp_question.getSelectionModel().clearSelection();
                fp_answer.setText("");

                registerQuestionList();
            });

            slider.play();
        } else if (event.getSource() == side_alreadyHave) {
            slider.setNode(side_form);
            slider.setToX(0);
            slider.setDuration(Duration.seconds(.5));

            slider.setOnFinished((ActionEvent e) -> {
                side_alreadyHave.setVisible(false);
                side_createBtn.setVisible(true);

                fp_questionForm.setVisible(false);
                si_loginForm.setVisible(true);
                np_newPassForm.setVisible(false);

                fp_username.setText("");
                fp_question.getSelectionModel().clearSelection();
                fp_answer.setText("");
            });
            slider.play();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }
}
