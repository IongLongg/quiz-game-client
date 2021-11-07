package com.company.model;

import java.io.Serializable;

public class Answer implements Serializable {
    private String answer_id;
    private String answer_title;
    private boolean is_true;

    public Answer(String answer_title, boolean is_true) {
        this.answer_id = answer_id;
        this.answer_title = answer_title;
        this.is_true = is_true;
    }

    public String getAnswer_id() {
        return answer_id;
    }

    public void setAnswer_id(String answer_id) {
        this.answer_id = answer_id;
    }

    public String getAnswer_title() {
        return answer_title;
    }

    public void setAnswer_title(String answer_title) {
        this.answer_title = answer_title;
    }

    public boolean isIs_true() {
        return is_true;
    }

    public void setIs_true(boolean is_true) {
        this.is_true = is_true;
    }

}
