package com.shobu.api.dto.request;

import com.shobu.service.Player;
import com.shobu.domain.enums.Stone;
import com.shobu.domain.enums.TurnPhase;

public record StartGameRequest(TurnPhase startSide, Player player1, Player player2) {

}
