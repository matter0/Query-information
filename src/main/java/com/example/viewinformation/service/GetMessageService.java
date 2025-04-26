package com.example.viewinformation.service;

import com.example.viewinformation.pojo.HealthRecordDto;
import com.example.viewinformation.pojo.SendMessageVo;
import com.example.viewinformation.pojo.TimeLine;
import com.example.viewinformation.result.PageResult;
import jakarta.servlet.http.HttpServletResponse;

import java.util.List;
import java.util.Map;


public interface GetMessageService {
    //分页查询
    PageResult receiveMessage(Integer page,Integer size);

    //时间轴查询
    Map<String, TimeLine> fetchTimeLine(String patientId);

    //根据患者id查询健康档案
    HealthRecordDto fetchHealthRecords(String  Id);
    //增量查询
    PageResult<SendMessageVo> receiveMessageADD(Integer page, Integer size);
    //前端导出excel
   void exportExcelOnline(HttpServletResponse response);
    //定时发送有邮件
    void sendMail();
}
