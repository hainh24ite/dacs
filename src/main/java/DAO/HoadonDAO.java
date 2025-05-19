package DAO;
import database.Database;
import model.Hoadon;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class HoadonDAO {


    public static void insertHoaDon(Hoadon hoaDon) {
        String sql = "INSERT INTO hoadon (tenkhachhang, sdt, tensanpham, soluong, giaban, thoigianmua, thanhtien) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, hoaDon.getTenKhachHang());
            stmt.setString(2, hoaDon.getSoDienThoai());
            stmt.setString(3, hoaDon.getTenSanPham());
            stmt.setInt(4, hoaDon.getSoLuong());
            stmt.setDouble(5, hoaDon.getGiaBan());

            // Parse the time string to LocalDateTime using the appropriate format
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
            LocalDateTime dateTime = LocalDateTime.parse(hoaDon.getThoiGianMua(), formatter);
            stmt.setTimestamp(6, Timestamp.valueOf(dateTime));

            stmt.setDouble(7, hoaDon.getThanhTien());

            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Add overloaded method to support timestamp parameter
    public static void insertHoaDon(Hoadon hoaDon, Timestamp timestamp) {
        String sql = "INSERT INTO hoadon (tenkhachhang, sdt, tensanpham, soluong, giaban, thoigianmua, thanhtien,loinhuan) VALUES (?, ?, ?, ?, ?, ?, ?,?)";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, hoaDon.getTenKhachHang());
            stmt.setString(2, hoaDon.getSoDienThoai());
            stmt.setString(3, hoaDon.getTenSanPham());
            stmt.setInt(4, hoaDon.getSoLuong());
            stmt.setDouble(5, hoaDon.getGiaBan());
            stmt.setTimestamp(6, timestamp);
            stmt.setDouble(7, hoaDon.getThanhTien());
            stmt.setDouble(8, hoaDon.getLoinhuan());
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static List<Hoadon> layTatCaHoaDon() {
        List<Hoadon> ds = new ArrayList<>();
        String sql = "SELECT * FROM hoadon";

        try (Connection conn = Database.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Hoadon hd = new Hoadon();
                hd.setTenKhachHang(rs.getString("tenkhachhang"));
                hd.setSoDienThoai(rs.getString("sdt"));
                hd.setTenSanPham(rs.getString("tensanpham"));
                hd.setSoLuong(rs.getInt("soluong"));

                // Convert Timestamp to formatted String
                Timestamp timestamp = rs.getTimestamp("thoigianmua");
                if (timestamp != null) {
                    LocalDateTime localDateTime = timestamp.toLocalDateTime();
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
                    hd.setThoiGianMua(localDateTime.format(formatter));
                }

                hd.setThanhTien(rs.getDouble("thanhtien"));
                hd.setGiaBan(rs.getDouble("giaban"));
                ds.add(hd);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return ds;
    }

    public static void xoahoadon(Hoadon hd) {
        String sql = "DELETE FROM hoadon WHERE sdt=?";

        try (Connection conn = Database.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, hd.getSoDienThoai());
            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static List<Map.Entry<LocalDate, Double>> getDoanhThu7NgayGanNhat() {
        List<Map.Entry<LocalDate, Double>> list = new ArrayList<>();

        String sql = "SELECT DATE(thoigianmua) AS ngay, SUM(thanhtien) AS doanhthu " +
                "FROM hoadon " +
                "WHERE DATE(thoigianmua) >= DATE_SUB(CURDATE(), INTERVAL 6 DAY) " +
                "GROUP BY DATE(thoigianmua) ORDER BY DATE(thoigianmua)";

        try (Connection conn = Database.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {
                LocalDate ngay = rs.getDate("ngay").toLocalDate();
                double doanhthu = rs.getDouble("doanhthu");

                list.add(new AbstractMap.SimpleEntry<>(ngay, doanhthu));
            }

        } catch (SQLException e) {
            System.err.println("Lỗi truy vấn doanh thu: " + e.getMessage());
            e.printStackTrace();
        }

        return list;
    }

    public static List<Hoadon> thongkeloinhuan(LocalDate tuNgay, LocalDate denNgay) {
        List<Hoadon> list = new ArrayList<>();
        String sql = "SELECT DATE(thoigianmua) AS ngay, SUM(thanhtien) AS doanhthu, SUM(loinhuan) AS loinhuan " +
                "FROM hoadon " +
                "WHERE thoigianmua BETWEEN ? AND ? " +
                "GROUP BY DATE(thoigianmua)";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setDate(1, java.sql.Date.valueOf(tuNgay));
            ps.setDate(2, java.sql.Date.valueOf(denNgay));
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Hoadon hd = new Hoadon();
                hd.setThoiGianMua(rs.getString("ngay"));
                hd.setThanhTien(rs.getDouble("doanhthu"));  // ← Tổng doanh thu trong ngày
                hd.setLoinhuan(rs.getDouble("loinhuan"));
                list.add(hd);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }


}