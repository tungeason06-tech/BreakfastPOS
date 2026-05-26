package data_type;

/**
 * Represents an order detail entry from the database
 * Maps to the order_details table
 */
public class OrderDetail {
    private int id;
    private String orderId;
    private String productId;
    private int quantity;

    /**
     * Default constructor
     */
    public OrderDetail() {
    }

    /**
     * Parameterized constructor
     * 
     * @param id        The order detail ID
     * @param orderId   The order ID this detail belongs to
     * @param productId The product ID for this order detail
     * @param quantity  The quantity ordered
     */
    public OrderDetail(int id, String orderId, String productId, int quantity) {
        this.id = id;
        this.orderId = orderId;
        this.productId = productId;
        this.quantity = quantity;
    }

    /**
     * Constructor without ID (useful when creating new entries)
     * 
     * @param orderId   The order ID this detail belongs to
     * @param productId The product ID for this order detail
     * @param quantity  The quantity ordered
     */
    public OrderDetail(String orderId, String productId, int quantity) {
        this.orderId = orderId;
        this.productId = productId;
        this.quantity = quantity;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "OrderDetail{" +
                "id=" + id +
                ", orderId='" + orderId + '\'' +
                ", productId='" + productId + '\'' +
                ", quantity=" + quantity +
                '}';
    }
}
