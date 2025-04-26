package com.example.viewinformation.pojo;

import com.example.viewinformation.utils.ExcelExport;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class Doctor implements Serializable {
    @ExcelExport(value = "救护车医生姓名")
    private String chargeName;
    @ExcelExport(value = "救护车医生电话")
    private String chargeTelphoneNumber;
    @ExcelExport(value = "救护车医生所属单位")
    private String chargeBelongingName;


    @ExcelExport(value = "基层医院医生姓名")
    private String  hospitalDoctorName;
    @ExcelExport(value = "基层医生单位")
    private String createOrganization ;
    @ExcelExport(value = "基层医生手机号")
    private String createTelephoneNumber;

    @ExcelExport(value="pci医院医生姓名")
    private String  pciDoctorName;
    @ExcelExport(value="pci医生电话")
    private String pciDoctorTelphoneNumber;
    @ExcelExport(value="pci医生单位")
    private String pciDoctorBelongingName;

}
