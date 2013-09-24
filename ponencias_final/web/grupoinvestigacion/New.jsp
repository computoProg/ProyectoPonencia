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
                    </h:commandLink></div>
            </h:form>

            <title>Nuevo Grupo de Investigación</title>

            <link rel="stylesheet" type="text/css" href="/ponencias/faces/jsfcrud.css" />
        </head>
        <body>
        <h:panelGroup id="messagePanel" layout="block">
            <h:messages errorStyle="color: red" infoStyle="color: green" layout="table"/>
        </h:panelGroup>
        <h1>Nuevo Grupo de Investigación</h1>
        <h:form>
            <h:inputHidden id="validateCreateField" validator="#{GrupoInvestigacion.validateCreate}" value="value"/>
            <h:panelGrid columns="2">
                <h:outputText value="Grupo Investigación:"/>
                <h:inputText maxlength="45" id="grupoinvestigacion" value="#{GrupoInvestigacion.grupoInvestigacion.nombre}" title="GrupoInvestigacion" required="true" requiredMessage="The Grupo Investigación field is required." />
                <%--<h:outputText value="ParticipantesCollection:"/>
                <h:selectManyListbox id="participantesCollection" value="#{rol.rol.jsfcrud_transform[jsfcrud_class['beans.util.JsfUtil'].jsfcrud_method.collectionToArray][jsfcrud_class['beans.util.JsfUtil'].jsfcrud_method.arrayToList].participantesCollection}" title="ParticipantesCollection" size="6" converter="#{participantes.converter}" >
                    <f:selectItems value="#{participantes.participantesItemsAvailableSelectMany}"/>
                </h:selectManyListbox>--%>
                
                <%--Agregar limite a la descripción--%>
                
                <h:outputText value="Descripcion(600 caracteres):"/>
                <h:inputTextarea id="descripciongrupo" 
                                 label="Descripción del grupo de investigación"
                                 value="#{GrupoInvestigacion.grupoInvestigacion.descripcion}" 
                                 title="Descripción del grupo de investigación (solo se permiten 600 caracteres)"
                                 required="true" 
                                 requiredMessage="The Descripción field is required.">
                    <f:validateLength maximum="600"/>
                </h:inputTextarea>
               
                
                <h:outputText value="Clasificación:"/>
                <h:selectOneMenu value="#{GrupoInvestigacion.grupoInvestigacion.clasificacion}">
                    <f:selectItem itemValue="A" />
                    <f:selectItem itemValue="B" />
                    <f:selectItem itemValue="C" />
                </h:selectOneMenu>
            </h:panelGrid>
            <br />
            <h:commandButton action="#{GrupoInvestigacion.create}" value="Crear"/>
            <br />
            <br />
            <%--<h:commandButton action="#{GrupoInvestigacion.listSetup}" value="Mostrar Grupos" immediate="true"/>--%>
            <h:commandLink value="Volver a listar Grupos de Investigación" 
                           action="#{GrupoInvestigacion.listSetup}" 
                           immediate="true" />
            <%--<br />
            <br />
            <h:commandLink value="Inicio" action="welcome_admin" immediate="true" />
            --%>
        </h:form>
        </body>
    </html>
</f:view>
