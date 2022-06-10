import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.Instant;
import java.util.List;

public class WeatherFrame extends JFrame {
    private JButton btnCityAccept;
    private JLabel imSun;
    private JTextField tfCity;
    private JLabel lbCity;
    private JLabel imWindy;
    private JPanel mainPanel;
    private JLabel imPressure;
    private JLabel imPoland;
    private JLabel imCloudy;
    private JLabel lbSunny;
    private JLabel lbWindy;
    private JLabel lbPressure;
    private JLabel lbCloudy;
    private JButton btnClear;

    Database d = new Database();

    public WeatherFrame() {
        setContentPane(mainPanel);
        setTitle("WeatherApp by MG & JS");
        setSize(1920,1080);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);

        btnCityAccept.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String city = tfCity.getText();

                APIConnection api = new APIConnection();

                DataPoint recieved = api.getAPIData(city);
                //System.out.println(recieved);
                String timeNowAsString = Instant.ofEpochSecond(recieved.dt + recieved.timezone).toString();

                System.out.println(timeNowAsString);

                lbSunny.setText(String.valueOf(recieved.temp) +" °C");
                lbWindy.setText(String.valueOf(recieved.wind) + " km/h");
                lbPressure.setText(String.valueOf(recieved.pressure) + " hPa");
                lbCloudy.setText(String.valueOf(recieved.clouds) + " %");
                d.insertDataPoint(recieved.city, recieved.temp, recieved.wind, recieved.clouds, recieved.pressure, recieved.dt, recieved.timezone);
            }
        });
        btnClear.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                lbSunny.setText("");
                lbWindy.setText("");
                lbPressure.setText("");
                lbCloudy.setText("");

                List<DataPoint> DataPoints = d.selectDataPoints();

                System.out.println("Data list: ");
                for(DataPoint p: DataPoints)
                    System.out.println((p));
            }
        });
    }

    public static void main(String[] args){
        WeatherFrame myFrame = new WeatherFrame();

    }

    private void createUIComponents(){
        imSun = new JLabel(new ImageIcon("sunny.png"));
        imWindy = new JLabel(new ImageIcon("windy.png"));
        imPressure = new JLabel(new ImageIcon("pressure.png"));
        imCloudy= new JLabel(new ImageIcon("cloudy.png"));
        imPoland = new JLabel(new ImageIcon("poland_map.png"));
    }


}
