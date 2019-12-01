package ch.phildev.springphawtrix.service;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public interface MatrixService {

    Mono<byte[]> setBrightness(int brightness);

    Mono<byte[]> fillWithColor(String htmlHex);

    Mono<byte[]> console(String consoleCommand);
}
