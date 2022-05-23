package com.bithumbsystems.chat.api.core.model.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@RequiredArgsConstructor
@ToString
public enum ReturnCode {
    SUCCESS("성공"),
    ERROR("오류가 발생 하였습니다. 시스템 관리자에게 문의해주세요.");

    private final String message;
}