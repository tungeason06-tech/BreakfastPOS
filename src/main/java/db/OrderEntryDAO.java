package db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;

import data_type.OrderDetail;
import data_type.SaleOrder;


/**
 * 訂單明細資料表管理類別
 * 專門負責訂單明細資料的 CRUD 操作
 */
public class OrderEntryDAO{
    
   
    /**
     * 插入一筆銷售訂單資料
     */
    public boolean insertSaleOrder(SaleOrder saleOrder) {
        String sql = "INSERT INTO Sale_order(order_id, order_date, total_amount, customer_id) " +
                "VALUES(?, ?, ?, ?)";
                
        try (Connection conn = DBConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, saleOrder.getOrderId());
            
            
            // 使用saleOrder的時間欄位
            pstmt.setString(2, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(saleOrder.getOrderDate()));

            
            // 使用當前時間戳
            // pstmt.setTimestamp(2, new java.sql.Timestamp(System.currentTimeMillis()));
          
            
            pstmt.setDouble(3, saleOrder.getTotalAmount());
            pstmt.setString(4, saleOrder.getCustomerId());
            
            int affectedRows = pstmt.executeUpdate();
            
            return affectedRows > 0;
        } catch (SQLException e) {
            System.out.println("插入銷售訂單錯誤: " + e.getMessage());
            return false;
        }
    }

     /**
     * 插入一筆訂單明細資料
     */
    public boolean insertOrderDetail(OrderDetail orderDetail) {
        String sql = "INSERT INTO Order_detail(order_id, product_id, quantity) " +
                "VALUES(?, ?, ?)";
                
        try (Connection conn = DBConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, orderDetail.getOrderId());
            pstmt.setString(2, orderDetail.getProductId());
            pstmt.setInt(3, orderDetail.getQuantity());
            
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.out.println("插入訂單明細錯誤: " + e.getMessage());
            return false;
        }
    }
}
