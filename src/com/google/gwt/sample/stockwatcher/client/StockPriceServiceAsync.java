package com.google.gwt.sample.stockwatcher.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Interfaz que sirve para poder interactuar con el servicio remoto
 */
public interface StockPriceServiceAsync {

	/**
	 * Función que nos permite definir la función encargada de recibir la
	 * petición de recuperación de precio desde el servidor
	 * 
	 * @param symbols
	 *            String[] Array conteniendo los símbolos de las acciones
	 * @param callback
	 *            AsyncCallback Objeto encargado de recibir las respuestas del
	 *            servicio remoto
	 */
	void getPrices(String[] symbols, AsyncCallback<StockPrice[]> callback);

}
