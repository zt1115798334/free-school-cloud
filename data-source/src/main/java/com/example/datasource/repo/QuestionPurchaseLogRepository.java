package com.example.datasource.repo;

import com.example.datasource.entity.QuestionPurchaseLog;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2019/07/17 14:37
 * description:
 */
public interface QuestionPurchaseLogRepository extends CrudRepository<QuestionPurchaseLog, Long> {

    Optional<QuestionPurchaseLog> findByUserIdAndQuestionBankId(Long userId, Long questionBankId);
}
