/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package javafxapplication5;

import java.io.File;
import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Date;
import java.util.HashMap;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;

/**
 *
 * @author hkha9
 */
public class mainFormController implements Initializable {

    @FXML
    private AnchorPane main_form;

    @FXML
    private Label username;

    @FXML
    private Button dashboard_btn;

    @FXML
    private Button inventory_btn;

    @FXML
    private Button menu_btn;

    @FXML
    private Button customers_btn;

    @FXML
    private Button signout_btn;

    @FXML
    private AnchorPane dashboard_form;

    @FXML
    private AnchorPane inventory_form;

    @FXML
    private TableView<productData> inventory_tableView;

    @FXML
    private TableColumn<productData, String> inventory_col_productID;

    @FXML
    private TableColumn<productData, String> inventory_col_productName;

    @FXML
    private TableColumn<productData, String> inventory_col_size;

    @FXML
    private TableColumn<productData, String> inventory_col_stock;

    @FXML
    private TableColumn<productData, String> inventory_col_price;

    @FXML
    private TableColumn<productData, String> inventory_col_status;

    @FXML
    private TableColumn<productData, String> inventory_col_date;

    @FXML
    private TextField inventory_productID;

    @FXML
    private TextField inventory_productName;

    @FXML
    private ComboBox<?> inventory_size;

    @FXML
    private TextField inventory_stock;

    @FXML
    private TextField inventory_price;

    @FXML
    private ImageView inventory_imageView;

    @FXML
    private Button inventory_importBtn;

    @FXML
    private Button inventory_addBtn;

    @FXML
    private Button inventory_updateBtn;

    @FXML
    private Button inventory_clearBtn;

    @FXML
    private Button inventory_deleteBtn;

    @FXML
    private ComboBox<?> inventory_status;

    @FXML
    private AnchorPane menu_form;

    @FXML
    private ScrollPane menu_scrollPane;

    @FXML
    private GridPane menu_gridPane;

    @FXML
    private TableView<productData> menu_tableView;

    @FXML
    private TableColumn<productData, String> menu_col_productName;

    @FXML
    private TableColumn<productData, String> menu_col_quantity;

    @FXML
    private TableColumn<productData, String> menu_col_price;

    @FXML
    private Label menu_total;

    @FXML
    private TextField menu_amount;

    @FXML
    private Label menu_change;

    @FXML
    private Button menu_payBtn;

    @FXML
    private Button menu_removeBtn;

    @FXML
    private Button menu_receiptBtn;

    @FXML
    private AnchorPane customers_form;

    @FXML
    private TableView<customersData> customers_tableView;

    @FXML
    private TableColumn<customersData, String> customers_col_customerID;

    @FXML
    private TableColumn<customersData, String> customers_col_total;

    @FXML
    private TableColumn<customersData, String> customers_col_date;

    @FXML
    private TableColumn<customersData, String> customers_col_cashier;

    @FXML
    private Label dashboard_customers;

    @FXML
    private Label dashboard_todayIncome;

    @FXML
    private Label dashboard_totalIncome;

    @FXML
    private Label dashboard_soldProduct;

    @FXML
    private AreaChart<?, ?> dashboard_incomeChart;

    @FXML
    private BarChart<?, ?> dashboard_customerChart;

    private final MyConnection con = new MyConnection();
    private PreparedStatement ps;
    private ResultSet rs;

    private Image image;

    private Alert alert;

    private ObservableList<productData> cardListData = FXCollections.observableArrayList();

