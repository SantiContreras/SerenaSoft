package Paneles;

import java.awt.BorderLayout;
import Dao.EmpresaDao;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

public class PanelConfiguracionEmpresa extends JPanel {

    private final EmpresaDao empresaDao = new EmpresaDao();
    private String rutaLogoSeleccionada;
    private EmpresaDao.Empresa datosGuardados;

    //==========================================================
    // COMPONENTES
    //==========================================================
    private JTextField txtNombreComercial;
    private JTextField txtRazonSocial;
    private JTextField txtCuit;

    private JComboBox<String> cboCondicionIva;

    private JTextField txtDireccion;
    private JTextField txtLocalidad;
    private JTextField txtProvincia;
    private JTextField txtTelefono;
    private JTextField txtEmail;

    private JLabel lblLogo;

    private JButton btnSeleccionarLogo;
    private JButton btnGuardar;
    private JButton btnRestaurar;

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

    private final Color BORDE =
            new Color(215, 222, 232);

    private final Color TEXTO_SECUNDARIO =
            new Color(100, 110, 125);

    //==========================================================
    // CONSTRUCTOR
    //==========================================================
    public PanelConfiguracionEmpresa() {

        inicializarComponentes();

        construirPanel();

        configurarEventos();

        cargarDesdeBase();

    }

    //==========================================================
    // INICIALIZAR COMPONENTES
    //==========================================================
    private void inicializarComponentes() {

        txtNombreComercial = new JTextField();
        txtRazonSocial = new JTextField();
        txtCuit = new JTextField();

        cboCondicionIva = new JComboBox<>();

        cboCondicionIva.addItem("Responsable Inscripto");
        cboCondicionIva.addItem("Monotributista");
        cboCondicionIva.addItem("Exento");
        cboCondicionIva.addItem("Consumidor Final");

        txtDireccion = new JTextField();
        txtLocalidad = new JTextField();
        txtProvincia = new JTextField();
        txtTelefono = new JTextField();
        txtEmail = new JTextField();

        lblLogo = new JLabel(
                "LOGO",
                SwingConstants.CENTER
        );

        btnSeleccionarLogo =
                new JButton("Seleccionar Logo");

        btnGuardar =
                new JButton("Guardar Cambios");

        btnRestaurar =
                new JButton("Restaurar");

    }

