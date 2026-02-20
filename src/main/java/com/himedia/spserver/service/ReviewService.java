package com.himedia.spserver.service;

import com.himedia.spserver.dto.mapper.ReviewMapper;
import com.himedia.spserver.dto.request.ReviewInsertRequest;
import com.himedia.spserver.dto.response.ReviewDto;
import com.himedia.spserver.entity.*;
import com.himedia.spserver.repository.MemberRepository;
import com.himedia.spserver.repository.OrdersRepository;
import com.himedia.spserver.repository.ReviewRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository rr;
    private final ReviewMapper rm;
    private final MemberRepository mr;
    private final OrdersRepository or;

    private final S3Uploader s3Uploader;

    public List<ReviewDto> getReviewList(String fieldName, int id) {
        List<Review> reviewDatas;

        switch(fieldName) {
            case "hid":
                reviewDatas = rr.findAllByHid(id);
                break;
            case "tid":
                reviewDatas = rr.findAllByTid(id);
                break;
            case "eid":
                reviewDatas = rr.findAllByEid(id);
                break;
            default:
                throw new IllegalArgumentException("Invalid field name: " + fieldName);
        }

        List<ReviewDto> dtoList = new ArrayList<>();
        for (Review review : reviewDatas) {
            dtoList.add(rm.toReviewDto(review));
        }
        return dtoList;
    }

    //1.리뷰중복체크먼저
    public boolean existsByOid(int oid) {
        return rr.existsByOid(oid);
    }

    //2.리뷰등록
    public Review insertReview(ReviewInsertRequest dto, MultipartFile imageFile) {
        // (공통)  회원 userid조회
        Member member = mr.findByUserid(dto.getUserid());
        if (member == null) throw new RuntimeException("회원이 존재하지 않습니다.");
        Orders orders = or.findById(dto.getOid())
                .orElseThrow(() -> new RuntimeException("주문 정보를 찾을 수 없습니다."));


        // 2. 리뷰 객체 생성
        Review review = new Review();
        review.setOid(dto.getOid());
        review.setUserid(dto.getUserid());
        review.setMid(member.getMid());

        review.setCategory(orders.getCategory()); //카테고리
        review.setTitle(dto.getTitle());     // 제목
        review.setContent(dto.getContent()); // 내용
        review.setPoint(dto.getPoint());     // 평점
        review.setIndate(Timestamp.valueOf(LocalDateTime.now())); // 작성일자

        //  category에 따라 pid를 각 필드에 채움
        if ("숙소".equals(orders.getCategory())) {
            review.setHid(orders.getPid());  // 숙소
            review.setTid(0);
            review.setEid(0);

        } else if ("체험".equals(orders.getCategory())) {
            review.setEid(orders.getPid());  // 체험
            review.setHid(0);
            review.setTid(0);

        } else if ("교통".equals(orders.getCategory())) {
            review.setTid(orders.getPid());  // 교통
            review.setHid(0);
            review.setEid(0);
        }


        // 이미지 S3 업로드
        if (imageFile != null && !imageFile.isEmpty()) {
            try {
                String imageUrl = s3Uploader.upload(imageFile, "review");
                review.setImage(imageUrl);
            } catch (IOException e) {
                // 이미지 업로드 실패 시 리뷰 저장도 막기
                throw new RuntimeException("이미지 업로드 실패: " + e.getMessage(), e);
            }
        }

        // 4. DB 저장
        return rr.save(review);
    }



//    private String saveImage(MultipartFile imageFile) {
//        try {
//            // 저장 경로 (예: C:/upload/review/)
//            String uploadDir = "C:/upload/review/";
//
//            File folder = new File(uploadDir);
//            if (!folder.exists()) folder.mkdirs(); // 폴더 없으면 생성
//
//            // 저장될 파일명 (UUID + 원본 확장자)
//            String fileName = UUID.randomUUID() + "_" + imageFile.getOriginalFilename();
//            File saveFile = new File(uploadDir + fileName);
//
//            imageFile.transferTo(saveFile);
//
//            return "/upload/review/" + fileName; // DB에 저장될 경로
//        } catch (Exception e) {
//            throw new RuntimeException("이미지 저장 실패", e);
//        }
//    }


}
