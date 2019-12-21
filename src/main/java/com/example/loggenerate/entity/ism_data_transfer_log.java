/***********************************************************************
 * Module:  ism_data_transfer_log.java
 * Author:  luo
 * Purpose: Defines the Class ism_data_transfer_log
 ***********************************************************************/

package com.example.loggenerate.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Getter
@Setter
@ToString
public class ism_data_transfer_log {
    //序号
    public String id;
    //日志类型
    public String type_code;
    //操作时间
    public Date process_time;
    //操作单位
    public String uid;
    //操作接口
    public String process_api;
    //预计数据量
    public int expe_quantity;
    //上传数据量
    public int data_quantity;
    //操作日志
    public String process_log;
    //创建人名称
    public String create_name;
    //创建人登录名称
    public String create_by;
    //更新人名称
    public String update_name;
    //更新人登录名称
    public String update_by;
    //创建日期
    public Date create_date;
    //更新日期
    public Date update_date;

    public ism_data_transfer_log() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getProcess_time() {
        return process_time;
    }

    public void setProcess_time(Date process_time) {
        this.process_time = process_time;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getProcess_api() {
        return process_api;
    }

    public void setProcess_api(String process_api) {
        this.process_api = process_api;
    }

    public int getExpe_quantity() {
        return expe_quantity;
    }

    public void setExpe_quantity(int expe_quantity) {
        this.expe_quantity = expe_quantity;
    }

    public int getData_quantity() {
        return data_quantity;
    }

    public void setData_quantity(int data_quantity) {
        this.data_quantity = data_quantity;
    }

    public String getProcess_log() {
        return process_log;
    }

    public void setProcess_log(String process_log) {
        this.process_log = process_log;
    }

    public String getCreate_name() {
        return create_name;
    }

    public void setCreate_name(String create_name) {
        this.create_name = create_name;
    }

    public String getCreate_by() {
        return create_by;
    }

    public void setCreate_by(String create_by) {
        this.create_by = create_by;
    }

    public String getUpdate_name() {
        return update_name;
    }

    public void setUpdate_name(String update_name) {
        this.update_name = update_name;
    }

    public String getUpdate_by() {
        return update_by;
    }

    public void setUpdate_by(String update_by) {
        this.update_by = update_by;
    }

    public Date getCreate_date() {
        return create_date;
    }

    public void setCreate_date(Date create_date) {
        this.create_date = create_date;
    }

    public Date getUpdate_date() {
        return update_date;
    }

    public void setUpdate_date(Date update_date) {
        this.update_date = update_date;
    }

    public String getType_code() {
        return type_code;
    }

    public void setType_code(String type_code) {
        this.type_code = type_code;
    }
}