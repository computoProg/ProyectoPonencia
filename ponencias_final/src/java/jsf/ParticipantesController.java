package jsf;
import beans.Participantes;
import beans.ParticipantesFacade;
import beans.Proyecto;
import beans.Rol;
import beans.util.PagingInfo;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import javax.faces.FacesException;
import javax.annotation.Resource;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.model.SelectItem;
import javax.transaction.UserTransaction;
import beans.util.JsfUtil;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Aux de Programacion
 */
public class ParticipantesController {
    private static final String SMTP_HOST_NAME = "smtp.gmail.com";
    private static final int SMTP_HOST_PORT = 465;
    private static final String SMTP_AUTH_USER = "cifecontacto@gmail.com";
    private static final String SMTP_AUTH_PWD  = "soporte.cife";
    public static final String USER_SESSION_KEY = "user";

    public ParticipantesController() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        jpaController = (ParticipantesFacade) facesContext.getApplication().getELResolver().getValue(facesContext.getELContext(), null, "participantesJpa");
        pagingInfo = new PagingInfo();
        converter = new ParticipantesConverter();

    }
    private Participantes contrasena=null;
    public Participantes p=null;
    private Participantes participantes = null;
    private List<Proyecto> proyectos_participante=null;
    private List<Participantes> participantesItems = null;
    private List<Participantes> participantes_lista = null;
    private Participantes participantesb=null;
    private ParticipantesFacade jpaController = null;
    private int num_proyectos;
    private int participantes_num_filtro;
    private int aux_cedula=0,aux_vinculacion=0,aux_rol=0, aux_grupoInvestigacion;
    private String aux_nombre=" ", aux_estado=" ";
    private ParticipantesConverter converter = null;
    private PagingInfo pagingInfo = null;
    private boolean booleanPassword = false;
    @Resource
    private UserTransaction utx = null;

    public PagingInfo getPagingInfo() {
        if (pagingInfo.getItemCount() == -1) {
            pagingInfo.setItemCount(jpaController.participantes_para_num(aux_cedula, aux_nombre, aux_vinculacion, aux_rol,aux_grupoInvestigacion, aux_estado).size());
        }
        return pagingInfo;
    }

    public SelectItem[] getParticipantesItemsAvailableSelectMany() {
        return JsfUtil.getSelectItems(jpaController.findAll(), false);
    }

    public SelectItem[] getParticipantesItemsAvailableSelectOne() {
            Participantes par= jpaController.find(p.getCedula());
            if (par.getRol().getIdrol()!=1)
                return JsfUtil.getSelectItems(jpaController.participantesEvento(p.getCedula()), true);
            else
                return JsfUtil.getSelectItems(jpaController.findAll(), true);
    }

    
 public SelectItem[] getProyectosParticipantesItemsAvailableSelectOne() {
      return JsfUtil.getSelectItems(jpaController.proyectosParticipante(p.getCedula()), true);    
  }

    public Participantes getParticipantes() {
        if (participantes == null) {
            participantes = (Participantes) JsfUtil.getObjectFromRequestParameter("jsfcrud.currentParticipantes", converter, null);
        }
        if (participantes == null) {
            participantes = new Participantes();
        }        
        return participantes;
    }
     public Participantes getParticipantesb() {
        if (participantesb == null) {
            participantesb = (Participantes) JsfUtil.getObjectFromRequestParameter("jsfcrud.currentParticipantes", converter, null);
        }
        if (participantesb == null) {
            participantesb = new Participantes();
        }
        return participantesb;
    }
   public Participantes getP() {
        return p;
    }
    public Participantes getContrasena() {
        return contrasena;
    }    
    
    public boolean getBooleanPassword(){
        return booleanPassword;
    }
    
    public void cambiarPassword(){
        booleanPassword = true;
    }
    
    public String listSetup() {
        reset(true);
     
      int[] rango=new int[]{0, 0 + 10};
     
        participantes_lista=jpaController.participantes_lista(rango);
        participantes_num_filtro= participantes_lista.size();

        return "participantes_list";
    }
