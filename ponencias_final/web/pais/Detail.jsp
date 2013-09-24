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
            <title>Departamentos</title>

            <link rel="stylesheet" type="text/css" href="/ponencias/faces/jsfcrud.css" />
        </head>
        <body>
             <script  language="javascript">
    function confirmDelete()
    {
        return confirm('Esta seguro que desea borrar el participante?');
    }
    </script>
        <h:panelGroup id="messagePanel" layout="block">
            <h:messages errorStyle="color: red" infoStyle="color: green" layout="table"/>
        </h:panelGroup>
        <h1>Departamentos</h1>
        <h:form>
            <h:panelGrid columns="2">

                <h:outputText value="País:"/>
                <h:outputText value="#{Pais.pais.nombre}" title="Pais" />

                <h:outputText value="Departamentos:" />
                <h:panelGroup>
                    <h:outputText value="Cantidad de Departamentos: #{Pais.num_departamentos}" title="Departamentos" />
                    <h:outputText rendered="#{empty Pais.departamentos_pais}" value="(no existen departamentos asociadas a este Pais)"/>
                    <h:dataTable value="#{Pais.departamentos_pais}" var="item"
                                 border="0" cellpadding="2" cellspacing="0" rowClasses="jsfcrud_odd_row,jsfcrud_even_row" rules="all" style="border:solid 1px"
                                 rendered="#{not empty Pais.departamentos_pais}">
                        <h:column>
                            <f:facet name="header">
                                <h:outputText value="Nombre"/>
                            </f:facet>
                            <h:outputText value="#{item.nombre}"/>
                        </h:column>

                        <h:column>
                            <f:facet name="header">
                                <h:outputText escape="false" value="&nbsp;"/>
                            </f:facet>
                            <h:outputText value=" "/>
                            <h:commandButton value="Editar" action="#{Departamento.editSetup}" image="../editar.png" title="Editar">
                                <f:param name="jsfcrud.currentPais" value="#{jsfcrud_class['beans.util.JsfUtil'].jsfcrud_method['getAsConvertedString'][Pais.pais][Pais.converter].jsfcrud_invoke}"/>
                                <f:param name="jsfcrud.currentDepartamento" value="#{jsfcrud_class['beans.util.JsfUtil'].jsfcrud_method['getAsConvertedString'][item][Departamento.converter].jsfcrud_invoke}"/>
                                <f:param name="jsfcrud.relatedController" value="Pais" />
                                <f:param name="jsfcrud.relatedControllerType" value="beans.PaisController" />
                            </h:commandButton>
                            <h:outputText value=" "/>
                            <h:commandButton value="Borrar" action="#{Departamento.remove}" image="../delete.png" title="Borrar" onclick="return confirmDelete();">
                                <f:param name="jsfcrud.currentPais" value="#{jsfcrud_class['beans.util.JsfUtil'].jsfcrud_method['getAsConvertedString'][Pais.pais][Pais.converter].jsfcrud_invoke}"/>
                                <f:param name="jsfcrud.currentDepartamento" value="#{jsfcrud_class['beans.util.JsfUtil'].jsfcrud_method['getAsConvertedString'][item][Departamento.converter].jsfcrud_invoke}"/>
                                <f:param name="jsfcrud.relatedController" value="Pais" />
                                <f:param name="jsfcrud.relatedControllerType" value="beans.PaisController" />
                            </h:commandButton>
                        </h:column>
                    </h:dataTable>
                </h:panelGroup>

            </h:panelGrid>
            <br />

            <br />
            <br />
            <h:commandButton action="pais_list" value="Volver">
                <f:param name="jsfcrud.currentPais" value="#{jsfcrud_class['beans.util.JsfUtil'].jsfcrud_method['getAsConvertedString'][Pais.pais][Pais.converter].jsfcrud_invoke}" />
            </h:commandButton>
            <br />

            <br />
            <br />
            <h:commandLink value="Inicio" action="welcome_admin" immediate="true" >
            <f:param name="jsfcrud.currentPais" value="#{jsfcrud_class['beans.util.JsfUtil'].jsfcrud_method['getAsConvertedString'][Pais.pais][Pais.converter].jsfcrud_invoke}" />
            </h:commandLink>
        </h:form>
        </body>
    </html>
</f:view>

