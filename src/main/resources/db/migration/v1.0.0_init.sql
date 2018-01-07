CREATE TABLE user (
  id         BIGSERIAL PRIMARY KEY,
  username   VARCHAR(500) NOT NULL UNIQUE,
  password   VARCHAR(300) NOT NULL,
  email      VARCHAR(300) NOT NULL,
  firstname  VARCHAR(300),
  lastname   VARCHAR(300),
  patronymic VARCHAR(300)

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
  id         BIGSERIAL PRIMARY KEY,
  lastname   VARCHAR(300) NOT NULL,
  firstname  VARCHAR(300) NOT NULL,
  email      VARCHAR(300) NOT NULL UNIQUE,
  patronymic VARCHAR(300),
  UNIQUE (lastname, firstname, email, patronymic)
);


CREATE TABLE grade (
  id           SERIAL PRIMARY KEY,
  grade_no     INT        NOT NULL,
  grade_letter VARCHAR(1) NOT NULL,
  UNIQUE (grade_no, grade_letter)
);

CREATE TABLE parent_grade (
  parent_id BIGINT NOT NULL REFERENCES parent (id),
  grade_id  INT    NOT NULL REFERENCES grade (id),
  PRIMARY KEY (parent_id, grade_id)
);

CREATE TABLE student (
  id         BIGSERIAL PRIMARY KEY,
  firstname  VARCHAR(300) NOT NULL,
  lastname   VARCHAR(300) NOT NULL,
  patronymic VARCHAR(300),
  grade_id   INT          NOT NULL REFERENCES grade (id),
  UNIQUE (firstname, lastname, patronymic, grade_id)
);

CREATE TABLE parent_student (
  parent_id  BIGINT NOT NULL REFERENCES parent (id),
  student_id BIGINT NOT NULL REFERENCES student (id),
  PRIMARY KEY (parent_id, student_id)
);

CREATE INDEX student_grade
  ON student (grade_id);

CREATE INDEX p_s_pid
  ON parent_student (parent_id);

CREATE INDEX p_s_sid
  ON parent_student (student_id);



