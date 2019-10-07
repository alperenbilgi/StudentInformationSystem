INSERT INTO project.lecturestudent (SELECT 376, 3010 WHERE NOT EXISTS
            (SELECT * FROM project.lecturestudent WHERE lecturestudent_lecture = 376 AND lecturestudent_student = 3010));