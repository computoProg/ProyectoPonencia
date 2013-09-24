/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package jsf;

import beans.Pais;
import beans.PaisFacade;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

/**
 *
 * @author Aux de Programacion
 */
public class PaisConverter implements Converter{

    @Override
    public Object getAsObject(FacesContext facesContext, UIComponent component, String string) {
        if (string == null || string.length() == 0) {
            return null;
        }
        Integer id = new Integer(string);
        PaisFacade controller = (PaisFacade) facesContext.getApplication().getELResolver().getValue(facesContext.getELContext(), null, "paisJpa");
        return controller.find(id);
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object object) {
        if (object == null) {
            return null;
        }
        if (object instanceof Pais) {
            Pais o = (Pais) object;
            return o.getIdPais() == null ? "" : o.getIdPais().toString();
        } else {
            throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: beans.Pais");
        }
    }

}
