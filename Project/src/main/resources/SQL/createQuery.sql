DROP SCHEMA project CASCADE;
CREATE SCHEMA project;

------------------------------------------------------------------------------------------------------------------------

CREATE TABLE IF NOT EXISTS project."user" (
	user_id SMALLINT PRIMARY KEY,
	user_name TEXT NOT NULL,
	user_surname TEXT NOT NULL,
	user_email TEXT DEFAULT 'alperenbilgi07@gmail.com' NOT NULL,
	user_password TEXT DEFAULT 'e80282046f271ac66a13e77a485c135152b0f10609a906cbaa94d58d82669a98' NOT NULL,
	user_position SMALLINT DEFAULT 0 NOT NULL
);

INSERT INTO project."user" VALUES(3001, 'fatih can', 'kaya');
INSERT INTO project."user" VALUES(3002, 'mehmet', 'ikiz', DEFAULT, DEFAULT, 1);
INSERT INTO project."user" VALUES(3003, 'musa', 'ermek');
INSERT INTO project."user" VALUES(3004, 'ishak samet', 'oktar');
INSERT INTO project."user" VALUES(3005, 'yunus emre', 'bozkurt');
INSERT INTO project."user" VALUES(3006, 'ozcan arif', 'ozcan');
INSERT INTO project."user" VALUES(3007, 'alperen', 'bilgi', DEFAULT, DEFAULT, 2);
INSERT INTO project."user" VALUES(3008, 'enes', 'ayhan');
INSERT INTO project."user" VALUES(3009, 'sefa', 'tokac', DEFAULT, DEFAULT, 1);
INSERT INTO project."user" VALUES(3010, 'ali serhat', 'ozbagdat');
INSERT INTO project."user" VALUES(7085, 'bengisu', 'soydan');

------------------------------------------------------------------------------------------------------------------------

CREATE TABLE IF NOT EXISTS project.lecture (
	lecture_id SMALLINT PRIMARY KEY,
	lecture_name TEXT NOT NULL,
	lecture_credit SMALLINT NOT NULL,
	lecture_lecturer SMALLINT NOT NULL
);

INSERT INTO project.lecture VALUES(332, 'data communication and networking', 6, 7085);
INSERT INTO project.lecture VALUES(376, 'computer architecture', 6, 3009);
INSERT INTO project.lecture VALUES(368, 'algorithms II', 7, 7085);

------------------------------------------------------------------------------------------------------------------------

CREATE TABLE IF NOT EXISTS project.lecturestudent (
	lecturestudent_lecture SMALLINT NOT NULL,
	lecturestudent_student SMALLINT NOT NULL
);

INSERT INTO project.lecturestudent (SELECT 376, 3010 WHERE NOT EXISTS
            (SELECT * FROM project.lecturestudent WHERE lecturestudent_lecture = 376 AND lecturestudent_student = 3010));
INSERT INTO project.lecturestudent (SELECT 332, 3010 WHERE NOT EXISTS
            (SELECT * FROM project.lecturestudent WHERE lecturestudent_lecture = 332 AND lecturestudent_student = 3010));
INSERT INTO project.lecturestudent (SELECT 368, 3010 WHERE NOT EXISTS
            (SELECT * FROM project.lecturestudent WHERE lecturestudent_lecture = 368 AND lecturestudent_student = 3010));
INSERT INTO project.lecturestudent (SELECT 376, 3003 WHERE NOT EXISTS
            (SELECT * FROM project.lecturestudent WHERE lecturestudent_lecture = 376 AND lecturestudent_student = 3003));
INSERT INTO project.lecturestudent (SELECT 376, 3004 WHERE NOT EXISTS
            (SELECT * FROM project.lecturestudent WHERE lecturestudent_lecture = 376 AND lecturestudent_student = 3004));

------------------------------------------------------------------------------------------------------------------------

CREATE TABLE IF NOT EXISTS project.lecturegrade (
	lecturegrade_lecture SMALLINT NOT NULL,
	lecturegrade_student SMALLINT NOT NULL,
	lecturegrade_type SMALLINT NOT NULL,
	lecturegrade_grade SMALLINT NOT NULL
);

INSERT INTO project.lecturegrade (SELECT 376, 3003, 0, 90 WHERE NOT EXISTS
            (SELECT * FROM project.lecturegrade WHERE lecturegrade_lecture = 376 AND lecturegrade_student = 3003 AND lectureGrade_type = 0));
INSERT INTO project.lecturegrade (SELECT 376, 3003, 1, 80 WHERE NOT EXISTS
            (SELECT * FROM project.lecturegrade WHERE lecturegrade_lecture = 376 AND lecturegrade_student = 3003 AND lectureGrade_type = 1));
INSERT INTO project.lecturegrade (SELECT 376, 3010, 0, 17 WHERE NOT EXISTS
            (SELECT * FROM project.lecturegrade WHERE lecturegrade_lecture = 376 AND lecturegrade_student = 3010 AND lectureGrade_type = 0));

------------------------------------------------------------------------------------------------------------------------

CREATE TABLE IF NOT EXISTS project.log (
	"time" TEXT NOT NULL,
	"date" TEXT NOT NULL,
	"type" TEXT NOT NULL,
	message TEXT NOT NULL
);