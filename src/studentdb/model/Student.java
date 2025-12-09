package studentdb.model;

public class Student {
    private int id;
    private String firstName;
    private String lastName;
    private int age;
    private String email;

    // Default constructor
    public Student() {}

    // Constructor with all fields
    public Student(int id, String firstName, String lastName, int age, String email){
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.email = email;
    }

    // Constructor without ID (for new student before insert)
    public Student(String firstName, String lastName, int age, String email){
        this(0, firstName, lastName, age, email);
    }

    // Getters & Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
}
