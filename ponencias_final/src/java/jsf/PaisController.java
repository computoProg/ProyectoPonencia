/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package jsf;


import beans.Departamento;
import beans.Pais;
import beans.PaisFacade;
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
 * @author Aux de Programacion
 */
public class PaisController {
    private Pais pais = null;
    private List<Pais> paisItems = null;
    private List<Departamento> departamentos_pais = null;
    private PaisFacade jpaController = null;
    private PaisConverter converter = null;
    private PagingInfo pagingInfo = null;
    private int num_departamentos;
    private boolean boolPais = false;
    @Resource
    private UserTransaction utx = null;

    public PaisController(){
        FacesContext facesContext = FacesContext.getCurrentInstance();
        jpaController = (PaisFacade) facesContext.getApplication().getELResolver().getValue(facesContext.getELContext(), null, "paisJpa");
        pagingInfo = new PagingInfo();
        converter = new PaisConverter();
    }

    public PagingInfo getPagingInfo() {
        if (pagingInfo.getItemCount() == -1) {
            pagingInfo.setItemCount(jpaController.count());
        }
        return pagingInfo;
    }

    public SelectItem[] getPaisItemsAvailableSelectOne() {
        return JsfUtil.getSelectItems(jpaController.findAll(), true);
    }

    public SelectItem[] getDepartamentoItemsAvailableSelectMany() {
        return JsfUtil.getSelectItems(jpaController.findAll(), false);
    }

    public Pais getPais() {
        if (pais == null) {
            pais = (Pais) JsfUtil.getObjectFromRequestParameter("jsfcrud.currentPais", converter, null);
        }
        if (pais == null) {
            pais = new Pais();
        }
        return pais;
    }
    
    public boolean getBoolPais(){
        return boolPais;
    }
    
    public void changeBoolPais(){
        if(boolPais)
            boolPais=false;
        else
            boolPais = true;         
    }

    public int getNum_departamentos(){
        Pais  p = (Pais) JsfUtil.getObjectFromRequestParameter("jsfcrud.currentPais", converter, null);
        departamentos_pais=jpaController.departamentosPais(p.getIdPais());
        num_departamentos = departamentos_pais.size();
        return num_departamentos;
    }

    public List<Departamento> getDepartamentos_pais(){
        Pais   p = (Pais) JsfUtil.getObjectFromRequestParameter("jsfcrud.currentPais", converter, null);
        departamentos_pais =jpaController.departamentosPais(p.getIdPais());
        return departamentos_pais;
    }

    public String listSetup() {
        reset(true);
        return "ciudad_create";
    }

    public String createSetup() {
        reset(false);
        pais = new Pais();
        return "pais_create";
    }

    public String create() {
        changeBoolPais();
        try {
            utx.begin();
        } catch (Exception ex) {
        }
        try {
            Exception transactionException = null;
            jpaController.create(pais);
            try {
                utx.commit();
            } catch (javax.transaction.RollbackException ex) {
                transactionException = ex;
            } catch (Exception ex) {
            }
            if (transactionException == null) {
                JsfUtil.addSuccessMessage("Pais creado con exito.");
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
        return listSetup();
    }

    public String detailSetup() {
        return scalarSetup("pais_detail");
    }

    public String editSetup() {
        return scalarSetup("pais_edit");
    }

    private String scalarSetup(String destination) {
        reset(false);
        pais = (Pais) JsfUtil.getObjectFromRequestParameter("jsfcrud.currentPais", converter, null);
        if (pais == null) {
            String requestCiudadString = JsfUtil.getRequestParameter("jsfcrud.currentPais");
            JsfUtil.addErrorMessage("El pais con id " + requestCiudadString + " no existe.");
            return relatedOrListOutcome();
        }
        return destination;
    }

    public String edit() {
        String paisString = converter.getAsString(FacesContext.getCurrentInstance(), null, pais);
        String currentPaisString = JsfUtil.getRequestParameter("jsfcrud.currentPais");
        if (paisString == null || paisString.length() == 0 || !paisString.equals(currentPaisString)) {
            String outcome = editSetup();
            if ("pais_edit".equals(outcome)) {
                JsfUtil.addErrorMessage("No se pudo editar el Pais, intente de nuevo.");
            }
            return outcome;
        }
        try {
            utx.begin();
        } catch (Exception ex) {
        }
        try {
            Exception transactionException = null;
            jpaController.edit(pais);
            try {
                utx.commit();
            } catch (javax.transaction.RollbackException ex) {
                transactionException = ex;
            } catch (Exception ex) {
            }
            if (transactionException == null) {
                JsfUtil.addSuccessMessage("Pais actualizado con exito.");
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
        return "pais_list";

    }

    public String remove() {
    String idAsString = JsfUtil.getRequestParameter("jsfcrud.currentPais");
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
            JsfUtil.addSuccessMessage("Pais borrado con exito.");
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
         return relatedOrListOutcome();
    }

    private String relatedOrListOutcome() {
        String relatedControllerOutcome = relatedControllerOutcome();
        if (relatedControllerOutcome != null) {
            return relatedControllerOutcome;
        }
        return listSetup();
        }

    public List<Pais> getPaisItems() {
        if (paisItems == null) {
            getPagingInfo();
            paisItems = jpaController.findRange(new int[]{pagingInfo.getFirstItem(), pagingInfo.getFirstItem() + pagingInfo.getBatchSize()});
        }
        return paisItems;
    }

    public String next() {
        reset(false);
        getPagingInfo().nextPage();
        return "pais_list";
    }

    public String prev() {
        reset(false);
        getPagingInfo().previousPage();
        return "pais_list";
    }

    private String relatedControllerOutcome() throws IllegalArgumentException {
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
        pais = null;
        paisItems = null;
        pagingInfo.setItemCount(-1);
        if (resetFirstItem) {
            pagingInfo.setFirstItem(0);
        }
    }

    public void validateCreate(FacesContext facesContext, UIComponent component, Object value) {
        Pais newPais = new Pais();
        String newPaisString = converter.getAsString(FacesContext.getCurrentInstance(), null, newPais);
        String paisString = converter.getAsString(FacesContext.getCurrentInstance(), null, pais);
        if (!newPaisString.equals(paisString)) {
            createSetup();
        }
    }

    public Converter getConverter() {
        return converter;
    }
}
