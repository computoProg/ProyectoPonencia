/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package jsf;


import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import beans.Ciudad;
import beans.CiudadFacade;

/**
 *
 * @author Aux de Programacion
 */
public class CiudadConverter implements Converter{
    @Override
 public Object getAsObject(FacesContext facesContext, UIComponent component, String string) {
        if (string == null || string.length() == 0) {
            return null;
        }
        Integer id = new Integer(string);
        CiudadFacade controller = (CiudadFacade) facesContext.getApplication().getELResolver().getValue(facesContext.getELContext(), null, "ciudadJpa");
        return controller.find(id);
    }

    @Override
    public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
        if (object == null) {
            return null;
        }
        if (object instanceof Ciudad) {
            Ciudad o = (Ciudad) object;
            return o.getIdCiudad() == null ? "" : o.getIdCiudad().toString();
        } else {
            throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: beans.Ciudad");
        }
    }
}
