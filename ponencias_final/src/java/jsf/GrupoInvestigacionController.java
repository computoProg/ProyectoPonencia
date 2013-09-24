/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


package jsf;


import beans.GrupoInvestigacion;
import beans.GrupoInvestigacionFacade;
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
 * @author Aux de Programacion
 */
public class GrupoInvestigacionController {

    private GrupoInvestigacion grupoInvestigacion = null;
    private GrupoInvestigacion grupoInvestigacionb = null;
    private List<GrupoInvestigacion> grupoInvestigacionItems = null;
    private List<Participantes> participantes_grupoInvestigacion=null;
    private List<GrupoInvestigacion> grupoI_lista = null;
    private GrupoInvestigacionFacade jpaController = null;
    private GrupoInvestigacionConverter converter = null;
    private PagingInfo pagingInfo = null;
    private int numParticipantes;
    private int grupoI_num_filtro = 0;
    private String aux_nombre = "";
    private String aux_estado= "";
    private String aux_clasificacion = "";
    
    @Resource
    private UserTransaction utx = null;
    /** Creates a new instance of GrupoInvestigacionController */
    public GrupoInvestigacionController() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        jpaController = (GrupoInvestigacionFacade) facesContext.getApplication().getELResolver().getValue(facesContext.getELContext(), null, "GrupoInvestigacionJpa");
        pagingInfo = new PagingInfo();
        converter = new GrupoInvestigacionConverter();
    }

    public PagingInfo getPagingInfo() {
        if (pagingInfo.getItemCount() == -1) {
            pagingInfo.setItemCount(jpaController.count());
        }
        return pagingInfo;
    }

    public SelectItem[] getGrupoInvestigacionItemsAvailableSelectMany() {
        return JsfUtil.getSelectItems(jpaController.findAll(), false);
    }

    public SelectItem[] getGrupoInvestigacionItemsAvailableSelectOne() {
        return JsfUtil.getSelectItems(jpaController.findAll(), true);
    }
    public SelectItem[] getGrupoInvestigacionItemsAvailableSelectOneActivos() {
        SelectItem[] s;        
        s=JsfUtil.getSelectItems(jpaController.findAll_Except_NoActivos(), true);
        return s;
    }

    public List<Participantes> getParticipantes_grupoInvestigacion(){
        GrupoInvestigacion   gi = (GrupoInvestigacion) JsfUtil.getObjectFromRequestParameter("jsfcrud.currentGrupoInvestigacion", converter, null);
        participantes_grupoInvestigacion=jpaController.participantesGrupoInvestigacion(gi.getIdGrupoInvestigacion());
        return participantes_grupoInvestigacion;
    }

    public int getNum_participantes(){
        GrupoInvestigacion  gi = (GrupoInvestigacion) JsfUtil.getObjectFromRequestParameter("jsfcrud.currentGrupoInvestigacion", converter, null);
        participantes_grupoInvestigacion=jpaController.participantesGrupoInvestigacion(gi.getIdGrupoInvestigacion());
        numParticipantes = participantes_grupoInvestigacion.size();
        return numParticipantes;
    }

    public List<GrupoInvestigacion> getGrupoInvestigacionItems() {
        if (grupoInvestigacionItems == null) {
            getPagingInfo();
            grupoInvestigacionItems = jpaController.findRange(new int[]{pagingInfo.getFirstItem(), pagingInfo.getFirstItem() + pagingInfo.getBatchSize()});
        }
        return grupoInvestigacionItems;
    }

    public GrupoInvestigacion getGrupoInvestigacion() {
        if (grupoInvestigacion == null) {
            grupoInvestigacion = (GrupoInvestigacion) JsfUtil.getObjectFromRequestParameter("jsfcrud.currentGrupoInvestigacion", converter, null);
        }
        if (grupoInvestigacion == null) {
            grupoInvestigacion = new GrupoInvestigacion();
        }
        return grupoInvestigacion;
    }
    
    public GrupoInvestigacion getGrupoInvestigacionb() {
        if (grupoInvestigacionb == null) {
            grupoInvestigacionb = (GrupoInvestigacion) JsfUtil.getObjectFromRequestParameter("jsfcrud.currentGrupoInvestigacion", converter, null);
        }
        if (grupoInvestigacionb == null) {
            grupoInvestigacionb = new GrupoInvestigacion();
        }
        return grupoInvestigacionb;
    }
    
    
    public List<GrupoInvestigacion> getGrupoI_lista() {
        if (grupoI_lista == null) {
          getPagingInfo();
         grupoI_lista = jpaController.findRange(new int[]{pagingInfo.getFirstItem(), pagingInfo.getFirstItem() + pagingInfo.getBatchSize()});
        }
        return grupoI_lista;
    }
    
    public int getGrupoI_num_filtro(){
        return grupoI_num_filtro;
    }

    public String listSetup() {
        reset(true);
        return "grupoInvestigacion_list";
    }

    public String createSetup() {
        reset(false);
        grupoInvestigacion = new GrupoInvestigacion();
        grupoInvestigacion.setEstado("Activo");
        return "grupoInvestigacion_create";
    }

    public String create() {
        try {
            utx.begin();
        } catch (Exception ex) {
        }
        try {
            Exception transactionException = null;
            jpaController.create(grupoInvestigacion);
            try {
                utx.commit();
            } catch (javax.transaction.RollbackException ex) {
                transactionException = ex;
            } catch (Exception ex) {
            }
            if (transactionException == null) {
                JsfUtil.addSuccessMessage("Grupo de Investigación creado con exito.");
            } else {
                JsfUtil.ensureAddErrorMessage(transactionException, "Un error ha ocurrido.");
            }
        } catch (Exception e) {
            try {
                utx.rollback();
            } catch (Exception ex) {
            }
            JsfUtil.ensureAddErrorMessage(e, "Un error ha ocurrido");
            return null;
        }
        return listSetup();
    }

    public String createu() {
        try {
            utx.begin();
        } catch (Exception ex) {
        }
        try {
            Exception transactionException = null;
            jpaController.create(grupoInvestigacion);
            try {
                utx.commit();
            } catch (javax.transaction.RollbackException ex) {
                transactionException = ex;
            } catch (Exception ex) {
            }
            if (transactionException == null) {
                JsfUtil.addSuccessMessage("Grupo de Investigacion creado con exito.");
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
        return scalarSetup("grupoInvestigacion_detail");
    }

    public String editSetup() {
        return scalarSetup("grupoInvestigacion_edit");
    }

    private String scalarSetup(String destination) {
        reset(false);
        grupoInvestigacion = (GrupoInvestigacion) JsfUtil.getObjectFromRequestParameter("jsfcrud.currentGrupoInvestigacion", converter, null);
        if (grupoInvestigacion == null) {
            String requestProyectoString = JsfUtil.getRequestParameter("jsfcrud.currentProyecto");
            JsfUtil.addErrorMessage("El proyecto con id " + requestProyectoString + " no existe.");
            return relatedOrListOutcome();
        }
        return destination;
    }

    public String edit() {
        String grupoInvestigacionString = converter.getAsString(FacesContext.getCurrentInstance(), null, grupoInvestigacion);
        String currentProyectoString = JsfUtil.getRequestParameter("jsfcrud.currentGrupoInvestigacion");
        if (grupoInvestigacionString == null || grupoInvestigacionString.length() == 0 || !grupoInvestigacionString.equals(currentProyectoString)) {
            String outcome = editSetup();
            if ("grupoInvestigacion_edit".equals(outcome)) {
                JsfUtil.addErrorMessage("No se pudo editar el Grupo de Investigacion, intente de nuevo");
            }
            return outcome;
        }
        try {
            utx.begin();
        } catch (Exception ex) {
        }
        try {
            Exception transactionException = null;
            jpaController.edit(grupoInvestigacion);
            try {
                utx.commit();
            } catch (javax.transaction.RollbackException ex) {
                transactionException = ex;
            } catch (Exception ex) {
            }
            if (transactionException == null) {
                JsfUtil.addSuccessMessage("Grupo de Investigacion actualizado con exito.");
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
        return "grupoInvestigacion_list";
    }

    public String editu() {
        String grupoInvestigacionString = converter.getAsString(FacesContext.getCurrentInstance(), null, grupoInvestigacion);
        String currentProyectoString = JsfUtil.getRequestParameter("jsfcrud.currentGrupoInvestigacion");
        if (grupoInvestigacionString == null || grupoInvestigacionString.length() == 0 || !grupoInvestigacionString.equals(currentProyectoString)) {
            String outcome = editSetup();
            if ("grupoInvestigacion_edit".equals(outcome)) {
                JsfUtil.addErrorMessage("No se pudo editar el Grupo de Investigacion, intente de nuevo");
            }
            return outcome;
        }
        try {
            utx.begin();
        } catch (Exception ex) {
        }
        try {
            Exception transactionException = null;
            jpaController.edit(grupoInvestigacion);
            try {
                utx.commit();
            } catch (javax.transaction.RollbackException ex) {
                transactionException = ex;
            } catch (Exception ex) {
            }
            if (transactionException == null) {
                JsfUtil.addSuccessMessage("Grupo de Investigacion actualizado con exito.");
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
        String idAsString = JsfUtil.getRequestParameter("jsfcrud.currentGrupoInvestigacion");
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
                JsfUtil.addSuccessMessage("Grupo de Investigacion borrado con exito.");
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

    public void validateCreate(FacesContext facesContext, UIComponent component, Object value) {
        GrupoInvestigacion newGrupoInvestigacion = new GrupoInvestigacion();
        String newGrupoInvestigacionString = converter.getAsString(FacesContext.getCurrentInstance(), null, newGrupoInvestigacion);
        String grupoInvestigacionString = converter.getAsString(FacesContext.getCurrentInstance(), null, grupoInvestigacion);
        if (!newGrupoInvestigacionString.equals(grupoInvestigacionString)) {
            createSetup();
        }
    }

    public String prev() {
        reset(false);
        getPagingInfo().previousPage();
        return "grupoInvestigacion_list";
    }
    
    public String next() {
        reset(false);
        getPagingInfo().nextPage();
        return "grupoInvestigacion_list";
    }
    
    public String desvincular() {
        grupoInvestigacion = (GrupoInvestigacion) JsfUtil.getObjectFromRequestParameter("jsfcrud.currentGrupoInvestigacion", converter, null);
        grupoInvestigacion.setEstado("No_Activo");
        
            
        String grupoInvestString = converter.getAsString(FacesContext.getCurrentInstance(), null, grupoInvestigacion);
        String currentGrupoInvestString = JsfUtil.getRequestParameter("jsfcrud.currentGrupoInvestigacion");
        if (grupoInvestString == null || grupoInvestString.length() == 0 || !grupoInvestString.equals(currentGrupoInvestString)) {
            String outcome = editSetup();
            if ("grupoInvestigacion_edit".equals(outcome)) {
                JsfUtil.addErrorMessage("No se encuentra el grupo de investigación, intente nuevamente.");
            }
            return outcome;
        }
        try {
            utx.begin();
        } catch (Exception ex) {
        }
        try {
            Exception transactionException = null;
            jpaController.edit(grupoInvestigacion);

            try {
                utx.commit();
            } catch (javax.transaction.RollbackException ex) {
                transactionException = ex;
            } catch (Exception ex) {
            }
            if (transactionException == null) {
                JsfUtil.addSuccessMessage("El Grupo de Investigación se ha deshabilitado correctamente");
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
     
        return "grupoInvestigacion_list";
    }

    private void reset(boolean resetFirstItem) {
        grupoInvestigacion = null;
        grupoInvestigacionItems = null;
        grupoI_lista = null;
        pagingInfo.setItemCount(-1);
        if (resetFirstItem) {
            pagingInfo.setFirstItem(0);
        }
    }

    public Converter getConverter() {
        return converter;
    }
    
    public String busca_grupoI(){
        
        
        if(grupoInvestigacionb.getNombre() != null)
            aux_nombre =grupoInvestigacionb.getNombre();
        if(grupoInvestigacionb.getEstado() != null)
            aux_estado = grupoInvestigacionb.getEstado();
        if(grupoInvestigacionb.getClasificacion() != null)
            aux_clasificacion = grupoInvestigacionb.getClasificacion();
        
        pagingInfo.setItemCount(-1);
        getPagingInfo();
        pagingInfo.setItemCount((jpaController.grupoI_filtro(aux_nombre,aux_estado,aux_clasificacion).size()));
        int[] rango=new int[]{pagingInfo.getFirstItem(), pagingInfo.getFirstItem() + pagingInfo.getBatchSize()};

        grupoI_lista = jpaController.grupoI_filtro_rango(rango,aux_nombre,aux_estado, aux_clasificacion);
        grupoI_num_filtro=grupoI_lista.size();
        
        aux_estado = "";
        aux_nombre = "";
        aux_clasificacion = "";

        return "grupoInvestigacion_list";
    }
}
