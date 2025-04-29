package com.example.viewinformation.service.impl;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.TypeReference;

import com.example.viewinformation.pojo.*;
import com.example.viewinformation.result.PageResult;
import com.example.viewinformation.service.GetMessageService;
import com.example.viewinformation.utils.ExcelUtils;
import com.example.viewinformation.utils.HttpClientUtil;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;



import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.concurrent.*;
import java.util.stream.Collectors;


@Slf4j
@Service
public class GetMessageServiceImpl implements GetMessageService {
    private static List<LocalDateTime> alreadyExit=new CopyOnWriteArrayList<>();
    private static final Pattern CHINESE_PATTERN = Pattern.compile("^[\\u4e00-\\u9fa5]+$");
    //定义内部类，用于辅助实现根据创建时间排序功能
    public static class ExcelRow {
        public Doctor               doctor;
        public HealthRecord         health;
        public ChestPainScreening   chest;
        public MedicalRecord        medical;
        public StemiIndications     stemi;
        public LocalDateTime createTime;
    }
    //判断时间是否在dateTime时间点之前
    public static boolean isBefore(LocalDateTime dateTime) {
        // 创建目标日期：2025年3月18日 00:00:00
        LocalDateTime target = LocalDateTime.of(2025, 3, 1, 0, 0);
        return dateTime.isBefore(target);
    }
    /**
     *   判断是否是中国人的名字
     * @param
     * @return
     */
    public static boolean isChineseOnly(String input) {
        if (input == null || input.isEmpty()) {
            return false;
        }
        return CHINESE_PATTERN.matcher(input).matches();
    }
    private static final Set<String> TESTER_NAMES = new HashSet<>(Arrays.asList(
            "jcz", "lsq", "hzh", "jww", "ws", "贺子涵",
            "历业", "wx", "梁世奇", "mac17", "test",
            "关媛元", "张育力", "金成哲", "测试"
    ));
    /**
     * 判断给定字符串是否包含集合中的任意一个名字
     *
     * @param input 待检查的字符串
     * @return 如果包含集合中的任意一个名字，返回 true；否则返回 false
     */
    public static boolean containsTesterName(String input) {
        // 遍历集合中的每个名字
        for (String name : TESTER_NAMES) {
            // 检查输入字符串是否包含当前名字
            if (input.contains(name)) {
                return true; // 包含，返回 true
            }
        }
        return false; // 遍历完都没有找到匹配项，返回 false
    }
    // 配置为外部属性
    private static final String[] SERVICES = {"172.18.0.12:2308", "172.18.0.12:2316", "172.18.0.12:2324",
            "172.18.0.12:2332", "172.18.0.12:2340", "172.18.0.12:2348", "172.18.0.12:2356", "172.18.0.13:2364", "172.18.0.14:2372","172.18.0.14:2380","172.18.0.14:2388"};
//    private static final String[] SERVICES = {"10.88.0.4:2308", "10.88.0.4:2316", "10.88.0.4:2324",
//                    "10.88.0.4:2332", "10.88.0.4:2340", "10.88.0.4:2348", "10.88.0.4:2356", "10.88.0.4:2364", "10.88.0.4:2372","10.88.0.4:2380","10.88.0.4:2388"};
    /**
     *   全局查询
     * @param
     * @return
    */
    @Override
    public PageResult receiveMessage(Integer page, Integer size) {
        //每次查询之前都先清空Excel集合中的数据

        // 1. 收集所有服务的结果
        List<SendMessageVo> allDtos = new ArrayList<>();
        // 2. 顺序遍历每个服务并获取数据
        for (String sp : SERVICES) {
            try {
                List<SendMessageVo> serviceData = fetchFromService(sp);
                allDtos.addAll(serviceData);
            } catch (Exception e) {
                log.error("从服务 {} 抓取数据时发生异常", sp, e);
            }
        }
        // 3. 全局排序
        allDtos.sort(Comparator.comparing(SendMessageVo::getCreateTime)); // 正序（升序）

        log.info("总共拉取到 {} 条记录，准备分页", allDtos.size());

        // 4. 分页
        int start = (page - 1) * size;
        int end = Math.min(start + size, allDtos.size());
        List<SendMessageVo> pageList = start < allDtos.size()
                ? allDtos.subList(start, end)
                : Collections.emptyList();

        PageResult pr = new PageResult();
        pr.setTotal(allDtos.size());
        pr.setRecords(pageList);
        return pr;
    }
    /**
     * 单服务数据抓取与 DTO 组装
     */
    private List<SendMessageVo> fetchFromService(String sp) {
        List<SendMessageVo> list = new ArrayList<>();
        List<LocalDateTime> tempNew = new ArrayList<>();
        try {
            // 1) 拉取健康档案列表
            String tableDatas = HttpClientUtil.post(
                    "http://" + sp + "/api/common/health-records/getHealthRecordsList"
            );
            JSONObject json = JSONObject.parseObject(tableDatas);
            JSONArray dataArray = json.getJSONArray("data");
            List<HealthRecordDto> records =
                    dataArray.toJavaList(HealthRecordDto.class);
            log.info("{} 服务拉取到 {} 条健康记录", sp, records.size());

            // 2) 遍历每条记录并填充 DTO
            for (HealthRecordDto hr : records) {
                //判断是否是新增的量
                boolean exists = false;
                for (LocalDateTime t : alreadyExit) {
                    if (hr.getCreateTime().isEqual(t)) {
                        exists = true;
                    }
                }
                if (exists==false){
                    // 不在此处改 alreadyExit，而是收集到 tempNew
                    tempNew.add(hr.getCreateTime());
                }

                if (isBefore(hr.getCreateTime())) {
                    continue;
                }
                String patientName=hr.getPatientName();
                if (patientName!=null){
                    if (containsTesterName(patientName)
                            || !isChineseOnly(patientName)){
                        continue;
                    }
                }

                try {
                    SendMessageVo dto = buildDtoFor(hr, sp);
                    if (dto != null) {
                        list.add(dto);

                    }
                } catch (Exception e) {
                    log.error("处理患者 {} 时出错", hr.getPatientId(), e);
                }
            }
            // 循环外统一合并
            alreadyExit.addAll(tempNew);
        } catch (Exception e) {
            log.error("服务 {} 抓取基础数据失败", sp, e);
        }
        return list;
    }

