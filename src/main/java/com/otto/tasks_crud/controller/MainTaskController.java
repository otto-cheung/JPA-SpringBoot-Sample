package com.otto.tasks_crud.controller;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.otto.tasks_crud.models.MainTask;
import com.otto.tasks_crud.repository.MainTaskRepository;

@RestController
@RequestMapping("/api")
public class MainTaskController {

    private static final Logger LOGGER = Logger.getLogger(MainTask.class.getName());

    @Autowired
    private MainTaskRepository taskRepository;

    @PostMapping("/task")
    public ResponseEntity<MainTask> createTask(@RequestBody MainTask task) {
        try {
            LOGGER.info("Creating task: " + task.getTitle());
            return new ResponseEntity<>(taskRepository.save(task), HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.severe(e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/tasks")
    public ResponseEntity<List<MainTask>> createTasks(@RequestBody List<MainTask> tasks) {
        try {
            LOGGER.info("Creating tasks: " + tasks.size());
            return new ResponseEntity<>(taskRepository.saveAll(tasks), HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.severe(e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/tasks")
    public ResponseEntity<List<MainTask>> getTasks(
            @RequestParam(value = "id", required = false) Long id,
            @RequestParam(value = "title", required = false) String title,
            @RequestParam(value = "description", required = false) String description,
            @RequestParam(value = "limit", defaultValue = "10") int limit,
            @RequestParam(value = "offset", defaultValue = "0") int offset,
            @RequestParam(value = "orderBy", defaultValue = "title") String orderBy,
            @RequestParam(value = "searchCriteria", required = false) String searchCriteria) {

        try {
            if (id != null) {
                MainTask task = taskRepository.findById(id).orElse(null);
                if (task != null) {
                    return new ResponseEntity<>(Collections.singletonList(task), HttpStatus.OK);
                } else {
                    return new ResponseEntity<>(Collections.emptyList(), HttpStatus.OK);
                }
            }

            if (searchCriteria != null) {
                System.out.println(searchCriteria);
                return new ResponseEntity<>(
                        taskRepository.findAllWithSearchCriteria(searchCriteria, limit, offset, orderBy),
                        HttpStatus.OK);
            }

            if (title != null && description != null) {
                return new ResponseEntity<>(
                        taskRepository.findByTitleContainingOrDescriptionContaining(title, description, limit, offset,
                                orderBy),
                        HttpStatus.OK);
            }

            if (title != null && description == null) {
                return new ResponseEntity<>(taskRepository.findByTitleContaining(title, limit, offset, orderBy),
                        HttpStatus.OK);
            }

            if (description != null && title == null) {
                return new ResponseEntity<>(
                        taskRepository.findByDescriptionContaining(description, limit, offset, orderBy), HttpStatus.OK);
            }

            return new ResponseEntity<>(taskRepository.findAll(limit, offset, orderBy), HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.severe(e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/task/{id}")
    public ResponseEntity<MainTask> updateTask(@PathVariable Long id, @RequestBody MainTask task)

    {
        try {
            Optional<MainTask> existingTask = taskRepository.findById(id);
            if (existingTask.isPresent()) {
                MainTask updatedTask = existingTask.get();
                updatedTask.setTitle(task.getTitle());
                updatedTask.setDescription(task.getDescription());
                updatedTask.setStartDate(task.getStartDate());
                updatedTask.setEndDate(task.getEndDate());
                return new ResponseEntity<>(taskRepository.save(updatedTask), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            LOGGER.severe(e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/task/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        try {
            taskRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            LOGGER.severe(e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
