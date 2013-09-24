/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package jsf;

import beans.Ponencia;
import beans.TipoPonencia;
import beans.TipoPonenciaFacade;
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
public class TipoPonenciaController {

    public TipoPonenciaController() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        jpaController = (TipoPonenciaFacade) facesContext.getApplication().getELResolver().getValue(facesContext.getELContext(), null, "TipoPonenciaJpa");
        pagingInfo = new PagingInfo();
        converter = new TipoPonenciaConverter();
    }
    private TipoPonencia tipo = null;
    private List<TipoPonencia> tipoItems = null;
    private List<Ponencia> ponencias_tipo=null;
    private TipoPonenciaFacade jpaController = null;
    private TipoPonenciaConverter converter = null;
    private PagingInfo pagingInfo = null;
    private int num_ponencias;
    @Resource
    private UserTransaction utx = null;

    public PagingInfo getPagingInfo() {
        if (pagingInfo.getItemCount() == -1) {
            pagingInfo.setItemCount(jpaController.count());
        }
        return pagingInfo;
    }

    public SelectItem[] getTipoItemsAvailableSelectMany() {
        return JsfUtil.getSelectItems(jpaController.findAll(), false);
    }

    public SelectItem[] getTipoItemsAvailableSelectOne() {
        return JsfUtil.getSelectItems(jpaController.findAll(), true);
    }
public List<Ponencia> getPonenciasTipo(){
    TipoPonencia   r = (TipoPonencia) JsfUtil.getObjectFromRequestParameter("jsfcrud.currentTipoPonencia", converter, null);
    ponencias_tipo=jpaController.ponenciasTipo(r.getIdtipo());
    return ponencias_tipo;
}

public int getNum_ponencias(){
    TipoPonencia   r = (TipoPonencia) JsfUtil.getObjectFromRequestParameter("jsfcrud.currentTipoPonencia", converter, null);
    ponencias_tipo=jpaController.ponenciasTipo(r.getIdtipo());
    num_ponencias=ponencias_tipo.size();
    return num_ponencias;
}

    public TipoPonencia getTipo() {
        if (tipo == null) {
            tipo = (TipoPonencia) JsfUtil.getObjectFromRequestParameter("jsfcrud.currentTipoPonencia", converter, null);
        }
        if (tipo == null) {
            tipo = new TipoPonencia();
        }
        return tipo;
    }

    public String listSetup() {
        reset(true);
        return "tipoponencia_list";
    }

    public String createSetup() {
        reset(false);
        tipo = new TipoPonencia();
        return "tipoponencia_create";
    }

    public String create() {
        try {
            utx.begin();
        } catch (Exception ex) {
        }
        try {
            Exception transactionException = null;
            jpaController.create(tipo);
            try {
                utx.commit();
            } catch (javax.transaction.RollbackException ex) {
                transactionException = ex;
            } catch (Exception ex) {
            }
            if (transactionException == null) {
                JsfUtil.addSuccessMessage("Tipo creado con exito.");
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
        return scalarSetup("tipoponencia_detail");
    }

    public String editSetup() {
        return scalarSetup("tipoponencia_edit");
    }

    private String scalarSetup(String destination) {
        reset(false);
        tipo = (TipoPonencia) JsfUtil.getObjectFromRequestParameter("jsfcrud.currentTipoPonencia", converter, null);
        if (tipo == null) {
            String requestTipoPonenciaString = JsfUtil.getRequestParameter("jsfcrud.currentTipoPonencia");
            JsfUtil.addErrorMessage("El tipo con id " + requestTipoPonenciaString + " no existe.");
            return relatedOrListOutcome();
        }
        return destination;
    }

    public String edit() {
        String tipoString = converter.getAsString(FacesContext.getCurrentInstance(), null, tipo);
        String currentTipoPonenciaString = JsfUtil.getRequestParameter("jsfcrud.currentTipoPonencia");
        if (tipoString == null || tipoString.length() == 0 || !tipoString.equals(currentTipoPonenciaString)) {
            String outcome = editSetup();
            if ("tipoponencia_edit".equals(outcome)) {
                JsfUtil.addErrorMessage("No pudo editar el tipo, intente de nuevo.");
            }
            return outcome;
        }
        try {
            utx.begin();
        } catch (Exception ex) {
        }
        try {
            Exception transactionException = null;
            jpaController.edit(tipo);
            try {
                utx.commit();
            } catch (javax.transaction.RollbackException ex) {
                transactionException = ex;
            } catch (Exception ex) {
            }
            if (transactionException == null) {
                JsfUtil.addSuccessMessage("Tipo actualizado con exito.");
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
        return "tipoponencia_list";
    }

    public String remove() {
        String idAsString = JsfUtil.getRequestParameter("jsfcrud.currentTipoPonencia");
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
                JsfUtil.addSuccessMessage("Tipo borrado con exito.");
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

    public List<TipoPonencia> getTipoItems() {
        if (tipoItems == null) {
            getPagingInfo();
            tipoItems = jpaController.findRange(new int[]{pagingInfo.getFirstItem(), pagingInfo.getFirstItem() + pagingInfo.getBatchSize()});
        }
        return tipoItems;
    }

    public String next() {
        reset(false);
        getPagingInfo().nextPage();
        return "tipoponencia_list";
    }

    public String prev() {
        reset(false);
        getPagingInfo().previousPage();
        return "tipoponencia_list";
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
        tipo = null;
        tipoItems = null;
        pagingInfo.setItemCount(-1);
        if (resetFirstItem) {
            pagingInfo.setFirstItem(0);
        }
    }

    public void validateCreate(FacesContext facesContext, UIComponent component, Object value) {
        TipoPonencia newTipo = new TipoPonencia();
        String newTipoString = converter.getAsString(FacesContext.getCurrentInstance(), null, newTipo);
        String tipoString = converter.getAsString(FacesContext.getCurrentInstance(), null, tipo);
        if (!newTipoString.equals(tipoString)) {
            createSetup();
        }
    }

    public Converter getConverter() {
        return converter;
    }

}
