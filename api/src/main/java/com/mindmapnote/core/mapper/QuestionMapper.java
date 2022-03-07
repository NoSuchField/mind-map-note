package com.mindmapnote.core.mapper;

import com.mindmapnote.core.model.Question;
import com.mindmapnote.core.model.QuestionTreeNode;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Leon
 * @date 2022/02/16 21:21
 */
@Repository
public interface QuestionMapper {
    Integer add(Question question);

    Question getById(Integer id);

    void update(Question question);

    void delete(Integer id);

    void up(Integer id);

    void down(Integer id);

    List<Question> getAll();

    Question getUpRankQuestion(@Param("parent") Integer parent, @Param("rank") Long rank);

    Question getDownRankQuestion(@Param("parent") Integer parent, @Param("rank") Long rank);

    List<QuestionTreeNode> getTree();

}
