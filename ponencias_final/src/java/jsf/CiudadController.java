/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package jsf;

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
//import javax.faces.event.ValueChangeEvent;
import beans.Ciudad;
import beans.CiudadFacade;



/**
 *
 * @author Aux de Programacion
 */
public class CiudadController {
    private Ciudad ciudad = null;
    private List<Ciudad> ciudadItems = null;
    private CiudadFacade jpaController = null;
    private CiudadConverter converter = null;
    private PagingInfo pagingInfo = null;
        
    //private List<Ciudad> ciudad_lista_ordered = null;
    
 
    @Resource
    private UserTransaction utx = null;

    public CiudadController(){
        FacesContext facesContext = FacesContext.getCurrentInstance();
        jpaController = (CiudadFacade) facesContext.getApplication().getELResolver().getValue(facesContext.getELContext(), null, "ciudadJpa");
        pagingInfo = new PagingInfo();
        converter = new CiudadConverter();
    }

    public PagingInfo getPagingInfo() {
        if (pagingInfo.getItemCount() == -1) {
            pagingInfo.setItemCount(jpaController.count());
        }
        return pagingInfo;
    }

//    El siguiente método es para obtener las ciudades en orden alfabetico, verificar
//    bien el en la clase que lo voy a implementar
//    El método funciona bien, pero no me sirve, porque la implementación del crud es diferente y cuando 
//    y cuando voy a guardar el dato seleccionado me saca un error
    public SelectItem[] getCiudadItemsAvailableSelectOneOrdered(){
        SelectItem[] s;
        s=JsfUtil.getSelectItems(jpaController.getCiudadesOrdered(), true);
        return s;
    }
    
    /**DUDA en el siguiente metodo (jpacontroller.estados?)*/

    public SelectItem[] getCiudadItemsAvailableSelectOne() {        
        return JsfUtil.getSelectItems(jpaController.findAll(), true);
    }

    public SelectItem[] getCiudadItemsAvailableSelectMany() {
        return JsfUtil.getSelectItems(jpaController.findAll(), false);
    }

    

    public Ciudad getCiudad() {
        if (ciudad == null) {
            ciudad = (Ciudad) JsfUtil.getObjectFromRequestParameter("jsfcrud.currentCiudad", converter, null);
        }
        if (ciudad == null) {
            ciudad = new Ciudad();
        }
        return ciudad;
    }


    public String listSetup() {
        reset(true);
        return "ciudad_list";
    }

    public String createSetup() {
        reset(false);
        ciudad = new Ciudad();
        return "ciudad_create";
    }

    public String create() {
        try {
            utx.begin();
        } catch (Exception ex) {
        }
        try {
            Exception transactionException = null;
            jpaController.create(ciudad);
            try {
                utx.commit();
            } catch (javax.transaction.RollbackException ex) {
                transactionException = ex;
            } catch (Exception ex) {
            }
            if (transactionException == null) {
                JsfUtil.addSuccessMessage("Ciudad creado con exito.");
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
        return scalarSetup("ciudad_detail");
    }

    public String editSetup() {
        return scalarSetup("ciudad_edit");
    }

    private String scalarSetup(String destination) {
        reset(false);
        ciudad = (Ciudad) JsfUtil.getObjectFromRequestParameter("jsfcrud.currentCiudad", converter, null);
        if (ciudad == null) {
            String requestCiudadString = JsfUtil.getRequestParameter("jsfcrud.currentCiudad");
            JsfUtil.addErrorMessage("El ciudad con id " + requestCiudadString + " no existe.");
            return relatedOrListOutcome();
        }
        return destination;
    }

    public String edit() {
        String ciudadString = converter.getAsString(FacesContext.getCurrentInstance(), null, ciudad);
        String currentCiudadString = JsfUtil.getRequestParameter("jsfcrud.currentCiudad");
        if (ciudadString == null || ciudadString.length() == 0 || !ciudadString.equals(currentCiudadString)) {
            String outcome = editSetup();
            if ("ciudad_edit".equals(outcome)) {
                JsfUtil.addErrorMessage("No se pudo editar la ciudad, intente de nuevo.");
            }
            return outcome;
        }
        try {
            utx.begin();
        } catch (Exception ex) {
        }
        try {
            Exception transactionException = null;
            jpaController.edit(ciudad);
            try {
                utx.commit();
            } catch (javax.transaction.RollbackException ex) {
                transactionException = ex;
            } catch (Exception ex) {
            }
            if (transactionException == null) {
                JsfUtil.addSuccessMessage("Ciudad actualizado con exito.");
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
        return "ciudad_list";
    }

    public String remove() {//debo hacer una validación. si hay alguna ponencia asociada con esa ciudad
        //no puedo borrar la ciudad.
        String idAsString = JsfUtil.getRequestParameter("jsfcrud.currentCiudad");
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
                JsfUtil.addSuccessMessage("Ciudad borrado con exito.");
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

    public List<Ciudad> getCiudadItems() {
        if (ciudadItems == null) {
            getPagingInfo();
            ciudadItems = jpaController.findRange(new int[]{pagingInfo.getFirstItem(), pagingInfo.getFirstItem() + pagingInfo.getBatchSize()});
        }
        return ciudadItems;
    }

    public String next() {
        reset(false);
        getPagingInfo().nextPage();
        return "ciudad_list";
    }

    public String prev() {
        reset(false);
        getPagingInfo().previousPage();
        return "ciudad_list";
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
        ciudad = null;
        ciudadItems = null;
        pagingInfo.setItemCount(-1);
        if (resetFirstItem) {
            pagingInfo.setFirstItem(0);
        }
    }

    public void validateCreate(FacesContext facesContext, UIComponent component, Object value) {
        Ciudad newCiudad = new Ciudad();
        String newCiudadString = converter.getAsString(FacesContext.getCurrentInstance(), null, newCiudad);
        String ciudadString = converter.getAsString(FacesContext.getCurrentInstance(), null, ciudad);
        if (!newCiudadString.equals(ciudadString)) {
            createSetup();
        }
    }

    public Converter getConverter() {
        return converter;
    }
}