package com.example.todolist2025;

public class Task {
    private String content; // task content
    private String deadLine; // there is deadline if not null
    private boolean priority; // true - the task has high priority
    private String id; // firebase id

    public Task()
    {

    }
    public Task(String content,  String deadLine, boolean priority, String id) {
        this.content = content;
        this.priority = priority;
        this.deadLine = deadLine;
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDeadLine() {
        return deadLine;
    }

    public void setDeadLine(String deadLine) {
        this.deadLine = deadLine;
    }

    public boolean isPriority() {
        return priority;
    }

    public void setPriority(boolean priority) {
        this.priority = priority;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
