package util;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;

public class ConversorCPF implements Converter {

    /**
     *
     * @param context
     * @param component
     * @param value
     * @return
     * @throws ConverterException
     */
    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) throws ConverterException {
        // Convertendo CPF com máscara (111.111.111-11)
        // em CPF sem máscara (11111111111).
        String cpf = value;
        if (value != null && !value.equals("")) {
            cpf = value.replaceAll("\\.", "").replaceAll("\\-", "");
        }
        return cpf;
    }

    /**
     *
     * @param context
     * @param component
     * @param value
     * @return
     * @throws ConverterException
     */
    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) throws ConverterException {
        // Converter CPF sem máscara (11111111111)
        // em CPF com máscara (111.111.111-11)
        String cpf = (String) value;
        if (cpf != null && cpf.length() == 11) {
            cpf = cpf.substring(0, 3) + "." + cpf.substring(3, 6) + "." + cpf.substring(6, 9) + "-" + cpf.substring(9, 11);
        }

        return cpf;
    }
}