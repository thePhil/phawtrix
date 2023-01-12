package ch.phildev.springphawtrix.web.rest;

import ch.phildev.springphawtrix.app.management.PhawtrixAppManager;
import ch.phildev.springphawtrix.communicator.ConnectToMatrixHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@Slf4j
@RequestMapping(value = "/app",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class AppResource {

    private final PhawtrixAppManager appManager;
    private final ConnectToMatrixHandler connectToMatrixHandler;

    @GetMapping(value = "/command/{appName}/execute",
            consumes = MediaType.ALL_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<String> executeAppAsync(@PathVariable String appName) {

        Assert.hasText(appName, "AppName must be given");
        log.debug("reached the controller");

        return connectToMatrixHandler.connectScenario()
                .checkpoint()
                .then(appManager.initApp(appName))
                .doOnSuccess(data -> log.debug("App is initialized"))
                .checkpoint()
                .doOnSuccess(data -> appManager.executeApp(appName).subscribe())
                .map(sub -> "Successfully fired");
    }

    @PostMapping(value = "/command/{appName}/stop",
            consumes = MediaType.ALL_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<Void> stopApp(@PathVariable String appName) {
        Assert.hasText(appName, "AppName must be given");
        log.debug("Reached the stopping controller");

        return appManager.stopApp(appName);
    }
}
