package com.nuobuluo.huangye.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Date;

/**
 * json消息整合
 * 
 * @author way
 * 
 */
public class JsonUtil {
    private static Gson gson = new Gson();

    public static Gson getDateGson(){
		GsonBuilder builder = new GsonBuilder();
		builder.registerTypeAdapter(Date.class, new DateAdapter());
		/*builder.setDateFormat("yyyy-MM-dd HH:mm:ss");*/
		return  builder.create();
	}

	public static Gson getStringDateGson(){
		GsonBuilder builder = new GsonBuilder();
		builder.registerTypeAdapter(Date.class, new StringDateAdapter());
		builder.setDateFormat("yyyy-MM-dd HH:mm:ss");
		return  builder.create();
	}

    public static String toJson(Object obj) {
        return gson.toJson(obj);
    }

}
