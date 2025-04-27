package com.example.viewinformation.controller;

import com.example.viewinformation.pojo.HealthRecordDto;
import com.example.viewinformation.pojo.SendMessageVo;
import com.example.viewinformation.pojo.TimeLine;
import com.example.viewinformation.result.PageResult;
import com.example.viewinformation.result.Result;
import com.example.viewinformation.service.GetMessageService;
import com.example.viewinformation.utils.ExcelUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
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
    public void downloadExcel(HttpServletResponse response) {
       getMessageService.exportExcelOnline(response);
    }
    /**
     *   向json文件中添加邮箱数据
     * @param
     * @return
    */
    @PostMapping("/api/addEmailInfo/{email}")
    public Result saveEmail(@PathVariable String email){
        ExcelUtils.saveEmails(email);
        log.info("添加邮箱账号：{}",email);
        return Result.success("添加账号成功");
    }
    /**
     *   查看json文件中的邮箱数据
     * @param
     * @return
    */
    @PostMapping("/api/selectEmailInfo")
    public Result selectEmail(){
        List<String> list=ExcelUtils.loadEmails();
        log.info("查看邮箱账号数据：{}",list);
        return Result.success(list);
    }
    /**
     *   删除指定邮箱数据
     * @param
     * @return
    */
    @PostMapping("/api/deleteEmailInfo/{email}")
    public Result delete(@PathVariable String email){
        ExcelUtils.deleteEmail(email);
        log.info("删除邮箱账号：{}",email);
        return Result.success("删除账号成功");
    }
}


