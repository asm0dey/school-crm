alter table student
  drop constraint student_grade_id_fkey;

alter table student
  add constraint student_grade_id_fkey
    foreign key (grade_id) references grade
      on delete cascade;

alter table parent_student
  drop constraint parent_student_student_id_fkey;

alter table parent_student
  add constraint parent_student_student_id_fkey
    foreign key (student_id) references student
      on delete cascade;

alter table group_student
  drop constraint group_student_student_id_fkey;

alter table group_student
  add constraint group_student_student_id_fkey
    foreign key (student_id) references student
      on delete cascade;
