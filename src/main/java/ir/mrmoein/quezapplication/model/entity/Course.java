package ir.mrmoein.quezapplication.model.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class Course extends BaseEntity<Long> {

    @NotBlank
    @Column(nullable = false,unique = true)
    private String name;

    @Column
    private byte[] image;

    @ManyToOne
    private Teacher teacher;

    @Column
    private LocalDate startDate;

    @OneToMany(mappedBy = "category")
    private List<ExamQuestion> questions = new LinkedList<>();

    @Column
    private LocalDate endOfTerms;

    @ManyToMany(fetch = FetchType.LAZY)
    private List<Student> students;

    @OneToMany(mappedBy = "course")
    private List<Exam> exams = new LinkedList<>();

    public void setQuestions(ExamQuestion questions) {
        this.questions.add(questions);
    }

    public static CourseBuilder builder() {
        return new CourseBuilder();
    }

    public static class CourseBuilder {
        private String name;

        private byte[] image;

        private Teacher teacher;

        private LocalDate startDate;

        private LocalDate endOfTerms;

        private List<Student> students;

        public CourseBuilder name(String name) {
            this.name = name;
            return this;
        }

        public CourseBuilder image(byte[] image) {
            this.image = image;
            return this;
        }

        public CourseBuilder teacher(Teacher teacher) {
            this.teacher = teacher;
            return this;
        }


        public CourseBuilder startDate(LocalDate startDate) {
            this.startDate = startDate;
            return this;
        }

        public CourseBuilder endOfTerms(LocalDate endOfTerms) {
            this.endOfTerms = endOfTerms;
            return this;
        }

        public CourseBuilder students(Student student) {
            this.students.add(student);
            return this;
        }

        public Course build() {
            Course course = new Course();
            course.name = this.name;
            course.startDate = this.startDate;
            course.endOfTerms = this.endOfTerms;
            course.image = this.image;
            course.teacher = this.teacher;
            course.students = this.students;
            return course;
        }

    }

}
