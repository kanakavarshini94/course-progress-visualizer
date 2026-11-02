# Course Progress Visualizer

A beginner-friendly Java Full Stack project built with Java 21, Spring Boot, Spring MVC, Spring Data JPA, Thymeleaf, HTML, CSS and H2 database.

## Features
- Dashboard with total courses, completed topics, pending topics and overall progress.
- Create and delete courses.
- Add topics under each course.
- Mark topics as completed or pending.
- Visual progress bars and completion percentages.
- H2 file database so saved data remains after restarting the app.

## Requirements
- JDK 21 or later
- Maven 3.6+ (or use the Maven wrapper if you add one)

## Run in VS Code
1. Extract `course-progress-visualizer.zip`.
2. Open the extracted `course-progress-visualizer` folder in VS Code.
3. Confirm Java 21 is installed: open the VS Code terminal and run `java -version`.
4. Run the application from the project folder:
   - Windows: `mvnw.cmd spring-boot:run` (if Maven Wrapper is available), or `mvn spring-boot:run`
   - macOS/Linux: `./mvnw spring-boot:run` (if Maven Wrapper is available), or `mvn spring-boot:run`
5. Open `http://localhost:8080` in your browser.

If Maven is not installed, install Apache Maven or use the Spring Boot Dashboard extension in VS Code after importing the project as a Maven project.

## H2 database console
- URL: `http://localhost:8080/h2-console`
- JDBC URL: `jdbc:h2:file:./data/courseprogress`
- User Name: `sa`
- Password: leave empty

The `data` folder is created when the application first runs. Do not delete it if you want to keep your saved courses.

## Project structure
```
src/main/java/com/varshini/courseprogress/
  controller/   -> Web routes and request handling
  model/        -> Course and Topic entities
  repository/   -> Spring Data JPA repositories
  service/      -> Business logic
src/main/resources/
  templates/    -> Thymeleaf pages
  static/css/   -> Stylesheet
```

## Suggested demo
1. Add a course named `Java Full Stack`.
2. Open it and add topics such as Core Java, OOP, Collections, JDBC, Spring Boot, REST API and SQL.
3. Mark the topics you have completed.
4. Return to the dashboard and observe the updated progress percentage.

## Notes
- This is a learning/portfolio project, not a production-ready application.
- No login or multi-user account system is included in this starter version.
- The first run requires internet access for Maven to download project dependencies. Google Fonts are optional; the layout uses fallback fonts if they cannot load.
