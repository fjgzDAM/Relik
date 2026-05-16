package org.example.arquealia.ui;

import org.example.arquealia.modelo.ModeloInterfaceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import javax.swing.*;

@Component
public class UIStarter implements ApplicationListener<ApplicationReadyEvent> {

    @Autowired
    private ModeloInterfaceImpl modelo;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        // Launch Swing UI on EDT
        SwingUtilities.invokeLater(() -> new MainWindow(modelo));
    }
}

