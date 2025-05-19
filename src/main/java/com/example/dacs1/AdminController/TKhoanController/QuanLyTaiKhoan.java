package com.example.dacs1.AdminController.TKhoanController;

import DAO.TaiKhoanDAO;
import com.example.dacs1.Hash.Hashpw;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import model.TaiKhoan;

import java.util.Date;
import java.util.List;

public class QuanLyTaiKhoan {
    @FXML
    private TextField txtTenDangNhap, txtHoTen, txtMatKhau;
    @FXML
    private ComboBox<String> cbVaiTro;
    @FXML
    private TableView<TaiKhoan> tblTaiKhoan;
    @FXML
    private TableColumn<TaiKhoan, String> colTenDangNhap, colHoTen, colVaiTro;
    @FXML
    private TableColumn<TaiKhoan, Date> colNgayTao;
    @FXML
    private TableColumn<TaiKhoan, Boolean> colTrangThai;
    @FXML
    private  TextField txtTimKiem;

    ObservableList<TaiKhoan> data=FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        cbVaiTro.setItems(FXCollections.observableArrayList("admin", "nhanvien"));
        cbVaiTro.setValue("nhanvien");

        colTenDangNhap.setCellValueFactory(new PropertyValueFactory<>("tenDangNhap"));
        colHoTen.setCellValueFactory(new PropertyValueFactory<>("hoTen"));
        colVaiTro.setCellValueFactory(new PropertyValueFactory<>("vaiTro"));
        colNgayTao.setCellValueFactory(new PropertyValueFactory<>("ngayTao"));
        colTrangThai.setCellValueFactory(new PropertyValueFactory<>("trangThai"));

        List<TaiKhoan> ls=TaiKhoanDAO.getAll();
        data.addAll(ls);
        tblTaiKhoan.setItems(data);
    }

    private void loadTable() {
        tblTaiKhoan.getItems().clear();
        data.clear();
        List<TaiKhoan> ls=TaiKhoanDAO.getAll();
        data.addAll(ls);
        tblTaiKhoan.setItems(data);
    }

    @FXML
    private void handleThem(ActionEvent event) {
        TaiKhoan tk = new TaiKhoan(
                txtTenDangNhap.getText(),
                Hashpw.Hash(txtMatKhau.getText()),
                txtHoTen.getText(),
                cbVaiTro.getValue(),
                new Date(),
                true
        );
        if (TaiKhoanDAO.kiemtratentrung(txtTenDangNhap.getText())) {
            showAlert("Tên đăng nhập đã tồn tại");
            return;
        }
        TaiKhoanDAO.insert(tk);
        loadTable();
        clearFields();
    }


    @FXML
    private void handleXoa(ActionEvent event) {
        TaiKhoan selectedAccount = tblTaiKhoan.getSelectionModel().getSelectedItem();
        if (selectedAccount != null) {
            boolean success = TaiKhoanDAO.delete(selectedAccount.getTenDangNhap());
            if (success) {
                loadTable();  // Tải lại bảng sau khi xóa
                showAlert(Alert.AlertType.INFORMATION, "Thông báo", "Xóa tài khoản thành công.");
            } else {
                showAlert(Alert.AlertType.ERROR, "Lỗi", "Không thể xóa tài khoản.");
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "Cảnh báo", "Vui lòng chọn tài khoản để xóa.");
        }
    }

    @FXML
    private void handleLamMoi(ActionEvent event) {
        txtHoTen.clear();
        txtMatKhau.clear();
        txtTenDangNhap.clear();
    }



    @FXML
    private void handleTimKiem(ActionEvent event) {
        String keyword = txtTimKiem.getText().trim();
        if (keyword.isEmpty()) {
            loadTable();
        } else {
            tblTaiKhoan.setItems(FXCollections.observableArrayList(TaiKhoanDAO.timKiem(keyword)));
        }
    }

    private void clearFields() {
        txtTenDangNhap.clear();
        txtMatKhau.clear();
        txtHoTen.clear();
        cbVaiTro.setValue("nhanvien");
    }



    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Cảnh báo");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
