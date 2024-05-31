package com.model;

public class IssueData {

    public int getUpdateQty() {
        return updateQty;
    }

    public void setUpdateQty(int updateQty) {
        this.updateQty = updateQty;
    }

    public String getProductID() {
        return productID;
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    private String productID;
    private String qty;
    private int updateQty;

}
