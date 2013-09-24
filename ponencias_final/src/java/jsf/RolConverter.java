/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package jsf;

import beans.Rol;
import beans.RolFacade;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

/**
 *
 * @author Carmen
 */
public class RolConverter implements Converter {

    public Object getAsObject(FacesContext facesContext, UIComponent component, String string) {
        if (string == null || string.length() == 0) {
            return null;
        }
        Integer id = new Integer(string);
        RolFacade controller = (RolFacade) facesContext.getApplication().getELResolver().getValue(facesContext.getELContext(), null, "rolJpa");
        return controller.find(id);
    }

    public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
        if (object == null) {
            return null;
        }
        if (object instanceof Rol) {
            Rol o = (Rol) object;
            return o.getIdrol() == null ? "" : o.getIdrol().toString();
        } else {
            throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: beans.Rol");
        }
    }

}
