package DAO;

import database.Database;
import model.TaiKhoan;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TaiKhoanDAO {

    public static TaiKhoan checkLogin(String username, String password) {
        String sql = "SELECT * FROM taikhoan WHERE tendangnhap=? AND matkhau=?";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new TaiKhoan(
                        rs.getString("tendangnhap"),
                        rs.getString("matkhau"),
                        rs.getString("hoten"),
                        rs.getString("vaitro"),
                        rs.getTimestamp("ngaytao"),
                        rs.getBoolean("trangthai")
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void insert(TaiKhoan tk) {
        String sql = "INSERT INTO taikhoan (tendangnhap, matkhau, hoten, vaitro, ngaytao, trangthai) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = Database.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, tk.getTenDangNhap());
            stmt.setString(2, tk.getMatKhau());
            stmt.setString(3, tk.getHoTen());
            stmt.setString(4, tk.getVaiTro());
            stmt.setTimestamp(5, new Timestamp(tk.getNgayTao().getTime()));
            stmt.setBoolean(6, tk.isTrangThai());
            stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static List<TaiKhoan> getAll() {
        List<TaiKhoan> list = new ArrayList<>();
        String sql = "SELECT * FROM taikhoan";
        try (Connection conn = Database.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                TaiKhoan tk = new TaiKhoan(
                        rs.getString("tendangnhap"),
                        rs.getString("matkhau"),
                        rs.getString("hoten"),
                        rs.getString("vaitro"),
                        rs.getTimestamp("ngaytao"),
                        rs.getBoolean("trangthai")
                );
                list.add(tk);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public static boolean delete(String tendangnhap) {
        String sql = "DELETE FROM taikhoan WHERE tendangnhap=?";
        try (Connection conn = Database.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, tendangnhap);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0; // Trả về true nếu xóa thành công
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


    public static List<TaiKhoan> refreshSearch() {
        return getAll();
    }

    public static List<TaiKhoan> timKiem(String keyword) {
        List<TaiKhoan> list = new ArrayList<>();
        String sql = "SELECT * FROM taikhoan WHERE tendangnhap LIKE ? OR hoten LIKE ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, "%" + keyword + "%");
            stmt.setString(2, "%" + keyword + "%");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                TaiKhoan tk = new TaiKhoan(
                        rs.getString("tendangnhap"),
                        rs.getString("matkhau"),
                        rs.getString("hoten"),
                        rs.getString("vaitro"),
                        rs.getTimestamp("ngaytao"),
                        rs.getBoolean("trangthai")
                );
                list.add(tk);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
    public static  boolean kiemtratentrung(String tendangnhap) {
        String sql = "SELECT * FROM taikhoan WHERE tendangnhap = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)){
            stmt.setString(1, tendangnhap);
            ResultSet rs = stmt.executeQuery();

            if(rs.next()) {
                return true;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
