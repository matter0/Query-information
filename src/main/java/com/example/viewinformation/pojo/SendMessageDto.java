package com.example.viewinformation.pojo;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class SendMessageDto {
    private String createName; // 创建者姓名
    private LocalDateTime createTime; // 创建时间
//    private List<String > remoteEcg; // 远程心电图
    private String createOrganization ; // 创建者单位
    private String createTelephoneNumber;//创建者联系电话
    private String patientName;//患者姓名
    private String patientGender;//患者性别
    private Integer patientAge;//患者年龄
    private String patientId;//患者id



}
