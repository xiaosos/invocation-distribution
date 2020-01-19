package com.cbhb.detect.dto;

import com.cbhb.detect.constants.SysRetCodeConstants;
import com.cbhb.invocation.commons.result.AbstractRequest;
import com.cbhb.invocation.commons.tool.exception.ValidateException;
import org.apache.commons.lang3.StringUtils;

public class Detect90Request extends AbstractRequest {
    private String cardnum;

    public String getCardnum() {
        return cardnum;
    }

    public void setCardnum(String cardnum) {
        this.cardnum = cardnum;
    }

    @Override
    public void requestCheck() {
        if(null==cardnum || StringUtils.isBlank(cardnum)){
            throw new ValidateException(
                    SysRetCodeConstants.REQUEST_CHECK_FAILURE.getCode(),
                    SysRetCodeConstants.REQUEST_CHECK_FAILURE.getMessage());
        }
    }
}
