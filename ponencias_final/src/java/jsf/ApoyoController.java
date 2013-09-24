/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package jsf;

import beans.Apoyo;
import beans.ApoyoFacade;
import beans.Participantes;
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
public class ApoyoController {

    public ApoyoController() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        jpaController = (ApoyoFacade) facesContext.getApplication().getELResolver().getValue(facesContext.getELContext(), null, "apoyoJpa");
        pagingInfo = new PagingInfo();
        converter = new ApoyoConverter();
    }
    private Apoyo apoyo = null;
    private List<Apoyo> apoyoItems = null;
    private ApoyoFacade jpaController = null;
    private ApoyoConverter converter = null;
    private PagingInfo pagingInfo = null;
    private int num_participantes;
    private List<Participantes> parti_apoyo=null;
    private String aux_apoyo = "";
    private String aux_estado = "";
//    private List<Apoyo> apoyo_lista = null;
    private int apoyo_num_filtro = 0;

    @Resource
    private UserTransaction utx = null;

    public PagingInfo getPagingInfo() {
        if (pagingInfo.getItemCount() == -1) {
            pagingInfo.setItemCount(jpaController.count());
        }
        return pagingInfo;
    }

    public SelectItem[] getApoyoItemsAvailableSelectMany() {
        return JsfUtil.getSelectItems(jpaController.findAll(), false);
    }

    public SelectItem[] getApoyoItemsAvailableSelectOne() {
        return JsfUtil.getSelectItems(jpaController.findAll(), true);
    }
        
    public SelectItem[] getApoyoItemsAvailableSelectOneActivos() {
        SelectItem[] s;
        s=JsfUtil.getSelectItems(jpaController.findAll_Except_NoActivos(), true);
        return s;
    }

    public Apoyo getApoyo() {
        if (apoyo == null) {
            apoyo = (Apoyo) JsfUtil.getObjectFromRequestParameter("jsfcrud.currentApoyo", converter, null);
        }
        if (apoyo == null) {
            apoyo = new Apoyo();
        }
        return apoyo;
    }


    public String listSetup() {
        reset(true);
        return "apoyo_list";
    }

    public String createSetup() {
        reset(false);
        apoyo = new Apoyo();
        return "apoyo_create";
    }

    public String desvincular() {
        apoyo = (Apoyo) JsfUtil.getObjectFromRequestParameter("jsfcrud.currentApoyo", converter, null);
        apoyo.setEstado("No_Activo");
        
            
        String apoyoString = converter.getAsString(FacesContext.getCurrentInstance(), null, apoyo);
        String currentApoyoString = JsfUtil.getRequestParameter("jsfcrud.currentApoyo");
        if (apoyoString == null || apoyoString.length() == 0 || !apoyoString.equals(currentApoyoString)) {
            String outcome = editSetup();
            if ("apoyo_edit".equals(outcome)) {
                JsfUtil.addErrorMessage("No se encuentra el apoyo, intente nuevamente.");
            }
            return outcome;
        }
        try {
            utx.begin();
        } catch (Exception ex) {
        }
        try {
            Exception transactionException = null;
            jpaController.edit(apoyo);

            try {
                utx.commit();
            } catch (javax.transaction.RollbackException ex) {
                transactionException = ex;
            } catch (Exception ex) {
            }
            if (transactionException == null) {
                JsfUtil.addSuccessMessage("El Apoyo se ha deshabilitado correctamente");
            } else {
                JsfUtil.ensureAddErrorMessage(transactionException, "Un error ha ocurrido.");
            }
        } catch (Exception e) {
            try {
                utx.rollback();
            } catch (Exception ex) {
            }
            JsfUtil.ensureAddErrorMessage(e, "Un error ha ocurrido.");
            return null;
        }
     
        return "apoyo_list";
    }
    
    public String create() {
        try {
            utx.begin();
        } catch (Exception ex) {
        }
        try {
            Exception transactionException = null;
            jpaController.create(apoyo);
            try {
                utx.commit();
            } catch (javax.transaction.RollbackException ex) {
                transactionException = ex;
            } catch (Exception ex) {
            }
            if (transactionException == null) {
                JsfUtil.addSuccessMessage("Apoyo creado con exito.");
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
        return scalarSetup("apoyo_detail");
    }

    public String editSetup() {
        return scalarSetup("apoyo_edit");
    }

    private String scalarSetup(String destination) {
        reset(false);
        apoyo = (Apoyo) JsfUtil.getObjectFromRequestParameter("jsfcrud.currentApoyo", converter, null);
        if (apoyo == null) {
            String requestApoyoString = JsfUtil.getRequestParameter("jsfcrud.currentApoyo");
            JsfUtil.addErrorMessage("El apoyo con el id " + requestApoyoString + " no existe.");
            return relatedOrListOutcome();
        }
        return destination;
    }

    public String edit() {
        String apoyoString = converter.getAsString(FacesContext.getCurrentInstance(), null, apoyo);
        String currentApoyoString = JsfUtil.getRequestParameter("jsfcrud.currentApoyo");
        if (apoyoString == null || apoyoString.length() == 0 || !apoyoString.equals(currentApoyoString)) {
            String outcome = editSetup();
            if ("apoyo_edit".equals(outcome)) {
                JsfUtil.addErrorMessage("apoyo no se pudo editar intente de nuevo.");
            }
            return outcome;
        }
        try {
            utx.begin();
        } catch (Exception ex) {
        }
        try {
            Exception transactionException = null;
            jpaController.edit(apoyo);
            try {
                utx.commit();
            } catch (javax.transaction.RollbackException ex) {
                transactionException = ex;
            } catch (Exception ex) {
            }
            if (transactionException == null) {
                JsfUtil.addSuccessMessage("Apoyo actualizado con exito.");
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
        return "apoyo_list";
    }

    public String remove() {
        String idAsString = JsfUtil.getRequestParameter("jsfcrud.currentApoyo");
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
                JsfUtil.addSuccessMessage("Apoyo borrado con exito.");
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

    public List<Apoyo> getApoyoItems() {
        if (apoyoItems == null) {
            getPagingInfo();
            apoyoItems = jpaController.findRange(new int[]{pagingInfo.getFirstItem(), pagingInfo.getFirstItem() + pagingInfo.getBatchSize()});
        }
        return apoyoItems;
    }

    public String next() {
        reset(false);
        getPagingInfo().nextPage();
        return "apoyo_list";
    }

    public String prev() {
        reset(false);
        getPagingInfo().previousPage();
        return "apoyo_list";
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
        apoyo = null;
        apoyoItems = null;
        pagingInfo.setItemCount(-1);
        if (resetFirstItem) {
            pagingInfo.setFirstItem(0);
        }
    }

    public void validateCreate(FacesContext facesContext, UIComponent component, Object value) {
        Apoyo newApoyo = new Apoyo();
        String newApoyoString = converter.getAsString(FacesContext.getCurrentInstance(), null, newApoyo);
        String apoyoString = converter.getAsString(FacesContext.getCurrentInstance(), null, apoyo);
        if (!newApoyoString.equals(apoyoString)) {
            createSetup();
        }
    }

    public Converter getConverter() {
        return converter;
    }
    public String busca_apoyo(){        
        if(apoyo.getApoyo() != null)
            aux_apoyo = apoyo.getApoyo();
        if(!apoyo.getEstado().equals("---"))
            aux_estado = apoyo.getEstado();

        pagingInfo.setItemCount(-1);
        getPagingInfo();    
        pagingInfo.setItemCount((jpaController.apoyo_filtro(aux_apoyo,aux_estado).size()));
        int[] rango=new int[]{pagingInfo.getFirstItem(), pagingInfo.getFirstItem() + pagingInfo.getBatchSize()};
        apoyoItems=jpaController.apoyo_filtro_rango(rango, aux_apoyo ,aux_estado);
        apoyo_num_filtro=apoyoItems.size();        
        aux_estado = "";
        aux_apoyo = "";    
        return "apoyo_list";
    }
    
    public boolean validaEstado(String estado){        
        if(estado.equals("Activo"))
            return true;
        else
            return false;
    }
}
