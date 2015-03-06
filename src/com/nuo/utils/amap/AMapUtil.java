/**
 * 
 */
package com.nuo.utils.amap;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.text.Html;
import android.text.Spanned;
import android.widget.EditText;

import com.amap.api.maps.model.LatLng;
import com.amap.api.services.core.LatLonPoint;

public class AMapUtil {
	/**
	 * 判断edittext是否null
	 */
	public static String checkEditText(EditText editText) {
		if (editText != null && editText.getText() != null
				&& !(editText.getText().toString().trim().equals(""))) {
			return editText.getText().toString().trim();
		} else {
			return "";
		}
	}

	public static Spanned stringToSpan(String src) {
		return src == null ? null : Html.fromHtml(src.replace("\n", "<br />"));
	}

	public static String colorFont(String src, String color) {
		StringBuffer strBuf = new StringBuffer();

		strBuf.append("<font color=").append(color).append(">").append(src)
				.append("</font>");
		return strBuf.toString();
	}

	public static String makeHtmlNewLine() {
		return "<br />";
	}

	public static String makeHtmlSpace(int number) {
		final String space = "&nbsp;";
		StringBuilder result = new StringBuilder();
		for (int i = 0; i < number; i++) {
			result.append(space);
		}
		return result.toString();
	}

	public static String getFriendlyLength(int lenMeter) {
		if (lenMeter > 10000) // 10 km
		{
			int dis = lenMeter / 1000;
			return dis + ChString.Kilometer;
		}

		if (lenMeter > 1000) {
			float dis = (float) lenMeter / 1000;
			DecimalFormat fnum = new DecimalFormat("##0.0");
			String dstr = fnum.format(dis);
			return dstr + ChString.Kilometer;
		}

		if (lenMeter > 100) {
			int dis = lenMeter / 50 * 50;
			return dis + ChString.Meter;
		}

		int dis = lenMeter / 10 * 10;
		if (dis == 0) {
			dis = 10;
		}

		return dis + ChString.Meter;
	}

	public static boolean IsEmptyOrNullString(String s) {
		return (s == null) || (s.trim().length() == 0);
	}

	/**
	 * 把LatLng对象转化为LatLonPoint对象
	 */
	public static LatLonPoint convertToLatLonPoint(LatLng latlon) {
		return new LatLonPoint(latlon.latitude, latlon.longitude);
	}

	/**
	 * 把LatLonPoint对象转化为LatLon对象
	 */
	public static LatLng convertToLatLng(LatLonPoint latLonPoint) {
		return new LatLng(latLonPoint.getLatitude(), latLonPoint.getLongitude());
	}

	/**
	 * 把集合体的LatLonPoint转化为集合体的LatLng
	 */
	public static ArrayList<LatLng> convertArrList(List<LatLonPoint> shapes) {
		ArrayList<LatLng> lineShapes = new ArrayList<LatLng>();
		for (LatLonPoint point : shapes) {
			LatLng latLngTemp = AMapUtil.convertToLatLng(point);
			lineShapes.add(latLngTemp);
		}
		return lineShapes;
	}

	/**
	 * long类型时间格式化
	 */
	public static String convertToTime(long time) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date(time);
		return df.format(date);
	}
    private static final double x_pi = 3.14159265358979324 * 3000.0 / 180.0;
    public static double[] bd_encrypt(double gg_lat, double gg_lon, double bd_lat, double bd_lon)
    {
        double x = gg_lon, y = gg_lat;
        double z = Math.sqrt(x * x + y * y) + 0.00002 * Math.sin(y * x_pi);
        double theta = Math.atan2(y, x) + 0.000003 * Math.cos(x * x_pi);
        //bd_lon = z * Math.cos(theta) + 0.0065;
       // bd_lat = z * Math.sin(theta) + 0.006;
        //  point{lat,lon}
        double[] point ={z * Math.sin(theta) + 0.006,z * Math.cos(theta) + 0.0065};
        return point;
    }


    public static final String HtmlBlack = "#000000";
	public static final String HtmlGray = "#808080";




    public static final String DAY_NIGHT_MODE="daynightmode";
    public static final String DEVIATION="deviationrecalculation";
    public static final String JAM="jamrecalculation";
    public static final String TRAFFIC="trafficbroadcast";
    public static final String CAMERA="camerabroadcast";
    public static final String SCREEN="screenon";
    public static final String THEME="theme";
    public static final String ISEMULATOR="isemulator";


    public static final String ACTIVITYINDEX="activityindex";

    public static final int SIMPLEHUDNAVIE=0;
    public static final int EMULATORNAVI=1;
    public static final int SIMPLEGPSNAVI=2;
    public static final int SIMPLEROUTENAVI=3;


    public static final boolean DAY_MODE=false;
    public static final boolean NIGHT_MODE=true;
    public static final boolean YES_MODE=true;
    public static final boolean NO_MODE=false;
    public static final boolean OPEN_MODE=true;
    public static final boolean CLOSE_MODE=false;
}
