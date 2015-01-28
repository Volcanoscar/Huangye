package com.nuo.bean;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

/**
 * Created by zxl on 2015/1/26.
 * 信息搜索对象
 *
 */
public class KeySearchResult {

    private String typeCode;
    private String level1Code;
    private String typeName;
    private Integer count;

    public String getTypeCode() {
        return typeCode;
    }

    public void setTypeCode(String typeCode) {
        this.typeCode = typeCode;
    }

    public String getLevel1Code() {
        return level1Code;
    }

    public void setLevel1Code(String level1Code) {
        this.level1Code = level1Code;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public static List<KeySearchResult> parseMap(String result) {
        Gson gson = new Gson();
        return gson.fromJson(result, new TypeToken<List<KeySearchResult>>(){}.getType());
    }
}
