package com.google.gwt.sample.stockwatcher.server;

import com.google.gwt.sample.stockwatcher.client.DelistedException;
import com.google.gwt.sample.stockwatcher.client.StockPrice;
import com.google.gwt.sample.stockwatcher.client.StockPriceService;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import java.util.Random;

/**
 * Clase donde se encuentra la implementación de las funciones que se definen en
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

	// Constante que usaremos para definir el precio máximo que puede alcanzar
	// una acción
	private static final double MAX_PRICE = 100.0;

	// Constante que usaremos para definir el máximo cambio de precio que puede
	// alcanzar una acción
	private static final double MAX_PRICE_CHANGE = 0.02;

	/**
	 * Función que nos permite recuperar el precio y el cambi del mismo en un
	 * array de acciones
	 * 
	 * Si la función encuentra un código que sea igual a ERR lanzará una
	 * excepción del tipo DelistedException cuya clase hemos creado nosotros
	 * mismos
	 */
	public StockPrice[] getPrices(String[] symbols) throws DelistedException {

		// Instanciamos un objeto para generar números aleatorios
		Random rnd = new Random();

		// Creamos un array de objetos StockPrice del mismo tamaño que el array
		// de acciones que tenemos para poder almacenar en ellos los valores de
		// cada una de las acciones
		StockPrice[] prices = new StockPrice[symbols.length];

		// Iteramos por todas las acciones
		for (int i = 0; i < symbols.length; i++) {

			// Comprobamos si el símbolo introducido es ERR
			if (symbols[i].equals("ERR")) {

				// Si lo es lanzamos una excepción usando la clase que hemos
				// creado para tal propósito
				throw new DelistedException("ERR");
			}

			// Calculamos el precio de la acción multiplicando un número decimal
			// generado aleatoriamente entre 0 y 1 y multiplicándolo por el
			// precio máximo que las acciones pueden alcanzar
			double price = rnd.nextDouble() * MAX_PRICE;

			// Calculamos el cambio de precio de la acción multiplicando su
			// precio por el máximo precio de cambio y por un número decimal
			// aleatorio entre -1 y 1
			double change = price * MAX_PRICE_CHANGE
					* (rnd.nextDouble() * 2f - 1f);

			// Creamos un objeto StockPrice, pasándole los valores del símbolo,
			// el precio y el cambio, almacenándolo en el array que
			// anteriormente creamos
			prices[i] = new StockPrice(symbols[i], price, change);
		}

		// Devolvemos el array creado con los precios de las acciones
		return prices;
	}

}