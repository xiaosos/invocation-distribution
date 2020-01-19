package com.cbhb.detect.dto;

import com.cbhb.invocation.commons.result.AbstractResponse;

public class DetectCardnumDateResponse extends AbstractResponse {
    private String cardnum;
    private String date;
    private String message;
    private DetectResultDto.DetectType detectType;
    private Integer amt;
    private String detectTime;

    @Override
    public String toString() {
        return "DetectCardnumDateResponse{" +
                "cardnum='" + cardnum + '\'' +
                ", date='" + date + '\'' +
                ", message='" + message + '\'' +
                ", detectType=" + detectType +
                ", amt=" + amt +
                ", detectTime='" + detectTime + '\'' +
                '}';
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

    public DetectResultDto.DetectType getDetectType() {
        return detectType;
    }

    public void setDetectType(DetectResultDto.DetectType detectType) {
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
}
