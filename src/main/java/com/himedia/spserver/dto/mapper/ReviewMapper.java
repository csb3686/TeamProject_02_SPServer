package com.himedia.spserver.dto.mapper;

import com.himedia.spserver.dto.response.ReviewDto;
import com.himedia.spserver.entity.Review;
import com.himedia.spserver.repository.ReviewRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ReviewMapper {

        // 4. toReviewDto 라는 응답디티오를 매퍼로 만듬
        public ReviewDto toReviewDto(Review reviewEntity){
            ReviewDto resDto = new ReviewDto();
            resDto.setRid(reviewEntity.getRid());
            resDto.setTitle(reviewEntity.getTitle());
            resDto.setContent(reviewEntity.getContent());
            resDto.setPoint(reviewEntity.getPoint());
            resDto.setUserid(reviewEntity.getUserid());
            resDto.setIndate(reviewEntity.getIndate());
            resDto.setImage(reviewEntity.getImage());

            return resDto;
        }
}
