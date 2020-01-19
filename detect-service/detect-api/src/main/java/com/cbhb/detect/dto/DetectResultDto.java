package com.cbhb.detect.dto;

public class DetectResultDto {
    private Integer id;
    private String cardnum;
    private String date;
    private String message;
    private DetectType detectType;
    private Integer amt;
    private String detectTime;
    private String pid;
    private String url;
    private String detectContent;

    public String getDetectContent() {
        return detectContent;
    }

    public void setDetectContent(String detectContent) {
        this.detectContent = detectContent;
    }

    @Override
    public String toString() {
        return "DetectResultDto{" +
                "id=" + id +
                ", cardnum='" + cardnum + '\'' +
                ", date='" + date + '\'' +
                ", message='" + message + '\'' +
                ", detectType=" + detectType +
                ", amt=" + amt +
                ", detectTime='" + detectTime + '\'' +
                ", pid='" + pid + '\'' +
                ", url='" + url + '\'' +
                '}';
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCardnum() {
        return cardnum;
    }

    public void setCardnum(String cardnum) {
        this.cardnum = cardnum;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public DetectType getDetectType() {
        return detectType;
    }

    public void setDetectType(DetectType detectType) {
        this.detectType = detectType;
    }

    public Integer getAmt() {
        return amt;
    }

    public void setAmt(Integer amt) {
        this.amt = amt;
    }

    public String getDetectTime() {
        return detectTime;
    }

    public void setDetectTime(String detectTime) {
        this.detectTime = detectTime;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public  static enum DetectType{
        ALREADY,//已经开过的
        CAN,//可以开
        NORECORD,//没充值记录
        ERROR//异常情况

    }
}
