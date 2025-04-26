package com.example.viewinformation.pojo;

import com.example.viewinformation.utils.ExcelExport;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class MedicalRecord implements Serializable {

        @ExcelExport(value = "角色 (救治方) 属于什么，例如基层医院，救护车，pci,不同部门")
        private String roleName;

        @ExcelExport(value = "操作人姓名")
        private String operatorName;

//        @ExcelExport(value = "具体操作方式", kv = "1-口服;2-注射;3-其他")
//        private Integer specificOperation;
@ExcelExport(value = "具体操作方式(合并)")
private String specificOperationMerged;

        @ExcelExport(value = "操作时间(合并)")
        private String operationTimeMerged;


        @ExcelExport(value = "具体操作")
        private String operationContent;

//        @ExcelExport(value = "操作时间")
//        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
//        private LocalDateTime operationTime;
}    