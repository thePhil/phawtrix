package ch.phildev.springphawtrix.web.rest;

import com.google.common.io.BaseEncoding;

import ch.phildev.springphawtrix.web.rest.dto.AnswerDto;

public class AnswerUtil {

    public static AnswerDto payloadAsHexStringDto(byte[] payload) {
        return AnswerDto.builder().payLoadToMatrix(BaseEncoding.base16().encode(payload)).build();
    }
}
