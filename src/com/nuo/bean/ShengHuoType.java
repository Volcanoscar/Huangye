package com.nuo.bean;

import android.content.Intent;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 14-12-29.
 * 本地生活类型
 */
public class ShengHuoType {

    private String typeName;
    private String level;
    private Integer orderBy;
    private String level1Code;
    private String level2Code;
    private String level3Code;
    private Integer isParent;
    private String typeCode;
    private List<ShengHuoType> level4List;

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getLevel1Code() {
        return level1Code;
    }

    public void setLevel1Code(String level1Code) {
        this.level1Code = level1Code;
    }

    public String getLevel2Code() {
        return level2Code;
    }

    public void setLevel2Code(String level2Code) {
        this.level2Code = level2Code;
    }

    public String getLevel3Code() {
        return level3Code;
    }

    public void setLevel3Code(String level3Code) {
        this.level3Code = level3Code;
    }

    public Integer getIsParent() {
        return isParent;
    }

    public void setIsParent(Integer isParent) {
        this.isParent = isParent;
    }

    public String getTypeCode() {
        return typeCode;
    }

    public void setTypeCode(String typeCode) {
        this.typeCode = typeCode;
    }

    public List<ShengHuoType> getLevel4List() {
        return level4List;
    }

    public void setLevel4List(List<ShengHuoType> level4List) {
        this.level4List = level4List;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public Integer getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(Integer orderBy) {
        this.orderBy = orderBy;
    }

    public static List<ShengHuoType> parseMap(String result) {
        Gson gson = new Gson();
      /*  Map map = gson.fromJson(result, Map.class);
        if ("false".equals(map.get("result").toString())) {
            return null;
        }*/
        return gson.fromJson(result, new TypeToken<List<ShengHuoType>>(){}.getType());
    }
}
