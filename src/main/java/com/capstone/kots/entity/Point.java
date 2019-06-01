package com.capstone.kots.entity;

import lombok.ToString;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "points")
@ToString
public class Point {

    private int id;
    private Integer traceId;
    private TracePoint tracePoint;
    private Integer userId;
    private User user;
    private long latitude;
    private long longitude;
    private Timestamp timeSignaled;

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
    @Column(name = "trace_id")
    public Integer getTraceId() {
        return traceId;
    }

    public void setTraceId(Integer traceId) {
        this.traceId = traceId;
    }

    @Transient
    public TracePoint getTracePoint() {
        return tracePoint;
    }

    public void setTracePoint(TracePoint tracePoint) {
        this.tracePoint = tracePoint;
    }

    @Basic
    @Column(name = "user_id")
    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    @Basic
    @Column(name = "latitude")
    public long getLatitude() {
        return latitude;
    }

    public void setLatitude(long latitude) {
        this.latitude = latitude;
    }

    @Basic
    @Column(name = "longitude")
    public long getLongitude() {
        return longitude;
    }

    public void setLongitude(long longitude) {
        this.longitude = longitude;
    }

    @Basic
    @Column(name = "time_signaled")
    public Timestamp getTimeSignaled() {
        return timeSignaled;
    }

    public void setTimeSignaled(Timestamp timeSignaled) {
        this.timeSignaled = timeSignaled;
    }
}
