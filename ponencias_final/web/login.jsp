<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>

<%@taglib prefix="f" uri="http://java.sun.com/jsf/core"%>
<%@taglib prefix="h" uri="http://java.sun.com/jsf/html"%>
<%@taglib prefix="rich" uri="http://richfaces.org/rich" %>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Iniciar Sesión</title>
            <link rel="stylesheet" type="text/css" href="/ponencias/faces/jsfcrud.css" />
    </head>    
    <body>        
     <div align="center">
        <h1>Inicio de Sesión</h1>    
        <f:view>
        <h:messages errorStyle="color: red" 
                    infoStyle="color: green"
                    layout="table"/>        
        <h:form id="error">           
            <h:panelGrid columns="2" border="0">
                <b>Cédula:</b>
                <h:inputText id="cédula"                             
                             maxlength="10"
                             value="#{participantes.participantes.cedula}"
                             required="true"
                             requiredMessage="Por favor ingrese su cédula"/>
        
                <b>Contraseña: </b>
                <h:inputSecret id="contraseña"
                               maxlength="45"
                               value="#{participantes.participantes.contraseña}"
                               required="true"
                               requiredMessage="Por favor ingrese su contraseña">
                    <f:validateLength minimum="8"/>
                    <%--<f:validateRegex pattern="([a-zA-Z]+[0-9]+)|([0-9]+[a-zA-Z]+)"/>
                    <f:validateRegex pattern="[a-zA-Z0-9]+"/>
                    
                    <f:validateRegex pattern="((?=.*[0-9]))"/>
                    <f:validateRegex pattern="^[A-Za-z]+(\\.[0-9]+)$"/>--%>
                </h:inputSecret>
                <div align="left">
                <h:commandButton id="submit"
                             type="submit"
                             value="Ingresar"
                             style="align:right"
                             action="#{participantes.validateUser}"/>
                </div>                
                <h:commandLink id="create"
                           value="Registrarse"
                           action="#{participantes.createSetupU}"
                           immediate="true"/>            
            <br/>
            <br/>
            <%--Link recuperar contraseña aún no esta implementado --%>
              <h:commandLink value="recuperar contraseña"/>
            </h:panelGrid>            
            <br>                       
        </h:form>            
        </div>           
    </f:view>    
    </body>
</html>
