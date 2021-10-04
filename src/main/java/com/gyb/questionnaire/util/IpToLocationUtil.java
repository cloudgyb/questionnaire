package com.gyb.questionnaire.util;

import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.maxmind.geoip2.model.CityResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;

/**
 * ip查询位置信息工具类
 * <p>使用GeoIP2 数据库查询地理位置信息,GeoIP2 数据库内部维护了IP地址到地理位置信息的对应关系，
 * GeoIP2提供了非常便利的API。</p>
 *
 * @author cloudgyb
 * @since 2021/10/4 17:38
 */
public class IpToLocationUtil {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private DatabaseReader reader;
    private volatile static IpToLocationUtil instance;

    public IpToLocationUtil() {
        ClassPathResource resource = new ClassPathResource("/geoip/GeoLite2-City.mmdb");
        InputStream inputStream;
        try {
            inputStream = resource.getInputStream();
            reader = new DatabaseReader.Builder(inputStream).build();
        } catch (IOException e) {
            logger.error("初始化失败！", e);
        }
    }

    /**
     * 根据ip地址查询地理位置信息
     *
     * @param ip ipV4地址
     * @return CityResponse entity
     */
    public CityResponse ipToLocation(String ip) {
        CityResponse response = null;
        try {
            InetAddress ipAddress = InetAddress.getByName(ip);
            response = reader.city(ipAddress);
        } catch (IOException | GeoIp2Exception e) {
            logger.warn("通过ip（{}）查询地址位置信息错误！{}", ip, e.getLocalizedMessage());
        }
        return response;
    }

    public static IpToLocationUtil getInstance() {
        if (instance == null) {
            synchronized (IpToLocationUtil.class) {
                if (instance == null) {
                    instance = new IpToLocationUtil();
                }
            }
        }
        return instance;
    }
}
