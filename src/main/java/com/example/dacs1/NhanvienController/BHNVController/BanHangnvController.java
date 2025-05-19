package com.example.dacs1.NhanvienController.BHNVController;

import DAO.SanPhamDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.Hoadon;
import model.Sanpham;

import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class BanHangnvController {

    @FXML
    private ComboBox<String> comboSanPham;
    @FXML
    private TextField tfSDT;
    @FXML
    private TextField tfTenKhach;
    @FXML
    private TextField tfSoLuong;
    @FXML
    private TableView<Hoadon> tableHoaDon;
    @FXML
    private TableColumn<Hoadon, String>  colThoiGian;
    @FXML
    private TableColumn<Hoadon, String> colSanPham;
    @FXML
    private TableColumn<Hoadon, String>  colSDT;
    @FXML
    private TableColumn<Hoadon, String>  colTenKhach;
    @FXML
    private TableColumn<Hoadon, Integer> colSoLuong;
    @FXML
    private TableColumn<Hoadon, String> colThanhTien;
    @FXML
    private TableColumn<Hoadon, String> colGiaBan;
    @FXML
    private Label lblTongTien;

    @FXML
    private TextField tfSoLuongMoi;

    private ObservableList<Hoadon> danhSachHoaDon = FXCollections.observableArrayList();
    private Map<String, Sanpham> sanphamMap = new HashMap<>();

    @FXML
    public void initialize() {
        for (Sanpham sp : SanPhamDAO.getAllSanPham()) {
            comboSanPham.getItems().add(sp.getTensanpham());
            sanphamMap.put(sp.getTensanpham(), sp);
        }


        colSanPham.setCellValueFactory(new PropertyValueFactory<>("tenSanPham"));
        colSoLuong.setCellValueFactory(new PropertyValueFactory<>("soLuong"));
        colTenKhach.setCellValueFactory(new PropertyValueFactory<>("tenKhachHang"));
        colSDT.setCellValueFactory(new PropertyValueFactory<>("soDienThoai"));
        colGiaBan.setCellValueFactory(new PropertyValueFactory<>("giaBan"));
        colThoiGian.setCellValueFactory(new PropertyValueFactory<>("thoiGianMua"));
        colThanhTien.setCellValueFactory(new PropertyValueFactory<>("thanhTien"));

        // Format giá bán và thành tiền
        colGiaBan.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(formatCurrency(cellData.getValue().getGiaBan()))
        );
        colThanhTien.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(formatCurrency(cellData.getValue().getThanhTien()))
        );

        tableHoaDon.setItems(danhSachHoaDon);
        lblTongTien.setText("0");
    }

    @FXML
    public void handleNhap(ActionEvent e) {
        String tenSp = comboSanPham.getValue();
        String soluong = tfSoLuong.getText();
        String tenKhach = tfTenKhach.getText();
        String sdt = tfSDT.getText();

        if (tenSp == null || soluong.isEmpty() || tenKhach.isEmpty() || sdt.isEmpty()) {
            showAlert("Vui lòng nhập đầy đủ thông tin.");
            return;
        }
        int soLuong=0;
        try{
            soLuong = Integer.parseInt(soluong);
            if(soLuong <= 0){
                showAlert("Số lượng phải lớn hơn hoặc bằng 0.");
                return;
            }
        }catch (NumberFormatException ex){
            showAlert("Số lượng phải là số nguyên dương");
            return;
        }

        Sanpham sp = sanphamMap.get(tenSp);

        if (sp == null) {
            showAlert("Sản phẩm không tồn tại.");
            return;
        }

        double giaBan = sp.getGiaban();
        double giaNhap= sp.getGianhap();

        if (!sdt.matches("^(0[3|5|7|8|9])+([0-9]{8})\\b")) {
            showAlert("Số điện thoại không hợp lệ. Vui lòng nhập số điện thoại Việt Nam (10-11 số, bắt đầu bằng 03, 05, 07, 08, 09...)");
            return;
        }

        if (sp.getSoluong() < soLuong) {
            showAlert("Sản phẩm không đủ số lượng trong kho.");
            return;
        }

        double thanhTien = soLuong * giaBan;
        double loinhuan= thanhTien-soLuong*giaNhap;
        String time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));

        Hoadon hoaDon = new Hoadon(tenKhach, sdt, tenSp, soLuong, giaBan, thanhTien, time,loinhuan);
        danhSachHoaDon.add(hoaDon);

        sp.setSoluong(sp.getSoluong() - soLuong);
        SanPhamDAO.updateSoLuong(sp.getId(), sp.getSoluong());
        updateTongTien();
    }


    private void updateTongTien() {
        double tong = danhSachHoaDon.stream().mapToDouble(Hoadon::getThanhTien).sum();
        NumberFormat format = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        lblTongTien.setText(format.format(tong));

    }

    @FXML
    public void handleThanhToan() {
        if(danhSachHoaDon.isEmpty()){
            showAlert("Bạn chưa cập nhập thông tin sản phẩm nào");
        }
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/dacs1/Nhanvien/Banhangnv/nutthanhtoannv.fxml"));
            Parent root = loader.load();
            Xacnhannvcontroller controller = loader.getController();
            controller.setData(danhSachHoaDon);
            Stage stage = new Stage();
            stage.setTitle("Xác Nhận Thanh Toán");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    public void handleXoaHoaDon() {
        Hoadon selected = tableHoaDon.getSelectionModel().getSelectedItem();
        if (selected != null) {
            // Cộng lại số lượng sản phẩm đã xóa vào kho
            Sanpham sp = sanphamMap.get(selected.getTenSanPham());
            sp.setSoluong(sp.getSoluong() + selected.getSoLuong());
            SanPhamDAO.updateSoLuong(sp.getId(), sp.getSoluong());

            // Xóa khỏi danh sách hóa đơn
            danhSachHoaDon.remove(selected);

            // Cập nhật tổng tiền
            updateTongTien();
        } else {
            showAlert("Vui lòng chọn một hóa đơn để xóa.");
        }
    }

    @FXML
    public void handleSuaSoLuong() {
        Hoadon selected = tableHoaDon.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Vui lòng chọn một hóa đơn để sửa.");
            return;
        }

        String input = tfSoLuongMoi.getText();
        if (!input.matches("\\d+")) {
            showAlert("Số lượng mới phải là số nguyên dương.");
            return;
        }

        int soLuongMoi = Integer.parseInt(input);
        if (soLuongMoi <= 0) {
            showAlert("Số lượng mới phải lớn hơn 0.");
            return;
        }

        int soLuongCu = selected.getSoLuong();
        int chenhLech = soLuongMoi - soLuongCu;

        Sanpham sp = sanphamMap.get(selected.getTenSanPham());

        if (chenhLech > 0 && sp.getSoluong() < chenhLech) {
            showAlert("Không đủ hàng trong kho để cập nhật số lượng.");
            return;
        }

        // Cập nhật số lượng trong kho
        sp.setSoluong(sp.getSoluong() - chenhLech);
        SanPhamDAO.updateSoLuong(sp.getId(), sp.getSoluong());

        // Cập nhật trong hóa đơn
        selected.setSoLuong(soLuongMoi);
        selected.setThanhTien(soLuongMoi * sp.getGiaban());

        tableHoaDon.refresh(); // Cập nhật lại bảng
        updateTongTien();
        tfSoLuongMoi.clear();
    }


    private void showAlert(String msg) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Cảnh báo");
        alert.setContentText(msg);
        alert.show();
    }

    private String formatCurrency(double amount) {
        Locale vietnam = new Locale("vi", "VN");
        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(vietnam);
        return currencyFormatter.format(amount);
    }
}
