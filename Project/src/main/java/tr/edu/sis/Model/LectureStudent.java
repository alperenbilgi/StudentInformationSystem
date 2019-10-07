package tr.edu.sis.Model;

public class LectureStudent extends Lecture {

    private User student;

    public User getStudent() {
        return student;
    }

    public void setStudent(User student) {
        this.student = student;
    }
}
