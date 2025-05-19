package model;

import java.sql.Timestamp;


public class Sanpham {
    private int id;

    private String tensanpham;

    private int soluong;

    private String thuonghieu;

    private String hedieuhanh;

    private String kichthuocman;

    private String chipxuly;

    private String dungluongpin;

    private String xuatxu;

    private double gianhap;

    private double giaban;

    private Timestamp time;

    private Double tongtiennhap;

    public Sanpham(int id, String tensanpham, int soluong, String thuonghieu, String hedieuhanh, String kichthuocman, String chipxuly, String dungluongpin, String xuatxu, double gianhap, double giaban, Timestamp time,Double tongtiennhap) {
        this.id = id;
        this.tensanpham = tensanpham;
        this.soluong = soluong;
        this.thuonghieu = thuonghieu;
        this.hedieuhanh = hedieuhanh;
        this.kichthuocman = kichthuocman;
        this.chipxuly = chipxuly;
        this.dungluongpin = dungluongpin;
        this.xuatxu = xuatxu;
        this.gianhap = gianhap;
        this.giaban = giaban;
        this.time = time;
        this.tongtiennhap = tongtiennhap;
    }


    public Sanpham(String tensanpham, int soluong, String thuonghieu, String hedieuhanh, String kichthuocman, String chipxuly, String dungluongpin, String xuatxu, double gianhap, double giaban) {
        this.tensanpham = tensanpham;
        this.soluong = soluong;
        this.thuonghieu = thuonghieu;
        this.hedieuhanh = hedieuhanh;
        this.kichthuocman = kichthuocman;
        this.chipxuly = chipxuly;
        this.dungluongpin = dungluongpin;
        this.xuatxu = xuatxu;
        this.gianhap = gianhap;
        this.giaban = giaban;
        this.tongtiennhap= soluong * gianhap;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTensanpham() {
        return tensanpham;
    }

    public void setTensanpham(String tensanpham) {
        this.tensanpham = tensanpham;
    }

    public int getSoluong() {
        return soluong;
    }

    public void setSoluong(int soluong) {
        this.soluong = soluong;
    }

    public String getThuonghieu() {
        return thuonghieu;
    }

    public void setThuonghieu(String thuonghieu) {
        this.thuonghieu = thuonghieu;
    }

    public String getHedieuhanh() {
        return hedieuhanh;
    }

    public void setHedieuhanh(String hedieuhanh) {
        this.hedieuhanh = hedieuhanh;
    }

    public String getKichthuocman() {
        return kichthuocman;
    }

    public void setKichthuocman(String kichthuocman) {
        this.kichthuocman = kichthuocman;
    }

    public String getChipxuly() {
        return chipxuly;
    }

    public void setChipxuly(String chipxuly) {
        this.chipxuly = chipxuly;
    }

    public String getDungluongpin() {
        return dungluongpin;
    }

    public void setDungluongpin(String dungluongpin) {
        this.dungluongpin = dungluongpin;
    }

    public String getXuatxu() {
        return xuatxu;
    }

    public void setXuatxu(String xuatxu) {
        this.xuatxu = xuatxu;
    }


    public double getGianhap() {
        return gianhap;
    }

    public void setGianhap(double gianhap) {
        this.gianhap = gianhap;
    }

    public double getGiaban() {
        return giaban;
    }

    public void setGiaban(double giaban) {
        this.giaban = giaban;
    }

    public Timestamp getTime() {
        return time;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }

    public Double getTongtiennhap() {
        return tongtiennhap;
    }
    public void setTongtiennhap(Double tongtiennhap) {
        this.tongtiennhap = tongtiennhap;
    }
}

