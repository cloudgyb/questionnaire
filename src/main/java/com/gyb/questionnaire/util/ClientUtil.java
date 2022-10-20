package com.gyb.questionnaire.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gyb.questionnaire.dto.AddressInfo;
import com.maxmind.geoip2.model.CityResponse;
import com.maxmind.geoip2.record.City;
import com.maxmind.geoip2.record.Country;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;

/**
 * 客户端工具类
 * 根据User-Agent请求头获取客户端信息，不保证获取到的客户端信息一定正确
 * <p>
 * <li>Mozilla/5.0 (Linux; Android 10; Redmi 8A Build/QKQ1.191014.001; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/77.0.3865.120 MQQBrowser/6.2 TBS/045410 Mobile Safari/537.36 V1_AND_SQ_8.4.10_1524_YYB_D QQ/8.4.10.4875 NetType/WIFI WebP/0.3.0 Pixel/720 StatusBarHeight/56 SimpleUISwitch/0 QQTheme/2105 InMagicWin/0</li>
 * <li>Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/85.0.4183.102 Safari/537.36</li>
 * <li>Mozilla/5.0 (Linux; Android 9; vivo X21A Build/PKQ1.180819.001; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/77.0.3865.120 MQQBrowser/6.2 TBS/045410 Mobile Safari/537.36 V1_AND_SQ_8.4.18_1558_YYB_D QQ/8.4.18.4945 NetType/WIFI WebP/0.3.0 Pixel/1080 StatusBarHeight/85 SimpleUISwitch/0 QQTheme/1000 InMagicWin/0</li>
 * <li>Mozilla/5.0 (Linux; Android 9; vivo X21A Build/PKQ1.180819.001; wv) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/62.0.3202.84 Mobile Safari/537.36 VivoBrowser/8.6.12.9</li>
 * <li>Mozilla/5.0 (Linux; Android 6.0; Nexus 5 Build/MRA58N) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/85.0.4183.121 Mobile Safari/537.36</li>
 * <li>Mozilla/5.0 (iPhone; CPU iPhone OS 10_3_1 like Mac OS X) AppleWebKit/603.1.30 (KHTML, like Gecko) Version/10.0 Mobile/14E304 Safari/602.1</li>
 * <li>Mozilla/5.0 (iPhone; CPU iPhone OS 13_2_3 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/13.0.3 Mobile/15E148 Safari/604.1</li>
 * <li>Mozilla/5.0 (iPhone; CPU iPhone OS 13_2_3 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/13.0.3 Mobile/15E148 Safari/604.1</li>
 * <li>Mozilla/5.0 (iPhone; CPU iPhone OS 13_2_3 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/13.0.3 Mobile/15E148 Safari/604.1</li>
 * <li>Mozilla/5.0 (iPad; CPU OS 11_0 like Mac OS X) AppleWebKit/604.1.34 (KHTML, like Gecko) Version/11.0 Mobile/15A5341f Safari/604.1</li>
 * <li>Mozilla/5.0 (iPad; CPU OS 11_0 like Mac OS X) AppleWebKit/604.1.34 (KHTML, like Gecko) Version/11.0 Mobile/15A5341f Safari/604.1</li>
 * <li>Mozilla/5.0 (X11; Linux x86_64; rv:81.0) Gecko/20100101 Firefox/81.li0
 * </p>
 *
 * @author geng
 * 2020/11/22
 */
public final class ClientUtil {
    public static Client getClientInfo() {
        final Client client = new Client();
        final HttpServletRequest request = HttpServletUtil.getRequest();
        final String userAgent = request.getHeader("User-Agent");
        if (userAgent != null && !"".equals(userAgent)) {
            client.setName(getClientName(userAgent));
            client.setSystem(getClientSystem(userAgent));
            client.setIp(getClientIp(request));
            client.setMobile(isMobileClient(userAgent));
        }
        return client;
    }

