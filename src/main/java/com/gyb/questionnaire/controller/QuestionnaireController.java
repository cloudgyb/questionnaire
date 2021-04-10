package com.gyb.questionnaire.controller;

import com.gyb.questionnaire.config.RequiredLogin;
import com.gyb.questionnaire.dto.QuestionnaireDTO;
import com.gyb.questionnaire.entity.Questionnaire;
import com.gyb.questionnaire.service.QuestionnaireCloneService;
import com.gyb.questionnaire.service.QuestionnaireService;
import org.hibernate.validator.constraints.Length;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotBlank;

import java.util.List;

/**
 * 问卷
 *
 * @author geng
 * 2020/11/8
 */
@Controller
@RequestMapping("/questionnaire")
@Validated
public class QuestionnaireController {
	private final QuestionnaireService questionnaireService;
	private final QuestionnaireCloneService questionnaireCloneService;

	public QuestionnaireController(QuestionnaireService questionnaireService,
			QuestionnaireCloneService questionnaireCloneService) {
		this.questionnaireService = questionnaireService;
		this.questionnaireCloneService = questionnaireCloneService;
	}

	@GetMapping("/create")
	@RequiredLogin
	public String createQuestionnairePage() {
		return "questionnaire_create";
	}

	@PostMapping("/doCreate")
	@ResponseBody
	@RequiredLogin
	public ResponseResult createQuestionnaire(@NotBlank(message = "问卷名称不能为空")
	@Length(max = 30, message = "问卷名称不能超过30个字符") String name,
			@Length(max = 100, message = "欢迎语不能超过100个字符")
			@RequestParam(required = false) String greeting) {
		final Long id = questionnaireService.add(name, greeting);
		return ResponseResult.ok("创建成功", id);
	}

	@GetMapping("/createByTemplate")
	@RequiredLogin
	public String createQuestionnaireByTemplate(@RequestParam long templateId) {
		Long id = questionnaireService.addByTemplate(templateId);
		if (id == null)
			return String.format("redirect:/questionnaire/design/%s", "templateerror");
		return String.format("redirect:/questionnaire/design/%s", id);
	}

	/**
	 * 从已有的问卷克隆问卷
	 */
	@PostMapping("/copy")
	@RequiredLogin
	@ResponseBody
	public ResponseResult copy(@RequestParam Long questionnaireId,
			@NotBlank(message = "问卷名称不能为空")
			@Length(max = 30, message = "问卷名称不能超过30个字符") String name,
			@Length(max = 100, message = "欢迎语不能超过100个字符")
			@RequestParam(required = false) String greeting) {
		return questionnaireCloneService.copy(questionnaireId, name, greeting);
	}

	/**
	 * 进入问卷设计页面
	 */
	@GetMapping("/design/{questionnaireId}")
	@RequiredLogin
	public String designQuestionnairePage(@PathVariable Long questionnaireId, Model m) {
		final QuestionnaireDTO questionnaire = questionnaireService.getUserQuestionnaireDetail(questionnaireId);
		m.addAttribute("q", questionnaire);
		return "questionnaire_design";
	}

	/**
	 * 预览问卷页面
	 */
	@GetMapping("/preview/{questionnaireId}")
	@RequiredLogin
	public String previewQuestionnairePage(@PathVariable Long questionnaireId, Model m) {
		final QuestionnaireDTO questionnaire = questionnaireService.getUserQuestionnaireDetail(questionnaireId);
		m.addAttribute("qa", questionnaire);
		return "questionnaire_view";
	}


	/**
	 * 保存问卷信息
	 */
	@PostMapping("/design/saveQuestionnaire")
	@RequiredLogin
	@ResponseBody
	public ResponseResult saveQuestionnaire(@RequestBody QuestionnaireDTO dto) {
		final String name = dto.getName();
		final String greeting = dto.getGreeting();
		if (!StringUtils.hasText(name) || null == dto.getId())
			return ResponseResult.error("name或id参数为空！", null);
		if (name.length() > 30)
			return ResponseResult.error("问卷名称太长，不能超过30个字符", null);
		if (greeting != null && greeting.length() > 100)
			return ResponseResult.error("欢迎语不能超过100个字符", null);
		return questionnaireService.saveQuestionnaire(dto);
	}

	/**
	 * 发布问卷
	 */
	@PostMapping("/design/publishQuestionnaire")
	@RequiredLogin
	@ResponseBody
	public ResponseResult publishQuestionnaire(@RequestParam Long questionnaireId) {
		return questionnaireService.publishQuestionnaire(questionnaireId);
	}

	/**
	 * 停止问卷答卷收集，将问卷状态设置为结束，也就是问卷收集完成
	 */
	@PostMapping("/stop")
	@RequiredLogin
	@ResponseBody
	public ResponseResult stopQuestionnaire(@RequestParam Long questionnaireId) {
		return questionnaireService.stopQuestionnaire(questionnaireId);
	}

	/**
	 * 删除问题
	 * @param questionId 问题id
	 */
	@PostMapping("/design/delQuestion")
	@RequiredLogin
	@ResponseBody
	public ResponseResult deleteQuestion(@RequestParam Long questionId) {
		questionnaireService.deleteQuestion(questionId);
		return ResponseResult.ok();
	}

	/**
	 * 删除问题选项
	 * @param optionId 选项id
	 */
	@PostMapping("/design/delOption")
	@RequiredLogin
	@ResponseBody
	public ResponseResult deleteQuestionOption(@RequestParam Long optionId) {
		questionnaireService.deleteQuestionOption(optionId);
		return ResponseResult.ok();
	}

	@RequiredLogin
	@GetMapping("/list")
	public String userQuestionnaireList(@RequestParam(defaultValue = "1") int page,
			@RequestParam(defaultValue = "10") int size, Model m) {
		List<Questionnaire> userQuestionnaireList = questionnaireService.getUserQuestionnaireList(page, size);
		m.addAttribute("list", userQuestionnaireList);
		return "questionnaire_list";
	}

	/**
	 * 删除问卷
	 * @param questionnaireId 问卷id
	 */
	@RequiredLogin
	@PostMapping("/delete")
	@ResponseBody
	public ResponseResult deleteQuestionnaire(@RequestParam Long questionnaireId) {
		return questionnaireService.delete(questionnaireId);
	}

	/**
	 * 分享问卷
	 * @param qId 问卷id
	 */
	@RequiredLogin
	@GetMapping("/share")
	public String shareQuestionnaire(@RequestParam(required = false) Long qId,
			HttpServletRequest request, Model m) {
		if (qId == null)
			return "questionnaire_share";
		String schema = request.getScheme();
		String serverName = request.getServerName();
		int port = request.getServerPort();
		String baseUrl;
		String shareUrl;
		if (("http".equals(schema) && port == 80) || ("https".equals(schema) && port == 443))
			baseUrl = schema + "://" + serverName;
		else
			baseUrl = schema + "://" + serverName + ":" + port;
		shareUrl = baseUrl + "/q/" + qId;
		Questionnaire questionnaire = questionnaireService.getUserQuestionnaire(qId);
		m.addAttribute("q", questionnaire);
		m.addAttribute("shareURL", shareUrl);
		m.addAttribute("baseURL", baseUrl);
		return "questionnaire_share";
	}

}
