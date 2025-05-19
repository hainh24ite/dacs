package com.example.dacs1.NhanvienController.HDXNVController;

import DAO.HoadonDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Hoadon;


import java.io.IOException;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

public class HoadonnvController {

    @FXML
    private TableView<Hoadon> tableHoaDon;
    @FXML
    private TableColumn<Hoadon, String> colTenKhach, colSoDT, colSanPham, colThoiGian;
    @FXML
    private TableColumn<Hoadon, Integer> colSoLuong;
    @FXML
    private TableColumn<Hoadon, Double> colGiaBan, colTongTien;
    ObservableList<Hoadon> data = FXCollections.observableArrayList();

    @FXML
    private ImageView delete;
    private String vaitro;
    @FXML
    public void initialize() {
        colTenKhach.setCellValueFactory(new PropertyValueFactory<>("tenKhachHang"));
        colSoDT.setCellValueFactory(new PropertyValueFactory<>("soDienThoai"));
        colSanPham.setCellValueFactory(new PropertyValueFactory<>("tenSanPham"));
        colSoLuong.setCellValueFactory(new PropertyValueFactory<>("soLuong"));
        colThoiGian.setCellValueFactory(new PropertyValueFactory<>("thoiGianMua"));

        setCurrencyColumn(colGiaBan, "giaBan");
        setCurrencyColumn(colTongTien, "thanhTien");

        tableHoaDon.getItems().setAll(HoadonDAO.layTatCaHoaDon());
    }

    private void setCurrencyColumn(TableColumn<Hoadon, Double> column, String property) {
        column.setCellValueFactory(new PropertyValueFactory<>(property));
        column.setCellFactory(col -> new TextFieldTableCell<Hoadon, Double>() {
            @Override
            public void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : formatCurrency(item));
            }
        });
    }

    private String formatCurrency(double amount) {
        return NumberFormat.getCurrencyInstance(new Locale("vi", "VN")).format(amount);
    }


    @FXML
    private void xoaHoadonxuat(MouseEvent event) {
        showAlert("Bạn không có quyền thực hiện chức năng này!");
    }

    @FXML
    private void chitietHoadonxuat(MouseEvent event) {
        Hoadon selected=tableHoaDon.getSelectionModel().getSelectedItem();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/dacs1/Nhanvien/Hoadonxuatnv/chitiethoadonxuatnv.fxml"));
            Parent root = loader.load();
            ChitietHoadonxuatnv controller = loader.getController();
            controller.setData(selected);
            Stage stage = new Stage();
            stage.setTitle("Chi tiết hóa đơn xuất");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(((Node) event.getSource()).getScene().getWindow());
            stage.show();

            stage.setOnHidden(e -> {
                try {
                    loadHoaDonTable();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void loadHoaDonTable() {
        data.clear(); //
        List<Hoadon> list = HoadonDAO.layTatCaHoaDon();
        data.addAll(list);
        tableHoaDon.setItems(data);
    }
    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Thông báo");
        alert.setContentText(message);
        alert.showAndWait();
    }
}
