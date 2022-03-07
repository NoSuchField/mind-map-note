package com.mindmapnote.core.controller;

import com.mindmapnote.core.model.Question;
import com.mindmapnote.core.model.QuestionTreeNode;
import com.mindmapnote.core.model.Response;
import com.mindmapnote.core.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author Leon
 * @date 2022/02/16 21:11
 */
@CrossOrigin(origins = {"http://localhost:8080", "http://127.0.0.1:8080"})
@RestController
@RequestMapping("/question")
public class QuestionController {

    @Autowired
    private QuestionService questionService;

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public Response<Question> addQuestion(@RequestBody Question question) {
        return Response.success(questionService.add(question));
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public Response<String> updateQuestion(@RequestBody Question question) {
        questionService.update(question);
        return Response.success();
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public Response<String> deleteQuestion(@RequestParam Integer id) {
        questionService.delete(id);
        return Response.success();
    }

    @RequestMapping(value = "/up", method = RequestMethod.POST)
    public Response<String> upQuestion(@RequestParam Integer id) {
        questionService.up(id);
        return Response.success();
    }

    @RequestMapping(value = "/down", method = RequestMethod.POST)
    public Response<String> downQuestion(@RequestParam Integer id) {
        questionService.down(id);
        return Response.success();
    }

    @RequestMapping(value = "/get", method = RequestMethod.GET)
    public Response<Question> getQuestion(@RequestParam Integer id) {
        return Response.success(questionService.get(id));
    }

    @RequestMapping(value = "/random", method = RequestMethod.GET)
    public Response<Question> randomQuestion() {
        return Response.success(questionService.random());
    }

    @RequestMapping(value = "/tree", method = RequestMethod.GET)
    public Response<QuestionTreeNode> treeQuestion(@RequestParam(required = false) Integer rootId) {
        return Response.success(questionService.getTree(rootId));
    }
}
