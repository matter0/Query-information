package com.example.viewinformation.service.impl;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.example.viewinformation.pojo.*;
import com.example.viewinformation.result.PageResult;
import com.example.viewinformation.result.Result;
import com.example.viewinformation.service.GetMessageService;
import com.example.viewinformation.utils.HttpClientUtil;
import com.fasterxml.jackson.core.io.JsonEOFException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import com.alibaba.fastjson.TypeReference;  // 使用 FastJSON 的 TypeReference
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Service
public class GetMessageServiceImpl implements GetMessageService {

    /**
     * 进行分页查询
     *
     * @param page
     * @param size
     * @return PageResult
     */

    @Override
    public PageResult receiveMessage(Integer page, Integer size) {
//     String[] services = {"0x900_changchun_service_1:2304", "0x908_baicheng_service_:2312", "0x910_liaoyuan_service_1:2320",
//                "0x918_jilin_service_1:2328", "0x920_tonghua_service_1:2336", "0x928_yanbian_service_1:2344", "0x930_changling_service_1:2352"};
        String[] services = {"172.18.0.12:2308", "172.18.0.12:2316", "172.18.0.12:2324",
                "172.18.0.12:2332", "172.18.0.12:2340", "172.18.0.12:2348", "172.18.0.12:2356"};
        List<SendMessageDto> sendMessageDtos = new ArrayList<>();
//        for (String sp:services) {
             String sp="localhost:2304";
            // 获取健康档案中的数据
            String tableDatas = HttpClientUtil.post("http://"+sp+"/api/common/health-records/getHealthRecordsList");
            JSONObject jsonObject = JSONObject.parseObject(tableDatas);
            // 解析 data 数据为 List<getHealthRecordsList>
            JSONArray dataArray = jsonObject.getJSONArray("data");
            List<GetHealthRecordsList> healthRecords = dataArray.toJavaList(GetHealthRecordsList.class);
            log.info("查询健康档案");

            // 获取胸痛筛查中的数据
            String tableDatas1 = HttpClientUtil.post("http://"+sp+"/api/common/chest-pain-screening/getChestPainScreeningList");
            JSONObject jsonObject1 = JSONObject.parseObject(tableDatas1);
            // 解析 data 数据为 List<ChestPainScteening>
            JSONArray dataArray1 = jsonObject1.getJSONArray("data");
            List<GetChestPainScreening> screenings = dataArray1.toJavaList(GetChestPainScreening.class);
            log.info("查询心电图");

            // 填充sendMessageDtos信息
            for (GetHealthRecordsList healthRecord : healthRecords) {
                SendMessageDto sendMessageDto = new SendMessageDto();
                //根据patientid查询task
                String tableDatas2 = HttpClientUtil.post("http://"+sp+"/api/common/task-info/getTaskInfoByPatientId?patient_id=" +healthRecord.getPatientId());
                JSONObject jsonObject2 = JSONObject.parseObject(tableDatas2);
                // 解析 data 数据为单个 Task 对象
                Task task = null;
                if (jsonObject2 != null && jsonObject2.containsKey("data")) {
                    task = jsonObject2.getObject("data", Task.class);
                } else {
                    log.error("获取 task 数据失败或 data 字段为空");
                }
                Long taskId = null;
                TaskInfo taskInfo=new TaskInfo();
                if (task != null) {
                    taskId = task.getTaskId();
                    // 获取taskInfo数据
                    String tableDatas3 = HttpClientUtil.post("http://"+sp+"/api/common/task-info/getTaskInfoById?task_id=" + taskId);
                    JSONObject jsonObject3 = JSONObject.parseObject(tableDatas3);
                    if (jsonObject3 != null && jsonObject3.containsKey("data")) {
                            taskInfo = jsonObject3.getObject("data", TaskInfo.class);
                        if (taskInfo!=null){
                            sendMessageDto.setPciDoctorName(taskInfo.getPciDoctorName());
                        }

                    } else {
                        log.error("获取taskInfo数据失败或 data 字段为空");
                    }
                } else {
                    log.error("task 对象为空，无法继续操作");
                }
                //根据taskinfo中的救护车id查询救护车详细信息中的救护车医生姓名
                AmbulancesInfo ambulancesInfo=new AmbulancesInfo();
                String tableDatas4 = HttpClientUtil.post("http://"+sp+"/api/common/ambulances-info/getAmbulancesInfoById?ambulances_info_id=" +taskInfo.getAmbulanceId());
                JSONObject jsonObject4 = JSONObject.parseObject(tableDatas4);
                if (jsonObject4!= null && jsonObject4.containsKey("data")) {
                    ambulancesInfo = jsonObject4.getObject("data", AmbulancesInfo.class);
                    if(ambulancesInfo!=null){
                        sendMessageDto.setChargeName(ambulancesInfo.getChargeName());
                    }

                } else {
                    log.error("获取 task 数据失败或 data 字段为空");
                }
                //心电图
                //查找匹配的ChestPainScreening
                for (GetChestPainScreening screening1 : screenings) {
                    if (screening1.getPatientId().equals(healthRecord.getPatientId())) {
                       //查询图片信息
                        List<String> uuids = new ArrayList<>();
                        String data=screening1.getFilePaths();
                        if(data!=null){
                            log.info("data为——————————-——————：{}",data);
                            // 正则表达式匹配 UUID 格式（8-4-4-4-12）
                            String uuidPattern = "[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}";
                            // 编译正则表达式
                            Pattern pattern = Pattern.compile(uuidPattern);
                            Matcher matcher = pattern.matcher(screening1.getFilePaths());
                            // 存储匹配到的 UUID

                            // 查找匹配的 UUID
                            while (matcher.find()) {
                                uuids.add(matcher.group());
                            }
                            List<String > networkAddress=new ArrayList<>();
                            for (String uuid:uuids){
                                //根据uuid查询网络心电图地址
                                String  na = new String();
                                log.info("uuid为——————————-——————：{}",uuid);
                                String tableDatas5 = HttpClientUtil.get("http://"+sp+"/api/common/health_file/" + uuid);
                                JSONObject jsonObject5 = JSONObject.parseObject(tableDatas5);
                                if (jsonObject5!= null) {
                                    na = jsonObject5.getObject("data", String.class);
                                    networkAddress.add(na);
                                }
                            }
                            sendMessageDto.setRemoteEcg(networkAddress);

                        }

                    }
                }
                //根据创建者的id查询创建者的具体信息
                DoctorInfo doctorInfo=new DoctorInfo();
                String tableDatas6 = HttpClientUtil.post("http://"+sp+"/api/common/doctor-info/getDoctorInfoById?doctor_info_id=" +healthRecord.getCreatedUsrId());
                JSONObject jsonObject6 = JSONObject.parseObject(tableDatas6);
                if(jsonObject6!=null){
                    doctorInfo = jsonObject6.getObject("data", DoctorInfo.class);
                    System.out.println(doctorInfo);
                    if(doctorInfo!=null){
                        sendMessageDto.setCreateName(doctorInfo.getDoctorName());
                        sendMessageDto.setCreateOrganization(doctorInfo.getDoctorBelongingName());
                        sendMessageDto.setCreateTelephoneNumber(doctorInfo.getDoctorContact());
                    }
                }


                sendMessageDto.setPatientName(healthRecord.getPatientName());
                sendMessageDto.setPatientGender(healthRecord.getGender());
                sendMessageDto.setPatientAge(healthRecord.getAge());
                sendMessageDto.setPatientId(healthRecord.getPatientId());
                sendMessageDto.setCreateTime(healthRecord.getCreateTime());
                // 将填充好的SendMessageDto添加到sendMessageDtos列表中
                sendMessageDtos.add(sendMessageDto);
            }
//        }
        // 将数据根据createTime来进行排序
        sendMessageDtos.sort(Comparator.comparing(SendMessageDto::getCreateTime));
        // 分页逻辑
        int start = (page-1) * size;
        int end = Math.min(start + size, sendMessageDtos.size());
        System.out.println(start + "  " + end);
        List<SendMessageDto> pagedList = sendMessageDtos.subList(start, end);
        // 封装分页结果
        PageResult pageResult = new PageResult();
        pageResult.setTotal(sendMessageDtos.size()); // 设置总记录数
        pageResult.setRecords(pagedList); // 设置当前页的数据
        return pageResult;
    }

