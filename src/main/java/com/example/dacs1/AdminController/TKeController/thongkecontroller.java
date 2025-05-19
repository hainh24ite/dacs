package com.example.dacs1.AdminController.TKeController;

import DAO.HoadonDAO;
import DAO.SanPhamDAO;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import model.Hoadon;
import model.Sanpham;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;

public class thongkecontroller {

    @FXML
    private TextField txtSearch;
    @FXML
    private DatePicker fromDate;
    @FXML
    private DatePicker toDate;
    @FXML
    private TableView<Sanpham> tableView;
    @FXML
    private TableColumn<Sanpham, String> colSTT;
    @FXML
    private TableColumn<Sanpham, String> colTenSanPham;
    @FXML
    private TableColumn<Sanpham, String> colNgayNhap;
    @FXML
    private TableColumn<Sanpham, Integer> colSoLuongTon;
    @FXML
    private BarChart<String, Number> barchart;
    @FXML
    private TableView<Map.Entry<LocalDate, Double>> tabletongquan;
    @FXML
    private TableColumn<Map.Entry<LocalDate, Double>, String> ngay;
    @FXML
    private TableColumn<Map.Entry<LocalDate, Double>, String> doanhthu;
    @FXML
    private Label sanphamhienco;
    @FXML
    private TableView<Hoadon> tableDoanhthu;
    @FXML
    private TableColumn<Hoadon, String> tblNgay;
    @FXML
    private TableColumn<Hoadon, String> tblDoanhthu;
    @FXML
    private TableColumn<Hoadon, String> tblLoinhuan;
    @FXML
    private DatePicker tungaydoanhthu;
    @FXML
    private DatePicker denngaydoanhthu;
    private ObservableList<Sanpham> data = FXCollections.observableArrayList();
    private ObservableList<Hoadon> dataa = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // Đăng ký sự kiện khi người dùng thay đổi ngày
        tungaydoanhthu.setOnAction(e -> thongkedoanhthu());
        denngaydoanhthu.setOnAction(e -> thongkedoanhthu());
        // Bảng thống kê sản phẩm
        colSTT.setCellValueFactory(cellData -> new SimpleStringProperty(
                String.valueOf(tableView.getItems().indexOf(cellData.getValue()) + 1)));
        colTenSanPham.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTensanpham()));
        colNgayNhap.setCellValueFactory(cellData -> {
            if (cellData.getValue().getTime() != null) {
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                return new SimpleStringProperty(sdf.format(cellData.getValue().getTime()));
            } else {
                return new SimpleStringProperty("");
            }
        });
        colSoLuongTon.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("soluong"));

        loadTableData();
        initializeDoanhThu();
        sanphamhienco.setText(String.valueOf(SanPhamDAO.getSoluongsanpham()));


    }

    private void loadTableData() {
        List<Sanpham> list = SanPhamDAO.getAllSanPham();
        data.setAll(list);
        tableView.setItems(data);
    }

    @FXML
    private void handleSearch() {
        String keyword = txtSearch.getText().trim().toLowerCase();
        LocalDate from = fromDate.getValue();
        LocalDate to = toDate.getValue();

        List<Sanpham> list = SanPhamDAO.getAllSanPham();
        ObservableList<Sanpham> filtered = FXCollections.observableArrayList();

        for (Sanpham sp : list) {
            boolean matches = true;

            if (!keyword.isEmpty() && !sp.getTensanpham().toLowerCase().contains(keyword)) {
                matches = false;
            }

            if (from != null && sp.getTime() != null && sp.getTime().toInstant()
                    .isBefore(from.atStartOfDay().atZone(java.time.ZoneId.systemDefault()).toInstant())) {
                matches = false;
            }

            if (to != null && sp.getTime() != null && sp.getTime().toInstant()
                    .isAfter(to.plusDays(1).atStartOfDay().atZone(java.time.ZoneId.systemDefault()).toInstant())) {
                matches = false;
            }

            if (matches) {
                filtered.add(sp);
            }
        }

        tableView.setItems(filtered);
    }

    @FXML
    private void handleRefresh() {
        txtSearch.clear();
        fromDate.setValue(null);
        toDate.setValue(null);
        loadTableData();
    }

    @FXML
    void timkiembutton(ActionEvent event) {
        handleSearch();
    }


    private void initializeDoanhThu() {
        List<Map.Entry<LocalDate, Double>> doanhThuList = HoadonDAO.getDoanhThu7NgayGanNhat();

        LocalDate today = LocalDate.now();
        List<LocalDate> allDates = new ArrayList<>();
        for (int i = 6; i >= 0; i--) {
            allDates.add(today.minusDays(i));
        }

        Map<LocalDate, Double> doanhThuMap = new HashMap<>();
        for (Map.Entry<LocalDate, Double> entry : doanhThuList) {
            doanhThuMap.put(entry.getKey(), entry.getValue());
        }

        List<Map.Entry<LocalDate, Double>> completeDoanhThuList = new ArrayList<>();
        for (LocalDate date : allDates) {
            Double doanhThu = doanhThuMap.getOrDefault(date, 0.0);
            completeDoanhThuList.add(new AbstractMap.SimpleEntry<>(date, doanhThu));
        }

        // BarChart
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Doanh thu 7 ngày gần nhất");

        for (Map.Entry<LocalDate, Double> entry : completeDoanhThuList) {
            series.getData().add(new XYChart.Data<>(entry.getKey().toString(), entry.getValue()));
        }

        barchart.getData().clear();
        barchart.getData().add(series);

        // TableView doanh thu
        ObservableList<Map.Entry<LocalDate, Double>> tableData = FXCollections.observableArrayList(completeDoanhThuList);
        tabletongquan.setItems(tableData);

        ngay.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getKey().toString()));
        doanhthu.setCellValueFactory(cellData -> {
            Double value = cellData.getValue().getValue();
            String formatted = String.format("%,.0f ₫", value);
            return new SimpleStringProperty(formatted);
        });
    }

    private void thongkedoanhthu() {
        tblNgay.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getThoiGianMua()));

        tblDoanhthu.setCellValueFactory(cellData -> {
            Double doanhThu = cellData.getValue().getThanhTien();
            return new SimpleStringProperty(String.format("%,.0f ₫", doanhThu));
        });

        tblLoinhuan.setCellValueFactory(cellData -> {
            Double loiNhuan = cellData.getValue().getLoinhuan();
            return new SimpleStringProperty(String.format("%,.0f ₫", loiNhuan));
        });

        LocalDate tuNgay = tungaydoanhthu.getValue();
        LocalDate denNgay = denngaydoanhthu.getValue();

        if (tuNgay != null && denNgay != null) {
            System.out.println("Tìm doanh thu từ " + tuNgay + " đến " + denNgay);
            List<Hoadon> lhd = HoadonDAO.thongkeloinhuan(tuNgay, denNgay);

            if (lhd.isEmpty()) {
                System.out.println("Không có dữ liệu doanh thu trong khoảng thời gian này.");
            } else {
                System.out.println("Dữ liệu doanh thu: " + lhd.size() + " bản ghi.");
            }

            dataa.clear();  // Clear dữ liệu cũ
            dataa.addAll(lhd);  // Thêm dữ liệu mới vào ObservableList
            tableDoanhthu.setItems(dataa);  // Cập nhật lại tableView
        }
    }

}