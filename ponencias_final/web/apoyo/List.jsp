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
            <title>Lista de apoyos</title>

            <link rel="stylesheet" type="text/css" href="/ponencias/faces/jsfcrud.css" />
        </head>
        <body>
                                  <script  language="javascript">
    function confirmDelete()
    {
        return confirm('Esta seguro que desea borrar el apoyo?');
    }
    </script>
        <h:panelGroup id="messagePanel" layout="block">
            <h:messages errorStyle="color: red" infoStyle="color: green" layout="table"/>
        </h:panelGroup>
    
    <h:outputText value="HOME > Gestionar Apoyos"/>
    
        <h1>Gestionar Apoyos</h1>
        <h:form>
        <h:outputText value="Filtrar Apoyos:"/>
        <h:panelGrid columns="2" border="0">            
            
            <h:outputText value="Apoyo:" style="font-weight:bold"/>
            <h:inputText value="#{apoyo.apoyo.apoyo}"
                         id="apoyo"
                         title="Nombre del Apoyo"/>
            
            <h:outputText value="Estado" style="font-weight:bold"/>
            <h:selectOneMenu value="#{apoyo.apoyo.estado}"
                             id="estado">
                <f:selectItem itemValue="---" />
                <f:selectItem itemValue="Activo"/>
                <f:selectItem itemValue="No_Activo"/>
            </h:selectOneMenu>
            </h:panelGrid>
            
            <h:commandButton value="Aplicar Filtro"
                             action="#{apoyo.busca_apoyo}"/>             
        </h:form>
        
        <br/>
    
        <h1>Lista de apoyos</h1>
        <h:form styleClass="jsfcrud_list_form">
            <h:outputText escape="false" value="(no existen apoyos)<br />" rendered="#{apoyo.pagingInfo.itemCount == 0}" />
            <h:panelGroup rendered="#{apoyo.pagingInfo.itemCount > 0}">
                <h:commandButton action="#{apoyo.prev}" value="Anterior #{apoyo.pagingInfo.batchSize}" rendered="#{apoyo.pagingInfo.firstItem >= apoyo.pagingInfo.batchSize}" image="../ant.png"/>&nbsp;
                <h:outputText value="apoyo #{apoyo.pagingInfo.firstItem + 1}-#{apoyo.pagingInfo.lastItem} de #{apoyo.pagingInfo.itemCount}"/>&nbsp;
                <h:commandButton action="#{apoyo.next}" value="Sig #{apoyo.pagingInfo.batchSize}" rendered="#{apoyo.pagingInfo.lastItem + apoyo.pagingInfo.batchSize <= apoyo.pagingInfo.itemCount}" image="../sig.png"/>&nbsp;
                <h:commandButton action="#{apoyo.next}" value="Siguientes #{apoyo.pagingInfo.itemCount - apoyo.pagingInfo.lastItem} " image="../sig.png"
                               rendered="#{apoyo.pagingInfo.lastItem < apoyo.pagingInfo.itemCount && apoyo.pagingInfo.lastItem + apoyo.pagingInfo.batchSize > apoyo.pagingInfo.itemCount}"/>
                <h:dataTable value="#{apoyo.apoyoItems}" var="item" border="0" cellpadding="2" cellspacing="0" rowClasses="jsfcrud_odd_row,jsfcrud_even_row" rules="all" style="border:solid 1px">
                   
                    <h:column>
                        <f:facet name="header">
                            <h:outputText value="Apoyo"/>
                        </f:facet>
                        <h:outputText value="#{item.apoyo}"/>
                    </h:column>
                    <h:column>
                        <f:facet name="header">
                            <h:outputText value="Estado"/>
                        </f:facet>
                        <h:outputText value="#{item.estado}"/>
                    </h:column>                    
                    <h:column>
                        <f:facet name="header">
                            <h:outputText escape="false" value="&nbsp;"/>
                        </f:facet>
                        <h:commandButton value="Detalle" title="Detalles del apoyo" action="#{apoyo.detailSetup}">
                                <f:param name="jsfcrud.currentApoyo" value="#{jsfcrud_class['beans.util.JsfUtil'].jsfcrud_method['getAsConvertedString'][item][apoyo.converter].jsfcrud_invoke}"/>
                        </h:commandButton>
                        
  
                        <h:outputText value=" "/>
                        <h:commandButton value="Deshabilitar" action="#{apoyo.desvincular}" image="../delete.png" title="Deshabilitar el apoyo" onclick="return confirmDelete();">
                            <f:param name="jsfcrud.currentApoyo" value="#{jsfcrud_class['beans.util.JsfUtil'].jsfcrud_method['getAsConvertedString'][item][apoyo.converter].jsfcrud_invoke}"/>
                        </h:commandButton>
                    </h:column>

                </h:dataTable>
            </h:panelGroup>
            <br />
            <h:commandButton action="#{apoyo.createSetup}" value="Nuevo apoyo"/>

        </h:form>
        </body>
    </html>
</f:view>
