package com.team22;

import java.util.List;

public class OrbitAssessmentStrategy implements ReportStrategy {
    @Override
    public void execute(List<SpaceObject> spaceObjects) {
        OrbitAssessment assessment = new OrbitAssessment(spaceObjects);
        assessment.assessAndSave();
    }
}
