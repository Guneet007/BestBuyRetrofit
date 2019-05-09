package com.siemens.bestbuy;

public class Products {
    private  String name;
    private String startDate;
    private double salePrice;
    private  double customerReviewAverage;
    private String image;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public double getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(double salePrice) {
        this.salePrice = salePrice;
    }

    public double getCustomerReviewAverage() {
        return customerReviewAverage;
    }

    public void setCustomerReviewAverage(double customerReviewAverage) {
        this.customerReviewAverage = customerReviewAverage;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