    /**
     * 根据单条健康记录和服务地址，串行调用各接口，构建 SendMessageDto
     */
    private SendMessageVo buildDtoFor(HealthRecordDto hr, String sp) {
        SendMessageVo dto = new SendMessageVo();
        // --- 基本字段 ---
        dto.setPatientName(hr.getPatientName());
        dto.setPatientGender(hr.getGender());
        dto.setPatientAge(hr.getAge());
        dto.setPatientId(hr.getPatientId());
        dto.setCreateTime(hr.getCreateTime());
        // --- 拉取 Task ---
        String tasks = HttpClientUtil.post(
                "http://" + sp + "/api/common/task-info/getTaskInfoByPatientId?patient_id=" + hr.getPatientId()
        );
        JSONObject jo2 = JSONObject.parseObject(tasks);
        Task task = jo2.getObject("data", Task.class);

        // --- 拉取 TaskInfo ---
        TaskInfo taskInfo = new TaskInfo();
        if (task != null) {
            String ti = HttpClientUtil.post(
                    "http://" + sp + "/api/common/task-info/getTaskInfoById?task_id=" + task.getTaskId()
            );
            JSONObject jo3 = JSONObject.parseObject(ti);
            taskInfo = jo3.getObject("data", TaskInfo.class);
        }

        // --- 创建者信息 ---
        if (hr.getCreatedUsrId() != null) {
            String docInfo = HttpClientUtil.post(
                    "http://" + sp + "/api/common/doctor-info/getDoctorInfoById?doctor_info_id=" + hr.getCreatedUsrId()
            );
            JSONObject jo6 = JSONObject.parseObject(docInfo);
            DoctorInfo di = jo6.getObject("data", DoctorInfo.class);
            if (di != null && di.getDoctorName() != null) {
                if (containsTesterName(di.getDoctorName())
                        || !isChineseOnly(di.getDoctorName())){
                    return null;
                }
                dto.setCreateName(di.getDoctorName());
                dto.setCreateOrganization(di.getDoctorBelongingName());
                dto.setCreateTelephoneNumber(di.getDoctorContact());
            }
        }
        // --- PCI 医生信息 ---
        if (taskInfo != null && taskInfo.getPciDoctorId() != null) {
            if (taskInfo.getPciDoctorId()!=null){
                String pci = HttpClientUtil.post(
                        "http://" + sp + "/api/common/doctor-in-charge-info/getDoctorInChargeInfoById?doctor_in_charge_info_id="
                                + taskInfo.getPciDoctorId()
                );
                JSONObject jot = JSONObject.parseObject(pci);
                DoctorInChargeInfo dic = jot.getObject("data", DoctorInChargeInfo.class);
                if (dic != null&&dic.getDoctorName()!=null) {
                    if (containsTesterName(dic.getDoctorName())
                            || !isChineseOnly(dic.getDoctorName())){
                        return null;
                    }
                    dto.setPciDoctorName(dic.getDoctorName());
                    dto.setPciDoctorTelphoneNumber(dic.getDoctorContact());
                    dto.setPciDoctorBelongingName(dic.getDoctorBelongingName());
                }
            }
        }

        // --- 救护车负责人信息 ---
        if (taskInfo != null && taskInfo.getAmbulanceId() != null) {
            if (taskInfo.getAmbulanceId()!=null){
                String amb = HttpClientUtil.post(
                        "http://" + sp + "/api/common/ambulances-info/getAmbulancesInfoById?ambulances_info_id="
                                + taskInfo.getAmbulanceId()
                );
                JSONObject jo4 = JSONObject.parseObject(amb);
                AmbulancesInfo ai = jo4.getObject("data", AmbulancesInfo.class);
                if (ai != null&&ai.getChargeName()!=null) {
                    if (containsTesterName(ai.getChargeName())
                            || !isChineseOnly(ai.getChargeName())){
                        return null;
                    }
                    dto.setChargeName(ai.getChargeName());
                    dto.setChargeTelphoneNumber(ai.getTelphoneNumber());
                    dto.setChargeBelongingName(ai.getBelongingName());
                }
            }
        }

        // --- 心电图文件路径解析 ---
        String chest = HttpClientUtil.post(
                "http://" + sp + "/api/common/chest-pain-screening/getChestPainScreeningById?patient_id="
                        + hr.getPatientId()
        );
        JSONObject jon = JSONObject.parseObject(chest);
        ChestPainScreeningDto cps = jon.getObject("data", ChestPainScreeningDto.class);
        if (cps != null && cps.getFilePaths() != null) {
            List<String> uuids = new ArrayList<>();
            Matcher m = Pattern.compile(
                    "[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}"
            ).matcher(cps.getFilePaths());
            while (m.find()) uuids.add(m.group());
            List<String> urls = new ArrayList<>();
            String port = sp.split(":")[1];
            for (String uuid : uuids) {
                urls.add("https://www.rtxgzngl.cn:" + port + "/api/common/health_file/" + uuid + ".jpg");
            }
            dto.setRemoteEcg(urls);
        }
        return dto;
    }
    /**
    *   新增量查询
    * @param
    * @return
   */
    @Override
    public PageResult receiveMessageADD(Integer page,Integer size) {
    // 1. 创建线程池和 CompletionService
    ExecutorService executor = Executors.newFixedThreadPool(SERVICES.length);
    CompletionService<List<SendMessageVo>> completionService =
            new ExecutorCompletionService<>(executor);

    // 2. 为每个服务提交抓取任务
    for (String sp : SERVICES) {
        completionService.submit(() -> fetchFromServiceadd(sp));
    }

    // 3. 收集所有子任务结果
    List<SendMessageVo> allDtos = new ArrayList<>();
    for (int i = 0; i < SERVICES.length; i++) {
        try {
            Future<List<SendMessageVo>> future = completionService.take();
            allDtos.addAll(future.get());
        } catch (Exception e) {
            log.error("并发抓取服务数据时发生异常", e);
        }
    }
    executor.shutdown();

    // 4. 全局排序
    allDtos.sort(Comparator.comparing(SendMessageVo::getCreateTime)); // 正序（升序）
    log.info("总共拉取到 {} 条记录，准备分页", allDtos.size());

    // 5. 分页
    int start = (page - 1) * size;
    int end = Math.min(start + size, allDtos.size());
    List<SendMessageVo> pageList = start < allDtos.size()
            ? allDtos.subList(start, end)
            : Collections.emptyList();

    PageResult pr = new PageResult();
    pr.setTotal(allDtos.size());
    pr.setRecords(pageList);
    return pr;
}

