package ch.phildev.springphawtrix.web.rest;

import ch.phildev.springphawtrix.web.rest.dto.AnswerDto;
import com.google.common.io.BaseEncoding;

public class AnswerUtil {

    public static AnswerDto payloadAsHexStringDto(byte[] payload) {
        return AnswerDto.of(BaseEncoding.base16().encode(payload));
    }
}
