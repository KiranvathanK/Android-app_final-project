package shashi.com.smart_bus_tracking.models;

public class Results {
    public String id;
    public String destination;
    public String source_lat;
    public String source_lng;
    public String humbs_count;
    public String time_taken;
    public String avg_speed;
    public String max_speed;
    public String datee;

    public String getDestination() {
        return destination;
    }

    public String getSource_lat() {
        return source_lat;
    }

    public String getSource_lng() {
        return source_lng;
    }

    public String getHumbs_count() {
        return humbs_count;
    }

    public String getTime_taken() {
        return time_taken;
    }

    public String getAvg_speed() {
        return avg_speed;
    }

    public String getMax_speed() {
        return max_speed;
    }

    public String getDatee() {
        return datee;
    }

    public String getId() {
        return id;
    }
}
//
//  "id": "3",
//          "user_id": "2",
//          "destination": "Chitradurga",
//          "source_lat": "13.0329127",
//          "source_lng": "77.5606138",
//          "dest_lat": "14.1823",
//          "dest_lng": "76.5488",
//          "humbs_count": "0",
//          "time_taken": "20:41",
//          "avg_speed": "0.29723505229522157",
//          "max_speed": "15.623998641967773",
//          "datee": "2019-05-11",
//          "created_at": "2019-05-11 20:41:45",
//          "status": "1"