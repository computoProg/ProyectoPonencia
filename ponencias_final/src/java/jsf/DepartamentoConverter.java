/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package jsf;

import beans.Departamento;
import beans.DepartamentoFacade;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
/**
 *
 * @author Aux de Programacion
 */
public class DepartamentoConverter implements Converter{
 @Override
 public Object getAsObject(FacesContext facesContext, UIComponent component, String string) {
        if (string == null || string.length() == 0) {
            return null;
        }
        Integer id = new Integer(string);
        DepartamentoFacade controller = (DepartamentoFacade) facesContext.getApplication().getELResolver().getValue(facesContext.getELContext(), null, "departamentoJpa");
        return controller.find(id);
    }

    @Override
    public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
        if (object == null) {
            return null;
        }
        if (object instanceof Departamento) {
            Departamento o = (Departamento) object;
            return o.getIdDepartamento() == null ? "" : o.getIdDepartamento().toString();
        } else {
            throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: beans.Departamento");
        }
    }
}
