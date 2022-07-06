package com.abn.recipe.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@RequiredArgsConstructor
public class HttpError {
    private final String exceptionMessage;
    private final LocalDateTime time;
}
