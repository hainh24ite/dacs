package com.example.dacs1.NhanvienController.HDXNVController;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Hoadon;


import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class ChitietHoadonxuatnv {

    @FXML
    private TextField txtGiaban;

    @FXML
    private TextField txtSodienthoai;

    @FXML
    private TextField txtSoluong;

    @FXML
    private TextField txtTenkhachhang;

    @FXML
    private TextField txtTensanpham;

    @FXML
    private TextField txtThoigianmua;

    @FXML
    private TextField txtTongtien;

    private Hoadon hoadon;

    @FXML
    void RoikhoiClick(ActionEvent event) {
        Stage stage = (Stage) txtTensanpham.getScene().getWindow();
        stage.close();
    }
    public void setData(Hoadon hd){
        this.hoadon=hd;
        NumberFormat vn = NumberFormat.getCurrencyInstance(new Locale("vi","VN"));
        txtSodienthoai.setText(hd.getSoDienThoai());
        txtTenkhachhang.setText(hd.getTenKhachHang());
        txtGiaban.setText(vn.format(hd.getGiaBan()));
        txtSoluong.setText(String.valueOf(hd.getSoLuong()));
        txtTensanpham.setText(hd.getTenSanPham());
        txtTongtien.setText(vn.format(hd.getThanhTien()));
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
            LocalDateTime localDateTime = LocalDateTime.parse(hd.getThoiGianMua(), formatter);
            String formattedDate = localDateTime.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
            txtThoigianmua.setText(formattedDate);
        } catch (Exception e) {
            txtThoigianmua.setText("Lỗi định dạng thời gian");
            e.printStackTrace();
        }
    }

}
