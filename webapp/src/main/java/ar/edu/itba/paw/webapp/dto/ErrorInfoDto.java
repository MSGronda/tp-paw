package ar.edu.itba.paw.webapp.dto;


import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorInfoDto implements Serializable {

    private Integer status;
    private String title;
    private List<String> messages;
    private String path;

    public ErrorInfoDto() {
        messages = new ArrayList<>();
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

    public List<String> getMessages() {
        return messages;
    }

    public void setMessages(List<String> messages) {
        this.messages = messages;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void addMessage(String message) {
        messages.add(message);
    }

    public void addAllMessages(List<String> messages) {
        this.messages.addAll(messages);
    }
}
