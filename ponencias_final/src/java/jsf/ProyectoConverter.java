/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package jsf;

import beans.Proyecto;
import beans.ProyectoFacade;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

/**
 *
 * @author Carmen
 */
public class ProyectoConverter implements Converter {

    @Override
    public Object getAsObject(FacesContext facesContext, UIComponent component, String string) {
        if (string == null || string.length() == 0) {
            return null;
        }
        Integer id = new Integer(string);
        ProyectoFacade controller = (ProyectoFacade) facesContext.getApplication().getELResolver().getValue(facesContext.getELContext(), null, "proyectoJpa");
        return controller.find(id);
    }

    @Override
    public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
        if (object == null) {
            return null;
        }
        if (object instanceof Proyecto) {
            Proyecto o = (Proyecto) object;
            return o.getIdproyecto() == null ? "" : o.getIdproyecto().toString();
        } else {
            throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: beans.Proyecto");
        }
    }

}
