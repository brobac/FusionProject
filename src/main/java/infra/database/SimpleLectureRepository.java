package infra.database;

import domain.model.Lecture;
import domain.repository.LectureRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SimpleLectureRepository implements LectureRepository {
    private Map<Long, Lecture> repo = new HashMap<>();

    @Override
    public Lecture findByID(long id) {
        return repo.get(id);
    }

    @Override
    public List<Lecture> findAll(){
        return new ArrayList<>(repo.values());
    }

    public void save(Lecture lecture){
        repo.put(lecture.getID(), lecture);
    }

    @Override
    public void remove(long lectureID) {
        repo.remove(lectureID);
    }

    @Override
    public void insert(Lecture lecture) {

    }
}
