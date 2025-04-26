package com.example.viewinformation.pojo;

import com.example.viewinformation.utils.ExcelExport;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class MedicalRecordDto implements Serializable {
        private LocalDateTime createTime; // 创建时间
        private LocalDateTime modifyTime; // 修改时间
        private String createdUsrId; // 创建用户ID
        private String id; // 记录ID
        private String taskId; // 任务ID

        @ExcelExport(value = "角色所属的医院或救护车id")
        private Long roleId;

        @ExcelExport(value = "角色 (救治方) 属于什么，例如基层医院，救护车，pci,不同部门")
        private String roleName;

        @ExcelExport(value = "操作人id")
        private Long operatorId;

        @ExcelExport(value = "操作人姓名")
        private String operatorName;

        @ExcelExport(value = "用户类型")
        private Integer operatorType;

        @ExcelExport(value = "具体操作方式", kv = "1-口服;2-注射;3-其他")
        private Integer specificOperation;

        @ExcelExport(value = "具体操作")
        private String operationContent; // 修正拼写错误

        @ExcelExport(value = "操作时间")
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
        private LocalDateTime operationTime;

        @ExcelExport(value = "病人id")
        private Long patientId;
}    