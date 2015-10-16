package com.google.gwt.sample.stockwatcher.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Interfaz que sirve para poder interactuar con el servicio remoto
 */
public interface StockPriceServiceAsync {

	/**
	 * Funci�n que nos permite definir la funci�n encargada de recibir la
	 * petici�n de recuperaci�n de precio desde el servidor
	 * 
	 * @param symbols
	 *            String[] Array conteniendo los s�mbolos de las acciones
	 * @param callback
	 *            AsyncCallback Objeto encargado de recibir las respuestas del
	 *            servicio remoto
	 */
	void getPrices(String[] symbols, AsyncCallback<StockPrice[]> callback);

}
