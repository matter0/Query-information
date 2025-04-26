package com.example.viewinformation.pojo;

import com.example.viewinformation.utils.ExcelExport;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.models.auth.In;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 封装从 http://localhost:2308/api/common/health-records/getHealthRecordsList 查询的数据
 *
 * @param
 * @return
 */
@Data
public class HealthRecord implements Serializable {
    // ============= 基础信息 =============
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ExcelExport(value = "创建时间")
    private LocalDateTime createTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ExcelExport(value = "修改时间")
    private LocalDateTime modifyTime;

    // ============= 患者基本信息 =============

    @ExcelExport(value = "患者编号，使用二维码")
    private String patientNumber;

    @ExcelExport(value = "患者姓名")
    private String patientName;

    @ExcelExport(value = "患者性别", kv = "1-男;2-女")
    private Integer gender;

    @ExcelExport(value = "年龄")
    private Integer age;

    @ExcelExport(value = "联系人")
    private String contact;

    @ExcelExport(value = "联系电话")
    private String telephoneNumber;

    @ExcelExport(value = "住院号")
    private String admissionNumber;

    @ExcelExport(value = "身份证号")
    private String id;

    @ExcelExport(value = "入选胸痛/入选心梗", kv = "1-胸痛;2-心梗")
    private Integer chestpainOrHeartattack;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ExcelExport(value = "发病时间")
    private LocalDateTime diseaseTime;

    @ExcelExport(value = "精确到分钟", kv = "0-否;1-是")
    private Integer ifAccurate;

    @ExcelExport(value = "有无呼救", kv = "0-否;1-是")
    private Integer ifHelp;

    @ExcelExport(value = "发病地址", kv = "1-家中;2-医疗机构;3-公共场合")
    private Integer diseaseAddress;

    @ExcelExport(value = "来院方式")
    private String source;

    @ExcelExport(value = "患者地址")
    private String patientAddress;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ExcelExport(value = "院内首次治疗时间")
    private LocalDateTime firstTreatTimeHstpt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ExcelExport(value = "院内首次心电图完成时间")
    private LocalDateTime firstEcgTimeHstpt;

    @ExcelExport(value = "心电图路径")
    private String firstEcgPathHstpt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ExcelExport(value = "首份心电图确诊时间")
    private LocalDateTime firstEcgDefineTime;

    @ExcelExport(value = "远程心电传输", kv = "0-否;1-是")
    private Integer remoteEcg;

    @ExcelExport(value = "发病时长，分钟")
    private String diseaseDuration;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ExcelExport(value = "知情同意时间")
    private LocalDateTime informedConsentTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ExcelExport(value = "离开急诊时间")
    private LocalDateTime departureTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ExcelExport(value = "出生日期")
    private LocalDateTime birthday;

    @ExcelExport(value = "病案号")
    private String medicalRecordNumber;

    @ExcelExport(value = "家庭地址")
    private String homeAddress;

    @ExcelExport(value = "患者本人手机号码")
    private String patientTelphone;

    @ExcelExport(value = "医生姓名")
    private String doctorName;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ExcelExport(value = "到院时间，到哪个医院待定")
    private LocalDateTime arriveHstptTime;

    @ExcelExport(value = "到院时间是否估计", kv = "0-否;1-是")
    private Integer ifArriveHstptTimeEstimate;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ExcelExport(value = "入院时间")
    private LocalDateTime inHstptTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ExcelExport(value = "出院时间")
    private LocalDateTime outHstptTime;

    @ExcelExport(value = "主要诊断", kv = "1-STEMI;2-NSTEMI;3-UA;4-主动脉夹层;5-肺动脉栓塞;6-非ACS心源性胸痛;7-其他非心源性胸痛;8-待查")
    private Integer mainDiagnosis;

    @ExcelExport(value = "入院24小时内使用阿司匹林(包括起病后院前)", kv = "0-否;1-是")
    private Integer ifUsedAspirin;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ExcelExport(value = "首次阿司匹林使用时间")
    private LocalDateTime firstAspirinTime;

    @ExcelExport(value = "入院24小时内使用P2Y12受体拮抗剂(包括起病后院前)", kv = "0-否;1-是")
    private Integer ifP2y12Used;

    @ExcelExport(value = "入院24小时内使用B受体拮抗剂(包括起病后院前)", kv = "0-否;1-是")
    private Integer ifBetaUsed;

    @ExcelExport(value = "院前再灌注治疗", kv = "0-否;1-是")
    private Integer beforeReperfusion;

    @ExcelExport(value = "到院时再灌注治疗指征", kv = "1-具备;2-不具备(STEMI诊断不明确);3-不具备(胸痛缓解);4-不具备(ST段回落);5-不具备(其他原因);6-不具备(死亡)")
    private Integer beforeReperfusionTarget;

    @ExcelExport(value = "院内实施再灌注治疗")
    private String betweenReperfusion;

