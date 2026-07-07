package com.shobu.properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class PlayerProperties {

    @Value("${app.players.isaiah-id}")
    public UUID isaiahPlayerId;

    @Value("${app.players.julia-id}")
    public UUID juliaPlayerId;


}
