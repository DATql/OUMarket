module com.lqd.oumarket {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires de.jensd.fx.glyphs.fontawesome;
    opens com.lqd.oumarket to javafx.fxml;
    exports com.lqd.oumarket;
    exports com.lqd.pojo;
    exports com.lqd.services;
}
