package org.example.relik.cliente;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public abstract class GestorBase extends JFrame {
    protected String serverUrl;
    protected JTable table;
    protected DefaultTableModel tableModel;
    protected JTextField[] camposTexto;

    public GestorBase(String serverUrl, String titulo, int ancho, int alto) {
        this.serverUrl = serverUrl;
        setTitle(titulo);
        setSize(ancho, alto);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setResizable(true);
    }

    protected void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }

    protected void mostrarExito(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Éxito", JOptionPane.INFORMATION_MESSAGE);
    }

    protected String mostrarDialogoTexto(String titulo, String mensaje) {
        return JOptionPane.showInputDialog(this, mensaje, titulo, JOptionPane.PLAIN_MESSAGE);
    }

    protected int mostrarConfirmacion(String mensaje) {
        return JOptionPane.showConfirmDialog(this, mensaje, "Confirmación", JOptionPane.YES_NO_OPTION);
    }

    protected void crearTabla(String[] columnNames) {
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(tableModel);
        UITheme.styleTable(table);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }

    protected abstract void cargarDatos();

    protected abstract void agregarRegistro();

    protected abstract void editarRegistro();

    protected abstract void eliminarRegistro();
}


