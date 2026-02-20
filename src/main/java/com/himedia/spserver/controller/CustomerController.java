package com.himedia.spserver.controller;

import com.himedia.spserver.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@RestController
@RequestMapping("/customer")
public class CustomerController {

    @Autowired
    CustomerService cs;

    @GetMapping("/getQnaList/{page}")
    public HashMap<String, Object> getQnaList(@PathVariable("page")int  page) {
        HashMap<String, Object> result = cs.getQnaList(page);
        return result;
    }

    @GetMapping("/getQnaDetail/{qid}")
    public HashMap<String, Object> getQnaDetail(@PathVariable("qid") int qid) {
        HashMap<String, Object> result = new HashMap<>();
        result.put("qna", cs.getQnaDetail(qid));
        return result;
    }

    @GetMapping("/getNoticeList/{page}")
    public HashMap<String, Object> getNoticeList(@PathVariable("page")int  page) {
        HashMap<String, Object> result = cs.getNoticeList(page);
        return result;
    }

    @GetMapping("/getNoticeDetail/{nid}")
    public HashMap<String, Object> getNoticeDetail(@PathVariable("nid") int nid) {
        HashMap<String, Object> result = new HashMap<>();
        result.put("notice", cs.getNoticeDetail(nid));
        return result;
    }


}
