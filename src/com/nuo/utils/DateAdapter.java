package com.nuo.utils;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateAdapter implements JsonDeserializer<Date> {
	private final DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	public Date deserialize(JsonElement arg0, Type arg1,
			JsonDeserializationContext arg2) throws JsonParseException {
			return new Date(arg0.getAsBigDecimal().longValue());
	}
}