    private static String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip))
            ip = request.getHeader("x-real-ip");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        if (ip != null && ip.contains(",")) {
            ip = ip.split(",")[0];
        }
        return ip;
    }

    @SuppressWarnings("unused")
    public static String getClientIp() {
        final HttpServletRequest request = HttpServletUtil.getRequest();
        return getClientIp(request);
    }

    private static boolean isMobileClient(String userAgent) {
        return userAgent.contains("Mobile");
    }

    public static boolean isMobileClient() {
        final HttpServletRequest request = HttpServletUtil.getRequest();
        final String userAgent = request.getHeader("User-Agent");
        if (userAgent != null && !"".equals(userAgent)) {
            return isMobileClient(userAgent);
        }
        return false;
    }

    private static String getClientSystem(String userAgent) {
        if (userAgent.contains("Android"))
            return "Android";
        if (userAgent.contains("iPhone"))
            return "iPhone";
        if (userAgent.contains("iPad"))
            return "iPad";
        if (userAgent.contains("Linux"))
            return "Linux";
        if (userAgent.contains("Windows"))
            return "Windows";
        return "其他";
    }

    @SuppressWarnings("unused")
    public static String getClientSystem() {
        final HttpServletRequest request = HttpServletUtil.getRequest();
        final String userAgent = request.getHeader("User-Agent");
        if (userAgent != null && !"".equals(userAgent)) {
            return getClientSystem(userAgent);
        }
        return "未知";
    }

    private static String getClientName(String userAgent) {
        if (userAgent.contains("Chrome"))
            return "Chrome";
        if (userAgent.contains("Firefox"))
            return "Firefox";
        return "其他";
    }

    public static String getClientName() {
        final HttpServletRequest request = HttpServletUtil.getRequest();
        final String userAgent = request.getHeader("User-Agent");
        if (userAgent != null && !"".equals(userAgent)) {
            return getClientName(userAgent);
        }
        return "未知";
    }

    /**
     * 通过http接口获取地址位置信息的方法不再推荐使用，请使用{@link #getPositionByIp(String)}方法
     */
    @Deprecated
    public static String getPositionByIP(String ip) {
        if (ip == null || "".equals(ip))
            return "";
        if ("127.0.0.1".equals(ip))
            return "本机";
        String url = "http://whois.pconline.com.cn/ipJson.jsp?json=true&ip=" + ip;
        try {
            final RestTemplate restTemplate = new RestTemplate();
            String resp = restTemplate.getForObject(url, String.class);
            ObjectMapper objectMapper = new ObjectMapper();
            AddressInfo address = objectMapper.readValue(resp, AddressInfo.class);
            String addressStr = "";
            if (address != null) {
                if (StringUtils.hasText(address.getPro())) {
                    addressStr += address.getPro();
                    addressStr += " ";
                }
                if (StringUtils.hasText(address.getCity())) {
                    addressStr += address.getCity();
                    addressStr += " ";
                }
                if (StringUtils.hasText(address.getRegion())) {
                    addressStr += address.getRegion();
                }
            }
            addressStr = addressStr.trim();
            return addressStr;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String getPositionByIp(String ip) {
        if ("127.0.0.1".equals(ip))
            return "本机";
        final char delimiter = '·';
        CityResponse cityResponse = IpToLocationUtil.getInstance().ipToLocation(ip);
        StringBuilder sb = new StringBuilder();
        if (cityResponse != null) {
            final Country country = cityResponse.getCountry();
            if (country != null && country.getNames() != null) {
                String countryName = country.getNames().get("zh-CN");
                if (countryName != null && !"".equals(countryName)) {
                    sb.append(delimiter).append(countryName);
                }
            }
            final City city = cityResponse.getCity();
            if (city != null && city.getNames() != null) {
                String cityName = city.getNames().get("zh-CN");
                if (cityName != null && !"".equals(cityName)) {
                    sb.append(delimiter).append(cityName);
                }
            }
            return sb.toString().replaceFirst(delimiter + "", "");
        }
        return "未知";
    }

}
