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
                <title>Editar Grupo Investigacion</title>

            <link rel="stylesheet" type="text/css" href="/ponencias/faces/jsfcrud.css" />
        </head>
        <body>
            <h:panelGroup id="messagePanel" layout="block">
            <h:messages errorStyle="color: red" infoStyle="color: green" layout="table"/>
        </h:panelGroup>
        <h1>Editar Grupo de Investigación</h1>
        <h:form>
            <h:panelGrid columns="2">
                <h:outputText value="Idapoyo:" style="font-weight:bold"/>
                <h:outputText value="#{GrupoInvestigacion.grupoInvestigacion.idGrupoInvestigacion}" 
                              title="Idapoyo" />
                
                <h:outputText value="Nombre:" style="font-weight:bold"/> 
                <h:inputText id="grupoInvestigacion" 
                             maxlength="45"
                             value="#{GrupoInvestigacion.grupoInvestigacion.nombre}" 
                             title="Grupo de Investigacion" 
                             required="true" 
                             requiredMessage="El nombre es obligatorio" />
                
                <%--Agregar limite a la descripción--%>
                
                <h:outputText value="Descripcion(600 caracteres):" style="font-weight:bold"/>
                 <h:inputTextarea label="Descripción del grupo de investigación" id="descripciongrupo"
                                  value="#{GrupoInvestigacion.grupoInvestigacion.descripcion}" 
                                  title="Descripción del grupo de investigación (solo se permiten 600 caracteres)" >
                     <f:validateLength maximum="600"/>
                 </h:inputTextarea>
                 
                <h:outputText value="Clasificación:" style="font-weight:bold"/>
                <h:selectOneMenu value="#{GrupoInvestigacion.grupoInvestigacion.clasificacion}">
                    <f:selectItem itemValue="A" />
                    <f:selectItem itemValue="B" />
                    <f:selectItem itemValue="C" />
                </h:selectOneMenu>
                
                <h:outputText value="Estado:" style="font-weight:bold"/>
                <h:selectOneMenu value="#{GrupoInvestigacion.grupoInvestigacion.estado}">
                    <f:selectItem itemValue="Activo" />
                    <f:selectItem itemValue="No_Activo" />
                </h:selectOneMenu>
                </h:panelGrid>
            <br />

            <h:commandButton action="#{GrupoInvestigacion.edit}" value="Guardar">
                <f:param name="jsfcrud.currentGrupoInvestigacion" value="#{jsfcrud_class['beans.util.JsfUtil'].jsfcrud_method['getAsConvertedString'][GrupoInvestigacion.grupoInvestigacion][GrupoInvestigacion.converter].jsfcrud_invoke}"/>
            </h:commandButton>
            
            <br />
            <br />            
            <h:commandLink value="Volver a listar Grupos de Investigación" action="#{GrupoInvestigacion.listSetup}" immediate="true" />
            </h:form>
            <br />
        </body>
    </html>
 </f:view>