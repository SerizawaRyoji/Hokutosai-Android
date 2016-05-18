package com.hokutosai.hokutosai_android;

import java.io.Serializable;
import java.util.List;

/**
 * Created by ryoji on 2016/05/18.
 */
public class ReviewAssessment implements Serializable {

    int id;
    String name;
    Assessment my_assessment;
    AssessedScore assessment_aggregate;
    List<Assessment> assessments;

    public AssessedScore getAssessment_aggregate() {
        return assessment_aggregate;
    }

    public void setAssessment_aggregate(AssessedScore assessment_aggregate) {
        this.assessment_aggregate = assessment_aggregate;
    }

    public List<Assessment> getAssessments() {
        return assessments;
    }

    public void setAssessments(List<Assessment> assessments) {
        this.assessments = assessments;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Assessment getMy_assessment() {
        return my_assessment;
    }

    public void setMy_assessment(Assessment my_assessment) {
        this.my_assessment = my_assessment;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
