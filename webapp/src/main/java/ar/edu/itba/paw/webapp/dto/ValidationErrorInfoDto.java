package ar.edu.itba.paw.webapp.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ValidationErrorInfoDto implements Serializable {

    private Integer status;
    private String title;
    private List<ValidationErrorDto> messages;
    private String path;

    public ValidationErrorInfoDto() {
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<ValidationErrorDto> getMessages() {
        return messages;
    }

    public void setMessages(List<ValidationErrorDto> messages) {
        this.messages = messages;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}

