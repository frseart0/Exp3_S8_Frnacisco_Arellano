package vista;

import dao.Conexion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.JOptionPane;

public class FrmAgregarPelicula extends javax.swing.JFrame {

    public FrmAgregarPelicula() {
        initComponents();
        setLocationRelativeTo(null);
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {
        setTitle("Agregar Película");
        setSize(400, 350);
        setLayout(null);

        // Etiquetas
        javax.swing.JLabel lblTitulo = new javax.swing.JLabel("Título:");
        lblTitulo.setBounds(30, 30, 80, 25);
        add(lblTitulo);

        javax.swing.JLabel lblDirector = new javax.swing.JLabel("Director:");
        lblDirector.setBounds(30, 70, 80, 25);
        add(lblDirector);

        javax.swing.JLabel lblAnio = new javax.swing.JLabel("Año:");
        lblAnio.setBounds(30, 110, 80, 25);
        add(lblAnio);

        javax.swing.JLabel lblDuracion = new javax.swing.JLabel("Duración (min):");
        lblDuracion.setBounds(30, 150, 100, 25);
        add(lblDuracion);

        javax.swing.JLabel lblGenero = new javax.swing.JLabel("Género:");
        lblGenero.setBounds(30, 190, 80, 25);
        add(lblGenero);

        // Campos de texto
        javax.swing.JTextField txtTitulo = new javax.swing.JTextField();
        txtTitulo.setBounds(150, 30, 200, 25);
        add(txtTitulo);

        javax.swing.JTextField txtDirector = new javax.swing.JTextField();
        txtDirector.setBounds(150, 70, 200, 25);
        add(txtDirector);

        javax.swing.JTextField txtAnio = new javax.swing.JTextField();
        txtAnio.setBounds(150, 110, 200, 25);
        add(txtAnio);

        javax.swing.JTextField txtDuracion = new javax.swing.JTextField();
        txtDuracion.setBounds(150, 150, 200, 25);
        add(txtDuracion);

        javax.swing.JComboBox<String> cmbGenero = new javax.swing.JComboBox<>(
                new String[]{"Acción","Comedia","Drama","Terror","Ciencia Ficción","Romance"}
        );
        cmbGenero.setBounds(150, 190, 200, 25);
        add(cmbGenero);

        // Botones
        javax.swing.JButton btnGuardar = new javax.swing.JButton("Guardar");
        btnGuardar.setBounds(50, 250, 100, 30);
        add(btnGuardar);

        javax.swing.JButton btnLimpiar = new javax.swing.JButton("Limpiar");
        btnLimpiar.setBounds(200, 250, 100, 30);
        add(btnLimpiar);

        // Acción Guardar
        btnGuardar.addActionListener(evt -> {
            try {
                String titulo = txtTitulo.getText();
                String director = txtDirector.getText();
                String anio = txtAnio.getText();
                String duracion = txtDuracion.getText();
                String genero = cmbGenero.getSelectedItem().toString();

                // Validaciones
                if (titulo.isEmpty() || director.isEmpty() || anio.isEmpty() || duracion.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios");
                    return;
                }

                int anioNum = Integer.parseInt(anio);
                int duracionNum = Integer.parseInt(duracion);

                Connection con = Conexion.getConexion();

                String checkSql = "SELECT * FROM Cartelera WHERE titulo=? AND anio=?";
                PreparedStatement checkPs = con.prepareStatement(checkSql);
                checkPs.setString(1, titulo);
                checkPs.setInt(2, anioNum);
                ResultSet rs = checkPs.executeQuery();

                if (rs.next()) {
                    JOptionPane.showMessageDialog(this, "Ya existe una película con ese título y año");
                    return;
                }

                String sql = "INSERT INTO Cartelera (titulo,director,anio,duracion,genero) VALUES (?,?,?,?,?)";
                PreparedStatement ps = con.prepareStatement(sql);
                ps.setString(1, titulo);
                ps.setString(2, director);
                ps.setInt(3, anioNum);
                ps.setInt(4, duracionNum);
                ps.setString(5, genero);

                ps.executeUpdate();
                JOptionPane.showMessageDialog(this, "Película agregada correctamente");

                con.close();

            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
            }
        });

        // Acción Limpiar
        btnLimpiar.addActionListener(evt -> {
            txtTitulo.setText("");
            txtDirector.setText("");
            txtAnio.setText("");
            txtDuracion.setText("");
            cmbGenero.setSelectedIndex(0);
        });
    }
}
