package com.example.viewinformation.pojo;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.util.Date;

@Data // Lombok注解，自动生成 Getter、Setter、toString、equals、hashCode 方法
public class GetTimeLine {

    @JSONField(name = "呼救时间") // 映射 JSON 中的 "呼救时间"
    private RescueTime rescueTime;

    // 内部类用来表示 "呼救时间"
    @Data // Lombok注解，自动生成 Getter、Setter、toString、equals、hashCode 方法
    public  static class RescueTime {
        private String name;
        private String location;
        private Date time;
    }
}

