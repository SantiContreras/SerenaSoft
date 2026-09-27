package Paneles;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.EmptyBorder;

public class PanelConfigSeguridad extends JPanel {

    //==========================================================
    // ACCESO Y BLOQUEO
    //==========================================================
    private JCheckBox chkBloquearIntentos;
    private JSpinner spnIntentosMaximos;
    private JSpinner spnMinutosBloqueo;

    private JCheckBox chkBloquearUsuarioManual;
    private JCheckBox chkDesactivarUsuarioInactivo;

    private JSpinner spnDiasInactividad;

    //==========================================================
    // CONTRASEÑAS
    //==========================================================
    private JSpinner spnLongitudMinima;

    private JCheckBox chkMayuscula;
    private JCheckBox chkMinuscula;
    private JCheckBox chkNumero;
    private JCheckBox chkCaracterEspecial;

    private JCheckBox chkCambioPeriodico;
    private JSpinner spnDiasCambioClave;

    private JCheckBox chkNoRepetirClave;
    private JSpinner spnCantidadClavesAnteriores;

    //==========================================================
    // SESIONES
    //==========================================================
    private JCheckBox chkCerrarInactividad;
    private JSpinner spnMinutosInactividad;

    private JCheckBox chkUnaSesionUsuario;
    private JCheckBox chkCerrarSesionAlCambiarClave;
    private JCheckBox chkCerrarSesionesBloqueo;

    //==========================================================
    // AUDITORIA
    //==========================================================
    private JCheckBox chkRegistrarInicioSesion;
    private JCheckBox chkRegistrarIntentosFallidos;
    private JCheckBox chkRegistrarCambiosUsuarios;
    private JCheckBox chkRegistrarCambiosConfiguracion;
    private JCheckBox chkRegistrarOperacionesCriticas;

    //==========================================================
    // BOTONES
    //==========================================================
    private JButton btnRestaurar;
    private JButton btnGuardar;

    //==========================================================
    // COLORES
    //==========================================================
    private final Color FONDO =
            new Color(245, 247, 250);

    private final Color BLANCO =
            Color.WHITE;

    private final Color AZUL =
            new Color(25, 70, 145);

    private final Color AZUL_OSCURO =
            new Color(15, 50, 110);

    private final Color VERDE =
            new Color(25, 135, 84);

    private final Color GRIS =
            new Color(110, 120, 135);

    private final Color BORDE =
            new Color(215, 222, 232);

    private final Color TEXTO_SECUNDARIO =
            new Color(100, 110, 125);

    //==========================================================
    // CONSTRUCTOR
    //==========================================================
    public PanelConfigSeguridad() {

        inicializarComponentes();

        construirPanel();

        cargarDatosPrueba();

        configurarEventos();
    }

