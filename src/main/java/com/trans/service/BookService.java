package com.trans.service;

public interface BookService  {

    /**
     * 事务操作接口
     * @param num
     * @return
     */
    String transOpe(int num);

    /**
     * mybatis插入list
     * @return
     */
    String forlist();

}
