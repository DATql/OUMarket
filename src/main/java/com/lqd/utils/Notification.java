package com.lqd.utils;


import javafx.geometry.Pos;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;

public class Notification {

    public static Notifications notification(){
        Notifications n = Notifications.create();
        n.title("Error");
        n.text("Tài khoản hoặc mật khẩu không đúng");
        n.hideAfter(Duration.seconds(5));
        n.position(Pos.BASELINE_RIGHT);
        return n;
    }

}