    /**
     * 单服务数据抓取与 DTO 组装
     */
    private List<SendMessageVo> fetchFromServiceadd(String sp) {
        List<SendMessageVo> list = new ArrayList<>();
        List<LocalDateTime> tempNew = new ArrayList<>();
        try {
            // 1) 拉取健康档案列表
            String tableDatas = HttpClientUtil.post(
                    "http://" + sp + "/api/common/health-records/getHealthRecordsList"
            );
            JSONObject json = JSONObject.parseObject(tableDatas);
            JSONArray dataArray = json.getJSONArray("data");
            List<HealthRecordDto> records =
                    dataArray.toJavaList(HealthRecordDto.class);
            log.info("{} 服务拉取到 {} 条健康记录", sp, records.size());

            // 2) 遍历每条记录并填充 DTO
            for (HealthRecordDto hr : records) {
                //判断是否是新增的量
                boolean exists = false;
                for (LocalDateTime t : alreadyExit) {
                    if (hr.getCreateTime().isEqual(t)) {
                        exists = true;
                        break;
                    }
                }
                if (exists || isBefore(hr.getCreateTime())) {
                    continue;
                }
                // 不在此处改 alreadyExit，而是收集到 tempNew
                tempNew.add(hr.getCreateTime());

                try {
                    SendMessageVo dto = buildDtoFor(hr, sp);
                    if (dto != null) {
                        list.add(dto);
                    }
                } catch (Exception e) {
                    log.error("处理患者 {} 时出错", hr.getPatientId(), e);
                }

            }
            // 循环外统一合并
            alreadyExit.addAll(tempNew);
        } catch (Exception e) {
            log.error("服务 {} 抓取基础数据失败", sp, e);
        }
        return list;
    }

