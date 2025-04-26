package com.example.viewinformation.pojo;


import java.io.Serializable;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 基层医生信息表
 * </p>
 *
 * @author lyh
 * @since 2024-10-26
 */
@Data
public class DoctorInfo implements Serializable{

    private static final long serialVersionUID = 1L;

    @Schema(description = "基层医生主键，雪花算法")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long doctorId;

    @Schema(description = "基层医生姓名，数据库内加密")
    private String doctorName;

    @Schema(description = "基层医生手机号，数据库内加密")
    private String doctorContact;

    @Schema(description = "基层医生工号，数据库内加密")
    private String doctorWorkId;

    @Schema(description = "基层医生简介，100字以内，数据库内加密")
    private String doctorOmitted;

    @Schema(description = "open_id")
    private String doctorOpenId;

    @Schema(description = "基层医生单位主键，普通医院主键")
    private Long doctorBelonging;

    @Schema(description = "基层医生单位名称")
    private String doctorBelongingName;

    @Schema(description = "基层医生工作状态：0无法参与急救/1可以参与急救")
    private Integer doctorStatus;

    @Schema(description = "注册状态：0注册待审核/1注册成功")
    private Integer registerStatus;

}
