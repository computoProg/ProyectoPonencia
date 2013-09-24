/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package jsf;

import beans.Participantes;
import beans.Vinculacion;
import beans.VinculacionFacade;
import beans.util.PagingInfo;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import javax.faces.FacesException;
import javax.annotation.Resource;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.model.SelectItem;
import javax.transaction.UserTransaction;
import beans.util.JsfUtil;

/**
 *
 * @author Carmen
 */
public class VinculacionController {

    public VinculacionController() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        jpaController = (VinculacionFacade) facesContext.getApplication().getELResolver().getValue(facesContext.getELContext(), null, "vinculacionJpa");
        pagingInfo = new PagingInfo();
        converter = new VinculacionConverter();
    }
    private Vinculacion vinculacion = null;
    private List<Vinculacion> vinculacionItems = null;
    private VinculacionFacade jpaController = null;
    private VinculacionConverter converter = null;
    private PagingInfo pagingInfo = null;
    private int num_participantes;
    private List<Participantes> participantes_vinculacion=null;
    @Resource
    private UserTransaction utx = null;

    public PagingInfo getPagingInfo() {
        if (pagingInfo.getItemCount() == -1) {
            pagingInfo.setItemCount(jpaController.count());
        }
        return pagingInfo;
    }

    public SelectItem[] getVinculacionItemsAvailableSelectMany() {
        return JsfUtil.getSelectItems(jpaController.findAll(), false);
    }

    public SelectItem[] getVinculacionItemsAvailableSelectOne() {
        return JsfUtil.getSelectItems(jpaController.findAll(), true);
    }
    public List<Participantes> getParticipantesVinculacion(){
            Vinculacion v = (Vinculacion) JsfUtil.getObjectFromRequestParameter("jsfcrud.currentVinculacion", converter, null);
        participantes_vinculacion=jpaController.participantesVinculacion(v.getIdvinculacion());
        return participantes_vinculacion;
    }

    public Vinculacion getVinculacion() {
        if (vinculacion == null) {
            vinculacion = (Vinculacion) JsfUtil.getObjectFromRequestParameter("jsfcrud.currentVinculacion", converter, null);
        }
        if (vinculacion == null) {
            vinculacion = new Vinculacion();
        }
        return vinculacion;
    }

    public String listSetup() {
        reset(true);
        return "vinculacion_list";
    }

    public String createSetup() {
        reset(false);
        vinculacion = new Vinculacion();
        return "vinculacion_create";
    }

    public String create() {
        try {
            utx.begin();
        } catch (Exception ex) {
        }
        try {
            Exception transactionException = null;
            jpaController.create(vinculacion);
            try {
                utx.commit();
            } catch (javax.transaction.RollbackException ex) {
                transactionException = ex;
            } catch (Exception ex) {
            }
            if (transactionException == null) {
                JsfUtil.addSuccessMessage("Vinculacion creado con exito.");
            } else {
                JsfUtil.ensureAddErrorMessage(transactionException, "un error ha ocurrido.");
            }
        } catch (Exception e) {
            try {
                utx.rollback();
            } catch (Exception ex) {
            }
            JsfUtil.ensureAddErrorMessage(e, "un error ha ocurrido.");
            return null;
        }
        return listSetup();
    }

    public String detailSetup() {
        return scalarSetup("vinculacion_detail");
    }

    public String editSetup() {
        return scalarSetup("vinculacion_edit");
    }

    private String scalarSetup(String destination) {
        reset(false);
        vinculacion = (Vinculacion) JsfUtil.getObjectFromRequestParameter("jsfcrud.currentVinculacion", converter, null);
        if (vinculacion == null) {
            String requestVinculacionString = JsfUtil.getRequestParameter("jsfcrud.currentVinculacion");
            JsfUtil.addErrorMessage("La vinculacion con id " + requestVinculacionString + " no existe.");
            return relatedOrListOutcome();
        }
        return destination;
    }

    public String edit() {
        String vinculacionString = converter.getAsString(FacesContext.getCurrentInstance(), null, vinculacion);
        String currentVinculacionString = JsfUtil.getRequestParameter("jsfcrud.currentVinculacion");
        if (vinculacionString == null || vinculacionString.length() == 0 || !vinculacionString.equals(currentVinculacionString)) {
            String outcome = editSetup();
            if ("vinculacion_edit".equals(outcome)) {
                JsfUtil.addErrorMessage("no se pudo editar vinculacion intente de nuevo.");
            }
            return outcome;
        }
        try {
            utx.begin();
        } catch (Exception ex) {
        }
        try {
            Exception transactionException = null;
            jpaController.edit(vinculacion);
            try {
                utx.commit();
            } catch (javax.transaction.RollbackException ex) {
                transactionException = ex;
            } catch (Exception ex) {
            }
            if (transactionException == null) {
                JsfUtil.addSuccessMessage("Vinculacion actualizado con exito.");
            } else {
                JsfUtil.ensureAddErrorMessage(transactionException, "un error ha ocurrido.");
            }
        } catch (Exception e) {
            try {
                utx.rollback();
            } catch (Exception ex) {
            }
            JsfUtil.ensureAddErrorMessage(e, "un error ha ocurrido.");
            return null;
        }
        return "vinculacion_list";
    }

    public String remove() {
        String idAsString = JsfUtil.getRequestParameter("jsfcrud.currentVinculacion");
        Integer id = new Integer(idAsString);
        try {
            utx.begin();
        } catch (Exception ex) {
        }
        try {
            Exception transactionException = null;
            jpaController.remove(jpaController.find(id));
            try {
                utx.commit();
            } catch (javax.transaction.RollbackException ex) {
                transactionException = ex;
            } catch (Exception ex) {
            }
            if (transactionException == null) {
                JsfUtil.addSuccessMessage("Vinculacion borrado con exito.");
            } else {
                JsfUtil.ensureAddErrorMessage(transactionException, "un error ha ocurrido.");
            }
        } catch (Exception e) {
            try {
                utx.rollback();
            } catch (Exception ex) {
            }
            JsfUtil.ensureAddErrorMessage(e, "un error ha ocurrido.");
            return null;
        }
        return relatedOrListOutcome();
    }

    private String relatedOrListOutcome() {
        String relatedControllerOutcome = relatedControllerOutcome();
        if (relatedControllerOutcome != null) {
            return relatedControllerOutcome;
        }
        return listSetup();
    }

    public List<Vinculacion> getVinculacionItems() {
        if (vinculacionItems == null) {
            getPagingInfo();
            vinculacionItems = jpaController.findRange(new int[]{pagingInfo.getFirstItem(), pagingInfo.getFirstItem() + pagingInfo.getBatchSize()});
        }
        return vinculacionItems;
    }
    public int getNum_participantes(){
    Vinculacion  vincu = vinculacion = (Vinculacion) JsfUtil.getObjectFromRequestParameter("jsfcrud.currentVinculacion", converter, null);

participantes_vinculacion=jpaController.participantesVinculacion(vincu.getIdvinculacion());
num_participantes=participantes_vinculacion.size();
return num_participantes;
    }

    public String next() {
        reset(false);
        getPagingInfo().nextPage();
        return "vinculacion_list";
    }

    public String prev() {
        reset(false);
        getPagingInfo().previousPage();
        return "vinculacion_list";
    }

    private String relatedControllerOutcome() {
        String relatedControllerString = JsfUtil.getRequestParameter("jsfcrud.relatedController");
        String relatedControllerTypeString = JsfUtil.getRequestParameter("jsfcrud.relatedControllerType");
        if (relatedControllerString != null && relatedControllerTypeString != null) {
            FacesContext context = FacesContext.getCurrentInstance();
            Object relatedController = context.getApplication().getELResolver().getValue(context.getELContext(), null, relatedControllerString);
            try {
                Class<?> relatedControllerType = Class.forName(relatedControllerTypeString);
                Method detailSetupMethod = relatedControllerType.getMethod("detailSetup");
                return (String) detailSetupMethod.invoke(relatedController);
            } catch (ClassNotFoundException e) {
                throw new FacesException(e);
            } catch (NoSuchMethodException e) {
                throw new FacesException(e);
            } catch (IllegalAccessException e) {
                throw new FacesException(e);
            } catch (InvocationTargetException e) {
                throw new FacesException(e);
            }
        }
        return null;
    }

    private void reset(boolean resetFirstItem) {
        vinculacion = null;
        vinculacionItems = null;
        pagingInfo.setItemCount(-1);
        if (resetFirstItem) {
            pagingInfo.setFirstItem(0);
        }
    }

    public void validateCreate(FacesContext facesContext, UIComponent component, Object value) {
        Vinculacion newVinculacion = new Vinculacion();
        String newVinculacionString = converter.getAsString(FacesContext.getCurrentInstance(), null, newVinculacion);
        String vinculacionString = converter.getAsString(FacesContext.getCurrentInstance(), null, vinculacion);
        if (!newVinculacionString.equals(vinculacionString)) {
            createSetup();
        }
    }

    public Converter getConverter() {
        return converter;
    }

}
