package application;

import domain.model.Account;
import domain.model.Student;
import domain.repository.AccountRepository;
import domain.repository.StudentRepository;
import infra.database.option.student.StudentOption;
import infra.dto.ModelMapper;
import infra.dto.StudentDTO;

import java.util.ArrayList;
import java.util.List;

public class StudentAppService {
    private StudentRepository stdRepo;
    private AccountRepository accRepo;

    public StudentAppService(StudentRepository stdRepo, AccountRepository accRepo) {
        this.stdRepo = stdRepo;
        this.accRepo = accRepo;
    }

    public void create(StudentDTO stdDTO){
        Student std = Student.builder()
                    .name(stdDTO.getName())
                    .birthDate(stdDTO.getBirthDate())
                    .department(stdDTO.getDepartment())
                    .year(stdDTO.getYear())
                    .studentCode(stdDTO.getStudentCode())
                    .maxCredit(stdDTO.getMaxCredit())
                    .build();

        long stdID = stdRepo.save(std);

        Account acc = Account.builder()
                        .id(std.getStudentCode())
                        .password(stdDTO.getBirthDate())
                        .memberID(stdID)
                        .build();

        accRepo.save(acc);
    }

    //TODO : partial update 불가
    //TODO : 바뀌면 안되는 값에 대한 처리?
    public void update(StudentDTO stdDTO){
        Student std = Student.builder()
                .id(stdDTO.getId())
                .name(stdDTO.getName())
                .birthDate(stdDTO.getBirthDate())
                .department(stdDTO.getDepartment())
                .year(stdDTO.getYear())
                .studentCode(stdDTO.getStudentCode())
                .maxCredit(stdDTO.getMaxCredit())
                .build();

        stdRepo.save(std);
    }

    //TODO : id가 없을때 예외처리
    public void delete(StudentDTO stdDTO){
        Student std = Student.builder().id(stdDTO.getId()).build();

        stdRepo.remove(std);
    }

    public StudentDTO retrieveByID(long id){
        return ModelMapper.studentToDTO( stdRepo.findByID(id));
    }

    public List<StudentDTO> retrieveByOption(StudentOption... options){
        return stdListToDTOList(stdRepo.findByOption(options));
    }

    //TODO : 비효율적
    public List<StudentDTO> retrieveAll(){
        return stdListToDTOList(stdRepo.findAll());
    }

    private List<StudentDTO> stdListToDTOList(List<Student> stdList){
        List<StudentDTO> list = new ArrayList<>();

        for(Student std : stdList){
            list.add(
                    ModelMapper.studentToDTO(std)
            );
        }

        return list;
    }
}
