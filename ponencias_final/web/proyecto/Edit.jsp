<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@taglib uri="http://richfaces.org/rich" prefix="rich" %>

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
            <title>Editar Proyecto</title>

            <link rel="stylesheet" type="text/css" href="/ponencias/faces/jsfcrud.css" />
        </head>
        <body>
        <h:panelGroup id="messagePanel" layout="block">
            <h:messages errorStyle="color: red" infoStyle="color: green" layout="table"/>
        </h:panelGroup>
            <h:outputText value="HOME > Gestionar Proyectos"/>
            <h1>Editar Proyecto: <h:outputText value="#{proyecto.proyecto.nombre}"/></h1>
        <h:form>
            <h:panelGrid columns="2">
                <h:outputText value="ID" style="font-weight:bold"/>
                <h:outputText value="#{proyecto.proyecto.idproyecto}"/>
                
                <h:outputText value="Nombre:" style="font-weight:bold"/>
                <h:inputText maxlength="45" value="#{proyecto.proyecto.nombre}"
                             id="nombre"
                             title="Nombre del proyecto"
                             required="true"
                             requiredMessage="El nombre del proyecto es obligatorio."/>
                
                <h:outputText value="Estado:" style="font-weight:bold"/>
                <h:selectOneMenu value="#{proyecto.proyecto.estado}"
                             id="estado"
                             title="Estado en el que se encuentra el proyecto"
                             required="true"
                             requiredMessage="El estado del proyecto es obligatorio">
                    <f:selectItem itemValue="Activo"/>
                    <f:selectItem itemValue="No_Activo"/>
                </h:selectOneMenu>
                
                <h:outputText value="Descripcion(600 caracteres):" style="font-weight:bold"/>
                <h:inputTextarea label="Descripción del proyecto" value="#{proyecto.proyecto.descripcion}"
                                 id="descripcion"
                                 title="Descripción del proyecto (solo se permiten 600 caracteres)">
                    <f:validateLength maximum="600"/>
                </h:inputTextarea>
            </h:panelGrid>
            
            <br />
            <h:commandButton action="#{proyecto.edit}" value="Guardar Proyecto">
                <f:param name="jsfcrud.currentProyecto" value="#{jsfcrud_class['beans.util.JsfUtil'].jsfcrud_method['getAsConvertedString'][proyecto.proyecto][proyecto.converter].jsfcrud_invoke}"/>
            </h:commandButton>
            
            <br />
            <br />
            <h:commandLink value="Volver a listar los proyectos" action="#{proyecto.listSetup()}" immediate="true" />
        </h:form>
        </body>
    </html>
</f:view>
