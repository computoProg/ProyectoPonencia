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
            <title>Editar Participantes</title>

            <link rel="stylesheet" type="text/css" href="/ponencias/faces/jsfcrud.css" />
        </head>
        <body>
            <script  language="javascript">
                function confirmDelete()
                {
                    return confirm('¿Esta seguro que desea desvincularse?\n\ Al desvincularse usted no podrá acceder de nuevo a la aplicación\n\Para vincularse de nuevo debe dirigirse al administrador');
                }
            </script>
            
        <h:panelGroup id="messagePanel" layout="block">
            <h:messages errorStyle="color: red" infoStyle="color: green" layout="table"/>
        </h:panelGroup>
        <h1>Editar Participantes</h1>
        <h:form>
            <h:panelGrid columns="2">
                <h:outputText value="Cédula:" style="font-weight: bold"/>
                <h:outputText value="#{participantes.participantes.cedula}" 
                              title="Cedula" />
                
                <h:outputText value="Nombre:" style="font-weight: bold"/>
                <h:inputText id="nombre" maxlength="45" value="#{participantes.participantes.nombre}" 
                             title="Nombre" 
                             required="true" 
                             requiredMessage="El nombre es obligatorio." />
                
                <h:outputText value="Apellidos:" style="font-weight: bold"/>
                <h:inputText id="apellido" maxlength="45" value="#{participantes.participantes.apellido}" 
                             title="Apellido" 
                             required="true" 
                             requiredMessage="El Apellido es obligatorio." />
                
                <h:outputText value="E-mail:" style="font-weight: bold"/>
                <h:inputText id="email" maxlength="60" value="#{participantes.participantes.email}" 
                             title="Email"
                             required="true" 
                             requiredMessage="El e-mail es obligatorio." />
                
                <h:outputText value="Vinculación:" style="font-weight: bold"/>
                <h:selectOneMenu id = "vinculacion"
                        value="#{participantes.participantes.vinculacion}"
                        required="true"
                        requiredMessage="La vinculación es obligatoria">
                    <f:selectItems value="#{vinculacion.vinculacionItemsAvailableSelectOne}"/>
                </h:selectOneMenu>
                
            </h:panelGrid>
             
            <br />
            <h:commandButton action="#{participantes.edit}" value="Guardar Cambios">
                <f:param name="jsfcrud.currentParticipantes" 
                         value="#{jsfcrud_class['beans.util.JsfUtil'].jsfcrud_method['getAsConvertedString'][participantes.participantes][participantes.converter].jsfcrud_invoke}"/>
            </h:commandButton>
            
            <h:commandButton action="#{participantes.desvincular}"
                             onclick="return confirmDelete();"
                             value="Desvincular cuenta">
                <f:param name="jsfcrud.currentParticipantes" 
                         value="#{jsfcrud_class['beans.util.JsfUtil'].jsfcrud_method['getAsConvertedString'][participantes.participantes][participantes.converter].jsfcrud_invoke}"/>
            </h:commandButton>
        </h:form>
        </body>
    </html>
</f:view>
