package infra.dto;


import domain.model.Course;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CourseDTO {
    private long id;
    private int targetYear;
    private int credit;
    private String courseCode;
    private String department;
    private String courseName;

    private CourseDTO(){}

    public CourseDTO(long id, int targetYear, int credit,
                     String courseCode, String department, String courseName){
        this.id = id;
        this.targetYear = targetYear;
        this.credit = credit;
        this.courseCode = courseCode;
        this.department = department;
        this.courseName = courseName;
    }

}