    //==========================================================
    // CONSTRUIR PANEL
    //==========================================================
    private void construirPanel() {

        setLayout(new BorderLayout());

        setBackground(FONDO);

        setBorder(
                new EmptyBorder(
                        0,
                        0,
                        0,
                        0
                )
        );

        //------------------------------------------------------
        // CONTENEDOR PRINCIPAL
        //------------------------------------------------------
        JPanel contenedor =
                new JPanel(
                        new BorderLayout()
                );

        contenedor.setBackground(BLANCO);

        contenedor.setBorder(
                BorderFactory.createCompoundBorder(

                        BorderFactory.createLineBorder(
                                BORDE
                        ),

                        new EmptyBorder(
                                25,
                                30,
                                25,
                                30
                        )
                )
        );

        //------------------------------------------------------
        // HEADER
        //------------------------------------------------------
        JPanel panelHeader =
                new JPanel(
                        new BorderLayout()
                );

        panelHeader.setOpaque(false);

        JPanel panelTitulos =
                new JPanel();

        panelTitulos.setOpaque(false);

        panelTitulos.setLayout(
                new javax.swing.BoxLayout(
                        panelTitulos,
                        javax.swing.BoxLayout.Y_AXIS
                )
        );

        JLabel lblTitulo =
                new JLabel(
                        "EMPRESA"
                );

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
                        "Datos generales y fiscales del comercio"
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

        panelTitulos.add(lblTitulo);

        panelTitulos.add(
                javax.swing.Box
                        .createVerticalStrut(5)
        );

        panelTitulos.add(lblSubtitulo);

        panelHeader.add(
                panelTitulos,
                BorderLayout.WEST
        );

        contenedor.add(
                panelHeader,
                BorderLayout.NORTH
        );

        //------------------------------------------------------
        // FORMULARIO
        //------------------------------------------------------
        JPanel formulario =
                new JPanel(
                        new GridBagLayout()
                );

        formulario.setOpaque(false);

        formulario.setBorder(
                new EmptyBorder(
                        25,
                        0,
                        15,
                        0
                )
        );

        GridBagConstraints c =
                new GridBagConstraints();

        c.insets =
                new Insets(
                        8,
                        8,
                        8,
                        8
                );

        c.anchor =
                GridBagConstraints.WEST;

        c.fill =
                GridBagConstraints.HORIZONTAL;

        //------------------------------------------------------
        // FILA 1
        //------------------------------------------------------
        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 0;

        formulario.add(
                crearLabel(
                        "Nombre Comercial"
                ),
                c
        );

        c.gridx = 1;
        c.weightx = 1;

        configurarCampo(
                txtNombreComercial,
                260
        );

        formulario.add(
                txtNombreComercial,
                c
        );

        c.gridx = 2;
        c.weightx = 0;

        formulario.add(
                crearLabel(
                        "Razón Social"
                ),
                c
        );

        c.gridx = 3;
        c.weightx = 1;

        configurarCampo(
                txtRazonSocial,
                260
        );

        formulario.add(
                txtRazonSocial,
                c
        );

        //------------------------------------------------------
        // FILA 2
        //------------------------------------------------------
        c.gridx = 0;
        c.gridy = 1;
        c.weightx = 0;

        formulario.add(
                crearLabel("CUIT"),
                c
        );

        c.gridx = 1;
        c.weightx = 1;

        configurarCampo(
                txtCuit,
                220
        );

        formulario.add(
                txtCuit,
                c
        );

        c.gridx = 2;
        c.weightx = 0;

        formulario.add(
                crearLabel(
                        "Condición IVA"
                ),
                c
        );

        c.gridx = 3;
        c.weightx = 1;

        cboCondicionIva.setPreferredSize(
                new Dimension(
                        260,
                        32
                )
        );

        formulario.add(
                cboCondicionIva,
                c
        );

        //------------------------------------------------------
        // FILA 3
        //------------------------------------------------------
        c.gridx = 0;
        c.gridy = 2;
        c.weightx = 0;

        formulario.add(
                crearLabel(
                        "Dirección"
                ),
                c
        );

        c.gridx = 1;
        c.gridwidth = 3;
        c.weightx = 1;

        configurarCampo(
                txtDireccion,
                600
        );

        formulario.add(
                txtDireccion,
                c
        );

        c.gridwidth = 1;

        //------------------------------------------------------
        // FILA 4
        //------------------------------------------------------
        c.gridx = 0;
        c.gridy = 3;
        c.weightx = 0;

        formulario.add(
                crearLabel(
                        "Localidad"
                ),
                c
        );

        c.gridx = 1;
        c.weightx = 1;

        configurarCampo(
                txtLocalidad,
                220
        );

        formulario.add(
                txtLocalidad,
                c
        );

        c.gridx = 2;
        c.weightx = 0;

        formulario.add(
                crearLabel(
                        "Provincia"
                ),
                c
        );

        c.gridx = 3;
        c.weightx = 1;

        configurarCampo(
                txtProvincia,
                220
        );

        formulario.add(
                txtProvincia,
                c
        );

        //------------------------------------------------------
        // FILA 5
        //------------------------------------------------------
        c.gridx = 0;
        c.gridy = 4;
        c.weightx = 0;

        formulario.add(
                crearLabel(
                        "Teléfono"
                ),
                c
        );

        c.gridx = 1;
        c.weightx = 1;

        configurarCampo(
                txtTelefono,
                220
        );

        formulario.add(
                txtTelefono,
                c
        );

        c.gridx = 2;
        c.weightx = 0;

        formulario.add(
                crearLabel(
                        "Email"
                ),
                c
        );

        c.gridx = 3;
        c.weightx = 1;

        configurarCampo(
                txtEmail,
                260
        );

        formulario.add(
                txtEmail,
                c
        );

        //------------------------------------------------------
        // LOGO
        //------------------------------------------------------
        c.gridx = 0;
        c.gridy = 5;
        c.weightx = 0;

        formulario.add(
                crearLabel(
                        "Logo"
                ),
                c
        );

        c.gridx = 1;

        lblLogo.setPreferredSize(
                new Dimension(
                        120,
                        90
                )
        );

        lblLogo.setBorder(
                BorderFactory.createLineBorder(
                        BORDE
                )
        );

        lblLogo.setForeground(
                TEXTO_SECUNDARIO
        );

        formulario.add(
                lblLogo,
                c
        );

        c.gridx = 2;
        c.gridwidth = 2;

        btnSeleccionarLogo.setPreferredSize(
                new Dimension(
                        170,
                        36
                )
        );

        formulario.add(
                btnSeleccionarLogo,
                c
        );

        c.gridwidth = 1;

        contenedor.add(
                formulario,
                BorderLayout.CENTER
        );

        //------------------------------------------------------
        // BOTONES
        //------------------------------------------------------
        JPanel panelBotones =
                new JPanel(
                        new java.awt.FlowLayout(
                                java.awt.FlowLayout.RIGHT,
                                10,
                                0
                        )
                );

        panelBotones.setOpaque(false);

        aplicarEstiloBoton(
                btnRestaurar,
                new Color(
                        110,
                        120,
                        135
                )
        );

        aplicarEstiloBoton(
                btnGuardar,
                new Color(
                        25,
                        135,
                        84
                )
        );

        panelBotones.add(
                btnRestaurar
        );

        panelBotones.add(
                btnGuardar
        );

        contenedor.add(
                panelBotones,
                BorderLayout.SOUTH
        );

        add(
                contenedor,
                BorderLayout.CENTER
        );

    }

