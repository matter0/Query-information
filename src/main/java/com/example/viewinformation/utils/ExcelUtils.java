package com.example.viewinformation.utils;


import java.util.Properties;

import jakarta.activation.DataHandler;
import jakarta.activation.DataSource;
import jakarta.mail.*;
import jakarta.mail.internet.*;
import jakarta.mail.util.ByteArrayDataSource;

import java.util.Properties;




import org.apache.poi.ss.usermodel.*;

import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import javax.imageio.ImageIO;

import jakarta.servlet.http.HttpServletResponse; // ✅

import java.awt.*;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.*;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;
import java.util.Map.Entry;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Excel导入导出工具类
 * 原文链接（不定时增加新功能）: https://zyqok.blog.csdn.net/article/details/121994504
 *
 * @author sunnyzyq
 * @date 2021/12/17
 */
@SuppressWarnings("unused")
public class ExcelUtils {

    private static final String XLSX = ".xlsx";
    private static final String XLS = ".xls";
    public static final String ROW_MERGE = "row_merge";
    public static final String COLUMN_MERGE = "column_merge";
    private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private static final String ROW_NUM = "rowNum";
    private static final String ROW_DATA = "rowData";
    private static final String ROW_TIPS = "rowTips";
    private static final int CELL_OTHER = 0;
    private static final int CELL_ROW_MERGE = 1;
    private static final int CELL_COLUMN_MERGE = 2;
    private static final int IMG_HEIGHT = 30;
    private static final int IMG_WIDTH = 30;
    private static final char LEAN_LINE = '/';
    private static final int BYTES_DEFAULT_LENGTH = 10240;
    private static final NumberFormat NUMBER_FORMAT = NumberFormat.getNumberInstance();


//    public static <T> List<T> readFile(File file, Class<T> clazz) throws Exception {
//        JSONArray array = readFile(file);
//        return getBeanList(array, clazz);
//    }
//
//    public static <T> List<T> readMultipartFile(MultipartFile mFile, Class<T> clazz) throws Exception {
//        JSONArray array = readMultipartFile(mFile);
//        return getBeanList(array, clazz);
//    }
//
//    public static JSONArray readFile(File file) throws Exception {
//        return readExcel(null, file);
//    }
//
//    public static JSONArray readMultipartFile(MultipartFile mFile) throws Exception {
//        return readExcel(mFile, null);
//    }
//
//    public static Map<String, JSONArray> readFileManySheet(File file) throws Exception {
//        return readExcelManySheet(null, file);
//    }
//
//    public static Map<String, JSONArray> readFileManySheet(MultipartFile file) throws Exception {
//        return readExcelManySheet(file, null);
//    }
//
//    private static <T> List<T> getBeanList(JSONArray array, Class<T> clazz) throws Exception {
//        List<T> list = new ArrayList<>();
//        Map<Integer, String> uniqueMap = new HashMap<>(16);
//        for (int i = 0; i < array.size(); i++) {
//            list.add(getBean(clazz, array.getJSONObject(i), uniqueMap));
//        }
//        return list;
//    }
//
//    /**
//     * 获取每个对象的数据
//     */
//    private static <T> T getBean(Class<T> c, JSONObject obj, Map<Integer, String> uniqueMap) throws Exception {
//        T t = c.newInstance();
//        Field[] fields = c.getDeclaredFields();
//        List<String> errMsgList = new ArrayList<>();
//        boolean hasRowTipsField = false;
//        StringBuilder uniqueBuilder = new StringBuilder();
//        int rowNum = 0;
//        for (Field field : fields) {
//            // 行号
//            if (field.getName().equals(ROW_NUM)) {
//                rowNum = obj.getInteger(ROW_NUM);
//                field.setAccessible(true);
//                field.set(t, rowNum);
//                continue;
//            }
//            // 是否需要设置异常信息
//            if (field.getName().equals(ROW_TIPS)) {
//                hasRowTipsField = true;
//                continue;
//            }
//            // 原始数据
//            if (field.getName().equals(ROW_DATA)) {
//                field.setAccessible(true);
//                field.set(t, obj.toString());
//                continue;
//            }
//            // 设置对应属性值
//            setFieldValue(t, field, obj, uniqueBuilder, errMsgList);
//        }
//        // 数据唯一性校验
//        if (uniqueBuilder.length() > 0) {
//            if (uniqueMap.containsValue(uniqueBuilder.toString())) {
//                Set<Integer> rowNumKeys = uniqueMap.keySet();
//                for (Integer num : rowNumKeys) {
//                    if (uniqueMap.get(num).equals(uniqueBuilder.toString())) {
//                        errMsgList.add(String.format("数据唯一性校验失败,(%s)与第%s行重复)", uniqueBuilder, num));
//                    }
//                }
//            } else {
//                uniqueMap.put(rowNum, uniqueBuilder.toString());
//            }
//        }
//        // 失败处理
//        if (errMsgList.isEmpty() && !hasRowTipsField) {
//            return t;
//        }
//        StringBuilder sb = new StringBuilder();
//        int size = errMsgList.size();
//        for (int i = 0; i < size; i++) {
//            if (i == size - 1) {
//                sb.append(errMsgList.get(i));
//            } else {
//                sb.append(errMsgList.get(i)).append(";");
//            }
//        }
//        // 设置错误信息
//        for (Field field : fields) {
//            if (field.getName().equals(ROW_TIPS)) {
//                field.setAccessible(true);
//                field.set(t, sb.toString());
//            }
//        }
//        return t;
//    }
//
//    private static <T> void setFieldValue(T t, Field field, JSONObject obj, StringBuilder uniqueBuilder, List<String> errMsgList) {
//        // 获取 ExcelImport 注解属性
//        ExcelImport annotation = field.getAnnotation(ExcelImport.class);
//        if (annotation == null) {
//            return;
//        }
//        String cname = annotation.value();
//        if (cname.trim().length() == 0) {
//            return;
//        }
//        // 获取具体值
//        String val = null;
//        if (obj.containsKey(cname)) {
//            val = getString(obj.getString(cname));
//        }
//        if (val == null) {
//            return;
//        }
//        field.setAccessible(true);
//        // 判断是否必填
//        boolean require = annotation.required();
//        if (require && val.isEmpty()) {
//            errMsgList.add(String.format("[%s]不能为空", cname));
//            return;
//        }
//        // 数据唯一性获取
//        boolean unique = annotation.unique();
//        if (unique) {
//            if (uniqueBuilder.length() > 0) {
//                uniqueBuilder.append("--").append(val);
//            } else {
//                uniqueBuilder.append(val);
//            }
//        }
//        // 判断是否超过最大长度
//        int maxLength = annotation.maxLength();
//        if (maxLength > 0 && val.length() > maxLength) {
//            errMsgList.add(String.format("[%s]长度不能超过%s个字符(当前%s个字符)", cname, maxLength, val.length()));
//        }
//        // 判断当前属性是否有映射关系
//        LinkedHashMap<String, String> kvMap = getKvMap(annotation.kv());
//        if (!kvMap.isEmpty()) {
//            boolean isMatch = false;
//            for (String key : kvMap.keySet()) {
//                if (kvMap.get(key).equals(val)) {
//                    val = key;
//                    isMatch = true;
//                    break;
//                }
//            }
//            if (!isMatch) {
//                errMsgList.add(String.format("[%s]的值不正确(当前值为%s)", cname, val));
//                return;
//            }
//        }
//        // 其余情况根据类型赋值
//        String fieldClassName = field.getType().getSimpleName();
//        try {
//            if ("String".equalsIgnoreCase(fieldClassName)) {
//                field.set(t, val);
//            } else if ("boolean".equalsIgnoreCase(fieldClassName)) {
//                field.set(t, Boolean.valueOf(val));
//            } else if ("int".equalsIgnoreCase(fieldClassName) || "Integer".equals(fieldClassName)) {
//                try {
//                    field.set(t, Integer.valueOf(val));
//                } catch (NumberFormatException e) {
//                    errMsgList.add(String.format("[%s]的值格式不正确(当前值为%s)", cname, val));
//                }
//            } else if ("double".equalsIgnoreCase(fieldClassName)) {
//                field.set(t, Double.valueOf(val));
//            } else if ("long".equalsIgnoreCase(fieldClassName)) {
//                field.set(t, Long.valueOf(val));
//            } else if ("BigDecimal".equalsIgnoreCase(fieldClassName)) {
//                field.set(t, new BigDecimal(val));
//            } else if ("Date".equalsIgnoreCase(fieldClassName)) {
//                try {
//                    field.set(t, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(val));
//                } catch (Exception e) {
//                    field.set(t, new SimpleDateFormat("yyyy-MM-dd").parse(val));
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    private static Map<String, JSONArray> readExcelManySheet(MultipartFile mFile, File file) throws IOException {
//        Workbook book = getWorkbook(mFile, file);
//        if (book == null) {
//            return Collections.emptyMap();
//        }
//        Map<String, JSONArray> map = new LinkedHashMap<>();
//        for (int i = 0; i < book.getNumberOfSheets(); i++) {
//            Sheet sheet = book.getSheetAt(i);
//            JSONArray arr = readSheet(sheet);
//            map.put(sheet.getSheetName(), arr);
//        }
//        book.close();
//        return map;
//    }
//
//    private static JSONArray readExcel(MultipartFile mFile, File file) throws IOException {
//        Workbook book = getWorkbook(mFile, file);
//        if (book == null) {
//            return new JSONArray();
//        }
//        JSONArray array = readSheet(book.getSheetAt(0));
//        book.close();
//        return array;
//    }
//
//    private static Workbook getWorkbook(MultipartFile mFile, File file) throws IOException {
//        boolean fileNotExist = (file == null || !file.exists());
//        if (mFile == null && fileNotExist) {
//            return null;
//        }
//        // 解析表格数据
//        InputStream in;
//        String fileName;
//        if (mFile != null) {
//            // 上传文件解析
//            in = mFile.getInputStream();
//            fileName = getString(mFile.getOriginalFilename()).toLowerCase();
//        } else {
//            // 本地文件解析
//            in = new FileInputStream(file);
//            fileName = file.getName().toLowerCase();
//        }
//        Workbook book;
//        if (fileName.endsWith(XLSX)) {
//            book = new XSSFWorkbook(in);
//        } else if (fileName.endsWith(XLS)) {
//            POIFSFileSystem poifsFileSystem = new POIFSFileSystem(in);
//            book = new HSSFWorkbook(poifsFileSystem);
//        } else {
//            return null;
//        }
//        in.close();
//        return book;
//    }
//
//    private static JSONArray readSheet(Sheet sheet) {
//        // 首行下标
//        int rowStart = sheet.getFirstRowNum();
//        // 尾行下标
//        int rowEnd = sheet.getLastRowNum();
//        // 获取表头行
//        Row headRow = sheet.getRow(rowStart);
//        if (headRow == null) {
//            return new JSONArray();
//        }
//        int cellStart = headRow.getFirstCellNum();
//        int cellEnd = headRow.getLastCellNum();
//        Map<Integer, String> keyMap = new HashMap<>();
//        for (int j = cellStart; j < cellEnd; j++) {
//            // 获取表头数据
//            String val = getCellValue(headRow.getCell(j));
//            if (val != null && val.trim().length() != 0) {
//                keyMap.put(j, val);
//            }
//        }
//        // 如果表头没有数据则不进行解析
//        if (keyMap.isEmpty()) {
//            return (JSONArray) Collections.emptyList();
//        }
//        // 获取每行JSON对象的值
//        JSONArray array = new JSONArray();
//        // 如果首行与尾行相同，表明只有一行，返回表头数据
//        if (rowStart == rowEnd) {
//            JSONObject obj = new JSONObject();
//            // 添加行号
//            obj.put(ROW_NUM, 1);
//            for (int i : keyMap.keySet()) {
//                obj.put(keyMap.get(i), "");
//            }
//            array.add(obj);
//            return array;
//        }
//        for (int i = rowStart + 1; i <= rowEnd; i++) {
//            Row eachRow = sheet.getRow(i);
//            JSONObject obj = new JSONObject();
//            // 添加行号
//            obj.put(ROW_NUM, i + 1);
//            StringBuilder sb = new StringBuilder();
//            for (int k = cellStart; k < cellEnd; k++) {
//                if (eachRow != null) {
//                    String val = getCellValue(eachRow.getCell(k));
//                    // 所有数据添加到里面，用于判断该行是否为空
//                    sb.append(val);
//                    obj.put(keyMap.get(k), val);
//                }
//            }
//            if (sb.length() > 0) {
//                array.add(obj);
//            }
//        }
//        return array;
//    }
//
//    private static String getCellValue(Cell cell) {
//        // 空白或空
//        if (cell == null || cell.getCellType() == CellType.BLANK) {
//            return "";
//        }
//        // String类型
//        if (cell.getCellType() == CellType.STRING) {
//            String val = cell.getStringCellValue();
//            if (val == null || val.trim().length() == 0) {
//                return "";
//            }
//            return val.trim();
//        }
//        // 数字类型
//        if (cell.getCellType() == CellType.NUMERIC) {
//            String s = cell.getNumericCellValue() + "";
//            // 去掉尾巴上的小数点0
//            if (Pattern.matches(".*\\.0*", s)) {
//                return s.split("\\.")[0];
//            } else {
//                return s;
//            }
//        }
//        // 布尔值类型
//        if (cell.getCellType() == CellType.BOOLEAN) {
//            return cell.getBooleanCellValue() + "";
//        }
//        // 错误类型
//        return cell.getCellFormula();
//    }
//
//    public static <T> void exportTemplate(HttpServletResponse response, String fileName, Class<T> clazz) {
//        exportTemplate(response, fileName, fileName, clazz, false);
//    }
//
//    public static <T> void exportTemplate(HttpServletResponse response, String fileName, String sheetName,
//                                          Class<T> clazz) {
//        exportTemplate(response, fileName, sheetName, clazz, false);
//    }
//
//    public static <T> void exportTemplate(HttpServletResponse response, String fileName, Class<T> clazz,
//                                          boolean isContainExample) {
//        exportTemplate(response, fileName, fileName, clazz, isContainExample);
//    }
//
//    public static <T> void exportTemplate(HttpServletResponse response, String fileName, String sheetName,
//                                          Class<T> clazz, boolean isContainExample) {
//        // 获取表头字段
//        List<ExcelClassField> headFieldList = getExcelClassFieldList(clazz);
//        // 获取表头数据和示例数据
//        List<List<Object>> sheetDataList = new ArrayList<>();
//        List<Object> headList = new ArrayList<>();
//        List<Object> exampleList = new ArrayList<>();
//        Map<Integer, List<String>> selectMap = new LinkedHashMap<>();
//        for (int i = 0; i < headFieldList.size(); i++) {
//            ExcelClassField each = headFieldList.get(i);
//            headList.add(each.getName());
//            exampleList.add(each.getExample());
//            LinkedHashMap<String, String> kvMap = each.getKvMap();
//            if (kvMap != null && kvMap.size() > 0) {
//                selectMap.put(i, new ArrayList<>(kvMap.values()));
//            }
//        }
//        sheetDataList.add(headList);
//        if (isContainExample) {
//            sheetDataList.add(exampleList);
//        }
//        // 导出数据
//        export(response, fileName, sheetName, sheetDataList, selectMap);
//    }
//
//    private static <T> List<ExcelClassField> getExcelClassFieldList(Class<T> clazz) {
//        // 解析所有字段
//        Field[] fields = clazz.getDeclaredFields();
//        boolean hasExportAnnotation = false;
//        Map<Integer, List<ExcelClassField>> map = new LinkedHashMap<>();
//        List<Integer> sortList = new ArrayList<>();
//        for (Field field : fields) {
//            ExcelClassField cf = getExcelClassField(field);
//            if (cf.getHasAnnotation() == 1) {
//                hasExportAnnotation = true;
//            }
//            int sort = cf.getSort();
//            if (map.containsKey(sort)) {
//                map.get(sort).add(cf);
//            } else {
//                List<ExcelClassField> list = new ArrayList<>();
//                list.add(cf);
//                sortList.add(sort);
//                map.put(sort, list);
//            }
//        }
//        Collections.sort(sortList);
//        // 获取表头
//        List<ExcelClassField> headFieldList = new ArrayList<>();
//        if (hasExportAnnotation) {
//            for (Integer sort : sortList) {
//                for (ExcelClassField cf : map.get(sort)) {
//                    if (cf.getHasAnnotation() == 1) {
//                        headFieldList.add(cf);
//                    }
//                }
//            }
//        } else {
//            headFieldList.addAll(map.get(0));
//        }
//        return headFieldList;
//    }
//
//    private static ExcelClassField getExcelClassField(Field field) {
//        ExcelClassField cf = new ExcelClassField();
//        String fieldName = field.getName();
//        cf.setFieldName(fieldName);
//        ExcelExport annotation = field.getAnnotation(ExcelExport.class);
//        // 无 ExcelExport 注解情况
//        if (annotation == null) {
//            cf.setHasAnnotation(0);
//            cf.setName(fieldName);
//            cf.setSort(0);
//            return cf;
//        }
//        // 有 ExcelExport 注解情况
//        cf.setHasAnnotation(1);
//        cf.setName(annotation.value());
//        String example = getString(annotation.example());
//        if (!example.isEmpty()) {
//            if (isNumeric(example) && example.length() < 8) {
//                cf.setExample(Double.valueOf(example));
//            } else {
//                cf.setExample(example);
//            }
//        } else {
//            cf.setExample("");
//        }
//        cf.setSort(annotation.sort());
//        // 解析映射
//        String kv = getString(annotation.kv());
//        cf.setKvMap(getKvMap(kv));
//        return cf;
//    }
//
//    private static LinkedHashMap<String, String> getKvMap(String kv) {
//        LinkedHashMap<String, String> kvMap = new LinkedHashMap<>();
//        if (kv.isEmpty()) {
//            return kvMap;
//        }
//        String[] kvs = kv.split(";");
//        if (kvs.length == 0) {
//            return kvMap;
//        }
//        for (String each : kvs) {
//            String[] eachKv = getString(each).split("-");
//            if (eachKv.length != 2) {
//                continue;
//            }
//            String k = eachKv[0];
//            String v = eachKv[1];
//            if (k.isEmpty() || v.isEmpty()) {
//                continue;
//            }
//            kvMap.put(k, v);
//        }
//        return kvMap;
//    }
//
//    /**
//     * 导出表格到本地
//     *
//     * @param file      本地文件对象
//     * @param sheetData 导出数据
//     */
////    public static void exportFile(File file, List<List<Object>> sheetData) {
////        if (file == null) {
////            System.out.println("文件创建失败");
////            return;
////        }
////        if (sheetData == null) {
////            sheetData = new ArrayList<>();
////        }
////        Map<String, List<List<Object>>> map = new HashMap<>();
////        map.put(file.getName(), sheetData);
////        export(null, file, file.getName(), map, null);
////    }
//
//    /**
//     * 导出表格到本地
//     *
//     * @param <T>      导出数据类似，和K类型保持一致
//     * @param filePath 文件父路径（如：D:/doc/excel/）
//     * @param fileName 文件名称（不带尾缀，如：学生表）
//     * @param list     导出数据
//     * @throws IOException IO异常
//     */
////    public static <T> File exportFile(String filePath, String fileName, List<T> list) throws IOException {
////        File file = getFile(filePath, fileName);
////        List<List<Object>> sheetData = getSheetData(list);
////        exportFile(file, sheetData);
////        return file;
////    }
//
//    /**
//     * 获取文件
//     *
//     * @param filePath filePath 文件父路径（如：D:/doc/excel/）
//     * @param fileName 文件名称（不带尾缀，如：用户表）
//     * @return 本地File文件对象
//     */
//    private static File getFile(String filePath, String fileName) throws IOException {
//        String dirPath = getString(filePath);
//        String fileFullPath;
//        if (dirPath.isEmpty()) {
//            fileFullPath = fileName;
//        } else {
//            // 判定文件夹是否存在，如果不存在，则级联创建
//            File dirFile = new File(dirPath);
//            if (!dirFile.exists()) {
//                boolean mkdirs = dirFile.mkdirs();
//                if (!mkdirs) {
//                    return null;
//                }
//            }
//            // 获取文件夹全名
//            if (dirPath.endsWith(String.valueOf(LEAN_LINE))) {
//                fileFullPath = dirPath + fileName + XLSX;
//            } else {
//                fileFullPath = dirPath + LEAN_LINE + fileName + XLSX;
//            }
//        }
//        System.out.println(fileFullPath);
//        File file = new File(fileFullPath);
//        if (!file.exists()) {
//            boolean result = file.createNewFile();
//            if (!result) {
//                return null;
//            }
//        }
//        return file;
//    }
//
//    private static <T> List<List<Object>> getSheetData(List<T> list) {
//        // 获取表头字段
//        List<ExcelClassField> excelClassFieldList = getExcelClassFieldList(list.get(0).getClass());
//        List<String> headFieldList = new ArrayList<>();
//        List<Object> headList = new ArrayList<>();
//        Map<String, ExcelClassField> headFieldMap = new HashMap<>();
//        for (ExcelClassField each : excelClassFieldList) {
//            String fieldName = each.getFieldName();
//            headFieldList.add(fieldName);
//            headFieldMap.put(fieldName, each);
//            headList.add(each.getName());
//        }
//        // 添加表头名称
//        List<List<Object>> sheetDataList = new ArrayList<>();
//        sheetDataList.add(headList);
//        // 获取表数据
//        for (T t : list) {
//            Map<String, Object> fieldDataMap = getFieldDataMap(t);
//            Set<String> fieldDataKeys = fieldDataMap.keySet();
//            List<Object> rowList = new ArrayList<>();
//            for (String headField : headFieldList) {
//                if (!fieldDataKeys.contains(headField)) {
//                    continue;
//                }
//                Object data = fieldDataMap.get(headField);
//                if (data == null) {
//                    rowList.add("");
//                    continue;
//                }
//                ExcelClassField cf = headFieldMap.get(headField);
//                // 判断是否有映射关系
//                LinkedHashMap<String, String> kvMap = cf.getKvMap();
//                if (kvMap == null || kvMap.isEmpty()) {
//                    rowList.add(data);
//                    continue;
//                }
//                String val = kvMap.get(data.toString());
//                if (isNumeric(val)) {
//                    rowList.add(Double.valueOf(val));
//                } else {
//                    rowList.add(val);
//                }
//            }
//            sheetDataList.add(rowList);
//        }
//        return sheetDataList;
//    }
//
//    private static <T> Map<String, Object> getFieldDataMap(T t) {
//        Map<String, Object> map = new HashMap<>();
//        Field[] fields = t.getClass().getDeclaredFields();
//        try {
//            for (Field field : fields) {
//                String fieldName = field.getName();
//                field.setAccessible(true);
//                Object object = field.get(t);
//                map.put(fieldName, object);
//            }
//        } catch (IllegalArgumentException | IllegalAccessException e) {
//            e.printStackTrace();
//        }
//        return map;
//    }
//
//    public static void exportEmpty(HttpServletResponse response, String fileName) {
//        List<List<Object>> sheetDataList = new ArrayList<>();
//        List<Object> headList = new ArrayList<>();
//        headList.add("导出无数据");
//        sheetDataList.add(headList);
//        export(response, fileName, sheetDataList);
//    }
//
////    public static void export(HttpServletResponse response, String fileName, List<List<Object>> sheetDataList) {
////        export(response, fileName, fileName, sheetDataList, null);
////    }
//
////    public static void exportManySheet(HttpServletResponse response, String fileName, Map<String, List<List<Object>>> sheetMap) {
////        export(response, null, fileName, sheetMap, null);
////    }
//
//
////    public static void export(HttpServletResponse response, String fileName, String sheetName,
////                              List<List<Object>> sheetDataList) {
////        export(response, fileName, sheetName, sheetDataList, null);
////    }
//
////    public static void export(HttpServletResponse response, String fileName, String sheetName,
////                              List<List<Object>> sheetDataList, Map<Integer, List<String>> selectMap) {
////
////        Map<String, List<List<Object>>> map = new HashMap<>();
////        map.put(sheetName, sheetDataList);
////        export(response, null, fileName, map, selectMap);
////    }
//
//    public static <T, K> void export(HttpServletResponse response, String fileName, List<T> list, Class<K> template) {
//        // list 是否为空
//        boolean lisIsEmpty = list == null || list.isEmpty();
//        // 如果模板数据为空，且导入的数据为空，则导出空文件
//        if (template == null && lisIsEmpty) {
//            exportEmpty(response, fileName);
//            return;
//        }
//        // 如果 list 数据，则导出模板数据
//        if (lisIsEmpty) {
//            exportTemplate(response, fileName, template);
//            return;
//        }
//        // 导出数据
//        List<List<Object>> sheetDataList = getSheetData(list);
//        export(response, fileName, sheetDataList);
//    }
//
//    public static void export(HttpServletResponse response, String fileName, List<List<Object>> sheetDataList, Map<Integer, List<String>> selectMap) {
//        export(response, fileName, fileName, sheetDataList, selectMap);
//    }
//
////    private static void export(HttpServletResponse response, File file, String fileName,
////                               Map<String, List<List<Object>>> sheetMap, Map<Integer, List<String>> selectMap) {
////        // 整个 Excel 表格 book 对象
////        SXSSFWorkbook book = new SXSSFWorkbook();
////        // 每个 Sheet 页
////        Set<Entry<String, List<List<Object>>>> entries = sheetMap.entrySet();
////        for (Entry<String, List<List<Object>>> entry : entries) {
////            List<List<Object>> sheetDataList = entry.getValue();
////            Sheet sheet = book.createSheet(entry.getKey());
////            Drawing<?> patriarch = sheet.createDrawingPatriarch();
////            // 设置表头背景色（灰色）
////            CellStyle headStyle = book.createCellStyle();
////            headStyle.setFillForegroundColor(IndexedColors.GREY_80_PERCENT.index);
////            headStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
////            headStyle.setAlignment(HorizontalAlignment.CENTER);
////            headStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.index);
////            // 设置表身背景色（默认色）
////            CellStyle rowStyle = book.createCellStyle();
////            rowStyle.setAlignment(HorizontalAlignment.CENTER);
////            rowStyle.setVerticalAlignment(VerticalAlignment.CENTER);
////            // 设置表格列宽度（默认为15个字节）
////            sheet.setDefaultColumnWidth(15);
////            // 创建合并算法数组
////            int rowLength = sheetDataList.size();
////            int columnLength = sheetDataList.get(0).size();
////            int[][] mergeArray = new int[rowLength][columnLength];
////            for (int i = 0; i < sheetDataList.size(); i++) {
////                // 每个 Sheet 页中的行数据
////                Row row = sheet.createRow(i);
////                List<Object> rowList = sheetDataList.get(i);
////                for (int j = 0; j < rowList.size(); j++) {
////                    // 每个行数据中的单元格数据
////                    Object o = rowList.get(j);
////                    int v = 0;
////                    if (o instanceof URL) {
////                        // 如果要导出图片的话, 链接需要传递 URL 对象
////                        setCellPicture(book, row, patriarch, i, j, (URL) o);
////                    } else {
////                        Cell cell = row.createCell(j);
////                        if (i == 0) {
////                            // 第一行为表头行，采用灰色底背景
////                            v = setCellValue(cell, o, headStyle);
////                        } else {
////                            // 其他行为数据行，默认白底色
////                            v = setCellValue(cell, o, rowStyle);
////                        }
////                    }
////                    mergeArray[i][j] = v;
////                }
////            }
////            // 合并单元格
////            mergeCells(sheet, mergeArray);
////            // 设置下拉列表
////            setSelect(sheet, selectMap);
////        }
////        // 写数据
////        if (response != null) {
////            // 前端导出
////            try {
////                write(response, book, fileName);
////            } catch (IOException e) {
////                e.printStackTrace();
////            }
////        } else {
////            // 本地导出
////            FileOutputStream fos;
////            try {
////                fos = new FileOutputStream(file);
////                ByteArrayOutputStream ops = new ByteArrayOutputStream();
////                book.write(ops);
////                fos.write(ops.toByteArray());
////                fos.close();
////            } catch (Exception e) {
////                e.printStackTrace();
////            }
////        }
////    }
//
//    /**
//     * 合并当前Sheet页的单元格
//     *
//     * @param sheet      当前 sheet 页
//     * @param mergeArray 合并单元格算法
//     */
//    private static void mergeCells(Sheet sheet, int[][] mergeArray) {
//        // 横向合并
//        for (int x = 0; x < mergeArray.length; x++) {
//            int[] arr = mergeArray[x];
//            boolean merge = false;
//            int y1 = 0;
//            int y2 = 0;
//            for (int y = 0; y < arr.length; y++) {
//                int value = arr[y];
//                if (value == CELL_COLUMN_MERGE) {
//                    if (!merge) {
//                        y1 = y;
//                    }
//                    y2 = y;
//                    merge = true;
//                } else {
//                    merge = false;
//                    if (y1 > 0) {
//                        sheet.addMergedRegion(new CellRangeAddress(x, x, (y1 - 1), y2));
//                    }
//                    y1 = 0;
//                    y2 = 0;
//                }
//            }
//            if (y1 > 0) {
//                sheet.addMergedRegion(new CellRangeAddress(x, x, (y1 - 1), y2));
//            }
//        }
//        // 纵向合并
//        int xLen = mergeArray.length;
//        int yLen = mergeArray[0].length;
//        for (int y = 0; y < yLen; y++) {
//            boolean merge = false;
//            int x1 = 0;
//            int x2 = 0;
//            for (int x = 0; x < xLen; x++) {
//                int value = mergeArray[x][y];
//                if (value == CELL_ROW_MERGE) {
//                    if (!merge) {
//                        x1 = x;
//                    }
//                    x2 = x;
//                    merge = true;
//                } else {
//                    merge = false;
//                    if (x1 > 0) {
//                        sheet.addMergedRegion(new CellRangeAddress((x1 - 1), x2, y, y));
//                    }
//                    x1 = 0;
//                    x2 = 0;
//                }
//            }
//            if (x1 > 0) {
//                sheet.addMergedRegion(new CellRangeAddress((x1 - 1), x2, y, y));
//            }
//        }
//    }
//
////    private static void write(HttpServletResponse response, SXSSFWorkbook book, String fileName) throws IOException {
////        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
////        response.setCharacterEncoding("utf-8");
////        String name = new String(fileName.getBytes("GBK"), "ISO8859_1") + XLSX;
////        response.addHeader("Content-Disposition", "attachment;filename=" + name);
////        ServletOutputStream out = response.getOutputStream();
////        book.write(out);
////        out.flush();
////        out.close();
////    }
//
//    private static int setCellValue(Cell cell, Object o, CellStyle style) {
//        // 设置样式
//        cell.setCellStyle(style);
//        // 数据为空时
//        if (o == null) {
//            cell.setCellType(CellType.STRING);
//            cell.setCellValue("");
//            return CELL_OTHER;
//        }
//        // 是否为字符串
//        if (o instanceof String) {
//            String s = o.toString();
//            // 当数字类型长度超过8位时，改为字符串类型显示（Excel数字超过一定长度会显示为科学计数法）
//            if (isNumeric(s) && s.length() < 8) {
//                cell.setCellType(CellType.NUMERIC);
//                cell.setCellValue(Double.parseDouble(s));
//                return CELL_OTHER;
//            } else {
//                cell.setCellType(CellType.STRING);
//                cell.setCellValue(s);
//            }
//            if (s.equals(ROW_MERGE)) {
//                return CELL_ROW_MERGE;
//            } else if (s.equals(COLUMN_MERGE)) {
//                return CELL_COLUMN_MERGE;
//            } else {
//                return CELL_OTHER;
//            }
//        }
//        // 是否为字符串
//        if (o instanceof Integer || o instanceof Long || o instanceof Double || o instanceof Float) {
//            cell.setCellType(CellType.NUMERIC);
//            cell.setCellValue(Double.parseDouble(o.toString()));
//            return CELL_OTHER;
//        }
//        // 是否为Boolean
//        if (o instanceof Boolean) {
//            cell.setCellType(CellType.BOOLEAN);
//            cell.setCellValue((Boolean) o);
//            return CELL_OTHER;
//        }
//        // 如果是BigDecimal，则默认3位小数
//        if (o instanceof BigDecimal) {
//            cell.setCellType(CellType.NUMERIC);
//            cell.setCellValue(((BigDecimal) o).setScale(3, RoundingMode.HALF_UP).doubleValue());
//            return CELL_OTHER;
//        }
//        // 如果是Date数据，则显示格式化数据
//        if (o instanceof Date) {
//            cell.setCellType(CellType.STRING);
//            cell.setCellValue(formatDate((Date) o));
//            return CELL_OTHER;
//        }
//        // 如果是其他，则默认字符串类型
//        cell.setCellType(CellType.STRING);
//        cell.setCellValue(o.toString());
//        return CELL_OTHER;
//    }
//
//    private static void setCellPicture(SXSSFWorkbook wb, Row sr, Drawing<?> patriarch, int x, int y, URL url) {
//        // 设置图片宽高
//        sr.setHeight((short) (IMG_WIDTH * IMG_HEIGHT));
//        // （jdk1.7版本try中定义流可自动关闭）
//        try (InputStream is = url.openStream(); ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
//            byte[] buff = new byte[BYTES_DEFAULT_LENGTH];
//            int rc;
//            while ((rc = is.read(buff, 0, BYTES_DEFAULT_LENGTH)) > 0) {
//                outputStream.write(buff, 0, rc);
//            }
//            // 设置图片位置
//            XSSFClientAnchor anchor = new XSSFClientAnchor(0, 0, 0, 0, y, x, y + 1, x + 1);
//            // 设置这个，图片会自动填满单元格的长宽
//            anchor.setAnchorType(AnchorType.MOVE_AND_RESIZE);
//            patriarch.createPicture(anchor, wb.addPicture(outputStream.toByteArray(), HSSFWorkbook.PICTURE_TYPE_JPEG));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    private static String formatDate(Date date) {
//        if (date == null) {
//            return "";
//        }
//        SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT);
//        return format.format(date);
//    }
//
//    private static void setSelect(Sheet sheet, Map<Integer, List<String>> selectMap) {
//        if (selectMap == null || selectMap.isEmpty()) {
//            return;
//        }
//        Set<Entry<Integer, List<String>>> entrySet = selectMap.entrySet();
//        for (Entry<Integer, List<String>> entry : entrySet) {
//            int y = entry.getKey();
//            List<String> list = entry.getValue();
//            if (list == null || list.isEmpty()) {
//                continue;
//            }
//            String[] arr = new String[list.size()];
//            for (int i = 0; i < list.size(); i++) {
//                arr[i] = list.get(i);
//            }
//            DataValidationHelper helper = sheet.getDataValidationHelper();
//            CellRangeAddressList addressList = new CellRangeAddressList(1, 65000, y, y);
//            DataValidationConstraint dvc = helper.createExplicitListConstraint(arr);
//            DataValidation dv = helper.createValidation(dvc, addressList);
//            if (dv instanceof HSSFDataValidation) {
//                dv.setSuppressDropDownArrow(false);
//            } else {
//                dv.setSuppressDropDownArrow(true);
//                dv.setShowErrorBox(true);
//            }
//            sheet.addValidationData(dv);
//        }
//    }
//
//    private static boolean isNumeric(String str) {
//        if (Objects.nonNull(str) && "0.0".equals(str)) {
//            return true;
//        }
//        for (int i = str.length(); --i >= 0; ) {
//            if (!Character.isDigit(str.charAt(i))) {
//                return false;
//            }
//        }
//        return true;
//    }
//
//    private static String getString(String s) {
//        if (s == null) {
//            return "";
//        }
//        if (s.isEmpty()) {
//            return s;
//        }
//        return s.trim();
//    }








//    public static File exportFileMultiTypeSheet(String filePath, String fileName, Map<String, List<?>> sheetData) {
//        SXSSFWorkbook workbook = new SXSSFWorkbook();
//        FileOutputStream fos = null;
//
//        try {
//            for (Map.Entry<String, List<?>> entry : sheetData.entrySet()) {
//                String sheetName = entry.getKey();
//                List<?> dataList = entry.getValue();
//
//                // 创建Sheet，无论数据是否为空
//                Sheet sheet = workbook.createSheet(sheetName);
//                Row headerRow = sheet.createRow(0);
//
//                // 获取字段列表（尝试从第一个元素获取Class，如果列表为空则使用默认处理）
//                Class<?> clazz = dataList != null && !dataList.isEmpty()
//                        ? dataList.get(0).getClass()
//                        : Object.class; // 默认使用Object.class，但需要进一步处理
//
//                // 特殊处理：如果数据为空，尝试通过反射获取可能的目标类（例如MedicalRecord.class）
//                if (dataList == null || dataList.isEmpty()) {
//                    // 这里假设你的sheetName和类名有某种映射关系，或者需要其他方式获取目标类
//                    // 例如：可以通过sheetName推断类名，或者要求调用方传入类信息
//                    // 由于当前方法签名没有类信息，我们只能创建一个只有默认表头的空sheet
//                    // 如果需要更精确的空表头，需要修改方法签名或使用其他方式传递类信息
//                    Cell cell = headerRow.createCell(0);
//                    cell.setCellValue("无数据");
//                    continue; // 跳过后续处理
//                }
//
//                Field[] fields = clazz.getDeclaredFields();
//                List<Field> exportFields = new ArrayList<>();
//                int colIndex = 0;
//
//                // 设置表头
//                for (Field field : fields) {
//                    if (!field.isAnnotationPresent(ExcelExport.class)) {
//                        continue;
//                    }
//
//                    field.setAccessible(true);
//                    exportFields.add(field);
//
//                    ExcelExport annotation = field.getAnnotation(ExcelExport.class);
//                    String header = annotation.value();
//                    Cell cell = headerRow.createCell(colIndex++);
//                    cell.setCellValue(header == null || header.isEmpty() ? field.getName() : header);
//                }
//
//                // 设置内容（只有数据不为空时才填充）
//                if (dataList != null && !dataList.isEmpty()) {
//                    for (int i = 0; i < dataList.size(); i++) {
//                        Object obj = dataList.get(i);
//                        Row dataRow = sheet.createRow(i + 1);
//
//                        for (int j = 0; j < exportFields.size(); j++) {
//                            Field field = exportFields.get(j);
//                            Cell cell = dataRow.createCell(j);
//
//                            Object value = field.get(obj);
//                            cell.setCellValue(value == null ? "" : value.toString());
//                        }
//                    }
//                }
//            }
//
//            // 创建目录
//            File dir = new File(filePath);
//            if (!dir.exists()) {
//                dir.mkdirs();
//            }
//
//            // 构建完整文件路径
//            File file = new File(dir, fileName + ".xlsx");
//            fos = new FileOutputStream(file);
//            workbook.write(fos);
//            return file;
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            throw new RuntimeException("导出失败: " + e.getMessage(), e);
//        } finally {
//            if (fos != null) {
//                try {
//                    fos.close();
//                } catch (IOException ignored) {}
//            }
//            try {
//                workbook.dispose();
//            } catch (Exception ignored) {}
//        }
//    }






//public static File exportFileMultiTypeSheet(String filePath, String fileName, Map<String, List<?>> sheetData) {
//    SXSSFWorkbook workbook = new SXSSFWorkbook();
//    FileOutputStream fos = null;
//
//    try {
//        for (Map.Entry<String, List<?>> entry : sheetData.entrySet()) {
//            String sheetName = entry.getKey();
//            List<?> dataList = entry.getValue();
//
//            if (dataList == null || dataList.isEmpty()) {
//                Sheet sheet = workbook.createSheet(sheetName);
//                Row row = sheet.createRow(0);
//                Cell cell = row.createCell(0);
//                cell.setCellValue("无数据");
//                continue;
//            }
//
//            Class<?> clazz = dataList.get(0).getClass();
//            Field[] fields = clazz.getDeclaredFields();
//            List<Field> exportFields = new ArrayList<>();
//
//            for (Field field : fields) {
//                if (field.isAnnotationPresent(ExcelExport.class)) {
//                    field.setAccessible(true);
//                    exportFields.add(field);
//                }
//            }
//
//            Sheet sheet = workbook.createSheet(sheetName);
//            int colOffset = 0;
//
//            for (Object obj : dataList) {
//                for (int i = 0; i < exportFields.size(); i++) {
//                    Field field = exportFields.get(i);
//                    ExcelExport annotation = field.getAnnotation(ExcelExport.class);
//                    String header = annotation.value();
//                    String fieldName = (header == null || header.isEmpty()) ? field.getName() : header;
//
//                    Row row = sheet.getRow(i);
//                    if (row == null) {
//                        row = sheet.createRow(i);
//                    }
//
//                    Cell nameCell = row.createCell(0);
//                    if (nameCell.getStringCellValue() == null || nameCell.getStringCellValue().isEmpty()) {
//                        nameCell.setCellValue(fieldName);
//                    }
//
//                    Cell valueCell = row.createCell(colOffset + 1);
//                    Object value = field.get(obj);
//
//                    // 新增：调用统一格式化方法
//                    if (value instanceof List && !((List<?>) value).isEmpty() && ((List<?>) value).get(0) instanceof URL) {
//                        @SuppressWarnings("unchecked")
//                        List<URL> imageUrls = (List<URL>) value;
//                        Drawing<?> drawing = sheet.createDrawingPatriarch();
//                        CreationHelper helper = workbook.getCreationHelper();
//                        int imageRow = i;
//
//                        for (int imgIndex = 0; imgIndex < imageUrls.size(); imgIndex++) {
//                            URL imgUrl = imageUrls.get(imgIndex);
//                            try {
//                                byte[] imgBytes = downloadImage(imgUrl.toString());
//                                if (imgBytes != null) {
//                                    int pictureIdx = workbook.addPicture(imgBytes, Workbook.PICTURE_TYPE_JPEG);
//                                    ClientAnchor anchor = helper.createClientAnchor();
//                                    anchor.setCol1(colOffset + imgIndex + 1); // 每张图放在不同列
//                                    anchor.setRow1(i);                        // 同一行
//                                    Picture pict = drawing.createPicture(anchor, pictureIdx);
//                                    pict.resize(0.01);
//                                }
//                            } catch (Exception ex) {
//                                System.err.println("图片插入失败: " + imgUrl);
//                                ex.printStackTrace();
//                            }
//                            imageRow++;
//                        }
//                    } // ✅ 新增对 List<String> 的处理逻辑
//                    else if (value instanceof List && !((List<?>) value).isEmpty() && ((List<?>) value).get(0) instanceof String) {
//                        @SuppressWarnings("unchecked")
//                        List<String> stringList = (List<String>) value;
//                        for (int strIndex = 0; strIndex < stringList.size(); strIndex++) {
//                            Cell listCell = row.createCell(colOffset + 1 + strIndex); // 当前数据列 + 自增列
//                            listCell.setCellValue(stringList.get(strIndex));
//                        }
//                    }
//                    else {
//                        String displayValue = formatValue(value, annotation);
//                        valueCell.setCellValue(displayValue);
//                    }
//                }
//                colOffset++;
//            }
//        }
//
//        File dir = new File(filePath);
//        if (!dir.exists()) {
//            dir.mkdirs();
//        }
//
//        File file = new File(dir, fileName + ".xlsx");
//        fos = new FileOutputStream(file);
//        workbook.write(fos);
//        return file;
//
//    } catch (Exception e) {
//        e.printStackTrace();
//        throw new RuntimeException("导出失败: " + e.getMessage(), e);
//    } finally {
//        if (fos != null) {
//            try {
//                fos.close();
//            } catch (IOException ignored) {}
//        }
//        try {
//            workbook.dispose();
//        } catch (Exception ignored) {}
//    }
//}
//public static File exportFileMergedObjectsToOneSheet(String filePath, String fileName, List<List<?>> dataLists) {
//    SXSSFWorkbook workbook = new SXSSFWorkbook();
//    Sheet sheet = workbook.createSheet(fileName);
//    Drawing<?> drawing = sheet.createDrawingPatriarch(); // 图片绘制对象
//    int currentRow = 0;
//
//    try {
//        List<String> allHeaders = new ArrayList<>();
//        List<List<Field>> fieldPerList = new ArrayList<>();
//        List<List<ExcelExport>> annoPerList = new ArrayList<>();
//
//        for (List<?> dataList : dataLists) {
//            if (dataList.isEmpty()) {
//                fieldPerList.add(Collections.emptyList());
//                annoPerList.add(Collections.emptyList());
//                continue;
//            }
//            Class<?> clazz = dataList.get(0).getClass();
//            List<Field> exportFields = new ArrayList<>();
//            List<ExcelExport> annotations = new ArrayList<>();
//
//            for (Field field : clazz.getDeclaredFields()) {
//                if (field.isAnnotationPresent(ExcelExport.class)) {
//                    field.setAccessible(true);
//                    ExcelExport anno = field.getAnnotation(ExcelExport.class);
//                    exportFields.add(field);
//                    annotations.add(anno);
//                    allHeaders.add(anno.value().isEmpty() ? field.getName() : anno.value());
//                }
//            }
//
//            fieldPerList.add(exportFields);
//            annoPerList.add(annotations);
//        }
//
//        // 写表头
//        Row headerRow = sheet.createRow(currentRow++);
//        headerRow.createCell(0).setCellValue("序号");
//        for (int i = 0; i < allHeaders.size(); i++) {
//            headerRow.createCell(i + 1).setCellValue(allHeaders.get(i));
//        }
//
//        int maxRows = dataLists.stream().mapToInt(List::size).max().orElse(0);
//        CreationHelper helper = workbook.getCreationHelper();
//
//        for (int i = 0; i < maxRows; i++) {
//            Row row = sheet.createRow(currentRow++);
//            row.createCell(0).setCellValue(i + 1);
//            int cellIndex = 1;
//
//            for (int j = 0; j < dataLists.size(); j++) {
//                List<?> list = dataLists.get(j);
//                List<Field> fields = fieldPerList.get(j);
//                List<ExcelExport> annos = annoPerList.get(j);
//                Object obj = i < list.size() ? list.get(i) : null;
//
//                for (int k = 0; k < fields.size(); k++) {
//                    Field field = fields.get(k);
//                    ExcelExport anno = annos.get(k);
//                    Cell cell = row.createCell(cellIndex++);
//
//                    if (obj == null) {
//                        cell.setCellValue("");
//                        continue;
//                    }
//
//                    Object value = field.get(obj);
//
//                    if (anno.isImage()) {
//                        if (value instanceof URL) {
//                            insertImage(sheet, drawing, helper, (URL) value, currentRow - 1, cell.getColumnIndex());
//                        } else if (value instanceof List<?>) {
//                            List<?> urls = (List<?>) value;
//                            for (Object u : urls) {
//                                if (u instanceof URL) {
//                                    insertImage(sheet, drawing, helper, (URL) u, currentRow - 1, cell.getColumnIndex());
//                                }
//                            }
//                        }
//                        continue;
//                    }
//
//                    if (!anno.kv().isEmpty()) {
//                        Map<String, String> kvMap = Arrays.stream(anno.kv().split(","))
//                                .map(s -> s.split("="))
//                                .filter(p -> p.length == 2)
//                                .collect(Collectors.toMap(p -> p[0], p -> p[1]));
//                        cell.setCellValue(kvMap.getOrDefault(String.valueOf(value), String.valueOf(value)));
//                    } else {
//                        cell.setCellValue(value == null ? "" : value.toString());
//                    }
//                }
//            }
//        }
//
//        File dir = new File(filePath);
//        if (!dir.exists()) dir.mkdirs();
//        File file = new File(dir, fileName + ".xlsx");
//        try (FileOutputStream fos = new FileOutputStream(file)) {
//            workbook.write(fos);
//        }
//        return file;
//    } catch (Exception e) {
//        throw new RuntimeException("导出失败：" + e.getMessage(), e);
//    } finally {
//        try {
//            workbook.dispose();
//        } catch (Exception ignored) {}
//    }
//}
//public static File exportFileMergedObjectsToOneSheet(String filePath, String fileName, List<List<?>> dataLists) {
//    SXSSFWorkbook workbook = new SXSSFWorkbook();
//    Sheet sheet = workbook.createSheet(fileName);
//    Drawing<?> drawing = sheet.createDrawingPatriarch();
//    int currentRow = 0;
//
//    try {
//        List<String> allHeaders = new ArrayList<>();
//        List<List<Field>> fieldPerList = new ArrayList<>();
//        List<List<ExcelExport>> annoPerList = new ArrayList<>();
//
//        for (List<?> dataList : dataLists) {
//            if (dataList.isEmpty()) {
//                fieldPerList.add(Collections.emptyList());
//                annoPerList.add(Collections.emptyList());
//                continue;
//            }
//            Class<?> clazz = dataList.get(0).getClass();
//            List<Field> exportFields = new ArrayList<>();
//            List<ExcelExport> annotations = new ArrayList<>();
//
//            for (Field field : clazz.getDeclaredFields()) {
//                if (field.isAnnotationPresent(ExcelExport.class)) {
//                    field.setAccessible(true);
//                    ExcelExport anno = field.getAnnotation(ExcelExport.class);
//                    exportFields.add(field);
//                    annotations.add(anno);
//                    allHeaders.add(anno.value().isEmpty() ? field.getName() : anno.value());
//                }
//            }
//
//            fieldPerList.add(exportFields);
//            annoPerList.add(annotations);
//        }
//
//        // 表头
//        Row headerRow = sheet.createRow(currentRow++);
//        headerRow.createCell(0).setCellValue("序号");
//        for (int i = 0; i < allHeaders.size(); i++) {
//            headerRow.createCell(i + 1).setCellValue(allHeaders.get(i));
//        }
//
//        int maxRows = dataLists.stream().mapToInt(List::size).max().orElse(0);
//        CreationHelper helper = workbook.getCreationHelper();
//
//        for (int i = 0; i < maxRows; i++) {
//            Row row = sheet.createRow(currentRow++);
//            row.createCell(0).setCellValue(i + 1);
//            int cellIndex = 1;
//
//            for (int j = 0; j < dataLists.size(); j++) {
//                List<?> list = dataLists.get(j);
//                List<Field> fields = fieldPerList.get(j);
//                List<ExcelExport> annos = annoPerList.get(j);
//                Object obj = i < list.size() ? list.get(i) : null;
//
//                for (int k = 0; k < fields.size(); k++) {
//                    Field field = fields.get(k);
//                    ExcelExport anno = annos.get(k);
//                    Cell cell = row.createCell(cellIndex++);
//
//                    if (obj == null) {
//                        cell.setCellValue("");
//                        continue;
//                    }
//
//                    Object value = field.get(obj);
//
//                    if (anno.isImage()) {
//                        try {
//                            List<BufferedImage> imageList = new ArrayList<>();
//
//                            if (value instanceof URL) {
//                                BufferedImage image = ImageIO.read((URL) value);
//                                if (image != null) {
//                                    imageList.add(image);
//                                }
//                            } else if (value instanceof List<?>) {
//                                for (Object u : (List<?>) value) {
//                                    if (u instanceof URL) {
//                                        BufferedImage image = ImageIO.read((URL) u);
//                                        if (image != null) {
//                                            imageList.add(image);
//                                        }
//                                    }
//                                }
//                            }
//
//                            if (!imageList.isEmpty()) {
//                                BufferedImage merged = mergeImagesVertically(imageList);
//                                ByteArrayOutputStream baos = new ByteArrayOutputStream();
//                                if (ImageIO.write(merged, "jpg", baos)) {
//                                    byte[] imageBytes = baos.toByteArray();
//                                    int pictureIdx = workbook.addPicture(imageBytes, Workbook.PICTURE_TYPE_JPEG);
//                                    ClientAnchor anchor = helper.createClientAnchor();
//                                    anchor.setCol1(cell.getColumnIndex());
//                                    anchor.setRow1(row.getRowNum());
//                                    anchor.setCol2(cell.getColumnIndex() + 1);
//                                    anchor.setRow2(row.getRowNum() + 1);
//                                    drawing.createPicture(anchor, pictureIdx);
//                                }
//                            }
//                        } catch (Exception e) {
//                            cell.setCellValue("图片加载失败");
//                            e.printStackTrace(); // 添加错误日志以便调试
//                        }
//                        continue;
//                    }
//
//                    // ✅ 使用统一格式化方法处理 kv 映射 & 日期等
//                    String formatted = formatValue(value, anno);
//                    cell.setCellValue(formatted);
//                }
//            }
//        }
//
//        File dir = new File(filePath);
//        if (!dir.exists()) dir.mkdirs();
//        File file = new File(dir, fileName + ".xlsx");
//        try (FileOutputStream fos = new FileOutputStream(file)) {
//            workbook.write(fos);
//        }
//        return file;
//    } catch (Exception e) {
//        throw new RuntimeException("导出失败：" + e.getMessage(), e);
//    } finally {
//        try {
//            workbook.dispose();
//            // ② 完全关闭 Workbook，释放内存（POI 4.x+ 支持）
//            workbook.close();
//        } catch (Exception ignored) {}
//        // ③ 清空静态缓存，断开所有对大对象的引用
//        GetMessageServiceImpl.ExcelDoctor.clear();
//        GetMessageServiceImpl.ExcelMedicalRecord.clear();
//        GetMessageServiceImpl.ExcelHealthRecord.clear();
//        GetMessageServiceImpl.ExcelStemiINdication.clear();
//        GetMessageServiceImpl.ExcelChestPainScteening.clear();
//    }
//}
   /**
    *   直接本地导出
    * @param
    * @return
   */
public static File exportFileMergedObjectsToOneSheet(
        String filePath,
        String fileName,
        List<List<?>> dataLists
) {
    SXSSFWorkbook workbook = new SXSSFWorkbook();
    Sheet sheet = workbook.createSheet(fileName);
    Drawing<?> drawing = sheet.createDrawingPatriarch();
    int currentRow = 0;

    try {
        // 1. 先为每个子列表，找出非 null 的第一个元素来决定它的“类”与注解字段
        List<String>                allHeaders   = new ArrayList<>();
        List<List<Field>>           fieldPerList = new ArrayList<>();
        List<List<ExcelExport>>     annoPerList  = new ArrayList<>();

        for (List<?> dataList : dataLists) {
            // 如果完全为空或全是 null，就跳过
            if (dataList.isEmpty() || dataList.stream().allMatch(Objects::isNull)) {
                fieldPerList.add(Collections.emptyList());
                annoPerList .add(Collections.emptyList());
                continue;
            }

            // 找到第一个非 null 对象，来反射获取它的类和注解字段
            Object sample = dataList.stream()
                    .filter(Objects::nonNull)
                    .findFirst()
                    .orElseThrow();  // 上面已保证至少一个非 null

            Class<?> clazz = sample.getClass();
            List<Field>       exportFields = new ArrayList<>();
            List<ExcelExport> annotations  = new ArrayList<>();

            for (Field field : clazz.getDeclaredFields()) {
                if (field.isAnnotationPresent(ExcelExport.class)) {
                    field.setAccessible(true);
                    ExcelExport anno = field.getAnnotation(ExcelExport.class);
                    exportFields.add(field);
                    annotations .add(anno);
                    allHeaders.add(
                            anno.value().isEmpty()
                                    ? field.getName()
                                    : anno.value()
                    );
                }
            }

            fieldPerList.add(exportFields);
            annoPerList .add(annotations);
        }

        // 2. 创建表头
        Row headerRow = sheet.createRow(currentRow++);
        headerRow.createCell(0).setCellValue("序号");
        for (int i = 0; i < allHeaders.size(); i++) {
            headerRow.createCell(i + 1).setCellValue(allHeaders.get(i));
        }

        int maxRows = dataLists.stream()
                .mapToInt(List::size)
                .max()
                .orElse(0);
        CreationHelper helper = workbook.getCreationHelper();

        // 3. 填充数据行
        for (int i = 0; i < maxRows; i++) {
            Row row = sheet.createRow(currentRow++);
            row.createCell(0).setCellValue(i + 1);
            int cellIndex = 1;

            for (int j = 0; j < dataLists.size(); j++) {
                List<?>             list  = dataLists.get(j);
                List<Field>         fields= fieldPerList.get(j);
                List<ExcelExport>   annos = annoPerList .get(j);

                // 该列表如果没有字段，直接跳过固定列数
                if (fields.isEmpty()) {
                    continue;
                }

                Object obj = i < list.size() ? list.get(i) : null;
                for (int k = 0; k < fields.size(); k++) {
                    Field       field = fields.get(k);
                    ExcelExport anno  = annos .get(k);
                    Cell cell = row.createCell(cellIndex++);

                    // 1) 如果该行该列无对象，写空
                    if (obj == null) {
                        cell.setCellValue("");
                        continue;
                    }

                    Object value = field.get(obj);
                    // 2) 图片字段
                    if (anno.isImage()) {
                        try {
                            List<BufferedImage> imageList = new ArrayList<>();

                            if (value instanceof URL) {
                                BufferedImage image = ImageIO.read((URL) value);
                                if (image != null) {
                                    imageList.add(image);
                                }
                            } else if (value instanceof List<?>) {
                                for (Object u : (List<?>) value) {
                                    if (u instanceof URL) {
                                        BufferedImage image = ImageIO.read((URL) u);
                                        if (image != null) {
                                            imageList.add(image);
                                        }
                                    }
                                }
                            }

                            if (!imageList.isEmpty()) {
                                BufferedImage merged = mergeImagesVertically(imageList);
                                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                if (ImageIO.write(merged, "jpg", baos)) {
                                    byte[] imageBytes = baos.toByteArray();
                                    int pictureIdx = workbook.addPicture(imageBytes, Workbook.PICTURE_TYPE_JPEG);
                                    ClientAnchor anchor = helper.createClientAnchor();
                                    anchor.setCol1(cell.getColumnIndex());
                                    anchor.setRow1(row.getRowNum());
                                    anchor.setCol2(cell.getColumnIndex() + 1);
                                    anchor.setRow2(row.getRowNum() + 1);
                                    drawing.createPicture(anchor, pictureIdx);
                                }
                            }
                        } catch (Exception e) {
                            cell.setCellValue("图片加载失败");
                            e.printStackTrace(); // 添加错误日志以便调试
                        }
                        continue;
                    }

                    // 3) 其它字段 —— 调用统一格式化
                    String formatted = formatValue(value, anno);
                    cell.setCellValue(formatted);
                    CellStyle cellStyle = workbook.createCellStyle();
                    cellStyle.setWrapText(true);
                    cell.setCellStyle(cellStyle);
                }
            }
        }

        // 4. 写出文件并返回
        File dir = new File(filePath);
        if (!dir.exists()) dir.mkdirs();
        File file = new File(dir, fileName + ".xlsx");
        try (FileOutputStream fos = new FileOutputStream(file)) {
            workbook.write(fos);
        }
        return file;

    } catch (Exception e) {
        throw new RuntimeException("导出失败：" + e.getMessage(), e);

    } finally {
        // —— 释放资源 ——
        try {
            workbook.dispose();
            workbook.close();
        } catch (IOException ignored) {}
    }
}
/**
 *   直接提供给前端下载链接
 * @param
 * @return
*/
public static void exportExcelToResponse(
        HttpServletResponse response,
        String fileName,
        List<List<?>> dataLists
) {
    SXSSFWorkbook workbook = new SXSSFWorkbook();
    Sheet sheet = workbook.createSheet(fileName);
    Drawing<?> drawing = sheet.createDrawingPatriarch();
    int currentRow = 0;

    try {
        List<String> allHeaders = new ArrayList<>();
        List<List<Field>> fieldPerList = new ArrayList<>();
        List<List<ExcelExport>> annoPerList = new ArrayList<>();

        for (List<?> dataList : dataLists) {
            if (dataList.isEmpty() || dataList.stream().allMatch(Objects::isNull)) {
                fieldPerList.add(Collections.emptyList());
                annoPerList.add(Collections.emptyList());
                continue;
            }

            Object sample = dataList.stream().filter(Objects::nonNull).findFirst().orElseThrow();
            Class<?> clazz = sample.getClass();
            List<Field> exportFields = new ArrayList<>();
            List<ExcelExport> annotations = new ArrayList<>();

            for (Field field : clazz.getDeclaredFields()) {
                if (field.isAnnotationPresent(ExcelExport.class)) {
                    field.setAccessible(true);
                    ExcelExport anno = field.getAnnotation(ExcelExport.class);
                    exportFields.add(field);
                    annotations.add(anno);
                    allHeaders.add(anno.value().isEmpty() ? field.getName() : anno.value());
                }
            }

            fieldPerList.add(exportFields);
            annoPerList.add(annotations);
        }

        // 表头
        Row headerRow = sheet.createRow(currentRow++);
        headerRow.createCell(0).setCellValue("序号");
        for (int i = 0; i < allHeaders.size(); i++) {
            headerRow.createCell(i + 1).setCellValue(allHeaders.get(i));
        }

        int maxRows = dataLists.stream().mapToInt(List::size).max().orElse(0);
        CreationHelper helper = workbook.getCreationHelper();

        for (int i = 0; i < maxRows; i++) {
            Row row = sheet.createRow(currentRow++);
            row.createCell(0).setCellValue(i + 1);
            int cellIndex = 1;

            for (int j = 0; j < dataLists.size(); j++) {
                List<?> list = dataLists.get(j);
                List<Field> fields = fieldPerList.get(j);
                List<ExcelExport> annos = annoPerList.get(j);

                if (fields.isEmpty()) continue;
                Object obj = i < list.size() ? list.get(i) : null;

                for (int k = 0; k < fields.size(); k++) {
                    Field field = fields.get(k);
                    ExcelExport anno = annos.get(k);
                    Cell cell = row.createCell(cellIndex++);

                    if (obj == null) {
                        cell.setCellValue("");
                        continue;
                    }

                    Object value = field.get(obj);

                    if (anno.isImage()) {
                        try {
                            List<BufferedImage> imageList = new ArrayList<>();

                            if (value instanceof URL) {
                                BufferedImage img = ImageIO.read((URL) value);
                                if (img != null) imageList.add(img);
                            } else if (value instanceof List<?>) {
                                for (Object u : (List<?>) value) {
                                    if (u instanceof URL) {
                                        BufferedImage img = ImageIO.read((URL) u);
                                        if (img != null) imageList.add(img);
                                    }
                                }
                            }

                            if (!imageList.isEmpty()) {
                                BufferedImage merged = mergeImagesVertically(imageList);
                                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                ImageIO.write(merged, "jpg", baos);
                                byte[] bytes = baos.toByteArray();

                                int pictureIdx = workbook.addPicture(bytes, Workbook.PICTURE_TYPE_JPEG);
                                ClientAnchor anchor = helper.createClientAnchor();
                                anchor.setCol1(cell.getColumnIndex());
                                anchor.setRow1(row.getRowNum());
                                anchor.setCol2(cell.getColumnIndex() + 1);
                                anchor.setRow2(row.getRowNum() + 1);
                                drawing.createPicture(anchor, pictureIdx);
                            }
                        } catch (Exception e) {
                            cell.setCellValue("图片加载失败");
                        }
                        continue;
                    }

                    String formatted = formatValue(value, anno);
                    cell.setCellValue(formatted);
                }
            }
        }

        // 设置响应头：告诉浏览器进行下载
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("UTF-8");
        String encodedFileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8).replaceAll("\\+", "%20");
        response.setHeader("Content-Disposition", "attachment; filename*=UTF-8''" + encodedFileName + ".xlsx");

        // 写入响应流
        try (OutputStream os = response.getOutputStream()) {
            workbook.write(os);
            os.flush();
        }

    } catch (Exception e) {
        throw new RuntimeException("导出失败：" + e.getMessage(), e);
    } finally {
        try {
            workbook.dispose();
            workbook.close();
        } catch (IOException ignored) {}
    }
}
/**
 *邮件相关导出Excel
 * @param
 * @return
*/
public static byte[] exportFileMergedObjectsOnEmail(
        String fileName,
        List<List<?>> dataLists
) {
    SXSSFWorkbook workbook = new SXSSFWorkbook();
    Sheet sheet = workbook.createSheet(fileName);
    Drawing<?> drawing = sheet.createDrawingPatriarch();
    int currentRow = 0;

    try {
        // ...（原有的逻辑，包括创建表头和填充数据）
        // 1. 先为每个子列表，找出非 null 的第一个元素来决定它的“类”与注解字段
        List<String>                allHeaders   = new ArrayList<>();
        List<List<Field>>           fieldPerList = new ArrayList<>();
        List<List<ExcelExport>>     annoPerList  = new ArrayList<>();

        for (List<?> dataList : dataLists) {
            // 如果完全为空或全是 null，就跳过
            if (dataList.isEmpty() || dataList.stream().allMatch(Objects::isNull)) {
                fieldPerList.add(Collections.emptyList());
                annoPerList .add(Collections.emptyList());
                continue;
            }

            // 找到第一个非 null 对象，来反射获取它的类和注解字段
            Object sample = dataList.stream()
                    .filter(Objects::nonNull)
                    .findFirst()
                    .orElseThrow();  // 上面已保证至少一个非 null

            Class<?> clazz = sample.getClass();
            List<Field>       exportFields = new ArrayList<>();
            List<ExcelExport> annotations  = new ArrayList<>();

            for (Field field : clazz.getDeclaredFields()) {
                if (field.isAnnotationPresent(ExcelExport.class)) {
                    field.setAccessible(true);
                    ExcelExport anno = field.getAnnotation(ExcelExport.class);
                    exportFields.add(field);
                    annotations .add(anno);
                    allHeaders.add(
                            anno.value().isEmpty()
                                    ? field.getName()
                                    : anno.value()
                    );
                }
            }

            fieldPerList.add(exportFields);
            annoPerList .add(annotations);
        }

        // 2. 创建表头
        Row headerRow = sheet.createRow(currentRow++);
        headerRow.createCell(0).setCellValue("序号");
        for (int i = 0; i < allHeaders.size(); i++) {
            headerRow.createCell(i + 1).setCellValue(allHeaders.get(i));
        }

        int maxRows = dataLists.stream()
                .mapToInt(List::size)
                .max()
                .orElse(0);
        CreationHelper helper = workbook.getCreationHelper();

        // 3. 填充数据行
        for (int i = 0; i < maxRows; i++) {
            Row row = sheet.createRow(currentRow++);
            row.createCell(0).setCellValue(i + 1);
            int cellIndex = 1;

            for (int j = 0; j < dataLists.size(); j++) {
                List<?>             list  = dataLists.get(j);
                List<Field>         fields= fieldPerList.get(j);
                List<ExcelExport>   annos = annoPerList .get(j);

                // 该列表如果没有字段，直接跳过固定列数
                if (fields.isEmpty()) {
                    continue;
                }

                Object obj = i < list.size() ? list.get(i) : null;
                for (int k = 0; k < fields.size(); k++) {
                    Field       field = fields.get(k);
                    ExcelExport anno  = annos .get(k);
                    Cell cell = row.createCell(cellIndex++);

                    // 1) 如果该行该列无对象，写空
                    if (obj == null) {
                        cell.setCellValue("");
                        continue;
                    }

                    Object value = field.get(obj);
                    // 2) 图片字段
                    if (anno.isImage()) {
                        try {
                            List<BufferedImage> imageList = new ArrayList<>();

                            if (value instanceof URL) {
                                BufferedImage image = ImageIO.read((URL) value);
                                if (image != null) {
                                    imageList.add(image);
                                }
                            } else if (value instanceof List<?>) {
                                for (Object u : (List<?>) value) {
                                    if (u instanceof URL) {
                                        BufferedImage image = ImageIO.read((URL) u);
                                        if (image != null) {
                                            imageList.add(image);
                                        }
                                    }
                                }
                            }

                            if (!imageList.isEmpty()) {
                                BufferedImage merged = mergeImagesVertically(imageList);
                                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                if (ImageIO.write(merged, "jpg", baos)) {
                                    byte[] imageBytes = baos.toByteArray();
                                    int pictureIdx = workbook.addPicture(imageBytes, Workbook.PICTURE_TYPE_JPEG);
                                    ClientAnchor anchor = helper.createClientAnchor();
                                    anchor.setCol1(cell.getColumnIndex());
                                    anchor.setRow1(row.getRowNum());
                                    anchor.setCol2(cell.getColumnIndex() + 1);
                                    anchor.setRow2(row.getRowNum() + 1);
                                    drawing.createPicture(anchor, pictureIdx);
                                }
                            }
                        } catch (Exception e) {
                            cell.setCellValue("图片加载失败");
                            e.printStackTrace(); // 添加错误日志以便调试
                        }
                        continue;
                    }

                    // 3) 其它字段 —— 调用统一格式化
                    String formatted = formatValue(value, anno);
                    cell.setCellValue(formatted);
                    CellStyle cellStyle = workbook.createCellStyle();
                    cellStyle.setWrapText(true);
                    cell.setCellStyle(cellStyle);
                }
            }
        }
        // 写入 ByteArrayOutputStream
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            workbook.write(baos);
            return baos.toByteArray();
        }
    } catch (Exception e) {
        throw new RuntimeException("导出失败：" + e.getMessage(), e);
    } finally {
        try {
            workbook.dispose();
            workbook.close();
        } catch (IOException ignored) {}
    }
}
/**
 *   发送邮件
 * @param
 * @return
*/
public static void sendExcelByEmail(
        byte[] excelBytes,
        String fileName,
        String toEmail,
        String subject,
        String bodyText
) throws MessagingException {
    // 配置邮件服务器
    Properties props = new Properties();
    props.put("mail.smtp.host", "smtp.qq.com");
    props.put("mail.smtp.port", "465");
    props.put("mail.smtp.auth", "true");
    props.put("mail.smtp.ssl.enable", "true");

    // 创建会话
    Session session = Session.getInstance(props, new Authenticator() {
        protected PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication("3123496296@qq.com", "tqulxqjledwfdgjf");
        }
    });

    // 创建邮件消息
    Message message = new MimeMessage(session);
    message.setFrom(new InternetAddress("3123496296@qq.com"));
    message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
    message.setSubject(subject);

    // 创建邮件内容
    MimeBodyPart textPart = new MimeBodyPart();
    textPart.setText(bodyText);
    // 创建附件
    MimeBodyPart attachmentPart = new MimeBodyPart();
    DataSource source = new ByteArrayDataSource(excelBytes, "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
    attachmentPart.setDataHandler(new DataHandler(source));
    attachmentPart.setFileName(fileName + ".xlsx");

    // 组合邮件内容和附件
    Multipart multipart = new MimeMultipart();
    multipart.addBodyPart(textPart);
    multipart.addBodyPart(attachmentPart);

    message.setContent(multipart);

    // 发送邮件
    Transport.send(message);
}

    //多图插入解决
    private static BufferedImage mergeImagesVertically(List<BufferedImage> images) {
        if (images == null || images.isEmpty()) {
            return new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);
        }

        int width = images.stream().mapToInt(BufferedImage::getWidth).max().orElse(100);
        int totalHeight = images.stream().mapToInt(BufferedImage::getHeight).sum();

        BufferedImage combined = new BufferedImage(width, totalHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = combined.createGraphics();
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, width, totalHeight); // 填充白色背景

        int currentY = 0;
        for (BufferedImage img : images) {
            if (img != null) {
                g.drawImage(img, 0, currentY, width, img.getHeight(), null);
                currentY += img.getHeight();
            }
        }

        g.dispose();
        return combined;
    }


    /**
     * 统一格式化字段值的方法
     */
    private static String formatValue(Object value, ExcelExport annotation) {
        if (value == null) return "";

        // 优先处理 kv 映射
        String kv = annotation.kv();
        if (!kv.isEmpty()) {
            Map<String, String> kvMap = parseKvMapping(kv);
            String mapped = kvMap.get(value.toString());
            if (mapped != null) {
                return mapped;
            }
        }

        // 处理时间格式，重点改造点
        if (value instanceof LocalDateTime) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            return ((LocalDateTime) value).format(formatter);
        }

        // 新增：处理 List<String>，用换行符拼接
        if (value instanceof List<?>) {
            List<?> list = (List<?>) value;
            if (!list.isEmpty() && list.get(0) instanceof String) {
                return list.stream()
                        .map(Object::toString)
                        .collect(Collectors.joining("\n"));
            }
        }

        // 默认调用toString()
        return value.toString();
    }


    private static Map<String, String> parseKvMapping(String kv) {
        Map<String, String> map = new HashMap<>();
        String[] pairs = kv.split(";");
        for (String pair : pairs) {
            String[] kvArray = pair.split("-", 2); // 限制最多分成2段，防止值中有"-"
            if (kvArray.length == 2) {
                map.put(kvArray[0].trim(), kvArray[1].trim());
            }
        }
        return map;
    }
    public static byte[] downloadImage(String imageUrl) throws IOException {
        URL url = new URL(imageUrl);
        BufferedImage image = ImageIO.read(url);
        if (image == null) {
            throw new IOException("Image could not be read from " + imageUrl);
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(image, "jpg", baos); // 重新编码为标准 JPEG
        return baos.toByteArray();
    }



}
