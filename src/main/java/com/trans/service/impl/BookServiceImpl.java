package com.trans.service.impl;

import com.sun.org.apache.xpath.internal.operations.Bool;
import com.trans.dao.master.BookMapper;
import com.trans.domain.master.Book;
import com.trans.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class BookServiceImpl implements BookService  {

    @Autowired
    private BookMapper bookMapper;

    @Override
    @Transactional(value = "masterTransactionManager", rollbackFor = Exception.class, timeout = 5 )
    public String transOpe(int num) {
        // 查询实体类别
        Book book = bookMapper.selectByPrimaryKey((long)num);

        Book book1 = new Book();
        book1.setBookId(book.getBookId() + 1);
        book1.setName("dddddddd");
        book1.setNumber(num);
        book1.setDetail(book.getDetail());
        bookMapper.insert(book1);

        /*try {
            Thread.sleep(10000);
        } catch (Exception e) {
            e.printStackTrace();
        }*/

        Book book2 = new Book();
        book2.setBookId(book1.getBookId() + 1);
        book2.setName("dddddddddd222222");
        book2.setNumber(num);
        book2.setDetail(book.getDetail());
        bookMapper.insert(book2);

        return book1.toString();
    }

    @Override
    @Transactional(value = "masterTransactionManager")
    public String forlist() {
        Book book1 = new Book((long)1015, "唐诗三百首", 3, "hhhhh");
        Book book2 = new Book((long)1016, "宋词", 4, "hhhhdddh");

        List<Book> bookList = new ArrayList<>();
        bookList.add(book1);
        bookList.add(book2);

        int n = bookMapper.insertforlist(bookList);
        return "插入数据量: " + n;
    }
}
