package com.example.viewinformation.pojo;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class AmbulancesInfo implements Serializable {
    // 任务创建时间
    private LocalDateTime createTime;

    // 任务修改时间
    private LocalDateTime modifyTime;

    // 创建用户ID
    private Long createdUsrId;

    // 任务ID
    private String taskId;

    // 救护车ID
    private String ambulanceId;

    // 车辆ID
    private String carId;

    // 用户OpenID
    private String openId;

    // 任务状态
    private Integer status;

    // 任务发生地点
    private String location;

    // 电话号码
    private String telphoneNumber;

    // 负责人姓名
    private String chargeName;

    // 工作ID
    private String workId;

    // 备用电话号码
    private String telphoneNumberBackup;

    // 所属单位ID
    private String belongingId;

    // 所属单位名称
    private String belongingName;

    // PCI所属单位ID
    private String pciBelongingId;

    // PCI所属单位名称
    private String pciBelongingName;

    // 是否有摄像头
    private String ifCamera;

    // 是否有心电图设备
    private String ifEcg;

    // GPS丢失次数
    private Integer gpsMissingNumber;

    // 离线时间
    private LocalDateTime offlineTime;
}
