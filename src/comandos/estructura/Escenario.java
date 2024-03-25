package comandos.estructura;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Set;

import comandos.estructura.Posicion;
import comandos.juego.Imagen;
import comandos.personajes.Comando;

public class Escenario {

	private final String nombre;
	private final int ancho;
	private final int alto;
	private HashSet<Posicion> zonas;
	private HashSet<Posicion> ventanas;
	private Posicion arma; // se inicializa en la construcción en (0,0)
	private HashSet<Posicion> posBomba; // se inicializa vacía en la contrucción
	private LocalDateTime inicio; // no ofrece metodo de consulta
	private HashSet<Comando> comandosIntroducidos;
	private Estado estado;

	public Escenario(String nombre, int ancho, int alto, HashSet<Posicion> ventanas) {
		// la posición de las ventanas es un argumento variable
		this.nombre = nombre;
		this.ancho = ancho;
		this.alto = alto;
		this.zonas = new HashSet<Posicion>(); // como el escenario tiene todas las zonas abiertas la coleccion se
												// inicializa a vacia
		this.ventanas = ventanas;
		this.arma = new Posicion(0, 0);
		this.posBomba = new HashSet<Posicion>();
		this.inicio = null;
		this.comandosIntroducidos = new HashSet<Comando>();
		this.estado = null;
	}

	public Escenario(int ancho, int alto, String nombre) {
		this.nombre = nombre;
		this.alto = alto;
		this.ancho = ancho;
		this.zonas = new HashSet<Posicion>();
		this.ventanas = new HashSet<Posicion>();
		this.arma = new Posicion(0, 0);
		this.posBomba = new HashSet<Posicion>();
		this.inicio = null;
		this.comandosIntroducidos = new HashSet<Comando>();
		this.estado = null;
		
		for (int i = 0; i < 7; i = i + 2) {
			for (int j = 1; j < 5; j++) {
				Posicion p = new Posicion(j, i);
				this.zonas.add(p);
			}
		}
		// puede que no sea necesaria
		for (int i = 1; i < 8; i = i + 2) {
			for (int j = 0; j < 5; j = j + 2) {
				Posicion p = new Posicion(j, i);
				this.ventanas.add(p);
			}
		}
	}
	
	public Estado getEstado() {
		return estado;
	}
	
	public Comando posicionOcupadaPorComando(Posicion p) { //función auxiliar
		for (Comando comando : comandosIntroducidos) {
			if (comando.getPosActual().getX() == p.getX() && comando.getPosActual().getY() == p.getY()) {
				return comando;
			}
		}
		return null;
	}

	public Set<Posicion> getPosBomba() {
		return Collections.unmodifiableSet(this.posBomba);
	}

	public String getNombre() {
		return nombre;
	}

	public int getAncho() {
		return ancho;
	}

	public int getAlto() {
		return alto;
	}

	public Set<Posicion> getZonas() {
		return Collections.unmodifiableSet(this.zonas);
	}

	public Set<Posicion> getVentanas() {
		return Collections.unmodifiableSet(this.ventanas);
	}

	public Posicion getArma() {
		return arma;
	}

	public boolean estaCerrado(int i, int j) {
		Posicion p = new Posicion(i, j);
		if (zonas.contains(p)) {
			return true;
		}
		return false;
	}

	public boolean estaCerrado(Posicion p) {
		if (zonas.contains(p)) {
			return true;
		}
		return false;
	}
	
	public boolean tieneVentana(int i, int j) {
		Posicion p = new Posicion(i, j);
		if (ventanas.contains(p)) {
			return true;
		}
		return false;
	}
	
	private boolean estaDentro(Posicion p) {
		if ((p.getX() >= 0 && p.getX() <= ancho - 1) && (p.getY() >= 0 && p.getY() <= alto - 1)) {
			return true;
		} else {
			return false;
		}
	}
	
	public long tiempoTotal() {
		LocalDateTime fin = LocalDateTime.now();
		return ChronoUnit.SECONDS.between(inicio, fin);
	}
	
