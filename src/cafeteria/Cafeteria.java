package cafeteria;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.Month;
import java.time.ZoneId;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;

/**
 * INSTRUCCIONES
 * 
 * Realiza un programa individual sobre el uso de Eventos. 
 * Debes generar el programa  y subirlo al moodle, en esta sección.
 * 
 * El programa Tar7##.java consiste en la programación de un un Evento 
 * libre que debe resolver algo, no solo académico, sino que resuelva 
 * algo practico. Debe contener como requisitos mínimos los siguientes:
 * 
 * 
 * · Capturar más de 3 datos.
 * 
 * · Tener al menos una lista, choice o checkbox.
 * 
 * · Que ofrezca resultados diferentes.
 * 
 * · Tener al menos 4 opciones.
 * 
 * · Que existan al menos 3 colores diferentes.
 * 
 * Deben de ser original
 * 
 * -----------------------------------------------------------------------------
 * 
 * # Cafetería UCC 
 * Un sistema de punto de venta como proyecto final de la
 * materia de Programación Avanzada. El usuario puede iniciar sesión, efectuar
 * órdenes, agregar productos, y administrar usuarios, así como visualizar
 * estadísticas acerca de su desempeño, haciendo uso de los tópicos vistos
 * durante la materia, como estructuras de datos, eventos, programación
 * multihilos y manejo de archivos y excepciones.
 *
 * ## Tecnologías 
 * El sistema fue realizado en Java 1.8, sin hacer uso de
 * librerías externas, aprovechando el diseñador gráfico de interfaces de
 * Netbeans, Matisse.
 *
 * Un archivo de valores separados por comas (CSV) funge como base de datos
 * local.
 *
 * ## Modelos 
 * Los modelos diseñados para este proyecto son:
 *
 * ### Orden 
 * Representa una orden efectuada por un usuario para un cliente. Dado
 * que se toma como temática una cafetería informal, no se cuenta con el modelo
 * de cliente. Los productos solicitados se almacenan en un arreglo, y la hora
 * de inicio y fin como milisegundos desde el tiempo Epoch Unix.
 *
 * Sus campos son:
 *
 * - ID: Entero 
 * - Número de mesa: Entero 
 * - Clientes: Entero 
 * - Orden: Arreglo de enteros 
 * - Estado: Una enumeración con los valores "Proceso", "Cancelado" o
 * "Finalizado" 
 * - Hora de inicio: Entero largo 
 * - Hora de fin: Entero largo 
 * - Subtotal: Flotante 
 * - Total: Flotante 
 * - Comentario: Cadena 
 * - ID de empleado: Entero
 *
 * ### Producto 
 * Representa un producto a ser vendido al cliente. Sus campos son:
 *
 * - ID: Entero 
 * - Nombre: Cadena 
 * - Precio: Flotante 
 * - Stock: Entero
 *
 * ### Usuario 
 * Representa el empleado haciendo uso del sistema de punto de
 * venta. Un usuario administrador podrá administrar a otros usuarios, mientras
 * que otro usuario sin estos permisos sólo podrá modificar órdenes y productos.
 * La contraseña del usuario se almacena como el resultado de una función hash
 * (SHA-256), para proveer de mayor seguridad.
 *
 * Sus campos son: 
 * - ID: Entero 
 * - Nombre: Cadena 
 * - Apellidos: Cadena 
 * - Rol: Una enumeración con los valores "Usuario" o "Administrador" 
 * - Fecha de inicio: Entero largo 
 * - RFC: Cadena 
 * - Usuario: Cadena 
 * - Hash: Cadena
 *
 * ## Uso 
 * Al ejecutar el programa, aparecerá una pantalla de inicio de sesión,
 * por defecto se incluye el super usuario "admin" con el nombre de usuario y
 * contraseña homónimos, así como el usuario regular "usuario". El programa le
 * informará de haber ingresado un usuario o contraseña incorrectos.
 *
 * Al ingresar a la ventana principal se observan una serie de enlaces laterales
 * para navegar entre las distintas pantallas, entre las cuales se encuentran:
 *
 * - Dashboard: Provee de algunas métricas generales relevantes, así como de un
 * mecanismo para cancelar o finalizar pedidos de forma rápida. 
 * - Órdenes: Permite visualizar un histórico de las órdenes realizadas, así como crear,
 * editar o eliminarlas. 
 * - Productos: Lista los productos disponibles dentro del
 * punto de venta, así como permite agregar, alterar o eliminar productos. 
 * - Estadísticas: Genera un análisis a fondo acerca de las métricas relevantes
 * para las órdenes realizadas, los productos guardados y los usuarios
 * registrados. 
 * - Usuarios: Permite administrar los usuarios del sistema.
 *
 * Al crear o editar un registro, aparecen los cuadros de diálogo modales para
 * capturar la información pertinente y permiten cancelar o realizar la
 * aprobación, tras verificar los campos ingresados.
 *
 * Las pantallas con vistas de tablas cuentan con un sistema de refresco rápido
 * de información para permitir procesar información entrante de otros medios,
 * como por ejemplo, la edición directa del archivo de base de datos.
 *
 * Al crear, editar o eliminar un registro, se actualizan de forma instantánea
 * las métricas relevantes dentro del Dashboard y de la pantalla de
 * Estadísticas.
 *
 * Modificar los stock o precios de productos no existentes no alteran las
 * órdenes anteriores, permitiendo un control más exacto de las ganancias y de
 * promociones recurrentes. No es posible colocar órdenes con las cual no se
 * cuente con el stock suficiente.
 *
 * ### Puntos de mejora 
 * Dado que para la presente asignación sólo se permite la
 * subida de dos archivos dentro de la plataforma escolar, algunos de los puntos
 * a desarrollar para contar con un producto más pulido son:
 *
 * - La realización de una API con las tecnologías de Spring Boot y Hibernate,
 * separando responsabilidades, basada en una instancia en la nube de PostgreSQL
 * o MongoDB. 
 * - El uso de librerías de terceros para la interconexión con dicha
 * API. 
 * - Algunos diseños responsivos.
 * 
 *
 * @author Diego Domínguez Melo
 *
 * Fecha: 13 de noviembre del 2022
 */
public class Cafeteria extends javax.swing.JFrame {

    /**
     * El color base del proyecto.
     */
    Color THEME = new Color(51, 51, 255);

    /**
     * Un arreglo con las órdenes actuales
     */
    ArrayList<String> orders = new ArrayList();

    /**
     * Un arreglo con los productos actuales
     */
    ArrayList<String> products = new ArrayList();

    /**
     * Un arreglo con los usuarios actuales
     */
    ArrayList<String> users = new ArrayList();

    /**
     * El archivo de base de datos
     */
    File database = new File("db.csv");

    /**
     * El usuario actual
     */
    String currentUser = null;

    /**
     * Obteniendo información, creando formulario y cargándolo
     */
    public Cafeteria() {
        loadData();
        initComponents();

        loadDashboardMetrics();
        addTableListeners();

        setCurrentDateOnDashboard();
        loadDashboardOrders();

        setVisible(false);
        showLogin();
    }

