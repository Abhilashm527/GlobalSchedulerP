package com.ivoyant.GlobalScheduler.Model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class Project {
    private String project_id ;

    @JsonProperty("projectTitle")
    private String project_title ;

    @JsonProperty("description")
    private String description;

    private Date createdon;

}
