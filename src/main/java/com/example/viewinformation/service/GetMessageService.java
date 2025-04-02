package com.example.viewinformation.service;

import com.example.viewinformation.pojo.GetHealthRecordsList;
import com.example.viewinformation.pojo.TimeLine;
import com.example.viewinformation.result.PageResult;


public interface GetMessageService {
    //分页查询
    PageResult receiveMessage(Integer page,Integer size);
//    //时间轴查询
//    TimeLine fetchTimeLine(Long patientId);

    GetHealthRecordsList fetchHealthRecords(Long Id);
}
