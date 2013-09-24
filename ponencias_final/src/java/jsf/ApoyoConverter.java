/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package jsf;

import beans.Apoyo;
import beans.ApoyoFacade;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

/**
 *
 * @author Carmen
 */
public class ApoyoConverter implements Converter {

    public Object getAsObject(FacesContext facesContext, UIComponent component, String string) {
        if (string == null || string.length() == 0) {
            return null;
        }
        Integer id = new Integer(string);
        ApoyoFacade controller = (ApoyoFacade) facesContext.getApplication().getELResolver().getValue(facesContext.getELContext(), null, "apoyoJpa");
        return controller.find(id);
    }

    public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
        if (object == null) {
            return null;
        }
        if (object instanceof Apoyo) {
            Apoyo o = (Apoyo) object;
            return o.getIdapoyo() == null ? "" : o.getIdapoyo().toString();
        } else {
            throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: beans.Apoyo");
        }
    }

}
