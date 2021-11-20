package infra.database;

import domain.generic.LectureTime;
import domain.model.Lecture;
import domain.model.LecturePlanner;
import domain.model.Registering;
import domain.repository.LectureRepository;
import infra.MyBatisConnectionFactory;
import infra.dto.LectureDTO;
import infra.dto.LectureTimeDTO;
import infra.dto.ModelMapper;
import infra.mapper.LectureMapper;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import java.util.*;

public class RDBLectureRepository implements LectureRepository {
    private SqlSessionFactory sqlSessionFactory = MyBatisConnectionFactory.getSqlSessionFactory();

    public RDBLectureRepository() {}

    @Override
    public Lecture findByID(long id) {
        Lecture lecture = null;
        SqlSession session = sqlSessionFactory.openSession();
        LectureMapper mapper = session.getMapper(LectureMapper.class);
        try {

            Set<Registering> registerings = getRegSetFrom(mapper.selectRegisterings(id));

            Set<LectureTime> lectureTimes = getLtimeFrom(mapper.selectLectureTimes(id));

            //TODO : 강의계획서 어떤거 들어갈지?
            //TODO : 강의계획서 내용 넣는거 변경 필요할듯?
            //TODO : 변경시 findAll도 함께 변경 필요
            Map<String, Object> plannerInfo = mapper.selectPlanner(id);
            LecturePlanner planner = new LecturePlanner();
            planner.writeItem("goal", plannerInfo.get("lecture_goal").toString());

            lecture = mapToLecture(mapper.findByID(id), lectureTimes, registerings, planner);

            session.commit();

        } catch (Exception e) {
            e.printStackTrace();
            session.rollback();
        } finally {
            session.close();
        }

        return lecture;
    }

    @Override
    public void save(Lecture lecture) {
        SqlSession session = sqlSessionFactory.openSession();
        LectureMapper mapper = session.getMapper(LectureMapper.class);
        try {
            mapper.updateLecture();
            session.commit();

        } catch (Exception e) {
            e.printStackTrace();
            session.rollback();
        } finally {
            session.close();
        }
    }

    public void insert(Lecture lecture) {
        SqlSession session = sqlSessionFactory.openSession();
        LectureMapper mapper = session.getMapper(LectureMapper.class);
        LectureDTO lectureDTO = ModelMapper.lectureToDTO(lecture);
        Set<LectureTimeDTO> lectureTimes = lectureDTO.getLectureTimes();
        try {
            Map<String, String> items = new HashMap<>(lectureDTO.getPlanner().getItems());
            items.put("id","");
            mapper.insertLecturePlanner(items);
            lectureDTO.setPlannerID(Integer.parseInt(items.get("id")));

            mapper.insert(lectureDTO);

            for(LectureTimeDTO lectureTimeDTO : lectureTimes){
                mapper.insertLectureTime(
                        lectureDTO.getId(), lectureTimeDTO.getLectureDay(),
                        lectureTimeDTO.getRoom(), lectureTimeDTO.getStartTime(),
                        lectureTimeDTO.getEndTime()
                );
            }

            session.commit();

        } catch (Exception e) {
            e.printStackTrace();
            session.rollback();
        } finally {
            session.close();
        }
    }

    @Override
    public void remove(long lectureID) {

    }

    @Override
    public List<Lecture> findAll() {
        List<Lecture> list = new ArrayList<>();
        SqlSession session = sqlSessionFactory.openSession();
        LectureMapper mapper = session.getMapper(LectureMapper.class);
        try {
            // 개설교과목 목록 조회
            List<Map<String, Object>> lectureList = mapper.selectLectureList();

            //각 개설교과목에 해당하는 강의시간 객채만들어서 할당
            for (Map map : lectureList) {
                long id = (long)map.get("lecture_PK");

                // registerings 에 registering 추가
                Set<Registering> registerings = getRegSetFrom(
                        mapper.selectRegisterings((long) map.get("lecture_PK"))
                );

                //lectureTimes 에 lectureTime 추가
                Set<LectureTime> lectureTimes = getLtimeFrom(
                        mapper.selectLectureTimes((long) map.get("lecture_PK"))
                );

                Map<String, Object> plannerInfo = mapper.selectPlanner(id);
                LecturePlanner planner = new LecturePlanner();
                planner.writeItem("goal", plannerInfo.get("lecture_goal").toString());

                // Lecture 생성해서 리턴할 List에 추가
                list.add(
                        mapToLecture(map, lectureTimes, registerings, planner)
                );
            }
            session.commit();

        } catch (Exception e) {
            e.printStackTrace();
            session.rollback();
        } finally {
            session.close();
        }

        return list;
    }

    private Set<Registering> getRegSetFrom(List<Map<String, Object>> regs){
        Set<Registering> registerings = new HashSet<>();
        for (Map<String, Object> reg : regs) {
            registerings.add(
                    Registering.builder()
                            .id((long)reg.get("registering_PK"))
                            .studentCode(reg.get("student_code").toString())
                            .registeringTime(reg.get("register_date").toString())
                            .lectureID((long)reg.get("lecture_PK"))
                            .build()
            );
        }

        return registerings;
    }

    private Set<LectureTime> getLtimeFrom(List<Map<String, Object>> times){
        Set<LectureTime> lectureTimes = new HashSet<>();
        for (Map time : times) {
            lectureTimes.add(
                    LectureTime.builder()
                            .lectureDay(time.get("day_of_week").toString())
                            .startTime((int) time.get("start_period"))
                            .endTime((int) time.get("end_period"))
                            .room(time.get("lecture_room").toString())
                            .build()
            );
        }
        return lectureTimes;
    }

    private Lecture mapToLecture(Map<String, Object> lectureMap, Set<LectureTime> times,
                                    Set<Registering> regs, LecturePlanner planner){
        return Lecture.builder()
                .id((long)lectureMap.get("lecture_PK"))
                .courseID((long)lectureMap.get("course_PK"))
                .lecturerID(lectureMap.get("lecture_code").toString())
                .lectureCode(lectureMap.get("professor_code").toString())
                .limit((int)lectureMap.get("capacity"))
                .registerings(regs)
                .lectureTimes(times)
                .planner(planner)
                .build();

    }
}
