package com.example.dacs1.AdminController.HDNController;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Sanpham;

import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class ChitietHoadonnhap {

    @FXML
    private TextField txtGiaban;

    @FXML
    private TextField txtSoluong;

    @FXML
    private TextField txtTensanpham;

    @FXML
    private TextField txtThoigianmua;

    @FXML
    private TextField txtTongtien;
    private Sanpham sanpham;

    @FXML
    void RoikhoiClick(ActionEvent event) {
        Stage stage = (Stage) txtTensanpham.getScene().getWindow();
        stage.close();
    }
    public void setData(Sanpham sp){
        this.sanpham=sp;
        NumberFormat vndFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));

        txtGiaban.setText(vndFormat.format(sp.getGiaban()));
        txtSoluong.setText(String.valueOf(sanpham.getSoluong()));
        txtTensanpham.setText(sanpham.getTensanpham());
        txtTongtien.setText(vndFormat.format(sp.getGiaban()*sp.getSoluong()));
        try {
            LocalDateTime localDateTime = sanpham.getTime().toLocalDateTime();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
            String formattedDate = localDateTime.format(formatter);
            txtThoigianmua.setText(formattedDate);
        } catch (Exception e) {
            txtThoigianmua.setText("Lỗi định dạng thời gian");
            e.printStackTrace();
        }
    }
}
