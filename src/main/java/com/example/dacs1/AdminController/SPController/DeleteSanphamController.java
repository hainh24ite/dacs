package com.example.dacs1.AdminController.SPController;

import DAO.SanPhamDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.Sanpham;

import java.util.List;
import java.util.Optional;

public class DeleteSanphamController {

    @FXML
    private TableView<Sanpham> tableSanPham;
    @FXML
    private TableColumn<Sanpham, Integer> colId;
    @FXML
    private TableColumn<Sanpham, String> colTenSanPham;
    @FXML
    private TableColumn<Sanpham, String> colXuatXu;
    @FXML
    private TableColumn<Sanpham, String> colHeDieuHanh;
    @FXML
    private TableColumn<Sanpham, String> colChipXuLy;
    @FXML
    private TableColumn<Sanpham, String> colThuongHieu;

    @FXML
    private TextField txtTimKiem;

    private Sanpham selectedSanPham;
    private Runnable deleteSuccessCallback;
    private Runnable searchCallback;
    private Stage stage;
    ObservableList<Sanpham> data = FXCollections.observableArrayList();

    public void setStage(Stage stage) {
        this.stage = stage;
    }


    public void setSanPham(Sanpham sanpham) {
        if (tableSanPham != null) {
            tableSanPham.getItems().clear();
            tableSanPham.getItems().add(sanpham);
        }
    }

    public void setDeleteSuccessCallback(Runnable callback) {
        this.deleteSuccessCallback = callback;
    }

//    public void setSearchCallback(Runnable callback) {
//        this.searchCallback = callback;
//    }

    // Initialize table columns
    public void initialize() {
        // Đảm bảo ánh xạ đúng với các thuộc tính trong Sanpham
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colTenSanPham.setCellValueFactory(new PropertyValueFactory<>("tensanpham"));
        colThuongHieu.setCellValueFactory(new PropertyValueFactory<>("thuonghieu"));
        colHeDieuHanh.setCellValueFactory(new PropertyValueFactory<>("hedieuhanh"));
        colChipXuLy.setCellValueFactory(new PropertyValueFactory<>("chipxuly"));
        colXuatXu.setCellValueFactory(new PropertyValueFactory<>("xuatxu"));

    }

    @FXML
    private void DeleteClick(ActionEvent event) {
        selectedSanPham = tableSanPham.getSelectionModel().getSelectedItem();
        if (selectedSanPham == null) {
            showErrorAlert("Vui lòng chọn một sản phẩm để xóa.");
            return;
        }
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Xác nhận xóa");
        confirmation.setHeaderText("Bạn có chắc chắn muốn xóa sản phẩm này?");
        confirmation.setContentText(selectedSanPham.getTensanpham());

        Optional<ButtonType> result = confirmation.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                SanPhamDAO.xoaSanPham(selectedSanPham.getId());

                if (deleteSuccessCallback != null) {
                    deleteSuccessCallback.run();
                }

                closeWindow();
                showSuccessAlert();
            } catch (Exception e) {
                showErrorAlert("Lỗi khi xóa: " + e.getMessage());
            }
        }
    }

    @FXML
    private void cancelDelete(ActionEvent event) {

        closeWindow();
    }

    @FXML
    private void searchClick(ActionEvent event) {
        String tuKhoa = txtTimKiem.getText().trim();
        if (tuKhoa.isEmpty()) {
            showErrorAlert("Vui lòng nhập từ khóa tìm kiếm");
            return;
        }

        try {
            if (tuKhoa.matches("\\d+")) {
                int id = Integer.parseInt(tuKhoa);
                Sanpham sanpham = SanPhamDAO.timSanPhamTheoId(id);
                if (sanpham != null) {
                    setSanPham(sanpham);
                } else {
                    showInfoAlert("Không tìm thấy sản phẩm với ID: " + id);
                }
            } else {
                List<Sanpham> ketQua = SanPhamDAO.timSanPhamTheoTen(tuKhoa);
                if (ketQua != null && !ketQua.isEmpty()) {
                    if (ketQua.size() == 1) {
                        setSanPham(ketQua.get(0));
                    } else {
                        data.setAll(ketQua);
                        tableSanPham.setItems(data);
                        if (searchCallback != null) {
                            searchCallback.run();
                        }
                    }
                } else {
                    showInfoAlert("Không tìm thấy sản phẩm nào phù hợp với từ khóa: " + tuKhoa);
                }
            }
        } catch (Exception e) {
            showErrorAlert("Lỗi khi tìm kiếm: " + e.getMessage());
        }
    }

    private void closeWindow() {
        Stage stage = (Stage) txtTimKiem.getScene().getWindow();
        stage.close();
    }

    private void showSuccessAlert() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Thành công");
        alert.setHeaderText(null);
        alert.setContentText("Đã xóa sản phẩm thành công!");
        alert.showAndWait();
    }

    private void showErrorAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Lỗi");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showInfoAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Thông báo");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void setData(Sanpham sanpham) {
        this.selectedSanPham = sanpham;
        txtTimKiem.setText(selectedSanPham.getTensanpham());
        data.add(sanpham);
        tableSanPham.setItems(data);
    }
}
