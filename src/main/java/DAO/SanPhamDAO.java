package DAO;

import database.Database;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Sanpham;

import java.sql.*;

public class SanPhamDAO {

    public static int getSoluongsanpham(){
        String sql = "select * from sanpham";
        int soluongsanpham = 0;
        try{
            Connection connn = Database.getConnection();
            PreparedStatement st=connn.prepareStatement(sql);
            ResultSet rs=st.executeQuery();

            while (rs.next()){
                soluongsanpham++;
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return soluongsanpham;
    }

    // Lấy danh sách tất cả sản phẩm
    public static ObservableList<Sanpham> getAllSanPham() {
        ObservableList<Sanpham> danhSachSanPham = FXCollections.observableArrayList();
        String query = "SELECT * FROM sanpham";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Sanpham sp = new Sanpham(
                        rs.getInt("id"),
                        rs.getString("tensanpham"),
                        rs.getInt("soluong"),
                        rs.getString("thuonghieu"),
                        rs.getString("hedieuhanh"),
                        rs.getString("kichthuocman"),
                        rs.getString("chipxuly"),
                        rs.getString("dungluongpin"),
                        rs.getString("xuatxu"),
                        rs.getDouble("gianhap"),
                        rs.getDouble("giaban"),
                        rs.getTimestamp("ngaytao"),
                        rs.getDouble("tongtiennhap")
                );
                danhSachSanPham.add(sp);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return danhSachSanPham;
    }

    // Thêm sản phẩm mới
    public static void themSanPham(Sanpham sanpham) {
        String sql = "INSERT INTO sanpham (tensanpham, soluong, thuonghieu, hedieuhanh, kichthuocman, chipxuly, dungluongpin, xuatxu, gianhap, giaban, tongtiennhap) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = Database.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, sanpham.getTensanpham());
            pstmt.setInt(2, sanpham.getSoluong());
            pstmt.setString(3, sanpham.getThuonghieu());
            pstmt.setString(4, sanpham.getHedieuhanh());
            pstmt.setString(5, sanpham.getKichthuocman());
            pstmt.setString(6, sanpham.getChipxuly());
            pstmt.setString(7, sanpham.getDungluongpin());
            pstmt.setString(8, sanpham.getXuatxu());
            pstmt.setDouble(9, sanpham.getGianhap());
            pstmt.setDouble(10, sanpham.getGiaban());
            pstmt.setDouble(11, sanpham.getTongtiennhap() != null ? sanpham.getTongtiennhap() : 0.0);

            pstmt.executeUpdate();

            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    sanpham.setId(rs.getInt(1)); // Gán ID tự tăng cho đối tượng
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Kiểm tra tên sản phẩm có trùng hay không
    public static boolean isTenSanPhamTrung(String ten) {
        String sql = "SELECT COUNT(*) FROM sanpham WHERE LOWER(tensanpham) = LOWER(?)";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, ten);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    // Cập nhật sản phẩm
    public static void suaSanPham(Sanpham sanpham) {
        String sql = "UPDATE sanpham SET tensanpham=?, soluong=?, thuonghieu=?, hedieuhanh=?, kichthuocman=?, chipxuly=?, dungluongpin=?, xuatxu=?, gianhap=?, giaban=?, tongtiennhap=? WHERE id=?";

        try (Connection conn = Database.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, sanpham.getTensanpham());
            pstmt.setInt(2, sanpham.getSoluong());
            pstmt.setString(3, sanpham.getThuonghieu());
            pstmt.setString(4, sanpham.getHedieuhanh());
            pstmt.setString(5, sanpham.getKichthuocman());
            pstmt.setString(6, sanpham.getChipxuly());
            pstmt.setString(7, sanpham.getDungluongpin());
            pstmt.setString(8, sanpham.getXuatxu());
            pstmt.setDouble(9, sanpham.getGianhap());
            pstmt.setDouble(10, sanpham.getGiaban());
            pstmt.setDouble(11, sanpham.getTongtiennhap() != null ? sanpham.getTongtiennhap() : 0.0);
            pstmt.setInt(12, sanpham.getId());

            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Lỗi khi cập nhật sản phẩm", e);
        }
    }

    // Xóa sản phẩm theo ID
    public static void xoaSanPham(int id) {
        String sql = "DELETE FROM sanpham WHERE id=?";

        try (Connection conn = Database.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Tìm sản phẩm theo ID
    public static Sanpham timSanPhamTheoId(int id) {
        String sql = "SELECT * FROM sanpham WHERE id = ?";
        Sanpham sp = null;

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                sp = new Sanpham(
                        rs.getInt("id"),
                        rs.getString("tensanpham"),
                        rs.getInt("soluong"),
                        rs.getString("thuonghieu"),
                        rs.getString("hedieuhanh"),
                        rs.getString("kichthuocman"),
                        rs.getString("chipxuly"),
                        rs.getString("dungluongpin"),
                        rs.getString("xuatxu"),
                        rs.getDouble("gianhap"),
                        rs.getDouble("giaban"),
                        rs.getTimestamp("ngaytao"),
                        rs.getDouble("tongtiennhap")
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return sp;
    }

    // Tìm sản phẩm theo tên, thương hiệu, xuất xứ hoặc hệ điều hành
    public static ObservableList<Sanpham> timSanPhamTheoTen(String tuKhoa) {
        ObservableList<Sanpham> ketQua = FXCollections.observableArrayList();
        String sql = "SELECT * FROM sanpham WHERE LOWER(tensanpham) LIKE ? OR LOWER(thuonghieu) LIKE ? OR LOWER(xuatxu) LIKE ? OR LOWER(hedieuhanh) LIKE ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            String tuKhoaTim = "%" + tuKhoa.toLowerCase() + "%";

            for (int i = 1; i <= 4; i++) {
                stmt.setString(i, tuKhoaTim);
            }

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Sanpham sp = new Sanpham(
                        rs.getInt("id"),
                        rs.getString("tensanpham"),
                        rs.getInt("soluong"),
                        rs.getString("thuonghieu"),
                        rs.getString("hedieuhanh"),
                        rs.getString("kichthuocman"),
                        rs.getString("chipxuly"),
                        rs.getString("dungluongpin"),
                        rs.getString("xuatxu"),
                        rs.getDouble("gianhap"),
                        rs.getDouble("giaban"),
                        rs.getTimestamp("ngaytao"),
                        rs.getDouble("tongtiennhap")
                );
                ketQua.add(sp);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return ketQua;
    }

    public static ObservableList<Sanpham> timSPTheoTen(String tuKhoa){
        ObservableList<Sanpham> ketQua = FXCollections.observableArrayList();
        String sql = "SELECT * FROM sanpham WHERE LOWER(tensanpham) LIKE ?";
        try(
            Connection conn = Database.getConnection();
            PreparedStatement pr= conn.prepareStatement(sql)){
            String tuKhoaTim = "%" + tuKhoa.toLowerCase() + "%";
            pr.setString(1,tuKhoaTim);
            try(ResultSet rs=pr.executeQuery()){
                while(rs.next()){
                    Sanpham sp=new Sanpham(
                            rs.getInt("id"),
                            rs.getString("tensanpham"),
                            rs.getInt("soluong"),
                            rs.getString("thuonghieu"),
                            rs.getString("hedieuhanh"),
                            rs.getString("kichthuocman"),
                            rs.getString("chipxuly"),
                            rs.getString("dungluongpin"),
                            rs.getString("xuatxu"),
                            rs.getDouble("gianhap"),
                            rs.getDouble("giaban"),
                            rs.getTimestamp("ngaytao"),
                            rs.getDouble("tongtiennhap")
                    );
                    ketQua.add(sp);
                }
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return ketQua;
    }
    // Cập nhật số lượng sản phẩm
    public static void updateSoLuong(int id, int soLuongMoi) {
        String sql = "UPDATE sanpham SET soluong = ? WHERE id = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, soLuongMoi);
            ps.setInt(2, id);
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

