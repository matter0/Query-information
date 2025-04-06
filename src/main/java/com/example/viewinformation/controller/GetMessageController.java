package com.example.viewinformation.controller;

import com.example.viewinformation.pojo.GetHealthRecordsList;
import com.example.viewinformation.pojo.SendMessageDto;
import com.example.viewinformation.pojo.TimeLine;
import com.example.viewinformation.result.PageResult;
import com.example.viewinformation.result.Result;
import com.example.viewinformation.service.GetMessageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;


@RestController
@Slf4j
@Tag(name= "查询病例信息")
public class GetMessageController {
    @Autowired
    private GetMessageService getMessageService;
    @Operation( summary = "查询病人病例信息",description = "分页查询病人病例信息")
    @PostMapping("/api/messageReceive")
    public Result<PageResult<SendMessageDto>> fetchData(@RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "10") Integer size) {
        PageResult<SendMessageDto> pageResult=getMessageService.receiveMessage(page,size);
        return Result.success(pageResult);
    }


    @Operation(summary = "根据病人id查询健康档案")
    @PostMapping("/api/getHealthRecord/{patientId}")
    public Result fetchHealthRecord(@PathVariable("patientId") Long patientId){
        GetHealthRecordsList healthRecordsList=getMessageService.fetchHealthRecords(patientId);
        return Result.success(healthRecordsList);
    }


    @Operation(summary = "通过病人id查询时间轴信息", description = "通过病人id查询时间轴信息",
            parameters = {
                    @Parameter(name = "patientId", description = "病人ID", required = true)
            })
    @PostMapping("/api/getTimeLine/{patientId}")
    public Result fetchTimeLine(@PathVariable("patientId") Long patientId){
        Map<String, TimeLine> map=getMessageService.fetchTimeLine(patientId);
        return Result.success(map);
    }

}


