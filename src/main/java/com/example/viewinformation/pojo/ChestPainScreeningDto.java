package com.example.viewinformation.pojo;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 封装从http://localhost:2308/api/common/chest-pain-screening/getChestPainScreeningList接口查询的数据
 * @param
 * @return
*/

@Data
public class ChestPainScreeningDto implements Serializable {

        // 基础信息
        private LocalDateTime createTime;          // 创建时间 "2025-03-13 10:17:11"
        private LocalDateTime modifyTime;         // 修改时间 "2025-03-13 10:23:26"
        private Long createdUsrId;       // 创建用户ID 123456

        // 患者信息
        private String patientId;        // 患者ID "5001"
        private String patientName;      // 患者姓名 "李四"
        private String gender;           // 性别 "男"
        private Integer age;             // 年龄 45

        // 检验指标
        private Double serumTroponin;    // 血清肌钙蛋白 0.03
        private Double serumMyoglobin;   // 血清肌红蛋白 85.5
        private Double proBnp;           // 脑钠肽前体 1250.7

        // 文件信息
        private String  filePaths;        // 图片数据

        // 诊断结论
        private String conclusion;       // 结论(包含异常信息和部位描述)
    }

