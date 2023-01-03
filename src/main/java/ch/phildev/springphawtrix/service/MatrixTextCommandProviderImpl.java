package ch.phildev.springphawtrix.service;

import ch.phildev.springphawtrix.domain.PhawtrixCommand;
import ch.phildev.springphawtrix.web.rest.dto.DrawTextDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

@Component
@RequiredArgsConstructor
@Slf4j
public class MatrixTextCommandProviderImpl implements MatrixTextCommandProvider {
    private final CommandEncoder commandEncoder;
    private final CoordinateDecoder coordinateDecoder;
    private final ColorHandler colorHandler;

    @Override
    public byte[] textDeliverFrame(DrawTextDto drawTextDto) {
        return commandEncoder.getPayloadForMatrix(PhawtrixCommand.DRAW_TEXT,
                coordinateDecoder.getPayloadFromCoordinates(drawTextDto.getCoordinates()),
                colorHandler.getHexColorAsPayloadArray(drawTextDto.getHexTextColor()),
                drawTextDto.getText().trim().getBytes(StandardCharsets.UTF_8));
    }
}
