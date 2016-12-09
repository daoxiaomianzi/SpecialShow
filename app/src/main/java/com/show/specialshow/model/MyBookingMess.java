package com.show.specialshow.model;

import java.io.Serializable;
import java.util.List;

import com.alibaba.fastjson.JSON;

public class MyBookingMess implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 6261553867803288799L;
    private String shop_name;//预约门店名称
    private String service_name;//服务名称
    private String service_id;//服务id
    private String staff_name;//手艺人名称
    private String staff_id;//手艺人id
    private String appointment_time;//预约时间
    private String add_time;//提交时间
    private int status;//预约状态
    private String people_num;//预约人数
    private String people_mobile;//预约电话
    private String people_name;//预约人名称
    private String people_remark;//
    private String shop_id;//商户id
    private String appointment_id;//预约id
    private String service_price;//预约总费用

    public String getStaff_id() {
        return staff_id;
    }

    public void setStaff_id(String staff_id) {
        this.staff_id = staff_id;
    }

    public String getService_id() {
        return service_id;
    }

    public void setService_id(String service_id) {
        this.service_id = service_id;
    }

    public String getService_price() {
        return service_price;
    }

    public void setService_price(String service_price) {
        this.service_price = service_price;
    }

    public String getShop_name() {
        return shop_name;
    }

    public void setShop_name(String shop_name) {
        this.shop_name = shop_name;
    }

    public String getService_name() {
        return service_name;
    }

    public void setService_name(String service_name) {
        this.service_name = service_name;
    }

    public String getStaff_name() {
        return staff_name;
    }

    public void setStaff_name(String staff_name) {
        this.staff_name = staff_name;
    }

    public String getAppointment_time() {
        return appointment_time;
    }

    public void setAppointment_time(String appointment_time) {
        this.appointment_time = appointment_time;
    }

    public String getAdd_time() {
        return add_time;
    }

    public void setAdd_time(String add_time) {
        this.add_time = add_time;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getPeople_num() {
        return people_num;
    }

    public void setPeople_num(String people_num) {
        this.people_num = people_num;
    }

    public String getPeople_mobile() {
        return people_mobile;
    }

    public void setPeople_mobile(String people_mobile) {
        this.people_mobile = people_mobile;
    }

    public String getPeople_name() {
        return people_name;
    }

    public void setPeople_name(String people_name) {
        this.people_name = people_name;
    }

    public String getPeople_remark() {
        return people_remark;
    }

    public void setPeople_remark(String people_remark) {
        this.people_remark = people_remark;
    }

    public String getShop_id() {
        return shop_id;
    }

    public void setShop_id(String shop_id) {
        this.shop_id = shop_id;
    }

    public String getAppointment_id() {
        return appointment_id;
    }

    public void setAppointment_id(String appointment_id) {
        this.appointment_id = appointment_id;
    }

    public static List<MyBookingMess> parse(String result) {
        try {
            return JSON.parseArray(result, MyBookingMess.class);
        } catch (Exception e) {
            return null;
        }
    }
}
