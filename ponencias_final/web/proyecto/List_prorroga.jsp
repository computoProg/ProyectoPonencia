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
                    </h:commandLink></div></h:form>           <title>Proyecto </title>

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
        <h1>Proyecto </h1>
        <h:form>
            <h:panelGrid columns="2">
                <h:outputText value="Proyectos:" />
                <h:panelGroup>
                    <h:outputText value="Cantidad de proyectos: #{proyecto.num_conProrroga}" title="Nombre" />
                    <h:outputText rendered="#{empty proyecto.proyectos_conProrroga}" value="(No existen eventos)"/>
                    <h:dataTable value="#{proyecto.proyectos_conProrroga}" var="item"
                                 border="0" cellpadding="2" cellspacing="0" rowClasses="jsfcrud_odd_row,jsfcrud_even_row" rules="all" style="border:solid 1px"
                                 rendered="#{not empty proyecto.proyectos_conProrroga}">

                        <h:column>
                            <f:facet name="header">
                                <h:outputText value="ID"/>
                            </f:facet>
                            <h:outputText value="#{item.idproyecto}"/>
                        </h:column>

                        <h:column>
                            <f:facet name="header">
                                <h:outputText value="Nombre"/>
                            </f:facet>
                            <h:outputText value="#{item.nombre}"/>
                        </h:column>
                        <h:column>
                            <f:facet name="header">
                                <h:outputText value="Fecha de Inicio"/>
                            </f:facet>
                            <h:outputText value="#{item.fechainicio}">
                                <f:convertDateTime pattern="d/M/yyyy" />
                            </h:outputText>
                        </h:column>
                        <h:column>
                            <f:facet name="header">
                                <h:outputText value="Fecha Fin"/>
                            </f:facet>
                            <h:outputText value="#{item.fechafin}">
                                <f:convertDateTime pattern="d/M/yyyy" />
                            </h:outputText>
                        </h:column>

                        <h:column>
                            <f:facet name="header">
                                <h:outputText value="Estado"/>
                            </f:facet>
                            <h:outputText value="#{item.estadoIdestado.nombre}"/>
                        </h:column>
                       <%--
                        <h:column>
                            <f:facet name="header">
                                <h:outputText escape="false" value="&nbsp;"/>
                            </f:facet>

                            <h:outputText value=" "/>
                            <h:commandButton value="Editar" action="#{proyecto.editSetup}" image="../editar.png" title="Editar">
                                <f:param name="jsfcrud.currentProyecto" value="#{jsfcrud_class['beans.util.JsfUtil'].jsfcrud_method['getAsConvertedString'][proyecto.proyecto][proyecto.converter].jsfcrud_invoke}"/>
                                <f:param name="jsfcrud.currentEvento" value="#{jsfcrud_class['beans.util.JsfUtil'].jsfcrud_method['getAsConvertedString'][item][evento.converter].jsfcrud_invoke}"/>
                                <f:param name="jsfcrud.relatedController" value="proyecto" />
                                <f:param name="jsfcrud.relatedControllerType" value="beans.ProyectoController" />
                            </h:commandButton>
                            <h:outputText value=" "/>
                            <h:commandButton value="Borrar" action="#{evento.remove}" image="../delete.png" title="Borrar" onclick="return confirmDelete();">
                                <f:param name="jsfcrud.currentProyecto" value="#{jsfcrud_class['beans.util.JsfUtil'].jsfcrud_method['getAsConvertedString'][proyecto.proyecto][proyecto.converter].jsfcrud_invoke}"/>
                                <f:param name="jsfcrud.currentEvento" value="#{jsfcrud_class['beans.util.JsfUtil'].jsfcrud_method['getAsConvertedString'][item][evento.converter].jsfcrud_invoke}"/>
                                <f:param name="jsfcrud.relatedController" value="proyecto" />
                                <f:param name="jsfcrud.relatedControllerType" value="beans.ProyectoController" />
                            </h:commandButton>
</h:column>--%>
                    </h:dataTable>
                </h:panelGroup>

   

            </h:panelGrid>
            <br />

            <br />
            <br />
            <h:commandButton action="proyecto_list" value="Volver" title="Volver" >
                <f:param name="jsfcrud.currentProyecto" value="#{jsfcrud_class['beans.util.JsfUtil'].jsfcrud_method['getAsConvertedString'][proyecto.proyecto][proyecto.converter].jsfcrud_invoke}" />
            </h:commandButton>
            <br />

            <br />
            <br />
            <h:commandLink value="Inicio" action="#{participantes.validaWelcome}" immediate="true" >
             <f:param name="jsfcrud.currentProyecto" value="#{jsfcrud_class['beans.util.JsfUtil'].jsfcrud_method['getAsConvertedString'][proyecto.proyecto][proyecto.converter].jsfcrud_invoke}" />
            </h:commandLink>

        </h:form>
        </body>
    </html>
</f:view>