CREATE TABLE actor (
  id   INTEGER PRIMARY KEY,
  name TEXT    NOT NULL UNIQUE,
  note TEXT
);

CREATE TABLE op_type (
  id   INTEGER PRIMARY KEY,
  name TEXT    NOT NULL UNIQUE,
  note TEXT
);

CREATE TABLE op (
  id         INTEGER PRIMARY KEY,
  name       TEXT    NOT NULL UNIQUE,
  note       TEXT,
  actor_id   INTEGER NOT NULL REFERENCES actor (id) ON DELETE RESTRICT,
  op_type_id INTEGER NOT NULL REFERENCES op_type (id) ON DELETE RESTRICT,
  dt         DATE    NOT NULL,
  scheduled  BOOLEAN,
  UNIQUE (actor_id, op_type_id, dt)
);