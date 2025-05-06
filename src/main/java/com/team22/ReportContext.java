package com.team22;

import java.util.List;

public class ReportContext {
    private ReportStrategy strategy;

    public ReportContext(ReportStrategy strategy) {
        this.strategy = strategy;
    }

    public void setStrategy(ReportStrategy strategy) {
        this.strategy = strategy;
    }

    public void runReport(List<SpaceObject> spaceObjects) {
        strategy.execute(spaceObjects);
    }
}
