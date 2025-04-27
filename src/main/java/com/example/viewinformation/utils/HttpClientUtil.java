package com.example.viewinformation.utils;

import com.alibaba.fastjson.JSONObject;
import jodd.http.HttpRequest;
import jodd.http.HttpResponse;
import java.nio.charset.StandardCharsets;

import java.util.Map;

/**
 * Http工具类
 */
public class HttpClientUtil {
    /**
     * @param url 调用接口
     * @param map 查询条件
     * @return String
     */
    public static String post(String url, Map<String, Object> map) {
//        String param = JSONObject.toJSONString(map);
        HttpResponse res = HttpRequest.post(url).connectionTimeout(90000).timeout(90000)
                .contentType("application/json", "UTF-8").bodyText(JSONObject.toJSONString(map))
                .send();
        res.charset("utf-8");
        return res.bodyText();
    }

    public static String post(String url, String jsonStr) {
        HttpResponse resp = HttpRequest.post(url).connectionTimeout(60000).timeout(60000)
                .contentType("application/json", StandardCharsets.UTF_8.toString()).body(jsonStr)
                .send();
        resp.charset(StandardCharsets.UTF_8.toString());
        return resp.bodyText();
    }

    /**
     * 发送post请求
     * @param url    : 请求的连接
     * @return
     */
    public static String post(String url) {
        try {
            HttpResponse response = HttpRequest.post(url).send(); // 发送post请求
            response.charset("UTF-8"); // 设置响应的字符集为UTF-8
            return response.bodyText(); // 返回响应正文
        } catch (Exception e) {
            e.printStackTrace(); // 打印异常信息
            return null; // 发生异常时返回null或抛出自定义异常
        }
    }
    public static String get(String url) {
        try {
            HttpResponse response = HttpRequest.get(url).send(); // 发送get请求
            response.charset("UTF-8"); // 设置响应的字符集为UTF-8
            return response.bodyText(); // 返回响应正文
        } catch (Exception e) {
            e.printStackTrace(); // 打印异常信息
            return null; // 发生异常时返回null或抛出自定义异常
        }
    }
}

//public class HttpClientUtil {

//    static final  int TIMEOUT_MSEC = 5 * 1000;
//
//    /**
//     * 发送GET方式请求
//     * @param url
//     * @param paramMap
//     * @return
//     */
//    public static String doGet(String url,Map<String,String> paramMap){
//        // 创建Httpclient对象
//        CloseableHttpClient httpClient = HttpClients.createDefault();
//
//        String result = "";
//        CloseableHttpResponse response = null;
//
//        try{
//            URIBuilder builder = new URIBuilder(url);
//            if(paramMap != null){
//                for (String key : paramMap.keySet()) {
//                    builder.addParameter(key,paramMap.get(key));
//                }
//            }
//            URI uri = builder.build();
//
//            //创建GET请求
//            HttpGet httpGet = new HttpGet(uri);
//
//            //发送请求
//            response = httpClient.execute(httpGet);
//
//            //判断响应状态
//            if(response.getStatusLine().getStatusCode() == 200){
//                result = EntityUtils.toString(response.getEntity(),"UTF-8");
//            }
//        }catch (Exception e){
//            e.printStackTrace();
//        }finally {
//            try {
//                response.close();
//                httpClient.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//
//        return result;
//    }
//
//    /**
//     * 发送POST方式请求
//     * @param url
//     * @param paramMap
//     * @return
//     * @throws IOException
//     */
//    public static String doPost(String url, Map<String, String> paramMap) throws IOException {
//        // 创建Httpclient对象
//        CloseableHttpClient httpClient = HttpClients.createDefault();
//        CloseableHttpResponse response = null;
//        String resultString = "";
//
//        try {
//            // 创建Http Post请求
//            HttpPost httpPost = new HttpPost(url);
//
//            // 创建参数列表
//            if (paramMap != null) {
//                List<NameValuePair> paramList = new ArrayList();
//                for (Map.Entry<String, String> param : paramMap.entrySet()) {
//                    paramList.add(new BasicNameValuePair(param.getKey(), param.getValue()));
//                }
//                // 模拟表单
//                UrlEncodedFormEntity entity = new UrlEncodedFormEntity(paramList);
//                httpPost.setEntity(entity);
//            }
//
//            httpPost.setConfig(builderRequestConfig());
//
//            // 执行http请求
//            response = httpClient.execute(httpPost);
//
//            resultString = EntityUtils.toString(response.getEntity(), "UTF-8");
//        } catch (Exception e) {
//            throw e;
//        } finally {
//            try {
//                response.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//
//        return resultString;
//    }
//
//    /**
//     * 发送POST方式请求
//     * @param url
//     * @param paramMap
//     * @return
//     * @throws IOException
//     */
//    public static String doPost4Json(String url, Map<String, String> paramMap) throws IOException {
//        // 创建Httpclient对象
//        CloseableHttpClient httpClient = HttpClients.createDefault();
//        CloseableHttpResponse response = null;
//        String resultString = "";
//
//        try {
//            // 创建Http Post请求
//            HttpPost httpPost = new HttpPost(url);
//
//            if (paramMap != null) {
//                //构造json格式数据
//                JSONObject jsonObject = new JSONObject();
//                for (Map.Entry<String, String> param : paramMap.entrySet()) {
//                    jsonObject.put(param.getKey(),param.getValue());
//                }
//                StringEntity entity = new StringEntity(jsonObject.toString(),"utf-8");
//                //设置请求编码
//                entity.setContentEncoding("utf-8");
//                //设置数据类型
//                entity.setContentType("application/json");
//                httpPost.setEntity(entity);
//            }
//
//            httpPost.setConfig(builderRequestConfig());
//
//            // 执行http请求
//            response = httpClient.execute(httpPost);
//
//            resultString = EntityUtils.toString(response.getEntity(), "UTF-8");
//        } catch (Exception e) {
//            throw e;
//        } finally {
//            try {
//                response.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//
//        return resultString;
//    }
//    private static RequestConfig builderRequestConfig() {
//        return RequestConfig.custom()
//                .setConnectTimeout(TIMEOUT_MSEC)
//                .setConnectionRequestTimeout(TIMEOUT_MSEC)
//                .setSocketTimeout(TIMEOUT_MSEC).build();
//    }

//}


