package com.sp.app.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.sp.app.domain.dto.SavingsCalcDto;
import com.sp.app.mapper.SavingsCalcMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class SavingsCalcServiceImpl implements SavingsCalcService {

    private final SavingsCalcMapper mapper;

    @Override
    public void saveSavingsCalc(SavingsCalcDto dto) throws Exception {
        try {
            mapper.insertSavingsCalc(dto);
        } catch (Exception e) {
            log.info("saveSavingsCalc : ", e);
            throw e;
        }
    }

    @Override
    public List<SavingsCalcDto> listByMember(Map<String, Object> map) {
        try {
            return mapper.listByMember(map);
        } catch (Exception e) {
            log.info("listByMember : ", e);
            return List.of();
        }
    }

    @Override
    public SavingsCalcDto findById(long calcId) {
        try {
            return mapper.findById(calcId);
        } catch (Exception e) {
            log.info("findById : ", e);
            return null;
        }
    }

    @Override
    public boolean deleteSavingsCalc(long calcId, long memberId) throws Exception {
        try {
            Map<String, Object> map = Map.of("calcId", calcId, "memberId", memberId);
            int rows = mapper.deleteSavingsCalc(map);
            return rows > 0;
        } catch (Exception e) {
            log.info("deleteSavingsCalc : ", e);
            throw e;
        }
    }

    @Override
    public int countByMember(long memberId) {
        try {
            return mapper.countByMember(memberId);
        } catch (Exception e) {
            log.info("countByMember : ", e);
            return 0;
        }
    }
}
