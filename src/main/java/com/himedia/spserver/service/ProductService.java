package com.himedia.spserver.service;

import com.himedia.spserver.dto.response.HReviewListDto;
import com.himedia.spserver.entity.*;
import com.himedia.spserver.repository.*;
import com.himedia.spserver.util.Paging;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
@Transactional
@RequiredArgsConstructor
public class ProductService {

    private final ExperienceRepository er;
    private final CityRepository cr;
    private final HotelRepository hr;
    private final OptionsRepository or;
    private final TransDetailRepository tr;
    private final ReviewRepository rr;
    private final OrdersRepository orr;

    public HashMap<String, Object> getExperienceList(int page, String sort, String keyword) {
        Sort sorting = Sort.by(Sort.Direction.DESC, "eid");

        if ("high".equals(sort)) sorting = Sort.by(Sort.Direction.DESC, "price2");
        if ("low".equals(sort)) sorting = Sort.by(Sort.Direction.ASC, "price2");
        if ("view".equals(sort)) sorting = Sort.by(Sort.Direction.DESC, "viewcount");
        if ("sale".equals(sort)) sorting = Sort.by(Sort.Direction.DESC, "salecount");

        int displayRow = 15;
        Pageable pageable = PageRequest.of(page - 1, displayRow, sorting);
        Page<Experience> pageResult;

        if (keyword != null && !keyword.isEmpty()) {
            pageResult = er.findByNameContainingOrContentContaining(keyword, keyword, pageable);
        } else {
            pageResult = er.findAll(pageable);
        }

        int totalCount = (int) pageResult.getTotalElements();
        Paging paging = new Paging();
        paging.setPage(page);
        paging.setDisplayRow(displayRow);
        paging.setDisplayPage(5);
        paging.setTotalCount(totalCount);
        paging.calPaging();

        HashMap<String, Object> result = new HashMap<>();
        result.put("ExperienceList", pageResult.getContent());
        result.put("paging", paging);

        return result;
    }

    public List<City> getCityList() {
        return cr.findAll();
    }

    public HashMap<String, Object> getHotelList(int page, String sort, String keyword) {

        int displayRow = 15;
        int displayPage = 5;

        if ("high".equals(sort) || "low".equals(sort)) {
            List<Hotel> allHotels;

            if (keyword != null && !keyword.isEmpty()) {
                allHotels = hr.findByNameContainingOrContentContaining(keyword, keyword);
            } else {
                allHotels = hr.findAll();
            }


            for (Hotel hotel : allHotels) {
                Options opt = or.findTop1ByHidOrderByPrice1Asc(hotel.getHid());
                if (opt != null) {
                    hotel.setPrice1(opt.getPrice1());
                    hotel.setPrice2(opt.getPrice2());
                } else {
                    hotel.setPrice1(0);
                    hotel.setPrice2(0);
                }
            }


            if ("high".equals(sort)) {
                allHotels.sort((a, b) -> Integer.compare(b.getPrice2(), a.getPrice2()));
            } else {
                allHotels.sort((a, b) -> Integer.compare(a.getPrice2(), b.getPrice2()));
            }


            int totalCount = allHotels.size();
            int startIdx = (page - 1) * displayRow;
            int endIdx = Math.min(startIdx + displayRow, totalCount);

            List<Hotel> pageList = new ArrayList<>();
            if (startIdx < totalCount) {
                pageList = allHotels.subList(startIdx, endIdx);
            }

            // 페이징 정보 구성
            Paging paging = new Paging();
            paging.setPage(page);
            paging.setDisplayRow(displayRow);
            paging.setDisplayPage(displayPage);
            paging.setTotalCount(totalCount);
            paging.calPaging();

            HashMap<String, Object> result = new HashMap<>();
            result.put("HotelList", pageList);
            result.put("paging", paging);
            return result;
        }

        Sort sorting = Sort.by(Sort.Direction.DESC, "hid");
        if ("view".equals(sort)) sorting = Sort.by(Sort.Direction.DESC, "viewcount");
        if ("sale".equals(sort)) sorting = Sort.by(Sort.Direction.DESC, "salecount");
        if ("default".equals(sort) || sort == null) sorting = Sort.by(Sort.Direction.DESC, "hid");

        Pageable pageable = PageRequest.of(page - 1, displayRow, sorting);
        Page<Hotel> pageResult;

        if (keyword != null && !keyword.isEmpty()) {
            pageResult = hr.findByNameContainingOrContentContaining(keyword, keyword, pageable);
        } else {
            pageResult = hr.findAll(pageable);
        }

        List<Hotel> hotelList = pageResult.getContent();

        for (Hotel hotel : hotelList) {
            Options opt = or.findTop1ByHidOrderByPrice1Asc(hotel.getHid());
            if (opt != null) {
                hotel.setPrice1(opt.getPrice1());
                hotel.setPrice2(opt.getPrice2());
            } else {
                hotel.setPrice1(0);
                hotel.setPrice2(0);
            }
        }

        int totalCount = (int) pageResult.getTotalElements();
        Paging paging = new Paging();
        paging.setPage(page);
        paging.setDisplayRow(displayRow);
        paging.setDisplayPage(displayPage);
        paging.setTotalCount(totalCount);
        paging.calPaging();

        HashMap<String, Object> result = new HashMap<>();
        result.put("HotelList", hotelList);
        result.put("paging", paging);
        return result;
    }








