module com.lqd.oumarket {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.base;

    opens com.lqd.oumarket to javafx.fxml;
    exports com.lqd.oumarket;
}
