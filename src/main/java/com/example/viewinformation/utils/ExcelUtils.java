package com.example.viewinformation.utils;


import java.util.Properties;
import com.example.viewinformation.exception.BaseException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.activation.DataHandler;
import jakarta.activation.DataSource;
import jakarta.mail.*;
import jakarta.mail.internet.*;
import jakarta.mail.util.ByteArrayDataSource;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import javax.imageio.ImageIO;
import jakarta.servlet.http.HttpServletResponse; // ✅
import java.awt.*;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.*;
import java.lang.reflect.Field;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Excel导入导出工具类
 *
 * @author sunnyzyq
 * @date 2021/12/17
 */
@SuppressWarnings("unused")
@Slf4j
public class ExcelUtils {

    private static final String FILE_PATH = "./config/emails.json"; // 你的json文件路径
    private static final ObjectMapper mapper = new ObjectMapper();

    // 加一个全局读写锁
    private static final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    /**
     *   读json中的文件
     * @param
     * @return
    */
    public static List<String> loadEmails() {
        lock.readLock().lock();  // 加读锁
        try {
            File file = new File(FILE_PATH);
            if (!file.exists()) {
                return new ArrayList<>();
            }
            EmailList emailList = mapper.readValue(file, EmailList.class);
            return emailList.getEmails();
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        } finally {
            lock.readLock().unlock();  // 解锁
        }
    }
    /**
     *   写json中的文件
     * @param
     * @return
    */
    public static void saveEmails(String email) {
        lock.writeLock().lock(); // 加写锁
        try {
            // 先读取已有数据
            List<String> existingEmails = new ArrayList<>();
            File file = new File(FILE_PATH);
            if (file.exists()) {
                EmailList existingEmailList = mapper.readValue(file, EmailList.class);
                if (existingEmailList != null && existingEmailList.getEmails() != null) {
                    existingEmails = existingEmailList.getEmails();
                }
            }

            // 检查是否已存在
            if (existingEmails.contains(email)) {
                throw new BaseException("邮箱已经存在："+email);
            } else {
                // 添加新邮箱
                existingEmails.add(email);

                // 写回文件
                EmailList emailList = new EmailList();
                emailList.setEmails(existingEmails);
                mapper.writerWithDefaultPrettyPrinter().writeValue(file, emailList);
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new BaseException("添加邮箱失败：" + e.getMessage());
        } finally {
            lock.writeLock().unlock(); // 解锁
        }
    }
    /**
     * 删除json文件中的指定邮箱
     */
    public static void deleteEmail(String email) {
        lock.writeLock().lock(); // 加写锁
        try {
            File file = new File(FILE_PATH);
            if (!file.exists()) {
                throw new BaseException("邮箱列表文件不存在，无法删除！");
            }

            // 读取已有邮箱列表
            EmailList emailList = mapper.readValue(file, EmailList.class);
            List<String> existingEmails = emailList.getEmails();
            if (existingEmails == null || !existingEmails.remove(email)) {
                throw new BaseException("要删除的邮箱不存在：" + email);
            }

            // 写回更新后的邮箱列表
            emailList.setEmails(existingEmails);
            mapper.writerWithDefaultPrettyPrinter().writeValue(file, emailList);

        } catch (IOException e) {
            e.printStackTrace();
            throw new BaseException("删除邮箱失败：" + e.getMessage());
        } finally {
            lock.writeLock().unlock(); // 解锁
        }
    }



    public static class EmailList {
        private List<String> emails;

        public List<String> getEmails() {
            return emails;
        }

        public void setEmails(List<String> emails) {
            this.emails = emails;
        }
    }
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
//    SXSSFWorkbook workbook = new SXSSFWorkbook();
    XSSFWorkbook workbook = new XSSFWorkbook();

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
        log.info("excel文件准备完毕，准备下载");
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
        log.info("excel文件下载完成");

    } catch (Exception e) {
        throw new RuntimeException("导出失败：" + e.getMessage(), e);
    } finally {
        try {
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
        List<String> toEmails, // 改成 List<String>
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

    // 设置多个收件人
    InternetAddress[] recipientAddresses = new InternetAddress[toEmails.size()];
    for (int i = 0; i < toEmails.size(); i++) {
        recipientAddresses[i] = new InternetAddress(toEmails.get(i));
    }
    message.setRecipients(Message.RecipientType.TO, recipientAddresses);

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
    /**
     *   解析kv映射
     * @param
     * @return
    */
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
    /**
     *   图片相关
     * @param
     * @return
    */
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
