SELECT *
FROM project."user", project.lecture, project.lecturegrade
WHERE lecturegrade_lecture = lecture_id
AND lecture_lecturer = user_id
AND lecturegrade_student = 3003;

SELECT * FROM project."user" WHERE user_id = 3003;