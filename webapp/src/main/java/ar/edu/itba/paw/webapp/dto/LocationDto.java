package ar.edu.itba.paw.webapp.dto;
import ar.edu.itba.paw.models.SubjectClassTime;

import javax.ws.rs.core.UriInfo;
import java.time.LocalTime;

public class LocationDto {
    private Long id;
    private Integer day;
    private LocalTime startTime;
    private LocalTime endTime;
    private String location;
    private String building;
    private String mode;

    public static LocationDto fromLocation(UriInfo uriInfo, SubjectClassTime subjectClassTime){
        final LocationDto locationDto = new LocationDto();

        locationDto.id = subjectClassTime.getId();
        locationDto.day = subjectClassTime.getDay();
        locationDto.startTime = subjectClassTime.getStartTime();
        locationDto.endTime = subjectClassTime.getEndTime();
        locationDto.location = subjectClassTime.getClassLoc();
        locationDto.building = subjectClassTime.getBuilding();
        locationDto.mode = subjectClassTime.getMode();

        return locationDto;
    }

    // Getters

    public Long getId() {
        return id;
    }

    public Integer getDay() {
        return day;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public String getLocation() {
        return location;
    }

    public String getBuilding() {
        return building;
    }

    public String getMode() {
        return mode;
    }

    // Setters

    public void setId(Long id) {
        this.id = id;
    }

    public void setDay(Integer day) {
        this.day = day;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setBuilding(String building) {
        this.building = building;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }
}
