import javax.swing.*;
import javax.swing.table.DefaultTableModel;
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
    private JTable DataTable;
    private JScrollPane scrollPane;
    private JButton btnTableShow;

    Database d = new Database();

    public WeatherFrame() {
        setContentPane(mainPanel);
        setTitle("WeatherApp by MG & JS");
        setSize(1280,720);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        DefaultTableModel table = (DefaultTableModel) DataTable.getModel();
        //scrollPane.setViewportView(table);
        //JScrollPane scrollPane = new JScrollPane();

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
                table.setRowCount(0);
                tfCity.setText("");
            }
        });
        btnTableShow.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                List<DataPoint> DataPoints;

                if (tfCity.getText().isEmpty()) {
                    DataPoints = d.selectDataPoints();
                } else {
                    DataPoints = d.selectDataPointsByCity(tfCity.getText());
                }
                System.out.println("Data list: ");
                for(DataPoint p: DataPoints)
                    System.out.println((p));

                table.setRowCount(0);
                String[] columnNames = {"city", "temp [°C]", "wind [km/h]", "pressure [hPa]", "clouds [%]", "date"};
                DataTable.setModel(table);
                table.setColumnIdentifiers(columnNames);

                for (DataPoint dat : DataPoints) {
                    Object[] o = new Object[6];
                    o[0] = dat.city;
                    o[1] = dat.temp;
                    o[2] = dat.wind;
                    o[3] = dat.pressure;
                    o[4] = dat.clouds;
                    o[5] = Instant.ofEpochSecond(dat.dt + dat.timezone).toString();
                    table.addRow(o);
                }
            }
        });
    }

    public static void main(String[] args){
        WeatherFrame myFrame = new WeatherFrame();

    }

    private void createUIComponents(){
        imSun = new JLabel(new ImageIcon("temperature_128px.png"));
        imWindy = new JLabel(new ImageIcon("windy_128px.png"));
        imPressure = new JLabel(new ImageIcon("pressure_128px.png"));
        imCloudy= new JLabel(new ImageIcon("cloudy_128px.png"));
        //imPoland = new JLabel(new ImageIcon("poland_map.png"));
    }


}
