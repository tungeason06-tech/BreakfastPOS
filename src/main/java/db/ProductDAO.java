package db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;

import data_type.Product;

/**
 * 產品資料表管理類別 專門負責產品資料的 CRUD 操作
 */
public class ProductDAO {public int getTotalSalesAmount() {
    String sql = "SELECT SUM(total_amount) FROM sale_order";

    try (
            Connection conn = DBConfig.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery()) {

        if (rs.next()) {
            return rs.getInt(1);
        }

    } catch (SQLException e) {
        System.out.println("取得銷售總額失敗: " + e.getMessage());
    }

    return 0;
}


    /**
     * 插入新產品
     */
    public boolean insert(Product product) {
        String sql = "INSERT INTO Product(product_id, category, name, price, image_url, description) "
                + "VALUES(?, ?, ?, ?, ?, ?)";

        try (
                Connection conn = DBConfig.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, product.getProductId());
            pstmt.setString(2, product.getCategory());
            pstmt.setString(3, product.getName());
            pstmt.setInt(4, product.getPrice());
            pstmt.setString(5, product.getImgUrl());
            pstmt.setString(6, product.getDescription());
            pstmt.executeUpdate();
            System.out.println("插入產品成功: " + product.getName());
            return true;
        } catch (SQLException e) {
            System.out.println("插入產品錯誤: " + e.getMessage());
            return false;
        }
    }

    /**
     * 更新產品資訊
     */
    public boolean update(Product product) {
        String sql = "UPDATE Product SET category = ?, name = ?, price = ?, image_url = ?, description = ? "
                + "WHERE product_id = ?";

        try (Connection conn = DBConfig.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, product.getCategory());
            pstmt.setString(2, product.getName());
            pstmt.setInt(3, product.getPrice());
            pstmt.setString(4, product.getImgUrl());
            pstmt.setString(5, product.getDescription());
            pstmt.setString(6, product.getProductId());
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected == 0) {
                System.out.println("產品不存在: " + product.getProductId());
            } else {
                System.out.println("更新產品成功: " + product.getName());
            }
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.out.println("更新產品錯誤: " + e.getMessage());
            return false;
        }
    }

    /**
     * 刪除產品
     */
    public boolean delete(Product product) {
        String sql = "DELETE FROM Product WHERE product_id = ?";

        try (Connection conn = DBConfig.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, product.getProductId());
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected == 0) {
                System.out.println("產品不存在: " + product.getProductId());
            } else {
                System.out.println("刪除產品成功: " + product.getProductId());
            }
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.out.println("刪除產品錯誤: " + e.getMessage());
            return false;
        }
    }


    /**
     * 根據ID獲取產品
     */
    public List<Product> getProductsById(String productId) {
        if (productId == null || productId.isBlank()) {
            return new ArrayList<>();
        }

        String sql = "SELECT * FROM Product WHERE product_id = ?";
        try (Connection conn = DBConfig.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, productId.trim());

            try (ResultSet rs = pstmt.executeQuery()) {
                List<Product> products = new ArrayList<>();
                while (rs.next()) {
                    Product product = new Product();
                    product.setProductId(rs.getString("product_id"));
                    product.setCategory(rs.getString("category"));
                    product.setName(rs.getString("name"));
                    product.setPrice(rs.getInt("price"));
                    product.setImgUrl(rs.getString("image_url"));
                    product.setDescription(rs.getString("description"));
                    products.add(product);
                }
                return products;
            }
        } catch (SQLException e) {
            System.out.println("根據ID獲取產品錯誤: " + e.getMessage());
        }

        return null;
    }


    /**
     * 根據類別獲取產品
     */
    public List<Product> getAllProducts() {
        String sql = "SELECT * FROM Product ORDER BY product_id";
        List<Product> products = new ArrayList<>();

        try (Connection conn = DBConfig.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql); ResultSet rs = pstmt.executeQuery();) {
           
            while (rs.next()) {
                Product product = new Product();
                product.setProductId(rs.getString("product_id"));
                product.setCategory(rs.getString("category"));
                product.setName(rs.getString("name"));
                product.setPrice(rs.getInt("price"));
                product.setImgUrl(rs.getString("image_url"));
                product.setDescription(rs.getString("description"));
                products.add(product);
            }
        } catch (SQLException e) {
            System.out.println("獲取所有產品錯誤: " + e.getMessage());
        }
        return products;
    }

    /**
     * 根據類別獲取產品
     */
    public List<Product> getProductsByCategory(String category) {
        String sql = "SELECT * FROM Product WHERE category = ?";
        List<Product> products = new ArrayList<>();

        try (Connection conn = DBConfig.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, category);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Product product = new Product();
                product.setProductId(rs.getString("product_id"));
                product.setCategory(rs.getString("category"));
                product.setName(rs.getString("name"));
                product.setPrice(rs.getInt("price"));
                product.setImgUrl(rs.getString("image_url"));
                product.setDescription(rs.getString("description"));
                products.add(product);
            }
        } catch (SQLException e) {
            System.out.println("根據類別獲取產品錯誤: " + e.getMessage());
        }
        return products;
    }

    /**
     * 根據名稱模糊搜尋產品
     */
    public List<Product> getProductsByName(String name) {
        String sql = "SELECT * FROM Product WHERE name LIKE ?";
        List<Product> products = new ArrayList<>();
        try (Connection conn = DBConfig.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, "%" + name + "%");
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Product product = new Product();
                product.setProductId(rs.getString("product_id"));
                product.setCategory(rs.getString("category"));
                product.setName(rs.getString("name"));
                product.setPrice(rs.getInt("price"));
                product.setImgUrl(rs.getString("image_url"));
                product.setDescription(rs.getString("description"));
                products.add(product);
            }
        } catch (SQLException e) {
            System.out.println("根據名稱模糊搜尋產品錯誤: " + e.getMessage());
        }
        return products;
    }


    /**
     * 從資料庫獲取所有產品，並轉換為 TreeMap 格式
     *
     * @return 以產品ID為鍵的產品 TreeMap
     */
    public TreeMap<String, Product> getProducts() {
        List<Product> productList = this.getAllProducts();
        TreeMap<String, Product> productMap = new TreeMap<>();

        // 將列表轉換為 TreeMap
        for (Product product : productList) {
            productMap.put(product.getProductId(), product);
        }

        return productMap;
    }

    public String[] getCategories() {
        // 取得所有產品
        List<Product> products = this.getAllProducts();

        // 創建一個 Set 來存儲不重複的類別
        Set<String> uniqueCategories = new HashSet<>();

        // 遍歷所有產品並收集類別
        for (Product product : products) {
            String category = product.getCategory();
            if (category != null) {
                uniqueCategories.add(category);
            }
        }

        // 將 Set 轉換為字串陣列
        String[] categoriesArray = new String[uniqueCategories.size()];
        return uniqueCategories.toArray(categoriesArray);
    }

    // 更簡單版本的 getCategories 方法
    public String[] getCategories_simple_version() {
        List<Product> products = this.getAllProducts();
        List<String> categories = new ArrayList<>();

        // 遍历所有产品
        for (Product product : products) {
            String category = product.getCategory();
            // 如果类别列表中没有这个类别，就添加进去
            if (!categories.contains(category)) {
                categories.add(category);
            }
        }

        // 轉換為字串陣列
        String[] categoriesArray = new String[categories.size()];
        return categories.toArray(categoriesArray);
        //return categories.toArray(new String[0]);
        //return categories.stream().distinct().toArray(String[]::new);
    }









}



