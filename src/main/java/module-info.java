module com.lqd.oumarket {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    opens com.lqd.oumarket to javafx.fxml;
    exports com.lqd.oumarket;
    exports com.lqd.pojo;
    exports com.lqd.services;
}
