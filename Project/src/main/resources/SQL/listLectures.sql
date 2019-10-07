SELECT *
FROM project."user", project.lecture, project.lecturestudent
WHERE lecturestudent_lecture = lecture_id
AND lecture_lecturer = user_id
AND lecturestudent_student = 3010;

SELECT * FROM project."user" WHERE user_id = 3010;