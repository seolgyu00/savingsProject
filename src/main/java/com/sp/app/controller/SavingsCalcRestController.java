package com.sp.app.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sp.app.domain.dto.LoginUser;
import com.sp.app.domain.dto.SavingsCalcDto;
import com.sp.app.security.CustomUserDetails;
import com.sp.app.service.SavingsCalcService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/savings")
public class SavingsCalcRestController {

    private final SavingsCalcService savingsCalcService;

    // 계산 결과 저장
    @PostMapping
    public ResponseEntity<?> handleSave(
            @RequestBody SavingsCalcDto dto,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        try {
            LoginUser loginUser = userDetails.getLoginUser();
            dto.setMemberId(loginUser.getMember_id());

            savingsCalcService.saveSavingsCalc(dto);

            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.info("handleSave : ", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    // 나의 적금 내역 목록 조회 (페이징)
    @GetMapping
    public ResponseEntity<?> handleList(
            @RequestParam(name = "pageNo", defaultValue = "1") int currentPage,
            @RequestParam(name = "pageSize", defaultValue = "9") int size,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        try {
            LoginUser loginUser = userDetails.getLoginUser();
            long memberId = loginUser.getMember_id();

            int totalCount = savingsCalcService.countByMember(memberId);

            int totalPage = 0;
            if (totalCount != 0) {
                totalPage = totalCount / size + (totalCount % size > 0 ? 1 : 0);
            }

            currentPage = Math.min(currentPage, Math.max(totalPage, 1));

            int offset = (currentPage - 1) * size;
            if (offset < 0) offset = 0;

            Map<String, Object> map = new HashMap<>();
            map.put("memberId", memberId);
            map.put("offset", offset);
            map.put("size", size);

            List<SavingsCalcDto> list = savingsCalcService.listByMember(map);

            return ResponseEntity.ok(Map.of(
                    "list", list,
                    "totalCount", totalCount,
                    "pageNo", currentPage,
                    "totalPage", totalPage
            ));
        } catch (Exception e) {
            log.info("handleList : ", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    // 단건 상세 조회
    @GetMapping("/{calcId}")
    public ResponseEntity<?> handleDetail(
            @PathVariable("calcId") long calcId,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        try {
            LoginUser loginUser = userDetails.getLoginUser();

            SavingsCalcDto dto = Objects.requireNonNull(savingsCalcService.findById(calcId));

            // 본인 소유 확인
            if (dto.getMemberId() != loginUser.getMember_id()) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("접근 권한이 없습니다.");
            }

            return ResponseEntity.ok(dto);
        } catch (NullPointerException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("내역이 존재하지 않습니다.");
        } catch (Exception e) {
            log.info("handleDetail : ", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    // 삭제
    @DeleteMapping("/{calcId}")
    public ResponseEntity<?> handleDelete(
            @PathVariable("calcId") long calcId,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        try {
            LoginUser loginUser = userDetails.getLoginUser();

            boolean deleted = savingsCalcService.deleteSavingsCalc(calcId, loginUser.getMember_id());

            if (!deleted) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("삭제 권한이 없거나 내역이 존재하지 않습니다.");
            }

            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.info("handleDelete : ", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
