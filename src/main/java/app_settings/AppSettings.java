package app_settings;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

public class AppSettings {

    public VBox getRootPane() {

        VBox root = new VBox(20);
        root.setAlignment(Pos.CENTER);

        Label title = new Label("系統設定");
        title.setFont(new Font(30));

        Label version = new Label("版本：1.0");
        Label database = new Label("資料庫：SQLite");
        Label author = new Label("開發者：董宇軒");

        root.getChildren().addAll(
                title,
                version,
                database,
                author
        );

        return root;
    }
}