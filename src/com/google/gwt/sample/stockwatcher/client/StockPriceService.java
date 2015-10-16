package com.google.gwt.sample.stockwatcher.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * Interfaz que sirve para definir las funciones que habr� que implementar en
 * StockPriceServiceImpl
 * 
 * @RemoteServiceRelativePath sirve para especificar la ruta sobre la que se
 *                            deber�n hacer las peticiones y que se define en
 *                            StockWatcher/war/WEB-INF/web.xml
 */
@RemoteServiceRelativePath("stockPrices")
public interface StockPriceService extends RemoteService {

	/**
	 * Funci�n que sirve para recuperar los precios de las acciones que se pasan
	 * como par�metro
	 * 
	 * @param symbols
	 *            Array con los s�mbolos de las acciones
	 * @return StockPrice[] Array con los precios de las acciones
	 * @throws DelistedException
	 *             Si se produce un error se lanza una excepcion del tipo
	 *             DelistedExcepction que hemos definido nosotros mismos
	 */
	StockPrice[] getPrices(String[] symbols) throws DelistedException;
}