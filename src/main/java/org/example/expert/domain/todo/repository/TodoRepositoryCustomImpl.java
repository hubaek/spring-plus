package org.example.expert.domain.todo.repository;

import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.example.expert.domain.todo.dto.request.TodoSearchRequest;
import org.example.expert.domain.todo.dto.response.TodoSearchResponse;
import org.example.expert.domain.todo.entity.Todo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static org.example.expert.domain.comment.entity.QComment.comment;
import static org.example.expert.domain.manager.entity.QManager.manager;
import static org.example.expert.domain.todo.entity.QTodo.todo;
import static org.example.expert.domain.user.entity.QUser.user;

@Repository
@RequiredArgsConstructor
public class TodoRepositoryCustomImpl implements TodoRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Optional<Todo> findByIdWithUser(Long todoId) {
        Todo result = jpaQueryFactory
                .selectFrom(todo)
                .leftJoin(todo.user, user).fetchJoin()
                .where(idEq(todoId))
                .fetchOne();    // 단건 조회

        // Optional로 감싸서 반환
        return Optional.ofNullable(result);
    }

    @Override
    public List<TodoSearchResponse> searchTodosByFilters(Pageable pageable, TodoSearchRequest searchRequest) {
        String title = searchRequest.getTitle();
        String nickname = searchRequest.getNickname();

        var commentCount = JPAExpressions
                .select(comment.count())
                .from(comment)
                .where(comment.todo.eq(todo));

        var managerCount = JPAExpressions
                .select(manager.count())
                .from(manager)
                .where(manager.todo.eq(todo));

//        return jpaQueryFactory
//                .select(Projections.fields(
//                        TodoSearchResponse.class,
//                        todo.title.as("title")),
//                        Expressions.as(commentCount,"commentCount"),
//                        Expressions.as(managerCount,"managerCount")
//                )
//                .from(todo)
//                .join(todo.user, user).fetchJoin()
//                .where(titleContains(title),
//                        nicknameContains(nickname)
//                )
//                .orderBy(todo.createdAt.desc())
//                .limit(pageable.getPageSize())
//                .offset(pageable.getOffset())
//                .fetch();


        return jpaQueryFactory
                .select(Projections.fields(TodoSearchResponse.class,
                        todo.title,
                        comment.id.countDistinct().as("commentCount"),
                        manager.id.countDistinct().as("managerCount")))
                .from(todo)
                .leftJoin(todo.managers, manager)
                .leftJoin(manager.user, user)
                .leftJoin(todo.comments, comment)
                .where(titleContains(title),
                        nicknameContains(nickname)
                )
                .groupBy(todo.id)
                .orderBy(todo.createdAt.desc())
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset())
                .fetch();
    }

    private BooleanExpression nicknameContains(String nickname) {
        return nickname != null ? user.nickname.contains(nickname) : null;
    }

    private BooleanExpression titleContains(String title) {
        return title != null ? todo.title.contains(title) : null;
    }

    private BooleanExpression idEq(Long todoId) {
        return todoId != null ? todo.id.eq(todoId) : null;
    }
}
