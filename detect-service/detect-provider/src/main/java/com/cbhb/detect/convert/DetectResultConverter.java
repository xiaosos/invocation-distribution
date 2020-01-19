package com.cbhb.detect.convert;

import com.cbhb.detect.dal.entity.DetectResult;
import com.cbhb.detect.dto.DetectCardnumDateResponse;
import com.cbhb.detect.dto.DetectResultDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mappings;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 */
@Mapper(componentModel = "spring")
public interface DetectResultConverter {

    @Mappings({})
    DetectResultDto detectResult2dto(DetectResult detectResult);

    @Mappings({})
    DetectResult dto2detect(DetectResultDto dto);

    @Mappings({})
    DetectCardnumDateResponse dto2detectCardnumDateResponse(DetectResultDto dto);

    @Mappings({})
    DetectResultDto detectCardnumDateResponse2dto(DetectCardnumDateResponse response);

    @Mappings({})
    List<DetectResultDto> detectResult2List(List<DetectResult> detectResults);

}
