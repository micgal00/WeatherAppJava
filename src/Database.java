import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class Database {
    public static final String DRIVER = "org.sqlite.JDBC";
    public static final String DB_URL = "jdbc:sqlite:pogoda.db";

    private Connection conn;
    private Statement stat;

    public Database() {
        try {
            Class.forName(Database.DRIVER);
        } catch (ClassNotFoundException e) {
            System.err.println("Brak sterownika JDBC");
            e.printStackTrace();
        }

        try {
            conn = DriverManager.getConnection(DB_URL);
            stat = conn.createStatement();
        } catch (SQLException e) {
            System.err.println("Problem z otwarciem polaczenia");
            e.printStackTrace();
        }

        createTables();
    }

    public boolean createTables()  {
        String createDataPoints = "CREATE TABLE IF NOT EXISTS DataPoints (id_datapoint INTEGER PRIMARY KEY AUTOINCREMENT, city varchar(255), temperature double, windspeed double, clouds double, pressure int, time int, timezone int)";

        try {
            stat.execute(createDataPoints);
        } catch (SQLException e) {
            System.err.println("Blad przy tworzeniu tabeli");
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean insertDataPoint(String city, double temp, double windspeed, double clouds, int pressure, int time, int timezone) {
        try {
            PreparedStatement prepStmt = conn.prepareStatement(
                    "insert into DataPoints values (NULL, ?, ?, ?, ?, ?, ?, ?);");
            prepStmt.setString(1, city);
            prepStmt.setDouble(2, temp);
            prepStmt.setDouble(3, windspeed);
            prepStmt.setDouble(4, clouds);
            prepStmt.setInt(5, pressure);
            prepStmt.setInt(6, time);
            prepStmt.setInt(7, timezone);
            prepStmt.execute();
        } catch (SQLException e) {
            System.err.println("Blad przy wstawianiu czytelnika");
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public List<DataPoint> selectDataPoints() {
        List<DataPoint> DataPoints = new LinkedList<DataPoint>();
        try {
            ResultSet result = stat.executeQuery("SELECT * FROM DataPoints");
            int id;
            String city;
            double temp, windspeed, clouds;
            int pressure, time, timezone;
            while(result.next()) {
                id = result.getInt("id_datapoint");
                city = result.getString("city");
                temp = result.getDouble("temperature");
                windspeed = result.getDouble("windspeed");
                clouds = result.getDouble("clouds");
                pressure = result.getInt("pressure");
                time = result.getInt("time");
                timezone = result.getInt("timezone");
                DataPoints.add(new DataPoint(id, city, temp, pressure, windspeed, clouds, time, timezone));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        return DataPoints;
    }

    public List<DataPoint> selectDataPointsByCity(String cityName) {
        List<DataPoint> DataPoints = new LinkedList<DataPoint>();
        try {
            ResultSet result = stat.executeQuery("SELECT * FROM DataPoints WHERE city = '" + cityName + "'");
            int id;
            String city;
            double temp, windspeed, clouds;
            int pressure, time, timezone;
            while(result.next()) {
                id = result.getInt("id_datapoint");
                city = result.getString("city");
                temp = result.getDouble("temperature");
                windspeed = result.getDouble("windspeed");
                clouds = result.getDouble("clouds");
                pressure = result.getInt("pressure");
                time = result.getInt("time");
                timezone = result.getInt("timezone");
                DataPoints.add(new DataPoint(id, city, temp, pressure, windspeed, clouds, time, timezone));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        return DataPoints;
    }
}
