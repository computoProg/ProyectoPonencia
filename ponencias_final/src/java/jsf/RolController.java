/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package jsf;

import beans.Participantes;
import beans.Rol;
import beans.RolFacade;
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
public class RolController {

    public RolController() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        jpaController = (RolFacade) facesContext.getApplication().getELResolver().getValue(facesContext.getELContext(), null, "rolJpa");
        pagingInfo = new PagingInfo();
        converter = new RolConverter();
    }
    private Rol rol = null;
    private List<Rol> rolItems = null;
    private List<Participantes> participantes_rol=null;
    private RolFacade jpaController = null;
    private RolConverter converter = null;
    private PagingInfo pagingInfo = null;
    private int num_participantes;
    @Resource
    private UserTransaction utx = null;

    public PagingInfo getPagingInfo() {
        if (pagingInfo.getItemCount() == -1) {
            pagingInfo.setItemCount(jpaController.count());
        }
        return pagingInfo;
    }

    public SelectItem[] getRolItemsAgetSelectItemsvailableSelectMany() {
        return JsfUtil.getSelectItems(jpaController.findAll(), false);
    }

    public SelectItem[] getRolItemsAvailableSelectOne() {
        return JsfUtil.getSelectItems(jpaController.rolesUsuario(), true);
    }

    public SelectItem[] getRolItemsAvailableSelectOneUser() {
        return JsfUtil.getSelectItems(jpaController.rolesUsuario(), true);
    }

    public SelectItem[] getRolItemsAvailableSelectOneAdmin() {
        return JsfUtil.getSelectItems(jpaController.findAll(), true);
    }

    public Rol getRol() {
        if (rol == null) {
            rol = (Rol) JsfUtil.getObjectFromRequestParameter("jsfcrud.currentRol", converter, null);
        }
        if (rol == null) {
            rol = new Rol();
        }
        return rol;
    }

    public String listSetup() {
        reset(true);
        return "rol_list";
    }

    public String createSetup() {
        reset(false);
        rol = new Rol();
        return "rol_create";
    }

    public String create() {
        try {
            utx.begin();
        } catch (Exception ex) {
        }
        try {
            Exception transactionException = null;
            jpaController.create(rol);
            try {
                utx.commit();
            } catch (javax.transaction.RollbackException ex) {
                transactionException = ex;
            } catch (Exception ex) {
            }
            if (transactionException == null) {
                JsfUtil.addSuccessMessage("Rol creado con exito.");
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
        return scalarSetup("rol_detail");
    }

    public String editSetup() {
        return scalarSetup("rol_edit");
    }

    private String scalarSetup(String destination) {
        reset(false);
        rol = (Rol) JsfUtil.getObjectFromRequestParameter("jsfcrud.currentRol", converter, null);
        if (rol == null) {
            String requestRolString = JsfUtil.getRequestParameter("jsfcrud.currentRol");
            JsfUtil.addErrorMessage("El rol con id " + requestRolString + " no existe.");
            return relatedOrListOutcome();
        }
        return destination;
    }

    public String edit() {
        String rolString = converter.getAsString(FacesContext.getCurrentInstance(), null, rol);
        String currentRolString = JsfUtil.getRequestParameter("jsfcrud.currentRol");
        if (rolString == null || rolString.length() == 0 || !rolString.equals(currentRolString)) {
            String outcome = editSetup();
            if ("rol_edit".equals(outcome)) {
                JsfUtil.addErrorMessage("No se pudo editar el rol, intente de nuevo.");
            }
            return outcome;
        }
        try {
            utx.begin();
        } catch (Exception ex) {
        }
        try {
            Exception transactionException = null;
            jpaController.edit(rol);
            try {
                utx.commit();
            } catch (javax.transaction.RollbackException ex) {
                transactionException = ex;
            } catch (Exception ex) {
            }
            if (transactionException == null) {
                JsfUtil.addSuccessMessage("Rol actualizado con exito.");
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
        return "rol_list";
    }

    public String remove() {
        String idAsString = JsfUtil.getRequestParameter("jsfcrud.currentRol");
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
                JsfUtil.addSuccessMessage("Rol borrado con exito.");
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
public List<Participantes> getParticipantesRol(){
             Rol   r = (Rol) JsfUtil.getObjectFromRequestParameter("jsfcrud.currentRol", converter, null);
             participantes_rol=jpaController.participantesRol(r.getIdrol());
             return participantes_rol;
}
public int getNum_participantes(){
     Rol   r = (Rol) JsfUtil.getObjectFromRequestParameter("jsfcrud.currentRol", converter, null);
             participantes_rol=jpaController.participantesRol(r.getIdrol());
             num_participantes=participantes_rol.size();
             return num_participantes;
}
    public List<Rol> getRolItems() {
        if (rolItems == null) {
            getPagingInfo();
            rolItems = jpaController.findRange(new int[]{pagingInfo.getFirstItem(), pagingInfo.getFirstItem() + pagingInfo.getBatchSize()});
        }
        return rolItems;
    }

    public String next() {
        reset(false);
        getPagingInfo().nextPage();
        return "rol_list";
    }

    public String prev() {
        reset(false);
        getPagingInfo().previousPage();
        return "rol_list";
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
        rol = null;
        rolItems = null;
        pagingInfo.setItemCount(-1);
        if (resetFirstItem) {
            pagingInfo.setFirstItem(0);
        }
    }

    public void validateCreate(FacesContext facesContext, UIComponent component, Object value) {
        Rol newRol = new Rol();
        String newRolString = converter.getAsString(FacesContext.getCurrentInstance(), null, newRol);
        String rolString = converter.getAsString(FacesContext.getCurrentInstance(), null, rol);
        if (!newRolString.equals(rolString)) {
            createSetup();
        }
    }

    public Converter getConverter() {
        return converter;
    }

}
