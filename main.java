import java.sql.*;

public class main {
    public static void main(String[] args) {

        String url = "jdbc:mysql://localhost:3306/student_db";
        String user = "root";
        String password = "root";

        String sql =
            "INSERT INTO students (id, name, age) VALUES (?, ?, ?)";

        try (Connection con =
                 DriverManager.getConnection(url, user, password)) {

            Object[][] students = {
                {101, "Rahul", 20},
                {102, "Priya", 19},
                {103, "Aman", 21},
                {104, "Neha", 20},
                {105, "Rohit", 22}
            };

            // Insert student records using batch
            try (PreparedStatement ps = con.prepareStatement(sql)) {

                for (Object[] student : students) {
                    ps.setInt(1, (Integer) student[0]);
                    ps.setString(2, (String) student[1]);
                    ps.setInt(3, (Integer) student[2]);

                    ps.addBatch();
                }

                int[] results = ps.executeBatch();
                int processed = 0;

                for (int result : results) {
                    if (result != Statement.EXECUTE_FAILED) {
                        processed++;
                    }
                }

                System.out.println(
                    "Number of processed records: " + processed
                );

            } catch (BatchUpdateException e) {
                System.out.println(
                    "Batch insertion error: " + e.getMessage()
                );
                System.out.println(
                    "Update counts reported: " +
                    e.getUpdateCounts().length
                );
            }

            // Print all student records
            String query = "SELECT * FROM students";

            try (
                Statement stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery(query)
            ) {
                System.out.println("\nStudent Records");
                System.out.println("--------------------------------");

                while (rs.next()) {
                    System.out.println(
                        "ID: " + rs.getInt("id") +
                        ", Name: " + rs.getString("name") +
                        ", Age: " + rs.getInt("age")
                    );
                }
            }

        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }
    }
}
