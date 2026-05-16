package org.example.arquealia.ui;

import org.example.arquealia.dominio.Arqueologo;
import org.example.arquealia.dominio.Hallazgo;
import org.example.arquealia.dominio.Museo;
import org.example.arquealia.dominio.RestoMaterial;
import org.example.arquealia.dominio.Yacimiento;
import org.example.arquealia.modelo.ModeloInterfaceImpl;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

/**
 * Ventana principal simple en Swing para la aplicación.
 * Proporciona operaciones CRUD básicas sobre Yacimiento, Museo y Arqueologo.
 */
public class MainWindow extends JFrame {

    private final ModeloInterfaceImpl modelo;

    private final DefaultListModel<Yacimiento> yacimientoListModel = new DefaultListModel<>();
    private final JList<Yacimiento> yacimientoJList = new JList<>(yacimientoListModel);

    private final DefaultListModel<Museo> museoListModel = new DefaultListModel<>();
    private final JList<Museo> museoJList = new JList<>(museoListModel);

    private final DefaultListModel<Arqueologo> arqueologoListModel = new DefaultListModel<>();
    private final JList<Arqueologo> arqueologoJList = new JList<>(arqueologoListModel);
    
    private final DefaultListModel<RestoMaterial> restoListModel = new DefaultListModel<>();
    private final JList<RestoMaterial> restoJList = new JList<>(restoListModel);

    private final DefaultListModel<Hallazgo> hallazgoListModel = new DefaultListModel<>();
    private final JList<Hallazgo> hallazgoJList = new JList<>(hallazgoListModel);
    public MainWindow(ModeloInterfaceImpl modelo) {
        super("Relik - Gestión de hallazgos (prototipo)");
        this.modelo = modelo;
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);

        JTabbedPane tabs = new JTabbedPane();
        tabs.add("Yacimientos", createYacimientoPanel());
        tabs.add("Museos", createMuseoPanel());
        tabs.add("Arqueólogos", createArqueologoPanel());
        tabs.add("Restos", createRestoMaterialPanel());
        tabs.add("Hallazgos", createHallazgoPanel());

        add(tabs, BorderLayout.CENTER);

