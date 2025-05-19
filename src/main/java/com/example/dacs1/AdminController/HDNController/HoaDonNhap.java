package com.example.dacs1.AdminController.HDNController;

import DAO.SanPhamDAO;
import javafx.beans.property.SimpleStringProperty;
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
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Sanpham;

import java.io.IOException;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

public class HoaDonNhap {

    @FXML
    private TableView<Sanpham> tableView;

    @FXML
    private TableColumn<Sanpham, String> colTenSanPham;

    @FXML
    private TableColumn<Sanpham, Integer> colSoLuong;

    @FXML
    private TableColumn<Sanpham, String> colThoiGian;

    @FXML
    private TableColumn<Sanpham, String> colGiaBan;
    @FXML
    private TableColumn<Sanpham, String> colGiaNhap;

    @FXML
    private TableColumn<Sanpham, String> colTongTien;

    ObservableList<Sanpham> data = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        colTenSanPham.setCellValueFactory(new PropertyValueFactory<>("tensanpham"));
        colSoLuong.setCellValueFactory(new PropertyValueFactory<>("soluong"));

        // Format thời gian nhập
        colThoiGian.setCellValueFactory(cellData -> {
            if (cellData.getValue().getTime() != null) {
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                return new SimpleStringProperty(sdf.format(cellData.getValue().getTime()));
            } else {
                return new SimpleStringProperty("");
            }
        });

        // Format giá bán VNĐ
        colGiaBan.setCellValueFactory(cellData -> {
            NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
            return new SimpleStringProperty(formatter.format(cellData.getValue().getGiaban()));
        });

        colGiaNhap.setCellValueFactory(cellData -> {
            NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
            return new SimpleStringProperty(formatter.format(cellData.getValue().getGianhap()));
        });

        // Format tổng tiền = giá * số lượng
        colTongTien.setCellValueFactory(cellData -> {
            double tong = cellData.getValue().getGiaban() * cellData.getValue().getSoluong();
            NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
            return new SimpleStringProperty(formatter.format(tong));
        });

        loadSanPhamTable();
    }

    @FXML
    private void xoaHoadonnhap(MouseEvent event) {
        Sanpham selected=tableView.getSelectionModel().getSelectedItem();
        if (selected == null){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Không có sản phẩm nào được chọn");
            alert.setHeaderText("Vui lòng chọn sản phẩm cần xóa");
            alert.showAndWait();
            return;
        }

        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Xác nhận xóa");
        confirmation.setHeaderText("Bạn có chắc chắn muốn xóa sản phẩm này?");
        confirmation.setContentText(selected.getTensanpham());

        Optional<ButtonType> result = confirmation.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                SanPhamDAO.xoaSanPham(selected.getId());
                loadSanPhamTable();
            } catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Lỗi");
                alert.setHeaderText(null);
                alert.setContentText("Lỗi khi xóa ");
                alert.showAndWait();
            }
        }
    }

    @FXML
    private void chitietHoadonnhap(MouseEvent event) {
        Sanpham selected=tableView.getSelectionModel().getSelectedItem();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/dacs1/Admin/Hoadonnhap/chitiethoadonnhap.fxml"));
            Parent root = loader.load();
            ChitietHoadonnhap controller = loader.getController();
            controller.setData(selected);
            Stage stage = new Stage();
            stage.setTitle("Chi tiết hóa đơn nhập");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(((Node) event.getSource()).getScene().getWindow());
            stage.show();

            stage.setOnHidden(e -> {
                try {
                    loadSanPhamTable();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void loadSanPhamTable() {
        data.clear(); // <-- XÓA dữ liệu cũ trước
        List<Sanpham> list = SanPhamDAO.getAllSanPham();
        data.addAll(list);
        tableView.setItems(data);
    }
}
