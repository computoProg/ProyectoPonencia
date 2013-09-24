/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package jsf;

import beans.TipoPonencia;
import beans.TipoPonenciaFacade;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
/**
 *
 * @author Aux de Programacion
 */
public class TipoPonenciaConverter implements Converter{
    @Override
 public Object getAsObject(FacesContext facesContext, UIComponent component, String string) {
        if (string == null || string.length() == 0) {
            return null;
        }
        Integer id = new Integer(string);
        TipoPonenciaFacade controller = (TipoPonenciaFacade) facesContext.getApplication().getELResolver().getValue(facesContext.getELContext(), null, "TipoPonenciaJpa");
        return controller.find(id);
    }

    @Override
    public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
        if (object == null) {
            return null;
        }
        if (object instanceof TipoPonencia) {
            TipoPonencia o = (TipoPonencia) object;
            return o.getIdtipo() == null ? "" : o.getIdtipo().toString();
        } else {
            throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: beans.TipoPonencia");
        }
    }
}
