package com.cbhb.invocation.controller;

import com.cbhb.detect.IDetectService;
import com.cbhb.detect.dto.DetectCardnumDateRequest;
import com.cbhb.detect.dto.DetectCardnumDateResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/invo")
public class DetectController {


    @Reference
    IDetectService detectService;

    Log log = LogFactory.getLog(DetectController.class);

    @RequestMapping("/detect")
    public String detect(){

        System.out.println("detect.come");
        DetectCardnumDateRequest request = new DetectCardnumDateRequest();
        request.setCardnum("362494787");
        request.setDate("20191217");
        DetectCardnumDateResponse detectCardnumDateResponse = detectService.detectCardnumDate(request);
        log.info(detectCardnumDateResponse);

        return "invocation.detect";
    }
}
