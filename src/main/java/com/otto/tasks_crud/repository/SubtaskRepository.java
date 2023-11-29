package com.otto.tasks_crud.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.otto.tasks_crud.models.Subtask;
import java.util.List;

@Repository
public interface SubtaskRepository extends ListCrudRepository<Subtask, Long> {

    @Query(value = "SELECT * FROM subtasks s WHERE s.title LIKE %:searchCriteria% OR s.description LIKE %:searchCriteria% ORDER BY :orderBy ASC LIMIT :limit OFFSET :offset", nativeQuery = true)
    List<Subtask> findAllWithSearchCriteria(@Param("searchCriteria") String searchCriteria, @Param("limit") int limit,
            @Param("offset") int offset,
            @Param("orderBy") String orderBy);

    @Query(value = "SELECT * FROM subtasks WHERE title LIKE %:title% OR description LIKE %:description% ORDER BY :orderBy ASC LIMIT :limit OFFSET :offset", nativeQuery = true)
    List<Subtask> findByTitleContainingOrDescriptionContaining(@Param("title") String title,
            @Param("description") String description, @Param("limit") int limit,
            @Param("offset") int offset,
            @Param("orderBy") String orderBy);

    @Query(value = "SELECT * FROM subtasks WHERE title LIKE %:title% ORDER BY :orderBy ASC LIMIT :limit OFFSET :offset", nativeQuery = true)
    List<Subtask> findByTitleContaining(@Param("title") String title,
            @Param("limit") int limit,
            @Param("offset") int offset,
            @Param("orderBy") String orderBy);

    @Query(value = "SELECT * FROM subtasks WHERE description LIKE %:description% ORDER BY :orderBy ASC LIMIT :limit OFFSET :offset", nativeQuery = true)
    List<Subtask> findByDescriptionContaining(@Param("description") String description,
            @Param("limit") int limit,
            @Param("offset") int offset,
            @Param("orderBy") String orderBy);

    @Query(value = "SELECT * FROM subtasks ORDER BY :orderBy ASC LIMIT :limit OFFSET :offset", nativeQuery = true)
    List<Subtask> findAll(@Param("limit") int limit,
            @Param("offset") int offset,
            @Param("orderBy") String orderBy);

}
