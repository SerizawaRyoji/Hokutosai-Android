package com.hokutosai.hokutosai_android;

import java.io.Serializable;

/**
 * Created by ryoji on 2016/05/02.
 */
public class AssessedScore  implements Serializable {

    private int assessed_count;
    private int total_score;

    public int getAssessed_count() {
        return assessed_count;
    }

    public void setAssessed_count(int assessed_count) {
        this.assessed_count = assessed_count;
    }

    public int getTotal_score() {
        return total_score;
    }

    public void setTotal_score(int total_score) {
        this.total_score = total_score;
    }
}
