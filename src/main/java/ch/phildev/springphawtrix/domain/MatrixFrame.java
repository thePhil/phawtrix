package ch.phildev.springphawtrix.domain;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

@Value(staticConstructor = "of")
@AllArgsConstructor
@Builder(toBuilder = true)
public class MatrixFrame {
    long frameNumber;
    List<byte[]> frameBuffer;

}
