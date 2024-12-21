package com.elsa.learning.leaderboard.model;

public class AnswerRequest {
    private String questionUuid;
    private int answerOption;

    public String getQuestionUuid() {
        return questionUuid;
    }

    public void setQuestionUuid(String questionUuid) {
        this.questionUuid = questionUuid;
    }

    public int getAnswerOption() {
        return answerOption;
    }

    public void setAnswerOption(int answerOption) {
        this.answerOption = answerOption;
    }
}
