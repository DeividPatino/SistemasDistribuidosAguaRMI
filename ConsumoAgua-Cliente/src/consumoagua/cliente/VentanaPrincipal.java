package consumoagua.cliente;

import consumoagua.rmi.lib.DatosConsumoAgua;
import consumoagua.rmi.lib.IRemotaCalculoConsumoAgua;
import net.sf.lipermi.handler.CallHandler;
import net.sf.lipermi.net.Client;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class VentanaPrincipal extends JFrame {

    private JTextField txtLitros;
    private JTextField txtHabitantes;
    private JLabel lblResultado;

    public VentanaPrincipal() {
        configurarVentana();
        crearInterfaz();
    }

    private void configurarVentana() {

        setTitle("Consumo de Agua por Habitante");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
    }

    private void crearInterfaz() {

        JPanel panelPrincipal =
                new JPanel(new BorderLayout(10, 10));

        panelPrincipal.setBorder(
                BorderFactory.createEmptyBorder(
                        20, 30, 20, 30
                )
        );

        JLabel lblTitulo =
                new JLabel(
                        "CONSUMO DE AGUA",
                        SwingConstants.CENTER
                );

        lblTitulo.setFont(
                new Font("Arial", Font.BOLD, 24)
        );

        panelPrincipal.add(
                lblTitulo,
                BorderLayout.NORTH
        );

        JPanel panelDatos =
                new JPanel(new GridBagLayout());

        GridBagConstraints gbc =
                new GridBagConstraints();

        gbc.insets =
                new Insets(10, 10, 10, 10);

        gbc.fill =
                GridBagConstraints.HORIZONTAL;

        JLabel lblLitros =
                new JLabel(
                        "Litros consumidos en el día:"
                );

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.5;

        panelDatos.add(
                lblLitros,
                gbc
        );

        txtLitros = new JTextField();

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 1.0;

        panelDatos.add(
                txtLitros,
                gbc
        );

        JLabel lblHabitantes =
                new JLabel(
                        "Habitantes de la vivienda:"
                );

        gbc.gridx = 0;
        gbc.gridy = 1;

        panelDatos.add(
                lblHabitantes,
                gbc
        );

        txtHabitantes = new JTextField();

        gbc.gridx = 1;
        gbc.gridy = 1;

        panelDatos.add(
                txtHabitantes,
                gbc
        );

        JButton btnCalcular =
                new JButton("CALCULAR");

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 1;

        panelDatos.add(
                btnCalcular,
                gbc
        );

        JButton btnLimpiar =
                new JButton("LIMPIAR");

        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.gridwidth = 1;

        panelDatos.add(
                btnLimpiar,
                gbc
        );

        lblResultado =
                new JLabel(
                        "Consumo promedio: -- litros por habitante",
                        SwingConstants.CENTER
                );

        lblResultado.setFont(
                new Font("Arial", Font.BOLD, 16)
        );

        btnCalcular.addActionListener(
                e -> calcularConsumo()
        );

        btnLimpiar.addActionListener(
                e -> limpiarCampos()
        );

        panelPrincipal.add(
                panelDatos,
                BorderLayout.CENTER
        );

        panelPrincipal.add(
                lblResultado,
                BorderLayout.SOUTH
        );

        add(panelPrincipal);
    }

    private void calcularConsumo() {

        String litrosTexto =
                txtLitros.getText().trim();

        String habitantesTexto =
                txtHabitantes.getText().trim();

        if (litrosTexto.isEmpty()
                || habitantesTexto.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Debe ingresar todos los datos.",
                    "Datos incompletos",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        double litros;

        try {

            litros =
                    Double.parseDouble(litrosTexto);

        } catch (NumberFormatException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "La cantidad de litros debe ser un número válido.",
                    "Dato incorrecto",
                    JOptionPane.ERROR_MESSAGE
            );

            return;
        }

        int habitantes;

        try {

            habitantes =
                    Integer.parseInt(habitantesTexto);

        } catch (NumberFormatException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "La cantidad de habitantes debe ser un número entero.",
                    "Dato incorrecto",
                    JOptionPane.ERROR_MESSAGE
            );

            return;
        }

        if (litros <= 0) {

            JOptionPane.showMessageDialog(
                    this,
                    "Los litros consumidos deben ser mayores que cero.",
                    "Dato incorrecto",
                    JOptionPane.ERROR_MESSAGE
            );

            return;
        }

        if (habitantes <= 0) {

            JOptionPane.showMessageDialog(
                    this,
                    "La cantidad de habitantes debe ser mayor que cero.",
                    "Dato incorrecto",
                    JOptionPane.ERROR_MESSAGE
            );

            return;
        }

        Client client = null;

        try {

            CallHandler callHandler =
                    new CallHandler();

            client =
                    new Client(
                            "localhost",
                            9007,
                            callHandler
                    );

            IRemotaCalculoConsumoAgua remote =
                    (IRemotaCalculoConsumoAgua)
                    client.getGlobal(
                            IRemotaCalculoConsumoAgua.class
                    );

            DatosConsumoAgua datos =
                    new DatosConsumoAgua(
                            litros,
                            habitantes
                    );

            datos =
                    remote.calcularConsumo(datos);

            lblResultado.setText(
                    String.format(
                            "Consumo promedio: %.2f litros por habitante",
                            datos.getResultado()
                    )
            );

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                    this,
                    "No se pudo conectar con el servidor.\n\n"
                    + "Verifique que el servidor esté ejecutándose "
                    + "en el puerto 9007.\n\n"
                    + "Detalle: "
                    + e.getMessage(),
                    "Error de conexión",
                    JOptionPane.ERROR_MESSAGE
            );

        } finally {

            if (client != null) {

                try {

                    client.close();

                } catch (IOException e) {

                    System.err.println(
                            "No se pudo cerrar la conexión: "
                            + e.getMessage()
                    );
                }
            }
        }
    }

    private void limpiarCampos() {

        txtLitros.setText("");

        txtHabitantes.setText("");

        lblResultado.setText(
                "Consumo promedio: -- litros por habitante"
        );

        txtLitros.requestFocus();
    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            VentanaPrincipal ventana =
                    new VentanaPrincipal();

            ventana.setVisible(true);
        });
    }
}
