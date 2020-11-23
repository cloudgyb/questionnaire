package com.gyb.questionnaire.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 浏览器客户端信息
 *
 * @author geng
 * 2020/11/22
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Client {
    private String name;
    private String system;
    private String ip;
    private boolean isMobile;

    public String getIp() {
        if("0:0:0:0:0:0:0:1".equals(this.ip))
            return "127.0.0.1";
        return this.ip;
    }
}
