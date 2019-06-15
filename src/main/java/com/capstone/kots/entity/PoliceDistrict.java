package com.capstone.kots.entity;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "police_districts")
public class PoliceDistrict {
    private int id;
    private String districtName;
    private String districtPhone;


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "district_name")
    public String getDistrictName() {
        return districtName;
    }

    public void setDistrictName(String districtName) {
        this.districtName = districtName;
    }

    @Basic
    @Column(name = "district_phone")

    public String getDistrictPhone() {
        return districtPhone;
    }

    public void setDistrictPhone(String districtPhone) {
        this.districtPhone = districtPhone;
    }
}
