package shashi.com.driving_style.models;

import java.util.List;

import shashi.com.smart_bus_tracking.models.Results;

public class GetLogResult {
    public boolean is_success;
    public String msg;
    public List<Logs> feedbacks;
    public List<Results> place;
    public List<Results> results;
}
