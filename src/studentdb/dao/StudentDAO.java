package studentdb.dao;

import studentdb.model.Student;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StudentDAO {

    // Add student to DB
    public boolean addStudent(Student s){
    String sql = "INSERT INTO students(first_name, last_name, age, email) VALUES(?,?,?,?)";
    try(Connection con = DBConnection.getConnection();
        PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

        if(con == null){
            System.out.println("Connection is NULL!");
            return false;
        } else {
            System.out.println("Connection OK!");
        }

        System.out.println("Attempting to insert: " + s.getFirstName() + ", " + s.getLastName() + ", " + s.getAge() + ", " + s.getEmail());

        ps.setString(1, s.getFirstName());
        ps.setString(2, s.getLastName());
        ps.setInt(3, s.getAge());
        ps.setString(4, s.getEmail());

        int affected = ps.executeUpdate();
        System.out.println("Rows affected: " + affected);

        if(affected == 0) return false;

        try(ResultSet rs = ps.getGeneratedKeys()){
            if(rs.next()) s.setId(rs.getInt(1));
        }
        return true;

    } catch(Exception e){
        System.out.println("Exception during insert!");
        e.printStackTrace();
        return false;
    }
}


    // Get all students
    public List<Student> getAllStudents(){
        List<Student> list = new ArrayList<>();
        String sql = "SELECT * FROM students ORDER BY id";
        try(Connection con = DBConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()) {

            if(con == null){
                System.out.println("DB Connection is null!");
                return list;
            }

            while(rs.next()){
                list.add(new Student(
                        rs.getInt("id"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getInt("age"),
                        rs.getString("email")
                ));
            }
        } catch(SQLException e){
            System.out.println("SQL Exception while fetching students!");
            e.printStackTrace();
        } catch(Exception e){
            e.printStackTrace();
        }
        return list;
    }

    // Get student by ID
    public Student getStudentById(int id){
        String sql = "SELECT * FROM students WHERE id=?";
        try(Connection con = DBConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)) {

            if(con == null){
                System.out.println("DB Connection is null!");
                return null;
            }

            ps.setInt(1, id);
            try(ResultSet rs = ps.executeQuery()){
                if(rs.next()){
                    return new Student(
                            rs.getInt("id"),
                            rs.getString("first_name"),
                            rs.getString("last_name"),
                            rs.getInt("age"),
                            rs.getString("email")
                    );
                }
            }
        } catch(SQLException e){
            System.out.println("SQL Exception while searching student!");
            e.printStackTrace();
        } catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
