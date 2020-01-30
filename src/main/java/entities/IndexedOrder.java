package entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.ArrayList;

public class IndexedOrder extends Order {

    private int rank;
    private CATEGORY_TYPE category;

    public enum CATEGORY_TYPE {ORDERED, CONFIRMED, PREPARING, READY, SERVED}

    public IndexedOrder(int order_id, long time_ordered, long order_confirmed, long order_preparing, long order_ready, long order_served, int table_num, ArrayList<Item> list) {
        super(order_id, time_ordered, order_confirmed, order_preparing, order_ready, order_served, table_num, list);
        if (order_served > 0) {
            category = CATEGORY_TYPE.SERVED;
        } else if (order_ready > 0) {
            category = CATEGORY_TYPE.READY;
        } else if (order_preparing > 0) {
            category = CATEGORY_TYPE.PREPARING;
        } else if (order_confirmed > 0) {
            category = CATEGORY_TYPE.CONFIRMED;
        } else {
            category = CATEGORY_TYPE.ORDERED;
        }

    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    @JsonIgnore
    public CATEGORY_TYPE getCategoryType() {
        return category;
    }

    public int getCategory() {
        return category.ordinal();
    }

    public long findLatestTime() {
        return Math.max(Math.max(getOrderConfirmed(), getOrderPreparing()), getOrderReady());
    }
}
