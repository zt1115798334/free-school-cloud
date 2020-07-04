package com.example.datasource.repo;

import com.example.datasource.entity.FileInfo;
import org.springframework.data.repository.CrudRepository;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2019/2/21 10:20
 * description:
 */
public interface FileInfoRepository extends CrudRepository<FileInfo, Long> {

}
