CREATE TABLE players (
                         player_id UUID PRIMARY KEY,
                         player_name VARCHAR(100) NOT NULL UNIQUE
);

CREATE TABLE game_results (
                              id BIGSERIAL PRIMARY KEY,
                              move_count INT NOT NULL,
                              white_player_id UUID,
                              black_player_id UUID,
                              winning_player_id UUID,
                              winner VARCHAR(20),

                              CONSTRAINT fk_white_player
                                  FOREIGN KEY (white_player_id) REFERENCES players(player_id),

                              CONSTRAINT fk_black_player
                                  FOREIGN KEY (black_player_id) REFERENCES players(player_id),

                              CONSTRAINT fk_winning_player
                                  FOREIGN KEY (winning_player_id) REFERENCES players(player_id)
);