    /**
     *   根据患者id查询健康档案
     * @param
     * @return
     */
    @Override
    public HealthRecordDto fetchHealthRecords(String patientId) {
        HealthRecordDto healthRecord = new HealthRecordDto();
//        //构造redis中的key，命名healthRecord+patientId
//        String key="healthRecord"+patientId;
//        GetHealthRecord ReHealthRecord =(GetHealthRecord)redisTemplate.opsForValue().get(key);
//        //查询redis中是否存在list
//        if (ReHealthRecord!=null&&!Objects.isNull(ReHealthRecord)){
//            //如果存在直接返回，无需查询数据库
//            healthRecord=ReHealthRecord;
//            log.info("直接调用reids中的数据");
//        }else {
            String service = new String();
            log.info("开始遍历网络端口");
            outerLoop:
            for (String s : SERVICES) {
                log.info("尝试连接端口：{}", s);
                try {
                    String tableDatas = HttpClientUtil.post("http://" + s + "/api/common/health-records/getHealthRecordsList");
                    JSONObject jsonObject = JSONObject.parseObject(tableDatas);
                    if (jsonObject != null) {
                        JSONArray dataArray = jsonObject.getJSONArray("data");
                        List<HealthRecordDto> healthRecords = dataArray.toJavaList(HealthRecordDto.class);
                        for (HealthRecordDto healthRecorde : healthRecords) {
                            if (Objects.equals(healthRecorde.getPatientId(), patientId)) {
                                log.info("找到患者 {}，服务地址为：{}", patientId, s);
                                service = s;
                                break outerLoop;
                            }
                        }
                    }
                } catch (Exception e) {
                    log.warn("请求端口 {} 失败：{}", s, e.getMessage());
                }
            }
            log.info("端口号为：{}", service);
            if (service != null && service != "") {
                try {
                    String tableDatas = HttpClientUtil.post("http://" + service + "/api/common/health-records/getHealthRecordsById?patient_id=" + patientId);
                    JSONObject jsonObject = JSONObject.parseObject(tableDatas);
                    if (jsonObject != null) {
                        healthRecord = jsonObject.getObject("data", HealthRecordDto.class);
                        log.info("成功查询患者{}的健康档案", patientId);
                    } else {
                        log.warn("患者{}的健康档案数据为空", patientId);
                    }
                } catch (Exception e) {
                    log.error("查询患者{}健康档案时发生异常", patientId, e);
                }
            }
//            //如果不存在，查询接口后，将查询到的数据放入redis中
//            redisTemplate.opsForValue().set(key,healthRecord);
//            log.info("已经将SendMessageDtos传入redis中");
//        }
        return healthRecord;
    }
    /**
     *   查询时间轴信息
     * @param patientId
     * @return TimeLine
     */
    @Override
    public Map<String, TimeLine> fetchTimeLine(String patientId) {
        Map<String,TimeLine> dataMap=new HashMap<>();
            String service = new String();
            log.info("开始遍历网络端口");
            outerLoop:
            for (String s : SERVICES) {
                log.info("尝试连接端口：{}", s);
                try {
                    String tableDatas = HttpClientUtil.post("http://" + s + "/api/common/health-records/getHealthRecordsList");
                    JSONObject jsonObject = JSONObject.parseObject(tableDatas);
                    if (jsonObject != null) {
                        JSONArray dataArray = jsonObject.getJSONArray("data");
                        List<HealthRecordDto> healthRecords = dataArray.toJavaList(HealthRecordDto.class);
                        for (HealthRecordDto healthRecord : healthRecords) {
                            if (Objects.equals(healthRecord.getPatientId(), patientId)) {
                                log.info("找到患者 {}，服务地址为：{}", patientId, s);
                                service = s;
                                break outerLoop;
                            }
                        }
                    }
                } catch (Exception e) {
                    log.warn("请求端口 {} 失败：{}", s, e.getMessage());
                }
            }
            log.info("端口号为：{}", service);
            if (service != null && service != "") {
                //以下代码作用为获取时间轴数据
                try {
                    // 获取 task 数据
                    String tableDatas2 = HttpClientUtil.post("http://" + service + "/api/common/task-info/getTaskInfoByPatientId?patient_id=" + patientId);
                    JSONObject jsonObject2 = JSONObject.parseObject(tableDatas2);
                    // 解析 data 数据为单个 Task 对象
                    Task task = null;
                    if (jsonObject2 != null && jsonObject2.containsKey("data")) {
                        task = jsonObject2.getObject("data", Task.class);
                        log.info("成功获取患者{}的task数据", patientId);
                    } else {
                        log.error("获取患者{}的task数据失败或data字段为空", patientId);
                    }
                    // 确保 task 不为 null，才能继续获取 taskId
                    if (task != null) {
                        // 获取时间轴数据
                        String tableDatasm = HttpClientUtil.post("http://" + service + "/api/chain/task/axis/" + task.getTaskId());
                        JSONObject jsonObjectm = JSONObject.parseObject(tableDatasm);
                        if (jsonObjectm != null && jsonObjectm.containsKey("data")) {
                            dataMap = jsonObjectm.getObject("data", new TypeReference<Map<String, TimeLine>>() {
                            });
                            log.info("成功获取患者{}的时间轴数据，共{}条记录", patientId, dataMap.size());
                        } else {
                            log.error("获取患者{}的时间轴数据失败或data字段为空", patientId);
                        }
                    } else {
                        log.error("患者{}的task对象为空", patientId);
                    }
                } catch (Exception e) {
                    log.error("查询患者{}时间轴信息时发生异常", patientId, e);
                }
            }
        return dataMap;
    }
    /**
     *   获取excel文件所需信息
     * @param
     * @return
    */
    public List<List<?>> getExcelInfo(){
           List<Doctor> ExcelDoctor =new ArrayList<>();
           List<MedicalRecord> ExcelMedicalRecord = new ArrayList<>();
           List<HealthRecord> ExcelHealthRecord =new ArrayList<>();
          List<StemiIndications> ExcelStemiINdication =new ArrayList<>();
          List<ChestPainScreening> ExcelChestPainScteening =new ArrayList<>();
        for (String sp: SERVICES) {
            try {
                // 1) 拉取健康档案列表
                String tableDatas = HttpClientUtil.post(
                        "http://" + sp + "/api/common/health-records/getHealthRecordsList"
                );
                JSONObject json = JSONObject.parseObject(tableDatas);
                JSONArray dataArray = json.getJSONArray("data");
                List<HealthRecordDto> records =
                        dataArray.toJavaList(HealthRecordDto.class);
                log.info("{} 服务拉取到 {} 条健康记录", sp, records.size());

                // 2) 遍历每条记录并填充 DTO
                for (HealthRecordDto hr : records) {
                    if (isBefore(hr.getCreateTime())) {
                        continue;
                    }
                    String patientName=hr.getPatientName();
                    if (patientName!=null){
                        if (containsTesterName(patientName)
                                || !isChineseOnly(patientName)){
                            continue;
                        }
                    }

                    try {
                        SendMessageVo dto = buildDtoFor(hr, sp);
                        if (dto != null) {
                            //Excel文件填充

                            //Doctor数据填充

                            //救护车医生
                            Doctor doctor=new Doctor();
                            doctor.setChargeName(dto.getChargeName());
                            doctor.setChargeTelphoneNumber(dto.getChargeTelphoneNumber());
                            doctor.setChargeBelongingName(dto.getChargeBelongingName());
                            //pci医生
                            doctor.setPciDoctorName(dto.getPciDoctorName());
                            doctor.setPciDoctorTelphoneNumber(dto.getPciDoctorTelphoneNumber());
                            doctor.setPciDoctorBelongingName(dto.getPciDoctorBelongingName());
                            //基层医生
                            doctor.setHospitalDoctorName(dto.getCreateName());
                            doctor.setCreateOrganization(dto.getCreateOrganization());
                            doctor.setCreateTelephoneNumber(dto.getCreateTelephoneNumber());
                            ExcelDoctor.add(doctor);


                            //MedicalRecord数据填充
                            String medicalRecordJson = HttpClientUtil.post(
                                    "http://" + sp + "/api/common/medical-records/getMedicalRecordById?patient_id=" + dto.getPatientId()
                            );
                            JSONObject jot = JSONObject.parseObject(medicalRecordJson);
                            JSONArray temp = jot.getJSONArray("data");
                            List<MedicalRecordDto> mrdList = temp.toJavaList(MedicalRecordDto.class);

                            // 合并多个 MedicalRecordDto 到一个 MedicalRecord
                            if (!mrdList.isEmpty()) {
                                MedicalRecord merged = new MedicalRecord();

                                StringBuilder roleNameBuilder = new StringBuilder();
                                StringBuilder operatorNameBuilder = new StringBuilder();
                                StringBuilder specificOperationBuilder = new StringBuilder();
                                StringBuilder operationContentBuilder = new StringBuilder();
                                StringBuilder operationTimeBuilder = new StringBuilder();

                                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

                                for (MedicalRecordDto item : mrdList) {
                                    if (item.getRoleName() != null) {
                                        roleNameBuilder.append(item.getRoleName()).append("\n");
                                    }
                                    if (item.getOperatorName() != null) {
                                        operatorNameBuilder.append(item.getOperatorName()).append("\n");
                                    }
                                    if (item.getSpecificOperation() != null) {
                                        // 映射：1-口服;2-注射;3-其他
                                        String text = switch (item.getSpecificOperation()) {
                                            case 1 -> "口服";
                                            case 2 -> "注射";
                                            case 3 -> "其他";
                                            default -> item.getSpecificOperation().toString();
                                        };
                                        specificOperationBuilder.append(text).append("\n");
                                    }
                                    if (item.getOperationContent() != null) {
                                        operationContentBuilder.append(item.getOperationContent()).append("\n");
                                    }
                                    if (item.getOperationTime() != null) {
                                        operationTimeBuilder.append(item.getOperationTime().format(formatter)).append("\n");
                                    }
                                }

                                // 设置拼接结果
                                merged.setRoleName(roleNameBuilder.toString().trim());
                                merged.setOperatorName(operatorNameBuilder.toString().trim());
                                merged.setSpecificOperationMerged(specificOperationBuilder.toString().trim());
                                merged.setOperationContent(operationContentBuilder.toString().trim());
                                merged.setOperationTimeMerged(operationTimeBuilder.toString().trim());
                                ExcelMedicalRecord.add(merged);
                            }


                            //HealthRecord数据填充
                            HealthRecord Hr=new HealthRecord();
                            String healthRecord = HttpClientUtil.post(
                                    "http://" + sp + "/api/common/health-records/getHealthRecordsById?patient_id="
                                            + dto.getPatientId()
                            );
                            JSONObject joth = JSONObject.parseObject(healthRecord);
                            HealthRecordDto Hrd = joth.getObject("data", HealthRecordDto.class);
                            if (Hrd != null) {
                                BeanUtils.copyProperties(Hrd,Hr);
                            }
                            ExcelHealthRecord.add(Hr);

                            //StemiIndications数据填充

                            StemiIndications Si=new StemiIndications();
                            String stemiIndication = HttpClientUtil.post(
                                    "http://" + sp + "/api/common/stemi-indications-screening/getStemiIndicationById?patient_id="
                                            + dto.getPatientId()
                            );
                            JSONObject jothm = JSONObject.parseObject(stemiIndication);
                            if (jothm!=null){
                                Si  = jothm.getObject("data", StemiIndications.class);
                            }
                            ExcelStemiINdication.add(Si);
                            //填充心电图数据
                            ChestPainScreening cps=new ChestPainScreening();
                            try {
                                String chestPainScreening = HttpClientUtil.post(
                                        "http://" + sp + "/api/common/chest-pain-screening/getChestPainScreeningById?patient_id=" + hr.getPatientId()
                                );

                                JSONObject jothmd = JSONObject.parseObject(chestPainScreening);
                                ChestPainScreeningDto cpsd = jothmd.getObject("data", ChestPainScreeningDto.class);

                                if (cpsd != null && cpsd.getFilePaths() != null) {
                                    Matcher m = Pattern.compile(
                                            "[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}"
                                    ).matcher(cpsd.getFilePaths());

                                    List<URL> urls = new ArrayList<>();
                                    String port = sp.contains(":") ? sp.split(":")[1] : "80";

                                    while (m.find()) {
                                        String urlStr = "https://www.rtxgzngl.cn:" + port + "/api/common/health_file/" + m.group() + ".jpg";
                                        try {
                                            urls.add(new URL(urlStr));  // 转为 URL
                                        } catch (MalformedURLException e) {
                                            e.printStackTrace(); // 或跳过该条
                                        }
                                    }
                                    // 替换规则应用于字符串
                                    String conclusionStr = cpsd.getConclusion();
                                    if (conclusionStr != null) {
                                        conclusionStr = conclusionStr.replace("%%%%%", "\n").replace("#####", "，");

                                        // 按换行符拆分为多行文本，放入List中
                                        List<String> tem = Arrays.asList(conclusionStr.split("\n"));
                                        cps.setConclusion(tem);
                                    }
                                    cps.setUrl(urls);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            ExcelChestPainScteening.add(cps);
                        }
                    } catch (Exception e) {
                        log.error("处理患者 {} 时出错", hr.getPatientId(), e);
                    }
                }
            } catch (Exception e) {
                log.error("服务 {} 抓取基础数据失败", sp, e);
            }
        }
        // —— 这里开始“方案二”排序逻辑 ——
        List<Doctor>               doctorList  = ExcelDoctor;
        List<HealthRecord>         healthList  = ExcelHealthRecord;
        List<ChestPainScreening>   chestList   = ExcelChestPainScteening;
        List<MedicalRecord>        medList     = ExcelMedicalRecord;
        List<StemiIndications>     stemiList   = ExcelStemiINdication;

        // 2. 按 healthList.size() 构造每一行的 ExcelRow
        List<ExcelRow> rows = new ArrayList<>();
        for (int i = 0, n = healthList.size(); i < n; i++) {
            ExcelRow row = new ExcelRow();
            row.doctor     = i < doctorList .size() ? doctorList .get(i) : null;
            row.health     =                      healthList .get(i);
            row.chest      = i < chestList  .size() ? chestList  .get(i) : null;
            row.medical    = i < medList    .size() ? medList    .get(i) : null;
            row.stemi      = i < stemiList  .size() ? stemiList  .get(i) : null;
            row.createTime = healthList.get(i).getCreateTime();
            rows.add(row);
        }

        // 3. 按 createTime 升序排序
        rows.sort(Comparator.comparing((ExcelRow r) -> r.createTime).reversed());
        // 4. 拆回各自的实体列表（已排序）
        List<Doctor>             sortedDoctors = rows.stream().map(r -> r.doctor).collect(Collectors.toList());
        List<HealthRecord>       sortedHealth  = rows.stream().map(r -> r.health).collect(Collectors.toList());
        List<ChestPainScreening> sortedChest   = rows.stream().map(r -> r.chest).collect(Collectors.toList());
        List<MedicalRecord>      sortedMed     = rows.stream().map(r -> r.medical).collect(Collectors.toList());
        List<StemiIndications>   sortedStemi   = rows.stream().map(r -> r.stemi).collect(Collectors.toList());

        // 5. 导出排序后的数据
        List<List<?>> mergedData = Arrays.asList(
                sortedDoctors,
                sortedHealth,
                sortedChest,
                sortedMed,
                sortedStemi
        );
        return mergedData;
    }
    /**
     *   前端导出Excel文件
     * @param
     * @return
     */
    public void exportExcelOnline(HttpServletResponse response){
        List<List<?>> data=getExcelInfo();
        ExcelUtils.exportExcelToResponse(response, "数据导出", data);
    }
    /**
     * 定时发送邮件
     * @param
     * @return
    */
    @Scheduled(cron = "0 0 9 * * ?")
    public void sendMail(){
        List<String> emails=ExcelUtils.loadEmails();
        try {
            // ...生成 excelBytes 的逻辑
            List<List<?>> data = getExcelInfo();
            // 生成 Excel 文件（byte[]）
            byte[] excelBytes = ExcelUtils.exportFileMergedObjectsOnEmail(
                    "导出示例", data
            );
            ExcelUtils.sendExcelByEmail(
                    excelBytes,
                    "患者信息",
                    emails,
                    "患者健康记录导出",
                    "您好，附件是导出的患者健康信息 Excel 表，请查收"
            );
        } catch (MessagingException e) {
            e.printStackTrace();
            System.out.println("邮件发送失败！");
        }
        log.info("邮件发送成功");
    }
}
