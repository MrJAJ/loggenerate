package com.example.loggenerate.dao;

import com.example.loggenerate.entity.ism_data_transfer_log;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


@Mapper
public interface LogDao {

    //保存日志信息
    //public void saveDataTransferLog(ism_data_transfer_log dataTransferLog_info);

    public List<ism_data_transfer_log> getEntity(@Param("startTime") String startTime, @Param("endTime") String endTime, @Param("recordNum") int recordNum);

    public List<ism_data_transfer_log> getAllEntity(@Param("startTime") String startTime, @Param("endTime") String endTime);

}
