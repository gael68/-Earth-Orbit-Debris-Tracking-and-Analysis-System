package com.team22;

import java.util.List;

public class ImpactReportStrategy implements ReportStrategy {
    @Override
    public void execute(List<SpaceObject> spaceObjects) {
        ImpactAnalysis analysis = new ImpactAnalysis(spaceObjects);
        analysis.analyzeLongTermImpact();
        analysis.generateDensityReport();
    }
}
