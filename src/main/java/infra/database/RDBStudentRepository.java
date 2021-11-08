package infra.database;

import domain.generic.LectureTime;
import domain.model.Student;
import domain.repository.StudentRepository;
import infra.PooledDataSource;
import infra.dto.ModelMapper;
import infra.dto.StudentDTO;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class RDBStudentRepository implements StudentRepository {
    private final DataSource ds = PooledDataSource.getDataSource();

    @Override
    public Student findByID(long id){
        StringBuilder query = new StringBuilder(
                "SELECT * FROM students_tb AS s " +
                "JOIN members_tb AS m " +
                "ON s.member_SQ = m.member_SQ "+
                "WHERE m.member_SQ = ? "
        );
        Connection conn = null;
        try{
            conn = ds.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(new String(query));
            pstmt.setLong(1, id);
            ResultSet res = pstmt.executeQuery();
            return getStdFrom(res).get(0);
        }catch(SQLException sqlException){
            sqlException.printStackTrace();
            throw new IllegalArgumentException("잘못된 id값입니다.");
        }
    }

    private ResultSet findLectureInfo(String stdCode){
        StringBuilder query = new StringBuilder(
                "SELECT * FROM enrollments_tb AS e " +
                        "JOIN lecture_times_tb AS t " +
                        "ON e.lecture_SQ = t.lecture_SQ "+
                        "WHERE e.student_code = ? "
        );

        Connection conn = null;
        try{
            conn = ds.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(new String(query));
            pstmt.setString(1, stdCode);
            return pstmt.executeQuery();
        }catch(SQLException sqlException){
            sqlException.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Student> findAll() {
        StringBuilder query = new StringBuilder(
                "SELECT * FROM students_tb AS s " +
                "JOIN members_tb AS m " +
                "ON s.member_SQ = m.member_SQ");
        Connection conn = null;
        try{
            conn = ds.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(new String(query));
            ResultSet res = pstmt.executeQuery();
            return getStdFrom(res);

        }catch(SQLException sqlException){
            sqlException.printStackTrace();
        }

        return null;
    }

    @Override
    public void save(Student student) {
        StudentDTO stdDTO = ModelMapper.studentToDTO(student);
        StringBuilder memberQuery = new StringBuilder(
                "INSERT INTO members_tb (member_SQ, name, birthday, department) " +
                        "VALUES(?, ?, ?, ?) " +
                        "ON DUPLICATE KEY UPDATE " +
                        "name=?, " +
                        "birthday=?, " +
                        "department=?;"
        );
        StringBuilder stdQuery = new StringBuilder(
                "INSERT INTO students_tb (member_SQ, student_code, year, credit, max_credit) " +
                        "VALUES(?, ?, ?, ?, ?) " +
                        "ON DUPLICATE KEY UPDATE " +
                        "student_code=?, " +
                        "year=?, " +
                        "credit=?, " +
                        "max_credit=?;"
        );

        Connection conn = null;
        try{
            conn = ds.getConnection();
            conn.setAutoCommit(false);
            PreparedStatement memberStmt = conn.prepareStatement(new String(memberQuery));
            PreparedStatement stdStmt = conn.prepareStatement(new String(stdQuery));

            memberStmt.setLong(1, stdDTO.getId());
            memberStmt.setString(2, stdDTO.getName());
            memberStmt.setString(3, stdDTO.getDepartment());
            memberStmt.setString(4, String.valueOf(stdDTO.getBirthDate()));
            memberStmt.setString(5, stdDTO.getName());
            memberStmt.setString(6, stdDTO.getDepartment());
            memberStmt.setString(7, String.valueOf(stdDTO.getBirthDate()));

            stdStmt.setLong(1, stdDTO.getId());
            stdStmt.setString(2, stdDTO.getStudentCode());
            stdStmt.setInt(3, stdDTO.getYear());
            stdStmt.setInt(4, stdDTO.getMaxCredit());
            stdStmt.setInt(5, stdDTO.getCredit());
            stdStmt.setString(6, stdDTO.getStudentCode());
            stdStmt.setInt(7, stdDTO.getYear());
            stdStmt.setInt(8, stdDTO.getMaxCredit());
            stdStmt.setInt(9, stdDTO.getCredit());

            memberStmt.execute();
            stdStmt.execute();
            conn.commit();
        }catch(SQLException sqlException){
            sqlException.printStackTrace();
            try{
                conn.rollback();
            }catch (SQLException e){
                e.printStackTrace();
            }
        }
    }

    private List<Student> getStdFrom(ResultSet resSet) throws SQLException {
        List<Student> stdList = new ArrayList<>();
        long resID = 0;
        int year = 0;
        String name;
        String birthDate;
        String department;
        String studentCode;
        Set<LectureTime> timeTable = new HashSet<>();
        Set<Long> registeredLectureIds = new HashSet<>();

        while(resSet.next()) {
            resID = resSet.getLong("member_SQ");
            year = resSet.getInt("year");
            name = resSet.getString("name");
            department = resSet.getString("department");
            birthDate = resSet.getString("birthDay");
            studentCode = resSet.getString("student_code");

            ResultSet lectureInfo = findLectureInfo(studentCode);
            while(lectureInfo.next()){
                registeredLectureIds.add(
                        lectureInfo.getLong("lecture_SQ")
                );
                timeTable.add(
                        LectureTime.builder()
                        .lectureDay(lectureInfo.getString("day_of_week"))
                        .room(lectureInfo.getString("lecture_room"))
                        .startTime(lectureInfo.getInt("start_period"))
                        .endTime(lectureInfo.getInt("end_period"))
                        .build()
                );
            }

            stdList.add(
                    Student.builder()
                            .id(resID)
                            .year(year)
                            .name(name)
                            .birthDate(birthDate)
                            .department(department)
                            .studentCode(studentCode)
                            .timeTable(timeTable)
                            .registeredLectureIDs(new ArrayList<>(registeredLectureIds))
                            .build()
            );
        }

        return stdList;
    }
}
