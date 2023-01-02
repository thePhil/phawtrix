package ch.phildev.springphawtrix.app.management;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.stereotype.Component;

import ch.phildev.springphawtrix.service.ColorHandler;
import ch.phildev.springphawtrix.service.CommandEncoder;
import ch.phildev.springphawtrix.service.MatrixTextCommandProvider;

@AllArgsConstructor
@Getter
@Builder
@Component
public class AppDrawingComponentHolder {
    private final MatrixTextCommandProvider matrixTextCommandProvider;
    private final CommandEncoder commandEncoder;
    private final ColorHandler colorHandler;
}
