/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package jsf;

import beans.Ponencia;
import beans.PonenciaFacade;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

/**
 *
 * @author Aux de Programacion
 */
public class PonenciaConverter implements Converter{

    @Override
    public Object getAsObject(FacesContext facesContext, UIComponent component, String string) {
        if (string == null || string.length() == 0) {
            return null;
        }
        Integer id = new Integer(string);
        PonenciaFacade controller = (PonenciaFacade) facesContext.getApplication().getELResolver().getValue(facesContext.getELContext(), null, "ponenciaJpa");
        return controller.find(id);
    }

    @Override
    public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
        if (object == null) {
            return null;
        }
        if (object instanceof Ponencia) {
            Ponencia o = (Ponencia) object;
            return o.getIdponencia() == null ? "" : o.getIdponencia().toString();
        } else {
            throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: beans.Ponencia");
        }
    }
}
