package com.example.dacs1.NhanvienController.SPNVController;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Sanpham;

import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class ChitietSanphamnvController {

    @FXML
    private TextField txtChip;

    @FXML
    private TextField txtDungluongpin;

    @FXML
    private TextField txtGiaban;

    @FXML
    private TextField txtGianhap;

    @FXML
    private TextField txtHdh;

    @FXML
    private TextField txtKichthuoc;

    @FXML
    private TextField txtSoluong;

    @FXML
    private TextField txtTensanpham;

    @FXML
    private TextField txtThoigian;

    @FXML
    private TextField txtThuonghieu;

    @FXML
    private TextField txtXuatxu;
    @FXML
    private  TextField txtTongtiennhap;

    @FXML
    void RoikhoiClick(ActionEvent event) {
        Stage stage = (Stage) txtChip.getScene().getWindow();
        stage.close();
    }

    private Sanpham sanphamm;

    public void setData(Sanpham sp) {


        this.sanphamm = sp;
        double tongtiennhap =sanphamm.getGianhap()*sanphamm.getSoluong();
        txtChip.setText(sanphamm.getChipxuly());
        txtDungluongpin.setText(sanphamm.getDungluongpin());
        txtHdh.setText(sanphamm.getHedieuhanh());
        txtKichthuoc.setText(sanphamm.getKichthuocman());
        txtTensanpham.setText(sanphamm.getTensanpham());
        txtThuonghieu.setText(sanphamm.getThuonghieu());
        txtXuatxu.setText(sanphamm.getXuatxu());
        txtSoluong.setText(String.valueOf(sanphamm.getSoluong()));


        // Format tiền tệ Việt Nam
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        txtGiaban.setText(currencyFormat.format(sanphamm.getGiaban()));
        txtGianhap.setText(currencyFormat.format(sanphamm.getGianhap()));
        txtTongtiennhap.setText(currencyFormat.format(tongtiennhap));

        // Hiển thị thời gian thêm sản phẩm
        if (sanphamm.getTime() != null) {
            try {
                LocalDateTime localDateTime = sanphamm.getTime().toLocalDateTime();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
                txtThoigian.setText(localDateTime.format(formatter));
            } catch (Exception e) {
                txtThoigian.setText("Lỗi định dạng thời gian");
                e.printStackTrace();
            }
        } else {
            txtThoigian.setText("Không có dữ liệu");
        }
    }

    @FXML
    public void initialize() {
        // Khóa chỉnh sửa vì chỉ xem chi tiết
        txtChip.setEditable(false);
        txtDungluongpin.setEditable(false);
        txtGiaban.setEditable(false);
        txtGianhap.setEditable(false);
        txtHdh.setEditable(false);
        txtKichthuoc.setEditable(false);
        txtSoluong.setEditable(false);
        txtTensanpham.setEditable(false);
        txtThoigian.setEditable(false);
        txtThuonghieu.setEditable(false);
        txtXuatxu.setEditable(false);
        txtTongtiennhap.setEditable(false);
    }
}
