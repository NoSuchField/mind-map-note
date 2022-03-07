package com.mindmapnote.core.model;

import lombok.Data;

import java.util.List;

/**
 * @author Leon
 * @date 2022/02/17 16:09
 */
@Data
public class QuestionTreeNode {

    private Integer id;

    private String label;

    private Boolean isCurrent;

    private Long rank;

    private Integer pid;

    private Boolean expand;

    private List<QuestionTreeNode> children;
}
