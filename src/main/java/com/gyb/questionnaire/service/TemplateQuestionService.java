package com.gyb.questionnaire.service;

import com.gyb.questionnaire.dao.TemplateQuestionDao;
import com.gyb.questionnaire.dao.TemplateQuestionOptionDao;
import com.gyb.questionnaire.dto.TemplateQuestionDTO;
import com.gyb.questionnaire.entity.TemplateQuestion;
import com.gyb.questionnaire.entity.TemplateQuestionOption;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
@Slf4j
@Transactional
public class TemplateQuestionService {
    private final TemplateQuestionDao questionDao;
    private final TemplateQuestionOptionDao questionOptionDao;

    @Autowired
    public TemplateQuestionService(TemplateQuestionDao questionDao,
                                   TemplateQuestionOptionDao questionOptionDao) {
        this.questionDao = questionDao;
        this.questionOptionDao = questionOptionDao;
    }

    /**
     * 添加“问题”
     * @param templateQuestion “问题”实体
     * @return 新添加的“问题”id
     */
    public int addQuestion(TemplateQuestion templateQuestion){
        return questionDao.add(templateQuestion);
    }

    /**
     * 根据id，获取“问题”
     * @param id “问题id”
     * @return “问题”实体
     */
    public TemplateQuestion getById(int id){
        return questionDao.get(id);
    }

    /**
     * 获取“访谈记录模板”下的所有问题
     * @param templateId 访谈记录模板id
     * @return “问题”List
     */
    public List<TemplateQuestion> getByTemplateId(long templateId){
        return questionDao.getByTemplateId(templateId);
    }

    /**
     * 封装模板下的“问题”DTO
     * @param templateId 模板id
     * @return QuestionDTO
     */
    public List<TemplateQuestionDTO> getDetailByTemplate(long templateId){
        List<TemplateQuestionDTO> list = null;
        List<TemplateQuestion> questions = getByTemplateId(templateId);
        if(questions !=null){
            list = new ArrayList<>();
            for(TemplateQuestion q:questions){
                TemplateQuestionDTO questionDTO = new TemplateQuestionDTO();
                questionDTO.setId(q.getId());
                questionDTO.setTemplateId(templateId);
                questionDTO.setQuestionOrder(q.getQuestionOrder());
                questionDTO.setQuestionNum(q.getQuestionNum());
                questionDTO.setQuestionTitle(q.getQuestionTitle());
                questionDTO.setQuestionType(q.getQuestionType());
                questionDTO.setInputPlaceholder(q.getInputPlaceholder());
                List<TemplateQuestionOption> questionOptions = questionOptionDao.getByQuestionId(q.getId());
                questionOptions.sort(Comparator.comparing(TemplateQuestionOption::getOptionOrder)); //按照“问题选项”顺序排序
                questionDTO.setQuestionOptions(questionOptions);
                list.add(questionDTO);
            }
            //第一种排序：
            /*Collections.sort(list,(l1,l2)->
                Integer.compare(l1.getQuestionOrder(),l2.getQuestionOrder()));*/
            //第二种排序（简化）：
            //Collections.sort(list, Comparator.comparing(QuestionDTO::getQuestionOrder));
            //第三种排序(进一步简化)：
            list.sort(Comparator.comparing(TemplateQuestionDTO::getQuestionOrder)); //按照“问题顺序”排序
        }
        return list;
    }

    /**
     * 更新“问题”
     * @param q 问题实体
     */
    public void updateQuestion(TemplateQuestion q){
        int n = questionDao.update(q);
        if(n < 1)
            log.warn("更新问题失败！id="+q.getId());
    }

    /**
     * 通过问题id删除“问题”
     * @param id “问题”id
     */
    public void deleteQuestionById(long id){
        //先删除“问题选项”
        questionOptionDao.deleteByQuestionId(id);
        //再删除该“问题”
        questionDao.delete(id);
    }

    /**
     * 根据访谈记录模板id，删除问题
     * @param templateId 访谈记录模板id
     * @return 删除的条数
     */
    public int deleteQuestionByTemplateId(long templateId){
        List<TemplateQuestion> questions = questionDao.getByTemplateId(templateId);
        if(questions != null && !questions.isEmpty()){
            for(TemplateQuestion q:questions){ //删除每个问题下的“选项”
                questionOptionDao.deleteByQuestionId(q.getId());
            }
        }
        return questionDao.deleteByTemplateId(templateId);//删除该访谈记录下所有的“问题”。
    }
}
