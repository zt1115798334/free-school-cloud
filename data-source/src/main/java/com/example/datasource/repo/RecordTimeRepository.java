package com.example.datasource.repo;

import com.example.datasource.entity.RecordTime;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2019/06/20 21:52
 * description:
 */
public interface RecordTimeRepository extends CrudRepository<RecordTime, Long>,
        JpaSpecificationExecutor<RecordTime> {
    Optional<RecordTime> findByIdAndDeleteState(Long id, Short deleteState);

    List<RecordTime> findByIdInAndDeleteState(List<Long> id, Short deleteState);

    @Modifying
    @Query(value = "update RecordTime set state =:afterState where userId in :userId and state = :beforeState and deleteState =:deleteState ")
    @Transactional(rollbackFor = RuntimeException.class)
    void updateState(List<Long> userId, String beforeState, String afterState, Short deleteState);

    @Modifying
    @Query(value = "update RecordTime set browsingVolume = browsingVolume + 1 where id =:id")
    @Transactional(rollbackFor = RuntimeException.class)
    void incrementBrowsingVolume(Long id);
}
