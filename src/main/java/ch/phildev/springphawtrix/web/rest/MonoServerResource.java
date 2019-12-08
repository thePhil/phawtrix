package ch.phildev.springphawtrix.web.rest;

import ch.phildev.springphawtrix.service.MatrixService;
import ch.phildev.springphawtrix.web.rest.dto.AnswerDto;
import ch.phildev.springphawtrix.web.rest.dto.BrightnessDto;
import ch.phildev.springphawtrix.web.rest.dto.ColorDto;
import ch.phildev.springphawtrix.web.rest.dto.ConsoleCommandDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(value = "/setup",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class MonoServerResource {

    private final MatrixService matrixService;

    public MonoServerResource(MatrixService matrixService) {
        this.matrixService = matrixService;
    }

    @PostMapping("/brightness")
    public Mono<AnswerDto> setBrightness(@RequestBody BrightnessDto brightness) {
        return matrixService
                .setBrightness(brightness.getBrightness())
                .map(AnswerUtil::payloadAsHexStringDto);
    }

    @PostMapping("/fill-with-color")
    public Mono<AnswerDto> fillMatrix(@RequestBody ColorDto htmlHex) {
        return matrixService.fillWithColor(htmlHex.getHexColor())
                .map(AnswerUtil::payloadAsHexStringDto);
    }

    @PostMapping("/console")
    public Mono<AnswerDto> console(@RequestBody ConsoleCommandDto commandString) {
        return matrixService.console(commandString.getCommand())
                .map(AnswerUtil::payloadAsHexStringDto);
    }
}
