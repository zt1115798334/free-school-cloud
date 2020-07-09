package com.example.serviceinformation.service.impl;

import com.example.common.dto.output.InformationOutputDTO;
import com.example.common.exception.custom.OperationException;
import com.example.common.utils.DateUtils;
import com.example.datasource.base.domain.CustomPage;
import com.example.datasource.entity.Information;
import com.example.datasource.jpa.PageUtils;
import com.example.datasource.jpa.SearchFilter;
import com.example.datasource.repo.InformationRepository;
import com.example.serviceinformation.service.AdditionalService;
import com.example.serviceinformation.service.InformationService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static com.example.datasource.jpa.SearchFilter.Operator;
import static com.example.datasource.jpa.SearchFilter.bySearchFilter;


/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2019/07/15 15:11
 * description:
 */
@AllArgsConstructor
@Service
public class InformationServiceImpl implements InformationService {


    private final InformationRepository informationRepository;

    private final AdditionalService additionalService;

    @Override
    public Information save(Information information) {
        Long id = information.getId();
        if (id != null && id != 0L) {
            Optional<Information> informationOptional = findByIdNotDelete(id);
            if (informationOptional.isPresent()) {
                Information informationDb = informationOptional.get();
                informationDb.setTitle(information.getTitle());
                informationDb.setDescribeContent(information.getDescribeContent());
                informationDb.setUpdatedTime(DateUtils.currentDateTime());
                return informationRepository.save(informationDb);
            }
            return null;
        } else {
            information.setBrowsingVolume(0L);
            information.setState(IN_RELEASE);
            information.setCreatedTime(currentDateTime());
            information.setDeleteState(UN_DELETED);
            return informationRepository.save(information);
        }
    }

    @Override
    public void deleteById(Long aLong) {
        informationRepository.findById(aLong).ifPresent(information -> {
            information.setDeleteState(DELETED);
            informationRepository.save(information);
        });
    }

    @Override
    public Optional<Information> findByIdNotDelete(Long aLong) {
        return informationRepository.findByIdAndDeleteState(aLong, UN_DELETED);
    }

    @Override
    public List<Information> findByIdsNotDelete(List<Long> id) {
        return informationRepository.findByIdInAndDeleteState(id, UN_DELETED);
    }

    @Override
    public Page<Information> findPageByEntity(Information information) {
        return null;
    }

    @Override
    public InformationOutputDTO saveInformation(Information information) {
        information = this.save(information);
        return additionalService.resultInformationDto(information, information.getUserId());

    }

    @Override
    public void deleteInformation(Long id) {
        this.deleteById(id);
    }

    @Override
    public void modifyInformationSateToNewRelease(Long id) {
        informationRepository.findById(id).ifPresent(information -> {
            information.setState(NEW_RELEASE);
            informationRepository.save(information);
        });
    }

    @Override
    public void modifyInformationSateToAfterRelease(List<Long> userId) {
        informationRepository.updateState(userId, NEW_RELEASE, AFTER_RELEASE, UN_DELETED);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void incrementInformationBrowsingVolume(Long id) {
        informationRepository.incrementBrowsingVolume(id);
    }

    @Override
    public Information findInformation(Long id) {
        return this.findByIdNotDelete(id).orElseThrow(() -> new OperationException("已删除"));
    }

    @Override
    public InformationOutputDTO findRoInformation(Long id, Long userId) {
        Information information = this.findInformation(id);
        this.incrementInformationBrowsingVolume(id);
        return additionalService.resultInformationDto(information, userId);
    }


    @Override
    public PageImpl<InformationOutputDTO> findInformationEffectivePage(Information information, Long userId) {
        List<SearchFilter> filters = getInformationFilter(getEffectiveState(), information);
        return getRoInformationCustomPage(information, userId, filters);

    }

    @Override
    public PageImpl<InformationOutputDTO> findInformationUserPage(Information information, Long userId) {
        List<SearchFilter> filters = getInformationFilter(getEffectiveState(), information);
        filters.add(new SearchFilter("userId", information.getUserId(), Operator.EQ));
        return getRoInformationCustomPage(information, userId, filters);

    }

    @Override
    public PageImpl<InformationOutputDTO> findInformationCollectionPage(CustomPage customPage, Long userId) {
        PageImpl<Long> topicIdPage = additionalService.findCollection(userId, TOPIC_TYPE_2, customPage);
        List<Information> informationList = this.findByIdsNotDelete(topicIdPage.getContent());
        List<InformationOutputDTO> dtoList = additionalService.resultInformationDtoList(informationList, userId);
        return new PageImpl<>(dtoList,topicIdPage.getPageable(),topicIdPage.getTotalElements());
    }

    private PageImpl<InformationOutputDTO> getRoInformationCustomPage(Information information, Long userId, List<SearchFilter> filters) {
        Specification<Information> specification = bySearchFilter(filters);
        Pageable pageable = PageUtils.buildPageRequest(information);
        Page<Information> informationPage = informationRepository.findAll(specification, pageable);
        List<InformationOutputDTO> dtoList = additionalService.resultInformationDtoList(informationPage.getContent(), userId);
        return new PageImpl<>(dtoList,informationPage.getPageable(),informationPage.getTotalElements());
    }

    private List<SearchFilter> getInformationFilter(List<SearchFilter> filters, Information information) {
        return getTopicFilter(filters, information.getSearchArea(), information.getSearchValue(), information.getStartDateTime(), information.getEndDateTime());
    }

}
