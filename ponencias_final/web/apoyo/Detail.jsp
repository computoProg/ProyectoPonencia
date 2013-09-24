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
                    </h:commandLink></div></h:form>           <title>Apoyo </title>

            <link rel="stylesheet" type="text/css" href="/ponencias/faces/jsfcrud.css" />
        </head>
        <body>
             <script  language="javascript">
    function confirmDelete()
    {
        return confirm('¿Esta seguro que desea dehabilitar el apoyo?');
    }
    </script>
        <h:panelGroup id="messagePanel" layout="block">
            <h:messages errorStyle="color: red" infoStyle="color: green" layout="table"/>
        </h:panelGroup>
        
        <h:outputText value="HOME > Gestionar Apoyo - Detalle"/>
            
        <h1>Detalle Apoyo: <h:outputText value="#{apoyo.apoyo.apoyo}"/></h1>
    
        <h:form>
            <h:panelGrid columns="2">
                <h:outputText value="ID"/>
                <h:outputText value="#{apoyo.apoyo.idapoyo}"/>
                
                <h:outputText value="Nombre:" style="font-weight:bold"/>
                <h:outputText value="#{apoyo.apoyo.apoyo}"/>
                
                <h:outputText value="Estado:" style="font-weight:bold"/>
                <h:outputText value="#{apoyo.apoyo.estado}"/>
                
                <h:outputText value="Descripción:" style="font-weight:bold"/>
                <h:outputText value="#{apoyo.apoyo.descripcion}"/>

            </h:panelGrid>

            <br />
            <h:commandButton action="#{apoyo.editSetup}" value="Editar Apoyo" title="Editar el apoyo" >
                <f:param name="jsfcrud.currentApoyo" value="#{jsfcrud_class['beans.util.JsfUtil'].jsfcrud_method['getAsConvertedString'][apoyo.apoyo][apoyo.converter].jsfcrud_invoke}" />
            </h:commandButton>


            <h:commandButton value="Deshabilitar Apoyo" 
                             action="#{apoyo.desvincular}"
                             onclick="return confirmDelete();">
             <f:param name="jsfcrud.currentApoyo" value="#{jsfcrud_class['beans.util.JsfUtil'].jsfcrud_method['getAsConvertedString'][apoyo.apoyo][apoyo.converter].jsfcrud_invoke}" />
            </h:commandButton>
            
            <br />
            <br />
            <h:commandLink value="Volver a listar los Apoyos" action="#{apoyo.listSetup()}" immediate="true" />
        </h:form>
        </body>
    </html>
</f:view>