    public void dashboardDisplayNumberCustomers() {
        String querySelect = "select count(id) from RECEIPT";

        try {
            int numCustomers = 0;

            ps = con.getConnection().prepareStatement(querySelect);
            rs = ps.executeQuery();

            if (rs.next()) {
                numCustomers = rs.getInt("count(id)");
            }
            dashboard_customers.setText(String.valueOf(numCustomers));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void dashboardDisplayTodayIncome() {
        Date currentDate = new Date();
        java.sql.Date sqlDate = new java.sql.Date(currentDate.getTime());

        String querySelect = "select sum(total) from RECEIPT where date='" + sqlDate + "'";

        try {
            double todayIncome = 0;

            ps = con.getConnection().prepareStatement(querySelect);
            rs = ps.executeQuery();

            if (rs.next()) {
                todayIncome = rs.getDouble("sum(total)");
            }
            dashboard_todayIncome.setText(todayIncome + "VND");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void dashboardDisplayTotalIncome() {
        String querySelect = "select sum(total) from RECEIPT";

        try {
            double totalIncome = 0;

            ps = con.getConnection().prepareStatement(querySelect);
            rs = ps.executeQuery();

            if (rs.next()) {
                totalIncome = rs.getDouble("sum(total)");
            }
            dashboard_totalIncome.setText(totalIncome + "VND");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void dashboardDisplaySoldProduct() {
        String querySelect = "select count(quantity) from CUSTOMER";

        try {
            int quantity = 0;

            ps = con.getConnection().prepareStatement(querySelect);
            rs = ps.executeQuery();

            if (rs.next()) {
                quantity = rs.getInt("count(quantity)");
            }
            dashboard_soldProduct.setText(String.valueOf(quantity));
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public void dashboardIncomeChart() {
        dashboard_incomeChart.getData().clear();

        String querySelect = "select date, sum(total) from RECEIPT group by date order by timestamp(date)";

        XYChart.Series chart = new XYChart.Series();

        try {
            ps = con.getConnection().prepareStatement(querySelect);
            rs = ps.executeQuery();

            while (rs.next()) {
                chart.getData().add(new XYChart.Data<>(rs.getString(1), rs.getFloat(2)));
            }
            dashboard_incomeChart.getData().add(chart);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void dashboardCustomerChart() {
        dashboard_customerChart.getData().clear();

        String querySelect = "select date, count(id) from RECEIPT group by date order by timestamp(date)";

        XYChart.Series chart = new XYChart.Series();

        try {
            ps = con.getConnection().prepareStatement(querySelect);
            rs = ps.executeQuery();

            while (rs.next()) {
                chart.getData().add(new XYChart.Data<>(rs.getString(1), rs.getInt(2)));
            }
            dashboard_customerChart.getData().add(chart);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void inventoryAddBtn() {
        if (inventory_productID.getText().isEmpty()
                || inventory_productName.getText().isEmpty()
                || inventory_size.getSelectionModel().getSelectedItem() == null
                || inventory_stock.getText().isEmpty()
                || inventory_price.getText().isEmpty()
                || inventory_status.getSelectionModel().getSelectedItem() == null
                || data.path == null) {

            alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Please fill all blank fields");
            alert.showAndWait();
        } else {
            String prodID = inventory_productID.getText();
            String prodName = inventory_productName.getText();
            String size = (String) inventory_size.getSelectionModel().getSelectedItem();
            String stock = inventory_stock.getText();
            String price = inventory_price.getText();
            String status = (String) inventory_status.getSelectionModel().getSelectedItem();

            String path = data.path;
            path = path.replace("\\", "\\\\");

            Date currentDate = new Date();
            java.sql.Date sqlDate = new java.sql.Date(currentDate.getTime());
            String date = String.valueOf(sqlDate);

            // kiểm tra product id
            String queryCheckProdID = "select prod_id from PRODUCT where prod_id='" + prodID + "'";

            try {
                ps = con.getConnection().prepareStatement(queryCheckProdID);
                rs = ps.executeQuery();

                if (rs.next()) {
                    alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Error Message");
                    alert.setHeaderText(null);
                    alert.setContentText(prodID + " is already existed");
                    alert.showAndWait();
                } else {
                    String query = "insert into PRODUCT(prod_id, prod_name, size, stock, price, status, image, date) values(?,?,?,?,?,?,?,?)";

                    ps = con.getConnection().prepareStatement(query);
                    ps.setString(1, prodID);
                    ps.setString(2, prodName);
                    ps.setString(3, size);
                    ps.setString(4, stock);
                    ps.setString(5, price);
                    ps.setString(6, status);
                    ps.setString(7, path);
                    ps.setString(8, date);

                    ps.executeUpdate();

                    alert = new Alert(AlertType.INFORMATION);
                    alert.setTitle("Info Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Successfully added Product!");
                    alert.showAndWait();

                    inventoryShowData();
                    inventoryGetClearBtn();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public void inventoryUpdateBtn() {
        if (inventory_productID.getText().isEmpty()
                || inventory_productName.getText().isEmpty()
                || inventory_size.getSelectionModel().getSelectedItem() == null
                || inventory_stock.getText().isEmpty()
                || inventory_price.getText().isEmpty()
                || inventory_status.getSelectionModel().getSelectedItem() == null
                || data.path == null || data.id == 0) {

            alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Please fill all blank fields");
            alert.showAndWait();
        } else {
            String prodID = inventory_productID.getText();
            String prodName = inventory_productName.getText();
            String size = (String) inventory_size.getSelectionModel().getSelectedItem();
            String stock = inventory_stock.getText();
            String price = inventory_price.getText();
            String status = (String) inventory_status.getSelectionModel().getSelectedItem();

            String path = data.path;
            path = path.replace("\\", "\\\\");

            String query = "update PRODUCT set prod_id ='" + prodID
                    + "', prod_name='" + prodName
                    + "', size='" + size
                    + "', stock='" + stock
                    + "', price='" + price
                    + "', status='" + status
                    + "', image='" + path
                    + "', date='" + data.date + "' where id=" + data.id;
            try {
                alert = new Alert(AlertType.CONFIRMATION);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Are you sure you want to update Product ID: " + prodID + "?");
                Optional<ButtonType> option = alert.showAndWait();

                if (option.get().equals(ButtonType.OK)) {
                    ps = con.getConnection().prepareStatement(query);
                    ps.executeUpdate();

                    alert = new Alert(AlertType.INFORMATION);
                    alert.setTitle("Info Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Successfully updated Product!");
                    alert.showAndWait();

                    // cập nhật table view
                    inventoryShowData();
                    // xóa dữ liệu các ô input
                    inventoryGetClearBtn();
                } else {
                    alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Error Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Something went wrong. Please try again");
                    alert.showAndWait();
                }
            } catch (Exception ex) {
                ex.printStackTrace();;
            }
        }
    }

    public void inventoryDeleteBtn() {
        if (data.id == 0) {
            alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Please fill all blank fields");
            alert.showAndWait();
        } else {
            String prodID = inventory_productID.getText();

            String query = "delete from PRODUCT where id=" + data.id;
            try {
                alert = new Alert(AlertType.CONFIRMATION);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Are you sure you want to delete Product ID: " + prodID + "?");
                Optional<ButtonType> option = alert.showAndWait();

                if (option.get().equals(ButtonType.OK)) {
                    ps = con.getConnection().prepareStatement(query);
                    ps.executeUpdate();

                    alert = new Alert(AlertType.INFORMATION);
                    alert.setTitle("Info Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Successfully delete Product!");
                    alert.showAndWait();

                    // cập nhật table view
                    inventoryShowData();
                    // xóa dữ liệu các ô input
                    inventoryGetClearBtn();
                } else {
                    alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Error Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Something went wrong. Please try again");
                    alert.showAndWait();
                }
            } catch (Exception ex) {
                ex.printStackTrace();;
            }
        }
    }

    public void inventoryGetClearBtn() {
        inventory_productID.setText("");
        inventory_productName.setText("");
        inventory_size.getSelectionModel().clearSelection();
        inventory_stock.setText("");
        inventory_price.setText("");
        inventory_status.getSelectionModel().clearSelection();
        data.path = "";
        data.id = 0;
        inventory_imageView.setImage(null);
    }

    public void inventoryImportBtn() {
        FileChooser openFile = new FileChooser();
        openFile.getExtensionFilters().add(new ExtensionFilter("Open Image File", "*png", "*jpg"));

        File file = openFile.showOpenDialog(main_form.getScene().getWindow());

        if (file != null) {
            data.path = file.getAbsolutePath();
            image = new Image(file.toURI().toString(), 130, 130, false, true);

            inventory_imageView.setImage(image);
        }
    }

    // gộp tất cả dữ liệu
    public ObservableList<productData> inventoryDataList() {
        ObservableList<productData> listData = FXCollections.observableArrayList();

        String querySelect = "select * from PRODUCT";

        try {
            ps = con.getConnection().prepareStatement(querySelect);
            rs = ps.executeQuery();

            productData prodData;

            while (rs.next()) {
                prodData = new productData(rs.getInt("id"),
                        rs.getString("prod_id"),
                        rs.getString("prod_name"),
                        rs.getString("size"),
                        rs.getInt("stock"),
                        rs.getDouble("price"),
                        rs.getString("status"),
                        rs.getString("image"),
                        rs.getDate("date"));

                listData.add(prodData);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return listData;
    }

    private ObservableList<productData> inventoryListData;

    // xem dữ liệu trên bảng
    public void inventoryShowData() {
        inventoryListData = inventoryDataList();

        inventory_col_productID.setCellValueFactory(new PropertyValueFactory<>("productId"));
        inventory_col_productName.setCellValueFactory(new PropertyValueFactory<>("productName"));
        inventory_col_size.setCellValueFactory(new PropertyValueFactory<>("size"));
        inventory_col_stock.setCellValueFactory(new PropertyValueFactory<>("stock"));
        inventory_col_price.setCellValueFactory(new PropertyValueFactory<>("price"));
        inventory_col_status.setCellValueFactory(new PropertyValueFactory<>("status"));
        inventory_col_date.setCellValueFactory(new PropertyValueFactory<>("date"));

        inventory_tableView.setItems(inventoryListData);
    }

    public void inventorySelectData() {
        productData prodData = inventory_tableView.getSelectionModel().getSelectedItem();
        int num = inventory_tableView.getSelectionModel().getSelectedIndex();

        if (num - 1 < -1) {
            return;
        }

        inventory_productID.setText(prodData.getProductId());
        inventory_productName.setText(prodData.getProductName());
        inventory_stock.setText(String.valueOf(prodData.getStock()));
        inventory_price.setText(String.valueOf(prodData.getPrice()));

        data.path = prodData.getImage();

        String path = "File:" + prodData.getImage();
        data.date = String.valueOf(prodData.getDate());
        data.id = prodData.getId();

        image = new Image(path, 130, 130, false, true);
        inventory_imageView.setImage(image);
    }

    private String[] sizeList = {"S", "M", "L", "XL", "XXL"};

    public void inventorySizeList() {
        List<String> listSize = new ArrayList<>();

        if (sizeList != null) {
            for (String data : sizeList) {
                listSize.add(data);
            }
        }
        //ObservableList thường là để cập nhật giao diện người dùng tự động khi dữ liệu cơ bản thay đổi.
        ObservableList listData = FXCollections.observableArrayList(listSize);

        inventory_size.setItems(listData);
    }

    private String[] statusList = {"In stock", "Out of stock"};

    public void inventoryStatusList() {
        List<String> listStatus = new ArrayList<>();

        if (statusList != null) {
            for (String data : statusList) {
                listStatus.add(data);
            }
        }
        //ObservableList thường là để cập nhật giao diện người dùng tự động khi dữ liệu cơ bản thay đổi.
        ObservableList listData = FXCollections.observableArrayList(listStatus);

        inventory_status.setItems(listData);
    }

    public ObservableList<productData> menuGetData() {
        String querySelect = "select * from PRODUCT";

        ObservableList<productData> listData = FXCollections.observableArrayList();
        try {
            ps = con.getConnection().prepareStatement(querySelect);
            rs = ps.executeQuery();

            productData prodData;

            while (rs.next()) {
                prodData = new productData(rs.getInt("id"),
                        rs.getString("prod_id"),
                        rs.getString("prod_name"),
                        rs.getString("size"),
                        rs.getInt("stock"),
                        rs.getDouble("price"),
                        rs.getString("image"),
                        rs.getDate("date"));
                listData.add(prodData);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return listData;
    }

    public void menuDisplayCard() {
        cardListData.clear();
        cardListData.addAll(menuGetData());

        int row = 0;
        int column = 0;

        menu_gridPane.getChildren().clear();
        menu_gridPane.getRowConstraints().clear();
        menu_gridPane.getColumnConstraints().clear();

        for (int i = 0; i < cardListData.size(); i++) {
            try {
                FXMLLoader load = new FXMLLoader();
                load.setLocation(getClass().getResource("cardProduct.fxml"));
                AnchorPane pane = load.load();
                cardProductController cardC = load.getController();
                cardC.setData(cardListData.get(i));

                if (column == 3) {
                    column = 0;
                    row += 1;
                }

                menu_gridPane.add(pane, column++, row);
                GridPane.setMargin(pane, new Insets(10));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public ObservableList<productData> menuGetOrder() {
        customerID();
        ObservableList<productData> listData = FXCollections.observableArrayList();

        String querySelect = "select * from CUSTOMER where customer_id=" + customerID;

        try {
            ps = con.getConnection().prepareStatement(querySelect);
            rs = ps.executeQuery();

            productData prodData;

            while (rs.next()) {
                prodData = new productData(rs.getInt("id"),
                        rs.getString("prod_id"),
                        rs.getString("prod_name"),
                        rs.getString("size"),
                        rs.getInt("quantity"),
                        rs.getDouble("price"),
                        rs.getString("image"),
                        rs.getDate("date"));
                listData.add(prodData);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return listData;
    }

    private ObservableList<productData> menuOrderListData;

    public void menuShowOrderData() {
        menuOrderListData = menuGetOrder();

        menu_col_productName.setCellValueFactory(new PropertyValueFactory<>("productName"));
        menu_col_quantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        menu_col_price.setCellValueFactory(new PropertyValueFactory<>("price"));

        menu_tableView.setItems(menuOrderListData);
    }

    private int getID;

    public void menuSelectOrder() {
        productData prodData = menu_tableView.getSelectionModel().getSelectedItem();
        int num = menu_tableView.getSelectionModel().getSelectedIndex();

        if ((num - 1) < -1) {
            return;
        }
        // lấy id của order trước
        getID = prodData.getId();
    }

    private double totalPrice;

    public void menuGetTotal() {
        customerID();
        String querySelect = "select sum(price) from CUSTOMER where customer_id=" + customerID;

        try {
            ps = con.getConnection().prepareStatement(querySelect);
            rs = ps.executeQuery();

            if (rs.next()) {
                totalPrice = rs.getDouble("sum(price)");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void menuDisplayTotal() {
        menuGetTotal();
        menu_total.setText(totalPrice + "VND");
    }

    private double amount;
    private double change;

    public void menuAmount() {
        menuGetTotal();
        if (menu_amount.getText().isEmpty() || totalPrice == 0) {
            alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Something went wrong. Please try again");
            alert.showAndWait();
        } else {
            amount = Double.parseDouble(menu_amount.getText());
            change = 0;
            if (amount < totalPrice || amount < 0) {
                alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Invalid amount. Please try again");
                alert.showAndWait();
                menu_amount.setText("");
            } else {
                change = (amount - totalPrice);
                menu_change.setText(change + "VND");
            }
        }
    }

    public void menuPayBtn() {
        if (totalPrice == 0) {
            alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Please choose your order first");
            alert.showAndWait();
        } else {
            menuGetTotal();
            String query = "insert into RECEIPT(customer_id, total, em_username, date) values(?,?,?,?)";

            try {
                if (amount == 0) {
                    alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Error Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Something went wrong. Please try again");
                    alert.showAndWait();
                } else {
                    alert = new Alert(AlertType.CONFIRMATION);
                    alert.setTitle("Error Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Are you sure?");
                    Optional<ButtonType> option = alert.showAndWait();

                    if (option.get().equals(ButtonType.OK)) {
                        customerID();

                        String customer_id = String.valueOf(customerID);
                        String total = String.valueOf(totalPrice);

                        Date currentDate = new Date();
                        java.sql.Date sqlDate = new java.sql.Date(currentDate.getTime());
                        String date = String.valueOf(sqlDate);

                        String username = data.username;

                        ps = con.getConnection().prepareStatement(query);

                        ps.setString(1, customer_id);
                        ps.setString(2, total);
                        ps.setString(3, username);
                        ps.setString(4, date);

                        ps.executeUpdate();

                        alert = new Alert(AlertType.INFORMATION);
                        alert.setTitle("Info Message");
                        alert.setHeaderText(null);
                        alert.setContentText("Successful payment");
                        alert.showAndWait();

                        menuShowOrderData();
                    } else {
                        alert = new Alert(AlertType.WARNING);
                        alert.setTitle("Info Message");
                        alert.setHeaderText(null);
                        alert.setContentText("Cancelled");
                        alert.showAndWait();
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public void menuRemoveBtn() {
        if (getID == 0) {
            alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Cannot remove because your order is empty");
            alert.showAndWait();
        } else {
            String querySelect = "select prod_id, quantity from CUSTOMER where id=" + getID;
            String query = "delete from CUSTOMER where id=" + getID;
            try {
                ps = con.getConnection().prepareStatement(querySelect);
                rs = ps.executeQuery();

                if (rs.next()) {
                    String prodID = rs.getString("prod_id");
                    int quantityToRemove = rs.getInt("quantity");

                    String updateQuery = "UPDATE PRODUCT SET stock=stock + ? WHERE prod_id=?";
                    ps = con.getConnection().prepareStatement(updateQuery);
                    ps.setInt(1, quantityToRemove);
                    ps.setString(2, prodID);
                    ps.executeUpdate();

                    alert = new Alert(AlertType.CONFIRMATION);
                    alert.setTitle("Error Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Are you sure you want to delete this order?");
                    Optional<ButtonType> option = alert.showAndWait();

                    if (option.get().equals(ButtonType.OK)) {
                        ps = con.getConnection().prepareStatement(query);
                        ps.executeUpdate();
                    }

                    menuGetTotal();
                    menuDisplayTotal();
                    menuShowOrderData();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public void menuReceiptBtn() {
        if (totalPrice == 0 || menu_amount.getText().isEmpty()) {
            alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Please order first");
            alert.showAndWait();
        } else {
            HashMap map = new HashMap();
            map.put("getReceipt", customerID - 1);

            try {
                JasperDesign jDesign = JRXmlLoader.load("D:\\JavaFXApplication5\\src\\javafxapplication5\\report.jrxml");
                JasperReport jReport = JasperCompileManager.compileReport(jDesign);
                JasperPrint jPrint = JasperFillManager.fillReport(jReport, map, con.getConnection());

                JasperViewer.viewReport(jPrint, false);

                menuRestart();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public void menuRestart() {
        totalPrice = 0;
        change = 0;
        amount = 0;
        menu_total.setText("0.0 VND");
        menu_amount.setText("");
        menu_change.setText("0.0 VND");
    }

    private int customerID;

    public void customerID() {
        String querySelect = "select max(customer_id) from CUSTOMER";
        try {
            ps = con.getConnection().prepareStatement(querySelect);
            rs = ps.executeQuery();

            if (rs.next()) {
                customerID = rs.getInt("max(customer_id)");
            }

            String queryCheckCustomerID = "select max(customer_id) from RECEIPT";
            ps = con.getConnection().prepareStatement(queryCheckCustomerID);
            rs = ps.executeQuery();
            int checkID = 0;

            if (rs.next()) {
                checkID = rs.getInt("max(customer_id)");
            }

            if (customerID == 0) {
                customerID += 1;
            } else if (customerID == checkID) {
                customerID += 1;
            }

            data.customerID = customerID;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public ObservableList<customersData> customersDataList() {
        ObservableList<customersData> listData = FXCollections.observableArrayList();

        String querySelect = "select * from RECEIPT";

        try {
            ps = con.getConnection().prepareStatement(querySelect);
            rs = ps.executeQuery();

            customersData cData;

            while (rs.next()) {
                cData = new customersData(rs.getInt("id"),
                        rs.getInt("customer_id"),
                        rs.getDouble("total"),
                        rs.getDate("date"),
                        rs.getString("em_username"));

                listData.add(cData);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return listData;
    }

    private ObservableList<customersData> customersListData;

    public void customersShowData() {
        customersListData = customersDataList();

        customers_col_customerID.setCellValueFactory(new PropertyValueFactory<>("customerID"));
        customers_col_total.setCellValueFactory(new PropertyValueFactory<>("total"));
        customers_col_date.setCellValueFactory(new PropertyValueFactory<>("date"));
        customers_col_cashier.setCellValueFactory(new PropertyValueFactory<>("emUsername"));

        customers_tableView.setItems(customersListData);
    }

    public void switchForm(ActionEvent event) {
        if (event.getSource() == dashboard_btn) {
            dashboard_form.setVisible(true);
            inventory_form.setVisible(false);
            menu_form.setVisible(false);
            customers_form.setVisible(false);

            dashboardDisplayNumberCustomers();
            dashboardDisplayTodayIncome();
            dashboardDisplayTotalIncome();
            dashboardDisplaySoldProduct();
            dashboardIncomeChart();
            dashboardCustomerChart();

        } else if (event.getSource() == inventory_btn) {
            dashboard_form.setVisible(false);
            inventory_form.setVisible(true);
            menu_form.setVisible(false);
            customers_form.setVisible(false);

            inventorySizeList();
            inventoryStatusList();
            inventoryShowData();

        } else if (event.getSource() == menu_btn) {
            dashboard_form.setVisible(false);
            inventory_form.setVisible(false);
            menu_form.setVisible(true);
            customers_form.setVisible(false);

            menuDisplayCard();
            menuDisplayTotal();
            menuShowOrderData();

        } else if (event.getSource() == customers_btn) {
            dashboard_form.setVisible(false);
            inventory_form.setVisible(false);
            menu_form.setVisible(false);
            customers_form.setVisible(true);

            customersShowData();
        }
    }

    public void Logout() {
        try {
            alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Info Message");
            alert.setHeaderText(null);
            alert.setContentText("Are you sure you want to logout?");
            Optional<ButtonType> option = alert.showAndWait();

            if (option.get().equals(ButtonType.OK)) {

                // ẩn main form 
                signout_btn.getScene().getWindow().hide();

                // chuyển sang trang login form
                Parent root = FXMLLoader.load(getClass().getResource("FXMLDocument.fxml"));

                Stage stage = new Stage();
                Scene scene = new Scene(root);

                stage.setTitle("Clothing Management System");

                stage.setScene(scene);
                stage.show();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void displayUsername() {
        String user = data.username;
        user = user.substring(0, 1).toUpperCase() + user.substring(1);

        username.setText(user);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        displayUsername();

        dashboardDisplayNumberCustomers();
        dashboardDisplayTodayIncome();
        dashboardDisplayTotalIncome();
        dashboardDisplaySoldProduct();
        dashboardIncomeChart();
        dashboardCustomerChart();

        inventorySizeList();
        inventoryStatusList();
        inventoryShowData();

        menuDisplayCard();
        menuGetOrder();
        menuDisplayTotal();
        menuShowOrderData();

        customersShowData();
    }

}
