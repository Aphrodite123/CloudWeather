package com.aphrodite.cloudweather.config;

/**
 * Created by Aphrodite on 2018/6/1.
 */
public class HttpConfig extends BaseConfig {
    public static final String APP_KEY = "24912613";

    public static final String APP_SECRET = "34b513ffabe6a78ccd9215c0c3aa2486";

    public static final String APP_CODE = "e83337fd0dc0408d89dfe6d1fe46756e";

    public static final String ALIYUN_HOST = "http://jisutqybmf.market.alicloudapi.com";

    /**
     * 百度地图key
     */
    public static final String BAIDU_APP_KEY = "uyZiuzQCPmcT6dpOlQs3xQBbn0MjTcbz";

    /**
     * 百度地图Host
     */
    public static final String BAIDU_MAP_HOST = "http://api.map.baidu.com/geocoder";

    /**
     * 天气预报查询接口
     */
    public interface QueryWeather {
        String PATH = "/weather/query";
    }

    /**
     * 获取城市接口
     */
    public interface QueryCities {
        String PATH = "/weather/city";
    }

    public interface Header {
        String AUTHORIZATION_KEY = "Authorization";
    }

    /**
     * city	    STRING	可选	城市（city,cityid,citycode三者任选其一）
     * citycode	STRING	可选	城市天气代号（city,cityid,citycode三者任选其一）
     * cityid	STRING	可选	城市ID（city,cityid,citycode三者任选其一）
     * ip	    STRING	可选	IP
     * location	STRING	可选	经纬度 纬度在前，,分割 如：39.983424,116.322987
     */
    public interface RequestParameters {
        String APP_CODE_KEY = "APPCODE ";
        String CITY_KEY = "city";
        String CITY_CODE_KEY = "citycode";
        String CITY_ID_KEY = "cityid";
        String IP_KEY = "ip";
        String LOCATION_KEY = "location";
    }

}
