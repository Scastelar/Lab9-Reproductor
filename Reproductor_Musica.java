package reproductor_musica;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;
import java.io.*;
import java.util.LinkedList;

public class Reproductor_Musica {

    private LinkedList<Cancion> listaCanciones;
    private String archivoBinario;
    private Player player;
    private Thread playThread;
    private boolean isPausado;
    private InputStream fileInputStream;
    private long duracion;
    private long pausar;

    public LinkedList<Cancion> getListaCanciones() {
        return listaCanciones;
    }

    public Reproductor_Musica(String archivoBinario) {
        this.archivoBinario = archivoBinario;
        this.listaCanciones = new LinkedList<>();
        this.isPausado = false;
        inicializarArchivo();
    }

    private void inicializarArchivo() {
        File archivo = new File(archivoBinario);
        if (!archivo.exists()) {
            try {
                archivo.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            cargarCanciones();
        }
    }

    public void agregarCancion(Cancion cancion) {
        listaCanciones.add(cancion);
        guardarCanciones();
    }

    private void guardarCanciones() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(archivoBinario))) {
            oos.writeObject(listaCanciones);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void cargarCanciones() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(archivoBinario))) {
            listaCanciones = (LinkedList<Cancion>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public Cancion getCancion(String nombre) {
        for (Cancion cancion : listaCanciones) {
            if (cancion.getNombre().equalsIgnoreCase(nombre)) {
                return cancion;
            }
        }
        return null;
    }

    public void reproducirCancion(String rutaArchivo) {
        detenerCancion();

        playThread = new Thread(() -> {
            try {
                fileInputStream = new FileInputStream(rutaArchivo);
                duracion = fileInputStream.available();

                player = new Player(fileInputStream);
                player.play();
            } catch (JavaLayerException | IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        playThread.start();
        isPausado = false;
    }

    public void pausarCancion() {
        if (player != null && !isPausado) {
            try {
                pausar = fileInputStream.available();
                player.close();
                isPausado = true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void reanudarCancion(String rutaArchivo) {
        if (isPausado) {
            playThread = new Thread(() -> {
                try {
                    fileInputStream = new FileInputStream(rutaArchivo);
                    fileInputStream.skip(duracion - pausar);
                    player = new Player(fileInputStream);
                    player.play();
                } catch (JavaLayerException | IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        fileInputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            playThread.start();
            isPausado = false;
        }
    }

    public void detenerCancion() {
        if (player != null) {
            player.close();
            player = null;
        }
        isPausado = false;
    }
}
