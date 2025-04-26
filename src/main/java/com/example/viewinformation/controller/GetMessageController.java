package com.example.viewinformation.controller;

import com.example.viewinformation.pojo.HealthRecordDto;
import com.example.viewinformation.pojo.SendMessageVo;
import com.example.viewinformation.pojo.TimeLine;
import com.example.viewinformation.result.PageResult;
import com.example.viewinformation.result.Result;
import com.example.viewinformation.service.GetMessageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Map;


@RestController
@Slf4j
@Tag(name= "查询病例信息")
public class GetMessageController {
    @Autowired
    private GetMessageService getMessageService;

    /**
     *   全局查询
     * @param
     * @return
    */
    @Operation( summary = "全局查询病人病例信息",description = "分页查询病人病例信息")
    @PostMapping("/api/messageReceive")
    public Result<PageResult<SendMessageVo>> fetchData(@RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "10") Integer size) {
        PageResult<SendMessageVo> pageResult=getMessageService.receiveMessage(page,size);
        return Result.success(pageResult);
    }

    /**
     *   增量查询
     * @param
     * @return
    */
    @Operation( summary = "增量查询病人病例信息",description = "分页查询病人病例信息")
    @PostMapping("/api/messageReceiveAdd")
    public Result<PageResult<SendMessageVo>> fetchAddData(@RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "10") Integer size) {
        PageResult<SendMessageVo> pageResult=getMessageService.receiveMessageADD(page,size);
        return Result.success(pageResult);
    }


    /**
     *   根据病人id查询健康档案
     * @param
     * @return
    */
    @Operation(summary = "根据病人id查询健康档案")
    @PostMapping("/api/getHealthRecord/{patientId}")
    public Result fetchHealthRecord(@PathVariable("patientId") String  patientId){
        HealthRecordDto healthRecordsList=getMessageService.fetchHealthRecords(patientId);
        return Result.success(healthRecordsList);
    }


    /**
     *   通过病人id查询时间轴信息
     * @param
     * @return
    */
    @Operation(summary = "通过病人id查询时间轴信息", description = "通过病人id查询时间轴信息",
            parameters = {
                    @Parameter(name = "patientId", description = "病人ID", required = true)
            })
    @PostMapping("/api/getTimeLine/{patientId}")
    public Result fetchTimeLine(@PathVariable("patientId") String  patientId){
        Map<String, TimeLine> map=getMessageService.fetchTimeLine(patientId);
        return Result.success(map);
    }

    /**
     *   前端导出下载excel
     * @param
     * @return
    */
    @GetMapping("/api/download-excel")
    public Result downloadExcel(HttpServletResponse response) {
       getMessageService.exportExcelOnline(response);
       return Result.success();
    }
}


