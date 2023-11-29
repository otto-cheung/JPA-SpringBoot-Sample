package com.otto.tasks_crud.controller;

import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.otto.tasks_crud.models.MainTask;
import com.otto.tasks_crud.models.Subtask;
import com.otto.tasks_crud.repository.SubtaskRepository;

@RestController
@RequestMapping("/api")
public class SubtaskController {

    private static final Logger LOGGER = Logger.getLogger(MainTask.class.getName());

    @Autowired
    private SubtaskRepository subtaskRepository;

    @PostMapping("/subtask")
    public ResponseEntity<Subtask> createSubtask(@RequestBody Subtask subtask) {
        try {
            return ResponseEntity.ok(subtaskRepository.save(subtask));
        } catch (Exception e) {
            LOGGER.severe(e.getMessage());
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PostMapping("/subtasks")
    public ResponseEntity<List<Subtask>> createSubtasks(@RequestBody List<Subtask> subtasks) {
        try {
            return ResponseEntity.ok(subtaskRepository.saveAll(subtasks));
        } catch (Exception e) {
            LOGGER.severe(e.getMessage());

            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/subtasks")
    public ResponseEntity<List<Subtask>> getSubtasks(
            @RequestParam(value = "id", required = false) Long id,
            @RequestParam(value = "title", required = false) String title,
            @RequestParam(value = "description", required = false) String description,
            @RequestParam(value = "limit", defaultValue = "10") int limit,
            @RequestParam(value = "offset", defaultValue = "0") int offset,
            @RequestParam(value = "orderBy", defaultValue = "title") String orderBy,
            @RequestParam(value = "searchCriteria", required = false) String searchCriteria) {

        try {
            if (id != null) {
                Subtask subtask = subtaskRepository.findById(id).orElse(null);
                if (subtask != null) {
                    return ResponseEntity.ok().body(List.of(subtask));
                } else {
                    return ResponseEntity.notFound().build();
                }
            }
            if (searchCriteria != null) {
                return ResponseEntity.ok()
                        .body(subtaskRepository.findAllWithSearchCriteria(searchCriteria, limit, offset, orderBy));
            }
            if (title != null && description != null) {
                return ResponseEntity.ok().body(subtaskRepository.findByTitleContainingOrDescriptionContaining(title,
                        description, limit, offset, orderBy));
            }
            if (title != null) {
                return ResponseEntity.ok().body(subtaskRepository.findByTitleContaining(title, limit, offset, orderBy));
            }
            if (description != null) {
                return ResponseEntity.ok()
                        .body(subtaskRepository.findByDescriptionContaining(description, limit, offset, orderBy));
            }

            return ResponseEntity.ok().body(subtaskRepository.findAll(limit, offset, orderBy));

        } catch (Exception e) {
            LOGGER.severe(e.getMessage());

            return ResponseEntity.badRequest().body(null);
        }
    }

    @PutMapping("/subtask/{id}")
    public ResponseEntity<Subtask> updateSubtask(@PathVariable("id") long id, @RequestBody Subtask subtask) {
        Optional<Subtask> subtaskData = subtaskRepository.findById(id);

        try {
            if (subtaskData.isPresent()) {
                Subtask _subtask = subtaskData.get();
                _subtask.setTitle(subtask.getTitle());
                _subtask.setDescription(subtask.getDescription());
                _subtask.setTask(subtask.getTask());
                return ResponseEntity.ok(subtaskRepository.save(_subtask));
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            LOGGER.severe(e.getMessage());

            return ResponseEntity.badRequest().body(null);
        }

    }

    @DeleteMapping("/subtask/{id}")
    public ResponseEntity<Subtask> deleteSubtask(@PathVariable("id") long id) {
        try {
            subtaskRepository.deleteById(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            LOGGER.severe(e.getMessage());

            return ResponseEntity.notFound().build();
        }
    }
}
