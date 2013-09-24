/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package jsf;

import beans.Participantes;
import beans.Proyecto;
import beans.ProyectoFacade;
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
public class ProyectoController {

    public ProyectoController() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        jpaController = (ProyectoFacade) facesContext.getApplication().getELResolver().getValue(facesContext.getELContext(), null, "proyectoJpa");
        pagingInfo = new PagingInfo();
        converter = new ProyectoConverter();
        converterpart = new ParticipantesConverter();
    }
    private Proyecto proyecto = null;
    private Proyecto proyectob = null;
    private List<Proyecto> proyectoItems = null;
    private List<Proyecto> proyectos_lista = null;
    //private List<Participantes> participantes_proyecto=null;
    private ProyectoFacade jpaController = null;
    private ProyectoConverter converter = null;
    private PagingInfo pagingInfo = null;
    private int proyectos_num_filtro = 0;
    private Participantes participanteb = null;
    private ParticipantesConverter converterpart = null;
    private String aux_nombre = "";
    private String aux_estado = "";
//    private int aux_cedula = 0;
    
    private List<Proyecto> proyectos_conProrroga = null;
    @Resource
    private UserTransaction utx = null;

    public PagingInfo getPagingInfo() {
        if (pagingInfo.getItemCount() == -1) {
            pagingInfo.setItemCount(jpaController.count());
        }
        return pagingInfo;
    }

    public SelectItem[] getProyectoItemsAvailableSelectMany() {
        return JsfUtil.getSelectItems(jpaController.findAll(), false);
    }

    public SelectItem[] getProyectoItemsAvailableSelectOne() {        
        return JsfUtil.getSelectItems(jpaController.findAll(), true);        
    }
    public SelectItem[] getProyectoItemsAvailableSelectOneActivos() {
        SelectItem[] s;
        //s=JsfUtil.getSelectItems(jpaController.findAll(), true);
        s=JsfUtil.getSelectItems(jpaController.findAll_Except_NoActivos(), true);
//        System.out.println("*******************************************************************");
//        System.out.println("Vamos a ver que es lo que contiene esta "+s);
        /*s es un apuntador a la lista, no es la lista en si.*/
//        return JsfUtil.getSelectItems(jpaController.findAll(), true);
        return s;
    }
  
    
    /*public List<Participantes> getParticipantes_proyecto(){
        Proyecto   e = (Proyecto) JsfUtil.getObjectFromRequestParameter("jsfcrud.currentProyecto", converter, null);
        participantes_proyecto=jpaController.participantesProyecto(e.getIdproyecto());
        return participantes_proyecto;
    }*/
    
    public List<Proyecto> getProyectos_lista() {
        if (proyectos_lista == null) {
          getPagingInfo();
          proyectos_lista = jpaController.findRange(new int[]{pagingInfo.getFirstItem(), pagingInfo.getFirstItem() + pagingInfo.getBatchSize()});
        }
        return proyectos_lista;
    }
    
    
    public int getProyectos_num_filtro(){
        return proyectos_num_filtro;
    }
    

    public String busca_proyecto(){
        
        /*if(participanteb.getCedula() != null)
            aux_cedula = participanteb.getCedula();*/
        if(proyectob.getNombre() != null)
            aux_nombre = proyectob.getNombre();
        if(!proyectob.getEstado().equals("---"))
            aux_estado = proyectob.getEstado();
        
        pagingInfo.setItemCount(-1);
        getPagingInfo();
//        pagingInfo.setItemCount((jpaController.proyecto_filtro(aux_cedula,aux_nombre,aux_estado).size()));
        pagingInfo.setItemCount((jpaController.proyecto_filtro(aux_nombre,aux_estado).size()));
        int[] rango=new int[]{pagingInfo.getFirstItem(), pagingInfo.getFirstItem() + pagingInfo.getBatchSize()};
        
//        proyectos_lista=jpaController.proyecto_filtro_rango(rango,aux_cedula,aux_nombre,aux_estado);
        proyectos_lista=jpaController.proyecto_filtro_rango(rango,aux_nombre,aux_estado);
        proyectos_num_filtro=proyectos_lista.size();        
        aux_estado = "";
        aux_nombre = "";
//        aux_cedula = 0;

        return "proyecto_list";
    }


 
 public Participantes getParticipanteb(){
     if(participanteb == null){
            participanteb = (Participantes)JsfUtil.getObjectFromRequestParameter("jsfcrud.currentParticipantes", converterpart, null);
        }
        if(participanteb == null){
            participanteb = new Participantes();
        }
        
        return participanteb;
 }
 

 

    public Proyecto getProyecto() {
        if (proyecto == null) {
            proyecto = (Proyecto) JsfUtil.getObjectFromRequestParameter("jsfcrud.currentProyecto", converter, null);
        }
        if (proyecto == null) {
            proyecto = new Proyecto();
        }
        return proyecto;
    }
    
    public Proyecto getProyectob() {
        if (proyectob == null) {
            proyecto = (Proyecto) JsfUtil.getObjectFromRequestParameter("jsfcrud.currentProyecto", converter, null);
        }
        if (proyectob == null) {
            proyectob = new Proyecto();
        }
        return proyectob;
    }

    public String listSetup() {
        reset(true);
        return "proyecto_list";
    }

    public String listProrroga() {
        reset(true);
        return "proyecto_prorroga";
    }
    public String createSetup() {
        reset(false);
        proyecto = new Proyecto();
        return "proyecto_create";
    }

    public String create() {
        try {
            utx.begin();
        } catch (Exception ex) {
        }
        try {
            Exception transactionException = null;
            jpaController.create(proyecto);
            try {
                utx.commit();
            } catch (javax.transaction.RollbackException ex) {
                transactionException = ex;
            } catch (Exception ex) {
            }
            if (transactionException == null) {
                JsfUtil.addSuccessMessage("Proyecto creado con exito.");
            } else {
                JsfUtil.ensureAddErrorMessage(transactionException, "Un error ha ocurrido.");
            }
        } catch (Exception e) {
            try {
                utx.rollback();
            } catch (Exception ex) {
            }
            JsfUtil.ensureAddErrorMessage(e, "un error ha ocurrido");
            return null;
        }
        return "proyecto_list";
    }
     public String createu() {
        try {
            utx.begin();
        } catch (Exception ex) {
        }
        try {
            Exception transactionException = null;
            jpaController.create(proyecto);
            try {
                utx.commit();
            } catch (javax.transaction.RollbackException ex) {
                transactionException = ex;
            } catch (Exception ex) {
            }
            if (transactionException == null) {
                JsfUtil.addSuccessMessage("Proyecto creado con exito.");
            } else {
                JsfUtil.ensureAddErrorMessage(transactionException, "Un error ha ocurrido.");
            }
        } catch (Exception e) {
            try {
                utx.rollback();
            } catch (Exception ex) {
            }
            JsfUtil.ensureAddErrorMessage(e, "un error ha ocurrido");
            return null;
        }
        return "welcome_user";
    }

    public String detailSetup() {
        return scalarSetup("proyecto_detail");
    }

    public String editSetup() {
        return scalarSetup("proyecto_edit");
    }

    private String scalarSetup(String destination) {
        reset(false);
        proyecto = (Proyecto) JsfUtil.getObjectFromRequestParameter("jsfcrud.currentProyecto", converter, null);
        if (proyecto == null) {
            String requestProyectoString = JsfUtil.getRequestParameter("jsfcrud.currentProyecto");
            JsfUtil.addErrorMessage("El proyecto con id " + requestProyectoString + " no existe.");
            return relatedOrListOutcome();
        }
        return destination;
    }
   
    public String desvincular() {
        proyecto = (Proyecto) JsfUtil.getObjectFromRequestParameter("jsfcrud.currentProyecto", converter, null);
        proyecto.setEstado("No_Activo");
        
            
        String proyectoString = converter.getAsString(FacesContext.getCurrentInstance(), null, proyecto);
        String currentProyectoString = JsfUtil.getRequestParameter("jsfcrud.currentProyecto");
        if (proyectoString == null || proyectoString.length() == 0 || !proyectoString.equals(currentProyectoString)) {
            String outcome = editSetup();
            if ("proyecto_edit".equals(outcome)) {
                JsfUtil.addErrorMessage("No se encuentra el proyecto, intente nuevamente.");
            }
            return outcome;
        }
        try {
            utx.begin();
        } catch (Exception ex) {
        }
        try {
            Exception transactionException = null;
            jpaController.edit(proyecto);

            try {
                utx.commit();
            } catch (javax.transaction.RollbackException ex) {
                transactionException = ex;
            } catch (Exception ex) {
            }
            if (transactionException == null) {
                JsfUtil.addSuccessMessage("El proyecto se ha deshabilitado correctamente");
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
     
        return "proyecto_list";
    }

    public String edit() {
        String proyectoString = converter.getAsString(FacesContext.getCurrentInstance(), null, proyecto);
        String currentProyectoString = JsfUtil.getRequestParameter("jsfcrud.currentProyecto");
        if (proyectoString == null || proyectoString.length() == 0 || !proyectoString.equals(currentProyectoString)) {
            String outcome = editSetup();
            if ("proyecto_edit".equals(outcome)) {
                JsfUtil.addErrorMessage("No se pudo editar proyecto, intente de nuevo");
            }
            return outcome;
        }
        try {
            utx.begin();
        } catch (Exception ex) {
        }
        try {
            Exception transactionException = null;
            jpaController.edit(proyecto);
            try {
                utx.commit();
            } catch (javax.transaction.RollbackException ex) {
                transactionException = ex;
            } catch (Exception ex) {
            }
            if (transactionException == null) {
                JsfUtil.addSuccessMessage("Proyecto actualizado con exito.");
            } else {
                JsfUtil.ensureAddErrorMessage(transactionException, "un error ha ocurrido.");
            }
        } catch (Exception e) {
            try {
                utx.rollback();
            } catch (Exception ex) {
            }
            JsfUtil.ensureAddErrorMessage(e, "Un error ha ocurrido.");
            return null;
        }
        return "proyecto_list";
    }
 public String editu() {
        String proyectoString = converter.getAsString(FacesContext.getCurrentInstance(), null, proyecto);
        String currentProyectoString = JsfUtil.getRequestParameter("jsfcrud.currentProyecto");
        if (proyectoString == null || proyectoString.length() == 0 || !proyectoString.equals(currentProyectoString)) {
            String outcome = editSetup();
            if ("proyecto_edit".equals(outcome)) {
                JsfUtil.addErrorMessage("No se pudo editar proyecto, intente de nuevo");
            }
            return outcome;
        }
        try {
            utx.begin();
        } catch (Exception ex) {
        }
        try {
            Exception transactionException = null;
            jpaController.edit(proyecto);
            try {
                utx.commit();
            } catch (javax.transaction.RollbackException ex) {
                transactionException = ex;
            } catch (Exception ex) {
            }
            if (transactionException == null) {
                JsfUtil.addSuccessMessage("Proyecto actualizado con exito.");
            } else {
                JsfUtil.ensureAddErrorMessage(transactionException, "un error ha ocurrido.");
            }
        } catch (Exception e) {
            try {
                utx.rollback();
            } catch (Exception ex) {
            }
            JsfUtil.ensureAddErrorMessage(e, "Un error ha ocurrido.");
            return null;
        }

        return "participante_detail";
    }

    public String remove() {
        String idAsString = JsfUtil.getRequestParameter("jsfcrud.currentProyecto");
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
                JsfUtil.addSuccessMessage("Proyecto borrado con exito.");
            } else {
                JsfUtil.ensureAddErrorMessage(transactionException, "ha ocurrido un error");
            }
        } catch (Exception e) {
            try {
                utx.rollback();
            } catch (Exception ex) {
            }
            JsfUtil.ensureAddErrorMessage(e, "ha ocurrido un error");
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

    public List<Proyecto> getProyectoItems() {
        if (proyectoItems == null) {
            getPagingInfo();
            proyectoItems = jpaController.findRange(new int[]{pagingInfo.getFirstItem(), pagingInfo.getFirstItem() + pagingInfo.getBatchSize()});
        }
        return proyectoItems;
    }


    public String next() {
        reset(false);
        getPagingInfo().nextPage();
        return "proyecto_list";
    }

    public String prev() {
        reset(false);
        getPagingInfo().previousPage();
        return "proyecto_list";
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
        proyecto = null;
        proyectoItems = null;
        proyectos_lista = null;
        pagingInfo.setItemCount(-1);
        if (resetFirstItem) {
            pagingInfo.setFirstItem(0);
        }
    }

    public void validateCreate(FacesContext facesContext, UIComponent component, Object value) {
        Proyecto newProyecto = new Proyecto();
        String newProyectoString = converter.getAsString(FacesContext.getCurrentInstance(), null, newProyecto);
        String proyectoString = converter.getAsString(FacesContext.getCurrentInstance(), null, proyecto);
        if (!newProyectoString.equals(proyectoString)) {
            createSetup();
        }
    }

    public Converter getConverter() {
        return converter;
    }
    
    public boolean validaEstado(String estado){        
        if(estado.equals("Activo"))
            return true;
        else
            return false;
    }

}
