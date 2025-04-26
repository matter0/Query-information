package com.example.viewinformation.pojo;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
@Data
public class DoctorInChargeInfo implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id; // 主键 ID
    private LocalDateTime createTime; // 创建时间
    private LocalDateTime modifyTime; // 修改时间
    private Long createdUsrId; // 创建用户 ID
    private String doctorId; // 医生 ID
    private String doctorName; // 医生姓名
    private String doctorContact; // 医生联系方式
    private String doctorWorkId; // 医生工号
    private String doctorOmitted; // 医生省略信息（可为空）
    private String doctorOpenId; // 医生 OpenID
    private Long doctorBelonging; // 医生所属机构 ID
    private String doctorBelongingName; // 医生所属机构名称
    private Integer doctorStatus; // 医生状态
    private Integer registerStatus; // 注册状态（可为空）
}
