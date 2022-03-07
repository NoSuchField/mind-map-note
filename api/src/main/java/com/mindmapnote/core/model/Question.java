package com.mindmapnote.core.model;

import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

/**
 * @author Leon
 * @date 2022/02/16 21:05
 */
@Data
public class Question implements Serializable {

    private static final long serialVersionUID = 572601802644385714L;

    private Integer id;

    private String uuid;

    private String title;

    private String detail;

    private String comment;

    private Long rank;

    private Integer parent;

    private List<Question> children;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Question question = (Question) o;
        return Objects.equals(id, question.id) && Objects.equals(uuid, question.uuid) && Objects.equals(title, question.title) && Objects.equals(detail, question.detail) && Objects.equals(comment, question.comment) && Objects.equals(rank, question.rank) && Objects.equals(parent, question.parent);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, uuid, title, detail, comment, rank, parent);
    }
}
