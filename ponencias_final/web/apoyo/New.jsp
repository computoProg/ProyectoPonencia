<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@taglib uri="http://richfaces.org/rich" prefix="rich"%>

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
            <title>Nuevo apoyo</title>

            <link rel="stylesheet" type="text/css" href="/ponencias/faces/jsfcrud.css" />
        </head>
        <body>
        <h:panelGroup id="messagePanel" layout="block">
            <h:messages errorStyle="color: red" infoStyle="color: green" layout="table"/>
        </h:panelGroup>
            <h:outputText value="HOME > Gestionar Apoyos"/>
        <h1>Crear Apoyo</h1>
        <h:form>
            <h:inputHidden id="validateCreateField" 
                           validator="#{apoyo.validateCreate}"
                           value="value"/>
            <h:panelGrid columns="2">
                <h:outputText value="* Nombre:" style="font-weight:bold"/>
                <h:inputText id="nombre" 
                             maxlength="45"
                             value="#{apoyo.apoyo.apoyo}"
                             title="Nombre"
                             required="true"
                             requiredMessage="El nombre del apoyo es obligatorio." />
                
                
                <h:outputText value = "* Estado:" style="font-weight:bold"/>
                <h:selectOneMenu id="estado"
                                 value="#{apoyo.apoyo.estado}"
                                 title="Estado del apoyo"
                                 required="true"
                                 requiredMessage="El estado del apoyo es obligatorio.">
                    <f:selectItem itemValue="Activo"/>
                    <f:selectItem itemValue="No_Activo"/>
                </h:selectOneMenu>
                
                <%--Agregar limite a la descripción--%>
                
                <h:outputText value="Descripcion(600 caracteres):" style="font-weight:bold"/>
                <h:inputTextarea label="Descripción del Apoyo" value="#{apoyo.apoyo.descripcion}"
                                 id="descripcion"                                 
                                 title="Descripción del Apoyo (solo se permiten 600 caracteres)">
                    <f:validateLength maximum="600"/>
                </h:inputTextarea>
            </h:panelGrid>
            
            <br />
            <h:commandButton action="#{apoyo.create}" 
                             value="Crear"/>
            
            <br />
            <br />
            <h:commandLink value="Volver a listar los Apoyos" action="#{apoyo.listSetup()}" immediate="true" />
        </h:form>
        </body>
    </html>
</f:view>
