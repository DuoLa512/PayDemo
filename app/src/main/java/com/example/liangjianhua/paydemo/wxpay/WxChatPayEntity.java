package com.example.liangjianhua.paydemo.wxpay;



/**
 * author: Administrator
 * created on: 2017/10/24 19:03
 * description:
 */
public class WxChatPayEntity {
    private String appid;//应用ID
    private String partnerid;//商户号
    private String prepayid;//预支付交易会话ID
    private String packageValue;//扩展字段
    private String noncestr;//随机字符串
    private String timeStamp;//时间戳
    private String sign;//签名

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getPartnerid() {
        return partnerid;
    }

    public void setPartnerid(String partnerid) {
        this.partnerid = partnerid;
    }

    public String getPrepayid() {
        return prepayid;
    }

    public void setPrepayid(String prepayid) {
        this.prepayid = prepayid;
    }

    public String getPackageValue() {
        return packageValue;
    }

    public void setPackageValue(String packageValue) {
        this.packageValue = packageValue;
    }

    public String getNoncestr() {
        return noncestr;
    }

    public void setNoncestr(String noncestr) {
        this.noncestr = noncestr;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }
}
