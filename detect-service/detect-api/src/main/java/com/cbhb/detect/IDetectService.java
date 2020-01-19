package com.cbhb.detect;

import com.cbhb.detect.dto.DetectCardnumDateRequest;
import com.cbhb.detect.dto.DetectCardnumDateResponse;

public interface IDetectService {
    public DetectCardnumDateResponse detectCardnumDate(DetectCardnumDateRequest detectCardnumDateRequest);
}
