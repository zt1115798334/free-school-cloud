package com.example.serviceinformation.service;

import com.example.common.dto.output.InformationOutputDTO;
import com.example.datasource.base.domain.CustomPage;
import com.example.datasource.base.service.BaseService;
import com.example.datasource.entity.Information;
import org.springframework.data.domain.PageImpl;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2019/07/15 15:11
 * description:
 */
public interface InformationService extends BaseService<Information, Long> {

    InformationOutputDTO saveInformation(Information information);

    void deleteInformation(Long id);

    void modifyInformationSateToNewRelease(Long id);

    void modifyInformationSateToAfterRelease(List<Long> userId);

    void incrementInformationBrowsingVolume(Long id);

    Information findInformation(Long id);

    InformationOutputDTO findRoInformation(Long id, Long userId);

    PageImpl<InformationOutputDTO> findInformationEffectivePage(Information information, Long userId);

    PageImpl<InformationOutputDTO> findInformationUserPage(Information information, Long userId);

    PageImpl<InformationOutputDTO> findInformationCollectionPage(CustomPage customPage, Long userId);

}
