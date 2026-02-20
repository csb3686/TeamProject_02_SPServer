package com.himedia.spserver.service;

import com.himedia.spserver.entity.*;
import com.himedia.spserver.repository.CustomerRepository;
import com.himedia.spserver.repository.NoticeRepository;
import com.himedia.spserver.util.Paging;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;

@Service
@Transactional
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository cr;

    private final NoticeRepository nr;

    public HashMap<String, Object> getQnaList(int page) {
        HashMap<String, Object> result = new HashMap<>();

        Paging paging = new Paging();
        paging.setPage(page);
        paging.setDisplayPage(10);
        paging.setDisplayRow(10);

        int count = cr.findAll().size();
        paging.setTotalCount(count);
        paging.calPaging();

        Pageable pageable = PageRequest.of(page-1, 10, Sort.by(Sort.Direction.DESC, "qid"));
        Page<Qna> ap = cr.findAll(pageable);
        result.put("qnaList", ap.getContent());
        result.put("paging", paging);
        return result;

    }

    public Qna getQnaDetail(int qid) {
        return cr.findByQid(qid); // 단일 데이터 반환
    }

    public HashMap<String, Object> getNoticeList(int page) {
        HashMap<String, Object> result = new HashMap<>();

        Paging paging = new Paging();
        paging.setPage(page);
        paging.setDisplayPage(10);
        paging.setDisplayRow(10);

        int count = nr.findAll().size();
        paging.setTotalCount(count);
        paging.calPaging();

        Pageable pageable = PageRequest.of(page-1, 10, Sort.by(Sort.Direction.DESC, "nid"));
        Page<Notice> ab = nr.findAll(pageable);
        result.put("noticeList", ab.getContent());
        result.put("paging", paging);
        return result;
    }

    public Notice getNoticeDetail(int nid) {return nr.findByNid(nid);}


}
