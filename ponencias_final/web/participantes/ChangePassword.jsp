<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<f:view>
    <html>
        <head>
            <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>  
             <h:form>
                <h:commandLink title="volver al inicio" action="#{participantes.validaWelcome}">
                    <h:graphicImage value="../Home.png"/><h:outputText value="inicio"/>
                </h:commandLink> | <h:commandLink  title="cambiar contraseÃ±a" action="#{participantes.go_change}">
                <h:graphicImage value="../clave.png"/>           <Strong><h:outputText value="Cambiar contraseña"/>
                </Strong> </h:commandLink> | <h:commandLink  title="Usuario" action="#{participantes.editSetup}">
                <h:graphicImage value="../user.png"/>           <Strong><h:outputText value="#{user.nombre}"/>
                </Strong> </h:commandLink> |<h:outputText value=" #{participantes.fecha}"/> <div align="right">
                    <h:commandLink action="#{participantes.logout}" value="(salir)">
                        <h:graphicImage value="../exit.png"/>
                    </h:commandLink></div></h:form>
            <title>Cambio Contraseña</title>

            <link rel="stylesheet" type="text/css" href="/ponencias/faces/jsfcrud.css" />
        </head>
        <body>
            
            <h:messages errorStyle="color: red" 
                        infoStyle="color: green" 
                        layout="table"/>
            
        <h1>Cambio Contraseña</h1>
        <h:form>
            <h:panelGrid columns="2">               
                <h:outputText value="Usuario:"/>
                <h:outputText id="nombre" 
                              value="#{participantes.participantes.nombre}" 
                              title="Nombre" />
                
                <h:outputText value="Contraseña Actual:"
                              style="font-weight: bold"/>
                <h:inputSecret id="contraseñaant" 
                             maxlength="45"
                             value="#{participantes.participantes.contraseña}" 
                             title="Contraseña Actual" 
                             required="true" 
                             requiredMessage="La contraseña Actual es obligatoria." />
                
                <h:outputText value="Contraseña Nueva:"
                              style="font-weight: bold"/>
                <h:inputSecret id="contraseña" 
                               maxlength="45"
                               value="#{participantes.participantesb.contraseña}" 
                               title="Contraseña Nueva" 
                               required="true" 
                               requiredMessage="La Contraseña Nueva es obligatoria" />
                
                <h:outputText value="Confirmar Nueva Contraseña:"
                              style="font-weight: bold"/>
                <h:inputSecret id="contraseñav" 
                               maxlength="45"
                               value="#{participantes.participantesb.contraseñav}" 
                               title="Confirmar Nueva Contraseña" 
                               required="true" 
                               requiredMessage="La validación de la Nueva Contraseña es obligatoria" />
            </h:panelGrid>
            <br />
            <h:commandButton id="submit"
                             type="submit"
                             action="#{participantes.change}" 
                             value="Guardar"/>                
            <br />
            <br />
            <h:commandButton value="Cancelar" 
                             action="#{participantes.validaWelcome}" 
                             immediate="true" />

        </h:form>
        </body>
    </html>
</f:view>
