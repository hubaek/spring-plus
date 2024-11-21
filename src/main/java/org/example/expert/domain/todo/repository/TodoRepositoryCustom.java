package org.example.expert.domain.todo.repository;


import org.example.expert.domain.todo.dto.request.TodoSearchRequest;
import org.example.expert.domain.todo.dto.response.TodoSearchResponse;
import org.example.expert.domain.todo.entity.Todo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface TodoRepositoryCustom {

    Optional<Todo> findByIdWithUser(Long todoId);

    List<TodoSearchResponse> searchTodosByFilters(Pageable pageable, TodoSearchRequest searchRequest);
}
