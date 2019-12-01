//package ch.phildev.springphawtrix.web.rest;
//
//import ch.phildev.phawtrix.domain.MqttConfig;
//import com.google.common.collect.ImmutableList;
//import com.google.common.io.BaseEncoding;
//import com.google.common.primitives.Bytes;
//import com.hivemq.client.mqtt.datatypes.MqttQos;
//import com.hivemq.client.mqtt.mqtt3.Mqtt3BlockingClient;
//import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import javax.inject.Inject;
//import javax.validation.constraints.Max;
//import javax.validation.constraints.Min;
//import javax.ws.rs.*;
//import javax.ws.rs.core.MediaType;
//import javax.ws.rs.core.Response;
//import java.awt.*;
//import java.nio.ByteBuffer;
//import java.util.Arrays;
//import java.util.Collections;
//import java.util.List;
//
//@Path("/setup")
//@Produces(MediaType.APPLICATION_JSON)
//@Consumes(MediaType.APPLICATION_JSON)
//public class ServerResource {
//
//    private final Logger log = LoggerFactory.getLogger(ServerResource.class);
//
//    @Inject
//    MqttConfig cfg;
//
//    @Inject
//    Mqtt3BlockingClient client;
//
//
//    @GET
//    @Path("scheduler-status")
//    public Response getSchedulerStatus() {
//
//        return Response.ok().build();
//    }
//
//    @POST
//    @Path("set-brightness")
//    public Response setBrightness(@RequestBody @Max(255) @Min(10) int brightness) {
//
//        log.debug(String.format("Received brightness of: %s", brightness));
//        byte[] brightnessArray = new byte[1];
//
//
//        brightnessArray[0] = (byte) brightness;
//        log.debug("My byte is a " + brightnessArray[0]);
//
////        commandToMatrix(PhawtrixCommand.CLEAR, Collections.emptyList());
//        commandToMatrix(PhawtrixCommand.SET_BRIGHTNESS, Collections.emptyList(), brightnessArray);
////        commandToMatrix(PhawtrixCommand.SHOW, Collections.emptyList());
//
//        return Response.ok().build();
//    }
//
//    @POST
//    @Path("fill-color")
//    public Response fillMatrixWithColor(@RequestBody String htmlHex) {
//        commandToMatrix(PhawtrixCommand.CLEAR, Collections.emptyList());
//        byte[] params = getHexColorAsArray(htmlHex);
//        commandToMatrix(PhawtrixCommand.FILL_MATRIX, Collections.emptyList(), params);
//        commandToMatrix(PhawtrixCommand.SHOW, Collections.emptyList());
//
//        return Response.ok().build();
//    }
//
//
//    /**
//     * Accepts commands in the format command;x-coordinate,y-coordinate;color-r,color-g,color-b;
//     *
//     * @param payloadIncoming
//     * @return
//     */
//    @POST
//    @Path("console")
//    public Response initAwtrix(@RequestBody String payloadIncoming) {
//
//
//        // decode command
//        PhawtrixCommand command = decodeCommand(splittedPayloads);
//
//        byte[] payload = commandToMatrix(command, splittedPayloads);
//        return Response.ok(payload).build();
//    }
//
//
//
//    private byte[] handleCommandWithCoordinates(PhawtrixCommand command, List<String> splittedPayloads) {
//        byte[] cmdArray = new byte[1];
//
//        cmdArray[0] = (byte) command.getByteValue();
//
//        byte[] cordArray = getCoordinatesArray(splittedPayloads.get(1));
//        byte[] paramArray = new byte[0];
//
//        switch (command) {
//            case DRAW_PIXEL:
//                paramArray = getColorAsArray(splittedPayloads.get(2));
//        }
//
//        return Bytes.concat(cmdArray, cordArray, paramArray);
//    }
//
//    private byte[] getCoordinatesArray(String coordinatesPair) {
//        byte[] cordArray = new byte[4];
//        cordArray[0] = (byte) 0xFFFFFFFF;
//        cordArray[1] = (byte) 0xFFFFFFFF;
//        cordArray[2] = Coordinates.of(coordinatesPair).getByteX();
//        cordArray[3] = Coordinates.of(coordinatesPair).getByteY();
//        return cordArray;
//    }
//}
