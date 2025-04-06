package com.example.viewinformation.service;

import com.example.viewinformation.pojo.GetHealthRecordsList;
import com.example.viewinformation.pojo.TimeLine;
import com.example.viewinformation.result.PageResult;

import java.util.Map;


public interface GetMessageService {
    //分页查询
    PageResult receiveMessage(Integer page,Integer size);

    //时间轴查询
    Map<String, TimeLine> fetchTimeLine(Long patientId);

    //根据患者id查询健康档案
    GetHealthRecordsList fetchHealthRecords(Long Id);
}
