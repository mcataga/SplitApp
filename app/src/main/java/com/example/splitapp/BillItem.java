package com.example.splitapp;

public class BillItem {
    public static String TAG = "BillItem";
    private String mId;
    private String mName;
    private double mTotalPrice;
    private double mAmountPaid;
    private double mAmountOwed;
    private String imageId;


    public BillItem() {}
    public BillItem(String id, String name, double totalPrice, double amountPaid, double amountOwed) {
        this.mId = id;
        this.mName = name;
        this.mTotalPrice = totalPrice;
        this.mAmountPaid = amountPaid;
        this.mAmountPaid = amountOwed;
    }

    public String getName() {
        return mName;
    }
    public void setName(String name) { this.mName = name; }
    public String getId() { return mId; }
    public void setId(String id) { this.mId = id; }
    public double getTotalPrice() { return mTotalPrice; }
    public void setImageId(String imageId) {this.imageId=imageId;}
    public String getImageId() {return imageId;}
    public void setTotalPrice(double totalPrice) { this.mTotalPrice = totalPrice; }
    public double getAmountPaid() { return mAmountPaid; }
    public void setAmountPaid(double amountPaid) { this.mAmountPaid = amountPaid; }
    public double getAmountOwed() { return mAmountOwed; }
    public void setAmountOwed(double amountOwed) { this.mAmountOwed = amountOwed; }
}
