

<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>

<%@taglib prefix="f" uri="http://java.sun.com/jsf/core"%>
<%@taglib prefix="h" uri="http://java.sun.com/jsf/html"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
       
        <title>Login</title>
                            <link rel="stylesheet" type="text/css" href="/ponencias/faces/jsfcrud.css" />

    </head>
    <body>

    <h1>Login</h1>
    
    <f:view>
       
        <h:messages errorStyle="color: red" infoStyle="color: green"
                    showDetail="true"/>
        <h:form id="login">
            <h:panelGrid columns="2" border="0">
               
                cedula: <h:inputText id="cedula"
                             value="#{participantes.participantes.cedula}" required="true" requiredMessage="por favor ingrese su cedula"/>
        
    

            </h:panelGrid>
         
              <h:commandButton id="recupera"

                           value="recuperar contraseña"
                           action="#{participantes.recuperaC}"
                           
                           />
               <h:commandButton id="volver"

                           value="regresar"
                           action="login"
                           immediate="true"
                           />
 
        </h:form>
           
    </f:view>
    
    </body>
</html>