public int getParticipantes_num_filtro(){
    return participantes_num_filtro;
}
    public String createSetupU() {
        reset(false);
// Las siguientes lineas no las debería hacer aqui cuando se hace el llamado a este método desde
// la página de inicio, ya que no es fijo que cuando se ingrese a es página
// Necesariamente se cree un participante.
        participantes = new Participantes();
        participantes.setEstado("Activo");
//        Rol = 2, es del usuario estandar; Rol = 1, es del administrador software y Rol = 3 es del admin cife
        participantes.setRol(new Rol(2)); 
        return "participantes_createU";
    }
    
//    Creo que este método(createSetupA) no lo voy va usar, se usaba era para crear el participante por medio del administrado
//    pero como el administrador no va a cumplir esta función por eso este método no se usa y esto va a omitir mucho
//    código
     public String createSetupA() {
        reset(false);
        participantes = new Participantes();
        participantes.setEstado("Activo");
        return "participantes_createA";
    }
      public String recuperaC() throws Exception {      
       participantes=jpaController.find(participantes.getCedula());
       if(participantes==null){
               JsfUtil.addErrorMessage("La cedula ingresada no está registrada, por favor verifique.");
               return "recupera";
       }
       
       Properties props = new Properties();

	        props.put("mail.transport.protocol", "smtps");
	        props.put("mail.smtps.host", SMTP_HOST_NAME);
	        props.put("mail.smtps.auth", "true");

	        Session mailSession = Session.getDefaultInstance(props);
	        // mailSession.setDebug(true);
	        Transport transport = mailSession.getTransport();
	        MimeMessage message = new MimeMessage(mailSession);
	        message.setSubject("Recuperacion contraseña ");
	        message.setContent("Estimad@ "+participantes.getNombre()+", recuerde que su contraseña para ingresar al portal es: "+participantes.getContraseña()+"\n<b>CIFE<b>","text/plain ");

	        message.addRecipient(Message.RecipientType.TO,
	             new InternetAddress(participantes.getEmail()));

	        transport.connect
	          (SMTP_HOST_NAME, SMTP_HOST_PORT, SMTP_AUTH_USER, SMTP_AUTH_PWD);

	        transport.sendMessage(message,
	            message.getRecipients(Message.RecipientType.TO));
                JsfUtil.addSuccessMessage("su contraeña ha sido enviada a su correo");
	        transport.close();
        participantes=null;
        return "login";
      }
    public String validateUser(){
        FacesContext context = FacesContext.getCurrentInstance();
        p=jpaController.find(participantes.getCedula());
        if(p!=null){
            if(!validatePass(participantes.getContraseña())){
                JsfUtil.addErrorMessage("La contraseña debe ser compuesta de solo letras y números.");
                return "login";
            }
            if((p.getContraseña()).equals(participantes.getContraseña())){
                if(p.getEstado().equals("No_Activo")){
                    return "participante_inactivo";
                }
                if(p.getRol().getIdrol()!=2){
                    context.getExternalContext().getSessionMap().put(USER_SESSION_KEY, p);
                    return "welcome_admin";
                }
                context.getExternalContext().getSessionMap().put(USER_SESSION_KEY, p);
                return "welcome_user";
            }else{
                JsfUtil.addErrorMessage("El usuario es incorrecto, por favor intente de nuevo."); 
                return "login";
            }
        }else{
            JsfUtil.addErrorMessage("El usuario es incorrecto, por favor intente de nuevo.");
        }
        return "login";
    }
    public boolean validateCedula(){
        p=jpaController.find(participantes.getCedula());
        if(p!=null)
            return true;                    
        return false;
    }
    
    
    // Pasar validaciones a otra capa o paquete
    public boolean validateMail(String mail){
        Pattern pattern;
        Matcher matcher;
        pattern = Pattern.compile("^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
        matcher = pattern.matcher(mail);
        return matcher.matches();
    }

    // Pasar validaciones a otra capa o paquete
    public boolean validatePass(String pass){
        Pattern pattern;
        Matcher matcher;
        //pattern = Pattern.compile("([a-zA-Z]+[0-9]+)|([0-9]+[a-zA-Z]+)");
        //pattern = Pattern.compile("(?!^[0-9]*$)(?!^[a-zA-Z]*$)^([a-zA-Z0-9]{8,10})$");
        pattern = Pattern.compile("(?!^[0-9]*$)(?!^[a-zA-Z]*$)(?!^[a]*$)^([a-zA-Z0-9]{8,45})$");
        matcher = pattern.matcher(pass);
        return matcher.matches();
    }
    
    // Pasar validaciones a otra capa o paquete
    public boolean validateMetaCaracteres(String MetaCaracteres){
        Pattern pattern;
        Matcher matcher;        
//        pattern = Pattern.compile("^[a-zA-Z0-9ñÑáéíóúÁÉÍÓÚ]+$");
        pattern = Pattern.compile("^[a-zA-ZñÑáéíóúÁÉÍÓÚ]+$");
        matcher = pattern.matcher(MetaCaracteres);
        return matcher.matches();
    }

    public String create() {
        try {
            utx.begin();
        } catch (Exception ex) {
        }
        try {
            Exception transactionException = null;
            // Pasar validaciones a otra capa o paquete
            if(!validateMetaCaracteres(participantes.getNombre()) | !validateMetaCaracteres(participantes.getApellido())){
                JsfUtil.addErrorMessage("El nombre y apellido deben ser compuestos de solo letras.");
                return "participantes_createU";
            }
            
            // Pasar validaciones a otra capa o paquete
            if(!validatePass(participantes.getContraseña()) | !validatePass(participantes.getContraseñav())){
                JsfUtil.addErrorMessage("La contraseña debe ser compuesta de solo letras y números.");
                return "participantes_createU";
            }
            
            if(!(participantes.getContraseña()).equals(participantes.getContraseñav()))
            {
                JsfUtil.addErrorMessage("las contraseñas no coinciden.");
//                Creo que siempre va a ser createU por lo que el if se hace inecesario
//                if(p.getRol().getIdrol() != 2)
//                    return "participantes_createA";
//                else
//                    return "participantes_createU";
                return "participantes_createU";
          }else if(validateCedula()){
//            }else if((participantes.getCedula()) == 1){
//                JsfUtil.addErrorMessage("Ya se encuentra un participante registrado con esta cédula en nuestra base de datos.");
                JsfUtil.addErrorMessage("Ya se existe una cuenta en el sistema con este número de identificación. Por favor contactar el administrador de la plataforma.");
                return "participantes_createU";
          }else if(!validateMail(participantes.getEmail())){
                JsfUtil.addErrorMessage("email incorrecto intente de nuevo.");
//                Creo que siempre va a ser createU por lo que el if se hace inecesario
//                  if(p.getRol().getIdrol() != 2)
//                    return "participantes_createA";
//                  else
//                    return "participantes_createU";
                  return "participantes_createU";
            }else{
                jpaController.create(participantes);
            try {
                utx.commit();
            } catch (javax.transaction.RollbackException ex) {
                transactionException = ex;
            } catch (Exception ex) {
            }
            if (transactionException == null) {
                if(p==null)
                JsfUtil.addSuccessMessage("Registro exitoso, ya puede iniciar sesion.");
                else
                JsfUtil.addSuccessMessage("Registro exitoso.");

            } else {
                JsfUtil.ensureAddErrorMessage(transactionException, "ha ocurrido un error");
            }
        }} catch (Exception e) {
            try {
                utx.rollback();
            } catch (Exception ex) {
            }
            JsfUtil.ensureAddErrorMessage(e, "A persistence error occurred.");
            return null;
        }

         if (p==null)
        return "login";
         else
        return "participantes_list";
    }

     public String logout() {
        HttpSession session = (HttpSession)
             FacesContext.getCurrentInstance().getExternalContext().getSession(false);
        if (session != null) {
            session.invalidate();
        }
        return "login";
    }

    public String detailSetup() {
        return scalarSetup("participantes_detail");
    }

    public String editSetup() {
        if(p.getRol().getIdrol()==1){
                 return scalarSetupA("participantes_edit");
        }
        else 
            return scalarSetupA("participantes_edit_user");
    }
    
    private String scalarSetup(String destination) {
        reset(false);        
        participantes = (Participantes) JsfUtil.getObjectFromRequestParameter("jsfcrud.currentParticipantes", converter, null);


        if(participantes==null){
            participantes = jpaController.find(p.getCedula());
        }
        if (participantes == null) {
            String requestParticipantesString = JsfUtil.getRequestParameter("jsfcrud.currentParticipantes");
            JsfUtil.addErrorMessage("El participante con el id " + requestParticipantesString + " no existe.");
            return relatedOrListOutcome();
        }
        return destination;
    }
    private String scalarSetupA(String destination) {
        reset(false);
        participantes = (Participantes) JsfUtil.getObjectFromRequestParameter("jsfcrud.currentParticipantes", converter, null);
        if(participantes==null){
            participantes=p;            
        }        
        if (participantes == null) {
            String requestParticipantesString = JsfUtil.getRequestParameter("jsfcrud.currentParticipantes");
            JsfUtil.addErrorMessage("El participante con el id " + requestParticipantesString + " no existe.");
            return relatedOrListOutcome();
        }
        else return destination;
    }

    public String edit() {
        String participantesString = converter.getAsString(FacesContext.getCurrentInstance(), null, participantes);
        String currentParticipantesString = JsfUtil.getRequestParameter("jsfcrud.currentParticipantes");
        if (participantesString == null || participantesString.length() == 0 || !participantesString.equals(currentParticipantesString)) {
            String outcome = editSetup();
            if ("participantes_edit".equals(outcome) || "participantes_edit_user".equals(outcome)) {
                JsfUtil.addErrorMessage("no se encuentra el participante, intente nuevamente.");
            }
            return outcome;
        }
        try {
            utx.begin();
        } catch (Exception ex) {
        }
        try {
            Exception transactionException = null;
               if(!participantes.getContraseña().equals(participantes.getContraseñav())){
                    JsfUtil.addErrorMessage("las contraseñas no coinciden.");
                    if(p.getRol().getIdrol()==1)
                            return "participantes_edit";
                    else 
                            return "participantes_edit_user";
               }
               else 
                   if(!validateMail(participantes.getEmail())){
                       JsfUtil.addErrorMessage("email incorrecto intente de nuevo.");
                       if(p.getRol().getIdrol()==1)
                           return "participantes_edit";
                       else 
                           return "participantes_edit_user";
                   }
                   else 
                       jpaController.edit(participantes);
            try {
                utx.commit();
            } catch (javax.transaction.RollbackException ex) {
                transactionException = ex;
            } catch (Exception ex) {
            }
            if (transactionException == null) {
                JsfUtil.addSuccessMessage("La informacion ha sido actualizada con exito.");
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
              int[] rango=new int[]{0, 0 + 10};            
              participantes_lista=jpaController.participantes_lista(rango);  
                /* verificar que es...*/
        if(p.getRol().getIdrol()!=1)
            return detailSetup();
        else 
            return "participantes_list";
    }
  
    public String desvincular() {
        if(p.getRol().getIdrol()==1){
            participantes = (Participantes) JsfUtil.getObjectFromRequestParameter("jsfcrud.currentParticipantes", converter, null);
            participantes.setEstado("No_Activo");
        }
        else{
            participantes.setEstado("No_Activo");
        }            
        String participantesString = converter.getAsString(FacesContext.getCurrentInstance(), null, participantes);
        String currentParticipantesString = JsfUtil.getRequestParameter("jsfcrud.currentParticipantes");
        if (participantesString == null || participantesString.length() == 0 || !participantesString.equals(currentParticipantesString)) {
            String outcome = editSetup();
            if ("participantes_edit".equals(outcome) || "participantes_edit_user".equals(outcome)) {
                JsfUtil.addErrorMessage("no se encuentra el participante, intente nuevamente.");
            }
            return outcome;
        }
        try {
            utx.begin();
        } catch (Exception ex) {
        }
        try {
            Exception transactionException = null;
            jpaController.edit(participantes);

            try {
                utx.commit();
            } catch (javax.transaction.RollbackException ex) {
                transactionException = ex;
            } catch (Exception ex) {
            }
            if (transactionException == null) {
                JsfUtil.addSuccessMessage("Usted se ha desvinculado correctamente");
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
       
        if(p.getRol().getIdrol() == 1)
            return "participantes_list";
        else
            return logout();
    } 
    
    public String go_change(){
        reset(false);
       return scalarSetup("participantes_change");
    }
    
     public String change() {
        FacesContext context = FacesContext.getCurrentInstance();
        p=jpaController.find(participantes.getCedula());        
        if(p!=null){
            if(!validatePass(participantes.getContraseña())){
                JsfUtil.addErrorMessage("La contraseña debe ser compuesta de solo letras y números.");
                return "participantes_change";
            }
            if(!(p.getContraseña()).equals(participantes.getContraseña())){
                JsfUtil.addErrorMessage("Contraseña Actual Incorrecta"); 
                return "participantes_change";
            }
        }
        try {
            utx.begin();
        } catch (Exception ex) {}
        
        try {
            Exception transactionException = null;
            if(!validatePass(participantesb.getContraseña()) | !validatePass(participantesb.getContraseña())){
                JsfUtil.addErrorMessage("La contraseña debe ser compuesta de solo letras y números.");
                return "participantes_change";
            }
            if(!participantesb.getContraseña().equals(participantesb.getContraseñav())){
                JsfUtil.addErrorMessage("los campos para la nueva contraseña no coinciden.");
                return "participantes_change";
            }
            else{
                participantes.setContraseña(participantesb.getContraseña());
                participantes.setContraseñav(participantesb.getContraseñav());
                jpaController.edit(participantes);
            }
            try {
                utx.commit();
            } catch (javax.transaction.RollbackException ex) {
                transactionException = ex;
            }
            if (transactionException == null) {
                JsfUtil.addSuccessMessage("La contraseña ha sido actualizada con exito.");
            } 
            else {
                JsfUtil.ensureAddErrorMessage(transactionException, "Un error ha ocurrido.");
            }
        } catch (Exception e) {
            try {
                utx.rollback();
            } catch (Exception ex) {}
            JsfUtil.ensureAddErrorMessage(e, "Un error ha ocurrido.");
            return null;
        }
        int[] rango=new int[]{0, 0 + 10};
        participantes_lista=jpaController.participantes_lista(rango);
        if(p.getRol().getIdrol()==2)
            return "welcome_user";
        else
            return "welcome_admin";
    }


    public String remove() {
        String idAsString = JsfUtil.getRequestParameter("jsfcrud.currentParticipantes");
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
                JsfUtil.addSuccessMessage("Participante borrado con exito.");
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
    public String busca()
    {

        participantes=jpaController.find(participantes.getCedula());
        return "participantes_list";
    }

    //devuelve la lista
public List<Proyecto> getProyectos_participante(){
            Participantes  parti = (Participantes) p;
                proyectos_participante=jpaController.proyectosParticipante(parti.getCedula());
            return proyectos_participante;
}

public int getNum_proyectos(){
    Participantes  parti = (Participantes) p;
proyectos_participante=jpaController.proyectosParticipante(parti.getCedula());
num_proyectos=proyectos_participante.size();

    return num_proyectos;
}
    public List<Participantes> getParticipantesItems() {

        if (participantesItems == null) {
            getPagingInfo();
            participantesItems = jpaController.findRange(new int[]{pagingInfo.getFirstItem(), pagingInfo.getFirstItem() + pagingInfo.getBatchSize()});
        }
        return participantesItems;
    }
    
     public List<Participantes> getParticipantes_lista() {
        if (participantes_lista == null) {
          getPagingInfo();
          participantes_lista = jpaController.findRange(new int[]{pagingInfo.getFirstItem(), pagingInfo.getFirstItem() + pagingInfo.getBatchSize()});
        }      
        return participantes_lista;
    }

    public String next() {
        reset(false);
        getPagingInfo().nextPage();
        busca_participantes();
        return "participantes_list";
    }

    public String prev() {
        reset(false);
        getPagingInfo().previousPage();
        busca_participantes();
        return "participantes_list";
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
        participantes = null;
        participantes_lista = null;
        pagingInfo.setItemCount(-1);
        if (resetFirstItem) {
            pagingInfo.setFirstItem(0);
        }
    }

    public Converter getConverter() {
        return converter;
    }
    public String validaWelcome()    {
     Participantes pa=jpaController.find(p.getCedula());
        if(pa.getRol().getIdrol()!=2)
            return "welcome_admin";
        else
            return "welcome_user";
    }


/*Método para obtener la fecha actual*/
public String getFecha(){
    /*SimpleDateFormat[] sdfs = {
    //new SimpleDateFormat("dd-MM-yyyy h:mm a"),
    //new SimpleDateFormat("dd/MMM/yy HH:mm:ss"),
    new SimpleDateFormat(" EEEE',' dd 'de' MMMM 'de' yyyy")
    };*/
    SimpleDateFormat[] sdfs = {new SimpleDateFormat(" EEEE',' dd 'de' MMMM 'de' yyyy")};
    //int dia = ;
    for (Date d = new Date();;d = new Date(d.getTime()+1468800000)) {//3600000
        for (SimpleDateFormat sdf : sdfs) {
            return sdf.format(d);
        }
    }
}

public String busca_participantes(){
    if(participantesb.getCedula()!=null)
        aux_cedula=participantesb.getCedula();    
    if(participantesb.getVinculacion()!=null)
        aux_vinculacion=participantesb.getVinculacion().getIdvinculacion();    
    if(participantesb.getRol()!=null)
        aux_rol=participantesb.getRol().getIdrol();
    if(participantesb.getNombre()!=null)
        aux_nombre=participantesb.getNombre();
    if(participantesb.getGrupoInvestigacionidGrupoInvestigacion()!=null)
        aux_grupoInvestigacion=participantesb.getGrupoInvestigacionidGrupoInvestigacion().getIdGrupoInvestigacion();
    if(participantesb.getEstado()!= null)
        if(!participantesb.getEstado().equals("---"))
            aux_estado=participantesb.getEstado();

    getPagingInfo();
    pagingInfo.setItemCount((jpaController.participantes_para_num(aux_cedula, aux_nombre,aux_vinculacion, aux_rol, aux_grupoInvestigacion, aux_estado).size()));
    int[] rango=new int[]{pagingInfo.getFirstItem(), pagingInfo.getFirstItem() + pagingInfo.getBatchSize()};

    participantes_lista=jpaController.participantes_cedula_nombre_vinculacion_rol(rango,aux_cedula, aux_nombre,aux_vinculacion, aux_rol,aux_grupoInvestigacion, aux_estado);
    participantes_num_filtro=participantes_lista.size();
    aux_cedula=0;
    aux_vinculacion=0;
    aux_nombre=" ";
    aux_rol=0;
    aux_grupoInvestigacion=0;
    aux_estado=" ";
    return "participantes_list";
    }    
}