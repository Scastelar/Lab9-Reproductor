package reproductor_musica;

public class Playlist {
    private Nodo cabeza;

    public Nodo getCabeza() {
        return cabeza;
    }

    public Playlist() {
        this.cabeza = null;
    }

    public void agregarCancion(Cancion newCancion) {
        Nodo newNode = new Nodo(newCancion);
        if (cabeza == null) {
            cabeza = newNode;
        } else {
            Nodo temp = cabeza;
            while (temp.getSiguiente() != null) {
                temp = temp.getSiguiente();
            }
            temp.setSiguiente(newNode);
        }
    }

    public void mostrarCanciones() {
        Nodo temp = cabeza;
        while (temp != null) {
            Cancion cancion = temp.getCancion();
            System.out.println("Nombre: " + cancion.getNombre() + " Artista: " + cancion.getArtista());
            temp = temp.getSiguiente();
        }
    }
    
    public Cancion obtenerCancion(String nombre) {
        Nodo temp = cabeza;
        while (temp != null) {
            if (temp.getCancion().getNombre().equals(nombre)) {
                return temp.getCancion();
            }
            temp = temp.getSiguiente();
        }
        return null; 
    }
}
