/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package jsf;


import beans.Participantes;
//import beans.ParticipantesFacade;
import beans.Ponencia;
import beans.PonenciaFacade;
import beans.util.PagingInfo;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import javax.faces.FacesException;
import javax.annotation.Resource;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.transaction.UserTransaction;
import beans.util.JsfUtil;
import java.text.ParseException;
import java.util.List;
import javax.faces.component.UIComponent;
import javax.faces.convert.Converter;
//import javax.faces.event.ValueChangeEvent;

/**
 *
 * @author Aux de Programacion
 */
public class PonenciaController {
    private Ponencia ponencia =null;
    private Participantes participante = null; // Creo que no se esta usando, mirar bien para quitarlo
    //private Participantes p = null;
    private List<Ponencia> ponencias = null;
    private List<Ponencia> ponencias_fecha = null;
    private List<Ponencia> ponenciaItems = null;
    private PonenciaFacade jpaController = null;
    //private ParticipantesFacade jpaControllerPart = null;
    private PonenciaConverter converter = null;
    private ParticipantesConverter converterpart = null;
    private PagingInfo pagingInfo = null;
    //private ParticipantesController participan=null;
    
    private Ponencia ponenciab = null;
    private Ponencia ponenciab2 = null;

    private int aux_apoyo=0;    
    private int aux_tipo=0;
    private int aux_proyecto=0;
    private int aux_participante=0;
    private int aux_nextprevponencia=0;

    private int aux_ciudad=0;

    private int itemcount=0;
    
    
    private List<Ponencia> ponencias_lista = null;    


    @Resource
    private UserTransaction utx = null;

