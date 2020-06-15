package com.trans.dao;

import com.trans.domain.Book;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface BookMapper {
    int deleteByPrimaryKey(Long bookId);

    int insert(Book record);

    int insertSelective(Book record);

    Book selectByPrimaryKey(Long bookId);

    int updateByPrimaryKeySelective(Book record);

    int updateByPrimaryKey(Book record);
}