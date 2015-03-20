package com.nuobuluo.huangye.bean;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

/**
 * Created by zxl on 2015/2/7.
 * 上传图片信息
 */
public class UploadImageInfo {

    private String fieldName;
    private String filePath;

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
    public static List<UploadImageInfo> parseMap(String result) {
        Gson gson = new Gson();
        return gson.fromJson(result, new TypeToken<List<UploadImageInfo>>(){}.getType());
    }
}