    public PonenciaController() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        jpaController = (PonenciaFacade) facesContext.getApplication().getELResolver().getValue(facesContext.getELContext(), null, "ponenciaJpa");
        //jpaControllerPart = (ParticipantesFacade) facesContext.getApplication().getELResolver().getValue(facesContext.getELContext(), null, "participantesJpa");
        //controllerParticipante = (ParticipantesFacade) facesContext.getApplication().getELResolver().getValue(facesContext.getELContext(), null, "participantesJpa");
        //participan = new ParticipantesController();
        pagingInfo = new PagingInfo();
        converter = new PonenciaConverter();
        converterpart = new ParticipantesConverter();
    }

    public PagingInfo getPagingInfo() {
        if (pagingInfo.getItemCount() == -1) {
            itemcount=jpaController.count();
            pagingInfo.setItemCount(itemcount);            
        }
        return pagingInfo;
    }

    public SelectItem[] getPonenciaItemsAvailableSelectMany() {
        return JsfUtil.getSelectItems(jpaController.findAll(), false);
    }

    public SelectItem[] getPonenciaItemsAvailableSelectOne() {
        return JsfUtil.getSelectItems(jpaController.findAll(), true);
    }

    public Ponencia getPonencia() {
        if (ponencia == null) {            
            ponencia = (Ponencia) JsfUtil.getObjectFromRequestParameter("jsfcrud.currentPonencia", converter, null);
        }        
        if (ponencia == null) {
            ponencia = new Ponencia();
        }
        return ponencia;
    }
    /*public Ponencia getPonencia1(int ceduactual) {
        
        if (ponencia == null) {
            //ponencia.setParticipantesCedula(ceduactual);
            ponencia = (Ponencia) JsfUtil.getObjectFromRequestParameter("jsfcrud.currentPonencia", converter, null);
        }        
        if (ponencia == null) {
            ponencia = new Ponencia();
        }
        return ponencia;
    }*/
    
    public Participantes getParticipante(){
        if(participante == null){
            participante = (Participantes)JsfUtil.getObjectFromRequestParameter("jsfcrud.currentParticipantes", converterpart, null);
        }
        if(participante == null){
            participante = new Participantes();
        }
        
        return participante;
    }
        
    public Ponencia getPonenciab() {//Este método no esta haciendo nada, ponenciab siempre es null
        
        if (ponenciab == null) {
            ponenciab = (Ponencia) JsfUtil.getObjectFromRequestParameter("jsfcrud.currentPonencia", converter, null);            
            //paisb= (Pais) JsfUtil.getObjectFromRequestParameter("jsfcrud.currentPais", converter, null);
            //paisb.getIdPais();
        }
        if (ponenciab == null) {
            ponenciab = new Ponencia();
            //paisb = new Pais();
        }           
        return ponenciab;
        
    }
    
    public Ponencia getPonenciab2() {
        if (ponenciab2 == null) {
            ponenciab2 = (Ponencia) JsfUtil.getObjectFromRequestParameter("jsfcrud.currentPonencia", converter, null);
        }
        if (ponenciab2 == null) {
            ponenciab2 = new Ponencia();
        }
        return ponenciab2;
    }    
    public List<Ponencia> getPonencias_lista() {               
        if (ponencias_lista == null && aux_nextprevponencia == 0) {            
            getPagingInfo();
            ponencias_lista = jpaController.findRange(new int[]{pagingInfo.getFirstItem(), pagingInfo.getFirstItem() 
                    + pagingInfo.getBatchSize()});
        }        
        if(aux_nextprevponencia==1){
            //System.out.println(" valorboolean dentro if: "+aux_nextprevponencia);
          aux_nextprevponencia=0;
          getPagingInfo();
          int[] rango=new int[]{pagingInfo.getFirstItem(), pagingInfo.getFirstItem() 
                            + pagingInfo.getBatchSize()};          
          ponencias_lista=jpaController.lista_next_prev(rango);
        }
        return ponencias_lista;
    }
    
    /*public SelectItem[] getCiudadItemsAvailableSelectOne() {//Esta consulta no la necesito, quitar dependencias y borrarla        
        return JsfUtil.getSelectItems(jpaController.Ciudades(), true);                                            
    }*/
      
    public String buscaPonencia() {
        ponencias =jpaController.busca_ponencia(ponencia.getNombre()+"%");
        return "ponencia_list";
    }
                   
    /*
     * NOTA: EL SIGUIENTE METODO DEBE FUNCIONAR CON AUX_PARTICIPANTE, LOS METODOS YA ESTAN DEFINIDOS
     * EN PONENCIACONVERTER PERO HAY QUE CAPTURAR EL PARTICIPANTE ACTUAL PARA PARA PODER TOMAR LA CEDULA
     * Y HACER LA CONSULTA
     */
    public String busca_ponencia(int rol, int cedula){
        //reset(true);
        aux_participante=cedula;                    
            if(ponenciab.getParticipantesCedula() != null){/*esta opción es verdadera, solo si la cédula del
             participante ingresado esta en la bd*/ /*este if es solo para los administradores*/                
                aux_participante = ponenciab.getParticipantesCedula().getCedula();        
                rol=2;
            }
            
            if(ponenciab.getCiudadidCiudad()!=null){               
                aux_ciudad = ponenciab.getCiudadidCiudad().getIdCiudad();                
            }
            
            if(ponenciab.getTipoIdtipo()!=null)
                aux_tipo = ponenciab.getTipoIdtipo().getIdtipo();

            if(ponenciab.getApoyoIdapoyo()!=null)
                aux_apoyo = ponenciab.getApoyoIdapoyo().getIdapoyo();

            if(ponenciab.getProyectoIdproyecto()!=null)
                aux_proyecto=ponenciab.getProyectoIdproyecto().getIdproyecto();
            
            /***************************************************************/            
            //hace el filtro bien, pero no muestra las ponencias
            //solucionado agregando (or :participante = 0), en la busqueda, ya que le hacia falta
            if(ponenciab.getFechaInicial()!=null && ponenciab2.getFechaInicial() !=  null){            
                if(ponenciab2.getFechaInicial().before(ponenciab.getFechaInicial())
                        && !(ponenciab2.getFechaInicial().equals(ponenciab.getFechaInicial()))){
                    JsfUtil.addErrorMessage("La fecha final no puede ser en un periodo anterior a la fecha inicial");
                }
                java.util.Date utlDate = ponenciab.getFechaInicial();
                java.util.Date utlDate2 = ponenciab2.getFechaInicial();

                java.sql.Date sqlDate = new java.sql.Date(utlDate.getTime());
                java.sql.Date sqlDate2 = new java.sql.Date(utlDate2.getTime());

                pagingInfo.setItemCount(-1);
                getPagingInfo();
                itemcount=jpaController.ponencias_fecha1_fecha2(null,sqlDate
                        , sqlDate2,aux_participante, aux_ciudad, aux_tipo, aux_apoyo
                        , aux_proyecto,rol).size();
                pagingInfo.setItemCount(itemcount);                         

                /*el rango es para mostrar solo las 10 primeras ponencias*/           
                int[] rango=new int[]{pagingInfo.getFirstItem(), pagingInfo.getFirstItem() 
                        + pagingInfo.getBatchSize()};
                ponencias_lista = jpaController.ponencias_fecha1_fecha2(rango,sqlDate, sqlDate2,aux_participante
                        , aux_ciudad, aux_tipo, aux_apoyo, aux_proyecto,rol);
            }

             /***************************************************************/
            
            if(ponenciab.getFechaInicial()==null && ponenciab2.getFechaInicial() == null){                
                pagingInfo.setItemCount(-1);
                getPagingInfo();  
                itemcount=(jpaController.ponencia_filtro_rango(null,aux_participante,aux_ciudad,aux_tipo
                        ,aux_apoyo,aux_proyecto,rol).size());
                pagingInfo.setItemCount(itemcount);

                /*el rango es para mostrar solo las 10 primeras ponencias*/
                 int[] rango=new int[]{pagingInfo.getFirstItem(), pagingInfo.getFirstItem() 
                         + pagingInfo.getBatchSize()};                 
                ponencias_lista=jpaController.ponencia_filtro_rango(rango,aux_participante,aux_ciudad,aux_tipo
                        ,aux_apoyo,aux_proyecto,rol);            
    //            ponencias_num_filtro=ponencias_lista.size();// no sé para que es esta linea, no hace nada
            }

            /***************************************************************/
            //muestra todas las ponencias que hay, apartir de la fecha seleccionada en adelante
            if(ponenciab.getFechaInicial()!=null && ponenciab2.getFechaInicial() == null){            
                    java.util.Date utlDate = ponenciab.getFechaInicial();
                    java.sql.Date sqlDate = new java.sql.Date(utlDate.getTime());
                    pagingInfo.setItemCount(-1);
                    getPagingInfo();
                    itemcount=jpaController.ponencias_fecha1(null,sqlDate,aux_participante
                            , aux_ciudad, aux_tipo, aux_apoyo, aux_proyecto,rol).size();
                    pagingInfo.setItemCount(itemcount);
                    int[] rango=new int[]{pagingInfo.getFirstItem(), pagingInfo.getFirstItem() 
                            + pagingInfo.getBatchSize()};
                    ponencias_lista = jpaController.ponencias_fecha1(rango,sqlDate, aux_participante, aux_ciudad
                            , aux_tipo, aux_apoyo, aux_proyecto,rol);
            }
            /***************************************************************/
            //muestra todas las ponencias anteriores que hay, apartir de la fecha seleccionada
            if(ponenciab.getFechaInicial()==null && ponenciab2.getFechaInicial() != null){

                    java.util.Date utlDate2 = ponenciab2.getFechaInicial();
                    java.sql.Date sqlDate2 = new java.sql.Date(utlDate2.getTime());
                    pagingInfo.setItemCount(-1);
                    getPagingInfo();
                    itemcount=jpaController.ponencias_fecha2(null,sqlDate2,aux_participante
                            , aux_ciudad, aux_tipo, aux_apoyo, aux_proyecto,rol).size();
                    pagingInfo.setItemCount(itemcount);
                    int[] rango=new int[]{pagingInfo.getFirstItem(), pagingInfo.getFirstItem() 
                            + pagingInfo.getBatchSize()};
                    ponencias_lista = jpaController.ponencias_fecha2(rango, sqlDate2,aux_participante, aux_ciudad
                            , aux_tipo, aux_apoyo, aux_proyecto,rol);
            }
            aux_ciudad = 0;
            aux_apoyo=0;
            aux_proyecto = 0;
            aux_tipo = 0;
            aux_participante=0;
            return "ponencia_list";          
    }
       
    public String listSetup(int rol, int cedula) {
          reset(true);
      itemcount=(jpaController.lista_ponencias(null,cedula,rol).size());
      pagingInfo.setItemCount(itemcount);          
      int[] rango=new int[]{0, 0 + 10};      
      ponencias_lista=jpaController.lista_ponencias(rango,cedula,rol);      
      return "ponencia_list";
    }

    public String createSetup() {
        reset(false);
        ponencia = new Ponencia();
        return "ponencia_create";
    }

    public String create(int cedulactual, int rol) {                
        
        if(rol == 2){
            Participantes pnuevo = new Participantes(cedulactual);        
            ponencia.setParticipantesCedula(pnuevo);
        }
        
        try {
            utx.begin();
        } catch (Exception ex) {
        }        
        try {
            
            Exception transactionException = null;
            jpaController.create(ponencia);             
            
            try {                
                utx.commit();                                
                System.out.println("******************************************la ponencia se creo con exito. ***************************************");
            } catch (javax.transaction.RollbackException ex) {
                
                transactionException = ex;
            } catch (Exception ex) {
            }
            if (transactionException == null) {
                
                JsfUtil.addSuccessMessage("Ponencia creado con exito.");
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
//        itemcount=(jpaController.lista_ponencias(null,cedulactual,rol).size());
//        pagingInfo.setItemCount(itemcount);          
//        int[] rango=new int[]{0, 0 + 10};      
//        ponencias_lista=jpaController.lista_ponencias(rango,cedulactual,rol);      
//        return listSetup(ponencia.getParticipantesCedula().getRol().getIdrol(),ponencia.getParticipantesCedula().getCedula());
        return listSetup(rol,cedulactual);
    }
    
    public String createu() {
        try {
            utx.begin();
        } catch (Exception ex) {
        }
        try {
            Exception transactionException = null;

            jpaController.create(ponencia);
            try {
                utx.commit();
            } catch (javax.transaction.RollbackException ex) {
                transactionException = ex;
            } catch (Exception ex) {
            }
            if (transactionException == null) {
                JsfUtil.addSuccessMessage("Ponencia creado con exito.");
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
        return "welcome_user";
    }

    public String detailSetup() {
        return scalarSetup("ponencia_detail");
    }

    public String editSetup() {        
        return scalarSetup("ponencia_edit");
    }

    private String scalarSetup(String destination) {
        reset(false);
        ponencia = (Ponencia) JsfUtil.getObjectFromRequestParameter("jsfcrud.currentPonencia", converter, null);
//        Creo que este if nunca se hace
        if (ponencia == null) {
            String requestPonenciaString = JsfUtil.getRequestParameter("jsfcrud.currentPonencia");
            JsfUtil.addErrorMessage("La ponencia " + requestPonenciaString + " no existe.");
            return relatedOrListOutcome(ponencia.getParticipantesCedula().getCedula(),ponencia.getParticipantesCedula().getRol().getIdrol());
//            return relatedOrListOutcome();
        }
        return destination;
    }

    public String edit() {
        String ponenciaString = converter.getAsString(FacesContext.getCurrentInstance(), null, ponencia);
        String currentPonenciaString = JsfUtil.getRequestParameter("jsfcrud.currentPonencia");
        if (ponenciaString == null || ponenciaString.length() == 0 || !ponenciaString.equals(currentPonenciaString)) {
            String outcome = editSetup();
            if ("ponencia_edit".equals(outcome)) {
                JsfUtil.addErrorMessage("No se pudo editar la ponencia, intente de nuevo.");
            }
            return outcome;
        }
        try {
            utx.begin();
        } catch (Exception ex) {
        }
        try {
            Exception transactionException = null;
            jpaController.edit(ponencia);
            try {
                utx.commit();
            } catch (javax.transaction.RollbackException ex) {
                transactionException = ex;
            } catch (Exception ex) {
            }
            if (transactionException == null) {
                JsfUtil.addSuccessMessage("Ponencia actualizado con exito.");
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
        return listSetup(ponencia.getParticipantesCedula().getRol().getIdrol(), ponencia.getParticipantesCedula().getCedula());
    }
    
    public String editu() {
        String ponenciaString = converter.getAsString(FacesContext.getCurrentInstance(), null, ponencia);
        String currentPonenciaString = JsfUtil.getRequestParameter("jsfcrud.currentPonencia");
        if (ponenciaString == null || ponenciaString.length() == 0 || !ponenciaString.equals(currentPonenciaString)) {
            String outcome = editSetup();
            if ("ponencia_edit".equals(outcome)) {
                JsfUtil.addErrorMessage("No se pudo editar la ponencia, intente de nuevo.");
            }
            return outcome;
        }
        try {
            utx.begin();
        } catch (Exception ex) {
        }
        try {
            Exception transactionException = null;
            jpaController.edit(ponencia);
            try {
                utx.commit();
            } catch (javax.transaction.RollbackException ex) {
                transactionException = ex;
            } catch (Exception ex) {
            }
            if (transactionException == null) {
                JsfUtil.addSuccessMessage("Ponencia actualizada con exito.");
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
        // Duda return
        return "participantes_detail";
    }

    public String remove(int cedulactual, int rol) {
        String idAsString = JsfUtil.getRequestParameter("jsfcrud.currentPonencia");        
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
                JsfUtil.addSuccessMessage("Ponencia borrada con exito.");
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
        return relatedOrListOutcome(cedulactual,rol);
    }

    private String relatedOrListOutcome() {
        String relatedControllerOutcome = relatedControllerOutcome();
        if (relatedControllerOutcome != null) {
            return relatedControllerOutcome;
        }
        return listSetup(1/*nose agregar este 1 si hace algún daño*/,0);
//        return listSetup(1/*nose agregar este 1 si hace algún daño*/,0);
    }  
    private String relatedOrListOutcome(int cedulactual, int rol) {
        String relatedControllerOutcome = relatedControllerOutcome();
        if (relatedControllerOutcome != null) {
            return relatedControllerOutcome;
        }
        return listSetup(rol,cedulactual);
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
        ponencia = null;
        ponencias_lista = null;
        pagingInfo.setItemCount(-1);
        if (resetFirstItem) {
            pagingInfo.setFirstItem(0);
        }
    }

   private void resetnextprevponencia() {       
        aux_nextprevponencia=1;
        ponencia = null;        
        pagingInfo.setItemCount(itemcount); 
    }
    
    public List<Ponencia> getPonenciaItems(){
        if (ponenciaItems == null) {
            getPagingInfo();
            ponenciaItems = jpaController.lista_ponencias(new int[]{pagingInfo.getFirstItem()
                    , pagingInfo.getFirstItem() + pagingInfo.getBatchSize()}
                    ,0, 1/*no se si agregar el 1 hace algún daño (Estos dos numeros no importan)*/);
        }
        return ponenciaItems;
     }

    public List<Ponencia> getEventos() {
        return ponencias;
     }

    public String next() throws ParseException{                
        //reset(true);
        resetnextprevponencia();
         getPagingInfo().nextPage();
         //pagingInfo.setItemCount(itemcount); 
        // busca();
         return "ponencia_list";
     }

    public List<Ponencia> getEventos_fecha() {
        getPagingInfo();
        if (ponencias_fecha == null) {
          getPagingInfo();
          ponencias_fecha = jpaController.findRange(new int[]{pagingInfo.getFirstItem(), pagingInfo.getFirstItem() + pagingInfo.getBatchSize()});
        }
        return ponencias_fecha;
    }

    public String prev() throws ParseException {
        //reset(false);
        //reset(true);
        resetnextprevponencia();
        getPagingInfo().previousPage();
        //pagingInfo.setItemCount(itemcount); 
      //  buscaFecha();
        return "ponencia_list";
    }

    public void validateCreate(FacesContext facesContext, UIComponent component, Object value) {
        Ponencia newPonencia = new Ponencia();
        String newPonenciaString = converter.getAsString(FacesContext.getCurrentInstance(), null, newPonencia);
        String ponenciaString = converter.getAsString(FacesContext.getCurrentInstance(), null, ponencia);
        if (!newPonenciaString.equals(ponenciaString)) {
            createSetup();
        }
    }

    public Converter getConverter() {
        return converter;
    }     
}

    /*public int getPonencias_num_filtro(){//no se para que sirve este metodo y no he visto el uso
        return ponencias_num_filtro;
    }*/

    /*public void cambioAlcance(ValueChangeEvent evt){
        aux_alcance = (String)evt.getNewValue();
    }*/