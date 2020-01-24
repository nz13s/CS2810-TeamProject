package sql;

import entities.Order;
import entities.Food;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Order {
    private PreparedStatement orderById;
    private PreparedStatement orderSave;
    private PreparedStatement foodSave;
    private PreparedStatement orderUpdate;


    public Order(Connection connection) throws SQLException {
        orderByIdBy = connection.prepareStatement(
                "SELECT orderid, timeordered, orderconfirmed, orderready, orderserved, tablenum "
                        + "FROM orders "
                        + "WHERE orderid = ?");
        orderSave = connection.prepareStatement(
                "INSERT INTO orders (table_num, time_ordered, order_confirmed, order_ready, order_served) "
                +"VALUES (?, ?, ?, ?, ?)");
        foodSave = connection.prepareStatement(
                "INSERT INTO food_orders (order_id, food_id) VALUES (?, ?)");
    }

    public Order getOrderByID(int orderID) throws SQLException {
        orderByIdID.setInt(1, foodID);
        ResultSet resultSet = orderById.executeQuery();
        if (resultSet.next()) {
            return new Orders(
                    resultSet.getInt("orderid"),
                    resultSet.getLong("timeordered"),
                    resultSet.getLong("orderconfirmed"),
                    resultSet.getLong("orderready"),
                    resultSet.getLong("orderserved"),
                    resultSet.getInt("tablenum"));
        }
        return null;
    }
    public void saveOrder(Order o){
        ArrayList<Food> list = o.getFoodItems();
        orderSave.setInt(1, o.getTableNum());
        orderSave.setLong(2, o.getTimeOrdered());
        orderSave.setLong(3, o.getOrderConfirmed());
        orderSave.setBoolean(4, o.orderReady());
        orderSave.setLong(5, o.getOrderServed());
        orderSave.execute();
        orderSave.close();

        for(int i = 0; i < foodList.size; i++){
            foodSave.setInt(1, o.getOrderId);
            foodSave.setInt(2, list.get(i).getFoodID());
            foodSave.addBatch;
        }
        foodSave.executeBatch();
        foodSave.close();
    }
}
