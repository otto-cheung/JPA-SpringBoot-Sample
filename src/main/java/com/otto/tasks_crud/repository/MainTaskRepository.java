package com.otto.tasks_crud.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.otto.tasks_crud.models.MainTask;

@Repository
public interface MainTaskRepository extends ListCrudRepository<MainTask, Long> {

        @Query(value = "SELECT * FROM tasks t WHERE t.title LIKE %:searchCriteria% OR t.description LIKE %:searchCriteria% ORDER BY :orderBy ASC LIMIT :limit OFFSET :offset", nativeQuery = true)
        List<MainTask> findAllWithSearchCriteria(@Param("searchCriteria") String searchCriteria,
                        @Param("limit") int limit,
                        @Param("offset") int offset,
                        @Param("orderBy") String orderBy);

        @Query(value = "SELECT * FROM tasks WHERE title LIKE %:title% OR description LIKE %:description% ORDER BY :orderBy ASC LIMIT :limit OFFSET :offset", nativeQuery = true)
        List<MainTask> findByTitleContainingOrDescriptionContaining(@Param("title") String title,
                        @Param("description") String description, @Param("limit") int limit,
                        @Param("offset") int offset,
                        @Param("orderBy") String orderBy);

        @Query(value = "SELECT * FROM tasks WHERE title LIKE %:title% ORDER BY :orderBy ASC LIMIT :limit OFFSET :offset", nativeQuery = true)
        List<MainTask> findByTitleContaining(@Param("title") String title,
                        @Param("limit") int limit,
                        @Param("offset") int offset,
                        @Param("orderBy") String orderBy);

        @Query(value = "SELECT * FROM tasks WHERE description LIKE %:description% ORDER BY :orderBy ASC LIMIT :limit OFFSET :offset", nativeQuery = true)
        List<MainTask> findByDescriptionContaining(@Param("description") String description,
                        @Param("limit") int limit,
                        @Param("offset") int offset,
                        @Param("orderBy") String orderBy);

        @Query(value = "SELECT * FROM tasks ORDER BY :orderBy ASC LIMIT :limit OFFSET :offset", nativeQuery = true)
        List<MainTask> findAll(@Param("limit") int limit,
                        @Param("offset") int offset,
                        @Param("orderBy") String orderBy);
}
