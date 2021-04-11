package com.gyb.questionnaire.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

/**
 * 调查问卷答卷问题答案实体
 *
 * @author gengyb
 * 2020/11/21
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Paper {
    private Long id;
    private Long questionnaireId;
    private Date submitTime;
    //答卷耗时，单位秒
    private int elapsedTime;
    private String elapsedTimeText;
    private int source;
    private String sourceText;
    private String ip;
    private String address;

    public void setElapsedTime(int elapsedTime) {
        this.elapsedTime = elapsedTime;
        this.elapsedTimeText = toTimeText(elapsedTime);
    }

    private String toTimeText(int elapsedTime) {
        int m = elapsedTime/60;
        int h = m/60;
        m = m%60;
        int s = elapsedTime%60;
        String res;
        if(h == 0)
            res = String.format("%02d:%02d", m,s);
        else
            res = String.format("%02d:%02d:%02d",h,m,s);
        return res;
    }

    public void setSource(int source) {
        this.source = source;
        this.sourceText = toSourceText(source);
    }

    private String toSourceText(int source) {
        switch (source){
            case 0:
                return "链接";
            case 1:
                return "QQ好友分享";
            case 2:
                return "微信分享";
            case 3:
                return "QQ空间分享";
            case 4:
                return "新浪微博分享";
            case 5:
                return "二维码扫码";
            default:
                return "其他";
        }
    }
}