	public List<Direccion> posiblesDirecciones(int x, int y) {
		LinkedList<Direccion> opciones = new LinkedList<Direccion>();

		for (Direccion dir : Direccion.values()) {
			if (dir.equals(Direccion.ARRIBA)) {
				if (estaDentro(new Posicion(x, y + 1)) && !estaCerrado(x, y + 1)) {// && //posicionOcupadaPorComando(new Posicion(x,y+1))==null) {
					opciones.add(Direccion.ARRIBA);
				}
			} else if (dir.equals(Direccion.ABAJO)) {
				if (estaDentro(new Posicion(x, y - 1)) && !estaCerrado(x, y - 1)) {// && posicionOcupadaPorComando(new Posicion(x,y-1))==null) {
					opciones.add(Direccion.ABAJO);
				}
			} else if (dir.equals(Direccion.IZQUIERDA)) {
				if (estaDentro(new Posicion(x - 1, y)) && !estaCerrado(x - 1, y)) {// && posicionOcupadaPorComando(new Posicion(x-1,y))==null ) {
					opciones.add(Direccion.IZQUIERDA);
				}
			} else { 
				if (estaDentro(new Posicion(x + 1, y)) && !estaCerrado(x + 1, y)) {// && posicionOcupadaPorComando(new Posicion(x+1,y))==null) {
					opciones.add(Direccion.DERECHA);
				}
			}
		}
		return Collections.unmodifiableList(opciones);
	}
	
	public boolean desplazarObjetivo(Direccion dir) {
		Posicion p = this.arma.adyacente(dir);
		if (estaDentro(p)) {
			this.arma = p;
			return true;
		} else {
			return false;
		}
	}
	
	public void liberarBomba(Posicion p) {
			posBomba.add(p);
	}
	
	public void iniciarPartida() {
		this.inicio = LocalDateTime.now();
		this.estado = Estado.EN_JUEGO;
	}

	public long tiempoJuego() {
		return ChronoUnit.SECONDS.between(inicio, LocalDateTime.now());
	}
	
	public boolean introducirComando(Comando c) {
		Random r = new Random();
		Posicion p = new Posicion(r.nextInt(ancho), alto - 1);

		c.setPosActual(p);
		c.setEscenario(this);
		if (comandosIntroducidos.add(c)) {
			return true;
		} else {
			return false;
		}
	}
	
	public void disparar(Posicion p) {  //Tengo que hacer la copia con el metodo clone
		Posicion p2 = null;
		if (posicionOcupadaPorComando(p) != null && tieneVentana(p.getX(), p.getY())) {
			for (Comando comando : comandosIntroducidos) {
				if (comando.getPosActual().getX() == p.getX() && comando.getPosActual().getY() == p.getY()) {
					p2 = p;
					// comandosIntroducidos.remove(comando);
				}
			}
		}
		
		if (p2 != null) {
			Comando comando = posicionOcupadaPorComando(p2);
			comandosIntroducidos.remove(posicionOcupadaPorComando(p2));
			Comando copia = comando.clone();
			comandosIntroducidos.add(copia);
			this.introducirComando(copia);
		}
		
		p2 = null;
		for (Posicion bomba : posBomba) {
			if (bomba.getX() == p.getX() && bomba.getY() == p.getY()) {
				p2 = p;
			}
		}
		
		if(p2 != null) {
			posBomba.remove(p2);
		}
	}
	
	public void actualizar() {
		//Alarma.dormir(1000);
		for (Comando comando : comandosIntroducidos) {
			Direccion dir = comando.movimiento();
			comando.lanzarBomba();
			
			if (comando.getPosActual().getY() == 0) {
				estado = Estado.FIN_POR_CONTROL;
			}
			
			if (dir != null) {
				Posicion p = comando.getPosActual().adyacente(dir);
				comando.setPosActual(p);
			}
		}
		
		HashSet<Posicion> copia = new HashSet<Posicion>();
		for (Posicion posicion : posBomba) {
			if (posicion.getY() == 0) {
				estado = Estado.FIN_POR_BOMBA;

			} else {
				copia.add(posicion.adyacente(Direccion.ABAJO));
				posBomba = copia;
			}
		}
	}
	
	public List<Imagen> getImagenes() {
		LinkedList<Imagen> resultado = new LinkedList<>();
		HashSet<Comando> listaComandos = comandosIntroducidos;
		for (Comando comando : listaComandos) {
			Imagen imagen = new Imagen(comando.getPosActual().getX(), comando.getPosActual().getY(), comando.getRuta());
			resultado.add(imagen);
		}

		for (int i = 0; i < this.ancho; i++) {
			for (int j = 0; j < this.alto; j++) {
				if (tieneVentana(i, j)) {
					Imagen imagen = new Imagen(i, j, "imagenes/ventana.png");
					resultado.add(imagen);
				} /*else {
					Imagen imagen = new Imagen(i, j, "imagenes/ladrillos.png"); 
					resultado.add(imagen);
				}*/
				if (estaCerrado(i, j)) { 
					Imagen imagen = new Imagen(i, j, "imagenes/zona-cerrada.png"); 
					resultado.add(imagen); 
				}
			}
		}
		
		for (Posicion bomba : posBomba) {
			Imagen imagen = new Imagen(bomba.getX(), bomba.getY(), "imagenes/bomba.png");
			resultado.add(imagen);
		}
		
		return Collections.unmodifiableList(resultado);
	}
	
}
