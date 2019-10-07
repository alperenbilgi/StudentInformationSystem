## Java Implementation

This is a Maven project which aims create a Student Information System. Students are able to choose lectures and see their grades after log in. Lecturers are able to list students in their lecture and assign notes. Also there are admins in system who controls registration process and other important processes. Project is constructed with MVC (Model-View-Controller) architecture.

In Model part, there is a User class which holds ID, Name, Surname, Email, Password and Position informations as parameters. There is Lecture class extended from User class, which holds ID, Name, Credit informations as paramaters. LectureStudent and LectureGrade classes are extended from Lecture class. Except these there is a Log Class which holds system's log record with Time, Date, Type and Message parameters. All these classes are constructed as Plain Old Java Objects (POJOs).

In Controller part, there is a Main class which holds currentUser information and main method. Users are able to make transactions via Database or Excel. There is a DataOperator interface for related methods. DBManager, FileManager and MemcachedManager classes are implemented from DataOperator interface. Database connection is provided from DBManager class and file connection is provided from FileManager class. DataManager class allows the user to make selection. By default, users make transactions via memcached but if related informations are not available in cache, transaction is made via secondary operator. Also all transactions are recorded to database and log.txt.

![Scheme](https://bitbucket.org/alperenbilgi/student-information-system/downloads/Class.png)

---

## Database Implementation

All data is stored in PostgreSQL. User, Lecture, LectureStudent, LectureGrade and Log tables are created in database. User table has ID, Name, Surname, Email, Password and Position attributes. Lecture table has ID, Name, Credit and Lecturer attributes. Knowledge of which student chose which course is stored in LectureStudent table and all grades are stored in LectureGrade table which includes Lecture, Student, Exam and Grade attributes. ID attributes are defined as primary key so each ID is unique. All attributes are defined as smallint or text type and hierarchical structure of database is kept at minimum cost.

![Scheme](https://bitbucket.org/alperenbilgi/student-information-system/downloads/Database.png)
