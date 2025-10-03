package vista;

import dao.Conexion;
import java.sql.*;
import javax.swing.*;

public class FrmEliminarPelicula extends JFrame {
    private JTextField txtId;

    public FrmEliminarPelicula() {
        setTitle("Eliminar Película");
        setSize(350, 200);
        setLayout(null);
        setLocationRelativeTo(null);

        JLabel lblId = new JLabel("ID:");
        lblId.setBounds(30, 40, 80, 25);
        add(lblId);

        txtId = new JTextField();
        txtId.setBounds(100, 40, 150, 25);
        add(txtId);

        JButton btnEliminar = new JButton("Eliminar");
        btnEliminar.setBounds(40, 100, 100, 30);
        add(btnEliminar);

        JButton btnLimpiar = new JButton("Limpiar");
        btnLimpiar.setBounds(180, 100, 100, 30);
        add(btnLimpiar);

        // Acción eliminar
        btnEliminar.addActionListener(e -> {
            try (Connection con = Conexion.getConexion()) {
                int id = Integer.parseInt(txtId.getText());

                int confirmar = JOptionPane.showConfirmDialog(this,
                        "¿Seguro que deseas eliminar la película con ID " + id + "?",
                        "Confirmar eliminación",
                        JOptionPane.YES_NO_OPTION);

                if (confirmar == JOptionPane.YES_OPTION) {
                    String sql = "DELETE FROM Cartelera WHERE id=?";
                    PreparedStatement ps = con.prepareStatement(sql);
                    ps.setInt(1, id);
                    int filas = ps.executeUpdate();
                    if (filas > 0) {
                        JOptionPane.showMessageDialog(this, "Película eliminada correctamente");
                    } else {
                        JOptionPane.showMessageDialog(this, "Película no encontrada");
                    }
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        });

        // Acción limpiar
        btnLimpiar.addActionListener(e -> txtId.setText(""));
    }
}
