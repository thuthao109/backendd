package com.capstone.kots.entity;

import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
import java.util.Optional;

@Entity
@Table(name = "police_districts")
@ToString
public class PoliceDistrict {
    private int id;
    private String districtName;
    private String districtPhone;

    @ElementCollection
    private List<PoliceWard> wardList;

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



    @Transient
    public List<PoliceWard> getWardList() {
        return wardList;
    }

    public void setWardList(List<PoliceWard> wardList) {
        this.wardList = wardList;
    }

    public PoliceDistrict() {
    }

    public PoliceDistrict(int id, String districtName, String districtPhone, List<PoliceWard> wardList) {
        this.id = id;
        this.districtName = districtName;
        this.districtPhone = districtPhone;
        this.wardList = wardList;
    }
}
