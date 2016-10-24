CREATE TABLE user (
  id         BIGSERIAL PRIMARY KEY,
  username   VARCHAR(500) NOT NULL UNIQUE,
  password   TEXT         NOT NULL,
  email      TEXT         NOT NULL,
  firstname  TEXT,
  lastname   TEXT,
  patronymic TEXT

);

CREATE INDEX user_name
  ON user (username);


CREATE TABLE role (
  name VARCHAR(100) PRIMARY KEY
);

INSERT INTO role (name) VALUES ('admin');
INSERT INTO role (name) VALUES ('author');

CREATE TABLE user_role (
  user_id BIGINT       NOT NULL REFERENCES user (id),
  role    VARCHAR(100) NOT NULL REFERENCES role (name),
  PRIMARY KEY (user_id, role)
);

CREATE INDEX u_r_uid
  ON user_role (user_id);

CREATE INDEX u_r_role
  ON user_role (role);

CREATE TABLE parent (
  id                 BIGSERIAL PRIMARY KEY,
  lastname   TEXT NOT NULL,
  firstname  TEXT NOT NULL,
  email      TEXT NOT NULL,
  patronymic TEXT
);
CREATE TABLE grade (
  id           SERIAL PRIMARY KEY,
  grade_no     INT        NOT NULL,
  grade_letter VARCHAR(1) NOT NULL,
  UNIQUE (grade_no, grade_letter)
);
CREATE TABLE parent_GRADE (
  parent_id BIGINT NOT NULL REFERENCES parent (id),
  grade_id  INT    NOT NULL REFERENCES grade (id),
  PRIMARY KEY (parent_id, grade_id)
);



