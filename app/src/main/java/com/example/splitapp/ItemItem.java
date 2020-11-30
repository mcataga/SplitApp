package com.example.splitapp;

public class ItemItem {
    private String mId;
    private String mName;
    private double mPrice;
    private int mSplit;

    public ItemItem() {}
    public ItemItem(String id, String name, double price, int split) {
        this.mId = id;
        this.mName = name;
        this.mPrice = price;
        if(split == 0) split = 1;
        this.mSplit = split;
    }

    public String getName() { return mName; }
    public void setName(String name) { this.mName = name; }
    public String getId() { return mId; }
    public void setId(String id) { this.mId = id; }
    public double getPrice() { return mPrice; }
    public void setPrice(double price) {this.mPrice=price;}
    public int getSplit() {return mSplit;}
    public void setSplit(int split) { this.mSplit = split; }
}
