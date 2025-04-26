package com.example.viewinformation.pojo;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class SendMessageVo implements Serializable {
    private LocalDateTime createTime; // 创建时间
    private List<String > remoteEcg; // 远程心电图

    private String createName; // 创建者姓名
    private String createOrganization ; // 创建者单位
    private String createTelephoneNumber;//创建者联系电话

    private String patientName;//患者姓名
    private Integer patientGender;//患者性别
    private Integer patientAge;//患者年龄
    private String patientId;//患者id

    private String pciDoctorName;//pci医生信息
    private String pciDoctorTelphoneNumber;//pci医生电话
    private String pciDoctorBelongingName;//pci医生单位

    private String chargeName;//救护车医生姓名
    private String chargeTelphoneNumber;//救护车医生电话
    private String chargeBelongingName;//救护车医生所属单位

}
