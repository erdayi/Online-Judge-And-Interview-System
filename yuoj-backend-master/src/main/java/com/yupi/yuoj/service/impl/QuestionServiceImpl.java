package com.yupi.yuoj.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yupi.yuoj.common.ErrorCode;
import com.yupi.yuoj.constant.CommonConstant;
import com.yupi.yuoj.exception.BusinessException;
import com.yupi.yuoj.exception.ThrowUtils;

import com.yupi.yuoj.manager.AiManager;
import com.yupi.yuoj.mapper.QuestionFavourMapper;
import com.yupi.yuoj.mapper.QuestionMapper;
import com.yupi.yuoj.model.dto.question.QuestionEsDTO;
import com.yupi.yuoj.model.dto.question.JudgeConfig;
import com.yupi.yuoj.model.dto.question.QuestionQueryRequest;
import com.yupi.yuoj.model.entity.Question;
import com.yupi.yuoj.model.entity.QuestionFavour;
import com.yupi.yuoj.model.entity.User;
import com.yupi.yuoj.model.vo.QuestionVO;
import com.yupi.yuoj.model.vo.UserVO;
import com.yupi.yuoj.service.QuestionService;
import com.yupi.yuoj.service.UserService;
import com.yupi.yuoj.utils.FormatCodeUtils;
import com.yupi.yuoj.utils.SqlUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.sort.SortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author 李鱼皮
 * @description 针对表【question(题目)】的数据库操作Service实现
 * @createDate 2023-08-07 20:58:00
 */
