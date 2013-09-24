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
                    <h:graphicImage value="../images/Home.png"/><h:outputText value="inicio"/>
                </h:commandLink> | <h:commandLink  title="cambiar contraseÃ±a" action="#{participantes.go_change}">
                <h:graphicImage value="../images/clave.png"/>           <Strong><h:outputText value="Cambiar contraseña"/>
                </Strong> </h:commandLink> | <h:commandLink  title="Usuario" action="#{participantes.editSetup}">
                <h:graphicImage value="../images/user.png"/>           <Strong><h:outputText value="#{user.nombre}"/>
                </Strong> </h:commandLink> |<h:outputText value=" #{participantes.fecha}"/> <div align="right">
                    <h:commandLink action="#{participantes.logout}" value="(salir)">
                        <h:graphicImage value="../images/exit.png"/>
                    </h:commandLink></div></h:form>
            <title>Editar Rol</title>

            <link rel="stylesheet" type="text/css" href="/ponencias/faces/jsfcrud.css" />
        </head>
        <body>
        <h:panelGroup id="messagePanel" layout="block">
            <h:messages errorStyle="color: red" infoStyle="color: green" layout="table"/>
        </h:panelGroup>
        <h1>Editar Rol</h1>
        <h:form>
            <h:panelGrid columns="2">
                <h:outputText value="Idrol:"/>
                <h:outputText value="#{rol.rol.idrol}" title="Idrol" />
                <h:outputText value="Rol:"/>
                <h:inputText id="rol" value="#{rol.rol.rol}" title="Rol" required="true" requiredMessage="The rol field is required." />
               <%-- <h:outputText value="ParticipantesCollection:"/>
                <h:selectManyListbox id="participantesCollection" value="#{rol.rol.jsfcrud_transform[jsfcrud_class['beans.util.JsfUtil'].jsfcrud_method.collectionToArray][jsfcrud_class['beans.util.JsfUtil'].jsfcrud_method.arrayToList].participantesCollection}" title="ParticipantesCollection" size="6" converter="#{participantes.converter}" >
                    <f:selectItems value="#{participantes.participantesItemsAvailableSelectMany}"/>
                </h:selectManyListbox>--%>

            </h:panelGrid>
            <br />
            <h:commandButton action="#{rol.edit}" value="Guardar">
                <f:param name="jsfcrud.currentRol" value="#{jsfcrud_class['beans.util.JsfUtil'].jsfcrud_method['getAsConvertedString'][rol.rol][rol.converter].jsfcrud_invoke}"/>
            </h:commandButton>
            <br />
          
            <h:commandButton action="#{rol.listSetup}" value="Mostar roles" immediate="true"/>
            <br />
            <br />
            <h:commandLink value="Inicio" action="welcome_admin" immediate="true" />

        </h:form>
        </body>
    </html>
</f:view>
