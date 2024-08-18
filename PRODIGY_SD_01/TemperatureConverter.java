import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class TemperatureConverter extends JFrame implements ActionListener{
	private JLabel resultLabel1;
	private JLabel resultLabel2;
    private JTextField temperatureInput;
    private JComboBox<String> unitSelect;

    public TemperatureConverter() {
    	setVisible(true);
        setTitle("Temperature Converter");
        setSize(400, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);

        JLabel label = new JLabel("Enter Temperature:");
        label.setBounds(20, 20, 150, 25);
        add(label);

        temperatureInput = new JTextField();
        temperatureInput.setBounds(180, 20, 150, 25);
        add(temperatureInput);

        String[] units = {"Celsius", "Fahrenheit", "Kelvin"};
        unitSelect = new JComboBox<>(units);
        unitSelect.setBounds(180, 60, 150, 25);
        add(unitSelect);

        JButton convertButton = new JButton("Convert");
        convertButton.setBounds(140, 100, 100, 25);
        add(convertButton);

        resultLabel1 = new JLabel("");
        resultLabel1.setBounds(20, 140, 350, 25);
        add(resultLabel1);

        resultLabel2 = new JLabel("");
        resultLabel2.setBounds(20, 160, 350, 25);
        add(resultLabel2);

        convertButton.addActionListener(this);
    }

    @Override
	public void actionPerformed(ActionEvent e) {
		convertTemperature();
	}

    private void convertTemperature() {
        try {
            double temperature = Double.parseDouble(temperatureInput.getText());
            String selectedUnit = (String) unitSelect.getSelectedItem();

            if ("Celsius".equals(selectedUnit)) {
                double fahrenheit = celsiusToFahrenheit(temperature);
                double kelvin = celsiusToKelvin(temperature);
                resultLabel1.setText("Fahrenheit: " + fahrenheit);
                resultLabel2.setText("Kelvin: " + kelvin);
            } else if ("Fahrenheit".equals(selectedUnit)) {
                double celsius = fahrenheitToCelsius(temperature);
                double kelvin = fahrenheitToKelvin(temperature);
                resultLabel1.setText("Celsius: " + celsius);
                resultLabel2.setText("Kelvin: " + kelvin);
            } else if ("Kelvin".equals(selectedUnit)) {
                double celsius = kelvinToCelsius(temperature);
                double fahrenheit = kelvinToFahrenheit(temperature);
                resultLabel1.setText("Celsius: " + celsius);
                resultLabel2.setText("Fahrenheit: " + fahrenheit);
            }
        } catch (NumberFormatException ex) {
            resultLabel1.setText("Please enter a valid number.");
            resultLabel2.setText("");
        }
    }

    private double celsiusToFahrenheit(double celsius) {
        return (celsius * 9 / 5) + 32;
    }

    private double celsiusToKelvin(double celsius) {
        return celsius + 273.15;
    }

    private double fahrenheitToCelsius(double fahrenheit) {
        return (fahrenheit - 32) * 5 / 9;
    }

    private double fahrenheitToKelvin(double fahrenheit) {
        return (fahrenheit - 32) * 5 / 9 + 273.15;
    }

    private double kelvinToCelsius(double kelvin) {
        return kelvin - 273.15;
    }

    private double kelvinToFahrenheit(double kelvin) {
        return (kelvin - 273.15) * 9 / 5 + 32;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new TemperatureConverter();
            }
        });
    }
}
