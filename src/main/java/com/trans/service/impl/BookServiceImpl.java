package com.trans.service.impl;

import com.sun.org.apache.xpath.internal.operations.Bool;
import com.trans.dao.BookMapper;
import com.trans.domain.Book;
import com.trans.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BookServiceImpl implements BookService  {

    @Autowired
    private BookMapper bookMapper;

    @Override
    @Transactional(rollbackFor = Exception.class, timeout = 1 )
    public String transOpe(int num) {
        // 查询实体类别
        Book book = bookMapper.selectByPrimaryKey((long)num);

        Book book1 = new Book();
        book1.setBookId(book.getBookId() + 1);
        book1.setName("dddddddd");
        book1.setNumber(num);
        book1.setDetail(book.getDetail());
        bookMapper.insert(book1);

        try {
            Thread.sleep(10000);
        } catch (Exception e) {
            e.printStackTrace();
        }


        Book book2 = new Book();
        book2.setBookId(book1.getBookId() + 1);
        book2.setName("dddddddddd222222");
        book2.setNumber(num);
        book2.setDetail(book.getDetail());
        bookMapper.insert(book2);


        return book1.toString();
    }
}
