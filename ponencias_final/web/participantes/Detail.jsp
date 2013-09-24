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
            <title>Detalle Participante</title>

            <link rel="stylesheet" type="text/css" href="/ponencias/faces/jsfcrud.css" />
        </head>
        <body>
     <script  language="javascript">
            function confirmDelete()
            {
                return confirm('Esta seguro que desea desvincular el participante?');
            }
    </script>
        <h:panelGroup id="messagePanel" layout="block">
            <h:messages errorStyle="color: red" infoStyle="color: green" layout="table"/>
        </h:panelGroup>
    <h1>Detalle Participante: <h:outputText value="#{participantes.participantes.nombre}"/></h1>
        <h:form>
            <h:panelGrid columns="2" >
                <h:outputText value="Cédula:" style="font-weight: bold"/>
                <h:outputText value="#{participantes.participantes.cedula}" title="Cedula" />
                
                <h:outputText value="Nombre:" style="font-weight: bold"/>
                <h:outputText value="#{participantes.participantes.nombre}" title="Nombre" />
                
                <h:outputText value="Apellido:" style="font-weight: bold"/>
                <h:outputText value="#{participantes.participantes.apellido}"/>
                
                <h:outputText value="E-mail:" style="font-weight: bold"/>
                <h:outputText value="#{participantes.participantes.email}"/> 
                
                <h:outputText value="Vinculacion:" style="font-weight: bold"/>
                <h:outputText value="#{participantes.participantes.vinculacion.vinculacion}"/>
                
                <h:outputText value="Rol:" style="font-weight: bold"/>
                <h:outputText value="#{participantes.participantes.rol.rol}"/>
                
                <h:outputText value="Grupo de Investigación:" style="font-weight: bold"/>
                <h:outputText value="#{participantes.participantes.grupoInvestigacionidGrupoInvestigacion.nombre}"/>

                <h:outputText value="Estado:" style="font-weight: bold"/>
                <h:outputText value="#{participantes.participantes.estado}"/>
            </h:panelGrid>
          
            <br />
            <h:commandButton action="#{participantes.editSetup}" value="Editar Participante">
                <f:param name="jsfcrud.currentParticipantes" value="#{jsfcrud_class['beans.util.JsfUtil'].jsfcrud_method['getAsConvertedString'][participantes.participantes][participantes.converter].jsfcrud_invoke}" />
            </h:commandButton>
            
            <br />
            <br />
            <h:commandLink value="Volver a listar los participantes" action="#{participantes.listSetup()}" immediate="true"
                           rendered="#{participantes.p.rol.idrol == 1}"/>            
        </h:form>
        </body>
    </html>
</f:view>
