CREATE TABLE "group" (
  id   BIGSERIAL PRIMARY KEY,
  name VARCHAR(300) NOT NULL
);

CREATE TABLE group_student (
  student_id BIGINT NOT NULL REFERENCES student (id),
  group_id   BIGINT NOT NULL REFERENCES "group" (id)
);

CREATE INDEX gr_s
  ON group_student (student_id);

CREATE INDEX gr_g
  ON group_student (group_id);