    public HashMap<String, Object> getTransList(int page, String sort, String keyword) {

        int displayRow = 15;
        int displayPage = 5;

        // ================================
        // 1) 가격 정렬(high / low)일 경우
        // ================================
        if ("high".equals(sort) || "low".equals(sort)) {

            List<TransDetail> allTrans;

            // 🔍 검색어 있을 때 → name, start, end 모두 검색
            if (keyword != null && !keyword.isEmpty()) {
                allTrans = tr.findByNameContainingOrStartContainingOrEndContaining(
                        keyword, keyword, keyword
                );
            } else {
                allTrans = tr.findAll();
            }

            // 🔽 가격 정렬
            if ("high".equals(sort)) {
                allTrans.sort((a, b) -> Integer.compare(b.getPrice2(), a.getPrice2()));
            } else {
                allTrans.sort((a, b) -> Integer.compare(a.getPrice2(), b.getPrice2()));
            }

            // 🔽 페이징 처리
            int totalCount = allTrans.size();
            int startIdx = (page - 1) * displayRow;
            int endIdx = Math.min(startIdx + displayRow, totalCount);

            List<TransDetail> pageList = new ArrayList<>();
            if (startIdx < totalCount) {
                pageList = allTrans.subList(startIdx, endIdx);
            }

            // 페이징 객체
            Paging paging = new Paging();
            paging.setPage(page);
            paging.setDisplayRow(displayRow);
            paging.setDisplayPage(displayPage);
            paging.setTotalCount(totalCount);
            paging.calPaging();

            HashMap<String, Object> result = new HashMap<>();
            result.put("TransList", pageList);
            result.put("paging", paging);

            return result;
        }

        // ================================
        // 2) 기본 정렬 (tid / sale)
        // ================================
        Sort sorting = Sort.by(Sort.Direction.DESC, "tid");

        if ("sale".equals(sort))
            sorting = Sort.by(Sort.Direction.DESC, "salecount");

        Pageable pageable = PageRequest.of(page - 1, displayRow, sorting);

        Page<TransDetail> pageResult;

        // 🔍 검색어 있을 때 → name, start, end 모두 검색
        if (keyword != null && !keyword.isEmpty()) {
            pageResult = tr.findByNameContainingOrStartContainingOrEndContaining(
                    keyword, keyword, keyword, pageable
            );
        } else {
            pageResult = tr.findAll(pageable);
        }

        int totalCount = (int) pageResult.getTotalElements();

        Paging paging = new Paging();
        paging.setPage(page);
        paging.setDisplayRow(displayRow);
        paging.setDisplayPage(displayPage);
        paging.setTotalCount(totalCount);
        paging.calPaging();

        HashMap<String, Object> result = new HashMap<>();
        result.put("TransList", pageResult.getContent());
        result.put("paging", paging);

        return result;
    }

