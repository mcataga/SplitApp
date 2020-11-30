package com.example.splitapp;

public class ProfileItem {
    private static final String TAG = "ProfileItem";
    private String mId;
    private String mName;
    private String phoneNumber;
    private String email;
    private String address;
    private String notes;
    private int totalSplit;
    private double mAmountOwed;
    private boolean isVisible;


    public ProfileItem() {}
    public ProfileItem(String id, String name, String phoneNumber, String email, String address, String notes, int totalSplit, double amountOwed, boolean isVisible) {
        this.mId = id;
        this.mName = name;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.address = address;
        this.notes = notes;
        this.totalSplit = totalSplit;
        this.mAmountOwed = amountOwed;
        this.isVisible = isVisible;
    }

    public String getName() {
        return mName;
    }
    public void setName(String name) { this.mName = name; }
    public String getId() { return mId; }
    public void setId(String id) { this.mId = id; }
    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
    public int getTotalSplit() { return totalSplit; }
    public void setTotalSplit(int totalSplit) { this.totalSplit = totalSplit; }
    public double getAmountOwed() { return mAmountOwed; }
    public void setAmountOwed(double amountOwed) { this.mAmountOwed = amountOwed; }
    public boolean getIsVisible() { return isVisible; }
    public void setIsVisible(boolean isVisible) { this.isVisible = isVisible; }

}