    //==========================================================
    // INICIALIZAR
    //==========================================================
    private void inicializarComponentes() {

        //======================================================
        // ACCESO
        //======================================================
        chkBloquearIntentos =
                new JCheckBox(
                        "Bloquear usuario después de varios intentos fallidos"
                );

        spnIntentosMaximos =
                new JSpinner(
                        new SpinnerNumberModel(
                                5,
                                1,
                                20,
                                1
                        )
                );

        spnMinutosBloqueo =
                new JSpinner(
                        new SpinnerNumberModel(
                                15,
                                1,
                                1440,
                                5
                        )
                );

        chkBloquearUsuarioManual =
                new JCheckBox(
                        "Permitir bloqueo manual de usuarios desde administración"
                );

        chkDesactivarUsuarioInactivo =
                new JCheckBox(
                        "Desactivar usuarios por inactividad prolongada"
                );

        spnDiasInactividad =
                new JSpinner(
                        new SpinnerNumberModel(
                                90,
                                1,
                                3650,
                                1
                        )
                );

        //======================================================
        // CONTRASEÑAS
        //======================================================
        spnLongitudMinima =
                new JSpinner(
                        new SpinnerNumberModel(
                                8,
                                4,
                                64,
                                1
                        )
                );

        chkMayuscula =
                new JCheckBox(
                        "Exigir al menos una letra mayúscula"
                );

        chkMinuscula =
                new JCheckBox(
                        "Exigir al menos una letra minúscula"
                );

        chkNumero =
                new JCheckBox(
                        "Exigir al menos un número"
                );

        chkCaracterEspecial =
                new JCheckBox(
                        "Exigir al menos un carácter especial"
                );

        chkCambioPeriodico =
                new JCheckBox(
                        "Solicitar cambio periódico de contraseña"
                );

        spnDiasCambioClave =
                new JSpinner(
                        new SpinnerNumberModel(
                                90,
                                1,
                                365,
                                1
                        )
                );

        chkNoRepetirClave =
                new JCheckBox(
                        "Evitar reutilización de contraseñas anteriores"
                );

        spnCantidadClavesAnteriores =
                new JSpinner(
                        new SpinnerNumberModel(
                                3,
                                1,
                                20,
                                1
                        )
                );

        //======================================================
        // SESIONES
        //======================================================
        chkCerrarInactividad =
                new JCheckBox(
                        "Cerrar sesión automáticamente por inactividad"
                );

        spnMinutosInactividad =
                new JSpinner(
                        new SpinnerNumberModel(
                                30,
                                5,
                                480,
                                5
                        )
                );

        chkUnaSesionUsuario =
                new JCheckBox(
                        "Permitir una sola sesión activa por usuario"
                );

        chkCerrarSesionAlCambiarClave =
                new JCheckBox(
                        "Cerrar sesiones abiertas después de cambiar contraseña"
                );

        chkCerrarSesionesBloqueo =
                new JCheckBox(
                        "Cerrar sesiones abiertas cuando un usuario sea bloqueado"
                );

        //======================================================
        // AUDITORIA
        //======================================================
        chkRegistrarInicioSesion =
                new JCheckBox(
                        "Registrar inicio y cierre de sesión"
                );

        chkRegistrarIntentosFallidos =
                new JCheckBox(
                        "Registrar intentos fallidos de acceso"
                );

        chkRegistrarCambiosUsuarios =
                new JCheckBox(
                        "Registrar altas, bajas y modificaciones de usuarios"
                );

        chkRegistrarCambiosConfiguracion =
                new JCheckBox(
                        "Registrar modificaciones de configuración"
                );

        chkRegistrarOperacionesCriticas =
                new JCheckBox(
                        "Registrar operaciones críticas del sistema"
                );

        //======================================================
        // ESTILO CHECKBOX
        //======================================================
        JCheckBox[] checks = {
            chkBloquearIntentos,
            chkBloquearUsuarioManual,
            chkDesactivarUsuarioInactivo,
            chkMayuscula,
            chkMinuscula,
            chkNumero,
            chkCaracterEspecial,
            chkCambioPeriodico,
            chkNoRepetirClave,
            chkCerrarInactividad,
            chkUnaSesionUsuario,
            chkCerrarSesionAlCambiarClave,
            chkCerrarSesionesBloqueo,
            chkRegistrarInicioSesion,
            chkRegistrarIntentosFallidos,
            chkRegistrarCambiosUsuarios,
            chkRegistrarCambiosConfiguracion,
            chkRegistrarOperacionesCriticas
        };

        for (JCheckBox check : checks) {

            check.setOpaque(false);

            check.setFont(
                    new Font(
                            "Segoe UI",
                            Font.PLAIN,
                            13
                    )
            );
        }

        //======================================================
        // BOTONES
        //======================================================
        btnRestaurar =
                crearBoton(
                        "Restaurar",
                        GRIS,
                        140
                );

        btnGuardar =
                crearBoton(
                        "Guardar Cambios",
                        VERDE,
                        160
                );
    }

