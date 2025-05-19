package com.example.dacs1.AdminController.BHController;


import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import model.Hoadon;
import DAO.HoadonDAO;
import javafx.collections.ObservableList;

import java.sql.Timestamp;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Locale;

public class Xacnhancontroller {

    @FXML
    private Label lblTenKhach;
    @FXML
    private Label lblSDT;
    @FXML
    private Label lblSanPham;
    @FXML
    private Label lblThoiGian;
    @FXML
    private Label lblTongTien;


    private ObservableList<Hoadon> danhSachHoaDon;

    // Add an interface for callback
    public interface OnThanhToanListener {
        void onThanhToanSuccess();
    }

    private OnThanhToanListener thanhToanListener;

    // Setter for the listener
    public void setOnThanhToanListener(OnThanhToanListener listener) {
        this.thanhToanListener = listener;
    }

    public void setData(ObservableList<Hoadon> danhSachHoaDon) {
        this.danhSachHoaDon = danhSachHoaDon;

        if (danhSachHoaDon != null && !danhSachHoaDon.isEmpty()) {
            StringBuilder spBuilder = new StringBuilder();
            double tongTien = 0;

            for (Hoadon hd : danhSachHoaDon) {
                spBuilder.append(hd.getTenSanPham())
                        .append(" x")
                        .append(hd.getSoLuong())
                        .append("\n");
                tongTien += hd.getThanhTien();
            }

            Hoadon first = danhSachHoaDon.get(0);
            lblTenKhach.setText(first.getTenKhachHang());
            lblSDT.setText(first.getSoDienThoai());
            lblSanPham.setText(spBuilder.toString().trim());
            lblThoiGian.setText(first.getThoiGianMua());

            // Format tiền tệ cho tổng tiền
            NumberFormat format = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
            lblTongTien.setText(format.format(tongTien));


        }
    }

    @FXML
    private void handleXacNhan() {
        if (danhSachHoaDon == null || danhSachHoaDon.isEmpty()) {
            showErrorAlert("Không có hóa đơn để xác nhận.");
            return;
        }

        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        DateTimeFormatter sqlFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        for (Hoadon hd : danhSachHoaDon) {
            try {
                String thoiGianMua = hd.getThoiGianMua();
                if (thoiGianMua == null || thoiGianMua.isEmpty()) {
                    thoiGianMua = LocalDateTime.now().format(inputFormatter);
                    hd.setThoiGianMua(thoiGianMua);
                }

                LocalDateTime parsedTime = LocalDateTime.parse(thoiGianMua, inputFormatter);
                Timestamp timestamp = Timestamp.valueOf(parsedTime);

                HoadonDAO.insertHoaDon(hd, timestamp);

            } catch (DateTimeParseException e) {
                showErrorAlert("Định dạng thời gian không hợp lệ: " + hd.getThoiGianMua());
                return;
            } catch (Exception e) {
                showErrorAlert("Lỗi hệ thống: " + e.getMessage());
                return;
            }
        }

        showSuccessAlert("Lưu hóa đơn thành công!");

        // ✅ Xóa dữ liệu khỏi TableView
        danhSachHoaDon.clear();

        // Gọi callback nếu có
        if (thanhToanListener != null) {
            thanhToanListener.onThanhToanSuccess();
        }

        closeWindow();
    }

    @FXML
    private void handleHuy() {
        closeWindow();
    }

    private void closeWindow() {
        Stage stage = (Stage) lblTenKhach.getScene().getWindow();
        stage.close();
    }

    private void showSuccessAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Xác nhận");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showErrorAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Lỗi");
        alert.setHeaderText("Đã xảy ra lỗi khi lưu hóa đơn");
        alert.setContentText(message);
        alert.showAndWait();
    }
}