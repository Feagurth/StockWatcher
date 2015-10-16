package com.google.gwt.sample.stockwatcher.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * Interfaz que sirve para definir las funciones que habrá que implementar en
 * StockPriceServiceImpl
 * 
 * @RemoteServiceRelativePath sirve para especificar la ruta sobre la que se
 *                            deberán hacer las peticiones y que se define en
 *                            StockWatcher/war/WEB-INF/web.xml
 */
@RemoteServiceRelativePath("stockPrices")
public interface StockPriceService extends RemoteService {

	/**
	 * Función que sirve para recuperar los precios de las acciones que se pasan
	 * como parámetro
	 * 
	 * @param symbols
	 *            Array con los símbolos de las acciones
	 * @return StockPrice[] Array con los precios de las acciones
	 * @throws DelistedException
	 *             Si se produce un error se lanza una excepcion del tipo
	 *             DelistedExcepction que hemos definido nosotros mismos
	 */
	StockPrice[] getPrices(String[] symbols) throws DelistedException;
}