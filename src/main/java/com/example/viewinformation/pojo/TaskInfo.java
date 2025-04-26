package com.example.viewinformation.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class TaskInfo implements Serializable {
    // 创建时间
    @JsonProperty("createTime")
    private LocalDateTime createTime;

    // 修改时间
    @JsonProperty("modifyTime")
    private LocalDateTime modifyTime;

    // 创建用户ID
    @JsonProperty("createdUsrId")
    private Long createdUsrId;

    // 任务ID
    @JsonProperty("taskId")
    private String taskId;

    // 救护车ID
    @JsonProperty("ambulanceId")
    private String ambulanceId;

    // 患者ID
    @JsonProperty("patientId")
    private String patientId;

    // 患者姓名
    @JsonProperty("patientName")
    private String patientName;

    // 胸痛中心ID
    @JsonProperty("cpcId")
    private String cpcId;

    // 胸痛中心名称
    @JsonProperty("cpcName")
    private String cpcName;

    // 医院ID
    @JsonProperty("hstptId")
    private String hstptId;

    // 医院名称
    @JsonProperty("hstptName")
    private String hstptName;

    // 经皮冠状动脉介入治疗ID
    @JsonProperty("pciId")
    private String pciId;

    // 经皮冠状动脉介入治疗名称
    @JsonProperty("pciName")
    private String pciName;

    // 胸痛中心用户ID
    @JsonProperty("cpcUsrId")
    private String cpcUsrId;

    // 位置
    @JsonProperty("location")
    private String location;

    // 结束时间
    @JsonProperty("endTime")
    private LocalDateTime endTime;

    // 开始时间
    @JsonProperty("startTime")
    private LocalDateTime startTime;

    // 是否接受救护车
    @JsonProperty("ifAmbulanceAccept")
    private Boolean ifAmbulanceAccept;

    // 是否接受PCI
    @JsonProperty("ifPciAccept")
    private Boolean ifPciAccept;

    // 目标医院
    @JsonProperty("aimedHstpt")
    private String aimedHstpt;

    // 溶栓状态
    @JsonProperty("thrombolysisStatus")
    private String thrombolysisStatus;

    // PCI医生ID
    @JsonProperty("pciDoctorId")
    private String pciDoctorId;

    // PCI医生姓名
    @JsonProperty("pciDoctorName")
    private String pciDoctorName;

    // 预计时间
    @JsonProperty("estimatedTime")
    private LocalDateTime estimatedTime;

    // 审核人ID
    @JsonProperty("auditorId")
    private Long auditorId;

    // 胸痛中心用户名
    @JsonProperty("cpcUsrName")
    private String cpcUsrName;

    // 医院医生ID
    @JsonProperty("hospitalDoctorId")
    private String hospitalDoctorId;

    // 医院医生姓名
    @JsonProperty("hospitalDoctorName")
    private String hospitalDoctorName;

    // 状态
    @JsonProperty("status")
    private Integer status;

    // 分组ID
    @JsonProperty("groupId")
    private String groupId;

    // 是否成功
    @JsonProperty("isSuccess")
    private Boolean isSuccess;

    // 审核原因
    @JsonProperty("auditReason")
    private String auditReason;

    // 负责人姓名
    @JsonProperty("chargeName")
    private String chargeName;

    // 是否强制结束
    @JsonProperty("isForcedEnd")
    private Boolean isForcedEnd;

    // 强制结束图片
    @JsonProperty("forcedEndPic")
    private String forcedEndPic;

    // 强制结束人姓名
    @JsonProperty("forcedEndName")
    private String forcedEndName;

    // 强制结束人电话号码
    @JsonProperty("forcedEndTelnumber")
    private String forcedEndTelnumber;

    // 强制结束人用户ID
    @JsonProperty("forcedEndUsrId")
    private Long forcedEndUsrId;

}
