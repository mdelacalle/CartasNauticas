package mdelacalle.com.cartasnauticas;

import android.graphics.Color;

import java.util.ArrayList;

public class AemetAPIConstants {

    public static ArrayList<AreaCostera> getAreasCosteras(){
        ArrayList<AreaCostera> areasCosteras = new ArrayList<>();
        areasCosteras.add(new AreaCostera(42,"Costa de Andalucía Occidental y Ceuta"));
        areasCosteras.add(new AreaCostera(47,"Costa de Andalucía Oriental y Melilla"));
        areasCosteras.add(new AreaCostera(41,"Costa de Asturias, Cantabria y País Vasco"));
        areasCosteras.add(new AreaCostera(45,"Costa de Cataluña"));
        areasCosteras.add(new AreaCostera(40,"Costa de Galicia"));
        areasCosteras.add(new AreaCostera(44,"Costa de Illes Balears"));
        areasCosteras.add(new AreaCostera(43,"Costa de las Islas Canarias"));
        areasCosteras.add(new AreaCostera(46,"Costa de Valencia y Murcia"));
        return areasCosteras;
    }

    public static int getBackgroundColor(String nivelAviso) {
        switch (nivelAviso) {
            case "Amarillo":
                return Color.argb(255,255,255,0);
            case "amarillo":
                return Color.argb(75,255,255,0);
            case "Naranja":
                return Color.argb(125,255,140,0);
            case "naranja":
                return Color.argb(125,255,140,0);
            case "Rojo":
                return Color.argb(125,255,0,0);
            case "rojo":
                return Color.argb(125,255,0,0);
            case "Yellow":
                return Color.YELLOW;
            case "Orange":
                return Color.rgb(255,140,0);
            case "Red":
                return Color.RED;
            default:
                return Color.WHITE;
        }
    }

    public static String getCertaintyString(String certainty) {

        switch (certainty) {
            case "Observed":
                return "Aviso observado";
            case "Likely":
                return  "Probabilidad >50%";
            case "Possible":
                return "Probabilidad <50%";
                default:
                    return "";
        }

    }

    public static String getUrgencyString(String urgency) {
        switch (urgency) {
            case "Inmediate":
                return "La acción debe ser inmediata";
            case "Expected":
                return  "La acción deberá realizarse dentro de la siguiente hora";
            case "Future":
                return "La acción deberá realizarse en un futuro cercano";
            default:
                return "";
        }
    }

    public static class URGENCY {
        public static String INMEDIATE = "Inmediate";
        public static String EXPECTED = "Expected";
        public static String FUTURE = "Future";
    }

    public static String getAreaByCode(String province) {
        switch (province) {
            case "61":
                return "Andalucía";
            case "62":
                return "Aragón";
            case "63":
                return "Asturias";
            case "64":
                return "Baleares";
            case "78":
                return "Ceuta";
            case "65":
                return "Canarias";
            case "66":
                return "Cantabria";
            case "67":
                return "Castilla y León";
            case "68":
                return "Castilla - La Mancha";
            case "69":
                return "Cataluña";
            case "77":
                return "Comunidad Valenciana";
            case "70":
                return "Extremadura";
            case "71":
                return "Galicia";
            case "72":
                return "Madrid";
            case "79":
                return "Melilla";
            case "73":
                return "Murcia";
            case "74":
                return "Navarra";
            case "75":
                return "País Vasco";
            case "76":
                return "La Rioja";
            default:
                return "XX";
        }
    }


    public static String getCodeByProvince(String province){
        switch (province){
            case "Andalucía":
                return "61";
            case "Aragón":
                return "62";
            case "Asturias":
                return "63";
            case "Baleares":
                return "64";
            case "Ceuta":
                return "78";
            case "Canarias":
                return "65";
            case "Cantabria":
                return "66";
            case "Castilla y León":
                return "67";
            case "Castilla - La Mancha":
                return "68";
            case "Cataluña":
                return "69";
            case "Comunidad Valenciana":
                return "77";
            case "Extremadura":
                return "70";
            case "Galicia":
                return "71";
            case "Madrid":
                return "72";
            case "Melilla":
                return "79";
            case "Murcia":
                return "73";
            case "Navarra":
                return "74";
            case "País Vasco":
                return "75";
            case "La Rioja":
                return "76";
            default:
                return "XX";

        }
    }
}
