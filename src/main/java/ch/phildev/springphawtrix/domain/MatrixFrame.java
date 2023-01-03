package ch.phildev.springphawtrix.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

import java.util.List;
import java.util.stream.Collectors;

import static com.google.common.io.BaseEncoding.base16;

@Value(staticConstructor = "of")
@AllArgsConstructor
@Builder(toBuilder = true)
public class MatrixFrame {
    long frameNumber;
    List<byte[]> frameBuffer;

    @Override
    public String toString() {

        List<String> fb = frameBuffer.stream()
                .map(base16()::encode)
                .collect(Collectors.toList());

        return "MatrixFrame{" +
                "frameNumber=" + frameNumber +
                ", frameBuffer=" + fb +
                '}';
    }
}
