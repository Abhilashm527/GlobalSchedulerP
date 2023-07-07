package com.ivoyant.GlobalScheduler.service;

import com.ivoyant.GlobalScheduler.Model.Project;
import com.ivoyant.GlobalScheduler.Model.Schedule;
import com.ivoyant.GlobalScheduler.Model.ScheduleState;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.sql.*;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

@Component
public class ProjectImpl {
    private final JdbcTemplate jdbcTemplate;

    private static final String insertSQl = "INSERT INTO public.projects(\n" +
            "\tproject_id, project_title, description ,createdon)\n" +
            "\tVALUES (?, ?, ?, ?);";

    private static final String updateSQl = "UPDATE public.projects\n" +
            "\tSET  project_title=?, description=?\n" +
            "\tWHERE project_id=?;";

    private static  final  String getProjectSQL = "select * from public.projects where project_id= ?";

    private static  final  String getAllProjects = "select * from public.projects ";
    private static final String deleteProjectSQl = "delete from public.projects where project_id = ?";

    private static final String getAllSchedulesByProjectId = "select * from public.schedule where projectid =?";

    public ProjectImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private Connection jdbcTemplateConnection;

    @PostConstruct
    public void prepareQueries() throws SQLException {
        jdbcTemplateConnection = jdbcTemplate.getDataSource().getConnection();
    }


    public Project create(Project project) throws SQLException {
        PreparedStatement insertStmt = jdbcTemplateConnection.prepareStatement(insertSQl);
        java.util.Date currentDate = new java.util.Date();
        Timestamp currentTimestamp = new Timestamp(currentDate.getTime());
        String projectId = generateRandomAlphanumeric();
        insertStmt.setString(1, projectId);
        insertStmt.setString(2, project.getProject_title());
        insertStmt.setString(3, project.getDescription());
        insertStmt.setTimestamp(4,currentTimestamp);
        insertStmt.execute();
        return project;
    }

    public static String generateRandomAlphanumeric() {
        String ALPHANUMERIC = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        StringBuilder sb = new StringBuilder(6);

        for (int i = 0; i < 6; i++) {
            int index = random.nextInt(ALPHANUMERIC.length());
            sb.append(ALPHANUMERIC.charAt(index));
        }

        return sb.toString();
    }

    public Project updateProject(Project project) throws SQLException {
        PreparedStatement updateStmt = jdbcTemplateConnection.prepareStatement(updateSQl);
        PreparedStatement getProjectByid = jdbcTemplateConnection.prepareStatement(getProjectSQL);
        getProjectByid.setString(1, project.getProject_id());
        ResultSet resultSet = getProjectByid.executeQuery();
        if (resultSet.next()) {
            updateStmt.setString(1, project.getProject_title());
            updateStmt.setString(2, project.getDescription());
            updateStmt.setString(3,project.getProject_id());
            updateStmt.executeUpdate();
        }

        // Close the statements and result set
        updateStmt.close();
        getProjectByid.close();
        resultSet.close();
        return project;
    }

    public boolean deleteProjectById(String id) {
        try {
            PreparedStatement deleteScheduleById = jdbcTemplateConnection.prepareStatement(deleteProjectSQl);
            deleteScheduleById.setString(1, id);
            int rows = deleteScheduleById.executeUpdate();
            if (rows <= 0)
                return false;
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public Project getProjectById(String id) throws SQLException {
        PreparedStatement getProjectByid = jdbcTemplateConnection.prepareStatement(getProjectSQL);
        getProjectByid.setString(1, id);
        ResultSet resultSet = getProjectByid.executeQuery();
        Project project = null;
        while (resultSet.next()) {
            project = new Project();
            project.setProject_id(resultSet.getString("project_id"));
            project.setProject_title(resultSet.getString("project_title"));
            project.setDescription(resultSet.getString("description"));
            project.setCreatedon(resultSet.getTimestamp("createdon"));
        }
        if (project == null)
            return null;
        return project;
    }

    public List<Project> getAllSchedules() throws SQLException {
        PreparedStatement getAllSchedules = jdbcTemplateConnection.prepareStatement(getAllProjects);
        ResultSet resultSet = getAllSchedules.executeQuery();
        List<Project> projectList = new ArrayList<>();
        while (resultSet.next()) {
            Project project = new Project();
            project.setProject_id(resultSet.getString("project_id"));
            project.setProject_title(resultSet.getString("project_title"));
            project.setDescription(resultSet.getString("description"));
            project.setCreatedon(resultSet.getTimestamp("createdon"));
            projectList.add(project);
        }
        return projectList;
    }

    public HashMap<String,Object> getAllSchedulesByProjectId(String id) throws SQLException {
        PreparedStatement getAllSchedules = jdbcTemplateConnection.prepareStatement(getAllSchedulesByProjectId);
        getAllSchedules.setString(1,id);
        ResultSet resultSet = getAllSchedules.executeQuery();
        List<HashMap<String, Object>> scheduleList = new ArrayList<>();
        HashMap<String, Object> scheduleHistory = new HashMap<>();

        while (resultSet.next()) {
            HashMap<String, Object> schedule = new HashMap<>();
            schedule.put("schedule Id", resultSet.getInt("scheduleId"));
            schedule.put("Schedulename",resultSet.getString("name"));
            schedule.put("Target", resultSet.getString("target"));
            schedule.put("Active", String.valueOf(resultSet.getBoolean("active")));
            schedule.put("TargetType", resultSet.getString("targetType"));
            scheduleList.add(schedule);
        }
        if (!scheduleList.isEmpty()) {
            scheduleHistory.put("Schedules", scheduleList);
            scheduleHistory.put("Project Id",id);
        }
        return scheduleHistory;
    }
}
