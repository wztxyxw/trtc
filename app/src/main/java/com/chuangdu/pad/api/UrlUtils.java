package com.chuangdu.pad.api;

import com.chuangdu.pad.models.UrlModel;

import java.util.ArrayList;

/**
 * @author sc
 */
public class UrlUtils {


    public static String API_ROOT_UPLOAD = "http://api-pdd-test.jinpai56.com:8080/";
    public static String API_ROOT = "http://api-pdd-test.jinpai56.com:8080/";
    public static String API_MAIN = "http://api-pdd-test.jinpai56.com:8080/";

//    public static String API_MAIN = "https://api-pdd.jinpai56.com/";
    public static String API_MAIN_TEST = "http://api-pdd-test.jinpai56.com:8080/";
//    public static String API_MAIN = "http://192.168.1.124:8280/";
//    public static String API_MAIN = "http://192.168.1.131:8280/";
    public static String APP_URL = "";
    public static String APP_DOWNLOAD_URL = "http://www.jinpai56.com";

    public static ArrayList<UrlModel> getUrlModels() {
        ArrayList<UrlModel> models = new ArrayList<>();
        models.add(new UrlModel("测试", "http://api-pdd-test.jinpai56.com:8080/"));
        models.add(new UrlModel("线上", "http://api-pdd-test.jinpai56.com:8080/"));
        return models;
    }
}
