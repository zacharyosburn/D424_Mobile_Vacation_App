package com.example.d308_app.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "vacations")
public class Vacation {

    @PrimaryKey(autoGenerate = true)
    private int vacationId;

    private String vacationName;
    private String vacationStay;
    private String startDate;
    private String endDate;
    private double price;

    public Vacation(int vacationId, String vacationName, String vacationStay, String startDate, String endDate, double price) {
        this.vacationId = vacationId;
        this.vacationName = vacationName;
        this.vacationStay = vacationStay;
        this.startDate = startDate;
        this.endDate = endDate;
        this.price = price;
    }

    public int getVacationId() {
        return vacationId;
    }

    public void setVacationId(int vacationId) {
        this.vacationId = vacationId;
    }

    public String getVacationName() {
        return vacationName;
    }

    public void setVacationName(String vacationName) {
        this.vacationName = vacationName;
    }

    public String getVacationStay() {
        return vacationStay;
    }

    public void setVacationStay(String vacationStay) {
        this.vacationStay = vacationStay;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
