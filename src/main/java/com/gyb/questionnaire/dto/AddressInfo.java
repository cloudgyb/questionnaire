package com.gyb.questionnaire.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 根据ip获取位置信息封装类
 *
 * @author geng
 * 2020/11/25
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AddressInfo {
    private String ip;       //: "127.0.0.1",
    private String pro;      //: "",
    private String proCode;      //: "999999",
    private String city;         //: "",
    private String cityCode;         //: "0",
    private String region;       //: "",
    private String regionCode; //:"0",
    private String addr;         //: " 本机地址",
    private String regionNames;      //: "",
    private String err;      //: "noprovince"
}
