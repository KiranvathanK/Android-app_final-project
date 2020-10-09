package shashi.com.driving_style.models;

public class AddResultInput {
    public  String uid;
    public  String 	destination;
    public  String source_lat;
    public  String source_lng;
    public  String dest_lat;
    public  String dest_lng;
    public  String humbs_count;
    public  String time_taken;
    public  String avg_speed;
    public  String max_speed;
    public  String datee;
    public  String loc;


    public String user_id;
    public String user_name;
    public String src;
    public String dest;
    public String humps;
    public String traffic;
    public String road_condn;

    public AddResultInput(String user_id, String user_name, String src, String dest, String humps, String traffic, String road_condn) {
        this.user_id = user_id;
        this.user_name = user_name;
        this.src = src;
        this.dest = dest;
        this.humps = humps;
        this.traffic = traffic;
        this.road_condn = road_condn;
    }

    public AddResultInput(String uid, String destination, String source_lat, String source_lng, String dest_lat, String dest_lng, String humbs_count, String time_taken, String avg_speed, String max_speed, String datee) {
        this.uid = uid;
        this.destination = destination;
        this.source_lat = source_lat;
        this.source_lng = source_lng;
        this.dest_lat = dest_lat;
        this.dest_lng = dest_lng;
        this.humbs_count = humbs_count;
        this.time_taken = time_taken;
        this.avg_speed = avg_speed;
        this.max_speed = max_speed;
        this.datee = datee;
    }

    public AddResultInput(String loc) {
        this.loc = loc;
    }
}
