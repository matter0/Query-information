package com.example.viewinformation.pojo;


import com.example.viewinformation.utils.ExcelExport;
import lombok.Data;

import java.io.Serializable;

@Data
public class StemiIndications  implements Serializable {

    @ExcelExport(value = "颅内出血史或未知部位脑卒中史",kv = "0-否;1-是;3-未选择")
    private Integer contraindications1;

    @ExcelExport(value = "近6月内有缺血性脑卒中发作",kv = "0-否;1-是;3-未选择")
    private Integer contraindications2;

    @ExcelExport(value = "中枢神经系统损伤、神经系统肿瘤或动静脉畸形",kv = "0-否;1-是;3-未选择")
    private Integer contraindications3;

    @ExcelExport(value = "近2个月出现过重大外伤、外科手术或头部损伤",kv = "0-否;1-是;3-未选择")
    private Integer contraindications4;

    @ExcelExport(value = "曾经有消化道大出血病史或目前有活动性消化道溃疡病",kv = "0-否;1-是;3-未选择")
    private Integer contraindications5;

    @ExcelExport(value = "各种血液病、出血性疾病或出血倾向者（月经除外）",kv = "0-否;1-是;3-未选择")
    private Integer contraindications6;

    @ExcelExport(value = "明确、高度怀疑或不能排除主动脉夹层",kv = "0-否;1-是;3-未选择")
    private Integer contraindications7;

    @ExcelExport(value = "感染性心内膜炎",kv = "0-否;1-是;3-未选择")
    private Integer contraindications8;

    @ExcelExport(value = "高血压患者经积极降压治疗后，血压仍≥180/110mmHg",kv = "0-否;1-是;3-未选择")
    private Integer contraindications9;

    @ExcelExport(value = "正在使用抗凝药物（如华法林或新型口服抗凝药）",kv = "0-否;1-是;3-未选择")
    private Integer contraindications10;

    @ExcelExport(value = "严重肝肾功能障碍、严重消耗状态或晚期恶性肿瘤等",kv = "0-否;1-是;3-未选择")
    private Integer contraindications11;

    @ExcelExport(value = "妊娠期女性",kv = "0-否;1-是;3-未选择")
    private Integer contraindications12;

    @ExcelExport(value = "长时间或有创复苏",kv = "0-否;1-是;3-未选择")
    private Integer contraindications13;

    @ExcelExport(value = "医师认为其他不适合静脉溶栓治疗的疾病及情况",kv = "0-否;1-是;3-未选择")
    private Integer contraindications14;

    @ExcelExport(value = "严重的持续性胸痛/胸闷发作≥30min",kv = "0-否;1-是;3-未选择")
    private Integer thrombolysis1;

    @ExcelExport(value = "相邻2个或更多导联ST段抬高在肢体导联≥0.1mV，胸前导联≥0.2mV；或新出现的完全性左（或右）束支传导阻滞",kv = "0-否;1-是;3-未选择")
    private Integer thrombolysis2;

    @ExcelExport(value = "年龄≤75岁",kv = "0-否;1-是;3-未选择")
    private Integer thrombolysis4;

    @ExcelExport(value = "发病时间≤12h",kv = "0-否;1-是;3-未选择")
    private Integer thrombolysis3;
    @ExcelExport(value = "患者和（或）家属签署知情同意书",kv = "0-否;1-是;3-未选择")
    private Integer contraindications15;
    @ExcelExport(value = "预计医疗接触至PCI导丝通过梗死相关动脉时间>120分钟",kv = "0-否;1-是;3-未选择")
    private Integer thrombolysis5;

}
