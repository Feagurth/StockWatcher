package com.google.gwt.sample.stockwatcher.client;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.core.client.JsonUtils;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.http.client.URL;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * Clase StockWatcher
 */
public class StockWatcher implements EntryPoint {

	/**
	 * Constante para definir el tiempo de refresco de las actualizaciones
	 */
	private static final int REFRESH_INTERVAL = 5000;

	/**
	 * Constante para definir la url necesaria para realizar peticiones JSON
	 */
	private static final String JSON_URL = GWT.getModuleBaseURL()
			+ "stockPrices?q=";

	// Objetos usados para la interfaz gráfica
	private VerticalPanel mainPanel = new VerticalPanel();
	private FlexTable stocksFlexTable = new FlexTable();
	private HorizontalPanel addPanel = new HorizontalPanel();
	private TextBox newSymbolTextBox = new TextBox();
	private Button addStockButton = new Button("Add");
	private Label lastUpdatedLabel = new Label();
	private Label errorMsgLabel = new Label();

	// ArrayList que contiene las acciones que el usuario ha introducido
	private ArrayList<String> stocks = new ArrayList<String>();

	// Creamos una instancia del servicio de datos private
	StockPriceServiceAsync stockPriceSvc = GWT.create(StockPriceService.class);

	// Creamos una instancia de la interfaz de constantes internacionalizadas
	private StockWatcherConstants constants = GWT
			.create(StockWatcherConstants.class);

	// Creamos una instancia de la interfaz de mensajes internacionalizados
	private StockWatcherMessages messages = GWT
			.create(StockWatcherMessages.class);

	/**
	 * Método de entrada. Es el primer método que se carga al iniciar la clase
	 * En este método irá toda la inicizalición de la interfaz gráfica, así como
	 * la asignación de eventos
	 */
	public void onModuleLoad() {

		// Definimos el título de la ventana sacando el valor de las constantes
		// internacionalizadas
		Window.setTitle(constants.stockWatcher());

		// Recuperamos el objeto con id 'appTitle' de la página html estática y
		// le añadimos el valor de la constantes internacionalizadas del nombre
		// de la aplicación
		RootPanel.get("appTitle").add(new Label(constants.stockWatcher()));

		// Creamos el botón de añadir acciones, pasándole como texto la cadena
		// sacada de la interfaz de constantes internacionalizadas
		addStockButton = new Button(constants.add());

		// Definimos los valores de los encabezados de las columnas, recuperando
		// el texto de las mismas de la interfaz de internacionalización
		stocksFlexTable.setText(0, 0, constants.symbol());
		stocksFlexTable.setText(0, 1, constants.price());
		stocksFlexTable.setText(0, 2, constants.change());
		stocksFlexTable.setText(0, 3, constants.remove());

		// Asignamos un relleno a las celdas de la tabla
		stocksFlexTable.setCellPadding(4);

		// Añadimos los estilos a la tabla
		stocksFlexTable.getRowFormatter().addStyleName(0, "watchListHeader");
		stocksFlexTable.addStyleName("watchList");
		stocksFlexTable.getCellFormatter().addStyleName(0, 0,
				"watchListNonNumericColumnHeader");
		stocksFlexTable.getCellFormatter().addStyleName(0, 1,
				"watchListNumericColumnHeader");
		stocksFlexTable.getCellFormatter().addStyleName(0, 2,
				"watchListNumericColumnHeader");
		stocksFlexTable.getCellFormatter().addStyleName(0, 3,
				"watchListRemoveColumn");

		// Añadimos los controles necesarios al panel
		addPanel.add(newSymbolTextBox);
		addPanel.add(addStockButton);
		addPanel.addStyleName("addPanel");

		// Definimos una etiqueta para mostrar los mensajes de error y le
		// asignamos el estilo que hemos definido para ella
		errorMsgLabel.setStyleName("errorMessage");

		// Hacemos la etiqueta invisible hasta que tengamos un error que mostrar
		errorMsgLabel.setVisible(false);

		// Definimos un evento para la pulsación del botón en el cuadro de texto
		newSymbolTextBox.addKeyDownHandler(new KeyDownHandler() {

			/**
			 * Evento de pulsación de tecla
			 */
			public void onKeyDown(KeyDownEvent event) {
				// Comprobamos si la tecla pulsada es la tecla enter
				if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
					// Llamamos a la función para añadir una acción
					addStock();
				}
			}
		});

