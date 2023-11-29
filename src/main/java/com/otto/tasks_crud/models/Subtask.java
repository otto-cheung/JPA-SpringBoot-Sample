package com.otto.tasks_crud.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

@Entity
@Table(name = "subtasks")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class Subtask extends Task {

    public Subtask() {
        super(null, null, null, null);
    }

    public Subtask(
            String id,
            String title,
            String description,
            java.util.Date startDate,
            java.util.Date endDate,
            MainTask task) {
        super(title, description, startDate, endDate);
        this.id = Long.parseLong(id);
        this.task = task;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne()
    @JoinColumn(name = "task_id")
    @JsonBackReference
    private MainTask task;

    ///
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public MainTask getTask() {
        return task;
    }

    public void setTask(MainTask task) {
        this.task = task;
    }

}
