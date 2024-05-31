package com.model;

public class CostingData {

    public String getProductId() {
        return ProductId;
    }

    public void setProductId(String RawID) {
        this.ProductId = RawID;
    }

    public String getCosting() {
        return costing;
    }

    public void setCosting(String costing) {
        this.costing = costing;
    }

    private String ProductId;
    private String costing;

}
