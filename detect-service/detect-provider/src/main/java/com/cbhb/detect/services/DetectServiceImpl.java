package com.cbhb.detect.services;

import com.cbhb.detect.IDetectService;
import com.cbhb.detect.convert.DetectResultConverter;
import com.cbhb.detect.dal.persistence.DetectResultMapper;
import com.cbhb.detect.dto.DetectCardnumDateRequest;
import com.cbhb.detect.dto.DetectCardnumDateResponse;
import com.cbhb.detect.dto.DetectResultDto;
import com.cbhb.detect.util.DetectAndDownload;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class DetectServiceImpl implements IDetectService{

    @Autowired
    private DetectResultMapper detectResultMapper;

    @Autowired
    DetectResultConverter converter ;

    Log log = LogFactory.getLog(DetectServiceImpl.class);
    @Override
    public DetectCardnumDateResponse detectCardnumDate(DetectCardnumDateRequest request) {
        log.info("=================com.cbhb.detect.services.DetectServiceImpl.detectCardnumDate===================================");
        DetectAndDownload dad = new DetectAndDownload();
        DetectResultDto detectResultDto = dad.detectByCardnumDate(request.getCardnum(), request.getDate());
        detectResultMapper.insert(converter.dto2detect(detectResultDto));

        return converter.dto2detectCardnumDateResponse(detectResultDto);

    }
}