    @ExcelExport(value = "直接PCI时间记录", kv = "1-球囊扩张时间;2-导丝通过时间;3-血管穿刺时间;4-手术开始时间")
    private Integer pciTimeRecorder;

    @ExcelExport(value = "患者转运至有PCI能力医院")
    private String transferToPci;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ExcelExport(value = "启动转运时间")
    private LocalDateTime transferStartTime;

    @ExcelExport(value = "心脏超声评价LVEF(左室射血分数)检查", kv = "0-否;1-是")
    private Integer lvefCheck;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ExcelExport(value = "首次心脏超声评价LVEF时间")
    private LocalDateTime firstLvefTime;

    @ExcelExport(value = "住院期间用药阿司匹林")
    private String aspirinUsedDuringHstps;

    @ExcelExport(value = "住院期间用药P2Y12受体拮抗剂", kv = "1-阿司匹林;2-氯吡格雷;3-替格瑞洛;4-普拉格雷;5-西洛他唑;6-吲哚布芬;7-其他")
    private Integer p2y12UsedDuringHstpt;

    @ExcelExport(value = "住院期间用药B受体拮抗剂", kv = "1-酒石酸美托洛尔;2-琥珀酸美托洛尔;3-比索洛尔;4-阿替洛尔;5-普萘洛尔;6-奈必洛尔;7-卡维地洛;8-其他")
    private Integer betaUsedDuringHstpt;

    @ExcelExport(value = "住院期间用药ACEI/ARB", kv = "1-贝那普利;2-依那普利;3-赖诺普利;4-卡托普利;5-福辛普利;6-莫西普利;7-培哚普利;8-雷米普利;9-缬沙坦;10-氯沙坦;11-厄贝沙坦;12-坎地沙坦;13-替米沙坦;14-奥美沙坦;15-沙库巴曲缬沙坦钠;16-单片复方制剂;17-其他(单药)")
    private Integer aaUsedDuringHstpt;

    @ExcelExport(value = "住院期间用药他汀", kv = "1-阿托伐他汀;2-瑞舒伐他汀;3-洛伐他汀;4-普伐他汀;5-辛伐他汀;6-氟伐他汀;7-匹伐他汀;8-依洛尤单抗;9-依折麦布;10-阿里西尤单抗;11-英克司兰;12-其他")
    private Integer statinUsedDuringHstpt;

    @ExcelExport(value = "出院带药阿司匹林")
    private String medicationDischargeAspirin;

    @ExcelExport(value = "出院应用P2Y12受体拮抗剂", kv = "1-阿司匹林;2-氯吡格雷;3-替格瑞洛;4-普拉格雷;5-西洛他唑;6-吲哚布芬;7-其他")
    private Integer medicationDischargeP2y12;

    @ExcelExport(value = "出院应用ACEI/ARB", kv = "1-贝那普利;2-依那普利;3-赖诺普利;4-卡托普利;5-福辛普利;6-莫西普利;7-培哚普利;8-雷米普利;9-缬沙坦;10-氯沙坦;11-厄贝沙坦;12-坎地沙坦;13-替米沙坦;14-奥美沙坦;15-沙库巴曲缬沙坦钠;16-单片复方制剂;17-其他(单药)")
    private Integer medicationDischargeAa;

    @ExcelExport(value = "出院应用B受体拮抗剂", kv = "1-酒石酸美托洛尔;2-琥珀酸美托洛尔;3-比索洛尔;4-阿替洛尔;5-普萘洛尔;6-奈必洛尔;7-卡维地洛;8-其他")
    private Integer medicationDischargeBeta;

    @ExcelExport(value = "出院应用他汀", kv = "1-阿托伐他汀;2-瑞舒伐他汀;3-洛伐他汀;4-普伐他汀;5-辛伐他汀;6-氟伐他汀;7-匹伐他汀;8-依洛尤单抗;9-依折麦布;10-阿里西尤单抗;11-英克司兰;12-其他")
    private Integer medicationDischargeStatin;

    @ExcelExport(value = "醛固酮受体拮抗剂适应证")
    private String aldosteroneFitSymptom;

    @ExcelExport(value = "出院应用醛固酮受体拮抗剂", kv = "1-螺内酯;2-非奈利酮;3-依普利酮")
    private Integer aldosteroneDischarge;

    @ExcelExport(value = "出院健康教育")
    private String outHstptEducation;

    @ExcelExport(value = "出院后30天内非计划内全因再入院")
    private String backAllcause;

    @ExcelExport(value = "首诊30天内全因死亡")
    private String firstDeadAllcause;

    @ExcelExport(value = "出院应用PCSK9抑制剂")
    private String firstCardiacAllcause;

    @ExcelExport(value = "住院结局", kv = "1-康复;2-死亡")
    private Integer hstptOutcome;

    @ExcelExport(value = "住院期间医疗费用，单位：元")
    private Double hstptDurationCost;

    @ExcelExport(value = "患者要去的医院是否在候选的医院里", kv = "0-否;1-是")
    private Integer ifAmongCandidates;

    @ExcelExport(value = "患者要去的医院")
    private String aimedHstpt;

