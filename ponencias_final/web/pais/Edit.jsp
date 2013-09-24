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
            <title>Editar Pais</title>

            <link rel="stylesheet" type="text/css" href="/ponencias/faces/jsfcrud.css" />
        </head>
        <body>
        <h:panelGroup id="messagePanel" layout="block">
            <h:messages errorStyle="color: red" infoStyle="color: green" layout="table"/>
        </h:panelGroup>
        <h1>Editar Pais</h1>
        <h:form>
            <h:panelGrid columns="2">
                <h:outputText value="Id:"/>
                <h:outputText value="#{Pais.pais.idPais}"
                              title="IdPais" />
                <h:outputText value="Pais:"/>
                <h:inputText id="pais"
                             value="#{Pais.pais.nombre}"
                             title="Pais"
                             required="true"
                             requiredMessage="The País field is required." />



            </h:panelGrid>
            <br />
            <h:commandButton action="#{Pais.edit}"
                             value="Guardar">
                <f:param name="jsfcrud.currentPais"
                         value="#{jsfcrud_class['beans.util.JsfUtil'].jsfcrud_method['getAsConvertedString'][Pais.pais][Pais.converter].jsfcrud_invoke}"/>
            </h:commandButton>
            <br />

            <h:commandButton action="#{Pais.listSetup}"
                             value="Mostrar Paises"
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
