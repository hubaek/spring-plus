package org.example.expert.domain.todo.repository;

import org.example.expert.domain.todo.entity.Todo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.assertj.core.api.Assertions.*;
import java.util.Optional;

@SpringBootTest
public class TodoRepositoryTest {

    @Autowired
    private TodoRepository todoRepository;

    @Test
    public void testFindByIdWithUser() {
        Optional<Todo> todoOptional = todoRepository.findByIdWithUser(2L);

        // Optional이 값이 있는지 확인
        assertThat(todoOptional).isPresent();

        Todo todo = todoOptional.get();
        assertThat(todo.getUser()).isNotNull();
    }

}