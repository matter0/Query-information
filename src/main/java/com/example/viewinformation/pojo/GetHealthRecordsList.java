package com.example.viewinformation.pojo;

import lombok.Data;

import java.time.LocalDateTime;
/**
 *   封装从http://localhost:2308/api/common/health-records/getHealthRecordsList查询的数据
 * @param
 * @return
 */
@Data
public class GetHealthRecordsList {
    // ============= 基础信息 =============
    private LocalDateTime createTime;                  // 创建时间
    private LocalDateTime modifyTime;                 // 修改时间
    private Long createdUsrId;                        // 创建用户ID

    // ============= 患者基本信息 =============
    private String patientId;                         // 患者ID
    private String patientNumber;                     // 患者编号
    private String patientName;                       // 患者姓名
    private String gender;                            // 性别
    private Integer age;                              // 年龄
    private String contact;                           // 联系人
    private String telephoneNumber;                   // 电话号码
    private String admissionNumber;                   // 入院编号
    private String id;                                // ID
    private LocalDateTime birthday;                   // 出生日期
    private String medicalRecordNumber;               // 病历号
    private String homeAddress;                       // 家庭地址
    private String patientTelphone;                   // 患者电话

    // ============= 疾病与症状信息 =============
    private String chestpainOrHeartattack;           // 胸痛或心脏病发作
    private LocalDateTime diseaseTime;                // 发病时间
    private String ifAccurate;                       // 是否准确
    private String ifHelp;                           // 是否需要帮助
    private String diseaseAddress;                    // 发病地点
    private String source;                            // 来源
    private String patientAddress;                    // 患者地址
    private String diseaseDuration;                   // 病程

    // ============= 心电图相关 =============
    private LocalDateTime firstTreatTimeHstpt;        // 首次治疗时间
    private LocalDateTime firstEcgTimeHstpt;          // 首次心电图时间
    private String firstEcgPathHstpt;                 // 首次心电图路径
    private LocalDateTime firstEcgDefineTime;         // 首次心电图确诊时间
    private String remoteEcg;                         // 远程心电图

    // ============= 时间节点信息 =============
    private LocalDateTime informedConsentTime;        // 知情同意时间
    private LocalDateTime departureTime;              // 离开时间
    private LocalDateTime arriveHstptTime;            // 到达医院时间
    private Boolean ifArriveHstptTimeEstimate;        // 是否估计到达时间
    private LocalDateTime inHstptTime;                // 入院时间
    private LocalDateTime outHstptTime;               // 出院时间

    // ============= 生命体征 =============
    private String arriveShock;                      // 到达时是否休克
    private String arriveArrest;                     // 到达时是否心脏骤停
    private String arriveFirstPressure;               // 到达时首次血压
    private Integer heartRate;                        // 心率
    private String bloodPressure;                     // 血压
    private Integer pulseOximetry;                    // 血氧饱和度
    private Integer respiratoryRate;                  // 呼吸频率

    // ============= 诊断信息 =============
    private String mainDiagnosis;                     // 主要诊断
    private String primaryDiagnosis;                  // 初步诊断
    private String dischargeDiagnosis;                // 出院诊断
    private String patientOutcome;                    // 患者结果

    // ============= 用药信息 =============
    private String ifUsedAspirin;                    // 是否使用阿司匹林
    private LocalDateTime firstAspirinTime;           // 首次阿司匹林时间
    private String ifP2y12Used;                      // 是否使用P2Y12抑制剂
    private String ifBetaUsed;                       // 是否使用β受体阻滞剂
    private String beforeReperfusion;                 // 再灌注前用药
    private String beforeReperfusionTarget;           // 再灌注前目标
    private String betweenReperfusion;                // 再灌注间期

    // ============= PCI信息 =============
    private String pciTimeRecorder;                   // PCI时间记录
    private String transferToPci;                    // 是否转PCI
    private LocalDateTime transferStartTime;          // 转运开始时间

