package domain.service;

import domain.generic.LectureTime;
import domain.generic.Period;
import domain.model.*;
import domain.repository.CourseRepository;
import domain.repository.LectureRepository;
import domain.repository.StudentRepository;
import domain.service.RegisterService;
import domain.service.RegisteringPeriod;
import infra.database.SimpleCourseRepository;
import infra.database.SimpleLectureRepository;
import infra.database.SimpleStudentRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class RegisterServiceTest {
    public static LectureRepository lectureRepo;
    public static CourseRepository courseRepo;
    public static StudentRepository stdRepo;
    public static RegisterService registerService;

    @BeforeAll
    public static void setUp(){
        lectureRepo = new SimpleLectureRepository();
        courseRepo = new SimpleCourseRepository();
        stdRepo = new SimpleStudentRepository();
    }

    @BeforeEach
    public void init() throws Exception {
        registerService = new RegisterService(lectureRepo, courseRepo);
        Professor p1 = new Professor(new ProfessorID("1"));

        Course c1 = new Course(new CourseID(1), 3);
        Course c2 = new Course(new CourseID(2), 3);
        Course c3 = new Course(new CourseID(3), 3);
        Course c4 = new Course(new CourseID(4), 21);
        courseRepo.save(c1);
        courseRepo.save(c2);
        courseRepo.save(c3);
        courseRepo.save(c4);

        Set<LectureTime> time1 = new HashSet<>();
        time1.add(
                new LectureTime(
                        LectureTime.DayOfWeek.MON,
                        LectureTime.LecturePeriod.THIRD,
                        LectureTime.LecturePeriod.FIFTH,
                        "D123"
                )
        );

        Lecture l1 = new Lecture(
                new LectureID("1"),
                p1.getId(),
                2,
                c1.getId(),
                time1
        );

        Set<LectureTime> time2 = new HashSet<>();
        time2.add(
                new LectureTime(
                        LectureTime.DayOfWeek.TUE,
                        LectureTime.LecturePeriod.THIRD,
                        LectureTime.LecturePeriod.FIFTH,
                        "D123"
                )
        );

        Lecture l2 = new Lecture(
                new LectureID("2"),
                p1.getId(),
                2,
                c2.getId(),
                time2
        );

        Set<LectureTime> time3 = new HashSet<>();
        time3.add(
                new LectureTime(
                        LectureTime.DayOfWeek.FRI,
                        LectureTime.LecturePeriod.THIRD,
                        LectureTime.LecturePeriod.FIFTH,
                        "D123"
                )
        );

        Lecture l3 = new Lecture(
                new LectureID("3"),
                p1.getId(),
                2,
                c2.getId(),
                time3
        );


        Lecture l4 = new Lecture(
                new LectureID("4"),
                p1.getId(),
                2,
                c3.getId(),
                time3
        );

        Set<LectureTime> time4 = new HashSet<>();
        time4.add(
                new LectureTime(
                        LectureTime.DayOfWeek.FRI,
                        LectureTime.LecturePeriod.FIRST,
                        LectureTime.LecturePeriod.FIFTH,
                        "D123"
                )
        );

        Lecture l5 = new Lecture(
                new LectureID("5"),
                p1.getId(),
                2,
                c3.getId(),
                time4
        );

        Set<LectureTime> time5 = new HashSet<>();
        time5.add(
                new LectureTime(
                        LectureTime.DayOfWeek.WED,
                        LectureTime.LecturePeriod.FIRST,
                        LectureTime.LecturePeriod.THIRD,
                        "D123"
                )
        );

        Lecture l6 = new Lecture(
                new LectureID("6"),
                p1.getId(),
                2,
                c4.getId(),
                time5
        );

        lectureRepo.save(l1);
        lectureRepo.save(l2);
        lectureRepo.save(l3);
        lectureRepo.save(l4);
        lectureRepo.save(l5);
        lectureRepo.save(l6);

        Student std1 = new Student(new StudentID("1"), Student.Year.FRESHMAN);
        Student std2 = new Student(new StudentID("2"), Student.Year.FRESHMAN);
        Student std3 = new Student(new StudentID("3"), Student.Year.FRESHMAN);
        Student std4 = new Student(new StudentID("4"), Student.Year.FRESHMAN);
        Student std5 = new Student(new StudentID("5"), Student.Year.SOPHOMORE);

        stdRepo.save(std1);
        stdRepo.save(std2);
        stdRepo.save(std3);
        stdRepo.save(std4);
        stdRepo.save(std5);

        Set<Student.Year> allowedYears = new HashSet<>();
        allowedYears.add(Student.Year.FRESHMAN);
        allowedYears.add(Student.Year.SENIOR);


        registerService.addRegisteringPeriod(
                new RegisteringPeriod(
                        new Period(
                                LocalDateTime.of(2021,03,01,12,00,00),
                                LocalDateTime.of(2021,12,01,12,00,00)
                        ),
                        allowedYears
                )
        );
    }

    @DisplayName("수강신청 성공 테스트")
    @Test
    public void registerSuccessTest() throws Exception {
        Lecture lecture = lectureRepo.findByID(new LectureID("1"));
        Student std = stdRepo.findByID(new StudentID("1"));

        registerService.register(lecture, std);
    }

    @DisplayName("수강인원초과 실패 테스트")
    @Test
    public void exceedLimitStdFailTest(){
        Student std1 = stdRepo.findByID(new StudentID("1"));
        Student std2 = stdRepo.findByID(new StudentID("2"));
        Student std3 = stdRepo.findByID(new StudentID("3"));
        Student std4 = stdRepo.findByID(new StudentID("4"));

        Lecture lecture = lectureRepo.findByID(new LectureID("1"));
        registerService.register(lecture, std1);
        registerService.register(lecture, std2);
        registerService.register(lecture, std3);

        IllegalStateException exception = assertThrows(IllegalStateException.class, ()-> {
            registerService.register(lecture, std4);
        });

        assertEquals("수강인원이 초과 되었습니다", exception.getMessage());
    }

    @DisplayName("중복강의 실패 테스트")
    @Test
    public void duplicatedCourseFailTest(){
        Student std1 = stdRepo.findByID(new StudentID("1"));
        Lecture lecture1 = lectureRepo.findByID(new LectureID("2"));
        Lecture lecture2 = lectureRepo.findByID(new LectureID("3"));

        registerService.register(lecture1, std1);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, ()-> {
            registerService.register(lecture2, std1);
        });

        assertEquals("중복된 수강입니다.", exception.getMessage());
    }

    @DisplayName("중복시간 실패 테스트 - 완전히 같은시간겹치는경우")
    @Test
    public void allDuplicatedTimeFailTest(){
        Student std1 = stdRepo.findByID(new StudentID("1"));
        Lecture lecture1 = lectureRepo.findByID(new LectureID("3"));
        Lecture lecture2 = lectureRepo.findByID(new LectureID("4"));

        registerService.register(lecture1, std1);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, ()-> {
            registerService.register(lecture2, std1);
        });

        assertEquals("같은시간에 강의를 수강중입니다.", exception.getMessage());
    }

    @DisplayName("중복시간 실패 테스트 - 부분 시간겹치는경우")
    @Test
    public void partitionDuplicatedTimeFailTest(){
        Student std1 = stdRepo.findByID(new StudentID("1"));
        Lecture lecture1 = lectureRepo.findByID(new LectureID("4"));
        Lecture lecture2 = lectureRepo.findByID(new LectureID("5"));

        registerService.register(lecture1, std1);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, ()-> {
            registerService.register(lecture2, std1);
        });

        assertEquals("같은시간에 강의를 수강중입니다.", exception.getMessage());
    }

    @DisplayName("학점초과 실패 테스트")
    @Test
    public void exceedCreditFailTest(){
        Student std1 = stdRepo.findByID(new StudentID("1"));
        Lecture l1 = lectureRepo.findByID(new LectureID("1"));
        Lecture l6 = lectureRepo.findByID(new LectureID("6"));

        registerService.register(l1, std1);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, ()-> {
            registerService.register(l6, std1);
        });

        assertEquals("수강할수 있는 학점을 초과했습니다.", exception.getMessage());
    }

    @DisplayName("수강안하는 강의 수강취소 실패 테스트")
    @Test
    public void notRegisteredLectureCancelFailTest(){
        Student std1 = stdRepo.findByID(new StudentID("1"));
        Lecture l1 = lectureRepo.findByID(new LectureID("1"));
        Lecture l2 = lectureRepo.findByID(new LectureID("2"));

        registerService.register(l1, std1);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, ()-> {
            registerService.cancel(l2, std1);
        });

        assertEquals("수강하지 않는 강의 입니다.", exception.getMessage());
    }

    @DisplayName("수강취소 성공 테스트")
    @Test
    public void cancelSuccessTest(){
        Student std1 = stdRepo.findByID(new StudentID("1"));
        Lecture l1 = lectureRepo.findByID(new LectureID("1"));

        registerService.register(l1, std1);
        registerService.cancel(l1, std1);
    }

    @DisplayName("수강기간X 수강신청 실패 - 수강기간 아닐때")
    @Test
    public void notRegisteringPeriodFailTest(){
        Set<Student.Year> allowedYears = new HashSet<>();
        allowedYears.add(Student.Year.SOPHOMORE);

        registerService.addRegisteringPeriod(
                new RegisteringPeriod(
                        new Period(
                                LocalDateTime.of(2021,03,01,12,00,00),
                                LocalDateTime.of(2021,04,01,12,00,00)
                        ),
                        allowedYears
                )
        );

        Student std5 = stdRepo.findByID(new StudentID("5"));
        Lecture l1 = lectureRepo.findByID(new LectureID("1"));

        IllegalStateException exception = assertThrows(IllegalStateException.class, ()-> {
            registerService.register(l1, std5);
        });

        assertEquals("해당학년 수강신청 기간이 아닙니다.", exception.getMessage());
    }

    @DisplayName("수강기간X 수강신청 실패 - 해당학년 아닐때")
    @Test
    public void notRegisteringPeriodAboutStdFailTest(){
        Student std5 = stdRepo.findByID(new StudentID("5"));
        Lecture l1 = lectureRepo.findByID(new LectureID("1"));

        IllegalStateException exception = assertThrows(IllegalStateException.class, ()-> {
            registerService.register(l1, std5);
        });

        assertEquals("해당학년 수강신청 기간이 아닙니다.", exception.getMessage());

    }
}