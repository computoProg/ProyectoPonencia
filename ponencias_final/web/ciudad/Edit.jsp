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
            <title>Editar Ciudad</title>

            <link rel="stylesheet" type="text/css" href="/ponencias/faces/jsfcrud.css" />
        </head>
        <body>
        <h:panelGroup id="messagePanel" layout="block">
            <h:messages errorStyle="color: red" infoStyle="color: green" layout="table"/>
        </h:panelGroup>
        <h1>Editar Ciudad</h1>
        <h:form>
            <h:panelGrid columns="2">
                <h:outputText value="IdCiudad:"/>
                <h:outputText value="#{Ciudad.ciudad.idCiudad}"
                              title="IdCiudad" />
                <h:outputText value="Ciudad:"/>
                <h:inputText id="apoyo"
                             maxlength="45"
                             value="#{Ciudad.ciudad.nombre}"
                             title="Ciudad" required="true"
                             requiredMessage="The ciudad field is required." />
                <%--<h:outputText value="Departamento:"/>
                <h:selectOneMenu id="departamento"
                                 value="#{Ciudad.ciudad.departamentoidDepartamento}"
                                 title = "Seleccione el departamento"
                                 required = "true"
                                 requiredMessage = "The Departamento field is required">

                    <f:selectItems value="#{Departamento.departamentoItemsAvailableSelectOne}"/>
                </h:selectOneMenu> 
                --%>
               

            </h:panelGrid>
            <br />
            <h:commandButton action="#{Ciudad.edit}"
                             value="Guardar">
                <f:param name="jsfcrud.currentCiudad"
                         value="#{jsfcrud_class['beans.util.JsfUtil'].jsfcrud_method['getAsConvertedString'][Ciudad.ciudad][Ciudad.converter].jsfcrud_invoke}"/>
            </h:commandButton>
            <br />

            <h:commandButton action="#{Ciudad.listSetup}" 
                             value="Mostrar Ciudades"
                             immediate="true"/>
            <br />
            <br />
            <h:commandButton value="Inicio" 
                             action="welcome_admin"
                             immediate="true" />
        </h:form>
        </body>
    </html>
</f:view>