@Service
public class QuestionServiceImpl extends ServiceImpl<QuestionMapper, Question>
        implements QuestionService {

    @Resource
    private UserService userService;

    @Resource
    private QuestionFavourMapper questionFavourMapper;

    @Resource
    private AiManager aiManager;

    // 注入 FormatCodeUtils 依赖
    @Resource
    private FormatCodeUtils formatCodeUtils;

    @Resource
    private ElasticsearchRestTemplate elasticsearchRestTemplate;

    /**
     * 校验题目是否合法
     *
     * @param question
     * @param add
     */
    @Override
    public void validQuestion(Question question, boolean add) {
        if (question == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String title = question.getTitle();
        String content = question.getContent();
        String tags = question.getTags();
        String answer = question.getAnswer();
        String judgeCase = question.getJudgeCase();
        String judgeConfig = question.getJudgeConfig();
        // 创建时，参数不能为空
        if (add) {
            ThrowUtils.throwIf(StringUtils.isAnyBlank(title, content, tags), ErrorCode.PARAMS_ERROR);
        }
        // 有参数则校验
        if (StringUtils.isNotBlank(title) && title.length() > 80) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "标题过长");
        }
        if (StringUtils.isNotBlank(content) && content.length() > 8192) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "内容过长");
        }
        if (StringUtils.isNotBlank(answer) && answer.length() > 8192) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "答案过长");
        }
        if (StringUtils.isNotBlank(judgeCase) && judgeCase.length() > 8192) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "判题用例过长");
        }
        if (StringUtils.isNotBlank(judgeConfig) && judgeConfig.length() > 8192) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "判题配置过长");
        }
    }

    /**
     * 获取查询包装类（用户根据哪些字段查询，根据前端传来的请求对象，得到 mybatis 框架支持的查询 QueryWrapper 类）
     *
     * @param questionQueryRequest
     * @return
     */
    @Override
    public QueryWrapper<Question> getQueryWrapper(QuestionQueryRequest questionQueryRequest) {
        QueryWrapper<Question> queryWrapper = new QueryWrapper<>();
        if (questionQueryRequest == null) {
            return queryWrapper;
        }
        Long id = questionQueryRequest.getId();
        String title = questionQueryRequest.getTitle();
        String content = questionQueryRequest.getContent();
        List<String> tags = questionQueryRequest.getTags();
        String answer = questionQueryRequest.getAnswer();
        Long userId = questionQueryRequest.getUserId();
        String sortField = questionQueryRequest.getSortField();
        String sortOrder = questionQueryRequest.getSortOrder();

        // 拼接查询条件
        queryWrapper.like(StringUtils.isNotBlank(title), "title", title);
        queryWrapper.like(StringUtils.isNotBlank(content), "content", content);
        queryWrapper.like(StringUtils.isNotBlank(answer), "answer", answer);
        if (CollectionUtils.isNotEmpty(tags)) {
            for (String tag : tags) {
                queryWrapper.like("tags", "\"" + tag + "\"");
            }
        }
        queryWrapper.eq(ObjectUtils.isNotEmpty(id), "id", id);
        queryWrapper.eq(ObjectUtils.isNotEmpty(userId), "userId", userId);
        queryWrapper.eq("isDelete", false);
        queryWrapper.orderBy(SqlUtils.validSortField(sortField), sortOrder.equals(CommonConstant.SORT_ORDER_ASC),
                sortField);
        return queryWrapper;
    }

    /**
     * 从 ES 查询题目
     *
     * @param questionQueryRequest
     * @return
     */
    @Override
    public Page<Question> searchFromEs(QuestionQueryRequest questionQueryRequest) {
        Long id = questionQueryRequest.getId();
        Long notId = questionQueryRequest.getNotId();
        String searchText = questionQueryRequest.getSearchText();
        String title = questionQueryRequest.getTitle();
        String content = questionQueryRequest.getContent();
        List<String> tagList = questionQueryRequest.getTags();
        List<String> orTagList = questionQueryRequest.getOrTags();
        Long userId = questionQueryRequest.getUserId();
        // es 起始页为 0
        long current = questionQueryRequest.getCurrent() - 1;
        long pageSize = questionQueryRequest.getPageSize();
        String sortField = questionQueryRequest.getSortField();
        String sortOrder = questionQueryRequest.getSortOrder();

        // 构造查询条件 使用的是复杂的 ElasticsearchTemplate 前方引用
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        // 过滤
        boolQueryBuilder.filter(QueryBuilders.termQuery("isDelete", 0));
        if (id != null) {
            boolQueryBuilder.filter(QueryBuilders.termQuery("id", id));
        }
        if (notId != null) {
            boolQueryBuilder.mustNot(QueryBuilders.termQuery("id", notId));
        }
        if (userId != null) {
            boolQueryBuilder.filter(QueryBuilders.termQuery("userId", userId));
        }
        // 必须包含所有标签
        if (CollectionUtils.isNotEmpty(tagList)) {
            for (String tag : tagList) {
                boolQueryBuilder.filter(QueryBuilders.termQuery("tags", tag));
            }
        }
        // 包含任何一个标签即可
        if (CollectionUtils.isNotEmpty(orTagList)) {
            BoolQueryBuilder orTagBoolQueryBuilder = QueryBuilders.boolQuery();
            for (String tag : orTagList) {
                orTagBoolQueryBuilder.should(QueryBuilders.termQuery("tags", tag));
            }
            orTagBoolQueryBuilder.minimumShouldMatch(1);
            boolQueryBuilder.filter(orTagBoolQueryBuilder);
        }
        // 按关键词检索
        if (StringUtils.isNotBlank(searchText)) {
            // should 类似数据库的 or 只要有一个满足即可
            boolQueryBuilder.should(QueryBuilders.matchQuery("title", searchText));
            boolQueryBuilder.should(QueryBuilders.matchQuery("description", searchText));
            boolQueryBuilder.should(QueryBuilders.matchQuery("content", searchText));
            boolQueryBuilder.minimumShouldMatch(1);
        }
        // 按标题检索
        if (StringUtils.isNotBlank(title)) {
            boolQueryBuilder.should(QueryBuilders.matchQuery("title", title));
            boolQueryBuilder.minimumShouldMatch(1);
        }
        // 按内容检索
        if (StringUtils.isNotBlank(content)) {
            boolQueryBuilder.should(QueryBuilders.matchQuery("content", content));
            boolQueryBuilder.minimumShouldMatch(1);
        }
        // 排序
        SortBuilder<?> sortBuilder = SortBuilders.scoreSort();
        if (StringUtils.isNotBlank(sortField)) {
            sortBuilder = SortBuilders.fieldSort(sortField);
            sortBuilder.order(CommonConstant.SORT_ORDER_ASC.equals(sortOrder) ? SortOrder.ASC : SortOrder.DESC);
        }
        // 分页
        PageRequest pageRequest = PageRequest.of((int) current, (int) pageSize);
        // 构造查询
        NativeSearchQuery searchQuery = new NativeSearchQueryBuilder().withQuery(boolQueryBuilder)
                .withPageable(pageRequest).withSorts(sortBuilder).build();
        SearchHits<QuestionEsDTO> searchHits = elasticsearchRestTemplate.search(searchQuery, QuestionEsDTO.class);
        // 复用 MySQL / MyBatis Plus 分页对象 封装返回结果
        Page<Question> page = new Page<>();
        page.setTotal(searchHits.getTotalHits());
        List<Question> resourceList = new ArrayList<>();
        // 查出结果后，从 db 获取最新动态数据（比如点赞数）
        if (searchHits.hasSearchHits()) {
            List<SearchHit<QuestionEsDTO>> searchHitList = searchHits.getSearchHits();
            for (SearchHit<QuestionEsDTO> questionEsDTOSearchHit : searchHitList) {
                resourceList.add(QuestionEsDTO.dtoToObj(questionEsDTOSearchHit.getContent()));
            }
        }
        page.setRecords(resourceList);
        return page;
    }

    @Override
    public QuestionVO getQuestionVO(Question question, HttpServletRequest request) {
        QuestionVO questionVO = QuestionVO.objToVo(question);
        // 1. 关联查询用户信息
        Long userId = question.getUserId();
        User user = null;
        if (userId != null && userId > 0) {
            user = userService.getById(userId);
        }
        UserVO userVO = userService.getUserVO(user);
        questionVO.setUserVO(userVO);
        // 2. 已登录，获取用户收藏状态
        User loginUser = userService.getLoginUserPermitNull(request);
        if (loginUser != null && question.getId() != null) {
            QueryWrapper<QuestionFavour> questionFavourQueryWrapper = new QueryWrapper<>();
            questionFavourQueryWrapper.eq("questionId", question.getId());
            questionFavourQueryWrapper.eq("userId", loginUser.getId());
            QuestionFavour questionFavour = questionFavourMapper.selectOne(questionFavourQueryWrapper);
            questionVO.setHasFavour(questionFavour != null);
        }
        return questionVO;
    }

    @Override
    public Page<QuestionVO> getQuestionVOPage(Page<Question> questionPage, HttpServletRequest request) {
        List<Question> questionList = questionPage.getRecords();
        Page<QuestionVO> questionVOPage = new Page<>(questionPage.getCurrent(), questionPage.getSize(), questionPage.getTotal());
        if (CollectionUtils.isEmpty(questionList)) {
            return questionVOPage;
        }
        // 1. 关联查询用户信息
        Set<Long> userIdSet = questionList.stream().map(Question::getUserId).collect(Collectors.toSet());
        Map<Long, List<User>> userIdUserListMap = userService.listByIds(userIdSet).stream()
                .collect(Collectors.groupingBy(User::getId));
        // 2. 已登录，获取用户收藏状态
        User loginUser = userService.getLoginUserPermitNull(request);
        Map<Long, Boolean> questionIdHasFavourMap = new HashMap<>();
        if (loginUser != null) {
            Set<Long> questionIdSet = questionList.stream().map(Question::getId).collect(Collectors.toSet());
            QueryWrapper<com.yupi.yuoj.model.entity.QuestionFavour> questionFavourQueryWrapper = new QueryWrapper<>();
            questionFavourQueryWrapper.in("questionId", questionIdSet);
            questionFavourQueryWrapper.eq("userId", loginUser.getId());
            List<com.yupi.yuoj.model.entity.QuestionFavour> questionFavourList = questionFavourMapper.selectList(questionFavourQueryWrapper);
            questionFavourList.forEach(questionFavour -> questionIdHasFavourMap.put(questionFavour.getQuestionId(), true));
        }
        // 填充信息
        List<QuestionVO> questionVOList = questionList.stream().map(question -> {
            QuestionVO questionVO = QuestionVO.objToVo(question);
            Long userId = question.getUserId();
            User user = null;
            if (userIdUserListMap.containsKey(userId)) {
                user = userIdUserListMap.get(userId).get(0);
            }
            questionVO.setUserVO(userService.getUserVO(user));
            questionVO.setHasFavour(questionIdHasFavourMap.getOrDefault(question.getId(), false));
            return questionVO;
        }).collect(Collectors.toList());
        questionVOPage.setRecords(questionVOList);
        return questionVOPage;
    }


    /**
     * AI 生成题目
     *
     * @param questionType 题目类型 例如 java
     * @param number       题目数量 例如 10
     * @param user         创建人
     * @return true or false
     */
    @Override
    public boolean aiGenerateQuestion(String questionType, int number, User user) {
        // 1. 入参校验（强化边界值）
        if (ObjectUtil.hasEmpty(questionType, user) || number <= 0 || number > 50) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "参数错误：题目类型不能为空、数量需1-50道、用户信息不能为空");
        }

        // 查询同类型的已存在题目标题
        Set<String> existingTitles = getExistingQuestionTitlesByType(questionType);

        // 2. 优化 Prompt（明确JSON字段类型，减少解析误差，要求代码格式良好）
        String systemPrompt = "你是专业程序员出题专家，生成{{数量}}道{{题目类型}}编程题，严格遵循以下要求：\n" +
                "1. 输出格式：JSON数组，每道题包含以下字段（字段类型严格匹配）：\n" +
                "   - title：字符串（10-50字，不重复）\n" +
                "   - content：字符串（详细题目描述，50-500字）\n" +
                "   - tags：字符串数组（至少包含{{题目类型}}，可加难度标签如[\"java\",\"简单\"]）\n" +
                "   - answer：字符串（完整可运行的{{题目类型}}代码，包含类/函数定义，代码必须有适当的缩进和换行，格式良好）\n" + // 强调格式要求
                "   - judgeCase：对象数组（至少2组，input为空格分隔的输入字符串，output为字符串格式结果）\n" +
                "   - judgeConfig：对象（timeLimit：int(1000-5000)毫秒，memoryLimit：int(128-512)MB，stackLimit：int(1024-8192)KB）\n" +
                "2. 示例（java题目）：\n" +
                "[{\n" +
                "  \"title\": \"两数之和\",\n" +
                "  \"content\": \"给定两个整数a和b，编写程序计算它们的和并输出结果\",\n" +
                "  \"tags\": [\"java\",\"简单\"],\n" +
                "  \"answer\": \"public class Main {\\n    public static void main(String[] args) {\\n        int a = Integer.parseInt(args[0]);\\n        int b = Integer.parseInt(args[1]);\\n        System.out.println(a + b);\\n    }\\n}\",\n" + // 格式良好的代码示例
                "  \"judgeCase\": [{\"input\":\"1 2\",\"output\":\"3\"},{\"input\":\"-1 5\",\"output\":\"4\"}],\n" +
                "  \"judgeConfig\": {\"timeLimit\":2000,\"memoryLimit\":256,\"stackLimit\":4096}\n" +
                "}]\n" +
                "3. 禁止输出任何多余内容（无注释、无说明、无换行外的多余字符），仅返回JSON数组，不要生成计算圆的面积";

        // 3. 拼接用户Prompt
        String userPrompt = String.format("生成%d道%s编程题目，确保标题在%s类型中唯一不重复", number, questionType, questionType);

        List<Question> questionList = new ArrayList<>();
        int retryCount = 0;
        final int MAX_RETRY = 3;
        int remaining = number;

        while (remaining > 0 && retryCount < MAX_RETRY) {
            try {
                // 4. 调用AI生成题目
                String aiResponse = aiManager.doChat(systemPrompt, userPrompt);
                ThrowUtils.throwIf(StringUtils.isBlank(aiResponse) || aiResponse.length() < 10,
                        ErrorCode.SYSTEM_ERROR, "AI生成结果为空或无效");

                // 5. 数据预处理
                aiResponse = aiResponse.trim().replaceAll("[\\u0000-\\u001F\\u007F]", "");

                // 处理JSON包装
                if (aiResponse.contains("```json") || aiResponse.contains("```")) {
                    int startIdx = aiResponse.indexOf('[');
                    int endIdx = aiResponse.lastIndexOf(']');
                    if (startIdx != -1 && endIdx != -1 && endIdx > startIdx) {
                        aiResponse = aiResponse.substring(startIdx, endIdx + 1);
                    }
                }

                // 解析JSON
                JSONArray jsonArray = JSONUtil.parseArray(aiResponse);

                // 处理当前批次题目
                List<Question> currentBatch = processQuestionBatch(jsonArray, questionType, user, existingTitles);

                if (!currentBatch.isEmpty()) {
                    questionList.addAll(currentBatch);

                    // 更新已存在标题集合
                    currentBatch.forEach(q -> existingTitles.add(q.getTitle()));

                    remaining = number - questionList.size();


                    if (remaining > 0) {
                        // 更新提示词，包含已存在的标题
                        systemPrompt = String.format(
                                "还需要生成%d道%s编程题，题目标题必须在%s类型中唯一。\n" +
                                        "以下标题已存在，请避免重复：%s",
                                remaining, questionType, questionType,
                                String.join("、", existingTitles.stream().limit(20).collect(Collectors.toList()))
                        );
                        userPrompt = String.format("继续生成%d道%s题目，避免重复", remaining, questionType);
                    }
                }

                retryCount++;

            } catch (Exception e) {
                retryCount++;
                if (retryCount >= MAX_RETRY && questionList.isEmpty()) {
                    throw new BusinessException(ErrorCode.SYSTEM_ERROR, "生成题目失败，请重试");
                }
            }
        }

        // 6. 批量保存到数据库
        if (!questionList.isEmpty()) {
            // 保存前再次去重检查（防止并发情况）
            List<Question> finalQuestions = removeDuplicatesBeforeSave(questionList, questionType);

            if (!finalQuestions.isEmpty()) {
                boolean saveSuccess = this.saveBatch(finalQuestions, Math.min(finalQuestions.size(), 10));
                ThrowUtils.throwIf(!saveSuccess, ErrorCode.OPERATION_ERROR, "题目保存失败");
                return true;
            }
        }

        return false;
    }

    /**
     * 只查询同类型的已存在题目标题
     */
    private Set<String> getExistingQuestionTitlesByType(String questionType) {
        try {
            // 方法1：如果数据库有专门的类型字段
            // QueryWrapper<Question> queryWrapper = new QueryWrapper<>();
            // queryWrapper.eq("question_type", questionType);
            // queryWrapper.select("title");

            // 方法2：通过tags字段判断（更通用）
            QueryWrapper<Question> queryWrapper = new QueryWrapper<>();
            queryWrapper.like("tags", "\"" + questionType + "\""); // 精确匹配JSON中的字符串
            queryWrapper.select("title");

            List<Question> existingQuestions = this.list(queryWrapper);
            Set<String> titles = existingQuestions.stream()
                    .map(Question::getTitle)
                    .collect(Collectors.toSet());

            return titles;
        } catch (Exception e) {
            return new HashSet<>();
        }
    }

    /**
     * 处理题目批次，进行同类型去重
     */
    private List<Question> processQuestionBatch(JSONArray jsonArray, String questionType, User user, Set<String> existingTitles) {
        List<Question> validQuestions = new ArrayList<>();
        Set<String> batchTitles = new HashSet<>(); // 用于批次内去重

        for (int i = 0; i < jsonArray.size(); i++) {
            try {
                JSONObject jsonObj = jsonArray.getJSONObject(i);
                String title = jsonObj.getStr("title");

                if (title == null || title.trim().isEmpty()) {
                    log.warn("跳过无标题的题目");
                    continue;
                }

                // 标准化标题（去除首尾空格）
                title = title.trim();

                // 检查重复：数据库中存在或批次内重复
                if (existingTitles.contains(title)) {
                    log.warn("跳过与数据库重复的题目");
                    continue;
                }

                if (batchTitles.contains(title)) {
                    log.warn("跳过分批内重复的题目");
                    continue;
                }

                Question question = buildQuestionFromJson(jsonObj, questionType, user);
                if (question != null) {
                    validQuestions.add(question);
                    batchTitles.add(title);
                }

            } catch (Exception e) {
                log.error("处理第道题目失败");
            }
        }

        return validQuestions;
    }

    /**
     * 保存前再次去重检查
     */
    private List<Question> removeDuplicatesBeforeSave(List<Question> questions, String questionType) {
        // 查询当前数据库中的最新状态
        Set<String> currentTitles = getExistingQuestionTitlesByType(questionType);

        List<Question> uniqueQuestions = new ArrayList<>();
        Set<String> processedTitles = new HashSet<>();

        for (Question question : questions) {
            String title = question.getTitle();
            if (title == null) continue;

            title = title.trim();

            if (currentTitles.contains(title) || processedTitles.contains(title)) {
                log.warn("保存前再次去重：跳过重复题目");
                continue;
            }

            uniqueQuestions.add(question);
            processedTitles.add(title);
        }

        return uniqueQuestions;
    }

    /**
     * 从JSON构建题目对象
     */
    private Question buildQuestionFromJson(JSONObject jsonObj, String questionType, User user) {
        try {
            Question question = new Question();

            // 基础字段
            question.setTitle(jsonObj.getStr("title"));
            question.setContent(jsonObj.getStr("content"));

            // 格式化代码
            String rawAnswer = jsonObj.getStr("answer");
            question.setAnswer(formatCodeUtils.formatCode(rawAnswer, questionType));

            // 标签处理
            JSONArray tagsJson = jsonObj.getJSONArray("tags");
            List<String> tagsList = tagsJson.stream().map(Object::toString).collect(Collectors.toList());

            // 确保包含编程语言标签
            if (tagsList.stream().noneMatch(tag -> tag.equalsIgnoreCase(questionType))) {
                tagsList.add(questionType);
            }

            // 添加AI生成标识
            if (tagsList.stream().noneMatch("AI"::equalsIgnoreCase)) {
                tagsList.add("AI");
            }
            question.setTags(JSONUtil.toJsonStr(tagsList));

            // 判题用例
            JSONArray judgeCaseJson = jsonObj.getJSONArray("judgeCase");
            ThrowUtils.throwIf(judgeCaseJson.size() < 2,
                    ErrorCode.SYSTEM_ERROR, "测试用例不足2组");
            question.setJudgeCase(JSONUtil.toJsonStr(judgeCaseJson));

            // 判题配置
            JSONObject judgeConfigJson = jsonObj.getJSONObject("judgeConfig");
            int timeLimit = Math.max(judgeConfigJson.getInt("timeLimit", 2000), 1000);
            int memoryLimit = Math.max(judgeConfigJson.getInt("memoryLimit", 256), 128);
            int stackLimit = Math.max(judgeConfigJson.getInt("stackLimit", 4096), 1024);

            JudgeConfig judgeConfig = new JudgeConfig();
            judgeConfig.setTimeLimit((long) timeLimit);
            judgeConfig.setMemoryLimit((long) memoryLimit);
            judgeConfig.setStackLimit((long) stackLimit);
            question.setJudgeConfig(JSONUtil.toJsonStr(judgeConfig));

            // 系统字段
            question.setUserId(user.getId());
            question.setCreateTime(new Date());
            question.setUpdateTime(new Date());
            question.setIsDelete(0);

            // 验证题目
            validQuestion(question, true);

            return question;
        } catch (Exception e) {
            log.error("构建题目对象失败");
            return null;
        }
    }
}




