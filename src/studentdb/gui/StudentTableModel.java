package studentdb.gui;

import studentdb.model.Student;
import javax.swing.table.AbstractTableModel;
import java.util.*;

public class StudentTableModel extends AbstractTableModel {

    private final String[] columns = {"ID", "First Name", "Last Name", "Age", "Email"};
    private List<Student> students = new ArrayList<>();

    public void setStudents(List<Student> list){
        this.students = list;
        fireTableDataChanged();
    }

    @Override
    public int getRowCount() {
        return students.size();
    }

    @Override
    public int getColumnCount() {
        return columns.length;
    }

    @Override
    public String getColumnName(int col){
        return columns[col];
    }

    @Override
    public Object getValueAt(int row, int col){
        Student s = students.get(row);
        switch(col){
            case 0: return s.getId();
            case 1: return s.getFirstName();
            case 2: return s.getLastName();
            case 3: return s.getAge();
            case 4: return s.getEmail();
            default: return "";
        }
    }
}
