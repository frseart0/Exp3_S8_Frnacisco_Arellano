package vista;

import dao.Conexion;
import java.sql.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class FrmListarPeliculas extends JFrame {
    private JTable tabla;
    private JComboBox<String> cmbGenero;
    private JTextField txtAnioInicio, txtAnioFin;

    public FrmListarPeliculas() {
        setTitle("Listado de Películas");
        setSize(700, 400);
        setLayout(null);
        setLocationRelativeTo(null);

        JLabel lblGenero = new JLabel("Género:");
        lblGenero.setBounds(20, 20, 60, 25);
        add(lblGenero);

        cmbGenero = new JComboBox<>(new String[]{"Todos","Acción","Comedia","Drama","Terror","Ciencia Ficción","Romance"});
        cmbGenero.setBounds(80, 20, 150, 25);
        add(cmbGenero);

        JLabel lblRango = new JLabel("Años:");
        lblRango.setBounds(250, 20, 50, 25);
        add(lblRango);

        txtAnioInicio = new JTextField();
        txtAnioInicio.setBounds(300, 20, 60, 25);
        add(txtAnioInicio);

        JLabel lblGuion = new JLabel("-");
        lblGuion.setBounds(365, 20, 10, 25);
        add(lblGuion);

        txtAnioFin = new JTextField();
        txtAnioFin.setBounds(380, 20, 60, 25);
        add(txtAnioFin);

        JButton btnFiltrar = new JButton("Filtrar");
        btnFiltrar.setBounds(460, 20, 100, 25);
        add(btnFiltrar);

        JButton btnMostrarTodo = new JButton("Mostrar Todo");
        btnMostrarTodo.setBounds(570, 20, 120, 25);
        add(btnMostrarTodo);


        tabla = new JTable();
        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setBounds(20, 70, 650, 250);
        add(scroll);

        cargarDatos(null, null, null);

        btnFiltrar.addActionListener(e -> {
            String genero = cmbGenero.getSelectedItem().toString();
            Integer inicio = txtAnioInicio.getText().isEmpty() ? null : Integer.parseInt(txtAnioInicio.getText());
            Integer fin = txtAnioFin.getText().isEmpty() ? null : Integer.parseInt(txtAnioFin.getText());
            cargarDatos(genero.equals("Todos") ? null : genero, inicio, fin);
        });

        btnMostrarTodo.addActionListener(e -> {
            txtAnioInicio.setText("");
            txtAnioFin.setText("");
            cmbGenero.setSelectedIndex(0);
            cargarDatos(null, null, null);
        });
    }

    private void cargarDatos(String genero, Integer anioInicio, Integer anioFin) {
        DefaultTableModel modelo = new DefaultTableModel(
                new String[]{"ID","Título","Director","Año","Duración","Género"}, 0);
        try (Connection con = Conexion.getConexion()) {
            StringBuilder sql = new StringBuilder("SELECT * FROM Cartelera WHERE 1=1");
            if (genero != null) sql.append(" AND genero=?");
            if (anioInicio != null && anioFin != null) sql.append(" AND anio BETWEEN ? AND ?");

            PreparedStatement ps = con.prepareStatement(sql.toString());
            int idx = 1;
            if (genero != null) ps.setString(idx++, genero);
            if (anioInicio != null && anioFin != null) {
                ps.setInt(idx++, anioInicio);
                ps.setInt(idx++, anioFin);
            }
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                modelo.addRow(new Object[]{
                        rs.getInt("id"),
                        rs.getString("titulo"),
                        rs.getString("director"),
                        rs.getInt("anio"),
                        rs.getInt("duracion"),
                        rs.getString("genero")
                });
            }
            tabla.setModel(modelo);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al cargar datos: " + e.getMessage());
        }
    }
}