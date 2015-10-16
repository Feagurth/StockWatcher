package com.google.gwt.sample.stockwatcher.server;

import com.google.gwt.sample.stockwatcher.client.DelistedException;
import com.google.gwt.sample.stockwatcher.client.StockPrice;
import com.google.gwt.sample.stockwatcher.client.StockPriceService;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import java.util.Random;

/**
 * Clase donde se encuentra la implementaci�n de las funciones que se definen en
 * StockPriceService
 * 
 * Al implementar como interfaz StockPriceService, estamos obligados a definir e
 * implementar todas las funciones que tenga el interfaz, definiendose de este
 * modo la funcionalidad del servicio
 */
public class StockPriceServiceImpl extends RemoteServiceServlet implements
		StockPriceService {

	/**
	 * Id generado aleatoriamente
	 */
	private static final long serialVersionUID = 4192379456341403664L;

	// Constante que usaremos para definir el precio m�ximo que puede alcanzar
	// una acci�n
	private static final double MAX_PRICE = 100.0;

	// Constante que usaremos para definir el m�ximo cambio de precio que puede
	// alcanzar una acci�n
	private static final double MAX_PRICE_CHANGE = 0.02;

	/**
	 * Funci�n que nos permite recuperar el precio y el cambi del mismo en un
	 * array de acciones
	 * 
	 * Si la funci�n encuentra un c�digo que sea igual a ERR lanzar� una
	 * excepci�n del tipo DelistedException cuya clase hemos creado nosotros
	 * mismos
	 */
	public StockPrice[] getPrices(String[] symbols) throws DelistedException {

		// Instanciamos un objeto para generar n�meros aleatorios
		Random rnd = new Random();

		// Creamos un array de objetos StockPrice del mismo tama�o que el array
		// de acciones que tenemos para poder almacenar en ellos los valores de
		// cada una de las acciones
		StockPrice[] prices = new StockPrice[symbols.length];

		// Iteramos por todas las acciones
		for (int i = 0; i < symbols.length; i++) {

			// Comprobamos si el s�mbolo introducido es ERR
			if (symbols[i].equals("ERR")) {

				// Si lo es lanzamos una excepci�n usando la clase que hemos
				// creado para tal prop�sito
				throw new DelistedException("ERR");
			}

			// Calculamos el precio de la acci�n multiplicando un n�mero decimal
			// generado aleatoriamente entre 0 y 1 y multiplic�ndolo por el
			// precio m�ximo que las acciones pueden alcanzar
			double price = rnd.nextDouble() * MAX_PRICE;

			// Calculamos el cambio de precio de la acci�n multiplicando su
			// precio por el m�ximo precio de cambio y por un n�mero decimal
			// aleatorio entre -1 y 1
			double change = price * MAX_PRICE_CHANGE
					* (rnd.nextDouble() * 2f - 1f);

			// Creamos un objeto StockPrice, pas�ndole los valores del s�mbolo,
			// el precio y el cambio, almacen�ndolo en el array que
			// anteriormente creamos
			prices[i] = new StockPrice(symbols[i], price, change);
		}

		// Devolvemos el array creado con los precios de las acciones
		return prices;
	}

}