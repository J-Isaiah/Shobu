package com.shobu.api.dto.request;

import com.shobu.application.Player;
import com.shobu.domain.Stone;

public record StartGameRequest(Stone startSide, Player player1, Player player2) {

}
