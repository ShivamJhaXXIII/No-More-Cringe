package com.shivam.no_more_cringe.model;

public class RoastResponse {
    private int cringeScore;
    private String roast;
    private String[] suggestion;

    public RoastResponse() {
    }

    public RoastResponse(int cringeScore, String roast, String[] suggestion) {
        this.cringeScore = cringeScore;
        this.roast = roast;
        this.suggestion = suggestion;
    }

    public int getCringeScore() {
        return cringeScore;
    }

    public void setCringeScore(int cringeScore) {
        this.cringeScore = cringeScore;
    }

    public String getRoast() {
        return roast;
    }

    public void setRoast(String roast) {
        this.roast = roast;
    }

    public String[] getSuggestion() {
        return suggestion;
    }

    public void setSuggestion(String[] suggestion) {
        this.suggestion = suggestion;
    }
}
