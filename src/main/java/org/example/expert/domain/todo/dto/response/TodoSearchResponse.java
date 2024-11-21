package org.example.expert.domain.todo.dto.response;

import lombok.Getter;

@Getter
public class TodoSearchResponse {

    private String title;
    private Long commentCount;
    private Long managerCount;

}