    public List<Review> getEReviewList() {
        return rr.findAll();
    }



    public Page<HReviewListDto> getEReviewListByCategory(String category, int page) {
        Pageable pageable = PageRequest.of(page, 10, Sort.by("indate").descending());
        Page<Object[]> pageData = rr.findReviewWithEx(category, pageable);

        return pageData.map(obj -> {
            Review review = (Review) obj[0];
            String productname = (String) obj[1];

            HReviewListDto dto = new HReviewListDto();
            dto.setRid(review.getRid());
            dto.setEid(review.getEid());
            dto.setOid(review.getOid());
            dto.setMid(review.getMid());
            dto.setTitle(review.getTitle());
            dto.setContent(review.getContent());
            dto.setPoint(review.getPoint());
            dto.setIndate(review.getIndate());
            dto.setImage(review.getImage());
            dto.setCategory(review.getCategory());
            dto.setUserid(review.getUserid());
            dto.setProductname(productname);

            return dto;
        });
    }


    public List<Review> getBestEReviewList() {
        return rr.findTop4ByOrderByPointDesc();
    }


    public List<Review> getBestEReviewListByCategory(String category) {
        return rr.findTop4ByCategoryOrderByPointDesc(category);
    }



    public List<Review> getHReviewList() {
        return rr.findAll();
    }




    public Page<HReviewListDto> getHReviewListByCategory(String category, int page) {
        Pageable pageable = PageRequest.of(page, 10, Sort.by("indate").descending());
        Page<Object[]> pageData = rr.findReviewWithHotel(category, pageable);

        return pageData.map(obj -> {
            Review review = (Review) obj[0];
            String productname = (String) obj[1];

            HReviewListDto dto = new HReviewListDto();
            dto.setRid(review.getRid());
            dto.setHid(review.getHid());
            dto.setOid(review.getOid());
            dto.setMid(review.getMid());
            dto.setTitle(review.getTitle());
            dto.setContent(review.getContent());
            dto.setPoint(review.getPoint());
            dto.setIndate(review.getIndate());
            dto.setImage(review.getImage());
            dto.setCategory(review.getCategory());
            dto.setUserid(review.getUserid());
            dto.setProductname(productname);

            return dto;
        });
    }


    public List<Review> getBestHReviewList() {
        return rr.findTop4ByOrderByPointDesc();
    }


    public List<Review> getBestHReviewListByCategory(String category) {
        return rr.findTop4ByCategoryOrderByPointDesc(category);
    }


