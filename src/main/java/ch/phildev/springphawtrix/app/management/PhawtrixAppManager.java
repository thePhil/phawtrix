package ch.phildev.springphawtrix.app.management;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import ch.phildev.springphawtrix.app.domain.PhawtrixApp;
import ch.phildev.springphawtrix.service.MatrixFrameDeliveryService;

/**
 * This manager is responsible for the lifecycle management of  a {@link PhawtrixApp}
 * <p>
 * The primary lifecycle include to following stages:
 *
 * <ol>
 *     <li>Initializing the app for usage <br>
 *         Done via {@link PhawtrixApp#init()}</li>
 *     <li>Persistance of the initialized app in {@link AppRepository} by delegating to the
 *     {@link ReactivePhawtrixAppRepositoryService}</li>
 *     <li>Executing the app
 *          <ul><li>Load from {@link AppRepository} with {@link ReactivePhawtrixAppRepositoryService}</li>
 *          <li>Stick into {@link PhawtrixCurrentAppHolder}</li>
 *          <li>Executing by using {@link PhawtrixApp#execute()}</li></ul>
 *     </li>
 *     <li>Shutting down the app</li>
 * </ol>
 * <p>
 * The manager uses a {@link ReactivePhawtrixAppRepositoryService} to retrieve apps from the repository and manages
 * the currently running app in an instance of a {@link PhawtrixCurrentAppHolder}.
 * <p>
 * The app is a publisher of frames that will be displayed using
 * {@link MatrixFrameDeliveryService#publishFrameToMatrix(Flux)}
 */
public interface PhawtrixAppManager {

    /**
     * Initialize an app for usage, by calling the apps setup method.
     * This will ensure the app is ready to go and all preconditions to successfully
     * run the app have been pre-filled.
     * <p>
     * Also the app has been persisted to the {@link ReactivePhawtrixAppRepositoryService}
     * so that it can be loaded to run.
     *
     * @param appName
     * @return the app initialized
     */
    Mono<PhawtrixApp> initApp(String appName);

    /**
     * Execute the app and return the currently executing app
     *
     * @param appName
     * @return
     */
    Mono<PhawtrixApp> executeApp(String appName);

    /**
     * Stop the app running with the given AppName and cleanup the app.
     *
     * @param appName
     * @return
     */
    Mono<PhawtrixApp> stopApp(String appName);

}
