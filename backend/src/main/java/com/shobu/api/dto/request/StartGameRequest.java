package com.shobu.api.dto.request;

import com.shobu.service.Player;
import com.shobu.domain.enums.Stone;

public record StartGameRequest(Stone startSide, Player startPlayer) {

}