    /**
     *   根据患者id查询健康档案
     * @param
     * @return
     */
    @Override
    public GetHealthRecordsList fetchHealthRecords(Long Id) {
//        String[] services = {"172.18.0.12:2308", "172.18.0.12:2316", "172.18.0.12:2324",
//                "172.18.0.12:2332", "172.18.0.12:2340", "172.18.0.12:2348", "172.18.0.12:2356"};
        String servive="localhost:2304";
//        //查找患者对应的网址端口
//        outerLoop: // 定义一个标签
//        for (String s: services) {
//            String tableDatas = HttpClientUtil.post("http://" + s + "/api/common/health-records/getHealthRecordsList");
//            JSONObject jsonObject = JSONObject.parseObject(tableDatas);
//            JSONArray dataArray = jsonObject.getJSONArray("data");
//            List<GetHealthRecordsList> healthRecords = dataArray.toJavaList(GetHealthRecordsList.class);
//            for (GetHealthRecordsList healthRecord : healthRecords) {
//                if (healthRecord.getPatientId().equals(Id)) {
//                    servive=s;
//                    break outerLoop; // 使用标签中断外层循环
//                }
//            }
//        }
        GetHealthRecordsList healthRecordsList = new GetHealthRecordsList();
        String tableDatas = HttpClientUtil.post("http://"+servive+"/api/common/health-records/getHealthRecordsById?patient_id=" + Id);
        JSONObject jsonObject = JSONObject.parseObject(tableDatas);
        if (jsonObject != null) {
            healthRecordsList = jsonObject.getObject("data", GetHealthRecordsList.class);
        }
        return healthRecordsList;
    }











