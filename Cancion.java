
package reproductor_musica;


public class Cancion {
     private String nombre;
     private String artista;
     private int duracion;
     private String imagen;
     private String tipo;

    public Cancion(String nombre, String artista, int duracion,String imagen,String tipo) {
        this.nombre = nombre;
        this.duracion = duracion;
        this.artista = artista;
        this.imagen = imagen;
        this.tipo = tipo;
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
}
