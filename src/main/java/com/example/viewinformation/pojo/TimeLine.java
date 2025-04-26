package com.example.viewinformation.pojo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
@Data
public class TimeLine implements Serializable {
    private String name; //名称
    private String location; //地点
    private Date time; //时间
}
