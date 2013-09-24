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
            <title>Rol</title>

            <link rel="stylesheet" type="text/css" href="/ponencias/faces/jsfcrud.css" />
        </head>
        <body>
             <script  language="javascript">
    function confirmDelete()
    {
        return confirm('Esta seguro que desea borrar el rol?');
    }
    </script>
        <h:panelGroup id="messagePanel" layout="block">
            <h:messages errorStyle="color: red" infoStyle="color: green" layout="table"/>
        </h:panelGroup>
        <h1>Rol</h1>
        <h:form>
            <h:panelGrid columns="2">
               <%-- <h:outputText value="Idrol:"/>
<h:outputText value="#{rol.rol.idrol}" title="Idrol" />--%>
                <h:outputText value="Rol:"/>
                <h:outputText value="#{rol.rol.rol}" title="Rol" />

                <h:outputText value="Participantes asociados:" />
                <h:panelGroup>
                    <h:outputText value="cantidad de participantes: #{rol.num_participantes}" title="Rol" />
                    <h:outputText rendered="#{empty rol.participantesRol}" value="(no existen participantes)"/>
                    <h:dataTable value="#{rol.participantesRol}" var="item"
                                 border="0" cellpadding="2" cellspacing="0" rowClasses="jsfcrud_odd_row,jsfcrud_even_row" rules="all" style="border:solid 1px" 
                                 rendered="#{not empty rol.participantesRol}">
                        <h:column>
                            <f:facet name="header">
                                <h:outputText value="Cedula"/>
                            </f:facet>
                            <h:outputText value="#{item.cedula}"/>
                        </h:column>
                        <h:column>
                            <f:facet name="header">
                                <h:outputText value="Nombre"/>
                            </f:facet>
                            <h:outputText value="#{item.nombre}"/>
                        </h:column>
                        <h:column>
                            <f:facet name="header">
                                <h:outputText value="Email"/>
                            </f:facet>
                            <h:outputText value="#{item.email}"/>
                        </h:column>
                       <%-- <h:column>
                            <f:facet name="header">
                                <h:outputText value="Contraseña"/>
                            </f:facet>
                            <h:outputText value="#{item.contraseña}"/>
                        </h:column>
                        <h:column>
                            <f:facet name="header">
                                <h:outputText value="Contraseñav"/>
                            </f:facet>
                            <h:outputText value="#{item.contraseñav}"/>
                        </h:column>--%>
                        <h:column>
                            <f:facet name="header">
                                <h:outputText value="Vinculacion"/>
                            </f:facet>
                            <h:outputText value="#{item.vinculacion.vinculacion}"/>
                        </h:column>
                        <h:column>
                            <f:facet name="header">
                                <h:outputText value="Rol"/>
                            </f:facet>
                            <h:outputText value="#{item.rol.rol}"/>
                        </h:column>
                        <h:column>
                            <f:facet name="header">
                                <h:outputText value="Apoyo"/>
                            </f:facet>
                            <h:outputText value="#{item.apoyo.apoyo}"/>
                        </h:column>
                        <h:column>
                            <f:facet name="header">
                                <h:outputText escape="false" value="&nbsp;"/>
                            </f:facet>
                           <%-- <h:commandLink value="Mostrar" action="#{participantes.detailSetup}">
                                <f:param name="jsfcrud.currentRol" value="#{jsfcrud_class['beans.util.JsfUtil'].jsfcrud_method['getAsConvertedString'][rol.rol][rol.converter].jsfcrud_invoke}"/>
                                <f:param name="jsfcrud.currentParticipantes" value="#{jsfcrud_class['beans.util.JsfUtil'].jsfcrud_method['getAsConvertedString'][item][participantes.converter].jsfcrud_invoke}"/>
                                <f:param name="jsfcrud.relatedController" value="rol" />
                                <f:param name="jsfcrud.relatedControllerType" value="beans.RolController" />
                            </h:commandLink>--%>
                            <h:outputText value=" "/>
                            <h:commandButton value="Editar" action="#{participantes.editSetup}" image="../images/editar.png" title="Editar">
                                <f:param name="jsfcrud.currentRol" value="#{jsfcrud_class['beans.util.JsfUtil'].jsfcrud_method['getAsConvertedString'][rol.rol][rol.converter].jsfcrud_invoke}"/>
                                <f:param name="jsfcrud.currentParticipantes" value="#{jsfcrud_class['beans.util.JsfUtil'].jsfcrud_method['getAsConvertedString'][item][participantes.converter].jsfcrud_invoke}"/>
                                <f:param name="jsfcrud.relatedController" value="rol" />
                                <f:param name="jsfcrud.relatedControllerType" value="beans.RolController" />
                            </h:commandButton>
                            <h:outputText value=" "/>
                            <h:commandButton value="Borrar" action="#{participantes.remove}" image="../images/delete.png" title="Borrar" onclick="return confirmDelete();">
                                <f:param name="jsfcrud.currentRol" value="#{jsfcrud_class['beans.util.JsfUtil'].jsfcrud_method['getAsConvertedString'][rol.rol][rol.converter].jsfcrud_invoke}"/>
                                <f:param name="jsfcrud.currentParticipantes" value="#{jsfcrud_class['beans.util.JsfUtil'].jsfcrud_method['getAsConvertedString'][item][participantes.converter].jsfcrud_invoke}"/>
                                <f:param name="jsfcrud.relatedController" value="rol" />
                                <f:param name="jsfcrud.relatedControllerType" value="beans.RolController" />
                            </h:commandButton>
                        </h:column>
                    </h:dataTable>
                </h:panelGroup>

            </h:panelGrid>
            <br />
           <%-- <h:commandButton action="#{rol.remove}" value="Borrar">
                <f:param name="jsfcrud.currentRol" value="#{jsfcrud_class['beans.util.JsfUtil'].jsfcrud_method['getAsConvertedString'][rol.rol][rol.converter].jsfcrud_invoke}" />
            </h:commandButton>--%>
            <br />
            <br />
            <h:commandButton action="rol_list" value="Volver">
                <f:param name="jsfcrud.currentRol" value="#{jsfcrud_class['beans.util.JsfUtil'].jsfcrud_method['getAsConvertedString'][rol.rol][rol.converter].jsfcrud_invoke}" />
            </h:commandButton>
            <br />
           
           <%-- <h:commandButton action="#{rol.listSetup}" value="Mostrar roles"/>--%>
            <br />
            <br />
            <h:commandLink value="Inicio" action="welcome_admin" immediate="true" >
            <f:param name="jsfcrud.currentRol" value="#{jsfcrud_class['beans.util.JsfUtil'].jsfcrud_method['getAsConvertedString'][rol.rol][rol.converter].jsfcrud_invoke}" />
            </h:commandLink>
        </h:form>
        </body>
    </html>
</f:view>
