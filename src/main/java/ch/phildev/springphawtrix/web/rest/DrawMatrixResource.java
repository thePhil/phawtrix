package ch.phildev.springphawtrix.web.rest;

import ch.phildev.springphawtrix.communicator.ConnectToMatrixHandler;
import ch.phildev.springphawtrix.communicator.PublishToMatrixHandler;
import ch.phildev.springphawtrix.domain.PhawtrixCommand;
import ch.phildev.springphawtrix.service.ByteHandler;
import ch.phildev.springphawtrix.service.ColorHandler;
import ch.phildev.springphawtrix.service.CommandEncoder;
import ch.phildev.springphawtrix.service.CoordinateDecoder;
import ch.phildev.springphawtrix.web.rest.dto.*;
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
    private final ByteHandler byteHandler;

    public DrawMatrixResource(ColorHandler colorHandler,
                              ConnectToMatrixHandler connectHandler,
                              PublishToMatrixHandler publishHandler,
                              CommandEncoder commandEncoder,
                              CoordinateDecoder coordinateDecoder,
                              ByteHandler byteHandler) {
        this.colorHandler = colorHandler;
        this.connectHandler = connectHandler;
        this.publishHandler = publishHandler;
        this.commandEncoder = commandEncoder;
        this.coordinateDecoder = coordinateDecoder;
        this.byteHandler = byteHandler;
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
                    The color has to be specified as hex string in it's usual format `#000000` for black.""")
    @ApiResponse(responseCode = "200", description = "The payload send to the matrix in it's raw form as hex encoded " +
                                                     "string.")
    public Mono<AnswerDto> printText(@RequestBody @Valid DrawTextDto drawTextDto) {
        log.debug("Received: " + drawTextDto);

        Flux<byte[]> cmdPayload = Flux.just(
                commandEncoder.getPayloadForMatrix(PhawtrixCommand.CLEAR),
                commandEncoder.getPayloadForMatrix(PhawtrixCommand.DRAW_TEXT,
                        coordinateDecoder.getPayloadFromCoordinates(drawTextDto.getCoordinates()),
                        colorHandler.getHexColorAsPayloadArray(drawTextDto.getHexTextColor()),
                        drawTextDto.getText().trim().getBytes(StandardCharsets.UTF_8)
                ),
                commandEncoder.getPayloadForMatrix(PhawtrixCommand.SHOW));

        return connectAndPublishAndGetAnswer(cmdPayload);
    }

    @PostMapping("/circle")
    @Operation(summary = "Draw a circle to the matrix, filled or empty, with given coordinates in a given color",
            description = """
                    This endpoint posts a circle to the matrix.

                    ### Circle Shape
                    If the circle is supposed to be filled, then you need to specify it explicitly (set field to `true`).
                    Otherwise it will display an empty circle, with an outline width of 1 pixel.

                    ### Radius
                    The radius of the circle can be at most 3, if the entire circle should be displayed on the matrix.
                    Otherwise the circle will be cut of relative to it's positioning.

                    **A radius of 0 will display a single pixel, at the specified coordinates**.
                    This means that a radius of 2 will add two pixels on each of the four sides of the 1. central pixel.

                    ### Coordinates
                    The coordinate system root is the top left corner (0x0).
                    The coordinates specified, mark the always existing center pixel of the circle.

                    ### Color
                    The color has to be specified as hex string in it's usual format `#000000` for black.""")
    @ApiResponse(responseCode = "200", description = "The payload send to the matrix in it's raw form as hex encoded " +
                                                     "string.")
    public Mono<AnswerDto> drawCircle(@RequestBody @Valid DrawCircleDto drawCircleDto) {

        log.debug("Received: " + drawCircleDto);

        var circleCommand = drawCircleDto.isFillCircle() ? PhawtrixCommand.FILL_CIRCLE : PhawtrixCommand.DRAW_CIRCLE;

        var circlePayload = Flux.just(commandEncoder.getPayloadForMatrix(PhawtrixCommand.CLEAR),
                commandEncoder.getPayloadForMatrix(circleCommand,
                        coordinateDecoder.getPayloadFromCoordinates(drawCircleDto.getCoordinates()),
                        byteHandler.intToByteArray(drawCircleDto.getRadius()),
                        colorHandler.getHexColorAsPayloadArray(drawCircleDto.getHexTextColor())
                ),
                commandEncoder.getPayloadForMatrix(PhawtrixCommand.SHOW));

        return connectAndPublishAndGetAnswer(circlePayload);
    }

    @PostMapping("/pixel")
    @Operation(summary = "Display a single pixel on the matrix at a specified location in a given color",
            description = """
                    This endpoint posts a single pixel to the matrix

                    ### Coordinates
                    The coordinate system root is the top left corner (0x0).
                    The coordinates specified, mark the location of the pixel.

                    ### Color
                    The color has to be specified as hex string in it's usual format `#000000` for black. """)
    @ApiResponse(responseCode = "200", description = "The payload send to the matrix in it's raw form as hex encoded " +
                                                     "string.")
    public Mono<AnswerDto> drawPixel(@RequestBody @Valid DrawPixelDto drawPixelDto) {
        log.debug("Drawing Pixel: " + drawPixelDto);

        var pixelPayload = Flux.just(commandEncoder.getPayloadForMatrix(PhawtrixCommand.CLEAR),
                commandEncoder.getPayloadForMatrix(PhawtrixCommand.DRAW_PIXEL,
                        coordinateDecoder.getPayloadFromCoordinates(drawPixelDto.getCoordinates()),
                        colorHandler.getHexColorAsPayloadArray(drawPixelDto.getHexTextColor())),
                commandEncoder.getPayloadForMatrix(PhawtrixCommand.SHOW));

        return connectAndPublishAndGetAnswer(pixelPayload);
    }

    @PostMapping("/rect")
    @Operation(summary = "Display a rectangle width x height at a specified location in a given color",
            description = """
                    This endpoint posts a rectangle to the matrix.

                    This Example will draw a rectangle along the outer rim of the matrix in orange:
                    ```json
                    {
                      "coordinates": {
                        "x": 0,
                        "y": 0
                      },
                      "width": 32,
                      "height": 8,
                      "hexTextColor": "#ffa500"
                    }
                    ```

                    ### Coordinates
                    The coordinate system root is the top left corner (0x0).
                    The coordinates specified, mark the location of the corner pixel.

                    ### Metrics
                    * The width is specified as number of pixels width (Sideways to the right from the root pixel corner)
                    * The height is speicifed as number of pixels height (downwards from the root pixel corner)

                    ### Color
                    The color has to be specified as hex string in it's usual format `#000000` for black. """)
    public Mono<AnswerDto> drawRectangle(@RequestBody @Valid DrawRectangleDto rectangleDto) {
        log.debug("Drawing rectangle: " + rectangleDto);

        var rectanglePayload = Flux.just(commandEncoder.getPayloadForMatrix(PhawtrixCommand.CLEAR),
                commandEncoder.getPayloadForMatrix(PhawtrixCommand.DRAW_RECT,
                        coordinateDecoder.getPayloadFromCoordinates(rectangleDto.getCoordinates()),
                        byteHandler.intToByteArray(rectangleDto.getWidth()),
                        byteHandler.intToByteArray(rectangleDto.getHeight()),
                        colorHandler.getHexColorAsPayloadArray(rectangleDto.getHexTextColor())
                ),
                commandEncoder.getPayloadForMatrix(PhawtrixCommand.SHOW));

        return connectAndPublishAndGetAnswer(rectanglePayload);
    }

    private Mono<AnswerDto> connectAndPublishAndGetAnswer(Flux<byte[]> matrixPayload) {
        return connectHandler.connectScenario()
                .then(
                        publishHandler.publishScenario(matrixPayload)
                ).map(AnswerUtil::payloadAsHexStringDto);
    }
}
