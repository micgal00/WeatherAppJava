import java.util.*;
import java.net.URL;
import java.net.HttpURLConnection;
import org.json.JSONObject;

class DataPoint {
    public int id;
    public String city;
    public double temp;
    public int pressure;
    public double wind;
    public double clouds;
    public int dt;
    public int timezone;

    DataPoint(int i, String ct, double t, int p, double w, double c, int d, int tz) {
        this.id = i;
        this.city = ct;
        this.temp = t;
        this.pressure = p;
        this.wind = w;
        this.clouds = c;
        this.dt = d;
        this.timezone = tz;
    }

    public String toString() { return String.format("%f %d %d", temp, pressure, dt); }
}

public class APIConnection {
    public DataPoint getAPIData(String city) {
        try {
            //Scanner input = new Scanner(System.in);
            //String city = input.nextLine();
            URL url = new URL("https://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=6a54ad3554c744c9e365b3e7858658dd&units=metric");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("Accept", "application/json");
            con.connect();

            StringBuilder str = new StringBuilder();
            try(Scanner in = new Scanner(con.getInputStream())) {
                while (in.hasNextLine()) {
                    str.append(in.nextLine());
                }
            }
            //System.out.println(str.toString());
            JSONObject jo = new JSONObject(str.toString());
            //System.out.println(jo.toString());

            JSONObject params = jo.getJSONObject("main");
            JSONObject wind = jo.getJSONObject("wind");
            JSONObject clouds = jo.getJSONObject("clouds");

            DataPoint dp = new DataPoint(
                    jo.getInt("id"),
                    city,
                    params.getDouble("temp"),
                    params.getInt("pressure"),
                    wind.getDouble("speed"),
                    clouds.getDouble("all"),
                    jo.getInt("dt"),
                    jo.getInt("timezone"));

            //System.out.println(dp);
            return dp;
        } catch (Exception e) {
            return new DataPoint(0,"not found",0,0,0, 0,0, 0);
        }
    }
}
