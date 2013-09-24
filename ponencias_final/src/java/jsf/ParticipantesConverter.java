/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package jsf;

import beans.Participantes;
import beans.ParticipantesFacade;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

/**
 *
 * @author Carmen
 */
public class ParticipantesConverter implements Converter {

    @Override
    public Object getAsObject(FacesContext facesContext, UIComponent component, String string) {
        if (string == null || string.length() == 0) {
            return null;
        }
        Integer id = new Integer(string);
        ParticipantesFacade controller = (ParticipantesFacade) facesContext.getApplication().getELResolver().getValue(facesContext.getELContext(), null, "participantesJpa");
        return controller.find(id);
    }

    @Override
    public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
        if (object == null) {
            return null;
        }
        if (object instanceof Participantes) {
            Participantes o = (Participantes) object;
            return o.getCedula() == null ? "" : o.getCedula().toString();
        } else {
            throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: beans.Participantes");
        }
    }
}
