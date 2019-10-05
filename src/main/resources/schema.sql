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

CREATE TRIGGER verify_op_insert
  BEFORE INSERT
  ON op
  WHEN NEW.scheduled = 1
BEGIN
  SELECT CASE
           WHEN EXISTS(SELECT 1
                       FROM op o
                       WHERE o.scheduled = 1
                         AND o.op_type_id = NEW.op_type_id
                         AND o.dt = NEW.dt)
                   THEN RAISE(ABORT, "uniqueness violated")
             END;
END;

CREATE TRIGGER verify_op_update
  BEFORE UPDATE
  ON op
  WHEN NEW.scheduled = 1
BEGIN
  SELECT CASE
           WHEN EXISTS(SELECT 1
                       FROM op o
                       WHERE o.scheduled = 1
                         AND o.op_type_id = NEW.op_type_id
                         AND o.dt = NEW.dt
                         AND o.id <> NEW.id)
                   THEN RAISE(ABORT, "uniqueness violated") END;
END;
END;