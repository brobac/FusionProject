package domain.model;

import domain.generic.LectureTime;

import java.util.*;

public class Student{
    private StudentID id;
    private int maxCredit = 21;
    private int credit;
    private List<LectureID> registeredLectureIDs;
    private Set<LectureTime> timeTable;
    private Year year;

    public enum Year {
        FRESHMAN, SOPHOMORE, JUNIOR, SENIOR
    }

    public Student(StudentID stdID, Year year){
        this.id = stdID;
        this.year = year;
        registeredLectureIDs = new ArrayList<>();
        timeTable = new HashSet<>();
    }

    public StudentID getID(){return id;}
    public List<LectureID> getRegisteredLectureIDs(){return registeredLectureIDs;}
    public Year getYear(){return year;}

    public void register(LectureID lectureID,
                            Set<LectureTime> lectureTimes, int lectureCredit){
        credit += lectureCredit;
        registeredLectureIDs.add(lectureID);
        timeTable.addAll(lectureTimes);
    }

    public void cancel(LectureID lectureID,
                       Set<LectureTime> lectureTimes, int lectureCredit){
        credit -= lectureCredit;
        registeredLectureIDs.remove(lectureID);
        timeTable.removeAll(lectureTimes);
    }

    public boolean isDuplicatedTime(Set<LectureTime> lectureTimes){
        for(LectureTime myTime : timeTable){
            for(LectureTime lectureTime : lectureTimes){
                if(myTime.isOverlappedTime(lectureTime)){
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isValidCredit(int lectureCredit) {
        if(credit+lectureCredit>=maxCredit){
            return false;
        }

        return true;
    }

    public boolean hasLecture(LectureID lectureID) {
        for(LectureID myLectureID : registeredLectureIDs){
            if(myLectureID.equals(lectureID)){
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Student student = (Student) o;
        return Objects.equals(id, student.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}
