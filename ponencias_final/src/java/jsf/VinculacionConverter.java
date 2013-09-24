/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package jsf;

import beans.Vinculacion;
import beans.VinculacionFacade;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

/**
 *
 * @author Carmen
 */
public class VinculacionConverter implements Converter {

    public Object getAsObject(FacesContext facesContext, UIComponent component, String string) {
        if (string == null || string.length() == 0) {
            return null;
        }
        Integer id = new Integer(string);
        VinculacionFacade controller = (VinculacionFacade) facesContext.getApplication().getELResolver().getValue(facesContext.getELContext(), null, "vinculacionJpa");
        return controller.find(id);
    }

    public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
        if (object == null) {
            return null;
        }
        if (object instanceof Vinculacion) {
            Vinculacion o = (Vinculacion) object;
            return o.getIdvinculacion() == null ? "" : o.getIdvinculacion().toString();
        } else {
            throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: beans.Vinculacion");
        }
    }

}