    public Page<Review> getTReviewListPage(int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "indate"));
        return rr.findAll(pageable);
    }

    public Page<HReviewListDto> getTReviewListByCategoryPage(String category, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "indate"));
        Page<Object[]> pageData = rr.findReviewWithTrans(category, pageable);
        return pageData.map(obj -> {
            Review review = (Review) obj[0];
            String productname = (String) obj[1];

            HReviewListDto dto = new HReviewListDto();
            dto.setRid(review.getRid());
            dto.setTid(review.getTid());
            dto.setOid(review.getOid());
            dto.setMid(review.getMid());
            dto.setTitle(review.getTitle());
            dto.setContent(review.getContent());
            dto.setPoint(review.getPoint());
            dto.setIndate(review.getIndate());
            dto.setImage(review.getImage());
            dto.setCategory(review.getCategory());
            dto.setUserid(review.getUserid());
            dto.setProductname(productname);

            return dto;
        });

    }


    public List<Review> getBestTReviewList() {
        return rr.findTop4ByOrderByPointDesc();
    }


    public List<Review> getBestTReviewListByCategory(String category) {
        return rr.findTop4ByCategoryOrderByPointDesc(category);
    }




    public Experience experienceDetail(int eid) {
        Optional<Experience> result = er.findById(eid);
        return result.orElse(null);
    }

    public List<Options> findAllByEid(int eid) {
        return or.findAllByHid(eid);
    }


    public HashMap<String, Object> getTopCityProducts() {

        HashMap<String, Object> result = new HashMap<>();

        // 📌 최근 1개월 기준
        LocalDateTime startDate = LocalDateTime.now().minusMonths(1);

        // 📌 1) 최근 1개월 판매량 상위 도시 Top 3 조회
        List<Integer> topCids =
                orr.findTopCidLastMonth(startDate, PageRequest.of(0, 3));

        if (topCids.isEmpty()) {
            result.put("status", "no-data");
            return result;
        }

        List<HashMap<String, Object>> cityList = new ArrayList<>();

        // 📌 2) 각 도시 반복 처리
        for (int cid : topCids) {

            HashMap<String, Object> cityMap = new HashMap<>();
            cityMap.put("cid", cid);

            // 🔥 2-1) City 테이블에서 도시명 찾기
            City city = cr.findByCidStartingWith(String.valueOf(cid))
                    .stream()
                    .findFirst()
                    .orElse(null);

            if (city != null) {
                cityMap.put("cityName", city.getAd1() + " " + city.getAd2());
            } else {
                cityMap.put("cityName", "도시명 없음");
            }

            // 🔥 2-2) 해당 도시의 베스트 상품 3종(숙소/체험/교통) 찾기
            HashMap<String, Object> topProducts = new HashMap<>();

            // --- 숙소 ---
            List<Integer> hotelPidList = orr.findTopPidByCidAndCategory(String.valueOf(cid), "숙소");
            if (!hotelPidList.isEmpty()) {
                int hotelId = hotelPidList.get(0);

                // 상품 = 호텔
                Hotel hotel = hr.findById(hotelId).orElse(null);

                // 옵션 1개 가져오기 (필요하다면)
                Options option = or.findFirstByHid(hotelId);

                if (hotel != null) {
                    hotel.setPrice1(option != null ? option.getPrice1() : null);
                    topProducts.put("숙소", hotel);
                }
            }

            // --- 체험 ---
            List<Integer> expPidList = orr.findTopPidByCidAndCategory(String.valueOf(cid), "체험");
            if (!expPidList.isEmpty()) {
                int pid = expPidList.get(0);
                Experience exp = er.findById(pid).orElse(null);
                topProducts.put("체험", exp);
            }

            // --- 교통 ---
            List<Integer> transPidList = orr.findTopPidByCidAndCategory(String.valueOf(cid), "교통");
            if (!transPidList.isEmpty()) {
                int pid = transPidList.get(0);
                TransDetail trans = tr.findById(pid).orElse(null);
                topProducts.put("교통", trans);
            }

            // 최종 map
            cityMap.put("topProducts", topProducts);

            // 리스트에 추가
            cityList.add(cityMap);
        }

        // 📌 최종 반환
        result.put("status", "success");
        result.put("cities", cityList);

        return result;
    }

    //조회수
    public void increaseHotelView(int hid) {
        Hotel hotel = hr.findById(hid).orElse(null);

        if (hotel != null) {
            hotel.setViewcount(hotel.getViewcount() + 1);
            hr.save(hotel);
        }
    }

    public void increaseExView(int eid) {
        Experience exp = er.findById(eid).orElse(null);

        if (exp != null) {
            exp.setViewcount(exp.getViewcount() + 1);
            er.save(exp);
        }
    }


//    public void increaseTransView(int tid) {
//        TransDetail trans = tr.findById(tid).orElse(null);
//
//        if (trans != null) {
//            trans.setViewcount(trans.getViewcount() + 1);
//            hr.save(trans);
//        }
//    }
}