    @ExcelExport(value = "最终患者选择的医院")
    private String arriveHstpt;

    @ExcelExport(value = "候选医院")
    private String candidateHstpt;

    @ExcelExport(value = "医院是否接受患者", kv = "0-否;1-是")
    private Integer ifAccept;

    @ExcelExport(value = "想要去的医院的联系电话")
    private String candidateHstptTelnumber;

    @ExcelExport(value = "想要去的医院的地址")
    private String candidateHstptLocation;

    @ExcelExport(value = "转移担保人，身份证(家属或本人)")
    private String transferGuarantor;

    @ExcelExport(value = "转移状态")
    private String transferStatus;

    @ExcelExport(value = "初步诊断")
    private String primaryDiagnosis;

    @ExcelExport(value = "首次抗血小板给药时间")
    private String antiplateletAdministrationTime;

    @ExcelExport(value = "阿司匹林剂量，单位：mg")
    private String aspirinDose;

    @ExcelExport(value = "用药类型", kv = "1-氯吡格雷;2-替格瑞洛")
    private Integer medicationType;

    @ExcelExport(value = "用药剂量，单位：mg")
    private String medicationDose;

    @ExcelExport(value = "溶栓核查", kv = "1-直接PCI;2-急诊仅造影;3-择期PCI;4-转运PCI;5-无再灌注措施;6-CABG;7-仅药物治疗(收入院)")
    private Integer thrombolysisVerification;

    @ExcelExport(value = "患者转归", kv = "1-出院;2-转院;3-死亡")
    private Integer patientOutcome;

    @ExcelExport(value = "出院诊断", kv = "1-STEMI;2-NSTEMI;3-UA;4-主动脉夹层;5-肺动脉栓塞;6-非ACS心源性胸痛;7-其他非心源性胸痛")
    private Integer dischargeDiagnosis;

    @ExcelExport(value = "住院天数，单位：天")
    private Integer hstptDays;

    @ExcelExport(value = "总费用，单位：元")
    private Double totalCost;

    @ExcelExport(value = "心率，单位：次/分钟")
    private String heartRate;

    @ExcelExport(value = "血压，格式：高压/低压 mmHg")
    private String bloodPressure;

    @ExcelExport(value = "指脉氧")
    private String pulseOximetry;

    @ExcelExport(value = "呼吸频率")
    private String respiratoryRate;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ExcelExport(value = "肌钙蛋白抽血时间")
    private LocalDateTime troponinBloodDrawTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ExcelExport(value = "肌钙蛋白报告时间")
    private LocalDateTime troponinReportTime;

    @ExcelExport(value = " 心肌梗死处：mmol/L")
    private String ldlCheck;

    @ExcelExport(value = "cTNi数值，单位：ng/ml")
    private String ctniNum;

    @ExcelExport(value = "cTNi结果", kv = "1-阳性;2-阴性")
    private Integer ctniResult;

    @ExcelExport(value = "cTnT数值，单位：ng/ml")
    private String ctntNum;

    @ExcelExport(value = "cTnT结果", kv = "1-阳性;2-阴性")
    private Integer ctntResult;

    @ExcelExport(value = "ct")
    private String ct;

    @ExcelExport(value = "彩超", kv = "0-否;1-是")
    private Integer colorB;

    @ExcelExport(value = "左室射血", kv = "0-否;1-是")
    private Integer leftEjection;

    @ExcelExport(value = "出院应用PCSK9抑制剂")
    private String medicationDischargePcsk9;

    @ExcelExport(value = "ACEI/ARB单次剂量  mg")
    private String medicationDischargeAaSingledose;

    @ExcelExport(value = "ACEI/ARB  剂量/频次  ")
    private String medicationDischargeAaRate;

    @ExcelExport(value = "抗血小板药物  剂量/频次 ")
    private String medicationDischargeP2y12Rate;

    @ExcelExport(value = "抗血小板药物单次剂量  mg")
    private String medicationDischargeP2y12Singledose;

    @ExcelExport(value = "调脂药物单次剂量  mg")
    private String medicationDischargeStatinSingledose;

    @ExcelExport(value = "调脂药物  剂量/频次  ")
    private String medicationDischargeStatinRate;

    @ExcelExport(value = "B受体阻阻滞剂单次剂量  mg")
    private String medicationDischargeBetaSingledose;

    @ExcelExport(value = "B受体阻阻滞剂  剂量/频次  ")
    private String medicationDischargeBetaRate;

    @ExcelExport(value = "PCSK9抑制剂单次剂量  mg")
    private String medicationDischargePcsk9Singledose;

    @ExcelExport(value = "PCSK9抑制剂  剂量/频次  ")
    private String medicationDischargePcsk9Rate;

    @ExcelExport(value = "阿司匹林单次剂量  mg")
    private String medicationDischargeAspirinSingledose;

    @ExcelExport(value = "阿司匹林  剂量/频次  ")
    private String medicationDischargeAspirinRate;
}