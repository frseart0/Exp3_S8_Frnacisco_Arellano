package vista;

import dao.Conexion;
import java.sql.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class FrmPrincipal extends JFrame {
    private JTable tabla;
    private JTextField txtTitulo, txtDirector, txtAnio, txtDuracion;
    private JComboBox<String> cmbGenero;

    private JTextField txtIdModificar, txtIdEliminar;

    public FrmPrincipal() {
        setTitle("Cartelera - Cine Magenta");
        setSize(800, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Crear contenedor con pestañas
        JTabbedPane tabs = new JTabbedPane();

        // Pestaña Listar
        JPanel pnlListar = new JPanel(null);
        tabla = new JTable();
        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setBounds(20, 20, 740, 300);
        pnlListar.add(scroll);

        JButton btnRecargar = new JButton("Recargar");
        btnRecargar.setBounds(20, 340, 100, 30);
        pnlListar.add(btnRecargar);

        btnRecargar.addActionListener(e -> cargarDatos());
        tabs.addTab("Listar", pnlListar);

        // Pestaña Agregar
        JPanel pnlAgregar = new JPanel(null);
        JLabel l1 = new JLabel("Título:"); l1.setBounds(30,30,100,25); pnlAgregar.add(l1);
        txtTitulo = new JTextField(); txtTitulo.setBounds(150,30,200,25); pnlAgregar.add(txtTitulo);

        JLabel l2 = new JLabel("Director:"); l2.setBounds(30,70,100,25); pnlAgregar.add(l2);
        txtDirector = new JTextField(); txtDirector.setBounds(150,70,200,25); pnlAgregar.add(txtDirector);

        JLabel l3 = new JLabel("Año:"); l3.setBounds(30,110,100,25); pnlAgregar.add(l3);
        txtAnio = new JTextField(); txtAnio.setBounds(150,110,200,25); pnlAgregar.add(txtAnio);

        JLabel l4 = new JLabel("Duración:"); l4.setBounds(30,150,100,25); pnlAgregar.add(l4);
        txtDuracion = new JTextField(); txtDuracion.setBounds(150,150,200,25); pnlAgregar.add(txtDuracion);

        JLabel l5 = new JLabel("Género:"); l5.setBounds(30,190,100,25); pnlAgregar.add(l5);
        cmbGenero = new JComboBox<>(new String[]{"Acción","Comedia","Drama","Terror","Ciencia Ficción","Romance"});
        cmbGenero.setBounds(150,190,200,25); pnlAgregar.add(cmbGenero);

        JButton btnGuardar = new JButton("Guardar"); btnGuardar.setBounds(80,250,100,30); pnlAgregar.add(btnGuardar);
        JButton btnLimpiar = new JButton("Limpiar"); btnLimpiar.setBounds(220,250,100,30); pnlAgregar.add(btnLimpiar);

        btnGuardar.addActionListener(e -> guardarPelicula());
        btnLimpiar.addActionListener(e -> limpiarAgregar());
        tabs.addTab("Agregar", pnlAgregar);

        // Pestaña Modificar (solo ejemplo con ID + título)
        JPanel pnlModificar = new JPanel(null);
        JLabel lm1 = new JLabel("ID Película:"); lm1.setBounds(30,30,100,25); pnlModificar.add(lm1);
        txtIdModificar = new JTextField(); txtIdModificar.setBounds(150,30,200,25); pnlModificar.add(txtIdModificar);

        JButton btnBuscar = new JButton("Buscar");
        btnBuscar.setBounds(380,30,100,25);
        pnlModificar.add(btnBuscar);

        // Reutilizar campos de agregar
        // (en un proyecto real conviene hacer un método separado o un panel aparte)
        JLabel lm2 = new JLabel("Título:"); lm2.setBounds(30,80,100,25); pnlModificar.add(lm2);
        JTextField txtTituloM = new JTextField(); txtTituloM.setBounds(150,80,200,25); pnlModificar.add(txtTituloM);

        JButton btnActualizar = new JButton("Actualizar");
        btnActualizar.setBounds(150,200,120,30);
        pnlModificar.add(btnActualizar);

        btnBuscar.addActionListener(e -> {
            try (Connection con = Conexion.getConexion()) {
                String sql = "SELECT titulo FROM Cartelera WHERE id=?";
                PreparedStatement ps = con.prepareStatement(sql);
                ps.setInt(1, Integer.parseInt(txtIdModificar.getText()));
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    txtTituloM.setText(rs.getString("titulo"));
                } else {
                    JOptionPane.showMessageDialog(this,"No encontrado");
                }
            } catch (Exception ex) { JOptionPane.showMessageDialog(this, ex.getMessage()); }
        });

        btnActualizar.addActionListener(e -> {
            try (Connection con = Conexion.getConexion()) {
                String sql = "UPDATE Cartelera SET titulo=? WHERE id=?";
                PreparedStatement ps = con.prepareStatement(sql);
                ps.setString(1, txtTituloM.getText());
                ps.setInt(2, Integer.parseInt(txtIdModificar.getText()));
                ps.executeUpdate();
                JOptionPane.showMessageDialog(this,"Película actualizada");
            } catch (Exception ex) { JOptionPane.showMessageDialog(this, ex.getMessage()); }
        });

        tabs.addTab("Modificar", pnlModificar);

        // Pestaña Eliminar
        JPanel pnlEliminar = new JPanel(null);
        JLabel le1 = new JLabel("ID Película:"); le1.setBounds(30,30,100,25); pnlEliminar.add(le1);
        txtIdEliminar = new JTextField(); txtIdEliminar.setBounds(150,30,200,25); pnlEliminar.add(txtIdEliminar);

        JButton btnEliminar = new JButton("Eliminar");
        btnEliminar.setBounds(150,80,120,30);
        pnlEliminar.add(btnEliminar);

        btnEliminar.addActionListener(e -> eliminarPelicula());
        tabs.addTab("Eliminar", pnlEliminar);

        // Agregar Tabs al frame
        add(tabs);

        // Cargar datos iniciales
        cargarDatos();
    }

    // Método para listar en tabla
    private void cargarDatos() {
        DefaultTableModel modelo = new DefaultTableModel(
                new String[]{"ID","Título","Director","Año","Duración","Género"}, 0);
        try (Connection con = Conexion.getConexion()) {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM Cartelera");
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

    // Guardar película
    private void guardarPelicula() {
        try (Connection con = Conexion.getConexion()) {
            String sql = "INSERT INTO Cartelera (titulo,director,anio,duracion,genero) VALUES (?,?,?,?,?)";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, txtTitulo.getText());
            ps.setString(2, txtDirector.getText());
            ps.setInt(3, Integer.parseInt(txtAnio.getText()));
            ps.setInt(4, Integer.parseInt(txtDuracion.getText()));
            ps.setString(5, cmbGenero.getSelectedItem().toString());
            ps.executeUpdate();
            JOptionPane.showMessageDialog(this,"Película agregada");
            limpiarAgregar();
            cargarDatos();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,"Error: "+e.getMessage());
        }
    }

    private void limpiarAgregar() {
        txtTitulo.setText("");
        txtDirector.setText("");
        txtAnio.setText("");
        txtDuracion.setText("");
        cmbGenero.setSelectedIndex(0);
    }

    // Eliminar película
    private void eliminarPelicula() {
        try (Connection con = Conexion.getConexion()) {
            int id = Integer.parseInt(txtIdEliminar.getText());
            int confirm = JOptionPane.showConfirmDialog(this,"¿Eliminar película "+id+"?","Confirmar",JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                String sql = "DELETE FROM Cartelera WHERE id=?";
                PreparedStatement ps = con.prepareStatement(sql);
                ps.setInt(1, id);
                int filas = ps.executeUpdate();
                if (filas > 0) {
                    JOptionPane.showMessageDialog(this,"Película eliminada");
                    cargarDatos();
                } else {
                    JOptionPane.showMessageDialog(this,"No encontrada");
                }
            }
        } catch (Exception e) { JOptionPane.showMessageDialog(this,"Error: "+e.getMessage()); }
    }

    public static void main(String[] args) {
        new FrmPrincipal().setVisible(true);
    }
}