        refreshAll();
        setVisible(true);
    }

    private JPanel createYacimientoPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        yacimientoJList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        panel.add(new JScrollPane(yacimientoJList), BorderLayout.CENTER);

        JPanel buttons = new JPanel();
        buttons.add(new JButton(new AbstractAction("Nuevo") {
            @Override public void actionPerformed(ActionEvent e) {
                String nombre = JOptionPane.showInputDialog(MainWindow.this, "Nombre del yacimiento:");
                if (nombre != null && !nombre.trim().isEmpty()) {
                    Yacimiento y = new Yacimiento();
                    y.setNombre(nombre.trim());
                    modelo.insertar(y);
                    refreshYacimientos();
                }
            }
        }));

        buttons.add(new JButton(new AbstractAction("Editar") {
            @Override public void actionPerformed(ActionEvent e) {
                Yacimiento sel = yacimientoJList.getSelectedValue();
                if (sel == null) return;
                String nombre = JOptionPane.showInputDialog(MainWindow.this, "Nuevo nombre:", sel.getNombre());
                if (nombre != null && !nombre.trim().isEmpty()) {
                    sel.setNombre(nombre.trim());
                    modelo.modificar(sel);
                    refreshYacimientos();
                }
            }
        }));

        buttons.add(new JButton(new AbstractAction("Eliminar") {
            @Override public void actionPerformed(ActionEvent e) {
                Yacimiento sel = yacimientoJList.getSelectedValue();
                if (sel == null) return;
                int ok = JOptionPane.showConfirmDialog(MainWindow.this, "Eliminar yacimiento '"+sel.getNombre()+"'?", "Confirmar", JOptionPane.YES_NO_OPTION);
                if (ok == JOptionPane.YES_OPTION) {
                    modelo.eliminar(sel);
                    refreshYacimientos();
                }
            }
        }));

        buttons.add(new JButton(new AbstractAction("Refrescar") {
            @Override public void actionPerformed(ActionEvent e) { refreshYacimientos(); }
        }));

        panel.add(buttons, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel createMuseoPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        museoJList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        panel.add(new JScrollPane(museoJList), BorderLayout.CENTER);

        JPanel buttons = new JPanel();
        buttons.add(new JButton(new AbstractAction("Nuevo") {
            @Override public void actionPerformed(ActionEvent e) {
                String nombre = JOptionPane.showInputDialog(MainWindow.this, "Nombre del museo:");
                String epoca = JOptionPane.showInputDialog(MainWindow.this, "Época especializada:");
                if (nombre != null && !nombre.trim().isEmpty() && epoca != null) {
                    Museo m = new Museo(nombre.trim(), epoca.trim());
                    modelo.insertar(m);
                    refreshMuseos();
                }
            }
        }));

        buttons.add(new JButton(new AbstractAction("Editar") {
            @Override public void actionPerformed(ActionEvent e) {
                Museo sel = museoJList.getSelectedValue();
                if (sel == null) return;
                String nombre = JOptionPane.showInputDialog(MainWindow.this, "Nuevo nombre:", sel.getNombre());
                String epoca = JOptionPane.showInputDialog(MainWindow.this, "Época especializada:", sel.getEpocaEspecializada());
                if (nombre != null && !nombre.trim().isEmpty() && epoca != null) {
                    sel.setNombre(nombre.trim());
                    sel.setEpocaEspecializada(epoca.trim());
                    modelo.modificar(sel);
                    refreshMuseos();
                }
            }
        }));

        buttons.add(new JButton(new AbstractAction("Eliminar") {
            @Override public void actionPerformed(ActionEvent e) {
                Museo sel = museoJList.getSelectedValue();
                if (sel == null) return;
                int ok = JOptionPane.showConfirmDialog(MainWindow.this, "Eliminar museo '"+sel.getNombre()+"'?", "Confirmar", JOptionPane.YES_NO_OPTION);
                if (ok == JOptionPane.YES_OPTION) {
                    modelo.eliminar(sel);
                    refreshMuseos();
                }
            }
        }));

        buttons.add(new JButton(new AbstractAction("Refrescar") {
            @Override public void actionPerformed(ActionEvent e) { refreshMuseos(); }
        }));

        panel.add(buttons, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel createArqueologoPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        arqueologoJList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        panel.add(new JScrollPane(arqueologoJList), BorderLayout.CENTER);

        JPanel buttons = new JPanel();
        buttons.add(new JButton(new AbstractAction("Nuevo") {
            @Override public void actionPerformed(ActionEvent e) {
                String nombre = JOptionPane.showInputDialog(MainWindow.this, "Nombre:");
                String correo = JOptionPane.showInputDialog(MainWindow.this, "Correo:");
                if (nombre != null && correo != null && !nombre.trim().isEmpty() && !correo.trim().isEmpty()) {
                    String pass = JOptionPane.showInputDialog(MainWindow.this, "Contraseña:", "1234");
                    Arqueologo a = new Arqueologo(nombre.trim(), correo.trim(), pass == null ? "1234" : pass);
                    modelo.insertar(a);
                    refreshArqueologos();
                }
            }
        }));

        buttons.add(new JButton(new AbstractAction("Eliminar") {
            @Override public void actionPerformed(ActionEvent e) {
                Arqueologo sel = arqueologoJList.getSelectedValue();
                if (sel == null) return;
                int ok = JOptionPane.showConfirmDialog(MainWindow.this, "Eliminar arqueólogo '"+sel.getNombre()+"'?", "Confirmar", JOptionPane.YES_NO_OPTION);
                if (ok == JOptionPane.YES_OPTION) {
                    modelo.eliminar(sel.getIdArqueologo());
                    refreshArqueologos();
                }
            }
        }));

        buttons.add(new JButton(new AbstractAction("Refrescar") {
            @Override public void actionPerformed(ActionEvent e) { refreshArqueologos(); }
        }));

        panel.add(buttons, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel createRestoMaterialPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        restoJList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        panel.add(new JScrollPane(restoJList), BorderLayout.CENTER);

        JPanel buttons = new JPanel();
        buttons.add(new JButton(new AbstractAction("Nuevo") {
            @Override public void actionPerformed(ActionEvent e) {
                String nombre = JOptionPane.showInputDialog(MainWindow.this, "Nombre del resto:");
                String epoca = JOptionPane.showInputDialog(MainWindow.this, "Época:");
                String tipologia = JOptionPane.showInputDialog(MainWindow.this, "Tipología:");
                if (nombre != null && epoca != null && tipologia != null && !nombre.trim().isEmpty()) {
                    RestoMaterial r = new RestoMaterial(nombre.trim(), epoca.trim(), tipologia.trim());
                    // asignar museo si hay disponible
                    r = modelo.asignarMuseo(r);
                    modelo.insertar(r);
                    refreshRestos();
                }
            }
        }));

        buttons.add(new JButton(new AbstractAction("Editar") {
            @Override public void actionPerformed(ActionEvent e) {
                RestoMaterial sel = restoJList.getSelectedValue();
                if (sel == null) return;
                String nombre = JOptionPane.showInputDialog(MainWindow.this, "Nuevo nombre:", sel.getNombre());
                String epoca = JOptionPane.showInputDialog(MainWindow.this, "Época:", sel.getEpoca());
                String tipologia = JOptionPane.showInputDialog(MainWindow.this, "Tipología:", sel.getTipologia());
                if (nombre != null && epoca != null && tipologia != null) {
                    sel.setNombre(nombre.trim());
                    sel.setEpoca(epoca.trim());
                    sel.setTipologia(tipologia.trim());
                    sel = modelo.asignarMuseo(sel);
                    modelo.modificar(sel);
                    refreshRestos();
                }
            }
        }));

        buttons.add(new JButton(new AbstractAction("Eliminar") {
            @Override public void actionPerformed(ActionEvent e) {
                RestoMaterial sel = restoJList.getSelectedValue();
                if (sel == null) return;
                int ok = JOptionPane.showConfirmDialog(MainWindow.this, "Eliminar resto '"+sel.getNombre()+"'?", "Confirmar", JOptionPane.YES_NO_OPTION);
                if (ok == JOptionPane.YES_OPTION) {
                    modelo.eliminar(sel);
                    refreshRestos();
                }
            }
        }));

        buttons.add(new JButton(new AbstractAction("Refrescar") {
            @Override public void actionPerformed(ActionEvent e) { refreshRestos(); }
        }));

        panel.add(buttons, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel createHallazgoPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        hallazgoJList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        panel.add(new JScrollPane(hallazgoJList), BorderLayout.CENTER);

        JPanel buttons = new JPanel();
        buttons.add(new JButton(new AbstractAction("Nuevo") {
            @Override public void actionPerformed(ActionEvent e) {
                // seleccionar arqueólogo, yacimiento y resto mediante combo
                List<Arqueologo> arqueos = modelo.listarArqueologos();
                List<Yacimiento> yacs = modelo.listarYacimientos();
                List<RestoMaterial> restos = modelo.listarRestosMateriales();
                if (arqueos.isEmpty() || yacs.isEmpty() || restos.isEmpty()) {
                    JOptionPane.showMessageDialog(MainWindow.this, "Necesita al menos un arqueólogo, un yacimiento y un resto material para crear un hallazgo.");
                    return;
                }
                Arqueologo a = (Arqueologo) JOptionPane.showInputDialog(MainWindow.this, "Arqueólogo:", "Seleccionar", JOptionPane.PLAIN_MESSAGE, null, arqueos.toArray(), arqueos.get(0));
                Yacimiento y = (Yacimiento) JOptionPane.showInputDialog(MainWindow.this, "Yacimiento:", "Seleccionar", JOptionPane.PLAIN_MESSAGE, null, yacs.toArray(), yacs.get(0));
                RestoMaterial r = (RestoMaterial) JOptionPane.showInputDialog(MainWindow.this, "Resto Material:", "Seleccionar", JOptionPane.PLAIN_MESSAGE, null, restos.toArray(), restos.get(0));
                if (a != null && y != null && r != null) {
                    Hallazgo h = new Hallazgo();
                    h.setArqueologo(a);
                    h.setYacimiento(y);
                    h.setRestoMaterial(r);
                    h.setFechaHallazgo(java.time.LocalDateTime.now());
                    modelo.insertar(h);
                    refreshHallazgos();
                }
            }
        }));

        buttons.add(new JButton(new AbstractAction("Eliminar") {
            @Override public void actionPerformed(ActionEvent e) {
                Hallazgo sel = hallazgoJList.getSelectedValue();
                if (sel == null) return;
                int ok = JOptionPane.showConfirmDialog(MainWindow.this, "Eliminar hallazgo '"+sel.getIdHallazgo()+"'?", "Confirmar", JOptionPane.YES_NO_OPTION);
                if (ok == JOptionPane.YES_OPTION) {
                    modelo.eliminar(sel);
                    refreshHallazgos();
                }
            }
        }));

        buttons.add(new JButton(new AbstractAction("Refrescar") {
            @Override public void actionPerformed(ActionEvent e) { refreshHallazgos(); }
        }));

        panel.add(buttons, BorderLayout.SOUTH);
        return panel;
    }

    private void refreshYacimientos() {
        yacimientoListModel.clear();
        List<Yacimiento> list = modelo.listarYacimientos();
        for (Yacimiento y : list) yacimientoListModel.addElement(y);
    }

    private void refreshMuseos() {
        museoListModel.clear();
        List<Museo> list = modelo.listarMuseos();
        for (Museo m : list) museoListModel.addElement(m);
    }

    private void refreshArqueologos() {
        arqueologoListModel.clear();
        List<Arqueologo> list = modelo.listarArqueologos();
        for (Arqueologo a : list) arqueologoListModel.addElement(a);
    }

    private void refreshRestos() {
        restoListModel.clear();
        List<RestoMaterial> list = modelo.listarRestosMateriales();
        for (RestoMaterial r : list) restoListModel.addElement(r);
    }

    private void refreshHallazgos() {
        hallazgoListModel.clear();
        List<Hallazgo> list = modelo.listarHallazgos();
        for (Hallazgo h : list) hallazgoListModel.addElement(h);
    }

    private void refreshAll() {
        refreshYacimientos();
        refreshMuseos();
        refreshArqueologos();
    }
}

