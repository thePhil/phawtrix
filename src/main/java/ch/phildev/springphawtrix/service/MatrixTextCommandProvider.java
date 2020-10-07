package ch.phildev.springphawtrix.service;

import ch.phildev.springphawtrix.web.rest.dto.DrawTextDto;

public interface MatrixTextCommandProvider {

    byte[] textDeliverFrame(DrawTextDto drawTextDto);
}