		// Definimos un evento para el botón de añadir acción
		addStockButton.addClickHandler(new ClickHandler() {
			/**
			 * Evento click
			 */
			public void onClick(ClickEvent event) {
				// Llamamos a la función para añadir una acción
				addStock();
			}
		});

		// Añadimos el botón al panel
		addPanel.add(addStockButton);

		// Añadimos al panel principal el resto de controles
		mainPanel.add(errorMsgLabel);
		mainPanel.add(stocksFlexTable);
		mainPanel.add(addPanel);
		mainPanel.add(lastUpdatedLabel);

		// Asociamos el panel principal con la página HTML anfritriona
		RootPanel.get("stockList").add(mainPanel);

		// Movemos el cursor al cuadro de texto
		newSymbolTextBox.setFocus(true);

		// Creamos un objeto timer
		Timer refreshTimer = new Timer() {
			/**
			 * Asignarmos al la función de refrescar el listado de acciónes para
			 * que se realice a cada ejecución del timer
			 */
			@Override
			public void run() {
				refreshWatchList();
			}
		};

		// Definimos el tiempo de refresco del timer
		refreshTimer.scheduleRepeating(REFRESH_INTERVAL);

	}

	/**
	 * Función que nos permite añadir una acción a la tabla
	 */
	private void addStock() {

		// Recuperamos el valor introducido en la caja de texto, recortamos los
		// espacios en blanco y lo pasamos a mayúsculas, tras lo cual lo
		// almacenamos en una variable
		final String symbol = newSymbolTextBox.getText().toUpperCase().trim();

		// Pasamos el foco al cuadro de texto
		newSymbolTextBox.setFocus(true);

		// Usamos expresiones regulares para validar el valor introducido.
		// El sistema solo permitirá numeros, letras y puntos con una longitud
		// entre 1 y 10
		if (!symbol.matches("^[0-9A-Z&#92;&#92;.]{1,10}$")) {
			// Si el valor introducido no es válido, mostramos un mensaje al
			// usuario cuyo contenido viene definino en el interfaz de
			// internacionalización correspondiente
			Window.alert(messages.invalidSymbol(symbol));

			// Seleccionamos todo el texto que contiene el TextBox
			newSymbolTextBox.selectAll();

			// Salimos de la función
			return;
		}

		// Si todo es correcto, borramos el valor introducido
		newSymbolTextBox.setText("");

		// Comprobamos si el ArrayList de acciones contiene el valor introducido
		if (stocks.contains(symbol)) {
			// Si el valor ya existe, no hace falta volver a introducirlo, por
			// tanto salimos de la función
			return;
		}

		// Almacenamos el número de filas que tiene la tabla
		int row = stocksFlexTable.getRowCount();

		// Añadimos la acción al ArrayList de almacenaje de acciones
		stocks.add(symbol);

		// Añadimos la acción a la última fila de la tabla
		stocksFlexTable.setText(row, 0, symbol);

		// Añadimos una etiqueta a cada celda de la columna 2
		stocksFlexTable.setWidget(row, 2, new Label());

		// Añadimos los estilos de las columnas a la nueva fila
		stocksFlexTable.getCellFormatter().addStyleName(row, 1,
				"watchListNumericColumn");
		stocksFlexTable.getCellFormatter().addStyleName(row, 2,
				"watchListNumericColumn");
		stocksFlexTable.getCellFormatter().addStyleName(row, 3,
				"watchListRemoveColumn");

		// Creamos un botón destinado a eliminar la fila de la lista
		Button removeStockButton = new Button("x");

		// Definimos el estilo para el botón
		removeStockButton.addStyleDependentName("remove");

		// Añadimos un evento click al botón recién creado
		removeStockButton.addClickHandler(new ClickHandler() {
			/**
			 * Función para el evento click
			 */
			public void onClick(ClickEvent event) {

				// Recuperamos y almacenamos la posición que le corresponde en
				// el ArrayList a la acción que vamos a eliminar
				int removedIndex = stocks.indexOf(symbol);

				// Eliminamos la acción del ArrayList
				stocks.remove(removedIndex);

				// Eliminamos de la tabla la fila correspondiente
				stocksFlexTable.removeRow(removedIndex + 1);
			}
		});

		// Usamos setWidget para incrustar el botón en la última columna de la
		// fila
		stocksFlexTable.setWidget(row, 3, removeStockButton);

		// Refrescamos la lista de acciones
		refreshWatchList();

	}

	/**
	 * Función que calcula y refresca los preciones de las acciones
	 */
	private void refreshWatchList() {

		// Comprobamos si tenemos algún valor en el array de acciones
		if (stocks.size() == 0) {
			// Si no hay ninguno, salimos de la función
			return;
		}

		// Recuperamos la url de las peticiones JSON y la asociamos a una
		// variable
		String url = JSON_URL;

		// Transformamos el arraylist en un iterador
		Iterator<String> iter = stocks.iterator();

		// Iteramos por todos los elementos
		while (iter.hasNext()) {

			// Añadimos a la url el código de la acción
			url += iter.next();

			// Comprobamos si hay mas registros tras el actual
			if (iter.hasNext()) {
				// Si es así, concatenamos a la url un signo +
				url += "+";
			}
		}

		// Transformamos la cadena de texto en una url y la almacenamos
		url = URL.encode(url);

		// Creamos un constructor de peticiones que realizará una petición tipo
		// GET a la url especificada para realizar consultas JSON
		RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, url);

		try {

			// A partir del constructor creamos una petición sobre la cual
			// asignaremos eventos para cuando se produzca un error o una
			// respuesta exitosa
			Request request = builder.sendRequest(null, new RequestCallback() {
				/**
				 * Función que se ejecutará cuando en la petición de datos se
				 * produzca un error
				 */
				public void onError(Request request, Throwable exception) {
					// Llamamos a la función diseñada para mostrar errores
					// pasándole una cadena con el mensaje que queremos mostrar
					// usando el sistema de internacionalización
					displayError(messages.retrieveJSONError(""));
				}

				/**
				 * Función que se ejecutará cuando la petición de datos sea
				 * exitosa
				 */
				public void onResponseReceived(Request request,
						Response response) {

					// Comprobamos que el código de respuesta enviado por el
					// servidor sea 200, o lo que es lo mismo que la petición ha
					// sido completada exitosamente
					if (200 == response.getStatusCode()) {

						// Llamamos a la función updateTable pasando como
						// parámetros un JsArray de objetos StockData que
						// conseguiremos tras usar JsonUtils para convertir la
						// respuesta en modo de texto de servidor en el JsArray
						// de objetos StockData que pide la función como
						// parámetros
						updateTable(JsonUtils
								.<JsArray<StockData>> safeEval(response
										.getText()));
					} else {
						// Si el código de respuesta no es 200, mostramos un
						// mensaje de error formado por una cadena que
						// recuperamos del sistema de internacionalización
						// concatenado con el texto de respuesta del servidor
						displayError(messages.retrieveJSONError("("
								+ response.getStatusText() + ")"));
					}
				}
			});
		} catch (RequestException e) {
			// Si se produce una excepción, mostramos así mismo un mensaje de
			// error
			displayError(messages.retrieveJSONError(""));
		}

		/*
		 * 
		 * Código para hacer peticiones de refresco de datos usando RPC
		 * 
		 * Comentado para poder implementar la recuperación de información en
		 * formato JSON
		 * 
		 * // Inicializamos el servicio Proxy if (stockPriceSvc == null) {
		 * stockPriceSvc = GWT.create(StockPriceService.class); }
		 * 
		 * // Configuramos el objeto de retrollamada encargado de recibir la //
		 * respuesta del servidor AsyncCallback<StockPrice[]> callback = new
		 * AsyncCallback<StockPrice[]>() {
		 *//**
		 * Función que se ejecutará cuando se produzca un error en la
		 * petición remota
		 */
		/*
		 * public void onFailure(Throwable caught) {
		 * 
		 * // Recuperamos el mensaje de error de la excepción y lo //
		 * almacenamos en una variable String details = caught.getMessage();
		 * 
		 * // Comprobamos si el excepción generada es una instancia de la //
		 * excepción que hemos creado nosotros if (caught instanceof
		 * DelistedException) {
		 * 
		 * // Si es así, recuperamos el símbolo de la acción que ha // generado
		 * la excepción y creamos un mensaje para mostrar // al usuario haciendo
		 * uso del sistema de // internacionalización details = messages
		 * .companyError(((DelistedException) caught) .getSymbol()); }
		 * 
		 * // Asignamos una cadena de texto con el mensaje de error a la //
		 * etiqueta correspondiente haciendo uso de la // internacionalización
		 * errorMsgLabel.setText(constants.error() + ": " + details);
		 * 
		 * // Hacemos la etiqueta visible errorMsgLabel.setVisible(true); }
		 *//**
		 * Función que se ejecutará cuando se produzca un exito en la
		 * petición remota
		 * 
		 * @param result
		 *            StockPrice[] Array objetos StockPrice que contiene los
		 *            valores de precios de las acciones
		 */
		/*
		 * public void onSuccess(StockPrice[] result) { // Actualizamos la tabla
		 * con los resultados obtenidos del // servidor updateTable(result); }
		 * };
		 * 
		 * // Llamamos al servicio de precios pasándole como parámetros el array
		 * de // símbolos y el objeto de retrollamada que será el encargado de
		 * tratar // con los resultados obtenidos por el servidor
		 * stockPriceSvc.getPrices(stocks.toArray(new String[0]), callback);
		 */

	}

	/**
	 * Función que nos permite actualizar los valores de la tabla de acciones
	 * 
	 * @param prices
	 *            Array con los valores de las acciones
	 */
	private void updateTable(StockPrice[] prices) {

		// Comprobamos si tenemos precios con los que trabajar en el array
		if (prices.length > 0) {

			// Iteramos por el array de valores de las acciones
			for (int i = 0; i < prices.length; i++) {

				// Actualizamos en la tabla el precio introducido
				updateTable(prices[i]);
			}

			// Actualizamos el valor de la etiqueta con la fecha y hora actual
			// del sistema, recuperando el mensaje de la interfaz de
			// internacionalización correspondiente
			lastUpdatedLabel.setText(messages.lastUpdate(new Date()));

			// Ocultamos cualquier mensaje de error que se haya producido
			// anteriormente
			errorMsgLabel.setVisible(false);

		}
	}

	/**
	 * Función que nos permite actualizar los valores de la tabla de acciones
	 * 
	 * @param prices
	 *            Array de objetos StockData
	 */
	private void updateTable(JsArray<StockData> prices) {
		// Comprobamos si tenemos precios con los que trabajar en el array
		if (prices.length() > 0) {

			// Iteramos por el array de valores de las acciones
			for (int i = 0; i < prices.length(); i++) {

				// Actualizamos en la tabla el precio introducido
				updateTable(prices.get(i));
			}

			// Actualizamos el valor de la etiqueta con la fecha y hora actual
			// del sistema, recuperando el mensaje de la interfaz de
			// internacionalización correspondiente
			lastUpdatedLabel.setText(messages.lastUpdate(new Date()));

			// Ocultamos cualquier mensaje de error que se haya producido
			// anteriormente
			errorMsgLabel.setVisible(false);

		}
	}

	/**
	 * Actualiza una sola linea de la tabla de acciones
	 * 
	 * @param price
	 *            Información de los valores de la acción
	 */
	private void updateTable(StockPrice price) {

		// Verificamos que la acción aún forma parte de los valores introducidos
		// en la tabla
		if (!stocks.contains(price.getSymbol())) {
			// Si no es así ,salimos de la función
			return;
		}

		// Almacenamos la posición en la tabla de la acción que hemos
		// introducido, cuya posición será la posición en el ArrayList de
		// acciones +1
		int row = stocks.indexOf(price.getSymbol()) + 1;

		// Format the data in the Price and Change fields.
		String priceText = NumberFormat.getFormat("#,##0.00").format(
				price.getPrice());

		// Definimos un formato específico para mostrar los valores de la acción
		NumberFormat changeFormat = NumberFormat
				.getFormat("+#,##0.00;-#,##0.00");

		// Usamos el formato creado para tratar el cambio de precio de la acción
		// almacenando el resultado en una variable
		String changeText = changeFormat.format(price.getChange());

		// Usamos el formato creado para tratar el cambio de precio en
		// percentiles y lo almacenamos en una variable
		String changePercentText = changeFormat
				.format(price.getChangePercent());

		// Modificamos los valores de la fila de la tabla con los nuevos valores
		stocksFlexTable.setText(row, 1, priceText);

		// Recuperamos el objeto Label que hay en la celda correspondiente a la
		// columna 2
		Label changeWidget = (Label) stocksFlexTable.getWidget(row, 2);

		// Cambiamos el valor del texto de la etiqueta por el nuevo valor
		changeWidget.setText(changeText + " (" + changePercentText + "%)");

		// Definimos una variable con el nombre de un estilo si no hay cambios
		String changeStyleName = "noChange";

		// Comprobamos si el cambio de precio en percentiles es menor de -0.1
		if (price.getChangePercent() < -0.1f) {
			// Si es así cambiamos el valor de la variable por el nombre del
			// estilo para cambios negativos
			changeStyleName = "negativeChange";
		}
		// Si el cambio de precio en percentiles no es menor de -0.1,
		// comprobamos si es mayor de 0.1
		else if (price.getChangePercent() > 0.1f) {
			// Si es así cambiamos el valor de la variable por el nombre del
			// estilo para cambios positivos
			changeStyleName = "positiveChange";
		}

		// Cambiamos el estilo de la etiqueta con el estilo que hemos obtenido
		changeWidget.setStyleName(changeStyleName);

	}

	/**
	 * Actualiza una sola linea de la tabla de acciones
	 * 
	 * @param price
	 *            Información de los valores de la acción
	 */
	private void updateTable(StockData price) {

		// Verificamos que la acción aún forma parte de los valores introducidos
		// en la tabla
		if (!stocks.contains(price.getSymbol())) {
			// Si no es así ,salimos de la función
			return;
		}

		// Almacenamos la posición en la tabla de la acción que hemos
		// introducido, cuya posición será la posición en el ArrayList de
		// acciones +1
		int row = stocks.indexOf(price.getSymbol()) + 1;

		// Format the data in the Price and Change fields.
		String priceText = NumberFormat.getFormat("#,##0.00").format(
				price.getPrice());

		// Definimos un formato específico para mostrar los valores de la acción
		NumberFormat changeFormat = NumberFormat
				.getFormat("+#,##0.00;-#,##0.00");

		// Usamos el formato creado para tratar el cambio de precio de la acción
		// almacenando el resultado en una variable
		String changeText = changeFormat.format(price.getChange());

		// Usamos el formato creado para tratar el cambio de precio en
		// percentiles y lo almacenamos en una variable
		String changePercentText = changeFormat
				.format(price.getChangePercent());

		// Modificamos los valores de la fila de la tabla con los nuevos valores
		stocksFlexTable.setText(row, 1, priceText);

		// Recuperamos el objeto Label que hay en la celda correspondiente a la
		// columna 2
		Label changeWidget = (Label) stocksFlexTable.getWidget(row, 2);

		// Cambiamos el valor del texto de la etiqueta por el nuevo valor
		changeWidget.setText(changeText + " (" + changePercentText + "%)");

		// Definimos una variable con el nombre de un estilo si no hay cambios
		String changeStyleName = "noChange";

		// Comprobamos si el cambio de precio en percentiles es menor de -0.1
		if (price.getChangePercent() < -0.1f) {
			// Si es así cambiamos el valor de la variable por el nombre del
			// estilo para cambios negativos
			changeStyleName = "negativeChange";
		}
		// Si el cambio de precio en percentiles no es menor de -0.1,
		// comprobamos si es mayor de 0.1
		else if (price.getChangePercent() > 0.1f) {
			// Si es así cambiamos el valor de la variable por el nombre del
			// estilo para cambios positivos
			changeStyleName = "positiveChange";
		}

		// Cambiamos el estilo de la etiqueta con el estilo que hemos obtenido
		changeWidget.setStyleName(changeStyleName);

	}

	/**
	 * Función para mostrar mensajes de error
	 * 
	 * @param error
	 *            String Cadena que contiene el error
	 */
	private void displayError(String error) {
		// Asignamos el mensaje de error a la etiqueta correspondientes
		errorMsgLabel.setText(constants.error() + ": " + error);

		// La hacemos visible
		errorMsgLabel.setVisible(true);
	}

}