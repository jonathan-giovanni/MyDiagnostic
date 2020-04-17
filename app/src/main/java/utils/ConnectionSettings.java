package utils;

/**
 * Created by admin on 12/6/17.
 */

public class ConnectionSettings {
    /**
     * Transición Home -> Detalle
     */
    public static final int CODIGO_DETALLE = 100;

    /**
     * Transición Detalle -> Actualización
     */
    public static final int CODIGO_ACTUALIZACION = 101;
    /**
     * Puerto que utilizas para la conexión.
     * Dejalo en blanco si no has configurado esta carácteristica.
     */
    private static final String PUERTO_HOST = "";
    /**
     * Dirección IP de genymotion o AVD
     */
    private static final String IP = "157.245.139.36";
    /**
     * URLs del Web Service
     */
    public static final String GETCountry = "http://" + IP + PUERTO_HOST + "/obtener_paises.php";
    public static final String GETBlood_type = "http://" + IP + PUERTO_HOST + "/obtener_tipoSangre.php";

    /**ENGLISH*/
    public static final String GETSymptom = "http://" + IP + PUERTO_HOST + "/obtener_metas.php";
    public static final String GetDisease = "http://" + IP + PUERTO_HOST + "/obtener_enfermedades.php";// el id del arrayJson es "diseases"
    public static final String GetDiseaseSymptom = "http://" + IP + PUERTO_HOST + "/obtener_multitabla.php"; // el id del arrayJson es "multitable"
    public static final String GETDiseases_category = "http://" + IP + PUERTO_HOST + "/obtener_categorias.php";
    public static final String GETData = "http://" + IP + PUERTO_HOST + "/obtener_dataversion.php"; //id:dataversion

    /**SPANISH*/
    public static final String getSintomas_esp = "http://" + IP + PUERTO_HOST + "/obtener_metas_esp.php";
    public static final String getEnfermedades_esp = "http://" + IP + PUERTO_HOST + "/obtener_enfermedades_esp.php";
    public static final String getEnfermedadSintoma_esp = "http://" + IP + PUERTO_HOST + "/obtener_multitabla_esp.php";
    public static final String getCategorias_esp = "http://" + IP + PUERTO_HOST + "/obtener_categorias_esp.php";
    public static final String getDataVersion_esp = "http://" + IP + PUERTO_HOST + "/obtener_dataversion_esp.php";

    /**
     * Clave para el valor extra que representa al identificador de una meta
     */
    public static final String EXTRA_ID = "IDEXTRA";

}
