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
            <title>Lista proyectos</title>

            <link rel="stylesheet" type="text/css" href="/ponencias/faces/jsfcrud.css" />
        </head>
        <body>
                                              <script  language="javascript">
    function confirmDelete()
    {
        return confirm('Esta seguro que desea borrar el proyecto?');
    }
    </script>
        <h:panelGroup id="messagePanel" layout="block">
            <h:messages errorStyle="color: red" infoStyle="color: green" layout="table"/>
        </h:panelGroup>
    
    <h:outputText value="HOME > Gestionar Proyectos"/>
    
        <h1>Gestionar Proyectos</h1>
        <h:form>
        <h:outputText value="Filtrar proyectos:"/>
        <h:panelGrid columns="2" border="0">
            <%--
            <h:outputText value="Participante:" style = "font-weight:bold"/>
            <h:inputText value="#{proyecto.participanteb.cedula}"
                         id="participante">
                <f:convertNumber integerOnly="true"/>
            </h:inputText>
            --%>
            
            <h:outputText value="Proyecto:" style="font-weight:bold"/>
            <h:inputText value="#{proyecto.proyectob.nombre}"
                         id="nombre"
                         title="Nombre del proyecto"/>
            
            <h:outputText value="Estado" style="font-weight:bold"/>
            <h:selectOneMenu value="#{proyecto.proyectob.estado}"
                             id="estado">
                <f:selectItem itemValue="---" />
                <f:selectItem itemValue="Activo"/>
                <f:selectItem itemValue="No_Activo"/>
            </h:selectOneMenu>
            </h:panelGrid>
            
            <h:commandButton value="Aplicar Filtro"
                             action="#{proyecto.busca_proyecto}"/>             
        </h:form>
        
        <br/>
        <h:form styleClass="jsfcrud_list_form">
            <h:outputText escape="false" value="(No existen proyectos)<br />" rendered="#{proyecto.pagingInfo.itemCount == 0}" />
            <h:panelGroup rendered="#{proyecto.pagingInfo.itemCount > 0}">
                 <h:commandButton action="#{proyecto.prev}" value="Anterior #{proyecto.pagingInfo.batchSize}" rendered="#{proyecto.pagingInfo.firstItem >= proyecto.pagingInfo.batchSize}" image="../ant.png"/>&nbsp;
                <h:outputText value="proyecto #{proyecto.pagingInfo.firstItem + 1}-#{proyecto.pagingInfo.lastItem} de #{proyecto.pagingInfo.itemCount}"/>&nbsp;
                <h:commandButton action="#{proyecto.next}" value="Sig #{proyecto.pagingInfo.batchSize}" rendered="#{proyecto.pagingInfo.lastItem + proyecto.pagingInfo.batchSize <= proyecto.pagingInfo.itemCount}" image="../sig.png"/>&nbsp;
                <h:commandButton action="#{proyecto.next}" value="Siguientes #{proyecto.pagingInfo.itemCount - proyecto.pagingInfo.lastItem} " image="../sig.png"
                               rendered="#{proyecto.pagingInfo.lastItem < proyecto.pagingInfo.itemCount && proyecto.pagingInfo.lastItem + proyecto.pagingInfo.batchSize > proyecto.pagingInfo.itemCount}"/>
                <h:dataTable value="#{proyecto.proyectos_lista}" var="item" border="0" cellpadding="2" cellspacing="0" rowClasses="jsfcrud_odd_row,jsfcrud_even_row" rules="all" style="border:solid 1px">

                    <h:column>
                        <f:facet name="header">
                            <h:outputText value="Nombre"/>
                        </f:facet>
                        <h:outputText value="#{item.nombre}"/>
                    </h:column>

                    <h:column>
                        <f:facet name="header">
                            <h:outputText value="Estado"/>
                        </f:facet>
                        <h:outputText value="#{item.estado}"/>
                    </h:column>
                    <h:column>
                        <f:facet name="header">
                            <h:outputText escape="false"
                                          value="&nbsp;"/>
                        </f:facet>                        
                        <h:commandButton value="Detalle" 
                                         title="Detalle del proyecto"
                                         action="#{proyecto.detailSetup}">
                            <f:param name="jsfcrud.currentProyecto"
                                     value="#{jsfcrud_class['beans.util.JsfUtil'].jsfcrud_method['getAsConvertedString'][item][proyecto.converter].jsfcrud_invoke}"/>
                        </h:commandButton>
                        <h:outputText value=" "/>
                        
                        <h:outputText value=" "/>
                        <h:commandButton value="Borrar" 
                                         action="#{proyecto.desvincular}"
                                         image="../delete.png"
                                         title="Deshabilitar el proyecto"
                                         onclick="return confirmDelete();">
                            <f:param name="jsfcrud.currentProyecto"
                                     value="#{jsfcrud_class['beans.util.JsfUtil'].jsfcrud_method['getAsConvertedString'][item][proyecto.converter].jsfcrud_invoke}"/>
                        </h:commandButton>
                    </h:column>

                </h:dataTable>    

            </h:panelGroup>

            <br />
            <h:commandButton action="#{proyecto.createSetup}"
                             value="Nuevo proyecto"/>

        </h:form>
        </body>
    </html>
</f:view>
