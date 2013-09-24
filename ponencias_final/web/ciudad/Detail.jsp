<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<f:view>
    <html>
            <div width:800px ; margin: 4px auto>

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
            <title>Alcance </title>

            <link rel="stylesheet" type="text/css" href="/ponencias/faces/jsfcrud.css" />
        </head>
        <body>
             <script  language="javascript">
    function confirmDelete()
    {
        return confirm('Esta seguro que desea borrar este Alcance?');
    }
    </script>
        <h:panelGroup id="messagePanel" layout="block">
            <h:messages errorStyle="color: red" infoStyle="color: green" layout="table"/>
        </h:panelGroup>
        <h1>Alcance</h1>
        <h:form>
            <h:panelGrid columns="2">
               <%-- <h:outputText value="Idalcance:"/>
                <h:outputText value="#{alcance.alcance.idalcance}" title="Idalcance" />--%>
                <h:outputText value="Ciudad:"/>
                <h:outputText value="#{Ciudad.ciudad.nombre}" title="Ciudad" />

                <h:outputText value="Eventos asociados:" />
                <h:panelGroup>
                    <h:outputText value="Cantidad de eventos: #{Ciudad.num_eventos}" title="Ciudad" />
                    <h:outputText rendered="#{empty Ciudad.eventos_ciudad}" value="no existen eventos"/>
                    <h:dataTable value="#{Ciudad.eventos_ciudad}" var="item"
                                 border="0" cellpadding="2" cellspacing="0" rowClasses="jsfcrud_odd_row,jsfcrud_even_row" rules="all" style="border:solid 1px"
                                 rendered="#{not empty Ciudad.eventos_ciudad}">

                        <h:column>
                            <f:facet name="header">
                                <h:outputText value="Nombre"/>
                            </f:facet>
                            <h:outputText value="#{item.nombre}"/>
                        </h:column>

                        <h:column>
                            <f:facet name="header">
                                <h:outputText value="FechaInicial"/>
                            </f:facet>
                            <h:outputText value="#{item.fechaInicial}">
                                <f:convertDateTime pattern="d/M/yyyy" />
                            </h:outputText>
                        </h:column>

                        <h:column>
                            <f:facet name="header">
                                <h:outputText value="Proyecto"/>
                            </f:facet>
                            <h:outputText value="#{item.proyecto.nombre}"/>
                        </h:column>

                        <h:column>
                            <f:facet name="header">
                                <h:outputText value="Alcance"/>
                            </f:facet>
                            <h:outputText value="#{item.alcance.alcance}"/>
                        </h:column>
                        <h:column>
                            <f:facet name="header">
                                <h:outputText escape="false" value="&nbsp;"/>
                            </f:facet>

                            <h:outputText value=" "/>
                            <h:commandButton value="Editar" action="#{evento.editSetup}" image="../editar.png" title="Editar">
                                <f:param name="jsfcrud.currentCiudad" value="#{jsfcrud_class['beans.util.JsfUtil'].jsfcrud_method['getAsConvertedString'][Ciudad.ciudad][Ciudad.converter].jsfcrud_invoke}"/>
                                <f:param name="jsfcrud.currentEvento" value="#{jsfcrud_class['beans.util.JsfUtil'].jsfcrud_method['getAsConvertedString'][item][evento.converter].jsfcrud_invoke}"/>
                                <f:param name="jsfcrud.relatedController" value="ciudad" />                            <f:param name="jsfcrud.relatedControllerType" value="beans.AlcanceController" />
                            </h:commandButton>
                            <h:outputText value=" "/>
                            <h:commandButton value="Borrar" action="#{evento.remove}" image="../delete.png" title="Borrar" onclick="return confirmDelete();">
                                <f:param name="jsfcrud.currentCiudad" value="#{jsfcrud_class['beans.util.JsfUtil'].jsfcrud_method['getAsConvertedString'][Ciudad.ciudad][Ciudad.converter].jsfcrud_invoke}"/>
                                <f:param name="jsfcrud.currentEvento" value="#{jsfcrud_class['beans.util.JsfUtil'].jsfcrud_method['getAsConvertedString'][item][evento.converter].jsfcrud_invoke}"/>
                                <f:param name="jsfcrud.relatedController" value="ciudad" />
                                <f:param name="jsfcrud.relatedControllerType" value="beans.CiudadController" />
                            </h:commandButton>
                        </h:column>
                    </h:dataTable>
                </h:panelGroup>

            </h:panelGrid>
            <br />

            <br />
            <br />
            <h:commandButton action="ciudad_list" value="Volver"  title="Volver">
                <f:param name="jsfcrud.currentCiudad" value="#{jsfcrud_class['beans.util.JsfUtil'].jsfcrud_method['getAsConvertedString'][Ciudad.ciudad][Ciudad.converter].jsfcrud_invoke}" />
            </h:commandButton>
            <br />

           <%-- <h:commandButton action="#{alcance.listSetup}" value="Mostrar alcance"/>--%>
            <br />
            <br />
            <h:commandLink value="Inicio" action="welcome_admin" immediate="true" >
                 <f:param name="jsfcrud.currentCiudad" value="#{jsfcrud_class['beans.util.JsfUtil'].jsfcrud_method['getAsConvertedString'][Ciudad.ciudad][Ciudad.converter].jsfcrud_invoke}" />
            </h:commandLink>
        </h:form>

        <div></body>
    </html>

</f:view>
