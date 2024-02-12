CREATE TABLE couriers
(
    ID      INTEGER PRIMARY KEY,
    FST_NME VARCHAR(64) NOT NULL,
    LST_NME VARCHAR(64) NOT NULL,
    ACTV    BOOLEAN     NOT NULL
);

INSERT INTO couriers (ID, FST_NME, LST_NME, ACTV) VALUES
(1, 'Bob', 'Sponge', 1),
(2, 'Patrick', 'Star', 0);