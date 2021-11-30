package application;

import domain.model.Account;
import domain.model.Registering;
import domain.model.Student;
import domain.repository.AccountRepository;
import domain.repository.RegisteringRepository;
import domain.repository.StudentRepository;
import infra.database.option.registering.LectureIDOption;
import infra.database.option.student.StudentCodeOption;
import infra.database.option.student.StudentOption;
import infra.dto.LectureDTO;
import infra.dto.ModelMapper;
import infra.dto.StudentDTO;

import java.util.List;

public class StudentAppService {
    private StudentRepository stdRepo;
    private AccountRepository accRepo;
    private RegisteringRepository regRepo;

    public StudentAppService(
            StudentRepository stdRepo, AccountRepository accRepo,
            RegisteringRepository regRepo) {
        this.stdRepo = stdRepo;
        this.accRepo = accRepo;
        this.regRepo = regRepo;
    }

    public void create(StudentDTO stdDTO) {
        //TODO : 생성시의 Validation 예외필요
        Student std = Student.builder()
                .name(stdDTO.getName())
                .birthDate(stdDTO.getBirthDate())
                .department(stdDTO.getDepartment())
                .year(stdDTO.getYear())
                .studentCode(stdDTO.getStudentCode())
                .credit(stdDTO.getCredit())
                .maxCredit(stdDTO.getMaxCredit())
                .build();

        long stdID = stdRepo.save(std);

        Account acc = Account.builder()
                .id(std.getStudentCode())
                .password(stdDTO.getBirthDate())
                .memberID(stdID)
                .position("STUD")
                .build();

        accRepo.save(acc);
    }

    //TODO : partial update 불가
    //TODO : 바뀌면 안되는 값에 대한 처리?
    public void update(StudentDTO stdDTO) throws IllegalArgumentException {
        Student std = stdRepo.findByID(stdDTO.getId());
        std.setName(stdDTO.getName());
        //TODO : 업데이트 가능한 항목들 추가필요

        stdRepo.save(std);
    }

    //TODO : id가 없을때 예외처리
    public void delete(StudentDTO stdDTO) {
        Student std = Student.builder().id(stdDTO.getId()).build();

        stdRepo.remove(std);
    }

    public StudentDTO retrieveByID(long id) {
        return ModelMapper.studentToDTO(stdRepo.findByID(id));
    }

    public StudentDTO[] retrieveByOption(StudentOption... options) {
        return stdListToDTOArr(stdRepo.findByOption(options));
    }

    //TODO : 비효율적
    public StudentDTO[] retrieveAll() {
        return stdListToDTOArr(stdRepo.findAll());
    }

    public StudentDTO[] retrieveRegisteringStd(LectureDTO lectureDTO) {
        List<Registering> regs = regRepo.findByOption(new LectureIDOption(lectureDTO.getId()));
        StudentDTO[] res = new StudentDTO[regs.size()];

        int i = 0;
        for (Registering reg : regs) {
            res[i++] = ModelMapper.studentToDTO(
                    stdRepo.findByOption(new StudentCodeOption(reg.getStudentCode())).get(0)
            );
        }

        return res;
    }

    private StudentDTO[] stdListToDTOArr(List<Student> stdList) {
        StudentDTO[] dtos = new StudentDTO[stdList.size()];

        for (int i = 0; i < dtos.length; i++) {
            dtos[i] = ModelMapper.studentToDTO(stdList.get(i));
        }

        return dtos;
    }
}
