package domain.service;

import domain.model.LectureTime;
import domain.model.*;
import domain.repository.LectureRepository;

import java.util.Set;

public class LectureManageService {
    private LectureRepository lectureRepo;

    public LectureManageService(LectureRepository lectureRepo){
        this.lectureRepo = lectureRepo;
    }

    public Lecture create(Course course, String lectureCode, Professor professor,
            int limit, Set<LectureTime> lectureTimes){
        //TODO : 더 좋은방법?
        //TODO : 강의를 다가져오기에는 부하가 너무 크지않나?
        if(isExistingTimes(lectureTimes)){
            throw new IllegalArgumentException("유효하지않은 시간입니다.");
        }

        //TODO : 다른방식으로 생성?
        return Lecture.builder()
                .course(course)
                .lectureCode(lectureCode)
                .professor(professor)
                .limit(limit)
                .lectureTimes(lectureTimes)
                .build();
    }


    //TODO : 교수와 학생의 강의시간 의존 문제
    public Lecture update(Lecture lecture){
        if(isExistingTimes(lecture.getLectureTimes())){
            throw new IllegalArgumentException("유효하지않은 시간입니다.");
        }

        return lecture;
    }

    private boolean isExistingTimes(Set<LectureTime> times){
        for(Lecture existingLecture : lectureRepo.findAll()){
            for(LectureTime existingTime : existingLecture.getLectureTimes()){
                for(LectureTime newTime : times){
                    if(existingTime.isSameRoom(newTime) &&
                            existingTime.isOverlappedTime(newTime)){
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
