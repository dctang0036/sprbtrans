package com.trans.controller;

import com.trans.service.BookService;
import com.trans.service.MultiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
@RequestMapping(value = "/cont")
public class Contr {

    @Autowired
    private BookService bookService;

    @Autowired
    private MultiService multiService;

    @RequestMapping(value = "/test/{num}", method = RequestMethod.GET)
    @ResponseBody
    public String test(@PathVariable("num") int num) {
//        int num = 1001;
        try {
            bookService.transOpe(num);
        } catch (Exception e) {
            System.out.println("事务异常");
            e.printStackTrace();
            return "error!";
        }
        return "success!";
    }

    @ResponseBody
    @RequestMapping(value = "/testlist", method = RequestMethod.GET)
    public String testlist() {
        return bookService.forlist();
    }

    @RequestMapping(value = "/multi", method = RequestMethod.GET)
    public void multiSelect() {
        multiService.multiselect();
    }

}