    /**
     * Código generado por el diseñador de Netbeans Matisse
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        orderModal = new javax.swing.JDialog();
        jPanel8 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        modalOrderTable = new javax.swing.JSpinner();
        modalOrderClients = new javax.swing.JSpinner();
        modalProductPanel = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        modalProductsTable = new javax.swing.JTable();
        modalDeleteProduct = new javax.swing.JButton();
        modalProductCombo = new javax.swing.JComboBox<>();
        modalAddProduct = new javax.swing.JButton();
        jLabel15 = new javax.swing.JLabel();
        modalOrderCancel = new javax.swing.JButton();
        modalOrderConfirm = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        modalProductComment = new javax.swing.JTextArea();
        modalOrderSubtotal = new javax.swing.JLabel();
        modalOrderTotal = new javax.swing.JLabel();
        loginFrame = new javax.swing.JFrame();
        jPanel10 = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        loginUsername = new javax.swing.JTextField();
        jLabel19 = new javax.swing.JLabel();
        loginPassword = new javax.swing.JPasswordField();
        login = new javax.swing.JButton();
        productModal = new javax.swing.JDialog();
        jPanel11 = new javax.swing.JPanel();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        productModalStock = new javax.swing.JSpinner();
        productModalPrice = new javax.swing.JFormattedTextField();
        jLabel25 = new javax.swing.JLabel();
        jPanel12 = new javax.swing.JPanel();
        productModalConfirm = new javax.swing.JButton();
        productModalCancel = new javax.swing.JButton();
        productModalName = new javax.swing.JTextField();
        jLabel22 = new javax.swing.JLabel();
        userModal = new javax.swing.JDialog();
        jPanel13 = new javax.swing.JPanel();
        jLabel23 = new javax.swing.JLabel();
        userModalPasswordConfirmLabel = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        jPanel14 = new javax.swing.JPanel();
        userModalConfirm = new javax.swing.JButton();
        userModalCancel = new javax.swing.JButton();
        userModalName = new javax.swing.JTextField();
        jLabel28 = new javax.swing.JLabel();
        userModalSurname = new javax.swing.JTextField();
        jLabel29 = new javax.swing.JLabel();
        userModalRole = new javax.swing.JComboBox<>();
        jLabel30 = new javax.swing.JLabel();
        userModalPasswordLabel = new javax.swing.JLabel();
        userModalRFC = new javax.swing.JTextField();
        userModalUsername = new javax.swing.JTextField();
        userModalPassword = new javax.swing.JPasswordField();
        userModalPasswordConfirm = new javax.swing.JPasswordField();
        sidebar = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        ordersLink = new javax.swing.JButton();
        dashboardLink = new javax.swing.JButton();
        productsLink = new javax.swing.JButton();
        statsLink = new javax.swing.JButton();
        usersLink = new javax.swing.JButton();
        logoutLink = new javax.swing.JButton();
        main = new javax.swing.JPanel();
        dashboardPanel = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        dashboardOrderCount = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        dashboardClientCount = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        dashboardTotalEarnings = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        dashboardCurrentOrders = new javax.swing.JTable();
        dashboardCancelOrder = new javax.swing.JButton();
        dashboardCompleteOrder = new javax.swing.JButton();
        jPanel9 = new javax.swing.JPanel();
        jLabel16 = new javax.swing.JLabel();
        dashboardUsername = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        dashboardDay = new javax.swing.JLabel();
        dashboardMonth = new javax.swing.JLabel();
        dashboardYear = new javax.swing.JLabel();
        ordersPanel = new javax.swing.JPanel();
        ordersPanelTitle = new javax.swing.JLabel();
        newOrder = new javax.swing.JButton();
        editOrder = new javax.swing.JButton();
        deleteOrder = new javax.swing.JButton();
        refreshOrders = new javax.swing.JButton();
        jScrollPane4 = new javax.swing.JScrollPane();
        ordersTable = new javax.swing.JTable();
        ordersTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        statsPanel = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        statsTotalProducts = new javax.swing.JLabel();
        statsMostRequestedProduct = new javax.swing.JLabel();
        statsCheapestProduct = new javax.swing.JLabel();
        statsMostExpensiveProduct = new javax.swing.JLabel();
        statsLeastStock = new javax.swing.JLabel();
        statsMostStock = new javax.swing.JLabel();
        statsAverageProductCost = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        statsTotalOrders = new javax.swing.JLabel();
        statsMostFrequentTable = new javax.swing.JLabel();
        statsAverageClients = new javax.swing.JLabel();
        statsAverageProducts = new javax.swing.JLabel();
        statsMaxProducts = new javax.swing.JLabel();
        statsLongestOrder = new javax.swing.JLabel();
        statsMostExpensiveOrder = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        statsTotalEmployees = new javax.swing.JLabel();
        statsMostAttendedOrders = new javax.swing.JLabel();
        statsOldestEmployee = new javax.swing.JLabel();
        statsMostRecentEmployee = new javax.swing.JLabel();
        usersPanel = new javax.swing.JPanel();
        usersPanelTitle = new javax.swing.JLabel();
        jScrollPane6 = new javax.swing.JScrollPane();
        usersTable = new javax.swing.JTable();
        newUser = new javax.swing.JButton();
        editUser = new javax.swing.JButton();
        deleteUser = new javax.swing.JButton();
        refreshUsers = new javax.swing.JButton();
        productsPanel = new javax.swing.JPanel();
        productsPanelTitle = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        productsTable = new javax.swing.JTable();
        newProduct = new javax.swing.JButton();
        editProduct = new javax.swing.JButton();
        deleteProduct = new javax.swing.JButton();
        refreshProducts = new javax.swing.JButton();

        orderModal.setTitle("Nueva orden");
        orderModal.setAlwaysOnTop(true);
        orderModal.setLocation(new java.awt.Point(0, 0));
        orderModal.setModal(true);
        orderModal.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentHidden(java.awt.event.ComponentEvent evt) {
                orderModalComponentHidden(evt);
            }
            public void componentShown(java.awt.event.ComponentEvent evt) {
                orderModalComponentShown(evt);
            }
        });

        jLabel9.setFont(new java.awt.Font("Inter", 0, 14)); // NOI18N
        jLabel9.setText("Número de mesa");

        jLabel10.setFont(new java.awt.Font("Inter", 0, 14)); // NOI18N
        jLabel10.setText("Clientes");

        jLabel13.setFont(new java.awt.Font("Inter", 0, 14)); // NOI18N
        jLabel13.setText("Subtotal");

        jLabel14.setFont(new java.awt.Font("Inter", 0, 14)); // NOI18N
        jLabel14.setText("Comentario");

        modalOrderTable.setFont(new java.awt.Font("Inter", 0, 12)); // NOI18N
        modalOrderTable.setModel(new javax.swing.SpinnerNumberModel(1, 1, null, 1));

        modalOrderClients.setFont(new java.awt.Font("Inter", 0, 12)); // NOI18N
        modalOrderClients.setModel(new javax.swing.SpinnerNumberModel(1, 1, null, 1));

        modalProductPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Productos", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Inter", 0, 18))); // NOI18N
        modalProductPanel.setMinimumSize(new java.awt.Dimension(350, 100));
        modalProductPanel.setPreferredSize(new java.awt.Dimension(350, 100));

        modalProductsTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Nombre", "Precio", "Stock"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        modalProductsTable.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        modalProductsTable.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane3.setViewportView(modalProductsTable);

        modalDeleteProduct.setText("Eliminar");
        modalDeleteProduct.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                modalDeleteProductActionPerformed(evt);
            }
        });

        modalAddProduct.setText("Agregar");
        modalAddProduct.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                modalAddProductActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout modalProductPanelLayout = new javax.swing.GroupLayout(modalProductPanel);
        modalProductPanel.setLayout(modalProductPanelLayout);
        modalProductPanelLayout.setHorizontalGroup(
            modalProductPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, modalProductPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(modalProductPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addGroup(modalProductPanelLayout.createSequentialGroup()
                        .addComponent(modalProductCombo, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(modalAddProduct)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(modalDeleteProduct, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        modalProductPanelLayout.setVerticalGroup(
            modalProductPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, modalProductPanelLayout.createSequentialGroup()
                .addContainerGap(16, Short.MAX_VALUE)
                .addGroup(modalProductPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(modalDeleteProduct)
                    .addComponent(modalProductCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(modalAddProduct))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jLabel15.setFont(new java.awt.Font("Inter", 0, 14)); // NOI18N
        jLabel15.setText("Total");

        modalOrderCancel.setBackground(new java.awt.Color(255, 0, 0));
        modalOrderCancel.setFont(new java.awt.Font("Inter", 1, 14)); // NOI18N
        modalOrderCancel.setForeground(new java.awt.Color(255, 255, 255));
        modalOrderCancel.setText("Cancelar");
        modalOrderCancel.setBorderPainted(false);
        modalOrderCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                modalOrderCancelActionPerformed(evt);
            }
        });

        modalOrderConfirm.setBackground(new java.awt.Color(51, 51, 255));
        modalOrderConfirm.setFont(new java.awt.Font("Inter", 1, 14)); // NOI18N
        modalOrderConfirm.setForeground(new java.awt.Color(255, 255, 255));
        modalOrderConfirm.setText("Confirmar");
        modalOrderConfirm.setBorderPainted(false);
        modalOrderConfirm.setEnabled(false);
        modalOrderConfirm.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                modalOrderConfirmActionPerformed(evt);
            }
        });

        modalProductComment.setColumns(20);
        modalProductComment.setRows(5);
        jScrollPane1.setViewportView(modalProductComment);

        modalOrderSubtotal.setFont(new java.awt.Font("Inter", 0, 12)); // NOI18N
        modalOrderSubtotal.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        modalOrderSubtotal.setText("$0.00");

        modalOrderTotal.setFont(new java.awt.Font("Inter", 0, 12)); // NOI18N
        modalOrderTotal.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        modalOrderTotal.setText("$0.00");

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addComponent(jLabel9)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(modalOrderTable, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(30, 30, 30)
                        .addComponent(jLabel10)
                        .addGap(27, 27, 27)
                        .addComponent(modalOrderClients, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 18, Short.MAX_VALUE))
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(modalProductPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(modalOrderSubtotal, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(modalOrderTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel8Layout.createSequentialGroup()
                                .addComponent(modalOrderCancel)
                                .addGap(18, 18, 18)
                                .addComponent(modalOrderConfirm)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(modalOrderTable, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel10)
                    .addComponent(modalOrderClients, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel13)
                    .addComponent(modalOrderSubtotal))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addGap(11, 11, 11)
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel15)
                            .addComponent(modalOrderTotal))
                        .addGap(18, 18, 18)
                        .addComponent(jLabel14)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(modalOrderConfirm)
                            .addComponent(modalOrderCancel)))
                    .addComponent(modalProductPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 234, Short.MAX_VALUE))
                .addContainerGap())
        );

        javax.swing.GroupLayout orderModalLayout = new javax.swing.GroupLayout(orderModal.getContentPane());
        orderModal.getContentPane().setLayout(orderModalLayout);
        orderModalLayout.setHorizontalGroup(
            orderModalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(orderModalLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        orderModalLayout.setVerticalGroup(
            orderModalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(orderModalLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        loginFrame.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        loginFrame.setTitle("Iniciar sesión");
        loginFrame.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentHidden(java.awt.event.ComponentEvent evt) {
                loginFrameComponentHidden(evt);
            }
        });

        jPanel10.setBackground(new java.awt.Color(51, 51, 255));

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 161, Short.MAX_VALUE)
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        jLabel11.setFont(new java.awt.Font("Inter", 1, 18)); // NOI18N
        jLabel11.setText("Cafetería UCC");

        jLabel17.setFont(new java.awt.Font("Inter", 0, 12)); // NOI18N
        jLabel17.setText("Usuario");

        loginUsername.setFont(new java.awt.Font("Inter", 0, 11)); // NOI18N

        jLabel19.setFont(new java.awt.Font("Inter", 0, 12)); // NOI18N
        jLabel19.setText("Contraseña");

        loginPassword.setFont(new java.awt.Font("Inter", 0, 11)); // NOI18N

        login.setFont(new java.awt.Font("Inter", 1, 14)); // NOI18N
        login.setText("Iniciar sesión");
        login.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                loginActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout loginFrameLayout = new javax.swing.GroupLayout(loginFrame.getContentPane());
        loginFrame.getContentPane().setLayout(loginFrameLayout);
        loginFrameLayout.setHorizontalGroup(
            loginFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(loginFrameLayout.createSequentialGroup()
                .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(loginFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(loginFrameLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(loginFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(loginUsername, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(loginPassword, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel11, javax.swing.GroupLayout.DEFAULT_SIZE, 215, Short.MAX_VALUE)
                            .addComponent(jLabel17, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel19, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(12, 12, 12))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, loginFrameLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(login)
                        .addContainerGap())))
        );
        loginFrameLayout.setVerticalGroup(
            loginFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(loginFrameLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(loginUsername, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(loginPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 63, Short.MAX_VALUE)
                .addComponent(login, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        productModal.setTitle("Nuevo producto");
        productModal.setAlwaysOnTop(true);
        productModal.setLocation(new java.awt.Point(0, 0));
        productModal.setModal(true);
        productModal.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                productModalComponentShown(evt);
            }
        });

        jLabel20.setFont(new java.awt.Font("Inter", 0, 14)); // NOI18N
        jLabel20.setText("Nombre");

        jLabel21.setFont(new java.awt.Font("Inter", 0, 14)); // NOI18N
        jLabel21.setText("Stock");

        productModalStock.setFont(new java.awt.Font("Inter", 0, 12)); // NOI18N
        productModalStock.setModel(new javax.swing.SpinnerNumberModel(1, 0, null, 1));
        productModalStock.setMinimumSize(new java.awt.Dimension(200, 30));
        productModalStock.setPreferredSize(new java.awt.Dimension(200, 30));

        productModalPrice.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0.00"))));
        productModalPrice.setFont(new java.awt.Font("Inter", 0, 12)); // NOI18N
        productModalPrice.setMinimumSize(new java.awt.Dimension(200, 30));
        productModalPrice.setPreferredSize(new java.awt.Dimension(200, 30));

        jLabel25.setFont(new java.awt.Font("Inter", 0, 14)); // NOI18N
        jLabel25.setText("Precio");

        productModalConfirm.setBackground(new java.awt.Color(51, 51, 255));
        productModalConfirm.setFont(new java.awt.Font("Inter", 1, 14)); // NOI18N
        productModalConfirm.setForeground(new java.awt.Color(255, 255, 255));
        productModalConfirm.setText("Confirmar");
        productModalConfirm.setBorderPainted(false);
        productModalConfirm.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                productModalConfirmActionPerformed(evt);
            }
        });

        productModalCancel.setBackground(new java.awt.Color(255, 0, 0));
        productModalCancel.setFont(new java.awt.Font("Inter", 1, 14)); // NOI18N
        productModalCancel.setForeground(new java.awt.Color(255, 255, 255));
        productModalCancel.setText("Cancelar");
        productModalCancel.setBorderPainted(false);
        productModalCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                productModalCancelActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(productModalConfirm, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(productModalCancel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(productModalConfirm)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(productModalCancel)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        productModalName.setMinimumSize(new java.awt.Dimension(200, 30));
        productModalName.setPreferredSize(new java.awt.Dimension(200, 30));

        jLabel22.setFont(new java.awt.Font("Inter", 0, 14)); // NOI18N
        jLabel22.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel22.setText("$");

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel12, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel25)
                    .addComponent(jLabel21)
                    .addComponent(jLabel20))
                .addGap(30, 30, 30)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(productModalStock, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(productModalName, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel11Layout.createSequentialGroup()
                        .addComponent(jLabel22, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(productModalPrice, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(productModalName, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel20))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(productModalPrice, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel25)
                    .addComponent(jLabel22))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(productModalStock, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel21))
                .addGap(18, 18, 18)
                .addComponent(jPanel12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout productModalLayout = new javax.swing.GroupLayout(productModal.getContentPane());
        productModal.getContentPane().setLayout(productModalLayout);
        productModalLayout.setHorizontalGroup(
            productModalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(productModalLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        productModalLayout.setVerticalGroup(
            productModalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel11, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        userModal.setTitle("Nuevo usuario");
        userModal.setAlwaysOnTop(true);
        userModal.setLocation(new java.awt.Point(0, 0));
        userModal.setModal(true);
        userModal.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                userModalComponentShown(evt);
            }
        });

        jLabel23.setFont(new java.awt.Font("Inter", 0, 14)); // NOI18N
        jLabel23.setText("Nombre");

        userModalPasswordConfirmLabel.setFont(new java.awt.Font("Inter", 0, 14)); // NOI18N
        userModalPasswordConfirmLabel.setText("Confirmar contraseña");

        jLabel26.setFont(new java.awt.Font("Inter", 0, 14)); // NOI18N
        jLabel26.setText("RFC");

        userModalConfirm.setBackground(new java.awt.Color(51, 51, 255));
        userModalConfirm.setFont(new java.awt.Font("Inter", 1, 14)); // NOI18N
        userModalConfirm.setForeground(new java.awt.Color(255, 255, 255));
        userModalConfirm.setText("Confirmar");
        userModalConfirm.setBorderPainted(false);
        userModalConfirm.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                userModalConfirmActionPerformed(evt);
            }
        });

        userModalCancel.setBackground(new java.awt.Color(255, 0, 0));
        userModalCancel.setFont(new java.awt.Font("Inter", 1, 14)); // NOI18N
        userModalCancel.setForeground(new java.awt.Color(255, 255, 255));
        userModalCancel.setText("Cancelar");
        userModalCancel.setBorderPainted(false);
        userModalCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                userModalCancelActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel14Layout = new javax.swing.GroupLayout(jPanel14);
        jPanel14.setLayout(jPanel14Layout);
        jPanel14Layout.setHorizontalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(userModalConfirm, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(userModalCancel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel14Layout.setVerticalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(userModalConfirm)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(userModalCancel)
                .addGap(0, 18, Short.MAX_VALUE))
        );

        userModalName.setMinimumSize(new java.awt.Dimension(200, 30));
        userModalName.setPreferredSize(new java.awt.Dimension(200, 30));

        jLabel28.setFont(new java.awt.Font("Inter", 0, 14)); // NOI18N
        jLabel28.setText("Apellidos");

        userModalSurname.setMinimumSize(new java.awt.Dimension(200, 30));
        userModalSurname.setPreferredSize(new java.awt.Dimension(200, 30));

        jLabel29.setFont(new java.awt.Font("Inter", 0, 14)); // NOI18N
        jLabel29.setText("Rol");

        userModalRole.setFont(new java.awt.Font("Inter", 0, 12)); // NOI18N
        userModalRole.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Usuario", "Administrador" }));

        jLabel30.setFont(new java.awt.Font("Inter", 0, 14)); // NOI18N
        jLabel30.setText("Usuario");

        userModalPasswordLabel.setFont(new java.awt.Font("Inter", 0, 14)); // NOI18N
        userModalPasswordLabel.setText("Contraseña");

        userModalRFC.setMinimumSize(new java.awt.Dimension(200, 30));
        userModalRFC.setPreferredSize(new java.awt.Dimension(200, 30));

        userModalUsername.setMinimumSize(new java.awt.Dimension(200, 30));
        userModalUsername.setPreferredSize(new java.awt.Dimension(200, 30));

        userModalPasswordConfirm.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                userModalPasswordConfirmActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel14, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(jLabel29, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel28, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel23, javax.swing.GroupLayout.Alignment.LEADING))
                    .addComponent(jLabel30))
                .addGap(30, 30, 30)
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(userModalName, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(userModalSurname, javax.swing.GroupLayout.DEFAULT_SIZE, 356, Short.MAX_VALUE)
                    .addGroup(jPanel13Layout.createSequentialGroup()
                        .addComponent(userModalRole, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel26)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(userModalRFC, javax.swing.GroupLayout.PREFERRED_SIZE, 1, Short.MAX_VALUE))
                    .addComponent(userModalUsername, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(userModalPasswordConfirmLabel)
                    .addComponent(userModalPasswordLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(userModalPassword)
                    .addComponent(userModalPasswordConfirm)))
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(userModalName, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel23))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(userModalSurname, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel28))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel29)
                    .addComponent(userModalRole, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel26)
                    .addComponent(userModalRFC, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel30)
                    .addComponent(userModalUsername, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(userModalPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(userModalPasswordLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(userModalPasswordConfirm, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(userModalPasswordConfirmLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(3, 3, 3))
        );

        javax.swing.GroupLayout userModalLayout = new javax.swing.GroupLayout(userModal.getContentPane());
        userModal.getContentPane().setLayout(userModalLayout);
        userModalLayout.setHorizontalGroup(
            userModalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(userModalLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        userModalLayout.setVerticalGroup(
            userModalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, userModalLayout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addComponent(jPanel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Cafetería UCC");
        setBackground(javax.swing.UIManager.getDefaults().getColor("nb.html.link.foreground.visited"));
        setPreferredSize(new java.awt.Dimension(1280, 720));

        sidebar.setBackground(new java.awt.Color(51, 51, 255));

        jLabel1.setFont(new java.awt.Font("Inter", 1, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Cafetería UCC");

        ordersLink.setBackground(new java.awt.Color(51, 51, 255));
        ordersLink.setFont(new java.awt.Font("Inter", 1, 18)); // NOI18N
        ordersLink.setForeground(new java.awt.Color(255, 255, 255));
        ordersLink.setText("Órdenes");
        ordersLink.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ordersLinkActionPerformed(evt);
            }
        });

        dashboardLink.setBackground(new java.awt.Color(255, 255, 255));
        dashboardLink.setFont(new java.awt.Font("Inter", 1, 18)); // NOI18N
        dashboardLink.setForeground(new java.awt.Color(0, 0, 0));
        dashboardLink.setText("Dashboard");
        dashboardLink.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dashboardLinkActionPerformed(evt);
            }
        });

        productsLink.setBackground(new java.awt.Color(51, 51, 255));
        productsLink.setFont(new java.awt.Font("Inter", 1, 18)); // NOI18N
        productsLink.setForeground(new java.awt.Color(255, 255, 255));
        productsLink.setText("Productos");
        productsLink.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                productsLinkActionPerformed(evt);
            }
        });

        statsLink.setBackground(new java.awt.Color(51, 51, 255));
        statsLink.setFont(new java.awt.Font("Inter", 1, 18)); // NOI18N
        statsLink.setForeground(new java.awt.Color(255, 255, 255));
        statsLink.setText("Estadísticas");
        statsLink.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                statsLinkActionPerformed(evt);
            }
        });

        usersLink.setBackground(new java.awt.Color(51, 51, 255));
        usersLink.setFont(new java.awt.Font("Inter", 1, 18)); // NOI18N
        usersLink.setForeground(new java.awt.Color(255, 255, 255));
        usersLink.setText("Usuarios");
        usersLink.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                usersLinkActionPerformed(evt);
            }
        });

        logoutLink.setBackground(new java.awt.Color(255, 51, 51));
        logoutLink.setFont(new java.awt.Font("Inter", 1, 18)); // NOI18N
        logoutLink.setForeground(new java.awt.Color(255, 255, 255));
        logoutLink.setText("Cerrar sesión");
        logoutLink.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                logoutLinkActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout sidebarLayout = new javax.swing.GroupLayout(sidebar);
        sidebar.setLayout(sidebarLayout);
        sidebarLayout.setHorizontalGroup(
            sidebarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(ordersLink, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(dashboardLink, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(sidebarLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(118, Short.MAX_VALUE))
            .addComponent(productsLink, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(statsLink, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(usersLink, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(logoutLink, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        sidebarLayout.setVerticalGroup(
            sidebarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(sidebarLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(17, 17, 17)
                .addComponent(dashboardLink, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(ordersLink, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(productsLink, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(statsLink, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(usersLink, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 296, Short.MAX_VALUE)
                .addComponent(logoutLink, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(63, 63, 63))
        );

        main.setBackground(new java.awt.Color(255, 255, 255));
        main.setPreferredSize(new java.awt.Dimension(960, 0));
        main.setLayout(new java.awt.CardLayout());

        dashboardPanel.setBackground(new java.awt.Color(255, 255, 255));
        dashboardPanel.setName("Dashboard"); // NOI18N
        dashboardPanel.setPreferredSize(new java.awt.Dimension(960, 720));
        dashboardPanel.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                dashboardPanelComponentShown(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Inter", 1, 24)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(0, 0, 0));
        jLabel2.setText("Dashboard");

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(51, 51, 255), 2, true));

        dashboardOrderCount.setFont(new java.awt.Font("Inter", 0, 24)); // NOI18N
        dashboardOrderCount.setForeground(new java.awt.Color(0, 0, 0));
        dashboardOrderCount.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        dashboardOrderCount.setText("---");

        jLabel4.setFont(new java.awt.Font("Inter", 0, 18)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(0, 0, 0));
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setText("Órdenes");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel4)
                    .addComponent(dashboardOrderCount, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(104, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addComponent(dashboardOrderCount, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(39, Short.MAX_VALUE))
        );

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));
        jPanel3.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(51, 51, 255), 2, true));

        dashboardClientCount.setFont(new java.awt.Font("Inter", 0, 24)); // NOI18N
        dashboardClientCount.setForeground(new java.awt.Color(0, 0, 0));
        dashboardClientCount.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        dashboardClientCount.setText("---");

        jLabel5.setFont(new java.awt.Font("Inter", 0, 18)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(0, 0, 0));
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel5.setText("Clientes");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(dashboardClientCount, javax.swing.GroupLayout.DEFAULT_SIZE, 73, Short.MAX_VALUE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(104, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addComponent(dashboardClientCount, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(39, Short.MAX_VALUE))
        );

        jPanel5.setBackground(new java.awt.Color(255, 255, 255));
        jPanel5.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(51, 51, 255), 2, true));

        dashboardTotalEarnings.setFont(new java.awt.Font("Inter", 0, 24)); // NOI18N
        dashboardTotalEarnings.setForeground(new java.awt.Color(0, 0, 0));
        dashboardTotalEarnings.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        dashboardTotalEarnings.setText("---");

        jLabel7.setFont(new java.awt.Font("Inter", 0, 18)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(0, 0, 0));
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel7.setText("Ganancias");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(dashboardTotalEarnings, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(104, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addComponent(dashboardTotalEarnings, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(39, Short.MAX_VALUE))
        );

        jLabel12.setFont(new java.awt.Font("Inter", 1, 22)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(0, 0, 0));
        jLabel12.setText("Órdenes pendientes");

        jPanel4.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        dashboardCurrentOrders.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Mesa", "Clientes", "Hora inicio", "Total", "Empleado"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        dashboardCurrentOrders.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane5.setViewportView(dashboardCurrentOrders);

        dashboardCancelOrder.setBackground(new java.awt.Color(255, 51, 51));
        dashboardCancelOrder.setFont(new java.awt.Font("Inter", 1, 14)); // NOI18N
        dashboardCancelOrder.setForeground(new java.awt.Color(255, 255, 255));
        dashboardCancelOrder.setText("Cancelar");
        dashboardCancelOrder.setBorderPainted(false);
        dashboardCancelOrder.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dashboardCancelOrderActionPerformed(evt);
            }
        });

        dashboardCompleteOrder.setBackground(new java.awt.Color(51, 255, 51));
        dashboardCompleteOrder.setFont(new java.awt.Font("Inter", 1, 14)); // NOI18N
        dashboardCompleteOrder.setForeground(new java.awt.Color(255, 255, 255));
        dashboardCompleteOrder.setText("Completar");
        dashboardCompleteOrder.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dashboardCompleteOrderActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane5)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(dashboardCancelOrder, javax.swing.GroupLayout.PREFERRED_SIZE, 226, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 154, Short.MAX_VALUE)
                        .addComponent(dashboardCompleteOrder, javax.swing.GroupLayout.PREFERRED_SIZE, 228, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(dashboardCompleteOrder, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(dashboardCancelOrder, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(56, 56, 56))
        );

        jPanel9.setBackground(new java.awt.Color(255, 255, 255));
        jPanel9.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));

        jLabel16.setFont(new java.awt.Font("Inter", 1, 22)); // NOI18N
        jLabel16.setForeground(new java.awt.Color(0, 0, 0));
        jLabel16.setText("Le damos la bienvenida, ");

        dashboardUsername.setFont(new java.awt.Font("Inter", 0, 18)); // NOI18N
        dashboardUsername.setForeground(new java.awt.Color(0, 0, 0));
        dashboardUsername.setText("usuario");

        jLabel18.setFont(new java.awt.Font("Inter", 0, 18)); // NOI18N
        jLabel18.setForeground(new java.awt.Color(0, 0, 0));
        jLabel18.setText("Hoy es");

        dashboardDay.setFont(new java.awt.Font("Inter", 1, 26)); // NOI18N
        dashboardDay.setForeground(new java.awt.Color(0, 0, 0));
        dashboardDay.setText("día y numero");

        dashboardMonth.setFont(new java.awt.Font("Inter", 0, 24)); // NOI18N
        dashboardMonth.setForeground(new java.awt.Color(0, 0, 0));
        dashboardMonth.setText("de mes del");

        dashboardYear.setFont(new java.awt.Font("Inter", 0, 20)); // NOI18N
        dashboardYear.setForeground(new java.awt.Color(0, 0, 0));
        dashboardYear.setText("año");

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel16)
                    .addComponent(dashboardUsername, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 227, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(dashboardDay, javax.swing.GroupLayout.PREFERRED_SIZE, 227, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(dashboardMonth, javax.swing.GroupLayout.PREFERRED_SIZE, 227, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(dashboardYear, javax.swing.GroupLayout.PREFERRED_SIZE, 227, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel16, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(dashboardUsername, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(dashboardDay, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(dashboardMonth)
                .addGap(18, 18, 18)
                .addComponent(dashboardYear)
                .addGap(61, 61, 61))
        );

        javax.swing.GroupLayout dashboardPanelLayout = new javax.swing.GroupLayout(dashboardPanel);
        dashboardPanel.setLayout(dashboardPanelLayout);
        dashboardPanelLayout.setHorizontalGroup(
            dashboardPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(dashboardPanelLayout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addGroup(dashboardPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(dashboardPanelLayout.createSequentialGroup()
                        .addGap(17, 17, 17)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 117, Short.MAX_VALUE)
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(110, 110, 110)
                        .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(118, 118, 118))
                    .addGroup(dashboardPanelLayout.createSequentialGroup()
                        .addGroup(dashboardPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(dashboardPanelLayout.createSequentialGroup()
                                .addGroup(dashboardPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 247, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        dashboardPanelLayout.setVerticalGroup(
            dashboardPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(dashboardPanelLayout.createSequentialGroup()
                .addGap(34, 34, 34)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(dashboardPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(93, 93, 93)
                .addComponent(jLabel12)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(dashboardPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, 312, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, Short.MAX_VALUE))
        );

        main.add(dashboardPanel, "Dashboard");

        ordersPanel.setBackground(new java.awt.Color(255, 255, 255));
        ordersPanel.setMaximumSize(new java.awt.Dimension(960, 720));
        ordersPanel.setMinimumSize(new java.awt.Dimension(960, 720));
        ordersPanel.setName("Orders"); // NOI18N
        ordersPanel.setPreferredSize(new java.awt.Dimension(960, 720));
        ordersPanel.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                ordersPanelComponentShown(evt);
            }
        });

        ordersPanelTitle.setFont(new java.awt.Font("Inter", 1, 24)); // NOI18N
        ordersPanelTitle.setForeground(new java.awt.Color(0, 0, 0));
        ordersPanelTitle.setText("Órdenes");

        newOrder.setBackground(new java.awt.Color(51, 51, 255));
        newOrder.setFont(new java.awt.Font("Inter", 1, 14)); // NOI18N
        newOrder.setForeground(new java.awt.Color(255, 255, 255));
        newOrder.setText("Nueva orden");
        newOrder.setBorderPainted(false);
        newOrder.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newOrderActionPerformed(evt);
            }
        });

        editOrder.setBackground(new java.awt.Color(255, 255, 255));
        editOrder.setFont(new java.awt.Font("Inter", 1, 14)); // NOI18N
        editOrder.setForeground(new java.awt.Color(0, 0, 0));
        editOrder.setText("Editar orden");
        editOrder.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(51, 51, 255)));
        editOrder.setBorderPainted(false);
        editOrder.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editOrderActionPerformed(evt);
            }
        });

        deleteOrder.setBackground(new java.awt.Color(255, 0, 0));
        deleteOrder.setFont(new java.awt.Font("Inter", 1, 14)); // NOI18N
        deleteOrder.setForeground(new java.awt.Color(255, 255, 255));
        deleteOrder.setText("Eliminar orden");
        deleteOrder.setBorderPainted(false);
        deleteOrder.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteOrderActionPerformed(evt);
            }
        });

        refreshOrders.setBackground(new java.awt.Color(255, 255, 255));
        refreshOrders.setFont(new java.awt.Font("Inter", 1, 14)); // NOI18N
        refreshOrders.setForeground(new java.awt.Color(0, 0, 0));
        refreshOrders.setText("Refrescar");
        refreshOrders.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                refreshOrdersActionPerformed(evt);
            }
        });

        ordersTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Mesa", "Clientes", "Orden", "Estado", "Hora inicio", "Hora fin", "Subtotal", "Total", "Comentario", "Empleado"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        ordersTable.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        ordersTable.setMaximumSize(new java.awt.Dimension(920, 0));
        ordersTable.setShowGrid(false);
        ordersTable.getTableHeader().setReorderingAllowed(false);
        jScrollPane4.setViewportView(ordersTable);
        if (ordersTable.getColumnModel().getColumnCount() > 0) {
            ordersTable.getColumnModel().getColumn(0).setPreferredWidth(30);
            ordersTable.getColumnModel().getColumn(1).setPreferredWidth(40);
        }

        javax.swing.GroupLayout ordersPanelLayout = new javax.swing.GroupLayout(ordersPanel);
        ordersPanel.setLayout(ordersPanelLayout);
        ordersPanelLayout.setHorizontalGroup(
            ordersPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ordersPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(ordersPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(ordersPanelLayout.createSequentialGroup()
                        .addComponent(newOrder, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(editOrder, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(deleteOrder, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(refreshOrders, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 923, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(ordersPanelTitle, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 533, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 75, Short.MAX_VALUE))
        );
        ordersPanelLayout.setVerticalGroup(
            ordersPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ordersPanelLayout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addComponent(ordersPanelTitle, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(32, 32, 32)
                .addGroup(ordersPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(newOrder, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(editOrder, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(deleteOrder, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(refreshOrders, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 466, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(114, Short.MAX_VALUE))
        );

        main.add(ordersPanel, "Órdenes");

        statsPanel.setBackground(new java.awt.Color(255, 255, 255));
        statsPanel.setName("Orders"); // NOI18N
        statsPanel.setPreferredSize(new java.awt.Dimension(960, 720));
        statsPanel.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                statsPanelComponentShown(evt);
            }
        });

        jLabel8.setFont(new java.awt.Font("Inter", 1, 24)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(0, 0, 0));
        jLabel8.setText("Estadísticas");

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Productos", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Inter", 1, 12), new java.awt.Color(0, 0, 0))); // NOI18N
        jPanel1.setForeground(new java.awt.Color(0, 0, 0));

        statsTotalProducts.setFont(new java.awt.Font("Inter", 0, 16)); // NOI18N
        statsTotalProducts.setForeground(new java.awt.Color(0, 0, 0));
        statsTotalProducts.setText("Total: ");

        statsMostRequestedProduct.setFont(new java.awt.Font("Inter", 0, 16)); // NOI18N
        statsMostRequestedProduct.setForeground(new java.awt.Color(0, 0, 0));
        statsMostRequestedProduct.setText("Más pedido:");

        statsCheapestProduct.setFont(new java.awt.Font("Inter", 0, 16)); // NOI18N
        statsCheapestProduct.setForeground(new java.awt.Color(0, 0, 0));
        statsCheapestProduct.setText("Más barato:");

        statsMostExpensiveProduct.setFont(new java.awt.Font("Inter", 0, 16)); // NOI18N
        statsMostExpensiveProduct.setForeground(new java.awt.Color(0, 0, 0));
        statsMostExpensiveProduct.setText("Más caro:");

        statsLeastStock.setFont(new java.awt.Font("Inter", 0, 16)); // NOI18N
        statsLeastStock.setForeground(new java.awt.Color(0, 0, 0));
        statsLeastStock.setText("Menor stock:");

        statsMostStock.setFont(new java.awt.Font("Inter", 0, 16)); // NOI18N
        statsMostStock.setForeground(new java.awt.Color(0, 0, 0));
        statsMostStock.setText("Mayor stock:");

        statsAverageProductCost.setFont(new java.awt.Font("Inter", 0, 16)); // NOI18N
        statsAverageProductCost.setForeground(new java.awt.Color(0, 0, 0));
        statsAverageProductCost.setText("Costo promedio:");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(statsAverageProductCost, javax.swing.GroupLayout.DEFAULT_SIZE, 325, Short.MAX_VALUE)
                    .addComponent(statsTotalProducts, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(statsMostRequestedProduct, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(statsCheapestProduct, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(statsMostExpensiveProduct, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(statsLeastStock, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(statsMostStock, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(statsTotalProducts)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(statsMostRequestedProduct)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(statsCheapestProduct)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(statsMostExpensiveProduct)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(statsLeastStock)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(statsMostStock)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(statsAverageProductCost)
                .addContainerGap())
        );

        jPanel6.setBackground(new java.awt.Color(255, 255, 255));
        jPanel6.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Órdenes", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Inter", 1, 12), new java.awt.Color(0, 0, 0))); // NOI18N
        jPanel6.setForeground(new java.awt.Color(0, 0, 0));

        statsTotalOrders.setFont(new java.awt.Font("Inter", 0, 16)); // NOI18N
        statsTotalOrders.setForeground(new java.awt.Color(0, 0, 0));
        statsTotalOrders.setText("Total: ");

        statsMostFrequentTable.setFont(new java.awt.Font("Inter", 0, 16)); // NOI18N
        statsMostFrequentTable.setForeground(new java.awt.Color(0, 0, 0));
        statsMostFrequentTable.setText("Mesa más frecuente: ");

        statsAverageClients.setFont(new java.awt.Font("Inter", 0, 16)); // NOI18N
        statsAverageClients.setForeground(new java.awt.Color(0, 0, 0));
        statsAverageClients.setText("Clientes promedio:");

        statsAverageProducts.setFont(new java.awt.Font("Inter", 0, 16)); // NOI18N
        statsAverageProducts.setForeground(new java.awt.Color(0, 0, 0));
        statsAverageProducts.setText("Productos promedio:");

        statsMaxProducts.setFont(new java.awt.Font("Inter", 0, 16)); // NOI18N
        statsMaxProducts.setForeground(new java.awt.Color(0, 0, 0));
        statsMaxProducts.setText("Máximo de productos:");

        statsLongestOrder.setFont(new java.awt.Font("Inter", 0, 16)); // NOI18N
        statsLongestOrder.setForeground(new java.awt.Color(0, 0, 0));
        statsLongestOrder.setText("Orden más larga:");

        statsMostExpensiveOrder.setFont(new java.awt.Font("Inter", 0, 16)); // NOI18N
        statsMostExpensiveOrder.setForeground(new java.awt.Color(0, 0, 0));
        statsMostExpensiveOrder.setText("Orden más cara:");

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(statsTotalOrders, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(statsMostFrequentTable, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(statsAverageClients, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(statsAverageProducts, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(statsMaxProducts, javax.swing.GroupLayout.DEFAULT_SIZE, 417, Short.MAX_VALUE)
                    .addComponent(statsLongestOrder, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(statsMostExpensiveOrder, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(statsTotalOrders)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(statsMostFrequentTable)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(statsAverageClients)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(statsAverageProducts)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(statsMaxProducts)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(statsLongestOrder)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(statsMostExpensiveOrder)
                .addContainerGap())
        );

        jPanel7.setBackground(new java.awt.Color(255, 255, 255));
        jPanel7.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Empleados", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Inter", 1, 12), new java.awt.Color(0, 0, 0))); // NOI18N
        jPanel7.setForeground(new java.awt.Color(0, 0, 0));

        statsTotalEmployees.setFont(new java.awt.Font("Inter", 0, 16)); // NOI18N
        statsTotalEmployees.setForeground(new java.awt.Color(0, 0, 0));
        statsTotalEmployees.setText("Total: ");

        statsMostAttendedOrders.setFont(new java.awt.Font("Inter", 0, 16)); // NOI18N
        statsMostAttendedOrders.setForeground(new java.awt.Color(0, 0, 0));
        statsMostAttendedOrders.setText("Más órdenes atendidas:");

        statsOldestEmployee.setFont(new java.awt.Font("Inter", 0, 16)); // NOI18N
        statsOldestEmployee.setForeground(new java.awt.Color(0, 0, 0));
        statsOldestEmployee.setText("Mayor antigüedad:");

        statsMostRecentEmployee.setFont(new java.awt.Font("Inter", 0, 16)); // NOI18N
        statsMostRecentEmployee.setForeground(new java.awt.Color(0, 0, 0));
        statsMostRecentEmployee.setText("Más reciente:");

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(statsTotalEmployees, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(statsMostAttendedOrders, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(statsOldestEmployee, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(statsMostRecentEmployee, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(statsTotalEmployees)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(statsMostAttendedOrders)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(statsOldestEmployee)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(statsMostRecentEmployee)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout statsPanelLayout = new javax.swing.GroupLayout(statsPanel);
        statsPanel.setLayout(statsPanelLayout);
        statsPanelLayout.setHorizontalGroup(
            statsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(statsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(statsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(statsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(statsPanelLayout.createSequentialGroup()
                            .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(34, 34, 34)
                            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 181, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(154, Short.MAX_VALUE))
        );
        statsPanelLayout.setVerticalGroup(
            statsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(statsPanelLayout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(statsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        main.add(statsPanel, "Estadísticas");

        usersPanel.setBackground(new java.awt.Color(255, 255, 255));
        usersPanel.setName("Orders"); // NOI18N
        usersPanel.setPreferredSize(new java.awt.Dimension(960, 720));
        usersPanel.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                usersPanelComponentShown(evt);
            }
        });

        usersPanelTitle.setFont(new java.awt.Font("Inter", 1, 24)); // NOI18N
        usersPanelTitle.setForeground(new java.awt.Color(0, 0, 0));
        usersPanelTitle.setText("Usuarios");

        usersTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Nombre", "Apellidos", "Rol", "Fecha de inicio", "RFC", "Usuario"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        usersTable.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        usersTable.setMaximumSize(new java.awt.Dimension(920, 0));
        usersTable.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        usersTable.setShowGrid(false);
        usersTable.getTableHeader().setReorderingAllowed(false);
        jScrollPane6.setViewportView(usersTable);
        if (usersTable.getColumnModel().getColumnCount() > 0) {
            usersTable.getColumnModel().getColumn(0).setPreferredWidth(30);
            usersTable.getColumnModel().getColumn(1).setPreferredWidth(40);
        }

        newUser.setBackground(new java.awt.Color(51, 51, 255));
        newUser.setFont(new java.awt.Font("Inter", 1, 14)); // NOI18N
        newUser.setForeground(new java.awt.Color(255, 255, 255));
        newUser.setText("Nuevo usuario");
        newUser.setBorderPainted(false);
        newUser.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newUserActionPerformed(evt);
            }
        });

        editUser.setBackground(new java.awt.Color(255, 255, 255));
        editUser.setFont(new java.awt.Font("Inter", 1, 14)); // NOI18N
        editUser.setForeground(new java.awt.Color(0, 0, 0));
        editUser.setText("Editar usuario");
        editUser.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(51, 51, 255), 1, true));
        editUser.setBorderPainted(false);
        editUser.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editUserActionPerformed(evt);
            }
        });

        deleteUser.setBackground(new java.awt.Color(255, 0, 0));
        deleteUser.setFont(new java.awt.Font("Inter", 1, 14)); // NOI18N
        deleteUser.setForeground(new java.awt.Color(255, 255, 255));
        deleteUser.setText("Eliminar usuario");
        deleteUser.setBorderPainted(false);
        deleteUser.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteUserActionPerformed(evt);
            }
        });

        refreshUsers.setBackground(new java.awt.Color(255, 255, 255));
        refreshUsers.setFont(new java.awt.Font("Inter", 1, 14)); // NOI18N
        refreshUsers.setForeground(new java.awt.Color(0, 0, 0));
        refreshUsers.setText("Refrescar");
        refreshUsers.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                refreshUsersActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout usersPanelLayout = new javax.swing.GroupLayout(usersPanel);
        usersPanel.setLayout(usersPanelLayout);
        usersPanelLayout.setHorizontalGroup(
            usersPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(usersPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(usersPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(usersPanelLayout.createSequentialGroup()
                        .addComponent(usersPanelTitle, javax.swing.GroupLayout.PREFERRED_SIZE, 520, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(usersPanelLayout.createSequentialGroup()
                        .addGroup(usersPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jScrollPane6)
                            .addGroup(usersPanelLayout.createSequentialGroup()
                                .addComponent(newUser)
                                .addGap(18, 18, 18)
                                .addComponent(editUser, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(deleteUser)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 325, Short.MAX_VALUE)
                                .addComponent(refreshUsers, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(77, 77, 77))))
        );
        usersPanelLayout.setVerticalGroup(
            usersPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(usersPanelLayout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addComponent(usersPanelTitle, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(32, 32, 32)
                .addGroup(usersPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(newUser, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(editUser, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(deleteUser, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(refreshUsers, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 26, Short.MAX_VALUE)
                .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 456, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(110, 110, 110))
        );

        main.add(usersPanel, "Usuarios");

        productsPanel.setBackground(new java.awt.Color(255, 255, 255));
        productsPanel.setName("Orders"); // NOI18N
        productsPanel.setPreferredSize(new java.awt.Dimension(960, 720));
        productsPanel.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                productsPanelComponentShown(evt);
            }
        });

        productsPanelTitle.setFont(new java.awt.Font("Inter", 1, 24)); // NOI18N
        productsPanelTitle.setForeground(new java.awt.Color(0, 0, 0));
        productsPanelTitle.setText("Productos");

        productsTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Nombre", "Precio", "Stock"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, true, true, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        productsTable.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        productsTable.setMaximumSize(new java.awt.Dimension(920, 0));
        productsTable.setShowGrid(false);
        productsTable.getTableHeader().setReorderingAllowed(false);
        jScrollPane2.setViewportView(productsTable);
        if (productsTable.getColumnModel().getColumnCount() > 0) {
            productsTable.getColumnModel().getColumn(0).setPreferredWidth(30);
            productsTable.getColumnModel().getColumn(1).setPreferredWidth(40);
        }

        newProduct.setBackground(new java.awt.Color(51, 51, 255));
        newProduct.setFont(new java.awt.Font("Inter", 1, 14)); // NOI18N
        newProduct.setForeground(new java.awt.Color(255, 255, 255));
        newProduct.setText("Nuevo producto");
        newProduct.setBorderPainted(false);
        newProduct.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newProductActionPerformed(evt);
            }
        });

        editProduct.setBackground(new java.awt.Color(255, 255, 255));
        editProduct.setFont(new java.awt.Font("Inter", 1, 14)); // NOI18N
        editProduct.setForeground(new java.awt.Color(0, 0, 0));
        editProduct.setText("Editar producto");
        editProduct.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(51, 51, 255), 1, true));
        editProduct.setBorderPainted(false);
        editProduct.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editProductActionPerformed(evt);
            }
        });

        deleteProduct.setBackground(new java.awt.Color(255, 0, 0));
        deleteProduct.setFont(new java.awt.Font("Inter", 1, 14)); // NOI18N
        deleteProduct.setForeground(new java.awt.Color(255, 255, 255));
        deleteProduct.setText("Eliminar producto");
        deleteProduct.setBorderPainted(false);
        deleteProduct.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteProductActionPerformed(evt);
            }
        });

        refreshProducts.setBackground(new java.awt.Color(255, 255, 255));
        refreshProducts.setFont(new java.awt.Font("Inter", 1, 14)); // NOI18N
        refreshProducts.setForeground(new java.awt.Color(0, 0, 0));
        refreshProducts.setText("Refrescar");
        refreshProducts.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                refreshProductsActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout productsPanelLayout = new javax.swing.GroupLayout(productsPanel);
        productsPanel.setLayout(productsPanelLayout);
        productsPanelLayout.setHorizontalGroup(
            productsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(productsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(productsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(productsPanelLayout.createSequentialGroup()
                        .addComponent(productsPanelTitle, javax.swing.GroupLayout.PREFERRED_SIZE, 520, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(productsPanelLayout.createSequentialGroup()
                        .addGroup(productsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jScrollPane2)
                            .addGroup(productsPanelLayout.createSequentialGroup()
                                .addComponent(newProduct)
                                .addGap(18, 18, 18)
                                .addComponent(editProduct, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(deleteProduct)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 301, Short.MAX_VALUE)
                                .addComponent(refreshProducts, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(77, 77, 77))))
        );
        productsPanelLayout.setVerticalGroup(
            productsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(productsPanelLayout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addComponent(productsPanelTitle, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(32, 32, 32)
                .addGroup(productsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(newProduct, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(editProduct, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(deleteProduct, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(refreshProducts, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 26, Short.MAX_VALUE)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 456, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(110, 110, 110))
        );

        main.add(productsPanel, "Productos");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(sidebar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(main, javax.swing.GroupLayout.PREFERRED_SIZE, 1010, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(sidebar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(main, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void ordersLinkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ordersLinkActionPerformed
        changePanels(evt, "Órdenes");
    }//GEN-LAST:event_ordersLinkActionPerformed

    private void dashboardLinkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_dashboardLinkActionPerformed
        changePanels(evt, "Dashboard");
    }//GEN-LAST:event_dashboardLinkActionPerformed

    private void newProductActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newProductActionPerformed
        showNewProductModal();
    }//GEN-LAST:event_newProductActionPerformed

    private void deleteProductActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteProductActionPerformed
        deleteRowFromTableAndArray(productsTable, products);
        refreshProductsTable();
    }//GEN-LAST:event_deleteProductActionPerformed

    private void refreshProductsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_refreshProductsActionPerformed
        loadData();
        loadProducts();
    }//GEN-LAST:event_refreshProductsActionPerformed

    private void productsLinkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_productsLinkActionPerformed
        changePanels(evt, "Productos");
    }//GEN-LAST:event_productsLinkActionPerformed

    private void statsLinkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_statsLinkActionPerformed
        changePanels(evt, "Estadísticas");
    }//GEN-LAST:event_statsLinkActionPerformed

    private void statsPanelComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_statsPanelComponentShown
        statsTotalOrders.setText("Total: " + Integer.toString(orders.size()));
        statsMostFrequentTable.setText("Mesa más frecuente: " + frequency(getDoubleSubList(orders, 1)).intValue());
        statsAverageClients.setText("Clientes promedio: " + doubleToFixed(average(getDoubleSubList(orders, 2))));
        statsAverageProducts.setText("Productos promedio: " + doubleToFixed(average(parseListToDouble(mapProductListToQuantity(getSubList(orders, 3))))));
        statsMaxProducts.setText("Máximo de productos: " + max(parseListToDouble(mapProductListToQuantity(getSubList(orders, 3)))).intValue());
        statsLongestOrder.setText("Orden más larga: " + getLongestOrder(orders));
        statsMostExpensiveOrder.setText("Orden más cara: $" + max(getDoubleSubList(orders, 8)));

        statsTotalProducts.setText("Total: " + Integer.toString(products.size()));
        statsMostRequestedProduct.setText("Más pedido: " + getMostRequestedProduct());
        statsCheapestProduct.setText("Más barato: $" + min(getDoubleSubList(products, 2)) + " (" + getProductNameByColumn(2, doubleToFixed(min(getDoubleSubList(products, 2)))) + ")");
        statsMostExpensiveProduct.setText("Más caro: $" + max(getDoubleSubList(products, 2)) + " (" + getProductNameByColumn(2, doubleToFixed(max(getDoubleSubList(products, 2)))) + ")");
        statsLeastStock.setText("Menor stock: " + min(getDoubleSubList(products, 3)).intValue() + " (" + getProductNameByStock(min(getDoubleSubList(products, 3)).intValue()) + ")");
        statsMostStock.setText("Mayor stock: " + max(getDoubleSubList(products, 3)).intValue() + " (" + getProductNameByStock(max(getDoubleSubList(products, 3)).intValue()) + ")");
        statsAverageProductCost.setText("Costo promedio: $" + doubleToFixed(average(getDoubleSubList(products, 2))));

        statsTotalEmployees.setText("Total: " + Integer.toString(users.size()));
        statsMostAttendedOrders.setText("Más órdenes atendidas: " + getUsernameById(frequency(getDoubleSubList(orders, 10)).intValue()) + " (" + filterArrayByString(getSubList(orders, 10), frequency(getDoubleSubList(orders, 10)).intValue() + "").size() + ")");
        statsOldestEmployee.setText("Mayor antigüedad: " + getOldestEmployee());
        statsMostRecentEmployee.setText("Más reciente: " + getMostRecentEmployee());
    }//GEN-LAST:event_statsPanelComponentShown

    private void refreshOrdersActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_refreshOrdersActionPerformed
        loadData();
        loadOrders();
    }//GEN-LAST:event_refreshOrdersActionPerformed

    private void deleteOrderActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteOrderActionPerformed
        deleteRowFromTableAndArray(ordersTable, orders);
        refreshOrdersTable();
    }//GEN-LAST:event_deleteOrderActionPerformed

    private void newOrderActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newOrderActionPerformed
        showNewOrderModal();
    }//GEN-LAST:event_newOrderActionPerformed

    private void ordersPanelComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_ordersPanelComponentShown
        loadOrders();
    }//GEN-LAST:event_ordersPanelComponentShown

    private void orderModalComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_orderModalComponentShown
        loadOrderModalCombobox();
    }//GEN-LAST:event_orderModalComponentShown

    private void modalAddProductActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_modalAddProductActionPerformed
        DefaultTableModel model = (DefaultTableModel) modalProductsTable.getModel();
        int index = modalProductCombo.getSelectedIndex();
        model.addRow(products.get(index).split(","));
        updateOrderModalTotals();
    }//GEN-LAST:event_modalAddProductActionPerformed

    private void modalDeleteProductActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_modalDeleteProductActionPerformed
        deleteRowFromTable(modalProductsTable);
        updateOrderModalTotals();
    }//GEN-LAST:event_modalDeleteProductActionPerformed

    private void modalOrderCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_modalOrderCancelActionPerformed
        orderModal.setVisible(false);
        resetOrderModalFields();
    }//GEN-LAST:event_modalOrderCancelActionPerformed

    private void modalOrderConfirmActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_modalOrderConfirmActionPerformed
        createNewOrder();
    }//GEN-LAST:event_modalOrderConfirmActionPerformed

    private void dashboardPanelComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_dashboardPanelComponentShown
        refreshDashboard();
    }//GEN-LAST:event_dashboardPanelComponentShown

    private void editOrderActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editOrderActionPerformed
        if (ordersTable.getSelectedRow() != -1) {
            showEditingOrderModal();
        }
    }//GEN-LAST:event_editOrderActionPerformed

    private void dashboardCompleteOrderActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_dashboardCompleteOrderActionPerformed
        editDashboardOrder("Finalizado");
    }//GEN-LAST:event_dashboardCompleteOrderActionPerformed

    private void dashboardCancelOrderActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_dashboardCancelOrderActionPerformed
        editDashboardOrder("Cancelado");
    }//GEN-LAST:event_dashboardCancelOrderActionPerformed

    private void loginActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_loginActionPerformed
        doLogin();
    }//GEN-LAST:event_loginActionPerformed

    private void logoutLinkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_logoutLinkActionPerformed
        logout();
    }//GEN-LAST:event_logoutLinkActionPerformed

    private void productsPanelComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_productsPanelComponentShown
        loadData();
        loadProducts();
    }//GEN-LAST:event_productsPanelComponentShown

    private void loginFrameComponentHidden(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_loginFrameComponentHidden

    }//GEN-LAST:event_loginFrameComponentHidden

    private void productModalComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_productModalComponentShown

    }//GEN-LAST:event_productModalComponentShown

    private void editProductActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editProductActionPerformed
        if (productsTable.getSelectedRow() != - 1) {
            showEditingProductModal();
        }
    }//GEN-LAST:event_editProductActionPerformed

    private void productModalCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_productModalCancelActionPerformed
        productModal.setVisible(false);
        resetProductModalFields();
    }//GEN-LAST:event_productModalCancelActionPerformed

    private void productModalConfirmActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_productModalConfirmActionPerformed
        createNewProduct();
    }//GEN-LAST:event_productModalConfirmActionPerformed

    private void usersLinkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_usersLinkActionPerformed
        changePanels(evt, "Usuarios");
    }//GEN-LAST:event_usersLinkActionPerformed

    private void newUserActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newUserActionPerformed
        showNewUserModal();
    }//GEN-LAST:event_newUserActionPerformed

    private void editUserActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editUserActionPerformed
        if (usersTable.getSelectedRow() != - 1) {
            showEditingUserModal();
        }
    }//GEN-LAST:event_editUserActionPerformed

    private void deleteUserActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteUserActionPerformed
        deleteRowFromTableAndArray(usersTable, users);
        refreshUsersTable();
    }//GEN-LAST:event_deleteUserActionPerformed

    private void refreshUsersActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_refreshUsersActionPerformed
        loadData();
        loadUsers();
    }//GEN-LAST:event_refreshUsersActionPerformed

    private void usersPanelComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_usersPanelComponentShown
        loadData();
        loadUsers();
    }//GEN-LAST:event_usersPanelComponentShown

    private void userModalConfirmActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_userModalConfirmActionPerformed
        createNewUser();
    }//GEN-LAST:event_userModalConfirmActionPerformed

    private void userModalCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_userModalCancelActionPerformed
        userModal.setVisible(false);
        resetUserModalFields();
    }//GEN-LAST:event_userModalCancelActionPerformed

    private void userModalComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_userModalComponentShown

    }//GEN-LAST:event_userModalComponentShown

    private void userModalPasswordConfirmActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_userModalPasswordConfirmActionPerformed

    }//GEN-LAST:event_userModalPasswordConfirmActionPerformed

    private void orderModalComponentHidden(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_orderModalComponentHidden
        resetOrderModalFields();
    }//GEN-LAST:event_orderModalComponentHidden

    // <editor-fold defaultstate="collapsed" desc="Data utilities">   
    /**
     *
     * Carga la información de todas las tables del archivo a los arreglos
     * dentro del programa
     *
     * @param array El arreglo al cual añadir la información
     * @param tableName El nombre de la tabla de la cual recuperar los registros
     */
    private void getData(ArrayList array, String tableName) {
        array.clear();
        try ( FileReader fr = new FileReader(database);  BufferedReader br = new BufferedReader(fr)) {

            String line;
            boolean foundTable = false;
            boolean foundHeaders = false;

            while (true) {
                line = br.readLine();

                if (line == null) {
                    break; // EOF
                }

                if (foundTable && foundHeaders && line.equals("")) {
                    break;
                }

                if (foundHeaders) {
                    array.add(line);
                }

                if (foundTable) {
                    foundHeaders = true;
                    continue;
                }

                if (line.equals(tableName)) {
                    foundTable = true;
                }
            }

        } catch (FileNotFoundException ex) {
            Logger.getLogger(Cafeteria.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Cafeteria.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Guarda los arreglos de información actuales al archivo de base de datos
     */
    private void saveData() {
        String[] tableNames = new String[]{"Ordenes", "Productos", "Usuarios"};
        String[] tableHeaders = new String[]{
            "ID,Número de mesa,Clientes,Orden,Estado,Hora inicio,Hora fin,Subtotal,Total,Comentario,Empleado",
            "ID,Nombre,Precio,Stock",
            "ID,Nombre,Apellidos,Rol,Fecha de inicio,RFC,Usuario,Hash"
        };
        ArrayList<ArrayList<String>> data = new ArrayList();
        data.add(orders);
        data.add(products);
        data.add(users);

        StringBuilder sb = new StringBuilder();
        String finalData;

        for (int i = 0; i < 3; i++) {
            sb.append(tableNames[i]).append("\n");
            sb.append(tableHeaders[i]).append("\n");

            for (String row : data.get(i)) {
                sb.append(row).append("\n");
            }

            sb.append("\n");
        }

        finalData = sb.toString();

        try ( FileOutputStream fos = new FileOutputStream(database);  BufferedOutputStream bos = new BufferedOutputStream(fos);  PrintStream out = new PrintStream(bos, true, StandardCharsets.UTF_8.toString())) {
            out.print(finalData);
        } catch (IOException ex) {
            Logger.getLogger(Cafeteria.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /**
     * Utilidad para guardar y recargar la información de la base de datos
     */
    private void refreshData() {
        saveData();
        loadData();
    }

    /**
     * Carga la información de las tres tablas en memoria
     */
    private void loadData() {
        getData(orders, "Ordenes");
        getData(products, "Productos");
        getData(users, "Usuarios");
    }

    // </editor-fold>
    //
    // <editor-fold defaultstate="collapsed" desc="Navigation methods">            
    /**
     * Navega hacia la vista de Dashboard
     */
    private void goToDashboard() {
        setCurrentButton(dashboardLink);
        CardLayout layout = (CardLayout) main.getLayout();
        layout.show(main, "Dashboard");
        JFrame frame = (JFrame) SwingUtilities.getRoot(dashboardLink);
        frame.setTitle("Cafetería UCC - Dashboard");
        setCurrentButton(dashboardLink);
    }

    /**
     * Cambia entre paneles, posibilitando la navegación
     *
     * @param evt El evento disparado por un enlace
     * @param cardName El nombre de la tarjeta a navegar
     */
    private void changePanels(ActionEvent evt, String cardName) {
        JButton button = (JButton) evt.getSource();
        setCurrentButton(button);
        CardLayout layout = (CardLayout) main.getLayout();
        layout.show(main, cardName);
        JFrame frame = (JFrame) SwingUtilities.getRoot(button);
        frame.setTitle("Cafetería UCC - " + cardName);
    }

    /**
     * Cambia el estilo del botón parámetro a uno activo
     *
     * @param button Botón a estilar
     */
    private void setActive(JButton button) {
        button.setForeground(Color.black);
        button.setBackground(Color.white);
    }

    /**
     * Cambia el estilo del botón parámetro a uno inactivo
     *
     * @param button Botón a estilar
     */
    private void setInactive(JButton button) {
        button.setForeground(Color.white);
        button.setBackground(THEME);
    }

    /**
     * Cambia el estilo del botón parámetro a activo e inactivo a los demás
     *
     * @param currentButton Botón a estilar
     */
    private void setCurrentButton(JButton currentButton) {
        for (Component component : sidebar.getComponents()) {
            if (component instanceof JButton) {

                JButton button = (JButton) component;
                if (!button.getText().equals("Cerrar sesión")) {
                    setInactive(button);
                }
            }
        }

        setActive(currentButton);
    }

    // </editor-fold>            
    //
    // <editor-fold defaultstate="collapsed" desc="General utilities">            
    /**
     * Muestra una alerta modal
     *
     * @param title El título del cuadro de diálogo
     * @param message El mensaje del cuadro de diálogo
     * @param component El componente padre del cuadro de diálogo
     */
    private void showAlert(String title, String message, Component component) {
//        JFrame frame = new JFrame();
//        frame.setVisible(true);
//        frame.setLocationRelativeTo(null);
//        frame.setAlwaysOnTop(true);

        Component container = component;

        if (container == null) {
            container = this;
        }

        JOptionPane.showMessageDialog(container, message, title, JOptionPane.ERROR_MESSAGE);
//        frame.dispose();
    }

//    private void showAlertOnTop(String title, String message, Component component) {
//        JOptionPane.showMessageDialog(component, title, message, JOptionPane.ERROR_MESSAGE);
//    }
    /**
     * Muestra una alerta modal que requiere confirmación del usuario
     *
     * @param title El título del cuadro de diálogo
     * @param message El mensaje del cuadro de diálogo
     * @param component El componente padre del cuadro de diálogo
     * @return Un entero representando la opción seleccionada
     */
    private int showConfirm(String title, String message, Component component) {
        Component container = component;

        if (container == null) {
            container = this;
        }
        return JOptionPane.showConfirmDialog(container, message, title, JOptionPane.WARNING_MESSAGE);
    }

    /**
     * Muestra información dentro de un cuadro modal
     *
     * @param title El título del cuadro de diálogo
     * @param message El mensaje del cuadro de diálogo
     */
    private void showInfo(String title, String message) {
//        JFrame frame = new JFrame();
//        frame.setVisible(true);
//        frame.setLocationRelativeTo(null);
//        frame.setAlwaysOnTop(true);
        JOptionPane.showMessageDialog(this, message, title,
                JOptionPane.INFORMATION_MESSAGE);
//        frame.dispose();
    }

    /**
     * Llena la tabla dada con los registros del arreglo parámetro
     *
     * @param table Tabla a llenar
     * @param array Arreglo de cadenas con el cual llenar la tabla
     */
    private void loadTable(JTable table, ArrayList<String> array) {
        clearTable(table);
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        for (String row : array) {
            model.addRow(row.split(","));
        }
    }

    /**
     * Carga la información personal del usuario en el Dashboard
     */
    private void loadUserDetails() {
        dashboardUsername.setText(getCurrentUserName());
    }

    /**
     * Agrega escuchadores de eventos a ciertas tablas.
     *
     * Se usa para deshabilitar la creación de órdenes sin productos
     * seleccionados
     */
    private void addTableListeners() {
        DefaultTableModel model = (DefaultTableModel) modalProductsTable.getModel();
        model.addTableModelListener(new OrderProductsModalListener(modalProductsTable, modalOrderConfirm));
    }

    /**
     * Presenta en formato de nombre, apellido y días, el tiempo que lleva un
     * usuario registrado en el sistema
     *
     * @param user Usuario registrado
     * @return La cadena con formato
     */
    private String formatUserSeniority(String user) {
        String[] data = user.split(",");
        String name = data[1];
        String surname = data[2];
        long start = Long.parseLong(data[4].trim());

        Duration duration = Duration.between(Instant.ofEpochMilli(start), Instant.now());

        String span = TimeUnit.MILLISECONDS.toDays(duration.getSeconds() * 1000) + " días";

        return String.format("%s %s (%s)", name, surname, span);
    }

    /**
     * Formatea un double como cadena con dos decimales
     *
     * @param d Número a dar formato
     * @return El double con formato
     */
    private String doubleToFixed(Double d) {
        return String.format("%.2f", d);
    }

    /**
     * Limpia la tabla dada
     *
     * @param table Tabla a limpiar
     */
    private void clearTable(JTable table) {
        ((DefaultTableModel) table.getModel()).setNumRows(0);
        table.revalidate();
    }

    /**
     * Convierte una cadena en un identificador único a través de una función
     * criptográfica
     *
     * @param string La cadena a convertir
     * @return Una cadena como identificador único
     */
    private String hash(String string) {
        String hashed = "";
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(string.getBytes());
            byte[] bytes = md.digest();
            hashed = Base64.getEncoder().encodeToString(bytes);
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(Cafeteria.class.getName()).log(Level.SEVERE, null, ex);
        }

        return hashed;
    }

    /**
     * Regresa una cadena formateada como fecha a partir de una cadena de
     * milisegundos desde el tiempo Epoch Unix
     *
     * @param millis Milisegundos desde el tiempo Epoch
     * @return Una fecha como cadena
     */
    private String formatDateFromEpochMillis(String millis) {

        if (millis.trim().equals("null")) {
            return "---";
        }

        long epoch = Long.parseLong(millis);
        Date date = new Date(epoch);
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        return formatter.format(date);
    }

    /**
     * Convierte cada una de las columnas dadas del arreglo en una fecha como
     * cadena
     *
     * @param array Arreglo a formatear
     * @param columns Arreglo con índices de las columnas objetivo
     * @return Un arreglo con las columnas dadas convertidas en fechas
     */
    private ArrayList<String> parseDateFromColumn(ArrayList<String> array, int[] columns) {
        ArrayList<String> newArray = new ArrayList();

        for (String row : array) {
            String[] finalRow = row.split(",");

            for (int column : columns) {
                finalRow[column] = formatDateFromEpochMillis(finalRow[column]);
            }

            newArray.add(String.join(",", finalRow));
        }

        return newArray;
    }

    /**
     * Convierte una cadena de milisegundos desde el tiempo Epoch a una cadena
     * formateada
     *
     * @param millis Milisegundos desde el tiempo Epoch
     * @return Una cadena formateada en horas y minutos
     */
    private String epochMillisToTime(String millis) {
        if (millis.trim().equals("null")) {
            return "---";
        }

        DateFormat date = new SimpleDateFormat("HH:mm");
        Date time = new Date(Long.parseLong(millis));
        return date.format(time);
    }

    /**
     * Convierte cada una de las columnas dadas del arreglo en una cadena de
     * tiempo
     *
     * @param array Arreglo a formatear
     * @param columns Arreglo con índices de las columnas objetivo
     * @return Un arreglo con las columnas dadas convertidas en tiempo de horas
     * y minutos
     */
    private ArrayList<String> parseTimeFromColumn(ArrayList<String> array, int[] columns) {
        ArrayList<String> newArray = new ArrayList();

        for (String row : array) {
            String[] finalRow = row.split(",");

            for (int column : columns) {
                finalRow[column] = epochMillisToTime(finalRow[column]);
            }

            newArray.add(String.join(",", finalRow));
        }

        return newArray;
    }

    // </editor-fold>
    //    
    // <editor-fold defaultstate="collapsed" desc="Dashboard methods">     
    /**
     * Actualiza la fecha actual en el Dashboard
     */
    private void setCurrentDateOnDashboard() {
        ZoneId z = ZoneId.of("America/Mexico_City");
        LocalDate today = LocalDate.now(z);

        DayOfWeek day = LocalDate.now().getDayOfWeek();
        Month month = LocalDate.now().getMonth();
        String localizedDay = day.getDisplayName(TextStyle.FULL, new Locale("es", "ES"));
        String localizedMonth = month.getDisplayName(TextStyle.FULL, new Locale("es", "ES"));

        dashboardDay.setText(localizedDay + ", " + today.getDayOfMonth());
        dashboardMonth.setText("de " + localizedMonth + " del");
        dashboardYear.setText("" + today.getYear());
    }

    /**
     * Carga un arreglo de órdenes parcial en la tabla del Dashboard
     */
    private void loadDashboardOrders() {
        clearTable(dashboardCurrentOrders);
        ArrayList<String> dashboardOrders = parseTimeFromColumn(deleteColumnsFromList(filterArrayByString(orders, "Proceso"), new int[]{3, 4, 6, 7, 9}), new int[]{3});
        loadTable(dashboardCurrentOrders, dashboardOrders);
    }

    /**
     * Carga las métricas del Dashboard
     */
    private void loadDashboardMetrics() {
        dashboardOrderCount.setText(Integer.toString(orders.size()));
        dashboardClientCount.setText(Integer.toString(sum(getDoubleSubList(orders, 2)).intValue()));
        dashboardTotalEarnings.setText("$" + sum(getDoubleSubList(filterArrayByString(orders, "Finalizado"), 8)).intValue());
    }

    /**
     * Recarga la información del Dashboard
     */
    private void refreshDashboard() {
        refreshDashboardOrders();
        loadDashboardMetrics();
    }

    // </editor-fold>
    //
    // <editor-fold defaultstate="collapsed" desc="Login utilities">            
    /**
     * Inicia sesión con las credenciales del cuadro de diálogo
     */
    private void doLogin() {
        String username = loginUsername.getText();
        String password = String.valueOf(loginPassword.getPassword());

        ArrayList<String> allUsers = getSubList(users, 6);

        int index = allUsers.lastIndexOf(username);

        if (index == - 1) {
            showAlert("El usuario ingresado no existe", "No se encontró el usuario", this);
            return;
        }

        currentUser = users.get(index);

        String targetPassword = currentUser.split(",")[7];
        String hashedPassword = hash(password);

        if (!targetPassword.equals(hashedPassword)) {
            showAlert("La contraseña ingresada es incorrecta", "Contraseña incorrecta", this);
            return;
        }

        showInfo("¡Bienvenido!", "Ingreso exitoso");
        loginFrame.setVisible(false);
        hideUnauthorizedActions();
        setVisible(true);
        setLocationRelativeTo(null);
        loadUserDetails();
        goToDashboard();
    }

    /**
     * Oculta los enlaces no visibles a usuarios sin permisos de administrador
     */
    private void hideUnauthorizedActions() {
        usersLink.setVisible(isAdmin());
    }

    /**
     * Obtiene los privilegios del usuario actual
     *
     * @return Un booleano que indica si el usuario es Administrador
     */
    private boolean isAdmin() {
        return currentUser.contains("Administrador");
    }

    /**
     * Muestra del cuadro de diálogo de inicio de sesión
     */
    private void showLogin() {
        loginFrame.pack();
        loginFrame.setLocationRelativeTo(null);
        loginFrame.setVisible(true);
    }

    /**
     * Cierra sesión dentro del programa y muestra la pantalla de login
     */
    private void logout() {
        setVisible(false);
        resetLoginFields();
        showLogin();
    }

    /**
     * Reinicia los campos del cuadro de diálogo de inicio de sesión
     */
    private void resetLoginFields() {
        loginUsername.setText("");
        loginPassword.setText("");
    }

    // </editor-fold>
    //
    // <editor-fold defaultstate="collapsed" desc="Order methods">  
    /**
     * Muestra el modal para crear una nueva orden
     */
    private void showNewOrderModal() {
        modalOrderConfirm.setText("Confirmar");
        orderModal.setTitle("Nueva orden");
        showOrderModal();
    }

    /**
     * Muestra el modal para editar una orden existente
     */
    private void showEditingOrderModal() {
        modalOrderConfirm.setText("Editar");
        orderModal.setTitle("Editar orden");
        loadOrderModal();
        showOrderModal();
    }

    /**
     * Crea o edita una orden válida con los datos del modal respectivo
     */
    private void createNewOrder() {
        String newID = "";
        String tableNumber = modalOrderTable.getValue().toString();
        String clientCount = modalOrderClients.getValue().toString();
        ArrayList<String> orderProducts = new ArrayList();

        DefaultTableModel model = (DefaultTableModel) modalProductsTable.getModel();
        Double subtotal = 0.0;
        for (int i = 0; i < model.getRowCount(); i++) {
            orderProducts.add((String) model.getValueAt(i, 0));
            Double costo = Double.parseDouble((String) model.getValueAt(i, 2));
            subtotal += costo;
        }

        String startTime = "" + Instant.now().toEpochMilli();
        String endTime = "null";
        Double total = subtotal * 1.16;
        String comment = modalProductComment.getText().isEmpty() ? "null" : modalProductComment.getText();

        String employeeID = getCurrentUserID();

        if (!checkOrderStock(String.join("|", orderProducts))) {
            showAlert("Sin stock suficiente", "No existe el stock suficiente para satisfacer esta orden.", orderModal);
            return;
        }

        if (modalOrderConfirm.getText().equals("Confirmar")) {
            newID = getNextOrderID() + "";
            String state = "Proceso";
            String[] recordArray = new String[]{newID, tableNumber, clientCount, String.join("|", orderProducts), state, startTime, endTime, doubleToFixed(subtotal), doubleToFixed(total), comment, employeeID};
            String record = String.join(",", recordArray);
            orders.add(record);
        } else if (modalOrderConfirm.getText().equals("Editar")) {
            int index = ordersTable.getSelectedRow();
            if (index == -1) {
                return;
            }

            String[] orderData = orders.get(index).split(",");
            newID = orderData[0];
            String state = orderData[4];

            String[] recordArray = new String[]{newID, tableNumber, clientCount, String.join("|", orderProducts), state, startTime, endTime, doubleToFixed(subtotal), doubleToFixed(total), comment, employeeID};
            String record = String.join(",", recordArray);

            orders.set(index, record);
        }

        refreshOrdersTable();
        orderModal.setVisible(false);
    }

    /**
     * Comprueba si existe stock suficiente para una orden
     *
     * @param order Una cadena separada por barras verticales con los ID's de
     * los productos de la orden
     * @return Si la orden puede ser satisfecha o no
     */
    private boolean checkOrderStock(String order) {

        HashMap<Integer, Integer> productTotals = new HashMap();
        for (String id : order.split("[|]")) {
            Integer parsedId = Integer.parseInt(id);
            Integer currentCount = productTotals.putIfAbsent(parsedId, 1);
            productTotals.put(parsedId, currentCount != null ? currentCount + 1 : 1);
        }

        for (Map.Entry row : productTotals.entrySet()) {
            Integer id = (Integer) row.getKey(), quantity = (Integer) row.getValue();

            if (quantity > getStockById(id)) {
                return false;
            }

        }

        return true;
    }

    /**
     * Modifica los stocks respectivos de los productos de la orden dada
     *
     * @param order Una cadena separada por barras verticales con los ID's de
     * los productos de la orden
     */
    private void modifyOrderStock(String order) {

        HashMap<Integer, Integer> productTotals = new HashMap();
        for (String id : order.split("[|]")) {
            Integer parsedId = Integer.parseInt(id);
            Integer currentCount = productTotals.putIfAbsent(parsedId, 1);
            productTotals.put(parsedId, currentCount != null ? currentCount + 1 : 1);
        }

        for (Map.Entry row : productTotals.entrySet()) {
            Integer id = (Integer) row.getKey(), quantity = (Integer) row.getValue();
            int currentStock = getStockById(id);

            setStockById(id, currentStock - quantity);
        }

    }

    /**
     * Carga las órdenes actuales en la tabla de la vista Órdenes
     */
    private void loadOrders() {
        loadTable(ordersTable, parseTimeFromColumn(orders, new int[]{5, 6}));
        ordersPanelTitle.setText("Órdenes (" + orders.size() + ")");
    }

    /**
     * Cancela o finaliza una orden del Dashboard
     *
     * @param newStatus El nuevo estado de la orden
     */
    private void editDashboardOrder(String newStatus) {
        int index = dashboardCurrentOrders.getSelectedRow();
        if (index == -1) {
            return;
        }

        DefaultTableModel model = (DefaultTableModel) dashboardCurrentOrders.getModel();
        String id = (String) model.getValueAt(index, 0);
        int parsedId = Integer.parseInt(id.trim());

        String order = getOrderById(parsedId);
        String[] completedOrder = order.split(",");
        completedOrder[4] = newStatus;
        completedOrder[6] = Instant.now().toEpochMilli() + "";

        if (newStatus.equals("Finalizado")) {
            modifyOrderStock(order.split(",")[3]);
        }

        setOrderById(parsedId, String.join(",", completedOrder));
        refreshDashboard();
    }

    /**
     * Refresca las órdenes del Dashboard
     */
    private void refreshDashboardOrders() {
        refreshData();
        clearTable(dashboardCurrentOrders);
        loadDashboardOrders();
    }

    /**
     * Refresca las órdenes de la tabla de la vista Órdenes
     */
    private void refreshOrdersTable() {
        refreshData();
        clearTable(ordersTable);
        loadOrders();
    }

    /**
     * Reinicia los campos del modal de nueva orden
     */
    private void resetOrderModalFields() {
        modalOrderTable.setValue(1);
        modalOrderClients.setValue(1);
//        modalOrderState.setSelectedIndex(0);
        modalProductCombo.setSelectedIndex(0);
        modalProductsTable.clearSelection();
        clearTable(modalProductsTable);
        updateOrderModalTotals();
        modalProductComment.setText("");
    }

    /**
     * Actualiza los totales del modal de nueva orden
     */
    private void updateOrderModalTotals() {
        DefaultTableModel model = (DefaultTableModel) modalProductsTable.getModel();

        double subtotal = 0;

        for (int i = 0; i < model.getRowCount(); i++) {
            Double costo = Double.parseDouble((String) model.getValueAt(i, 2));
            subtotal += costo;
        }

        modalOrderSubtotal.setText("$" + doubleToFixed(subtotal));
        modalOrderTotal.setText("$" + doubleToFixed(subtotal * 1.16));

    }

    /**
     * Carga los productos actuales dentro del cuadro de selección del modal de
     * nueva orden
     */
    private void loadOrderModalCombobox() {
        modalProductCombo.removeAllItems();
        for (String product : products) {
            String[] data = product.split(",");
            String id = data[0];
            String name = data[1];
            String finalProduct = String.format("%s - %s", id, name);
            modalProductCombo.addItem(finalProduct);
        }
    }

    /**
     * Carga el modal de edición de orden con los datos de la orden seleccionada
     */
    private void loadOrderModal() {
        int index = ordersTable.getSelectedRow();
        if (index == - 1) {
            return;
        }

        String[] data = orders.get(index).split(",");

        modalOrderTable.setValue(Integer.parseInt(data[1]));
        modalOrderClients.setValue(Integer.parseInt(data[2]));
//        modalOrderState.setSelectedItem(data[4]);
        refreshData(); // new products
        loadOrderModalCombobox();
        modalProductCombo.setSelectedIndex(0);

        DefaultTableModel model = (DefaultTableModel) modalProductsTable.getModel();

        for (String id : data[3].split("[|]")) {
            String product = getProductById(Integer.parseInt(id.trim()));
            model.addRow(product.split(","));
        }

        updateOrderModalTotals();

        modalProductComment.setText(data[9]);

    }

    /**
     * Obtiene una orden por su ID
     *
     * @param id El ID de la orden
     * @return La orden
     */
    private String getOrderById(int id) {
        for (String order : orders) {
            String[] data = order.split(",");
            int currentId = Integer.parseInt(data[0]);

            if (currentId == id) {
                return order;
            }
        }

        return null;
    }

    /**
     * Obtiene el ID de la siguiente orden a crear
     *
     * @return El siguiente ID
     */
    private int getNextOrderID() {
        if (!orders.isEmpty()) {
            String lastOrder = orders.get(orders.size() - 1);

            int lastOrderID = Integer.parseInt(lastOrder.split(",")[0].trim());

            return lastOrderID + 1;
        }

        return 1;
    }

    /**
     * Edita una orden por ID
     *
     * @param id El ID de la orden
     * @param newOrder La nueva orden
     */
    private void setOrderById(int id, String newOrder) {
        for (String order : orders) {
            String[] data = order.split(",");
            int currentId = Integer.parseInt(data[0]);

            if (currentId == id) {
                int index = orders.indexOf(order);
                orders.set(index, newOrder);
            }
        }
    }

    /**
     * Muestra el modal de órdenes
     */
    private void showOrderModal() {
        orderModal.pack();
        orderModal.setLocationRelativeTo(null);
        orderModal.setVisible(true);
    }

    // </editor-fold>
    //
    // <editor-fold defaultstate="collapsed" desc="Product methods">   
    /**
     * Muestra el modal de creación de productos
     */
    private void showNewProductModal() {
        productModalConfirm.setText("Confirmar");
        productModal.setTitle("Nuevo producto");
        resetProductModalFields();
        showProductModal();
    }

    /**
     * Muestra el modal de edición de productos
     */
    private void showEditingProductModal() {
        productModalConfirm.setText("Editar");
        productModal.setTitle("Editar producto");
        loadProductModal();
        showProductModal();
    }

    /**
     * Crea o edita un nuevo producto con los datos del modal correspondiente
     */
    private void createNewProduct() {
        String newID = "";

        if (!verifyProductModalFields()) {
            showAlert("Por favor rellene todos los campos", "Error", null);
            return;
        }

        String name = productModalName.getText();
        String price = productModalPrice.getText();
        String stock = productModalStock.getValue().toString();

        if (productModalConfirm.getText().equals("Confirmar")) {
            newID = getNextProductID() + "";
            String[] recordArray = new String[]{newID, name, price, stock};
            String record = String.join(",", recordArray);
            products.add(record);
        } else if (productModalConfirm.getText().equals("Editar")) {
            int index = productsTable.getSelectedRow();
            if (index == -1) {
                return;
            }

            String[] productData = products.get(index).split(",");
            newID = productData[0];

            String[] recordArray = new String[]{newID, name, price, stock};
            String record = String.join(",", recordArray);

            products.set(index, record);
        }

        refreshProductsTable();
        productModal.setVisible(false);
    }

    /**
     * Carga los productos actuales dentro de su tabla respectiva
     */
    private void loadProducts() {
        loadTable(productsTable, products);
        productsPanelTitle.setText("Productos (" + products.size() + ")");
    }

    /**
     * Refresca la tabla de productos
     */
    private void refreshProductsTable() {
        refreshData();
        clearTable(productsTable);
        loadProducts();
    }

    /**
     * Reinicia los campos del modal de creación de productos
     */
    private void resetProductModalFields() {
        productModalName.setText("");
        productModalPrice.setText("0");
        productModalStock.setValue(1);
    }

    /**
     * Verifica si los campos del modal de creación de productos no están vacíos
     *
     * @return Si los campos son válidos
     */
    private boolean verifyProductModalFields() {
        return !productModalName.getText().isEmpty()
                && !productModalPrice.getText().isEmpty();
    }

    /**
     * Carga el modal de edición de productos con los datos del producto
     * seleccionado
     */
    private void loadProductModal() {
        int index = productsTable.getSelectedRow();
        if (index == - 1) {
            return;
        }

        String[] data = products.get(index).split(",");

        productModalName.setText(data[1]);
        productModalPrice.setText(data[2]);
        productModalStock.setValue(Integer.parseInt(data[3]));

        refreshData(); // new products
    }

    /**
     * Obtiene un producto por su ID
     *
     * @param id El ID del producto
     * @return El producto
     */
    private String getProductById(int id) {
        for (String product : products) {
            String[] data = product.split(",");
            int currentId = Integer.parseInt(data[0]);

            if (currentId == id) {
                return product;
            }
        }

        return null;
    }

    /**
     * Obtiene el stcok de un producto por su ID
     *
     * @param id El ID del producto
     * @return El stock del producto
     */
    private Integer getStockById(int id) {
        String product = getProductById(id);
        if (product == null) {
            return 0;
        }

        return Integer.parseInt(product.split(",")[3]);
    }

    /**
     * Modifica el stock de un producto por su ID
     *
     * @param id El ID del producto
     * @param newStock El nuevo stock del producto
     */
    private void setStockById(int id, int newStock) {
        String product = getProductById(id);
        if (product == null) {
            return;
        }

        String[] newProductData = product.split(",");
        newProductData[3] = newStock + "";
        String finalProduct = String.join(",", newProductData);
        setProductById(id, finalProduct);
    }

    /**
     * Modifica un producto por su ID
     *
     * @param id El ID del producto
     * @param newProduct El nuevo producto
     */
    private void setProductById(int id, String newProduct) {
        for (String product : products) {
            String[] data = product.split(",");
            int currentId = Integer.parseInt(data[0]);

            if (currentId == id) {
                int index = products.indexOf(product);
                products.set(index, newProduct);
            }
        }
    }

    /**
     * Obtiene el ID del siguiente producto a crear
     *
     * @return El siguiente ID de producto
     */
    private int getNextProductID() {
        if (!products.isEmpty()) {
            String lastProduct = products.get(products.size() - 1);

            int lastProductID = Integer.parseInt(lastProduct.split(",")[0].trim());

            return lastProductID + 1;
        }

        return 1;
    }

    /**
     * Obtiene el nombre de un producto por su ID
     *
     * @param id El ID del producto
     * @return El nombre del producto
     */
    private String getProductNameById(int id) {
        String name = "---";
        for (String product : products) {
            String[] data = product.split(",");
            String currentName = data[1];
            int currentId = Integer.parseInt(data[0]);

            if (currentId == id) {
                name = currentName;
                break;
            }
        }

        return name;
    }

    /**
     * Obtiene el nombre de un producto por su stock
     *
     * @param stock El stock del producto
     * @return El nombre del producto
     */
    private String getProductNameByStock(int stock) {
        int id = -1;
        for (String product : products) {
            String[] data = product.split(",");
            int currentStock = Integer.parseInt(data[3]);
            int currentId = Integer.parseInt(data[0]);

            if (currentStock == stock) {
                id = currentId;
                break;
            }
        }

        return getProductNameById(id);
    }

    /**
     * Obtiene el nombre de un producto por un valor coincidente en una columna
     * dada
     *
     * @param column El índice de la columna
     * @param value El valor buscado
     * @return El nombre de producto
     */
    private String getProductNameByColumn(int column, String value) {
        int id = -1;
        for (String product : products) {
            String[] data = product.split(",");
            String currentField = data[column];
            int currentId = Integer.parseInt(data[0]);

            if (currentField.equals(value)) {
                id = currentId;
                break;
            }
        }

        return getProductNameById(id);
    }

    /**
     * Muestra el modal de creación y edición de productos
     */
    private void showProductModal() {
        productModal.pack();
        productModal.setLocationRelativeTo(null);
        productModal.setVisible(true);
    }
    // </editor-fold>
    // 
    // <editor-fold defaultstate="collapsed" desc="User methods">     

    /**
     * Obtiene el ID del usuario actual
     *
     * @return Una cadena de ID
     */
    private String getCurrentUserID() {
        if (currentUser == null) {
            return "1";
        }

        return currentUser.split(",")[0];
    }

    /**
     * Obtiene el nombre del usuario actual
     *
     * @return Una cadena con el nombre del usuario
     */
    private String getCurrentUserName() {
        if (currentUser == null) {
            return "Usuario";
        }

        return currentUser.split(",")[1];
    }

    /**
     * Obtiene el nombre de usuario dado su ID
     *
     * @param id El ID del usuario
     * @return El nombre de usuario
     */
    private String getUsernameById(int id) {
        String name = "---";
        for (String user : users) {
            String[] data = user.split(",");
            String currentName = data[1] + " " + data[2];
            int currentId = Integer.parseInt(data[0]);

            if (currentId == id) {
                name = currentName;
                break;
            }
        }

        return name;
    }

    /**
     * Muestra el modal para la creación de un nuevo usuario
     */
    private void showNewUserModal() {
        userModalConfirm.setText("Confirmar");
        userModal.setTitle("Nuevo usuario");
        userModalPassword.setVisible(true);
        userModalPasswordConfirm.setVisible(true);
        userModalPasswordLabel.setVisible(true);
        userModalPasswordConfirmLabel.setVisible(true);

        resetUserModalFields();
        showUserModal();
    }

    /**
     * Muestra el modal para la edición de un usuario existente
     */
    private void showEditingUserModal() {
        userModalConfirm.setText("Editar");
        userModal.setTitle("Editar usuario");

        // disable password editing
        userModalPassword.setVisible(false);
        userModalPasswordConfirm.setVisible(false);
        userModalPasswordLabel.setVisible(false);
        userModalPasswordConfirmLabel.setVisible(false);
        loadUserModal();
        showUserModal();
    }

    /**
     * Crea o edita un usuario nuevo con la información del modal respectivo
     */
    private void createNewUser() {
        String newID = "";
        String startDate = Instant.now().toEpochMilli() + "";
        // ID,Nombre,Apellidos,Rol,Fecha de inicio,RFC,Usuario,Hash

        String password = String.valueOf(userModalPassword.getPassword());
        String confirmPassword = String.valueOf(userModalPasswordConfirm.getPassword());

        if (!verifyUserModalFields()) {
            showAlert("Por favor rellene todos los campos", "Error", userModal);
            return;
        }

        if (!password.equals(confirmPassword)) {
            showAlert("Las contraseñas no son iguales", "Error", userModal);
            return;
        }

        String name = userModalName.getText();
        String surname = userModalSurname.getText();
        String role = userModalRole.getSelectedItem().toString();
        String RFC = userModalRFC.getText();
        String username = userModalUsername.getText();
        String hashedPassword = hash(password);

        if (userModalConfirm.getText().equals("Confirmar")) {
            newID = getNextUserID() + "";

            String[] recordArray = new String[]{newID, name, surname, role, startDate, RFC, username, hashedPassword};
            String record = String.join(",", recordArray);
            users.add(record);
        } else if (userModalConfirm.getText().equals("Editar")) {
            int index = usersTable.getSelectedRow();
            if (index == -1) {
                showInfo("No hay un usuario seleccionado", "Alerta");
                return;
            }

            String[] userData = users.get(index).split(",");
            newID = userData[0];
            startDate = userData[4];

            String[] recordArray = new String[]{newID, name, surname, role, startDate, RFC, username, hashedPassword};
            String record = String.join(",", recordArray);

            users.set(index, record);
        }

        refreshUsersTable();
        userModal.setVisible(false);

    }

    /**
     * Carga los usuarios actuales en la tabla correspondiente
     */
    private void loadUsers() {
        // Hiding password hashes
        ArrayList<String> filteredUsers = deleteColumnsFromList(users, new int[]{7});
        loadTable(usersTable, parseDateFromColumn(filteredUsers, new int[]{4}));
        usersPanelTitle.setText("Usuarios (" + users.size() + ")");
    }

    /**
     * Refresca la tabla de usuarios
     */
    private void refreshUsersTable() {
        refreshData();
        clearTable(usersTable);
        loadUsers();
    }

    /**
     * Reinicia los campos del cuadro modal de nuevo usuario
     */
    private void resetUserModalFields() {
        userModalName.setText("");
        userModalPassword.setText("");
        userModalPasswordConfirm.setText("");
        userModalRFC.setText("");
        userModalSurname.setText("");
        userModalUsername.setText("");
        userModalRole.setSelectedItem("Usuario");
    }

    /**
     * Verifica si los campos del modal de usuario son válidos
     *
     * @return Si los campos son válidos
     */
    private boolean verifyUserModalFields() {

        boolean editing = userModalConfirm.getText().equals("Editar");

        boolean passwordsNotEmpty = editing ? true : !userModalPassword.getText().isEmpty()
                && !userModalPasswordConfirm.getText().isEmpty();

        return !userModalName.getText().isEmpty() && passwordsNotEmpty
                && !userModalRFC.getText().isEmpty()
                && !userModalSurname.getText().isEmpty()
                && !userModalUsername.getText().isEmpty();
    }

    /**
     * Carga los datos del cuadro modal de edición de usuario
     */
    private void loadUserModal() {
        // ID,Nombre,Apellidos,Rol,Fecha de inicio,RFC,Usuario,Hash
        // 0  1      2         3   (4)             5   6      (7)
        int index = usersTable.getSelectedRow();
        if (index == - 1) {
            showInfo("No hay un usuario seleccionado", "Alerta");
            return;
        }

        String[] data = users.get(index).split(",");

        userModalName.setText(data[1]);
        userModalSurname.setText(data[2]);
        userModalRole.setSelectedItem(data[3]);
        userModalRFC.setText(data[5]);
        userModalUsername.setText(data[6]);

        refreshData(); // new users
    }

    /**
     * Obtiene un usuario por ID
     *
     * @param id El ID de usuario
     * @return El usuario
     */
    private String getUserById(int id) {
        for (String user : users) {
            String[] data = user.split(",");
            int currentId = Integer.parseInt(data[0]);

            if (currentId == id) {
                return user;
            }
        }

        return null;
    }

    /**
     * Establece un usuario por ID
     *
     * @param id El ID de usuario
     * @param newUser El nuevo usuario
     */
    private void setUserById(int id, String newUser) {
        for (String user : users) {
            String[] data = user.split(",");
            int currentId = Integer.parseInt(data[0]);

            if (currentId == id) {
                int index = users.indexOf(user);
                users.set(index, newUser);
            }
        }
    }

    /**
     * Obtiene el siguiente ID de usuario a crear
     *
     * @return El siguiente ID de usuario
     */
    private int getNextUserID() {
        if (!users.isEmpty()) {
            String lastUser = users.get(users.size() - 1);

            int lastUserID = Integer.parseInt(lastUser.split(",")[0].trim());

            return lastUserID + 1;
        }

        return 1;
    }

    /**
     * Muestra el modal de creación o edición de usuario
     */
    private void showUserModal() {
        userModal.pack();
        userModal.setLocationRelativeTo(null);
        userModal.setVisible(true);
    }

    // </editor-fold>
    //
    // <editor-fold defaultstate="collapsed" desc="Statistics methods">            
    /**
     * Calcula la moda del arreglo
     *
     * @param array Un arreglo de doubles
     * @return La moda del arreglo
     */
    private Double frequency(ArrayList<Double> array) {

        HashMap<Integer, Integer> map = new HashMap();

        int n = 0;
        double max = -1, freq = 0;

        for (Double i : array) {
            int index = i.intValue();
            if (map.containsKey(index)) {
                n = map.get(index);
                map.put(index, ++n);
            } else {
                map.put(index, 1);
            }
        }

        for (Map.Entry<Integer, Integer> e : map.entrySet()) {
            if (freq < e.getValue()) {
                freq = e.getValue();
                max = e.getKey();
            }
        }

        return max;
    }

    /**
     * Calcula el promedio del arreglo dado
     *
     * @param array Un arreglo de doubles
     * @return El promedio
     */
    private Double average(ArrayList<Double> array) {
        return array.stream()
                .mapToDouble(a -> a)
                .average().getAsDouble();
    }

    /**
     * Calcula el máximo elemento de un arreglo
     *
     * @param array Un arreglo de doubles
     * @return El máximo elemento
     */
    private Double max(ArrayList<Double> array) {
        return Collections.max(array);
    }

    /**
     * Calcula el mínimo elemento de un arreglo
     *
     * @param array Un arreglo de doubles
     * @return El mínimo elemento
     */
    private Double min(ArrayList<Double> array) {
        return Collections.min(array);
    }

    /**
     * Calcula la sumatoria de un arreglo de doubles
     *
     * @param array Un arreglo de doubles
     * @return La sumatoria
     */
    private Double sum(ArrayList<Double> array) {
        return array.stream().mapToDouble(a -> a).sum();
    }

    /**
     * Devuelve la orden con la duración más larga
     *
     * @param orders Un arreglo con las órdenes actuales
     * @return La duración formateada de la orden más larga
     */
    private String getLongestOrder(ArrayList<String> orders) {
        ArrayList<Long> diffs = new ArrayList();

        for (String order : orders) {
            String[] data = order.split(",");
            String start = data[5];
            String end = data[6];

            if (end.trim().equals("null")) {
                continue;
            }

            long parsedStart, parsedEnd;
            parsedStart = Long.parseLong(start.trim());
            parsedEnd = Long.parseLong(end.trim());

            diffs.add(parsedEnd - parsedStart);
        }

        long max = Collections.max(diffs);

        Duration duration = Duration.ofMillis(max);
        long s = duration.getSeconds();

        return String.format("%d horas, %2d minutos", s / 3600, (s % 3600) / 60);

    }

    /**
     * Obtiene el producto más solicitado entre todas las órdenes
     *
     * @return El producto más frecuentemente solicitado
     */
    private String getMostRequestedProduct() {
        ArrayList<Double> allOrderedProducts = flatProductArray(getSubList(orders, 3));
        Double mostRequestedId = frequency(allOrderedProducts);
        return getProductNameById(mostRequestedId.intValue());
    }

    /**
     * Devuelve el empleado registrado con mayor antigüedad
     *
     * @return El empleado
     */
    private String getOldestEmployee() {
        ArrayList<String> dates = getSubList(users, 4);
        long oldestDate = Collections.min(parseListToLong(dates));

        String oldestUser = "---";
        for (String user : users) {
            if (user.contains(Long.toString(oldestDate))) {
                oldestUser = formatUserSeniority(user);
            }
        }

        return oldestUser;
    }

    /**
     * Obtiene el empleado registrado más recientemente
     *
     * @return El empleado
     */
    private String getMostRecentEmployee() {

        ArrayList<String> dates = getSubList(users, 4);
        long mostRecentDate = Collections.max(parseListToLong(dates));

        String mostRecentUser = "---";
        for (String user : users) {
            if (user.contains(Long.toString(mostRecentDate))) {
                mostRecentUser = formatUserSeniority(user);
            }
        }

        return mostRecentUser;
    }

    // </editor-fold>
    //
    // <editor-fold defaultstate="collapsed" desc="Data manipulation">      
    /**
     *
     * Genera un nuevo arreglo a partir de las posiciones dadas de un arreglo de
     * cadenas, cada una representando una fila de información
     *
     * @param array El arreglo inicial
     * @param i El índice de columna
     * @return El nuevo arreglo
     */
    private ArrayList<String> getSubList(ArrayList<String> array, int i) {
        ArrayList<String> newArray = new ArrayList();

        for (String row : array) {
            newArray.add(row.split(",")[i]);
        }

        return newArray;
    }

    /**
     * Convierte todos los elementos de una lista de cadenas a double
     *
     * @param array Un arreglo de cadenas
     * @return Un arreglo de doubles
     */
    private ArrayList<Double> parseListToDouble(ArrayList<String> array) {
        ArrayList<Double> newArray = new ArrayList();

        for (String n : array) {
            newArray.add(Double.parseDouble(n.trim()));
        }

        return newArray;
    }

    /**
     * Convierte todos los elementos de una sublista a double
     *
     * @param array El arreglo
     * @param i El índice de columna a obtener por fila
     * @return Un arreglo de doubles
     */
    private ArrayList<Double> getDoubleSubList(ArrayList<String> array, int i) {
        return parseListToDouble(getSubList(array, i));
    }

    /**
     * Convierte todos los elementos de una lista a enteros largos
     *
     * @param array Un arreglo de cadenas
     * @return Un arreglo de enteros largos
     */
    private ArrayList<Long> parseListToLong(ArrayList<String> array) {
        ArrayList<Long> newArray = new ArrayList();

        for (String n : array) {
            newArray.add(Long.parseLong(n.trim()));
        }

        return newArray;
    }

    /**
     * Obtiene la longitud de los productos de una orden y las almacena en un
     * arreglo
     *
     * @param array Un arreglo de órdenes
     * @return Un arreglo de cantidad de productos
     */
    private ArrayList<String> mapProductListToQuantity(ArrayList<String> array) {

        ArrayList<String> newArray = new ArrayList();

        for (int i = 0; i < array.size(); i++) {
            newArray.add(Integer.toString(array.get(i).split("[|]").length));
        }

        return newArray;
    }

    /**
     * Filtra todas los elementos de un arreglo si contienen la cadena
     * especificada
     *
     * @param array Un arreglo de cadenas
     * @param string La cadena a buscar
     * @return Un arreglo con cadenas que contienen el objetivo
     */
    private ArrayList<String> filterArrayByString(ArrayList<String> array, String string) {
        return new ArrayList(array.stream().filter(a -> a.contains(string)).collect(Collectors.toList()));
    }

    /**
     * Convierte una lista de productos de una orden de cadena a arreglo
     *
     * @param array Un arreglo de órdenes
     * @return Un arreglo de productos
     */
    private ArrayList<Double> flatProductArray(ArrayList<String> array) {
        ArrayList<Double> newArray = new ArrayList();

        for (String product : array) {
            for (String id : product.split("[|]")) {
                newArray.add(Double.parseDouble("0" + id.trim()));
            }
        }

        return newArray;
    }

    /**
     * Elimina visualmente una fila de una tabla
     *
     * @param table La tabla
     * @return El índice eliminado
     */
    private int deleteRowFromTable(JTable table) {
        int index = table.getSelectedRow();

        if (index == -1) {
            return -1;
        }

        int input = showConfirm("Confirmar eliminación", "¿Desea borrar este registro?", table.getParent());

        if (index != -1 && input == JOptionPane.YES_OPTION) {
            DefaultTableModel model = (DefaultTableModel) table.getModel();
            model.removeRow(index);
            return index;
        }

        return -1;
    }

    /**
     * Elimina una fila de una tabla visualmente y su equivalente en el arreglo
     * correspondiente
     *
     * @param table La tabla
     * @param array El arreglo
     */
    private void deleteRowFromTableAndArray(JTable table, ArrayList<String> array) {
        int index = deleteRowFromTable(table);
        if (index != -1) {
            array.remove(index);
        }
    }

    /**
     * Elimina ciertas columnas de un arreglo
     *
     * @param array El arreglo
     * @param indices Los índices de columnas a eliminar
     * @return El arreglo con las columnas especificadas eliminadas
     */
    private ArrayList<String> deleteColumnsFromList(ArrayList<String> array, int[] indices) {
        ArrayList<String> newArray = new ArrayList();
        newArray.addAll(array);

        for (int i = 0; i < newArray.size(); i++) {
            String row = newArray.get(i);
            ArrayList<String> finalRow = new ArrayList();
            Collections.addAll(finalRow, row.split(","));

            for (int j = indices.length - 1; j >= 0; j--) {
                finalRow.remove(indices[j]);
            }

            newArray.set(i, String.join(",", finalRow));
        }

        return newArray;
    }

    // </editor-fold>
    //
    /**
     * El punto de entrada de la aplicación
     *
     * @param args Argumentos de línea de comandos - No se usan
     */
    public static void main(String args[]) {
        /* Establece el tema por defecto */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Cafeteria.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Cafeteria.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Cafeteria.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Cafeteria.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Crea y despliega el formulario en un hilo */
        java.awt.EventQueue.invokeLater(() -> {
            // Ocultando el Frame por defecto, para dar paso al login
            new Cafeteria().setVisible(false);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton dashboardCancelOrder;
    private javax.swing.JLabel dashboardClientCount;
    private javax.swing.JButton dashboardCompleteOrder;
    private javax.swing.JTable dashboardCurrentOrders;
    private javax.swing.JLabel dashboardDay;
    private javax.swing.JButton dashboardLink;
    private javax.swing.JLabel dashboardMonth;
    private javax.swing.JLabel dashboardOrderCount;
    private javax.swing.JPanel dashboardPanel;
    private javax.swing.JLabel dashboardTotalEarnings;
    private javax.swing.JLabel dashboardUsername;
    private javax.swing.JLabel dashboardYear;
    private javax.swing.JButton deleteOrder;
    private javax.swing.JButton deleteProduct;
    private javax.swing.JButton deleteUser;
    private javax.swing.JButton editOrder;
    private javax.swing.JButton editProduct;
    private javax.swing.JButton editUser;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JButton login;
    private javax.swing.JFrame loginFrame;
    private javax.swing.JPasswordField loginPassword;
    private javax.swing.JTextField loginUsername;
    private javax.swing.JButton logoutLink;
    private javax.swing.JPanel main;
    private javax.swing.JButton modalAddProduct;
    private javax.swing.JButton modalDeleteProduct;
    private javax.swing.JButton modalOrderCancel;
    private javax.swing.JSpinner modalOrderClients;
    private javax.swing.JButton modalOrderConfirm;
    private javax.swing.JLabel modalOrderSubtotal;
    private javax.swing.JSpinner modalOrderTable;
    private javax.swing.JLabel modalOrderTotal;
    private javax.swing.JComboBox<String> modalProductCombo;
    private javax.swing.JTextArea modalProductComment;
    private javax.swing.JPanel modalProductPanel;
    private javax.swing.JTable modalProductsTable;
    private javax.swing.JButton newOrder;
    private javax.swing.JButton newProduct;
    private javax.swing.JButton newUser;
    private javax.swing.JDialog orderModal;
    private javax.swing.JButton ordersLink;
    private javax.swing.JPanel ordersPanel;
    private javax.swing.JLabel ordersPanelTitle;
    private javax.swing.JTable ordersTable;
    private javax.swing.JDialog productModal;
    private javax.swing.JButton productModalCancel;
    private javax.swing.JButton productModalConfirm;
    private javax.swing.JTextField productModalName;
    private javax.swing.JFormattedTextField productModalPrice;
    private javax.swing.JSpinner productModalStock;
    private javax.swing.JButton productsLink;
    private javax.swing.JPanel productsPanel;
    private javax.swing.JLabel productsPanelTitle;
    private javax.swing.JTable productsTable;
    private javax.swing.JButton refreshOrders;
    private javax.swing.JButton refreshProducts;
    private javax.swing.JButton refreshUsers;
    private javax.swing.JPanel sidebar;
    private javax.swing.JLabel statsAverageClients;
    private javax.swing.JLabel statsAverageProductCost;
    private javax.swing.JLabel statsAverageProducts;
    private javax.swing.JLabel statsCheapestProduct;
    private javax.swing.JLabel statsLeastStock;
    private javax.swing.JButton statsLink;
    private javax.swing.JLabel statsLongestOrder;
    private javax.swing.JLabel statsMaxProducts;
    private javax.swing.JLabel statsMostAttendedOrders;
    private javax.swing.JLabel statsMostExpensiveOrder;
    private javax.swing.JLabel statsMostExpensiveProduct;
    private javax.swing.JLabel statsMostFrequentTable;
    private javax.swing.JLabel statsMostRecentEmployee;
    private javax.swing.JLabel statsMostRequestedProduct;
    private javax.swing.JLabel statsMostStock;
    private javax.swing.JLabel statsOldestEmployee;
    private javax.swing.JPanel statsPanel;
    private javax.swing.JLabel statsTotalEmployees;
    private javax.swing.JLabel statsTotalOrders;
    private javax.swing.JLabel statsTotalProducts;
    private javax.swing.JDialog userModal;
    private javax.swing.JButton userModalCancel;
    private javax.swing.JButton userModalConfirm;
    private javax.swing.JTextField userModalName;
    private javax.swing.JPasswordField userModalPassword;
    private javax.swing.JPasswordField userModalPasswordConfirm;
    private javax.swing.JLabel userModalPasswordConfirmLabel;
    private javax.swing.JLabel userModalPasswordLabel;
    private javax.swing.JTextField userModalRFC;
    private javax.swing.JComboBox<String> userModalRole;
    private javax.swing.JTextField userModalSurname;
    private javax.swing.JTextField userModalUsername;
    private javax.swing.JButton usersLink;
    private javax.swing.JPanel usersPanel;
    private javax.swing.JLabel usersPanelTitle;
    private javax.swing.JTable usersTable;
    // End of variables declaration//GEN-END:variables
}

// <editor-fold defaultstate="collapsed" desc="Helper classes">    
/**
 * Un escuchador personalizado de tabla para ejecutar acciones cuando se
 * eliminen filas de la misma
 *
 * @author Diego Domínguez Melo
 */
class OrderProductsModalListener implements TableModelListener {

    /**
     * La tabla a escuchar
     */
    JTable table;

    /**
     * Un botón a deshabilitar
     */
    JButton confirmButton;

    /**
     * Método constructor
     *
     * @param table Establece la tabla
     * @param confirmButton Establece el botón
     */
    OrderProductsModalListener(JTable table, JButton confirmButton) {
        this.table = table;
        this.confirmButton = confirmButton;
    }

    /**
     * Cada vez que cambie el modelo de la tabla, deshabilitar el botón (para
     * evitar crear órdenes vacías)
     *
     * @param e
     */
    @Override
    public void tableChanged(TableModelEvent e) {
        int rows = table.getRowCount();
        confirmButton.setEnabled(rows > 0);
    }
}


// </editor-fold>