    /**
     *   查询时间轴信息
     * @param patientId
     * @return TimeLine
     */
    @Override
    public Map<String, TimeLine> fetchTimeLine(Long patientId) {
        log.info("查询时间轴信息");
        String[] services = {"172.18.0.12:2308", "172.18.0.12:2316", "172.18.0.12:2324",
                "172.18.0.12:2332", "172.18.0.12:2340", "172.18.0.12:2348", "172.18.0.12:2356"};
        String servive="localhost:2304";
        //查找患者对应的网址端口
//        outerLoop: // 定义一个标签
//        for (String s: services) {
//            String tableDatas = HttpClientUtil.post("http://" + s + "/api/common/health-records/getHealthRecordsList");
//            JSONObject jsonObject = JSONObject.parseObject(tableDatas);
//            JSONArray dataArray = jsonObject.getJSONArray("data");
//            List<GetHealthRecordsList> healthRecords = dataArray.toJavaList(GetHealthRecordsList.class);
//            for (GetHealthRecordsList healthRecord : healthRecords) {
//                if (healthRecord.getPatientId().equals(patientId)) {
//                    servive=s;
//                    break outerLoop; // 使用标签中断外层循环
//                }
//            }
//        }
        //以下代码作用为获取时间轴数据
        Map<String,TimeLine> dataMap=new HashMap<>();
        // 获取 task 数据
        String tableDatas2 = HttpClientUtil.post("http://"+servive+"/api/common/task-info/getTaskInfoByPatientId?patient_id=" +patientId);
        JSONObject jsonObject2 = JSONObject.parseObject(tableDatas2);
        // 解析 data 数据为单个 Task 对象
        Task task = null;
        if (jsonObject2 != null && jsonObject2.containsKey("data")) {
            task = jsonObject2.getObject("data", Task.class);
        } else {
            log.error("获取 task 数据失败或 data 字段为空");
        }
        // 确保 task 不为 null，才能继续获取 taskId
        Long taskId = null;
        if (task != null) {
            taskId = task.getTaskId();
            // 获取时间轴数据
            String tableDatasm = HttpClientUtil.post("http://"+servive+"/api/chain/task/axis/" + taskId);
            JSONObject jsonObjectm = JSONObject.parseObject(tableDatasm);
            if (jsonObjectm != null && jsonObjectm.containsKey("data")) {
                dataMap = jsonObjectm.getObject("data", new TypeReference<Map<String, TimeLine>>(){});
            } else {
                log.error("获取时间轴数据失败或 data 字段为空");
            }
        } else {
            log.error("task 对象为空，无法继续操作");
        }
        return dataMap;
    }












}

