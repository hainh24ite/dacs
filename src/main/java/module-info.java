module com.example.dacs1 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.desktop;
    requires jbcrypt;

    opens model to javafx.base;
    opens com.example.dacs1 to javafx.fxml;
    exports com.example.dacs1;
    exports com.example.dacs1.AdminController.SPController;
    opens com.example.dacs1.AdminController.SPController to javafx.fxml;
    exports com.example.dacs1.NhanvienController.BHNVController;
    opens com.example.dacs1.NhanvienController.BHNVController to javafx.fxml;
    exports com.example.dacs1.AdminController.BHController;
    opens com.example.dacs1.AdminController.BHController to javafx.fxml;
    exports com.example.dacs1.AdminController.HDNController;
    opens com.example.dacs1.AdminController.HDNController to javafx.fxml;
    exports com.example.dacs1.AdminController.HDXController;
    opens com.example.dacs1.AdminController.HDXController to javafx.fxml;
    exports com.example.dacs1.DNController;
    opens com.example.dacs1.DNController to javafx.fxml;
    exports com.example.dacs1.AdminController.TKhoanController;
    opens com.example.dacs1.AdminController.TKhoanController to javafx.fxml;
    exports com.example.dacs1.AdminController.TKeController;
    opens com.example.dacs1.AdminController.TKeController to javafx.fxml;
    exports com.example.dacs1.NhanvienController.SPNVController;
    opens com.example.dacs1.NhanvienController.SPNVController to javafx.fxml;
    exports com.example.dacs1.NhanvienController.HDXNVController;
    opens com.example.dacs1.NhanvienController.HDXNVController to javafx.fxml;
    exports com.example.dacs1.ChatController;
    opens com.example.dacs1.ChatController to javafx.fxml;
}