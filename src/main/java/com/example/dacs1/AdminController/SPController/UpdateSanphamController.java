package com.example.dacs1.AdminController.SPController;

import DAO.SanPhamDAO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Sanpham;

import java.text.NumberFormat;
import java.util.Locale;

public class UpdateSanphamController {

    @FXML private TextField chipxuli;
    @FXML private TextField dungluongpin;
    @FXML private TextField giaban;
    @FXML private TextField gianhap;
    @FXML private TextField hedieuhanh;
    @FXML private TextField kichthuocman;
    @FXML private TextField soluong;
    @FXML private TextField tensanpham;
    @FXML private TextField thuonghieu;
    @FXML private TextField xuatxu;

    private Sanpham sanpham;

    public void setData(Sanpham sp) {
        this.sanpham = sp;
        tensanpham.setText(sp.getTensanpham());
        xuatxu.setText(sp.getXuatxu());
        chipxuli.setText(sp.getChipxuly());
        kichthuocman.setText(sp.getKichthuocman());
        hedieuhanh.setText(sp.getHedieuhanh());
        dungluongpin.setText(sp.getDungluongpin());
        thuonghieu.setText(sp.getThuonghieu());
        soluong.setText(String.valueOf(sp.getSoluong()));
        gianhap.setText(formatCurrency(sp.getGianhap()));
        giaban.setText(formatCurrency(sp.getGiaban()));
    }

    @FXML
    void CapnhatClick(ActionEvent event) {
        if (sanpham == null) {
            showAlert("Lỗi dữ liệu", "Không tìm thấy sản phẩm để cập nhật");
            return;
        }

        if (!validateAllFields()) return;

        try {
            int soLuong = Integer.parseInt(soluong.getText());
            double giaNhap = Double.parseDouble(gianhap.getText());
            double giaBan = Double.parseDouble(giaban.getText());

            if (!validateNumbers(soLuong, giaNhap, giaBan)) return;

            double tongTienNhap = soLuong * giaNhap;
            System.out.println("Tổng Tiền Nhập: " + formatCurrency(tongTienNhap));

            updateSanphamFromFields(soLuong, giaNhap, giaBan);
            SanPhamDAO.suaSanPham(sanpham);

            showInfo("Thành công", "Cập nhật sản phẩm thành công!\nTổng tiền nhập: " + formatCurrency(tongTienNhap));
            closeWindow();

        } catch (NumberFormatException e) {
            showAlert("Lỗi định dạng", "Vui lòng nhập số hợp lệ vào các trường số");
        } catch (Exception e) {
            showAlert("Lỗi hệ thống", "Không thể cập nhật: " + e.getMessage());
        }
    }

    @FXML
    void huyboClick(ActionEvent event) {
        closeWindow();
    }

    // ========== HELPER METHODS ==========

    private boolean isNullOrEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }

    private boolean validateAllFields() {
        if (isNullOrEmpty(tensanpham.getText()) ||
                isNullOrEmpty(xuatxu.getText()) ||
                isNullOrEmpty(thuonghieu.getText()) ||
                isNullOrEmpty(kichthuocman.getText()) ||
                isNullOrEmpty(hedieuhanh.getText()) ||
                isNullOrEmpty(dungluongpin.getText()) ||
                isNullOrEmpty(chipxuli.getText()) ||
                isNullOrEmpty(soluong.getText()) ||
                isNullOrEmpty(gianhap.getText()) ||
                isNullOrEmpty(giaban.getText())) {

            showAlert("Thiếu thông tin", "Vui lòng điền đầy đủ tất cả các trường");
            return false;
        }
        return true;
    }

    private boolean validateNumbers(int soluong, double gianhap, double giaban) {
        if (soluong < 0) {
            showAlert("Lỗi số lượng", "Số lượng không được âm");
            return false;
        }
        if (gianhap <= 0 || giaban <= 0) {
            showAlert("Lỗi giá", "Giá nhập và giá bán phải lớn hơn 0");
            return false;
        }
        if (giaban < gianhap) {
            showAlert("Lỗi giá", "Giá bán phải lớn hơn hoặc bằng giá nhập");
            return false;
        }
        return true;
    }

    private void updateSanphamFromFields(int soluong, double gianhap, double giaban) {
        sanpham.setTensanpham(tensanpham.getText().trim());
        sanpham.setXuatxu(xuatxu.getText().trim());
        sanpham.setThuonghieu(thuonghieu.getText().trim());
        sanpham.setKichthuocman(kichthuocman.getText().trim());
        sanpham.setHedieuhanh(hedieuhanh.getText().trim());
        sanpham.setDungluongpin(dungluongpin.getText().trim());
        sanpham.setChipxuly(chipxuli.getText().trim());
        sanpham.setSoluong(soluong);
        sanpham.setGianhap(gianhap);
        sanpham.setGiaban(giaban);
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void showInfo(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private String formatCurrency(double amount) {
        Locale localeVN = new Locale("vi", "VN");
        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(localeVN);
        return currencyFormatter.format(amount);
    }

    private void closeWindow() {
        Stage stage = (Stage) tensanpham.getScene().getWindow();
        stage.close();
    }
}
