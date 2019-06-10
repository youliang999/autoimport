package com.youliang.autoimport.upload;

import com.jjccb.onlineLoan.common.component.ExcelField;
import com.jjccb.onlineLoan.common.component.ExcelTitle;

@ExcelTitle({"机构名称", "省", "市", "区", "详细地址"})
public class BankOrgExcelData {

    @ExcelField(1)
    private String orgName;

    @ExcelField(2)
    private String province;

    @ExcelField(3)
    private String city;

    @ExcelField(4)
    private String district;

    @ExcelField(5)
    private String address;

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
