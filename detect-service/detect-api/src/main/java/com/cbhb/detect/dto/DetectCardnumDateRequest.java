package com.cbhb.detect.dto;

import com.cbhb.detect.constants.SysRetCodeConstants;
import com.cbhb.invocation.commons.result.AbstractRequest;
import com.cbhb.invocation.commons.tool.exception.ValidateException;
import org.apache.commons.lang3.StringUtils;

public class DetectCardnumDateRequest extends AbstractRequest{

    public String getCardnum() {
        return cardnum;
    }

    public void setCardnum(String cardnum) {
        this.cardnum = cardnum;
    }

    private String cardnum;
    private String date;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public void requestCheck() {
        if(null == cardnum || StringUtils.isBlank(cardnum)|| null == date || StringUtils.isBlank(date))
        {
            throw new ValidateException(
                    SysRetCodeConstants.REQUEST_CHECK_FAILURE.getCode(),
                    SysRetCodeConstants.REQUEST_CHECK_FAILURE.getMessage());
        }
    }
}
