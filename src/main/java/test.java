
import application.AdminAppService;
import application.CourseAppService;
import application.ProfessorAppService;
import application.StudentAppService;
import domain.model.Course;
import domain.repository.*;
import infra.database.MyBatisConnectionFactory;
import infra.database.repository.*;
import infra.dto.AdminDTO;
import infra.dto.CourseDTO;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

public class test {
    public static void main(String[] args) throws NoSuchFieldException, IllegalAccessException, ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException {
//        LectureRepository lectureRepo = new RDBLectureRepository();
//        Course c = Course.builder()
//                    .id(1)
//                    .courseCode("222")
//                .courseName("hello")
//                .department("4r")
//                    .targetYear(2)
//                .credit(3)
//                .build();
        
//        Refs r = new Refs('d', 5, new Refs2(2, 3));
//        Derived c = new Derived(1, 2, 3, "4ff", r);
//        byte[] a = Serializer.objectToBytes(c);
//        Derived c2 = (Derived) Deserializer.bytesToObject(a);
//        System.out.println("c2 = " + c2);
        
//        for(Field f : Serializer.getAllFields(ArrayList.class)){
//            System.out.println("f = " + f);
//        }

//        Class clazz = Class.forName("infra.dto.CourseDTO");
//
//        Constructor c = clazz.getDeclaredConstructor();
//        c.setAccessible(true);
//        CourseDTO courseDTO = (CourseDTO) c.newInstance();
//        Field f = clazz.getDeclaredField("id");
//        f.setAccessible(true);
//        f.setLong(courseDTO, 3);
//        System.out.println("courseDTO = " + courseDTO);
//        String n = Object.class.getName();
//        System.out.println("n = " + n);
//        List<Integer> a = new ArrayList<>();
//        a.add(2); a.add(3);
//
//        int[] a = new int[3];
//        a[0] = 1;



//        Field f = Course.class.getDeclaredField("courseCode");
//        f.setAccessible(true);
//        String a = new String((String)f.get(c));
//        System.out.println("a = " + a);
//        System.out.println(f.getType().getSimpleName());
//        System.out.println("name = " + name);
//        for(Field f : Serializer.getAllFields(Student.class)){
//            System.out.println("f = " + f.getType().getSimpleName().equals("int"));
//        }

//        Set<LectureTime> times = new HashSet<>();
//        times.add(
//                LectureTime.builder()
//                    .lectureDay("MON")
//                    .startTime(1)
//                    .endTime(2)
//                    .room("D330")
//                    .build()
//        );
//
//        Lecture l = Lecture.builder()
//                    .courseID(2)
//                    .lectureCode("SE1234")
//                    .lecturerID("P1000")
//                    .limit(3)
//                    .lectureTimes(times)
//                    .build();
//        lectureRepo.insert(l);
//        System.out.println("l = " + l);
//        for(Lecture l : lectureRepo.findAll()){
//            System.out.println("l = " + l);
//        }

        AdminRepository adminRepo = new RDBAdminRepository();
        LectureRepository lectureRepo = new RDBLectureRepository();
        CourseRepository courseRepo = new RDBCourseRepository(MyBatisConnectionFactory.getSqlSessionFactory());
        RegisteringRepository regRepo = new RDBRegisteringRepository();
        RegPeriodRepository periodRepo = new RDBRegPeriodRepository();
        StudentRepository stdRepo = new RDBStudentRepository();
        ProfessorRepository profRepo = new RDBProfessorRepository();
        AccountRepository accRepo = new RDBAccountRepository();
        StudentAppService stdService = new StudentAppService(
                stdRepo, accRepo
        );

        ProfessorAppService profService = new ProfessorAppService(
                profRepo, accRepo
        );

        AdminAppService adminService = new AdminAppService(
                adminRepo, accRepo
        );

        CourseAppService courseService = new CourseAppService(
                courseRepo
        );

//        StudentRetrieveAppService s = new StudentRetrieveAppService(stdRepo);
//        ProfessorRetrieveAppService p = new ProfessorRetrieveAppService(profRepo);
//        RegisterAppService r = new RegisterAppService(lectureRepo, stdRepo, courseRepo, regRepo, periodRepo);

        //회원 Create
        //create Admin
//        AdminDTO adminDTO = AdminDTO.builder()
//                .name("leehana4")
//                .birthDate("001020")
//                .department("SE")
//                .adminCode("F1234")
//                .build();
//        adminService.create(adminDTO);
        //end of admin create


        //create professor
//        ProfessorDTO profDTO = ProfessorDTO.builder()
//                .name("kimsungryul")
//                .birthDate("991020")
//                .department("SE")
//                .professorCode("P1000")
//                .telePhone("9000")
//                .build();
//
//        profService.create(profDTO);
//
//        ProfessorDTO profDTO2 = ProfessorDTO.builder()
//                .id(85)
//                .name("kimsunmyeong")
//                .birthDate("601010")
//                .department("AE")
//                .professorCode("P2000")
//                .telePhone("7000")
//                .build();
//
//        profService.create(profDTO2);
        //end of professor create

        //create student
//        StudentDTO stdDTO = StudentDTO.builder()
//                .name("kimjinwoo")
//                .birthDate("990329")
//                .department("SE")
//                .studentCode("20180303")
//                .year(2)
//                .build();
//
//        stdService.create(stdDTO);

//        StudentDTO l = StudentDTO.builder().id(77).build();
//        stdService.delete(l);
        
//        StudentDTO std = stdService.retrieveByID(78);
//        System.out.println("std = " + std);
        
//        List<StudentDTO> list = stdService.retrieveAll();
//        for(StudentDTO std : list){
//            System.out.println("std = " + std);
//        }

//        List<StudentDTO> list = stdService.retrieveByOption(new StudentYearOption("3"));
//        for(StudentDTO std : list){
//            System.out.println("std = " + std);
//        }

//
//        StudentDTO stdDTO2 = StudentDTO.builder()
//                .name("parkhyeongjun")
//                .birthDate("990329")
//                .department("SE")
//                .studentCode("20181111")
//                .year(2)
//                .build();
//
//        stdService.create(stdDTO2);
//
//        StudentDTO stdDTO3 = StudentDTO.builder()
//                .name("leeeunbean")
//                .birthDate("000312")
//                .department("SE")
//                .studentCode("20182222")
//                .year(2)
//                .build();
//
//        stdService.create(stdDTO3);
//
//        StudentDTO stdDTO4 = StudentDTO.builder()
//                .name("yeongeomji")
//                .birthDate("990429")
//                .department("SE")
//                .studentCode("20183333")
//                .year(3)
//                .build();
//
//        stdService.create(stdDTO4);
        //end of student create

        //retrieve all professor
//        for(Professor prof : p.findAll()){
//            System.out.println("prof = " + prof);
//        }
        //end of retrieve professor

        //retrieve all student
//        for(Student std : s.findAll()){
//            System.out.println("std = " + std);
//        }
        //end of retrieve professor

        //update professor
        //TODO : 아이디값 지정 필요
//        Professor prof = p.findByID(53);
//        System.out.println("prof = " + prof);
//        prof.setTelePhone("8001");
//        m.updateProfessor(prof);
//        Professor prof2 = p.findByID(53);
//        System.out.println("prof = " + prof2);
        //end of update professor

        //update student
        //TODO : 아이디값 지정 필요
//        Student std = s.findByID(44);
//        System.out.println("std = " + std);
//        std.setName("kimJinWoo");
//        m.updateStudent(std);
//        Student std2 = s.findByID(44);
//        System.out.println("std = " + std2);
        //end of update student

        //교과목 생성 테스트
        //2학년 2학기 과목
//        CourseDTO c1 = CourseDTO.builder()
//                .id(10)
//                .courseName("C++프로그래밍")
//                .courseCode("CS0077")
//                .department("SE")
//                .targetYear(2)
//                .credit(3)
//                .build();
//        courseService.create(c1);
//
//        Course c2 = Course.builder()
//                .courseName("운영체제")
//                .courseCode("CS0017")
//                .department("SE")
//                .targetYear(2)
//                .credit(3)
//                .build();
//
//        Course c3 = Course.builder()
//                .courseName("컴퓨터네트워크")
//                .courseCode("CS0016")
//                .department("SE")
//                .targetYear(2)
//                .credit(4)
//                .build();
//
//        Course c4 = Course.builder()
//                .courseName("융합프로젝트")
//                .courseCode("CS0069")
//                .department("SE")
//                .targetYear(2)
//                .credit(2)
//                .build();
//
//        Course c5 = Course.builder()
//                .courseName("오픈소스소프트웨어")
//                .courseCode("CS0080")
//                .department("SE")
//                .targetYear(2)
//                .credit(2)
//                .build();
//        //1학년 2학기
//        Course c6 = Course.builder()
//                .courseName("자바프로그래밍")
//                .courseCode("CS0010")
//                .department("SE")
//                .targetYear(1)
//                .credit(3)
//                .build();
//        //3학년 2학기
//        Course c7 = Course.builder()
//                .courseName("디자인패턴")
//                .courseCode("CS0027")
//                .department("SE")
//                .targetYear(3)
//                .credit(3)
//                .build();
//        //4학년 2학기
//        Course c8 = Course.builder()
//                .courseName("컴파일러")
//                .courseCode("CS0035")
//                .department("SE")
//                .targetYear(4)
//                .credit(3)
//                .build();
//
//        courseRepo.insert(c1);
//        courseRepo.insert(c2);
//        courseRepo.insert(c3);
//        courseRepo.insert(c4);
//        courseRepo.insert(c5);
//        courseRepo.insert(c6);
//        courseRepo.insert(c7);
//        courseRepo.insert(c8);
        //end of course create
        
        //find all course
//        CourseDTO c = courseService.RetrieveByID(2);
//        System.out.println("c = " + c);
        //end of find all course

        //update course name
        //TODO : 아이디값 어떻게?
//        Course updatingCourse = courseRepo.findByID(5);
//        updatingCourse.setCourseName("리눅스의 이해");
//        courseRepo.save(updatingCourse);
        //end of update course name


        //수강신청 기간설정
//        Student currentStd = stdRepo.findByID(61);
//        //TODO : 아이디값 어떻게?
//        for(Lecture lecture : lectureRepo.findAll()){
//            Course curCourse = courseRepo.findByID(lecture.getCourseID());
//            System.out.print("lecture = " + lecture);
//            System.out.print(" targetYear = "+curCourse.getYear());
//
//            if(r.isValidPeriodAbout(currentStd, curCourse)){
//                System.out.println(" : 수강신청 가능");
//            }else{
//                System.out.println(" : 수강신청 불가");
//            }
//        }
//        System.out.println();
//
//        RegisteringPeriodDTO rPeriod = RegisteringPeriodDTO.builder()
//                .period(
//                        PeriodDTO.builder()
//                                .beginTime(LocalDateTime.of(2021,11,10,00,00))
//                                .endTime(LocalDateTime.of(2021,12,10,00,00))
//                                .build())
//                .allowedYear(2)
//                .build();
//
//        r.addRegisteringPeriod(rPeriod);
//
//        for(Lecture lecture : lectureRepo.findAll()){
//            Course curCourse = courseRepo.findByID(lecture.getCourseID());
//            System.out.print("lecture = " + lecture);
//            System.out.print(" targetYear = "+curCourse.getYear());
//
//            if(r.isValidPeriodAbout(currentStd, curCourse)){
//                System.out.println(" : 수강신청 가능");
//            }else{
//                System.out.println(" : 수강신청 불가");
//            }
//        }

        //수강신청 기간설정 끝

        //수강신청
        //TODO: 아이디값
//        r.register(26, 61);
//        List<Registering> list = regRepo.findByOption(new StudentCodeOption("20180303"));
//        r.cancel(1, 26, 61);

        //수강신청 끝

//        r.register(15, 2);
//        Student std = stdRepo.findByID(2);
//        System.out.println("std = " + std);

//        for(Registering reg : list){
//            System.out.println("reg = " + reg);

//        }
//
//        r.cancel(6, 15, 2);

    }
}
