package com.example.datasource.base.service;


import com.example.common.base.service.BaseConstant;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2018/8/9 11:40
 * description:
 */
public interface BaseService<T, ID> extends BaseConstant {

    default T save(T t) {
        return null;
    }

    default Iterable<T> saveAll(Iterable<T> t) {
        return t;
    }

    default void deleteById(ID id) {
    }

    default void deleteById(List<ID> id) {
    }

    default void deleteAll(Iterable<T> t) {
    }

    default Optional<T> findById(ID id) {
        return Optional.empty();
    }

    default List<T> findByIds(List<ID> id) {
        return null;
    }

    default List<T> findByIdsOrder(List<ID> id) {
        return null;
    }

    default Optional<T> findByIdNotDelete(ID id) {
        return Optional.empty();
    }

    default List<T> findByIdsNotDelete(List<ID> id) {
        return null;
    }

    default List<T> findAll() {
        return null;
    }

    default List<T> findListByEntity(T t) {
        return null;
    }

    default Page<T> findPageByEntity(T t) {
        return null;
    }
}