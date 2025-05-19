package com.example.dacs1.AdminController.SPController;

import DAO.SanPhamDAO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Sanpham;

public class AddSanphamController {

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

    @FXML
    private void huyboClick(ActionEvent event) {
        Stage stage = (Stage) tensanpham.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void taocauhinhClick(ActionEvent event) {
        String ten = capitalize(tensanpham.getText());
        String chip = capitalize(chipxuli.getText());
        String dung = capitalize(dungluongpin.getText());
        String hedieu = capitalize(hedieuhanh.getText());
        String kich = kichthuocman.getText().trim();
        String thuong = capitalize(thuonghieu.getText());
        String xuat = capitalize(xuatxu.getText());
        String sl = soluong.getText().trim();
        String gn = gianhap.getText().trim();
        String gb = giaban.getText().trim();

        // Kiểm tra các trường hợp rỗng
        if (ten.isEmpty() || chip.isEmpty() || dung.isEmpty() || hedieu.isEmpty() ||
                kich.isEmpty() || thuong.isEmpty() || xuat.isEmpty() ||
                sl.isEmpty() || gn.isEmpty() || gb.isEmpty()) {
            showAlert("Cảnh báo", "Vui lòng điền đầy đủ tất cả thông tin.");
            return;
        }

        try {
            // Kiểm tra và chuyển đổi giá trị số hợp lệ
            int soLuong = Integer.parseInt(sl);
            double giaNhap = Double.parseDouble(gn);
            double giaBan = Double.parseDouble(gb);
            double tongTienNhap = giaNhap * soLuong;

            if (soLuong < 0 || giaNhap < 0 || giaBan < 0) {
                showAlert("Lỗi dữ liệu", "Số lượng, giá nhập và giá bán không được nhỏ hơn 0.");
                return;
            }

            if (SanPhamDAO.isTenSanPhamTrung(ten)) {
                showAlert("Lỗi trùng tên", "Tên sản phẩm đã tồn tại. Vui lòng chọn tên khác.");
                return;
            }

            if (xuat.matches(".*\\d.*")) {
                showAlert("Lỗi nhập dữ liệu", "Xuất xứ không được chứa số.");
                return;
            }

            if (giaBan < giaNhap) {
                showAlert("Lỗi giá bán", "Giá bán không được thấp hơn giá nhập.");
                return;
            }

            // In ra định dạng tiền cho tham khảo
            System.out.println("Giá Nhập: " + formatCurrency(giaNhap));
            System.out.println("Giá Bán: " + formatCurrency(giaBan));
            System.out.println("Tổng Tiền Nhập: " + formatCurrency(tongTienNhap));

            // Tạo đối tượng sản phẩm và lưu vào CSDL
            Sanpham sanpham = new Sanpham(ten, soLuong, thuong, hedieu, kich, chip, dung, xuat, giaNhap, giaBan);
            SanPhamDAO.themSanPham(sanpham);

            // Đóng cửa sổ sau khi lưu
            Stage stage = (Stage) tensanpham.getScene().getWindow();
            stage.close();

        } catch (NumberFormatException e) {
            showAlert("Lỗi nhập dữ liệu", "Giá và số lượng phải là số hợp lệ.");
        }
    }

    private String capitalize(String input) {
        if (input == null || input.trim().isEmpty()) return "";
        String[] words = input.trim().split("\\s+");
        StringBuilder result = new StringBuilder();
        for (String word : words) {
            result.append(Character.toUpperCase(word.charAt(0)))
                    .append(word.substring(1).toLowerCase()).append(" ");
        }
        return result.toString().trim();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private String formatCurrency(Double amount) {
        return String.format("%,.2f", amount).replace(',', '.');
    }

}
