package com.example.dacs1.DNController;

import DAO.TaiKhoanDAO;
import com.example.dacs1.Hash.Hashpw;
import com.example.dacs1.MainController;
import com.example.dacs1.MainControllernv;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.TaiKhoan;

import java.io.IOException;
import java.util.List;

public class DangNhapController {

    @FXML
    private PasswordField txtMatkhau;

    @FXML
    private TextField txtTendangnhap;

    @FXML
    void dangnhapClick(ActionEvent event) {
        List<TaiKhoan> list=TaiKhoanDAO.getAll();
        String tendangnhap = txtTendangnhap.getText();
        String matkhau = txtMatkhau.getText();
        if(tendangnhap.isEmpty() || matkhau.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Lỗi đăng nhập");
            alert.setHeaderText(null);
            alert.setContentText("Vui lòng nhập đầy đủ thông tin!");
            alert.showAndWait();
        } else {

            for (TaiKhoan tk : list) {
                if (tk.getTenDangNhap().equals(tendangnhap)) {
                    boolean bl = Hashpw.checkPassword(matkhau, tk.getMatKhau());
                    if (bl) {
                        try {
                            String vaitro = tk.getVaiTro().toLowerCase(); // admin hoặc nhanvien
                            FXMLLoader loader;
                            Parent root;

                            if (vaitro.equals("admin")) {
                                loader = new FXMLLoader(getClass().getResource("/com/example/dacs1/mainn.fxml"));
                                root = loader.load();
                                MainController mainController = loader.getController();
                                mainController.setCurrentUsername(tendangnhap);
                            } else if (vaitro.equals("nhanvien")) {
                                loader = new FXMLLoader(getClass().getResource("/com/example/dacs1/mainnnv.fxml"));
                                root = loader.load();
                                MainControllernv mainControllerNv = loader.getController();
                                mainControllerNv.setCurrentUsername(tendangnhap);
                            } else {
                                Alert alert = new Alert(Alert.AlertType.WARNING);
                                alert.setTitle("Lỗi đăng nhập");
                                alert.setHeaderText(null);
                                alert.setContentText("Vai trò người dùng không hợp lệ");
                                alert.showAndWait();
                                return;
                            }

                            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                            stage.setScene(new Scene(root));
                            stage.setTitle("Trang chủ - " + vaitro.toUpperCase());
                            stage.setWidth(1140);
                            stage.setHeight(705);
                            stage.centerOnScreen();
                        } catch (IOException e) {
                            e.printStackTrace();
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle("Lỗi");
                            alert.setContentText("Có lỗi xảy ra: " + e.getMessage());
                            alert.showAndWait();
                        }
                    } else {
                        Alert alert = new Alert(Alert.AlertType.WARNING);
                        alert.setTitle("Lỗi đăng nhập");
                        alert.setHeaderText(null);
                        alert.setContentText("Sai tên đăng nhập hoặc mật khẩu");
                        alert.showAndWait();
                    }
                    break;
                }
            }

        }
    }
}