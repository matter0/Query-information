package com.example.viewinformation.pojo;

import lombok.Data;

import java.io.Serializable;

@Data
public class Task implements Serializable {
    private Long hospitalDoctorId; // 医生ID
    private Long patientId; // 患者ID
    private Long groupId; // 组ID
    private String modifyTime; // 修改时间
    private Long hstptId; // 医院ID
    private String hstptName; // 医院名称
    private String hospitalDoctorName; // 医生姓名
    private String startTime; // 开始时间
    private Long createdUsrId; // 创建者ID
    private String createTime; // 创建时间
    private Long cpcId; // CPC ID
    private Long cpcUsrId; // CPC 用户ID
    private String location; // 地址
    private Long taskId; // 任务ID
    private String cpcName; // CPC 名称
    private int status; // 状态（0 表示正常）
}
