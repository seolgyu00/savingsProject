package com.sp.app.mapper;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.sp.app.domain.dto.SavingsCalcDto;

@Mapper
public interface SavingsCalcMapper {

    // 저장
    void insertSavingsCalc(SavingsCalcDto dto) throws SQLException;

    // 회원별 전체 목록 조회
    List<SavingsCalcDto> listByMember(Map<String, Object> map);

    // 단건 조회
    SavingsCalcDto findById(long calcId);

    // 삭제 (본인 소유 확인용 member_id 포함)
    int deleteSavingsCalc(Map<String, Object> map) throws SQLException;

    // 회원별 저장 개수
    int countByMember(long memberId);
}
