package com.trans.dao.slave;

import com.trans.domain.slave.Goods;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/*@Mapper
@Repository*/
public interface GoodsMapper {
    int deleteByPrimaryKey(Integer goodId);

    int insert(Goods record);

    int insertSelective(Goods record);

    Goods selectByPrimaryKey(Integer goodId);

    int updateByPrimaryKeySelective(Goods record);

    int updateByPrimaryKey(Goods record);
}