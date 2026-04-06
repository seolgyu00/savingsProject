package com.sp.app.service;

import java.util.List;
import java.util.Map;

import com.sp.app.domain.dto.SavingsCalcDto;

public interface SavingsCalcService {

    void saveSavingsCalc(SavingsCalcDto dto) throws Exception;

    List<SavingsCalcDto> listByMember(Map<String, Object> map);

    SavingsCalcDto findById(long calcId);

    boolean deleteSavingsCalc(long calcId, long memberId) throws Exception;

    int countByMember(long memberId);
}
