/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package jsf;

import beans.GrupoInvestigacion;
import beans.GrupoInvestigacionFacade;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

/**
 *
 * @author Aux de Programacion
 */
public class GrupoInvestigacionConverter implements Converter {

    @Override
    public Object getAsObject(FacesContext facesContext, UIComponent component, String string) {
        if(string == null || string.length() == 0)
            return null;
        Integer id = new Integer(string);
        GrupoInvestigacionFacade controller = (GrupoInvestigacionFacade)facesContext.getApplication().getELResolver().getValue(facesContext.getELContext(), null, "GrupoInvestigacionJpa");
        return controller.find(id);
    }

    @Override
    public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
        if(object == null)
            return null;
        if(object instanceof GrupoInvestigacion){
            GrupoInvestigacion gi = (GrupoInvestigacion)object;
            return gi.getIdGrupoInvestigacion()==null?"":gi.getIdGrupoInvestigacion().toString();
        }
        else{
            throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: beans.GrupoInvestigacion");
        }
    }

}
