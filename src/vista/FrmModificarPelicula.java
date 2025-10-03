package vista;

import dao.Conexion;
import java.sql.*;
import javax.swing.*;

public class FrmModificarPelicula extends JFrame {
    private JTextField txtId, txtTitulo, txtDirector, txtAnio, txtDuracion;
    private JComboBox<String> cmbGenero;

    public FrmModificarPelicula() {
        setTitle("Modificar Película");
        setSize(450, 400);
        setLayout(null);
        setLocationRelativeTo(null);

        JLabel lblId = new JLabel("ID:");
        lblId.setBounds(30, 30, 80, 25);
        add(lblId);

        txtId = new JTextField();
        txtId.setBounds(120, 30, 100, 25);
        add(txtId);

        JButton btnBuscar = new JButton("Buscar");
        btnBuscar.setBounds(250, 30, 100, 25);
        add(btnBuscar);

        JLabel lblTitulo = new JLabel("Título:");
        lblTitulo.setBounds(30, 80, 80, 25);
        add(lblTitulo);

        txtTitulo = new JTextField();
        txtTitulo.setBounds(120, 80, 250, 25);
        add(txtTitulo);

        JLabel lblDirector = new JLabel("Director:");
        lblDirector.setBounds(30, 120, 80, 25);
        add(lblDirector);

        txtDirector = new JTextField();
        txtDirector.setBounds(120, 120, 250, 25);
        add(txtDirector);

        JLabel lblAnio = new JLabel("Año:");
        lblAnio.setBounds(30, 160, 80, 25);
        add(lblAnio);

        txtAnio = new JTextField();
        txtAnio.setBounds(120, 160, 250, 25);
        add(txtAnio);

        JLabel lblDuracion = new JLabel("Duración:");
        lblDuracion.setBounds(30, 200, 80, 25);
        add(lblDuracion);

        txtDuracion = new JTextField();
        txtDuracion.setBounds(120, 200, 250, 25);
        add(txtDuracion);

        JLabel lblGenero = new JLabel("Género:");
        lblGenero.setBounds(30, 240, 80, 25);
        add(lblGenero);

        cmbGenero = new JComboBox<>(new String[]{"Acción","Comedia","Drama","Terror","Ciencia Ficción","Romance"});
        cmbGenero.setBounds(120, 240, 250, 25);
        add(cmbGenero);

        JButton btnModificar = new JButton("Modificar");
        btnModificar.setBounds(70, 300, 120, 30);
        add(btnModificar);

        JButton btnLimpiar = new JButton("Limpiar");
        btnLimpiar.setBounds(220, 300, 120, 30);
        add(btnLimpiar);

        // Acción buscar
        btnBuscar.addActionListener(e -> {
            try (Connection con = Conexion.getConexion()) {
                String sql = "SELECT * FROM Cartelera WHERE id=?";
                PreparedStatement ps = con.prepareStatement(sql);
                ps.setInt(1, Integer.parseInt(txtId.getText()));
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    txtTitulo.setText(rs.getString("titulo"));
                    txtDirector.setText(rs.getString("director"));
                    txtAnio.setText(String.valueOf(rs.getInt("anio")));
                    txtDuracion.setText(String.valueOf(rs.getInt("duracion")));
                    cmbGenero.setSelectedItem(rs.getString("genero"));
                } else {
                    JOptionPane.showMessageDialog(this, "Película no encontrada");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        });

        // Acción modificar
        btnModificar.addActionListener(e -> {
            try (Connection con = Conexion.getConexion()) {
                String sql = "UPDATE Cartelera SET titulo=?, director=?, anio=?, duracion=?, genero=? WHERE id=?";
                PreparedStatement ps = con.prepareStatement(sql);
                ps.setString(1, txtTitulo.getText());
                ps.setString(2, txtDirector.getText());
                ps.setInt(3, Integer.parseInt(txtAnio.getText()));
                ps.setInt(4, Integer.parseInt(txtDuracion.getText()));
                ps.setString(5, cmbGenero.getSelectedItem().toString());
                ps.setInt(6, Integer.parseInt(txtId.getText()));
                int filas = ps.executeUpdate();
                if (filas > 0) {
                    JOptionPane.showMessageDialog(this, "Película modificada correctamente");
                } else {
                    JOptionPane.showMessageDialog(this, "No se pudo modificar");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        });

        // Acción limpiar
        btnLimpiar.addActionListener(e -> {
            txtId.setText("");
            txtTitulo.setText("");
            txtDirector.setText("");
            txtAnio.setText("");
            txtDuracion.setText("");
            cmbGenero.setSelectedIndex(0);
        });
    }
}
