package com.trans.service.impl;

import com.trans.dao.master.BookMapper;
import com.trans.dao.slave.GoodsMapper;
import com.trans.domain.master.Book;
import com.trans.domain.slave.Goods;
import com.trans.service.MultiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLOutput;

@Service
public class MultiServiceImpl implements MultiService {

    @Autowired
    private BookMapper bookMapper;

    @Autowired
    private GoodsMapper goodsMapper;

    @Override
    public void multiselect() {
        Long bookId = (long)1003;
        Book book = bookMapper.selectByPrimaryKey(bookId);
        System.out.println("book string: " + book.toString());

        Integer goodsId = 3;
        Goods goods = goodsMapper.selectByPrimaryKey(goodsId);
        System.out.println("goods string: " + goods.toString());

    }
}
