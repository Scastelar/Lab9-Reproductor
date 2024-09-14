package reproductor_musica;

import java.io.Serializable;

public class Cancion implements Serializable {

    private static final long serialVersionUID = 1L;
    private String nombre;
    private String artista;
    private int duracion;
    private String imagen;
    private String tipo;
    private String ruta;

    public Cancion(String nombre, String artista, int duracion, String imagen, String tipo, String ruta) {
        this.nombre = nombre;
        this.duracion = duracion;
        this.artista = artista;
        this.imagen = imagen;
        this.tipo = tipo;
        this.ruta = ruta;
    }

    public String getArtista() {
        return artista;
    }

    public String getImagen() {
        return imagen;
    }

    public String getTipo() {
        return tipo;
    }

    public String getNombre() {
        return this.nombre;
    }

    public int getDuracion() {
        return this.duracion;
    }

    public String getRutaMP3() {
        return this.ruta;
    }
}