    // ============= 检查检验信息 =============
    private String lvefCheck;                        // LVEF检查
    private LocalDateTime firstLvefTime;              // 首次LVEF时间
    private LocalDateTime troponinBloodDrawTime;      // 肌钙蛋白抽血时间
    private LocalDateTime troponinReportTime;         // 肌钙蛋白报告时间
    private String ldlCheck;                         // LDL检查
    private String ctniNum;                           // cTnI数值
    private String ctniResult;                        // cTnI结果
    private String ctntNum;                           // cTnT数值
    private String ctntResult;                        // cTnT结果
    private String ct;                                // CT检查
    private String colorB;                            // 彩色B超
    private String leftEjection;                      // 左室射血分数

    // ============= 住院期间用药 =============
    private String aspirinUsedDuringHstps;           // 住院期间阿司匹林使用
    private String p2y12UsedDuringHstpt;            // 住院期间P2Y12使用
    private String betaUsedDuringHstpt;              // 住院期间β阻滞剂使用
    private String aaUsedDuringHstpt;                // 住院期间抗心律失常药使用
    private String statinUsedDuringHstpt;            // 住院期间他汀使用

    // ============= 出院用药 =============
    private String medicationDischargeAspirin;       // 出院阿司匹林
    private String medicationDischargeP2y12;         // 出院P2Y12
    private String medicationDischargeAa;            // 出院抗心律失常药
    private String medicationDischargeBeta;          // 出院β阻滞剂
    private String medicationDischargeStatin;        // 出院他汀
    private String medicationDischargePcsk9;         // 出院PCSK9抑制剂

    // ============= 出院用药剂量 =============
    private String medicationDischargeAaSingledose;   // 抗心律失常药单剂量
    private String medicationDischargeAaRate;         // 抗心律失常药频率
    private String medicationDischargeP2y12Rate;      // P2Y12频率
    private String medicationDischargeP2y12Singledose;// P2Y12单剂量
    private String medicationDischargeStatinSingledose;// 他汀单剂量
    private String medicationDischargeStatinRate;     // 他汀频率
    private String medicationDischargeBetaSingledose; // β阻滞剂单剂量
    private String medicationDischargeBetaRate;       // β阻滞剂频率
    private String medicationDischargePcsk9Singledose;// PCSK9单剂量
    private String medicationDischargePcsk9Rate;      // PCSK9频率
    private String medicationDischargeAspirinSingledose;// 阿司匹林单剂量
    private String medicationDischargeAspirinRate;    // 阿司匹林频率

    // ============= 醛固酮相关 =============
    private String aldosteroneFitSymptom;            // 醛固酮适应症
    private String aldosteroneDischarge;             // 出院醛固酮

    // ============= 出院与随访 =============
    private String outHstptEducation;                // 出院教育
    private String backAllcause;                     // 全因再入院
    private String firstDeadAllcause;                // 全因首次死亡
    private String firstCardiacAllcause;             // 心脏原因首次死亡

    // ============= 医院信息 =============
    private String hstptOutcome;                      // 医院结果
    private Double hstptDurationCost;                 // 住院时长费用
    private String ifAmongCandidates;                // 是否候选
    private String aimedHstpt;                        // 目标医院
    private String arriveHstpt;                       // 到达医院
    private String candidateHstpt;                    // 候选医院
    private String ifAccept;                         // 是否接受
    private String candidateHstptTelnumber;           // 候选医院电话
    private String candidateHstptLocation;            // 候选医院位置

    // ============= 转运信息 =============
    private String transferGuarantor;                 // 转运担保人
    private String transferStatus;                    // 转运状态

    // ============= 其他信息 =============
    private String doctorName;                        // 医生姓名
    private String antiplateletAdministrationTime;    // 抗血小板给药时间
    private String aspirinDose;                       // 阿司匹林剂量
    private String medicationType;                    // 药物类型
    private String medicationDose;                    // 药物剂量
    private String thrombolysisVerification;          // 溶栓验证
    private Integer hstptDays;                        // 住院天数
    private Double totalCost;                         // 总费用
}
