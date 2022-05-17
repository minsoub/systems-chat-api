package com.bithumbsystems.chat.api.core.model.response;

import com.bithumbsystems.chat.api.core.model.enums.ReturnCode;
import lombok.Getter;

@Getter
public class SingleResponse<T> {
    private final ReturnCode status;
    private final T data;

    public SingleResponse(T data) {
        this.status = ReturnCode.SUCCESS;
        this.data = data;
    }
}
