package com.ivoyant.GlobalScheduler.service;

import com.ivoyant.GlobalScheduler.Model.Project;
import com.ivoyant.GlobalScheduler.Model.Schedule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import javax.management.openmbean.OpenMBeanConstructorInfo;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

@Component
public class ScheduleController implements ScheduleIn {


    @Autowired
    private ScheduleImpl scheduleImpl;
    @Autowired
    private ProjectImpl projectImpl;

    @Override
    public ResponseEntity createSchedule(Schedule schedule) throws SQLException {
        Schedule schedule1 = scheduleImpl.create(schedule);
        return ResponseEntity.status(HttpStatusCode.valueOf(201)).body("Schedule has been created");
    }

    @Override
    public ResponseEntity getScheduleById(int id) throws SQLException {
        Schedule schedule = scheduleImpl.getSchedule(id);
        if (schedule == null)
            return ResponseEntity.status(HttpStatusCode.valueOf(404)).body("The Schedule is not found");
        return ResponseEntity.ok(schedule);
    }

    @Override
    public ResponseEntity updateScheduleById(Schedule schedule) throws SQLException {
        Schedule schedule1 = scheduleImpl.updateSchedule(schedule);
        return ResponseEntity.ok(schedule1);
    }

    @Override
    public ResponseEntity deleteScheduleById(int id) {
        boolean isRemoved = scheduleImpl.deleteSchedule(id);
        if (isRemoved)
            return ResponseEntity.ok("The schedule has been removed");
        return ResponseEntity.ok("The Schedule is not Found");
    }

    @Override
    public ResponseEntity getAllSchedule() {
        List<Schedule> scheduleList = scheduleImpl.getAllSchedules();
        if (scheduleList == null)
            return ResponseEntity.ok("No schedules");
        return ResponseEntity.ok(scheduleList);
    }

    @Override
    public ResponseEntity getScheduleHistory(int id) throws SQLException {
        HashMap<String, Object> scheduleList = scheduleImpl.getScheduleHistory(id);
        if (scheduleList == null || scheduleList.isEmpty())
            return ResponseEntity.status(HttpStatusCode.valueOf(404)).body("The Schedule is not found");
        return ResponseEntity.ok(scheduleList);
    }

    @Override
    public ResponseEntity createProject(Project project) throws SQLException {
        Project project1 = projectImpl.create(project);
        return ResponseEntity.status(HttpStatusCode.valueOf(201)).body("Project has been created");
    }
    @Override
    public ResponseEntity updateProject(Project project) throws SQLException {
        Project project1 = projectImpl.updateProject(project);
        return ResponseEntity.ok(project1);
    }
    @Override
    public ResponseEntity deleteProject(String id) throws SQLException {
        Boolean isRemoved = projectImpl.deleteProjectById(id);
        if (isRemoved)
            return ResponseEntity.ok("The Project has been removed");
        return ResponseEntity.ok("The Project is not Found");
    }
    @Override
    public ResponseEntity getProject(String id) throws SQLException {
        Project project = projectImpl.getProjectById(id);
        if (project == null)
            return ResponseEntity.status(HttpStatusCode.valueOf(404)).body("The project is not found");
        return ResponseEntity.ok(project);
    }
    @Override
    public ResponseEntity getAllProject() throws SQLException {
        List<Project> projectList = projectImpl.getAllSchedules();
        if (projectList == null || projectList.isEmpty())
            return ResponseEntity.ok("No Projects");
        return ResponseEntity.ok(projectList);
    }

    @Override
    public ResponseEntity getAllSchedulesByProjectId(String id) throws SQLException {
        HashMap<String,Object> scheduleList = projectImpl.getAllSchedulesByProjectId(id);
        if (scheduleList == null || scheduleList.isEmpty())
            return ResponseEntity.ok("No schedules found for the given Project Id : "+id);
        return ResponseEntity.ok(scheduleList);
    }
}
