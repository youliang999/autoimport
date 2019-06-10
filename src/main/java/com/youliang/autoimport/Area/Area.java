package com.youliang.autoimport.Area;

import com.jjccb.onlineLoan.common.model.BaseDO;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author lixubo
 * @version Id: Area.java, v0.1 2019/5/6 14:21 lixubo Exp $$
 */
@Entity
@Table(name = "T_AREA")
public class Area extends BaseDO {
    public static final int LEVEL_PROVINCE = 1;
    public static final int LEVEL_CITY = 2;
    public static final int LEVEL_DISTRICT = 3;

    private String name;
    @Column(name = "lv")
    private Integer level;
    private Long pid;
    private Double longitude;
    private Double latitude;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Long getPid() {
        return pid;
    }

    public void setPid(Long pid) {
        this.pid = pid;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }
}
