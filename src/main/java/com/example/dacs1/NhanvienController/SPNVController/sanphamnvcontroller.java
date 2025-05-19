package com.example.dacs1.NhanvienController.SPNVController;
import DAO.SanPhamDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Sanpham;

import javafx.event.ActionEvent;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.text.NumberFormat;
import java.util.Locale;

public class sanphamnvcontroller {

    @FXML
    private TableView<Sanpham> tableSanPham;

    @FXML
    private TableColumn<Sanpham, Integer> colId;
    @FXML
    private TableColumn<Sanpham, String> colTenSanPham;
    @FXML
    private TableColumn<Sanpham, Integer> colSoLuong;
    @FXML
    private TableColumn<Sanpham, String> colHeDieuHanh;
    @FXML
    private TableColumn<Sanpham, String> colThuongHieu;
    @FXML
    private TableColumn<Sanpham, String> colKichThuocMan;
    @FXML
    private TableColumn<Sanpham, String> colChipXuLy;
    @FXML
    private TableColumn<Sanpham, String> colDungLuongPin;
    @FXML
    private TableColumn<Sanpham, String> colXuatXu;
    @FXML
    private TableColumn<Sanpham, Double> colGiaBan;

    @FXML
    private TextField txtTimKiem;
    private String vaitro;

    ObservableList<Sanpham> data = FXCollections.observableArrayList();



    @FXML
    public void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colTenSanPham.setCellValueFactory(new PropertyValueFactory<>("tensanpham"));
        colSoLuong.setCellValueFactory(new PropertyValueFactory<>("soluong"));
        colThuongHieu.setCellValueFactory(new PropertyValueFactory<>("thuonghieu"));
        colHeDieuHanh.setCellValueFactory(new PropertyValueFactory<>("hedieuhanh"));
        colKichThuocMan.setCellValueFactory(new PropertyValueFactory<>("kichthuocman"));
        colChipXuLy.setCellValueFactory(new PropertyValueFactory<>("chipxuly"));
        colDungLuongPin.setCellValueFactory(new PropertyValueFactory<>("dungluongpin"));
        colXuatXu.setCellValueFactory(new PropertyValueFactory<>("xuatxu"));
        colGiaBan.setCellValueFactory(new PropertyValueFactory<>("giaban"));

        colGiaBan.setCellFactory(column -> new TableCell<Sanpham, Double>() {
            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(formatCurrency(item));
                }
            }
        });

        loadSanPhamData();
    }

    private void loadSanPhamData() {
        data.setAll(SanPhamDAO.getAllSanPham());
        tableSanPham.setItems(data);
    }

    @FXML
    private void ResetButton(ActionEvent event) {
        loadSanPhamData();
    }

    @FXML
    private void SearchButton(ActionEvent event) {
        String tk=txtTimKiem.getText();
        data.setAll(SanPhamDAO.timSPTheoTen(tk));
        tableSanPham.setItems(data);
    }

    @FXML
    private void AddClick(MouseEvent event) {
        showAlert("Bạn không có quyền thực hiện chức năng này!");
    }

    @FXML
    private void UpdateClick(MouseEvent event) {
        showAlert("Bạn không có quyền thực hiện chức năng này!");
    }

    @FXML
    private void DeleteClick(MouseEvent event) {
            showAlert("Bạn không có quyền thực hiện chức năng này!");
    }

    @FXML
    private void DetailClick(MouseEvent event) {
        Sanpham selected = tableSanPham.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Vui lòng chọn sản phẩm cần hiển thị");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/dacs1/Nhanvien/Sanphamnv/chitietsanphamnv.fxml"));
            Parent root = loader.load();

            ChitietSanphamnvController controller = loader.getController();
            controller.setData(selected);

            Stage stage = new Stage();
            stage.setTitle("Thông tin sản phẩm");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(((Node) event.getSource()).getScene().getWindow());
            stage.setOnHidden(e -> loadSanPhamData());
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Lỗi khi mở form chi tiết: " + e.getMessage());
        }
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Thông báo");
        alert.setContentText(message);
        alert.showAndWait();
    }

    private String formatCurrency(Double amount) {
        Locale vietnam = new Locale("vi", "VN");
        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(vietnam);
        return currencyFormatter.format(amount);
    }
}
