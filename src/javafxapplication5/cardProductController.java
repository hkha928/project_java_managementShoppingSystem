/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package javafxapplication5;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

/**
 *
 * @author hkha9
 */
public class cardProductController implements Initializable {

    @FXML
    private AnchorPane card_form;

    @FXML
    private Label prod_name;

    @FXML
    private Label prod_price;

    @FXML
    private ImageView prod_imageView;

    @FXML
    private Spinner<Integer> prod_spinner;

    @FXML
    private Button prod_addBtn;

    private productData prodData;

    private Image image;

    private String prodID;
    private String size;
    private String prod_date;
    private String prod_image;

    private SpinnerValueFactory<Integer> spin;

    private final MyConnection con = new MyConnection();
    private PreparedStatement ps;
    private ResultSet rs;

    private Alert alert;

    public void setData(productData prodData) {
        this.prodData = prodData;
        prodID = prodData.getProductId();
        size = prodData.getSize();
        prod_image = prodData.getImage();
        prod_date = String.valueOf(prodData.getDate());
        prod_name.setText(prodData.getProductName());
        prod_price.setText(String.valueOf(prodData.getPrice()) + "VND");
        String path = "File:" + prodData.getImage();
        image = new Image(path, 200, 94, false, true);
        prod_imageView.setImage(image);
        price = prodData.getPrice();
    }

    private int qty;

    public void setQuantity() {
        spin = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 100, 0);
        prod_spinner.setValueFactory(spin);
    }
    private double totalPrice;
    private double price;

    public void addBtn() {
        mainFormController mForm = new mainFormController();
        mForm.customerID();

        qty = prod_spinner.getValue();
        String checkStatus = "";
        String queryCheckStatus = "select status from PRODUCT where prod_id='" + prodID + "'";

        try {
            int checkStock = 0;
            String queryCheckStock = "select stock from PRODUCT where prod_id='" + prodID + "'";

            ps = con.getConnection().prepareStatement(queryCheckStock);
            rs = ps.executeQuery();

            if (rs.next()) {
                checkStock = rs.getInt("stock");
            }
            
            String prodName = prod_name.getText();
            if (checkStock == 0) {

                String queryUpdateStock = "update PRODUCT set prod_name='" + prodName 
                        + "', size='" + size
                        + "', stock=0, price=" + price
                        + ", status='Out of stock', image='" + prod_image
                        + "', date='" + prod_date + "' where prod_id='" + prodID + "'";
                ps = con.getConnection().prepareStatement(queryUpdateStock);
                ps.executeUpdate();
            }

            ps = con.getConnection().prepareStatement(queryCheckStatus);
            rs = ps.executeQuery();

            if (rs.next()) {
                checkStatus = rs.getString("status");
            }
            if (!checkStatus.equals("In stock") || qty == 0) {
                alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Something went wrong. Please try again");
                alert.showAndWait();
            } else {
                if (checkStock < qty) {
                    alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Error Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Not enough quantity product you need!");
                    alert.showAndWait();
                } else {
                    String customerID = String.valueOf(data.customerID);

                    String quantity = String.valueOf(qty);

                    totalPrice = (qty * price);
                    String totalP = String.valueOf(totalPrice);
                    
                    prod_image = prod_image.replace("\\", "\\\\");

                    java.util.Date currentDate = new java.util.Date();
                    java.sql.Date sqlDate = new java.sql.Date(currentDate.getTime());
                    String date = String.valueOf(sqlDate);

                    String empUsername = data.username;

                    String query = "insert into CUSTOMER(customer_id, prod_id, prod_name, size, quantity, price, image, em_username, date) values(?,?,?,?,?,?,?,?,?)";
                    ps = con.getConnection().prepareStatement(query);
                    ps.setString(1, customerID);
                    ps.setString(2, prodID);
                    ps.setString(3, prodName);
                    ps.setString(4, size);
                    ps.setString(5, quantity);
                    ps.setString(6, totalP);
                    ps.setString(7, prod_image);
                    ps.setString(8, empUsername);
                    ps.setString(9, date);

                    ps.executeUpdate();
                    
                    int updateStock = checkStock - qty;

                    
                    String queryUpdateStock = "update PRODUCT set prod_name='" + prodName
                            + "', size='" + size
                            + "', stock=" + updateStock
                            + ", price=" + price
                            + ", status='" + checkStatus
                            + "', image='" + prod_image
                            + "', date='" + prod_date + "' where prod_id='" + prodID + "'";

                    ps = con.getConnection().prepareStatement(queryUpdateStock);
                    ps.executeUpdate();

                    alert = new Alert(AlertType.INFORMATION);
                    alert.setTitle("Info Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Successfully added Order!");
                    alert.showAndWait();
                    
                    mForm.menuGetTotal();
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setQuantity();
    }

}
