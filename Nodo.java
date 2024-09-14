package reproductor_musica;

import java.io.Serializable;

public class Nodo implements Serializable {

    private static final long serialVersionUID = 1L;
    private Cancion cancion;
    private Nodo siguiente;

    public Nodo(Cancion cancion) {
        this.cancion = cancion;
        this.siguiente = null;
    }

    public Cancion getCancion() {
        return cancion;
    }

    public Nodo getSiguiente() {
        return siguiente;
    }

    public void setSiguiente(Nodo siguiente) {
        this.siguiente = siguiente;
    }
}
