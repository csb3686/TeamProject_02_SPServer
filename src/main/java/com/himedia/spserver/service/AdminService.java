package com.himedia.spserver.service;

import com.himedia.spserver.dto.mapper.MemberMapper;
import com.himedia.spserver.dto.mapper.NoticeMapper;
import com.himedia.spserver.dto.request.AdminNoticeEditRequest;
import com.himedia.spserver.dto.request.AdminNoticeWriteRequest;
import com.himedia.spserver.dto.response.AdminMemberDto;
import com.himedia.spserver.dto.request.AdminSerchDto;
import com.himedia.spserver.dto.response.HotelDTO;
import com.himedia.spserver.entity.*;
import com.himedia.spserver.repository.*;
import com.himedia.spserver.util.Paging;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class AdminService {
    private final MemberRepository mr;
    private final MemberMapper mm;
    private final NoticeRepository nr;
    private final NoticeMapper nm;
    private final EProduct ep;
    private final HProduct hp;
    private final OptionsRepository op;
    private final HotelRepository hr;
    private final TransDetailRepository tdr;
    private final ExperienceRepository er;
    private final TProduct tp;
    private final CityRepository cr;
    private final S3Uploader s3Uploader;

    public  Page<AdminMemberDto>getMemberList(int page) {
        Pageable pageable = PageRequest.of(page - 1, 7, Sort.by(Sort.Direction.DESC, "mid"));
        Page<Member> memberList = mr.findAll(pageable);

        return mm.toAdminDtoPage(memberList);
    }

    public HashMap<String, Object> getNoticeList(int page) {
        HashMap<String, Object> result = new HashMap<>();


        Paging paging = new Paging();
        paging.setPage(page);
        paging.setDisplayPage(10);
        paging.setDisplayRow(10);

        List<Notice> notices = nr.findAll();
        int count = notices.size();
        paging.setTotalCount(count);
        paging.calPaging();

        Pageable pageable = PageRequest.of(page - 1, paging.getDisplayRow(), Sort.by(Sort.Direction.DESC, "nid"));
        Page<Notice> noticeList = nr.findAll(pageable);

        result.put("noticeList", noticeList.getContent());
        result.put("paging", paging);
        return result;
    }

    public void writeNotice(AdminNoticeWriteRequest request) {
        Notice notice = nm.toEntity(request);
        nr.save(notice);
    }

    public void updateNotice(AdminNoticeEditRequest request) {
        Notice notice = nr.findByNid(request.getNid());
        notice.setTitle(request.getTitle());
        notice.setContent(request.getContent());
    }

    public void deleteNotice(int nid) {
        Notice notice = nr.findByNid(nid);
        nr.delete(notice);
    }

    public HashMap<String, Object> getOneMember(int mid) {
        HashMap<String, Object> result = new HashMap<>();
        result.put("member", mr.findByMid(mid));
        return result;
    }
  
    public HashMap<String, Object> getEProduct(int page, String search) {
        HashMap<String, Object> result = new HashMap<>();

        Paging paging = new Paging();
        paging.setPage(page);
        paging.setDisplayPage(10);
        paging.setDisplayRow(10);

        List<Experience> allList;
        if (search != null && !search.isEmpty()) {
            allList = ep.findByNameContainingIgnoreCase(search);
        } else {
            allList = ep.findAll();
        }

        int count = allList.size();
        paging.setTotalCount(count);
        paging.calPaging();

        Pageable pageable = PageRequest.of(page - 1, paging.getDisplayRow(), Sort.by(Sort.Direction.DESC, "eid"));
        Page<Experience> ap;

        if (search != null && !search.isEmpty()) {
            ap = ep.findByNameContainingIgnoreCase(search, pageable);
        } else {
            ap = ep.findAll(pageable);
        }

        result.put("eProduct", ap.getContent());
        result.put("paging", paging);
        return result;
    }


    public HashMap<String, Object> getAdminEProduct(int eid) {
        HashMap<String, Object> result = new HashMap<>();
        Experience exp = ep.findById(eid).orElse(null);

        if (exp == null) {
            result.put("msg", "상품을 찾을 수 없습니다.");
        } else {
            result.put("admin", exp);
            result.put("msg", "ok");
        }

        return result;
    }

    public boolean updateAdminEProduct(
            int eid, String name, String content, String city,
            int price1, int price2, int viewcount, int salecount,
            String hashtag, MultipartFile imageFile
    ) {
        Experience oldData = ep.findById(eid).orElse(null);
        if (oldData == null) return false;

        oldData.setName(name);
        oldData.setContent(content);
        oldData.setCid(city);
        oldData.setPrice1(price1);
        oldData.setPrice2(price2);
        oldData.setViewcount(viewcount);
        oldData.setSalecount(salecount);
        oldData.setHashtag(hashtag);

        if (imageFile != null && !imageFile.isEmpty()) {
            try {
                String imageUrl = s3Uploader.upload(imageFile, "eproduct");
                oldData.setImage(imageUrl);
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }

        ep.save(oldData);
        return true;
    }



    public boolean insertAdminEProduct(
            String name,
            String content,
            String city,
            int price1,
            int price2,
            int viewcount,
            int salecount,
            String hashtag,
            MultipartFile imageFile
    ) {
        try {
            Experience exp = new Experience();
            exp.setName(name);
            exp.setContent(content);
            exp.setCid(city);
            exp.setPrice1(price1);
            exp.setPrice2(price2);
            exp.setViewcount(viewcount);
            exp.setSalecount(salecount);
            exp.setHashtag(hashtag);

            if (imageFile != null && !imageFile.isEmpty()) {
                String imageUrl = s3Uploader.upload(imageFile, "eproduct");
                exp.setImage(imageUrl);
            } else {
                exp.setImage(null);
            }

            ep.save(exp);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }



    public List<City> searchCity(String keyword) {
        // 숫자만 입력된 경우 (법정동코드)
        if (keyword.matches("\\d+")) {
            if (keyword.length() == 5) {
                // 5자리면 앞자리로 검색
                return cr.findByCidStartingWith(keyword);
            } else if (keyword.length() == 10) {
                // 10자리면 정확한 코드로 검색
                City city = cr.findById(keyword).orElse(null);
                return city != null ? List.of(city) : new ArrayList<>();
            }
        }

        // 텍스트로 입력된 경우 (지역명)
        return cr.findByAd1ContainingOrAd2ContainingOrAd3Containing(
                keyword, keyword, keyword
        );
    }







    public HashMap<String, Object> updateAdmin(int mid) {

        HashMap<String, Object> result = new HashMap<>();
        Member member = mr.findByMid(mid);
        if(member.getRole().equals(Role.ADMIN)){
            result.put("msg", "이미 어드민 권한이 부여된 회원입니다");
        }else{
            member.setRole(Role.ADMIN);
            mr.save(member);
            result.put("msg", "ok");
        }
        return result;
    }

    public HashMap<String, Object> updateUser(int mid) {

        HashMap<String, Object> result = new HashMap<>();
        Member member = mr.findByMid(mid);
        if(member.getRole().equals(Role.USER)){
            result.put("msg", "이미 유저로 설정된 회원입니다");
        }else{
            member.setRole(Role.USER);
            mr.save(member);
            result.put("msg", "ok");
        }
        return result;

    }

    public HashMap<String, Object> getSerchResult(String key) {
        HashMap<String, Object> result = new HashMap<>();

        if(key!=null && !key.isEmpty()){

            String normalizedKey = key.replaceAll("\\s+", "");

            List<Hotel> hotel = hr.findByNameContaining(normalizedKey);
            List<TransDetail> trans = tdr.findNameContaining(normalizedKey);
            List<Experience> ex = er.findByNameContaining(normalizedKey);

            List<AdminSerchDto> asdto = new ArrayList<>();

            for (Hotel h : hotel) {
                asdto.add(new AdminSerchDto(
                        h.getHid(),
                        h.getName(),
                        "숙소"
                ));
            }

            for (TransDetail td : trans) {
                asdto.add(new AdminSerchDto(
                        td.getTid(),
                        td.getName(),
                        "교통"
                ));
            }

            for (Experience e : ex) {
                asdto.add(new AdminSerchDto(
                        e.getEid(),
                        e.getName(),
                        "체험"
                ));
            }

            result.put("productList", asdto);

            List<Member> member = mr.findByUseridOrNameContaining(normalizedKey, normalizedKey);
            List<Notice> notice = nr.findByTitleOrContentContaining(normalizedKey, normalizedKey);

            result.put("memberList", member);
            result.put("noticeList", notice);

        }

        return result;
    }

    public HashMap<String, Object> getHProduct(int page, String search) {

        HashMap<String, Object> result = new HashMap<>();

        Paging paging = new Paging();
        paging.setPage(page);
        paging.setDisplayPage(10);
        paging.setDisplayRow(10);


        List<Hotel> allList;
        if (search != null && !search.isEmpty()) {
            allList = hp.findByNameContainingIgnoreCase(search);
        } else {
            allList = hp.findAll();
        }


        int count = allList.size();
        paging.setTotalCount(count);
        paging.calPaging();


        Pageable pageable = PageRequest.of(
                page - 1,
                paging.getDisplayRow(),
                Sort.by(Sort.Direction.DESC, "hid")
        );

        Page<Hotel> hrp;
        if (search != null && !search.isEmpty()) {
            hrp = hp.findByNameContainingIgnoreCase(search, pageable);
        } else {
            hrp = hp.findAll(pageable);
        }

        result.put("hProduct", hrp.getContent());
        result.put("paging", paging);

        return result;
    }

    public List<HotelDTO> convertToDTO(List<Hotel> hotels) {

        List<HotelDTO> list = new ArrayList<>();

        for (Hotel h : hotels) {
            HotelDTO dto = new HotelDTO();
            dto.setHid(h.getHid());
            dto.setName(h.getName());
            dto.setImage(h.getImage());


            List<Options> options = op.findByHid(h.getHid());

            if (!options.isEmpty()) {

                Options o = options.get(0);
                dto.setPrice1(o.getPrice1());
                dto.setPrice2(o.getPrice2());
            } else {
                dto.setPrice1(null);
                dto.setPrice2(null);
            }

            list.add(dto);
        }

        return list;
    }




    public HashMap<String, Object> getAdminHProduct(int hid) {
        HashMap<String, Object> result = new HashMap<>();
        Hotel hotel = hr.findById(hid).orElse(null);
        if (hotel == null) {
            result.put("msg", "호텔을 찾을 수 없습니다.");
        } else {
            result.put("admin", hotel);
            result.put("msg", "ok");
        }

        return result;
    }

    public boolean updateAdminHProduct(int hid, String name, String content, String cid,
                                        int viewcount, int salecount,
                                       MultipartFile imageFile) {
        Hotel oldData = hr.findById(hid).orElse(null);
        if (oldData == null) return false;

        oldData.setName(name);
        oldData.setContent(content);
        oldData.setCid(cid);
        oldData.setViewcount(viewcount);
        oldData.setSalecount(salecount);


        if (imageFile != null && !imageFile.isEmpty()) {
            try {
                String imageUrl = s3Uploader.upload(imageFile, "hproduct");
                oldData.setImage(imageUrl);
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }

        hr.save(oldData);
        return true;
    }




    public HashMap<String, Object> insertHProduct(String name, String content, String notice, String cid, int viewcount, int salecount, MultipartFile imageFile) throws IOException {
        HashMap<String, Object> result = new HashMap<>();
        Hotel h = new Hotel();
        h.setName(name);
        h.setContent(content);
        h.setNotice(notice);
        h.setCid(cid);
        h.setViewcount(viewcount);
        h.setSalecount(salecount);
        if (imageFile != null && !imageFile.isEmpty()) {
            String imageUrl = s3Uploader.upload(imageFile, "eproduct");
            h.setImage(imageUrl);
        } else {
            h.setImage(null);
        }
         hr.save(h);

        Hotel hid = hr.findTopByOrderByHidDesc();
        result.put("hid",hid.getHid());
        return result;
    }


    public HashMap<String, Object> insertAdminHOption(Options options, int hid) {
        HashMap<String, Object> result = new HashMap<>();

        Options o = new Options();
        o.setHid(hid);
        o.setName(options.getName());
        o.setPrice1(options.getPrice1());
        o.setPrice2(options.getPrice2());
        o.setContent(options.getContent());
        o.setSalecount(options.getSalecount() != 0 ? options.getSalecount() : 0);
        o.setMaxcount(options.getMaxcount());

        // 이미지는 별도 API로 업로드되므로 여기서는 null로 설정
        o.setImage(null);

        op.save(o);

        // 저장된 opid 반환
        Options savedOption = op.findTopByOrderByOpidDesc();
        result.put("opid", savedOption.getOpid());
        result.put("message", "옵션 저장 성공");

        return result;
    }

    // ✅ 새로 추가: 옵션 이미지 업데이트
    public void updateOptionImage(int opid, MultipartFile imageFile) throws IOException {
        Options option = op.findByOpid(opid);

        if (option != null && imageFile != null && !imageFile.isEmpty()) {
            String imageUrl = s3Uploader.upload(imageFile, "oproduct");
            option.setImage(imageUrl);
            op.save(option);
        }
    }












    public HashMap<String, Object> getTProduct(int page, String search) {
        HashMap<String, Object> result = new HashMap<>();

        Paging paging = new Paging();
        paging.setPage(page);
        paging.setDisplayPage(10);
        paging.setDisplayRow(10);

        List<TransDetail> allList;
        if (search != null && !search.isEmpty()) {
            allList = tp.findByNameContainingIgnoreCase(search);
        } else {
            allList = tp.findAll();
        }

        int count = allList.size();
        paging.setTotalCount(count);
        paging.calPaging();

        Pageable pageable = PageRequest.of(page - 1, paging.getDisplayRow(), Sort.by(Sort.Direction.DESC, "tid"));
        Page<TransDetail> trp;

        if (search != null && !search.isEmpty()) {
            trp = tp.findByNameContainingIgnoreCase(search, pageable);
        } else {
            trp = tp.findAll(pageable);
        }

        result.put("tProduct", trp.getContent());
        result.put("paging", paging);
        return result;
    }


    public HashMap<String, Object> getAdminTProduct(int tid) {
        HashMap<String, Object> result = new HashMap<>();
        TransDetail td = tp.findById(tid).orElse(null);

        if (td == null) {
            result.put("msg", "상품을 찾을 수 없습니다.");
        } else {
            result.put("admin", td);
            result.put("msg", "ok");
        }

        return result;
    }


    public boolean updateAdminTProduct(
            int tid, String name, String category, String company, String start, String end,
            int starttime, int endtime, int maxcount, int price1, int price2, int salecount,
             MultipartFile imageFile
    ) {
        TransDetail oldData = tp.findById(tid).orElse(null);
        if (oldData == null) return false;

        oldData.setName(name);
        oldData.setCategory(category);

        oldData.setCompany(company);
        oldData.setStart(start);
        oldData.setEnd(end);
        oldData.setStarttime(starttime);
        oldData.setEndtime(endtime);
        oldData.setMaxcount(maxcount);
        oldData.setPrice1(price1);
        oldData.setPrice2(price2);
        oldData.setSalecount(salecount);

        if (imageFile != null && !imageFile.isEmpty()) {
            try {
                String imageUrl = s3Uploader.upload(imageFile, "tproduct");
                oldData.setImage(imageUrl);
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }

        tp.save(oldData);
        return true;
    }

    public void deleteEProduct(List<Integer> checkList) {
        er.deleteAllById(checkList);
    }

    public void deleteHProduct(List<Integer> checkList) {
        hr.deleteAllById(checkList);
    }


    public void deleteTProduct(List<Integer> checkList) {
        tdr.deleteAllById(checkList);
    }


    public HashMap<String, Object> getOptionByHid(int hid) {

        HashMap<String, Object> result = new HashMap<>();
        List<Options> optionsList = op.findByHid(hid);
        result.put("option", optionsList);
        return result;
    }

//    public void updateAdminHOption(String name, String content, int price1, int price2, int maxcout, int opid, int hid) {
//        System.out.println("서비스 options 값 잘 오고 있음");
//        HashMap<String,  Object> result = new HashMap<>();
//        Options option = op.findByOpid(opid);
//        option.setName(name);
//        option.setContent(content);
//        option.setHid(hid);
//        option.setMaxcount(maxcout);
//        option.setPrice1(price1);
//        option.setPrice2(price2);
//        op.save(option);
//        System.out.println("업데이트 다했음");
//
//    }

    public void updateAdminHOption(Options options) {
        System.out.println("서비스 options 값 잘 오고 있음");
        HashMap<String,  Object> result = new HashMap<>();
        Options option = op.findByOpid(options.getOpid());
        option.setName(options.getName());
        option.setContent(options.getContent());
        option.setHid(options.getHid());
        option.setMaxcount(options.getMaxcount());
        option.setPrice1(options.getPrice1());
        option.setPrice2(options.getPrice2());
        op.save(option);
        System.out.println("업데이트 다했음");

    }

    public boolean insertAdminTProduct(
            String name,
            String category,
            String company,
            String start,
            String end,
            String starttime,
            String endtime,
            int maxcount,
            int price1,
            int price2,
            Integer salecount,
            MultipartFile imageFile
    ) {
        try {


            int startTimeInt = convertTimeToInt(starttime);
            int endTimeInt = convertTimeToInt(endtime);

            TransDetail tsd = new TransDetail();
            tsd.setName(name);
            tsd.setCategory(category);
            tsd.setCompany(company);
            tsd.setStart(start);
            tsd.setEnd(end);
            tsd.setStarttime(startTimeInt);
            tsd.setEndtime(endTimeInt);
            tsd.setMaxcount(maxcount);
            tsd.setPrice1(price1);
            tsd.setPrice2(price2);
            tsd.setSalecount(salecount);

            if (imageFile != null && !imageFile.isEmpty()) {
                String imageUrl = s3Uploader.upload(imageFile, "tproduct");
                tsd.setImage(imageUrl);
            }

            tp.save(tsd);
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private int convertTimeToInt(String timeStr) {
        if (timeStr == null || !timeStr.contains(":")) return 0;
        return Integer.parseInt(timeStr.replace(":", ""));
    }

}



