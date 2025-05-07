package com.team22;

import java.util.List;

/**
 * Concrete implementation of the {@link ReportStrategy} interface.
 * This strategy runs the orbit assessment procedure, which evaluates
 * whether each space object is still in orbit and classifies risk levels.
 * Results are saved to CSV and TXT files.
 */
public class OrbitAssessmentStrategy implements ReportStrategy {

    /**
     * Executes the orbit assessment report by invoking {@link OrbitAssessment#assessAndSave()}
     * on the provided list of space objects.
     *
     * @param spaceObjects list of {@link SpaceObject} instances to analyze
     */
    @Override
    public void execute(List<SpaceObject> spaceObjects) {
        OrbitAssessment assessment = new OrbitAssessment(spaceObjects);
        assessment.assessAndSave();
    }
}
