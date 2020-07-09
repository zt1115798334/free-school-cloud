package com.example.serviceadditional.service;

import com.example.common.base.service.BaseConstant;
import com.example.common.dto.output.*;
import com.example.datasource.entity.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.List;

public interface TopicService extends BaseConstant {

    InformationOutputDTO resultInformationDto(Information information, Long userId);

    List<InformationOutputDTO> resultInformationDtoList(List<Information> list, Long userId);
}
