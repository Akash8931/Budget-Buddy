# Budget Buddy
Budget Buddy is a desktop-based personal finance tool designed to empower users in managing their spending and budgets effectively. This application features a user-friendly interface for setting budget limits, generating visual spending reports, and ensuring secure user authentication. With Budget Buddy, our goal is to simplify financial management and support informed decision-making to improve financial health.

### Collaborators:
- Andrew McNeill
- Krishna Sruthi Velaga
- Akash Reddy Karri
- Ahmed Hamza
- Tobechukwu Ejike

## Installation Instructions

### Prerequisites
Before starting the installation, ensure the following software is installed on your system:
- **Java JDK**: [Download from Oracle](https://www.oracle.com/java/technologies/javase-jdk11-downloads.html) - Ensure you have Java JDK installed for compiling and running the application.
- **Maven**: [Download from Maven's official site](https://maven.apache.org/download.cgi) - Maven is required for managing project dependencies and builds.
- **MySQL**: [Download from MySQL's website](https://dev.mysql.com/downloads/mysql/) - MySQL is used for database operations within the application.

### MySQL Connector/J
To connect your Java application to a MySQL database, the MySQL Connector/J JDBC driver is essential. This driver allows your application to interact with the database, performing operations like reading and writing data.

- **Download the Connector/J JAR File**: Go to the [MySQL Connector/J Download Page](https://dev.mysql.com/downloads/connector/j/). On the download page, you will need to select the version of the Connector/J that is compatible with your MySQL server and Java version. This is crucial for ensuring compatibility and functionality.
- You may be prompted to log in or sign up for an Oracle account. However, you can usually bypass this by clicking on the link that says "No thanks, just start my download."
- **Add the JAR to your Project**: Once downloaded, save the downloaded JAR file into a `lib` folder in your project directory. Create the `lib` folder if it does not exist.
Configure your IDE to include the JAR in your build path. For example, in Eclipse:
     - Right-click on your project in the Project Explorer.
     - Go to `Properties` -> `Java Build Path`.
     - On the `Libraries` tab, click `Add JARs...` or `Add External JARs...` and select the Connector/J JAR file from your `lib` folder.
- **Verify Connection:** Implement a simple test to verify that your application can connect to the MySQL database using the Connector/J. Example test code might look like this:
   ```java
   import java.sql.Connection;
   import java.sql.DriverManager;

   public class TestDatabaseConnection {
       public static void main(String[] args) {
           try {
               String url = "jdbc:mysql://localhost:3306/yourDatabase?user=yourUsername&password=yourPassword";
               Connection conn = DriverManager.getConnection(url);
               if (conn != null) {
                   System.out.println("Successfully connected to the database.");
                   conn.close();
               }
           } catch (Exception e) {
               e.printStackTrace();
           }
       }
   }
### JFreeChart Integration
JFreeChart is a comprehensive open-source library designed for creating a wide array of charts in Java. It is ideal for our Budget Buddy application, enabling dynamic and visually appealing financial reports that enhance user interaction.

#### Adding JFreeChart to Your Project
Incorporate JFreeChart into your project by adding it as a dependency in the Maven `pom.xml` file. This approach ensures that JFreeChart and its dependencies are correctly managed and updated:


## Clone the Repository
To begin setting up the Budget Buddy project on your local machine, follow these steps:
```bash
git clone https://github.com/Akash8931/SWE-FlexibleDesign.git
cd SWE-FlexibleDesign/src
```
## Usage
In your IDE, right-click on the `BudgetBuddy.java` file and select "Run" to start the application.
