package ch.phildev.springphawtrix.app.domain;

import reactor.core.publisher.Flux;

public interface PhawtrixApp {

    /**
     * Initialize the app and prepare for usage, so that the execute method can be called to ensure it can produce
     * frames.
     */
    void init();

    /**
     * Each execution step produces a frame, to be displayed on the phawtrix.
     * A frame is a series of byte arrays emitted by a flux. The flux is subscribed on, shortly before a frame is
     * sent to the phawtrix.
     * The flux must stop emitting byte arrays once the frame is closed. Usage of {@link Flux#just(Object[])} is
     * recommended.
     *
     * @return a frame as described before
     */
    Flux<byte[]> execute();

    /**
     * Stop or pause the execution of the app
     */
    void stop();
}
