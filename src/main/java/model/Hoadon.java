package model;


public class Hoadon {
    private String tenKhachHang;
    private String soDienThoai;
    private String tenSanPham;
    private int soLuong;
    private double thanhTien;
    private String thoiGianMua; // Keep as String based on usage in Xacnhancontroller
    private Double giaBan;
    private Double loinhuan;

    public Hoadon(String tenKhachHang, String soDienThoai, String tenSanPham, int soLuong, double thanhTien, Double giaBan, String thoiGianMua) {
        this.tenKhachHang = tenKhachHang;
        this.soDienThoai = soDienThoai;
        this.tenSanPham = tenSanPham;
        this.soLuong = soLuong;
        this.thanhTien = thanhTien;
        this.thoiGianMua = thoiGianMua; // Fixed: assign parameter to instance variable
        this.giaBan = giaBan;
    }

    public Hoadon(String tenKhachHang, String soDienThoai, String tenSanPham, int soLuong, double thanhTien, Double giaBan, String thoiGianMua, Double loinhuan) {
        this.tenKhachHang = tenKhachHang;
        this.soDienThoai = soDienThoai;
        this.tenSanPham = tenSanPham;
        this.soLuong = soLuong;
        this.thanhTien = thanhTien;
        this.thoiGianMua = thoiGianMua;
        this.giaBan = giaBan;
        this.loinhuan = loinhuan;
    }

    public Double getLoinhuan() {
        return loinhuan;
    }

    public void setLoinhuan(Double loinhuan) {
        this.loinhuan = loinhuan;
    }

    public Hoadon() {
    }

    public String getTenKhachHang() {
        return tenKhachHang;
    }

    public void setTenKhachHang(String tenKhachHang) {
        this.tenKhachHang = tenKhachHang;
    }

    public String getSoDienThoai() {
        return soDienThoai;
    }

    public void setSoDienThoai(String soDienThoai) {
        this.soDienThoai = soDienThoai;
    }

    public String getTenSanPham() {
        return tenSanPham;
    }

    public void setTenSanPham(String tenSanPham) {
        this.tenSanPham = tenSanPham;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }

    public double getThanhTien() {
        return thanhTien;
    }

    public void setThanhTien(double thanhTien) {
        this.thanhTien = thanhTien;
    }

    public String getThoiGianMua() {
        return thoiGianMua;
    }

    public void setThoiGianMua(String thoiGianMua) {
        this.thoiGianMua = thoiGianMua;
    }

    public Double getGiaBan() {
        return giaBan;
    }

    public void setGiaBan(Double giaBan) {
        this.giaBan = giaBan;
    }
}