package mypos;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.converter.IntegerStringConverter;

public class UserTableViewExample extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        // 步驟 1：建立一個 TableView 元件，並指定其資料模型為 User 類別
        TableView<User> tableView = new TableView<>();

        // 步驟 2：開啟表格的編輯功能，讓使用者能雙擊儲存格直接修改內容
        tableView.setEditable(true);
        
        // 步驟 3：建立並設定「ID」欄位
        TableColumn<User, String> column1 = new TableColumn<>("ID");
        // setCellValueFactory 會綁定 User 類別中的 "id" 屬性 (自動呼叫 getId() 方法來讀取資料)
        column1.setCellValueFactory(new PropertyValueFactory<>("id"));

        // 步驟 4：建立並設定「名字」欄位 (文字型態的可編輯欄位)
        TableColumn<User, String> column2 = new TableColumn<>("名字");
        column2.setCellValueFactory(new PropertyValueFactory<>("name"));
        // setCellFactory 將此欄位的儲存格變成可輸入的文本框 (TextField)
        column2.setCellFactory(TextFieldTableCell.forTableColumn());
        // setOnEditCommit 設定當使用者完成編輯按下 Enter 後，要如何將新值寫回 User 物件
        column2.setOnEditCommit(
            event -> {
                User user = event.getRowValue();
                user.setName(event.getNewValue());
            }
        );

        // 步驟 5：建立並設定「年齡」欄位 (整數型態的可編輯欄位)
        TableColumn<User, Integer> column3 = new TableColumn<>("年齡");
        column3.setCellValueFactory(new PropertyValueFactory<>("age"));
        // 因為輸入的是字串，但目標型態是 Integer，所以要加上 IntegerStringConverter 作轉換
        column3.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        column3.setOnEditCommit(
            event -> {
                User user = event.getRowValue();
                user.setAge(event.getNewValue());
            }
        );

        // 步驟 6：將設定好的三個欄位加入至 TableView 中
        tableView.getColumns().add(column1);
        tableView.getColumns().add(column2);
        tableView.getColumns().add(column3);

        // 設定欄位寬度自動填滿整個表格，以隱藏最後多餘的空白欄位
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // 步驟 7：準備表格資料 (使用 ObservableList，當資料變動時畫面會自動更新)
        ObservableList<User> data = FXCollections.observableArrayList(
                        new User("u001", "John", 25),  
                        new User("u002", "Jane", 30), 
                        new User("u003", "Bob", 40)
        );
        tableView.setItems(data);
        
        // 也可以透過 getItems().add() 逐筆加入後續的資料
        tableView.getItems().add(new User("u004", "Alice", 35));
        tableView.getItems().add(new User("u005", "Charlie", 28));   

        // 步驟 8：建立 UI 根節點並設定場景，將 TableView 放入垂直排版容器物件 (VBox) 中
        VBox vbox = new VBox(tableView);
        // 將根節點放入 Scene，並設定在主舞台 (Stage) 上顯示
        Scene scene = new Scene(vbox);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
 

}//
