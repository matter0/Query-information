package com.example.viewinformation.pojo;

import com.example.viewinformation.utils.ExcelExport;
import lombok.Data;

import java.net.URL;
import java.util.List;

@Data
public class ChestPainScreening {
    @ExcelExport(value = "心电图",isImage = true)
    private List<URL> Url;
    @ExcelExport(value = "结论")
    private List<String > conclusion;
}
