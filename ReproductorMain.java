package reproductor_musica;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.LinkedList;

public class ReproductorMain extends JFrame implements ActionListener {

    private Reproductor_Musica reproductor;
    private JTextField nombreTxt = new JTextField();
    private JTextField artistaTxt = new JTextField();
    private JTextField duracionTxt = new JTextField();
    private JTextField tipoTxt = new JTextField();
    private JLabel imagenLabel = new JLabel();
    private JButton imagenButton = new JButton("Seleccionar Imagen");
    private JButton mp3Button = new JButton("Seleccionar MP3");
    private JButton agregarButton = new JButton("Agregar Cancion");
    private JButton playButton = new JButton("Play");
    private JButton pausaButton = new JButton("Pause");
    private JButton stopButton = new JButton("Stop");
    private File archivoImagen, archivoMP3;
    private JFileChooser fileChooser;
    private JComboBox<String> playlistCombo;
    private JTextPane infoTextPane;

    public ReproductorMain() {
        reproductor = new Reproductor_Musica("canciones.bin");

        setTitle("Reproductor de Musica");
        setSize(740, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel panelInfo = new JPanel(new GridLayout(6, 2, 10, 10));
        panelInfo.setBackground(Color.white);
        panelInfo.setBorder(BorderFactory.createEmptyBorder());

        nombreTxt.setPreferredSize(new Dimension(25, 25));
        artistaTxt.setPreferredSize(new Dimension(25, 25));
        duracionTxt.setPreferredSize(new Dimension(25, 25));
        tipoTxt.setPreferredSize(new Dimension(25, 25));
        imagenLabel.setPreferredSize(new Dimension(25, 25));
        imagenLabel.setSize(25, 25);

        panelInfo.add(new JLabel("Nombre:"));
        panelInfo.add(nombreTxt);
        panelInfo.add(new JLabel("Artista:"));
        panelInfo.add(artistaTxt);
        panelInfo.add(new JLabel("Duracion:"));
        panelInfo.add(duracionTxt);
        panelInfo.add(new JLabel("Tipo:"));
        panelInfo.add(tipoTxt);

        imagenButton.addActionListener(this);
        panelInfo.add(imagenButton);
        imagenButton.setBackground(Color.pink);
        imagenButton.setForeground(Color.white);
        panelInfo.add(imagenLabel);
        mp3Button.setBackground(Color.pink);
        mp3Button.setForeground(Color.white);
        mp3Button.addActionListener(this);
        panelInfo.add(mp3Button);
        agregarButton.setBackground(Color.pink);
        agregarButton.setForeground(Color.white);
        agregarButton.addActionListener(this);
        panelInfo.add(agregarButton);

        JPanel panelSeleccion = new JPanel(new BorderLayout());
        panelSeleccion.setBackground(Color.white);
        panelSeleccion.setBorder(BorderFactory.createTitledBorder("Seleccionar Cancion"));

        playlistCombo = new JComboBox<>();
        playlistCombo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mostrarInformacionCancion();
            }
        });
        panelSeleccion.add(playlistCombo, BorderLayout.NORTH);

        infoTextPane = new JTextPane();
        infoTextPane.setEditable(false);
        panelSeleccion.add(new JScrollPane(infoTextPane), BorderLayout.CENTER);

        JPanel panelControl = new JPanel(new FlowLayout());
        panelControl.setBackground(Color.white);
        playButton.addActionListener(this);
        pausaButton.addActionListener(this);
        stopButton.addActionListener(this);

        panelControl.add(playButton);
        panelControl.add(pausaButton);
        panelControl.add(stopButton);
        add(panelInfo, BorderLayout.NORTH);
        add(panelSeleccion, BorderLayout.CENTER);
        add(panelControl, BorderLayout.SOUTH);

        actualizarListaCanciones();
        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == imagenButton) {
            seleccionarImagen();
        } else if (e.getSource() == mp3Button) {
            seleccionarMP3();
        } else if (e.getSource() == agregarButton) {
            agregarCancion();
        } else if (e.getSource() == playButton) {
            reproducirCancion();
        } else if (e.getSource() == pausaButton) {
            pausarCancion();
        } else if (e.getSource() == stopButton) {
            detenerCancion();
        }
    }

    private void seleccionarImagen() {
        fileChooser = new JFileChooser();
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            archivoImagen = fileChooser.getSelectedFile();
            ImageIcon imageIcon = new ImageIcon(archivoImagen.getAbsolutePath());
            Image image = imageIcon.getImage().getScaledInstance(imagenLabel.getWidth(), imagenLabel.getHeight(), Image.SCALE_SMOOTH);
            imagenLabel.setIcon(new ImageIcon(image));
            imagenLabel.setText("");
        }
    }

    private void seleccionarMP3() {
        fileChooser = new JFileChooser();
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            archivoMP3 = fileChooser.getSelectedFile();
        }
    }

    private void agregarCancion() {
        String nombre = nombreTxt.getText();
        String artista = artistaTxt.getText();
        String duracionStr = duracionTxt.getText();
        String tipo = tipoTxt.getText();

        if (nombre.isEmpty() || artista.isEmpty() || duracionStr.isEmpty() || archivoImagen == null || archivoMP3 == null) {
            JOptionPane.showMessageDialog(this, "Debe llenar todos los campos y seleccionar archivos.");
            return;
        }

        try {
            int duracion = Integer.parseInt(duracionStr);
            Cancion cancion = new Cancion(nombre, artista, duracion, archivoImagen.getPath(), tipo, archivoMP3.getPath());
            reproductor.agregarCancion(cancion);
            JOptionPane.showMessageDialog(this, "Canción agregada.");
            actualizarListaCanciones();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "La duración debe ser un número entero.");
        }
    }

    private void mostrarInformacionCancion() {
        String nombreSeleccionado = (String) playlistCombo.getSelectedItem();
        Cancion cancion = reproductor.getCancion(nombreSeleccionado);
        if (cancion != null) {
            infoTextPane.setText("");

            StyledDocument doc = infoTextPane.getStyledDocument();
            SimpleAttributeSet attrs = new SimpleAttributeSet();
            StyleConstants.setFontSize(attrs, 16);
            try {
                doc.insertString(doc.getLength(), "Nombre: " + cancion.getNombre() + "\n", attrs);
                doc.insertString(doc.getLength(), "Artista: " + cancion.getArtista() + "\n", attrs);
                doc.insertString(doc.getLength(), "Duración: " + cancion.getDuracion() + " segundos\n", attrs);
                doc.insertString(doc.getLength(), "Tipo: " + cancion.getTipo() + "\n\n", attrs);

                ImageIcon imageIcon = new ImageIcon(cancion.getImagen());
                Image image = imageIcon.getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH); // Ajusta el tamaño según sea necesario
                doc.insertString(doc.getLength(), "\n", attrs);
                doc.insertString(doc.getLength(), "\n", attrs);

                ImageIcon icon = new ImageIcon(image);
                doc.insertString(doc.getLength(), " ", attrs);
                doc.setCharacterAttributes(doc.getLength() - 1, 1, attrs, true);
                doc.insertString(doc.getLength(), " ", attrs);
                doc.insertString(doc.getLength(), " ", attrs);
                doc.insertString(doc.getLength(), " ", attrs);

                doc.insertString(doc.getLength(), " ", attrs);
                infoTextPane.insertIcon(icon);

            } catch (BadLocationException e) {
                e.printStackTrace();
            }
        }
    }

    private void actualizarListaCanciones() {
        playlistCombo.removeAllItems();
        LinkedList<Cancion> listaCanciones = reproductor.getListaCanciones();
        for (Cancion cancion : listaCanciones) {
            playlistCombo.addItem(cancion.getNombre());
        }
    }

    private void reproducirCancion() {
        String nombre = (String) playlistCombo.getSelectedItem();
        Cancion cancion = reproductor.getCancion(nombre);
        if (cancion != null) {
            reproductor.reproducirCancion(cancion.getRutaMP3());
        } else {
            JOptionPane.showMessageDialog(this, "Debe seleccionar una cancion.");
        }
    }

    private void pausarCancion() {
        reproductor.pausarCancion();
    }

    private void detenerCancion() {
        reproductor.detenerCancion();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ReproductorMain::new);
    }
}
