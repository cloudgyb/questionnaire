package com.gyb.questionnaire.controller;

import com.gyb.questionnaire.config.RequiredLogin;
import com.gyb.questionnaire.controller.form.PaperForm;
import com.gyb.questionnaire.dto.PaperDetailDTO;
import com.gyb.questionnaire.dto.QuestionnaireDTO;
import com.gyb.questionnaire.entity.Paper;
import com.gyb.questionnaire.entity.Questionnaire;
import com.gyb.questionnaire.service.PaperService;
import com.gyb.questionnaire.service.QuestionnaireService;
import com.gyb.questionnaire.util.ClientUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 答卷相关，进入答卷页面，提交答卷等操作
 *
 * @author geng
 * 2020/11/21
 */
@Controller
public class PaperController {
    private final QuestionnaireService questionnaireService;
    private final PaperService paperService;

    public PaperController(QuestionnaireService questionnaireService,
                           PaperService paperService) {
        this.questionnaireService = questionnaireService;
        this.paperService = paperService;
    }

    /**
     * 到答卷页面
     *
     * @param questionnaireId 问卷id
     * @return 如果问卷不能填写返回错误页面
     */
    @GetMapping("/q/{questionnaireId}")
    public String page(@PathVariable Long questionnaireId, Model m) {
        final Questionnaire q = questionnaireService.get(questionnaireId);
        if (q == null) {
            m.addAttribute("errMsg", "感谢您的参与，问卷不存在或者已被发布者删除啦！");
            return "paper_error";
        } else if (q.getStatus() != 1) {
            m.addAttribute("errMsg", "感谢您的参与，问卷已经结束啦！");
            return "paper_error";
        }
        final QuestionnaireDTO questionnaire = questionnaireService.getQuestionnaireDetail(q);
        m.addAttribute("qa", questionnaire);
        final boolean mobileClient = ClientUtil.isMobileClient();
        if (mobileClient) //是移动客户端
            return "paper_mobile";
        return "paper";
    }

    /**
     * 提交答卷
     * @param paperForm 答卷表单
     * @see PaperForm
     */
    @PostMapping("/q/submit")
    @ResponseBody
    public ResponseResult submit(@RequestBody PaperForm paperForm) {
        return paperService.submitPaper(paperForm);
    }

    /**
     * 到答卷提交后的致谢页面
     */
    @GetMapping("/q/thanks")
    public String thanksPage() {
        return "paper_thank";
    }

    /**
     * 获取问卷下的答卷列表
     * @param questionnaireId 问卷id
     */
    @GetMapping("/paper/list/{questionnaireId}")
    @RequiredLogin
    public String paperList(@PathVariable Long questionnaireId,Model model){
        final Questionnaire questionnaire = questionnaireService.getUserQuestionnaire(questionnaireId);
        final int n = paperService.paperCount(questionnaireId);
        if(questionnaire !=null)
            questionnaire.setPaperCount(n);
        List<Paper> list = paperService.getByQuestionnaireId(questionnaireId);
        model.addAttribute("list",list);
        model.addAttribute("q",questionnaire);
        return "paper_list";
    }

    /**
     * 获取答卷答案详情
     * @param paperId 答卷id
     */
    @GetMapping("/paper/view/{paperId}")
    @RequiredLogin
    public String paperDetail(@PathVariable Long paperId,Model m){
        final PaperDetailDTO paperDetail = paperService.getPaperDetail(paperId);
        m.addAttribute("dto",paperDetail);
        return "paper_detail";
    }

    @PostMapping("/paper/delete")
    @RequiredLogin
    @ResponseBody
    public ResponseResult delete(@RequestParam Long paperId){
       return paperService.deletePaper(paperId);
    }
}