    //==========================================================
    // CONFIGURAR CAMPO
    //==========================================================
    private void configurarCampo(
            JTextField campo,
            int ancho) {

        campo.setPreferredSize(
                new Dimension(
                        ancho,
                        32
                )
        );

        campo.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        14
                )
        );

    }

    //==========================================================
    // CREAR LABEL
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
    // BOTONES
    //==========================================================
    private void aplicarEstiloBoton(
            JButton boton,
            Color color) {

        boton.setPreferredSize(
                new Dimension(
                        160,
                        38
                )
        );

        boton.setFocusPainted(false);

        boton.setBackground(color);

        boton.setForeground(
                Color.WHITE
        );

        boton.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        13
                )
        );

    }

    //==========================================================
    // PERSISTENCIA Y EVENTOS
    //==========================================================
    private void configurarEventos() {
        btnSeleccionarLogo.addActionListener(e -> seleccionarLogo());
        btnGuardar.addActionListener(e -> guardarCambios());
        btnRestaurar.addActionListener(e -> {
            if (JOptionPane.showConfirmDialog(this,
                    "¿Descartar los cambios sin guardar y recuperar los datos de MariaDB?",
                    "Restaurar datos", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                cargarDesdeBase();
            }
        });
    }

    private void cargarDesdeBase() {
        try {
            datosGuardados = empresaDao.obtener();
            EmpresaDao.Empresa d = datosGuardados;
            txtNombreComercial.setText(d == null ? "" : n(d.nombreComercial));
            txtRazonSocial.setText(d == null ? "" : n(d.razonSocial));
            txtCuit.setText(d == null ? "" : n(d.cuit));
            cboCondicionIva.setSelectedItem(d == null || d.condicionIva == null
                    ? "Responsable Inscripto" : d.condicionIva);
            txtDireccion.setText(d == null ? "" : n(d.direccion));
            txtLocalidad.setText(d == null ? "" : n(d.localidad));
            txtProvincia.setText(d == null ? "" : n(d.provincia));
            txtTelefono.setText(d == null ? "" : n(d.telefono));
            txtEmail.setText(d == null ? "" : n(d.email));
            rutaLogoSeleccionada = d == null ? null : d.logoRuta;
            mostrarLogo(rutaLogoSeleccionada);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "No se pudieron cargar los datos de la empresa:\n" + ex.getMessage(),
                    "Error de conexión", JOptionPane.ERROR_MESSAGE);
        }
    }

    private static String n(String v) { return v == null ? "" : v; }

    private void seleccionarLogo() {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileFilter(new FileNameExtensionFilter("Imágenes PNG, JPG", "png", "jpg", "jpeg"));
        if (chooser.showOpenDialog(this) != JFileChooser.APPROVE_OPTION) return;
        File archivo = chooser.getSelectedFile();
        try {
            if (!Files.isRegularFile(archivo.toPath()) || Files.size(archivo.toPath()) > 2_000_000)
                throw new IllegalArgumentException("Seleccioná una imagen válida de hasta 2 MB.");
            java.awt.image.BufferedImage imagen = javax.imageio.ImageIO.read(archivo);
            if (imagen == null) throw new IllegalArgumentException("El archivo no es una imagen válida.");
            rutaLogoSeleccionada = archivo.getAbsolutePath();
            mostrarLogo(rutaLogoSeleccionada);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Logo inválido", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void mostrarLogo(String ruta) {
        lblLogo.setIcon(null);
        lblLogo.setText("SIN LOGO");
        if (ruta == null || ruta.isBlank()) return;
        try {
            java.awt.image.BufferedImage img = javax.imageio.ImageIO.read(new File(ruta));
            if (img == null) return;
            int w = Math.max(1, img.getWidth()), h = Math.max(1, img.getHeight());
            double escala = Math.min(115.0 / w, 80.0 / h);
            java.awt.Image mini = img.getScaledInstance(Math.max(1, (int)(w * escala)),
                    Math.max(1, (int)(h * escala)), java.awt.Image.SCALE_SMOOTH);
            lblLogo.setIcon(new ImageIcon(mini));
            lblLogo.setText("");
        } catch (Exception ex) { lblLogo.setText("LOGO NO DISPONIBLE"); }
    }

    private void guardarCambios() {
        try {
            EmpresaDao.Empresa d = new EmpresaDao.Empresa();
            d.id = datosGuardados == null ? 0 : datosGuardados.id;
            d.nombreComercial = txtNombreComercial.getText().trim();
            d.razonSocial = txtRazonSocial.getText().trim();
            d.cuit = txtCuit.getText().trim();
            d.condicionIva = (String)cboCondicionIva.getSelectedItem();
            d.direccion = txtDireccion.getText().trim();
            d.localidad = txtLocalidad.getText().trim();
            d.provincia = txtProvincia.getText().trim();
            d.telefono = txtTelefono.getText().trim();
            d.email = txtEmail.getText().trim();
            if (d.nombreComercial.isEmpty() || d.razonSocial.isEmpty())
                throw new IllegalArgumentException("Nombre comercial y razón social son obligatorios.");
            if (d.nombreComercial.length() > 150 || d.razonSocial.length() > 150 || d.direccion.length() > 200
                    || d.localidad.length() > 100 || d.provincia.length() > 100 || d.telefono.length() > 30
                    || d.email.length() > 150) throw new IllegalArgumentException("Uno de los campos supera su longitud permitida.");
            if (!d.cuit.isEmpty()) {
                String digitos = d.cuit.replace("-", "");
                if (!digitos.matches("\\d{11}") || !cuitValido(digitos))
                    throw new IllegalArgumentException("El CUIT debe tener 11 dígitos y un dígito verificador válido.");
                d.cuit = digitos.substring(0,2) + "-" + digitos.substring(2,10) + "-" + digitos.substring(10);
            }
            if (!d.email.isEmpty() && !d.email.matches("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$"))
                throw new IllegalArgumentException("Ingresá un correo electrónico válido.");
            d.logoRuta = datosGuardados == null ? null : datosGuardados.logoRuta;
            // Se conserva el archivo original hasta confirmar que el guardado en BD funcionó.
            String nuevoLogo = rutaLogoSeleccionada;
            boolean copiar = nuevoLogo != null && !nuevoLogo.isBlank() && !nuevoLogo.equals(d.logoRuta);
            Path destino = null;
            if (copiar) {
                String extension = nuevoLogo.toLowerCase().endsWith(".png") ? ".png" : ".jpg";
                Path carpeta = Path.of(System.getProperty("user.home"), "SerenaSoft", "config", "logos");
                Files.createDirectories(carpeta);
                destino = carpeta.resolve("logo_empresa_" + System.currentTimeMillis() + extension);
                Files.copy(Path.of(nuevoLogo), destino, StandardCopyOption.REPLACE_EXISTING);
                d.logoRuta = destino.toAbsolutePath().toString();
            }
            try {
                empresaDao.guardar(d);
            } catch (Exception ex) {
                if (destino != null) Files.deleteIfExists(destino);
                throw ex;
            }
            cargarDesdeBase();
            JOptionPane.showMessageDialog(this, "Datos de la empresa guardados correctamente en MariaDB.",
                    "Serena Soft", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "No se guardaron los cambios:\n" + ex.getMessage(),
                    "Configuración de empresa", JOptionPane.ERROR_MESSAGE);
        }
    }

    private static boolean cuitValido(String cuit) {
        int[] pesos = {5,4,3,2,7,6,5,4,3,2};
        int suma = 0;
        for (int i = 0; i < 10; i++) suma += (cuit.charAt(i) - '0') * pesos[i];
        int digito = 11 - suma % 11;
        if (digito == 11) digito = 0;
        else if (digito == 10) digito = 9;
        return digito == cuit.charAt(10) - '0';
    }
}
