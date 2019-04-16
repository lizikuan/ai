package com.kuan.controller;

import com.alibaba.fastjson.JSON;
import com.kuan.util.Base64Util;
import com.kuan.util.FileUtil;
import com.kuan.util.HttpUtil;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

@RestController
public class AianimalController {
private static String APPid="jwsGDViuUusZe5YjuS2qQ7u2";
private static String APPsecret="GIj8CXM8XCjFQDGGcAVscfTLUGvkAWxG";
    public static String getAuth() {
        String clientId = APPid;
        // 官网获取的 Secret Key 更新为你注册的
        String clientSecret = APPsecret;
        return getAuth(clientId, clientSecret);
    }
    public static String getAuth(String ak, String sk) {
        // 获取token地址
        String authHost = "https://aip.baidubce.com/oauth/2.0/token?";
        String getAccessTokenUrl = authHost
                // 1. grant_type为固定参数
                + "grant_type=client_credentials"
                // 2. 官网获取的 API Key
                + "&client_id=" + ak
                // 3. 官网获取的 Secret Key
                + "&client_secret=" + sk;
        try {
            URL realUrl = new URL(getAccessTokenUrl);
            // 打开和URL之间的连接
            HttpURLConnection connection = (HttpURLConnection) realUrl.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            // 获取所有响应头字段
            Map<String, List<String>> map = connection.getHeaderFields();
            // 遍历所有的响应头字段
            for (String key : map.keySet()) {
                System.err.println(key + "--->" + map.get(key));
            }
            // 定义 BufferedReader输入流来读取URL的响应
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String result = "";
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
            /**
             * 返回结果示例
             */
            System.err.println("result:" + result);
            JSONObject jsonObject = new JSONObject(result);
            String access_token = jsonObject.getString("access_token");
            System.out.println(access_token);
            return access_token;
        } catch (Exception e) {
            System.err.printf("获取token失败！");
            e.printStackTrace(System.err);
        }
        return null;
    }

    @RequestMapping(value = "/aiLoad",method = { RequestMethod.POST,RequestMethod.GET})
    public static String animal() {
        // 请求url
        String url = "https://aip.baidubce.com/rest/2.0/image-classify/v1/animal";
        try {
            // 本地文件路径
            String filePath = "E:/image/animal.jpg";
            byte[] imgData =FileUtil.readFileByBytes(filePath);
            String imgStr = Base64Util.encode(imgData);
            String imgParam = URLEncoder.encode(imgStr, "UTF-8");

            String param = "image=" + imgParam + "&top_num=" + 1+"&baike_num="+1;

            // 注意这里仅为了简化编码每一次请求都去获取access_token，线上环境access_token有过期时间， 客户端可自行缓存，过期后重新获取。
            String accessToken = getAuth();

            String result = HttpUtil.post(url, accessToken, param);
            com.alibaba.fastjson.JSONObject jsonObject = JSON.parseObject(result);
            String string = jsonObject.getString("result");
            System.out.println(string);
            return string;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {
        animal();
    }
}
