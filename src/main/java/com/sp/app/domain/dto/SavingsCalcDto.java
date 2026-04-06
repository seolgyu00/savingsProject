package com.sp.app.domain.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SavingsCalcDto {

    private long calcId;
    private long memberId;

    // 입력값
    private String productName;
    private long monthlyAmount;
    private int periodMonths;
    private double annualRate;
    private String calcMethod;       // 단리 | 복리
    private String taxType;          // 비과세 | 세금우대 | 일반과세 | 직접입력
    private double taxRate;          // 실제 적용 세율 (%)
    private double incomeTaxRate;    // 이자소득세율 (%)
    private double localTaxRate;     // 지방소득세율 (%)

    // 계산 결과
    private long principal;          // 납입 원금 합계
    private long interestBefore;     // 세전 이자 합계
    private long preTaxAmount;       // 세전 만기 수령액
    private long incomeTax;          // 이자소득세
    private long localTax;           // 지방소득세
    private long taxTotal;           // 세금 합계
    private long finalAmount;        // 세후 최종 수령액

    private String savedAt;          // 저장일시 (조회용 문자열)
}
