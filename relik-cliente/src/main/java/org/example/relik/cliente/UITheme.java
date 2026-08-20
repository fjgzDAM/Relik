package org.example.relik.cliente;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import java.awt.*;

public class UITheme {

    // Paleta Arqueológica (Tierras, Terracota, Arcilla, Oro Antiguo y Marrón Piedra)
    public static final Color COLOR_PARCHMENT_BG = new Color(245, 240, 230); // Fondo pergamino claro
    public static final Color COLOR_CARD_BG = new Color(255, 252, 245);      // Fondo tarjetas/formularios
    public static final Color COLOR_HEADER_BG = new Color(92, 51, 23);       // Terracota oscuro cabecera
    public static final Color COLOR_HEADER_TEXT = new Color(255, 248, 240);    // Blanco hueso cabecera

    public static final Color COLOR_BTN_PRIMARY = new Color(140, 85, 45);     // Marrón Arcilla (Botones estándar)
    public static final Color COLOR_BTN_ACTION = new Color(45, 110, 55);      // Verde Oliva/Excavación (Crear Hallazgo)
    public static final Color COLOR_BTN_DANGER = new Color(160, 45, 45);      // Rojo Óxido/Arcilla (Eliminar/Salir)
    public static final Color COLOR_BTN_SECONDARY = new Color(100, 85, 75);   // Gris Piedra (Editar/Recargar)

    public static final Color COLOR_TEXT_DARK = new Color(40, 25, 15);       // Café Espresso (Texto oscuro de alto contraste)
    public static final Color COLOR_TEXT_LIGHT = new Color(255, 255, 255);    // Blanco puro para contraste sobre botones oscuros

    public static final Color COLOR_TABLE_HEADER = new Color(110, 65, 35);    // Cabecera tabla
    public static final Color COLOR_TABLE_ALT_ROW = new Color(248, 243, 233); // Fila alternativa
    public static final Color COLOR_TABLE_GRID = new Color(220, 210, 195);    // Rejilla

    public static final Font FONT_TITLE = new Font("Segoe UI", Font.BOLD, 18);
    public static final Font FONT_SUBTITLE = new Font("Segoe UI", Font.BOLD, 14);
    public static final Font FONT_BOLD = new Font("Segoe UI", Font.BOLD, 13);
    public static final Font FONT_REGULAR = new Font("Segoe UI", Font.PLAIN, 13);

    public static JButton createButton(String text, Color bgColor) {
        JButton btn = new JButton(text);
        btn.setFont(FONT_BOLD);
        btn.setBackground(bgColor);
        btn.setForeground(COLOR_TEXT_LIGHT);
        btn.setFocusPainted(false);
        btn.setOpaque(true);
        btn.setContentAreaFilled(true);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(bgColor.darker(), 2),
                BorderFactory.createEmptyBorder(8, 14, 8, 14)
        ));
        return btn;
    }

    public static void styleTable(JTable table) {
        table.setFont(FONT_REGULAR);
        table.setRowHeight(28);
        table.setGridColor(COLOR_TABLE_GRID);
        table.setShowGrid(true);

        JTableHeader header = table.getTableHeader();
        header.setFont(FONT_BOLD);
        header.setBackground(COLOR_TABLE_HEADER);
        header.setForeground(COLOR_TEXT_LIGHT);
        header.setReorderingAllowed(false);

        // Custom renderer for alternating row colors and high text contrast
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (isSelected) {
                    c.setBackground(new Color(218, 165, 32)); // Oro Ámbar
                    c.setForeground(COLOR_TEXT_DARK);
                    setFont(FONT_BOLD);
                } else {
                    c.setBackground(row % 2 == 0 ? Color.WHITE : COLOR_TABLE_ALT_ROW);
                    c.setForeground(COLOR_TEXT_DARK);
                    setFont(FONT_REGULAR);
                }
                return c;
            }
        });
    }
}

