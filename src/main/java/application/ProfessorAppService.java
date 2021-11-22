package application;

import domain.model.Account;
import domain.model.Professor;
import domain.repository.AccountRepository;
import domain.repository.ProfessorRepository;
import infra.dto.ModelMapper;
import infra.dto.ProfessorDTO;

import java.util.ArrayList;
import java.util.List;

public class ProfessorAppService {
    private ProfessorRepository profRepo;
    private AccountRepository accRepo;

    public ProfessorAppService(ProfessorRepository profRepo, AccountRepository accRepo) {
        this.profRepo = profRepo;
        this.accRepo = accRepo;
    }

    public void create(ProfessorDTO profDTO) {
        Professor prof = Professor.builder()
                .name(profDTO.getName())
                .birthDate(profDTO.getBirthDate())
                .department(profDTO.getDepartment())
                .professorCode(profDTO.getProfessorCode())
                .telePhone(profDTO.getTelePhone())
                .build();

        long profID = profRepo.save(prof);

        Account acc = Account.builder()
                .id(profDTO.getProfessorCode())
                .password(profDTO.getBirthDate())
                .memberID(profID)
                .build();

        accRepo.save(acc);
    }

    //TODO : partial update 불가
    //TODO : 바뀌면 안되는 값에 대한 처리?
    public void update(ProfessorDTO profDTO) {
        Professor prof = Professor.builder()
                .id(profDTO.getId())
                .name(profDTO.getName())
                .birthDate(profDTO.getBirthDate())
                .department(profDTO.getDepartment())
                .professorCode(profDTO.getProfessorCode())
                .telePhone(profDTO.getTelePhone())
                .build();

        profRepo.save(prof);
    }

    //TODO : id가 없을때 예외처리
    public void delete(ProfessorDTO profDTO) {
        Professor prof = Professor.builder().id(profDTO.getId()).build();

        profRepo.remove(prof);
    }

    public ProfessorDTO retrieveByID(long id) {
        return ModelMapper.professorToDTO(profRepo.findByID(id));
    }

    //TODO : 추후구현
//    public List<ProfessorDTO> retrieveByOption() {
//        return stdListToDTOList(stdRepo.findByOption(options));
//    }


    //TODO : 비효율적
    public List<ProfessorDTO> retrieveAll() {
        return profListToDTOList(profRepo.findAll());
    }

    private List<ProfessorDTO> profListToDTOList(List<Professor> profList) {
        List<ProfessorDTO> list = new ArrayList<>();

        for (Professor prof : profList) {
            list.add(
                    ModelMapper.professorToDTO(prof)
            );
        }

        return list;
    }

}