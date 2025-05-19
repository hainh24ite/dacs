package model;

import java.util.Date;

public class TaiKhoan {
    private String tenDangNhap, matKhau, hoTen, vaiTro;
    private Date ngayTao;
    private boolean trangThai;

    public TaiKhoan(String tenDangNhap, String matKhau, String hoTen, String vaiTro, Date ngayTao, boolean trangThai) {
        this.tenDangNhap = tenDangNhap;
        this.matKhau = matKhau;
        this.hoTen = hoTen;
        this.vaiTro = vaiTro;
        this.ngayTao = ngayTao;
        this.trangThai = trangThai;
    }

    // Getter & Setter
    public String getTenDangNhap() { return tenDangNhap; }
    public String getMatKhau() { return matKhau; }
    public String getHoTen() { return hoTen; }
    public String getVaiTro() { return vaiTro; }
    public Date getNgayTao() { return ngayTao; }
    public boolean isTrangThai() { return trangThai; }

    public void setTenDangNhap(String tenDangNhap) { this.tenDangNhap = tenDangNhap; }
    public void setMatKhau(String matKhau) { this.matKhau = matKhau; }
    public void setHoTen(String hoTen) { this.hoTen = hoTen; }
    public void setVaiTro(String vaiTro) { this.vaiTro = vaiTro; }
    public void setNgayTao(Date ngayTao) { this.ngayTao = ngayTao; }
    public void setTrangThai(boolean trangThai) { this.trangThai = trangThai; }
}
