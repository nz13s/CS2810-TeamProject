package sql;

import entities.FoodOrders;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class FoodOrdersSql {
    private PreparedStatement getOrderbyFoodId;

    public FoodOrdersSql(Connection connection) throws SQLException {
        getOrderbyFoodId = connection.prepareStatement(
                "SELECT food_id, order_id, quantity "
                        + "FROM food_orders "
                        + "WHERE food_id = ?"
        );
    }

    public FoodOrders getOrderByFoodId(int foodID) throws SQLException {
        getOrderbyFoodId.setInt(1, foodID);
        ResultSet resultSet = getOrderbyFoodId.executeQuery();
        if (resultSet.next()) {
            return new FoodOrders(
                    resultSet.getInt("order_id"),
                    resultSet.getInt("food_id"),
                    resultSet.getInt("quantity"));
        }
        return null;
    }
}
