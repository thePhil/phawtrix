package ch.phildev.springphawtrix.web.rest;

import ch.phildev.springphawtrix.communicator.ConnectToMatrixHandler;
import ch.phildev.springphawtrix.communicator.PublishToMatrixHandler;
import ch.phildev.springphawtrix.domain.PhawtrixCommand;
import ch.phildev.springphawtrix.service.ColorHandler;
import ch.phildev.springphawtrix.service.CommandEncoder;
import ch.phildev.springphawtrix.service.CoordinateDecoder;
import ch.phildev.springphawtrix.web.rest.dto.AnswerDto;
import ch.phildev.springphawtrix.web.rest.dto.DrawDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import java.nio.charset.StandardCharsets;

@RestController
@Slf4j
@RequestMapping(value = "/draw",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE)
public class DrawMatrixResource {


    private final ColorHandler colorHandler;
    private final ConnectToMatrixHandler connectHandler;
    private final PublishToMatrixHandler publishHandler;
    private final CommandEncoder commandEncoder;
    private final CoordinateDecoder coordinateDecoder;

    public DrawMatrixResource(ColorHandler colorHandler,
                              ConnectToMatrixHandler connectHandler,
                              PublishToMatrixHandler publishHandler,
                              CommandEncoder commandEncoder,
                              CoordinateDecoder coordinateDecoder) {
        this.colorHandler = colorHandler;
        this.connectHandler = connectHandler;
        this.publishHandler = publishHandler;
        this.commandEncoder = commandEncoder;
        this.coordinateDecoder = coordinateDecoder;
    }

    @PostMapping("/text")
    @Operation(summary = "Displays the provided text at the provided coordinate in the defined color on the matrix",
            description = """
                    This endpoint pushes a text to the matrix.
                    ### Text
                    If the text doesn't fit onto the 32x8 matrix, it will not be scrolled and what doesn't fit will not be displayed.
                    The approximate length is 8 characters (currently the matrix uses a fix width font of 3 pixels per font + 1 space between two chars).
                    Spaces will be displayed by using 3 chars.

                    ### Coordinates
                    The coordinate system root is the top left corner (0x0). The coordinates specified mark the bottom left corner of the first character of the defined string.

                    * There is an x offset of 1 added to the specified x-coordinate.
                    * There is a y offset of 5 added to the specified y-coordinate, which will ensure that specifying (0,0) as coordinates, will display the text in its full height.

                    ### Color
                    The color has to be specified as hex string in it's usual format `#000000` for black."""
    )
    @ApiResponse(responseCode = "200", description = "The payload send to the matrix in it's raw form as hex encoded " +
                                                     "string.")
    public Mono<AnswerDto> printText(@RequestBody @Valid DrawDto drawDto) {
        log.debug("Received: " + drawDto);

        Flux<byte[]> cmdPayload = Flux.just(
                commandEncoder.getPayloadForMatrix(PhawtrixCommand.CLEAR),
                commandEncoder.getPayloadForMatrix(PhawtrixCommand.DRAW_TEXT,
                        coordinateDecoder.getPayloadFromCoordinates(drawDto.getCoordinates()),
                        colorHandler.getHexColorAsPayloadArray(drawDto.getHexTextColor()),
                        drawDto.getText().trim().getBytes(StandardCharsets.UTF_8)
                ),
                commandEncoder.getPayloadForMatrix(PhawtrixCommand.SHOW));

        return connectHandler.connectScenario()
                .then(
                        publishHandler.publishScenario(cmdPayload)
                ).map(AnswerUtil::payloadAsHexStringDto);
    }
}