    //==========================================================
    // CONSTRUIR PANEL
    //==========================================================
    private void construirPanel() {

        setLayout(new BorderLayout());

        setBackground(FONDO);

        //======================================================
        // CONTENEDOR GENERAL
        //======================================================
        JPanel contenedor =
                new JPanel(
                        new BorderLayout(0, 12)
                );

        contenedor.setBackground(BLANCO);

        contenedor.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(BORDE),
                        new EmptyBorder(
                                25,
                                30,
                                20,
                                30
                        )
                )
        );

        //======================================================
        // HEADER
        //======================================================
        JPanel header = new JPanel();

        header.setOpaque(false);

        header.setLayout(
                new BoxLayout(
                        header,
                        BoxLayout.Y_AXIS
                )
        );

        JLabel lblTitulo =
                new JLabel("SEGURIDAD");

        lblTitulo.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        22
                )
        );

        lblTitulo.setForeground(
                AZUL_OSCURO
        );

        JLabel lblSubtitulo =
                new JLabel(
                        "Políticas de acceso, contraseñas, sesiones y auditoría"
                );

        lblSubtitulo.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        14
                )
        );

        lblSubtitulo.setForeground(
                TEXTO_SECUNDARIO
        );

        header.add(lblTitulo);

        header.add(
                Box.createVerticalStrut(5)
        );

        header.add(lblSubtitulo);

        contenedor.add(
                header,
                BorderLayout.NORTH
        );

        //======================================================
        // CONTENIDO CENTRAL
        //======================================================
        JPanel centro = new JPanel();

        centro.setOpaque(false);

        centro.setLayout(
                new BoxLayout(
                        centro,
                        BoxLayout.Y_AXIS
                )
        );

        centro.setBorder(
                new EmptyBorder(
                        15,
                        0,
                        20,
                        0
                )
        );

        centro.setPreferredSize(
                new Dimension(
                        850,
                        820
                )
        );

        centro.setMinimumSize(
                new Dimension(
                        700,
                        820
                )
        );

        //======================================================
        // SECCIONES
        //======================================================
        JPanel panelAcceso =
                crearPanelAcceso();

        JPanel panelContrasenas =
                crearPanelContrasenas();

        JPanel panelSesiones =
                crearPanelSesiones();

        JPanel panelAuditoria =
                crearPanelAuditoria();

        configurarTamanoPanel(
                panelAcceso,
                190
        );

        configurarTamanoPanel(
                panelContrasenas,
                250
        );

        configurarTamanoPanel(
                panelSesiones,
                180
        );

        configurarTamanoPanel(
                panelAuditoria,
                180
        );

        panelAcceso.setAlignmentX(
                LEFT_ALIGNMENT
        );

        panelContrasenas.setAlignmentX(
                LEFT_ALIGNMENT
        );

        panelSesiones.setAlignmentX(
                LEFT_ALIGNMENT
        );

        panelAuditoria.setAlignmentX(
                LEFT_ALIGNMENT
        );

        //======================================================
        // AGREGAR SECCIONES
        //======================================================
        centro.add(panelAcceso);

        centro.add(
                Box.createVerticalStrut(15)
        );

        centro.add(panelContrasenas);

        centro.add(
                Box.createVerticalStrut(15)
        );

        centro.add(panelSesiones);

        centro.add(
                Box.createVerticalStrut(15)
        );

        centro.add(panelAuditoria);

        centro.add(
                Box.createVerticalStrut(15)
        );

        //======================================================
        // SCROLL CENTRAL
        //======================================================
        JScrollPane scrollContenido =
                new JScrollPane(centro);

        scrollContenido.setBorder(null);

        scrollContenido.setHorizontalScrollBarPolicy(
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER
        );

        scrollContenido.setVerticalScrollBarPolicy(
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED
        );

        scrollContenido
                .getVerticalScrollBar()
                .setUnitIncrement(16);

        scrollContenido
                .getViewport()
                .setBackground(BLANCO);

        contenedor.add(
                scrollContenido,
                BorderLayout.CENTER
        );

        //======================================================
        // BOTONES
        //======================================================
        JPanel botones =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.RIGHT,
                                10,
                                5
                        )
                );

        botones.setOpaque(false);

        botones.setBorder(
                new EmptyBorder(
                        5,
                        0,
                        0,
                        0
                )
        );

        botones.add(btnRestaurar);
        botones.add(btnGuardar);

        contenedor.add(
                botones,
                BorderLayout.SOUTH
        );

        add(
                contenedor,
                BorderLayout.CENTER
        );
    }

    //==========================================================
    // ACCESO Y BLOQUEO
    //==========================================================
    private JPanel crearPanelAcceso() {

        JPanel panel =
                new JPanel(
                        new GridBagLayout()
                );

        panel.setOpaque(false);

        panel.setBorder(
                crearBordeTitulo(
                        "Acceso y Bloqueo"
                )
        );

        GridBagConstraints c =
                crearConstraints();

        //======================================================
        // BLOQUEO INTENTOS
        //======================================================
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 4;

        panel.add(
                chkBloquearIntentos,
                c
        );

        //======================================================
        // INTENTOS
        //======================================================
        c.gridy = 1;
        c.gridwidth = 1;

        panel.add(
                crearLabel(
                        "Intentos máximos"
                ),
                c
        );

        c.gridx = 1;

        spnIntentosMaximos.setPreferredSize(
                new Dimension(
                        90,
                        32
                )
        );

        panel.add(
                spnIntentosMaximos,
                c
        );

        c.gridx = 2;

        panel.add(
                crearLabel(
                        "Bloqueo durante"
                ),
                c
        );

        c.gridx = 3;

        spnMinutosBloqueo.setPreferredSize(
                new Dimension(
                        90,
                        32
                )
        );

        panel.add(
                spnMinutosBloqueo,
                c
        );

        c.gridx = 4;

        panel.add(
                new JLabel("minutos"),
                c
        );

        //======================================================
        // BLOQUEO MANUAL
        //======================================================
        c.gridx = 0;
        c.gridy = 2;
        c.gridwidth = 5;

        panel.add(
                chkBloquearUsuarioManual,
                c
        );

        //======================================================
        // USUARIO INACTIVO
        //======================================================
        c.gridy = 3;

        panel.add(
                chkDesactivarUsuarioInactivo,
                c
        );

        c.gridy = 4;
        c.gridwidth = 1;

        panel.add(
                crearLabel(
                        "Días de inactividad"
                ),
                c
        );

        c.gridx = 1;

        spnDiasInactividad.setPreferredSize(
                new Dimension(
                        90,
                        32
                )
        );

        panel.add(
                spnDiasInactividad,
                c
        );

        return panel;
    }

    //==========================================================
    // CONTRASEÑAS
    //==========================================================
    private JPanel crearPanelContrasenas() {

        JPanel panel =
                new JPanel(
                        new GridBagLayout()
                );

        panel.setOpaque(false);

        panel.setBorder(
                crearBordeTitulo(
                        "Política de Contraseñas"
                )
        );

        GridBagConstraints c =
                crearConstraints();

        //======================================================
        // LONGITUD
        //======================================================
        c.gridx = 0;
        c.gridy = 0;

        panel.add(
                crearLabel(
                        "Longitud mínima"
                ),
                c
        );

        c.gridx = 1;

        spnLongitudMinima.setPreferredSize(
                new Dimension(
                        90,
                        32
                )
        );

        panel.add(
                spnLongitudMinima,
                c
        );

        //======================================================
        // REQUISITOS
        //======================================================
        c.gridx = 0;
        c.gridy = 1;
        c.gridwidth = 3;

        panel.add(
                chkMayuscula,
                c
        );

        c.gridy = 2;

        panel.add(
                chkMinuscula,
                c
        );

        c.gridy = 3;

        panel.add(
                chkNumero,
                c
        );

        c.gridy = 4;

        panel.add(
                chkCaracterEspecial,
                c
        );

        //======================================================
        // CAMBIO PERIODICO
        //======================================================
        c.gridy = 5;

        panel.add(
                chkCambioPeriodico,
                c
        );

        c.gridy = 6;
        c.gridwidth = 1;

        panel.add(
                crearLabel(
                        "Cambiar cada"
                ),
                c
        );

        c.gridx = 1;

        spnDiasCambioClave.setPreferredSize(
                new Dimension(
                        90,
                        32
                )
        );

        panel.add(
                spnDiasCambioClave,
                c
        );

        c.gridx = 2;

        panel.add(
                new JLabel("días"),
                c
        );

        //======================================================
        // HISTORIAL
        //======================================================
        c.gridx = 0;
        c.gridy = 7;
        c.gridwidth = 3;

        panel.add(
                chkNoRepetirClave,
                c
        );

        c.gridy = 8;
        c.gridwidth = 1;

        panel.add(
                crearLabel(
                        "Recordar últimas"
                ),
                c
        );

        c.gridx = 1;

        spnCantidadClavesAnteriores
                .setPreferredSize(
                        new Dimension(
                                90,
                                32
                        )
                );

        panel.add(
                spnCantidadClavesAnteriores,
                c
        );

        c.gridx = 2;

        panel.add(
                new JLabel(
                        "contraseñas"
                ),
                c
        );

        return panel;
    }

    //==========================================================
    // SESIONES
    //==========================================================
    private JPanel crearPanelSesiones() {

        JPanel panel =
                new JPanel(
                        new GridBagLayout()
                );

        panel.setOpaque(false);

        panel.setBorder(
                crearBordeTitulo(
                        "Sesiones"
                )
        );

        GridBagConstraints c =
                crearConstraints();

        //======================================================
        // INACTIVIDAD
        //======================================================
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 4;

        panel.add(
                chkCerrarInactividad,
                c
        );

        c.gridy = 1;
        c.gridwidth = 1;

        panel.add(
                crearLabel(
                        "Tiempo de inactividad"
                ),
                c
        );

        c.gridx = 1;

        spnMinutosInactividad.setPreferredSize(
                new Dimension(
                        90,
                        32
                )
        );

        panel.add(
                spnMinutosInactividad,
                c
        );

        c.gridx = 2;

        panel.add(
                new JLabel("minutos"),
                c
        );

        //======================================================
        // SESION UNICA
        //======================================================
        c.gridx = 0;
        c.gridy = 2;
        c.gridwidth = 4;

        panel.add(
                chkUnaSesionUsuario,
                c
        );

        c.gridy = 3;

        panel.add(
                chkCerrarSesionAlCambiarClave,
                c
        );

        c.gridy = 4;

        panel.add(
                chkCerrarSesionesBloqueo,
                c
        );

        return panel;
    }

    //==========================================================
    // AUDITORIA
    //==========================================================
    private JPanel crearPanelAuditoria() {

        JPanel panel =
                new JPanel();

        panel.setOpaque(false);

        panel.setLayout(
                new BoxLayout(
                        panel,
                        BoxLayout.Y_AXIS
                )
        );

        panel.setBorder(
                crearBordeTitulo(
                        "Auditoría y Registro"
                )
        );

        JCheckBox[] checks = {
            chkRegistrarInicioSesion,
            chkRegistrarIntentosFallidos,
            chkRegistrarCambiosUsuarios,
            chkRegistrarCambiosConfiguracion,
            chkRegistrarOperacionesCriticas
        };

        for (JCheckBox check : checks) {

            check.setAlignmentX(
                    LEFT_ALIGNMENT
            );

            panel.add(check);

            panel.add(
                    Box.createVerticalStrut(7)
            );
        }

        return panel;
    }

    //==========================================================
    // TAMAÑO DE PANELES
    //==========================================================
    private void configurarTamanoPanel(
            JPanel panel,
            int alto) {

        panel.setPreferredSize(
                new Dimension(
                        800,
                        alto
                )
        );

        panel.setMinimumSize(
                new Dimension(
                        600,
                        alto
                )
        );

        panel.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        alto
                )
        );
    }

    //==========================================================
    // DATOS DE PRUEBA
    //==========================================================
    private void cargarDatosPrueba() {

        //======================================================
        // ACCESO
        //======================================================
        chkBloquearIntentos.setSelected(true);

        spnIntentosMaximos.setValue(5);

        spnMinutosBloqueo.setValue(15);

        chkBloquearUsuarioManual.setSelected(true);

        chkDesactivarUsuarioInactivo.setSelected(false);

        spnDiasInactividad.setValue(90);

        //======================================================
        // CONTRASEÑAS
        //======================================================
        spnLongitudMinima.setValue(8);

        chkMayuscula.setSelected(true);

        chkMinuscula.setSelected(true);

        chkNumero.setSelected(true);

        chkCaracterEspecial.setSelected(false);

        chkCambioPeriodico.setSelected(false);

        spnDiasCambioClave.setValue(90);

        chkNoRepetirClave.setSelected(true);

        spnCantidadClavesAnteriores.setValue(3);

        //======================================================
        // SESIONES
        //======================================================
        chkCerrarInactividad.setSelected(true);

        spnMinutosInactividad.setValue(30);

        chkUnaSesionUsuario.setSelected(false);

        chkCerrarSesionAlCambiarClave.setSelected(true);

        chkCerrarSesionesBloqueo.setSelected(true);

        //======================================================
        // AUDITORIA
        //======================================================
        chkRegistrarInicioSesion.setSelected(true);

        chkRegistrarIntentosFallidos.setSelected(true);

        chkRegistrarCambiosUsuarios.setSelected(true);

        chkRegistrarCambiosConfiguracion.setSelected(true);

        chkRegistrarOperacionesCriticas.setSelected(true);
    }

    //==========================================================
    // EVENTOS
    //==========================================================
    private void configurarEventos() {

        //======================================================
        // HABILITAR/DESHABILITAR CAMPOS
        //======================================================
        chkBloquearIntentos.addActionListener(e -> {

            actualizarEstadoCampos();

        });

        chkDesactivarUsuarioInactivo.addActionListener(e -> {

            actualizarEstadoCampos();

        });

        chkCambioPeriodico.addActionListener(e -> {

            actualizarEstadoCampos();

        });

        chkNoRepetirClave.addActionListener(e -> {

            actualizarEstadoCampos();

        });

        chkCerrarInactividad.addActionListener(e -> {

            actualizarEstadoCampos();

        });

        //======================================================
        // RESTAURAR
        //======================================================
        btnRestaurar.addActionListener(e -> {

            cargarDatosPrueba();

            actualizarEstadoCampos();

        });

        //======================================================
        // GUARDAR
        //======================================================
        btnGuardar.addActionListener(e -> {

            javax.swing.JOptionPane
                    .showMessageDialog(
                            this,
                            "Configuración de seguridad guardada.",
                            "Seguridad",
                            javax.swing.JOptionPane
                                    .INFORMATION_MESSAGE
                    );

        });

        actualizarEstadoCampos();
    }

    //==========================================================
    // ESTADO DE CAMPOS
    //==========================================================
    private void actualizarEstadoCampos() {

        boolean bloquear =
                chkBloquearIntentos.isSelected();

        spnIntentosMaximos.setEnabled(
                bloquear
        );

        spnMinutosBloqueo.setEnabled(
                bloquear
        );

        spnDiasInactividad.setEnabled(
                chkDesactivarUsuarioInactivo
                        .isSelected()
        );

        spnDiasCambioClave.setEnabled(
                chkCambioPeriodico
                        .isSelected()
        );

        spnCantidadClavesAnteriores.setEnabled(
                chkNoRepetirClave
                        .isSelected()
        );

        spnMinutosInactividad.setEnabled(
                chkCerrarInactividad
                        .isSelected()
        );
    }

    //==========================================================
    // BORDER
    //==========================================================
    private javax.swing.border.TitledBorder crearBordeTitulo(
            String titulo) {

        return BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(
                        BORDE
                ),
                titulo,
                0,
                0,
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        15
                ),
                AZUL_OSCURO
        );
    }

    //==========================================================
    // CONSTRAINTS
    //==========================================================
    private GridBagConstraints crearConstraints() {

        GridBagConstraints c =
                new GridBagConstraints();

        c.insets =
                new Insets(
                        8,
                        10,
                        8,
                        10
                );

        c.anchor =
                GridBagConstraints.WEST;

        return c;
    }

    //==========================================================
    // LABEL
    //==========================================================
    private JLabel crearLabel(
            String texto) {

        JLabel label =
                new JLabel(texto);

        label.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        13
                )
        );

        label.setForeground(
                new Color(
                        60,
                        70,
                        85
                )
        );

        return label;
    }

    //==========================================================
    // BOTON
    //==========================================================
    private JButton crearBoton(
            String texto,
            Color color,
            int ancho) {

        JButton boton =
                new JButton(texto);

        boton.setPreferredSize(
                new Dimension(
                        ancho,
                        36
                )
        );

        boton.setFocusPainted(
                false
        );

        boton.setBackground(
                color
        );

        boton.setForeground(
                Color.WHITE
        );

        boton.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        12
                )
        );

        return boton;
    }
}
