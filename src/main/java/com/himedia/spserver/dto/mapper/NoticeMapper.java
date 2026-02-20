package com.himedia.spserver.dto.mapper;

import com.himedia.spserver.dto.request.AdminNoticeWriteRequest;
import com.himedia.spserver.dto.response.AdminNoticeDto;
import com.himedia.spserver.entity.Notice;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class NoticeMapper {
    public Notice toEntity(AdminNoticeWriteRequest request) {
        Notice notice = new Notice();
        notice.setTitle(request.getTitle());
        notice.setContent(request.getContent());
        return notice;
    }

    public AdminNoticeDto toAdminDto(Notice notice) {
        if(notice == null) return null;
        return new AdminNoticeDto(
                notice.getNid(),
                notice.getTitle(),
                notice.getContent(),
                notice.getIndate()
        );
    }

    public List<AdminNoticeDto> toAdminDtoList(List<Notice> notices) {
        return notices.stream()
                .map(this::toAdminDto)
                .toList();
    }
}
