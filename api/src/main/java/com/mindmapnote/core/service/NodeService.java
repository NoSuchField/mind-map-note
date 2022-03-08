package com.mindmapnote.core.service;

import com.mindmapnote.core.mapper.QuestionMapper;
import com.mindmapnote.core.model.Question;
import com.mindmapnote.core.model.QuestionTreeNode;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * @author Leon
 * @date 2022/02/16 21:20
 */
@Slf4j
@Service
public class NodeService {

    private final Logger logger = LoggerFactory.getLogger(NodeService.class);

    @Resource
    private QuestionMapper questionMapper;

    public Question add(Question question) {
        question.setRank(System.currentTimeMillis());
        Integer questionId = questionMapper.add(question);
        return questionMapper.getById(questionId);
    }


    public void update(Question question) {
        questionMapper.update(question);
    }

    public void delete(Integer id) {
        questionMapper.delete(id);
    }

    public void up(Integer id) {

        Question question = get(id);
        Long rank = question.getRank();
        Integer parent = question.getParent();

        Question upRankQuestion = questionMapper.getUpRankQuestion(parent, rank);

        if (upRankQuestion != null && !question.equals(upRankQuestion)) {
            Long r = question.getRank();
            question.setRank(upRankQuestion.getRank());
            upRankQuestion.setRank(r);
            update(question);
            update(upRankQuestion);
        }
    }

    public void down(Integer id) {
        Question question = get(id);
        Long rank = question.getRank();
        Integer parent = question.getParent();

        Question downRankQuestion = questionMapper.getDownRankQuestion(parent, rank);

        if (downRankQuestion != null && !question.equals(downRankQuestion)) {
            Long r = question.getRank();
            question.setRank(downRankQuestion.getRank());
            downRankQuestion.setRank(r);
            update(question);
            update(downRankQuestion);
        }
    }

    public Question get(Integer id) {
        return questionMapper.getById(id);
    }

    public Question random() {
        // id rank
        List<Question> questionList = questionMapper.getAll();
        List<Question> questionListWeighted = new ArrayList<>();

        for (Question question : questionList) {
            questionListWeighted.add(question);
        }
        Random r = new Random();
        return get(questionListWeighted.get(r.nextInt(questionListWeighted.size())).getId());
    }

    public void removeOrphanNodes() {
        logger.info("removing all orphan nodes");
        questionMapper.removeOrphanNodes();
    }

    private List<QuestionTreeNode> getChildren(QuestionTreeNode root,List<QuestionTreeNode> all){
        List<QuestionTreeNode> childrenList = all.stream()
                .filter(t -> Objects.equals(t.getPid(), root.getId()))
                .map(g -> {
                    //找子菜单
                    g.setChildren(getChildren(g,all));
                    return g;
                })
                //菜单排序
                .sorted((menu1,menu2) -> (int) ((menu1.getRank() == null ? 0 : menu1.getRank()) - (menu2.getRank() == null ? 0 : menu2.getRank())))
                .collect(Collectors.toList());
        return childrenList;
    }

    public QuestionTreeNode getTree(Integer rootId) {
        List<QuestionTreeNode> questionList = questionMapper.getTree();
        List<QuestionTreeNode> questionTree = questionList.stream()
                .filter(t -> Objects.equals(t.getPid(), rootId))
                .map((menu) -> {
                    menu.setChildren(this.getChildren(menu,questionList));
                    return menu;
                })
                .sorted((menu1,menu2) -> (int) ((menu1.getRank() == null ? 0 : menu1.getRank()) - (menu2.getRank() == null ? 0 : menu2.getRank())))
                .collect(Collectors.toList());

        QuestionTreeNode rootNode = new QuestionTreeNode();

        if (rootId == null) {
            rootNode.setId(0);
            rootNode.setExpand(true);
            rootNode.setLabel("Root");
        } else {
            Question question = get(rootId);
            rootNode.setId(question.getId());
            rootNode.setLabel(question.getTitle());
            rootNode.setExpand(true);
        }

        rootNode.setChildren(questionTree);
        return rootNode;
    }
}
