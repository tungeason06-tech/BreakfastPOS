package file_read_write;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.Formatter;

import data_type.OrderDetail;
import data_type.SaleOrder;

public class OrderFileWriter {

    /**
     * 將訂單主檔寫入 orders.csv
     */
    public static boolean insertSaleOrder(SaleOrder order) {
        String dirPath = "mydatasets";
        String filePath = String.format("%s/orders.csv", dirPath);
        
        File directory = new File(dirPath);
        if (!directory.exists()) {
            directory.mkdirs();
        }
        //為了達成「附加模式」，我們就必須借助 FileWriter 或是 FileOutputStream 來達到目的，因為 Formatter 本身並沒有提供直接的附加模式參數。
        try (Formatter formatter = new Formatter(new FileWriter(filePath, true))) {
            String timestamp = order.getOrderDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            // 格式：訂單編號,日期時間,總金額,客戶編號
            formatter.format("%s,%s,%.0f,%s\n", 
                    order.getOrderId(), timestamp, order.getTotalAmount(), order.getCustomerId());
            return true;
        } catch (IOException e) {
            System.err.println(String.format("寫入 orders.csv 發生錯誤: %s", e.getMessage()));
            return false;
        }
    }

    /**
     * 將訂單明細寫入 order_details.csv
     */
    public static boolean insertOrderDetail(OrderDetail detail) {
        String dirPath = "mydatasets";
        String filePath = String.format("%s/order_details.csv", dirPath);
        
        File directory = new File(dirPath);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        try (Formatter formatter = new Formatter(new FileWriter(filePath, true))) {
            // 格式：訂單編號,商品編號,數量
            formatter.format("%s,%s,%d\n", 
                    detail.getOrderId(), detail.getProductId(), detail.getQuantity());
            return true;
        } catch (IOException e) {
            System.err.println(String.format("寫入 order_details.csv 發生錯誤: %s", e.getMessage()));
            return false;
        }
    }
}