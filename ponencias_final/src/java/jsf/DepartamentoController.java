/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package jsf;

import beans.Ciudad;
import beans.Departamento;
//import beans.DepartamentoConverter;
import beans.DepartamentoFacade;
import beans.Pais;
//import beans.PaisConverter;
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
import javax.faces.event.ValueChangeEvent;



/**
 *
 * @author Aux de Programacion
 */
public class DepartamentoController {
    private Departamento departamento = null;
    private List<Departamento> departamentoItems = null;
    private List<Ciudad> ciudades_departamento = null;
    private DepartamentoFacade jpaController = null;
    private DepartamentoConverter converter = null;
    private PaisConverter converterPais = null;
    private PagingInfo pagingInfo = null;
    private int num_ciudades;
    private boolean boolDepto = false;
    @Resource
    private UserTransaction utx = null;

    public DepartamentoController(){
        FacesContext facesContext = FacesContext.getCurrentInstance();
        jpaController = (DepartamentoFacade) facesContext.getApplication().getELResolver().getValue(facesContext.getELContext(), null, "departamentoJpa");
        pagingInfo = new PagingInfo();
        converter = new DepartamentoConverter();
        converterPais = new PaisConverter();
    }

    public PagingInfo getPagingInfo() {
        if (pagingInfo.getItemCount() == -1) {
            pagingInfo.setItemCount(jpaController.count());
        }
        return pagingInfo;
    }

    /**DUDA en el siguiente metodo (jpacontroller.estados?)*/

    public SelectItem[] getDepartamentoItemsAvailableSelectOne() {
        return JsfUtil.getSelectItems(jpaController.findAll(), true);
    }

    public SelectItem[] getDepartamentoItemsAvailableSelectMany() {
        return JsfUtil.getSelectItems(jpaController.findAll(), false);
    }
    
    public SelectItem[] getCiudadDepItemsAvailableSelectOne(){
        Departamento d = getDepartamento();
        return JsfUtil.getSelectItems(jpaController.ciudadesDepartamento(d.getIdDepartamento()), true);
    }
    
    public SelectItem[] getDepartamentoPaisItemsAvailableSelectOne(){
        return JsfUtil.getSelectItems(getDepartamentoItems(), true);
    }

    public void cambioPais(ValueChangeEvent evt){
        Pais p = (Pais)evt.getNewValue();
        departamentoItems = jpaController.departamentosPais(p.getIdPais());
        FacesContext.getCurrentInstance().renderResponse();
    }

    public int cambioPais(){
        return 2;
    }

    public Departamento getDepartamento() {
        if (departamento == null) {
            departamento = (Departamento) JsfUtil.getObjectFromRequestParameter("jsfcrud.currentDepartamento", (Converter) converter, null);
        }
        if (departamento == null) {
            departamento = new Departamento();
        }
        return departamento;
    }
    
    public boolean getBoolDepto(){
        return boolDepto;
    }
    
    public String changeBoolDepto(){
        if(boolDepto)
            boolDepto=false;
        else
            boolDepto = true;
        return "ciudad_create";
    }

    public int getNum_ciudades(){
        Departamento  d = (Departamento) JsfUtil.getObjectFromRequestParameter("jsfcrud.currentDepartamento", (Converter) converter, null);
        ciudades_departamento=jpaController.ciudadesDepartamento(d.getIdDepartamento());
        num_ciudades = ciudades_departamento.size();
        return num_ciudades;
    }

    public List<Ciudad> getCiudades_departamento(){
        Departamento   d = (Departamento) JsfUtil.getObjectFromRequestParameter("jsfcrud.currentDepartamento", (Converter) converter, null);
        ciudades_departamento=jpaController.ciudadesDepartamento(d.getIdDepartamento());
        return ciudades_departamento;
    }

    public String listSetup() {
        reset(true);
        return "ciudad_create";
    }

    public String createSetup() {
        reset(false);
        departamento = new Departamento();
        return "departamento_create";
    }

    public String create() {
        changeBoolDepto();
        try {
            utx.begin();
        } catch (Exception ex) {
        }
        try {
            Exception transactionException = null;
            jpaController.create(departamento);
            try {
                utx.commit();
            } catch (javax.transaction.RollbackException ex) {
                transactionException = ex;
            } catch (Exception ex) {
            }
            if (transactionException == null) {
                JsfUtil.addSuccessMessage("Departamento creado con exito.");
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
        return scalarSetup("departamento_detail");
    }

    public String editSetup() {
        return scalarSetup("departamento_edit");
    }

    private String scalarSetup(String destination) {
        reset(false);
        departamento = (Departamento) JsfUtil.getObjectFromRequestParameter("jsfcrud.currentDepartamento", (Converter) converter, null);
        if (departamento == null) {
            String requestCiudadString = JsfUtil.getRequestParameter("jsfcrud.currentDepartamento");
            JsfUtil.addErrorMessage("El departamento con id " + requestCiudadString + " no existe.");
            return relatedOrListOutcome();
        }
        return destination;
    }

    public String edit() {
        String departamentoString = converter.getAsString(FacesContext.getCurrentInstance(), null, departamento);
        String currentDepartamentoString = JsfUtil.getRequestParameter("jsfcrud.currentDepartamento");
        if (departamentoString == null || departamentoString.length() == 0 || !departamentoString.equals(currentDepartamentoString)) {
            String outcome = editSetup();
            if ("departamento_edit".equals(outcome)) {
                JsfUtil.addErrorMessage("No se pudo editar el departamento, intente de nuevo.");
            }
            return outcome;
        }
        try {
            utx.begin();
        } catch (Exception ex) {
        }
        try {
            Exception transactionException = null;
            jpaController.edit(departamento);
            try {
                utx.commit();
            } catch (javax.transaction.RollbackException ex) {
                transactionException = ex;
            } catch (Exception ex) {
            }
            if (transactionException == null) {
                JsfUtil.addSuccessMessage("Departamento actualizado con exito.");
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
        return "departamento_list";
    }

    public String remove() {
        String idAsString = JsfUtil.getRequestParameter("jsfcrud.currentDepartamento");
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
                JsfUtil.addSuccessMessage("Departamento borrado con exito.");
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

    public List<Departamento> getDepartamentoItems() {
        if (departamentoItems == null) {
            getPagingInfo();
            departamentoItems = jpaController.findRange(new int[]{pagingInfo.getFirstItem(), pagingInfo.getFirstItem() + pagingInfo.getBatchSize()});
        }
        return departamentoItems;
    }

    public String next() {
        reset(false);
        getPagingInfo().nextPage();
        return "departamento_list";
    }

    public String prev() {
        reset(false);
        getPagingInfo().previousPage();
        return "departamento_list";
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
        departamento = null;
        departamentoItems = null;
        pagingInfo.setItemCount(-1);
        if (resetFirstItem) {
            pagingInfo.setFirstItem(0);
        }
    }

    public void validateCreate(FacesContext facesContext, UIComponent component, Object value) {
        Departamento newDepartamento = new Departamento();
        String newDepartamentoString = converter.getAsString(FacesContext.getCurrentInstance(), null, newDepartamento);
        String departamentoString = converter.getAsString(FacesContext.getCurrentInstance(), null, departamento);
        if (!newDepartamentoString.equals(departamentoString)) {
            createSetup();
        }
    }

    public Converter getConverter() {
        return (Converter) converter;
    }
    
}