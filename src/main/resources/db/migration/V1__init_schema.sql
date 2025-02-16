CREATE TABLE players
(
    id           UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name         VARCHAR(255)                   NOT NULL,
    last_name    VARCHAR(255)                   NOT NULL,
    email        VARCHAR(255)                   NOT NULL UNIQUE,
    username     VARCHAR(255)                   NOT NULL UNIQUE,
    phone_number VARCHAR(255)                   NOT NULL UNIQUE,
    created_at   TIMESTAMPTZ      DEFAULT NOW() NOT NULL,
    updated_at   TIMESTAMPTZ,
    deleted      BOOLEAN          DEFAULT FALSE NOT NULL,
    version      INTEGER          DEFAULT 0
);

CREATE TABLE games
(
    id         UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    winner_id  UUID                           NULL,
    start_time TIMESTAMPTZ,
    end_time   TIMESTAMPTZ,
    created_at TIMESTAMPTZ      DEFAULT NOW() NOT NULL,
    updated_at TIMESTAMPTZ,
    deleted    BOOLEAN          DEFAULT FALSE NOT NULL,
    version    INTEGER          DEFAULT 0,
    CONSTRAINT fk_winner FOREIGN KEY (winner_id) REFERENCES players (id)
);

CREATE TABLE game_players
(
    game_id   UUID NOT NULL,
    player_id UUID NOT NULL,
    PRIMARY KEY (game_id, player_id),
    CONSTRAINT fk_game FOREIGN KEY (game_id) REFERENCES games (id) ON DELETE CASCADE,
    CONSTRAINT fk_player FOREIGN KEY (player_id) REFERENCES players (id) ON DELETE CASCADE
);

CREATE TABLE decks
(
    id         UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    cards      JSONB                          NOT NULL,
    created_at TIMESTAMPTZ      DEFAULT NOW() NOT NULL,
    updated_at TIMESTAMPTZ,
    deleted    BOOLEAN          DEFAULT FALSE NOT NULL,
    version    INTEGER          DEFAULT 0
);

CREATE TABLE game_player_cards
(
    game_id   UUID  NOT NULL,
    player_id UUID  NOT NULL,
    cards     JSONB NOT NULL,
    PRIMARY KEY (game_id, player_id),
    CONSTRAINT fk_game FOREIGN KEY (game_id) REFERENCES games (id) ON DELETE CASCADE,
    CONSTRAINT fk_player FOREIGN KEY (player_id) REFERENCES players (id) ON DELETE CASCADE
);