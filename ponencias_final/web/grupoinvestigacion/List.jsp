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
            <title>Lista de Grupos de Investigación</title>

            <link rel="stylesheet" type="text/css" href="/ponencias/faces/jsfcrud.css" />
        </head>
        <body>
                                              <script  language="javascript">
    function confirmDelete()
    {
        return confirm('Esta seguro que desea deshabilitar el Grupo de Investigación?');
    }
    </script>
        <h:panelGroup id="messagePanel" layout="block">
            <h:messages errorStyle="color: red" infoStyle="color: green" layout="table"/>
        </h:panelGroup>
    
        <h:outputText value="HOME > Gestionar Proyectos"/>
        <h1>Gestionar Grupos de Investigación</h1>
        
        <h:form>
            <h:outputText value="Filtrar grupos de investigación:"/>
            <h:panelGrid columns="2" border="0">
                <h:outputText value="Nombre:" style="font-weight:bold"/>
                <h:inputText id="nombre"
                             maxlength="45"
                             value="#{GrupoInvestigacion.grupoInvestigacionb.nombre}"/>
                
                <h:outputText value="Estado:" style="font-weight:bold"/>
                <h:selectOneMenu value="#{GrupoInvestigacion.grupoInvestigacionb.estado}"
                                 id="estado">
                    <f:selectItem itemValue=""/>
                    <f:selectItem itemValue="Activo"/>
                    <f:selectItem itemValue="No_Activo"/>
                </h:selectOneMenu>
                
                <h:outputText value="Clasificación:" style="font-weight:bold"/>
                <h:selectOneMenu value="#{GrupoInvestigacion.grupoInvestigacionb.clasificacion}"
                                 id="clasificacion">
                    <f:selectItem itemValue=""/>
                    <f:selectItem itemValue="A"/>
                    <f:selectItem itemValue="B"/>
                    <f:selectItem itemValue="C"/>
                </h:selectOneMenu>
                
            </h:panelGrid>
            
            <h:commandButton value= "Aplicar Filtro"
                             action="#{GrupoInvestigacion.busca_grupoI}"/>
        </h:form>
        
        <h:form styleClass="jsfcrud_list_form">
            <h:outputText escape="false" value="(no existen Grupos de Investigacion)<br />" rendered="#{GrupoInvestigacion.pagingInfo.itemCount == 0}" />
            <h:panelGroup rendered="#{GrupoInvestigacion.pagingInfo.itemCount > 0}">
               <h:commandButton action="#{GrupoInvestigacion.prev}" value="Anterior #{GrupoInvestigacion.pagingInfo.batchSize}" rendered="#{GrupoInvestigacion.pagingInfo.firstItem >= GrupoInvestigacion.pagingInfo.batchSize}" image="../ant.png"/>&nbsp;
                <h:outputText value="Grupo #{GrupoInvestigacion.pagingInfo.firstItem + 1}-#{GrupoInvestigacion.pagingInfo.lastItem} de #{GrupoInvestigacion.pagingInfo.itemCount}"/>&nbsp;
                <h:commandButton action="#{GrupoInvestigacion.next}" value="Sig #{GrupoInvestigacion.pagingInfo.batchSize}" rendered="#{GrupoInvestigacion.pagingInfo.lastItem + GrupoInvestigacion.pagingInfo.batchSize <= GrupoInvestigacion.pagingInfo.itemCount}" image="../sig.png"/>&nbsp;
                <h:commandButton action="#{GrupoInvestigacion.next}" value="Siguientes #{GrupoInvestigacion.pagingInfo.itemCount - GrupoInvestigacion.pagingInfo.lastItem} " image="../sig.png"
                               rendered="#{GrupoInvestigacion.pagingInfo.lastItem < GrupoInvestigacion.pagingInfo.itemCount && GrupoInvestigacion.pagingInfo.lastItem + GrupoInvestigacion.pagingInfo.batchSize > GrupoInvestigacion.pagingInfo.itemCount}"/>


                <h:dataTable value="#{GrupoInvestigacion.grupoI_lista}" var="item" border="0" cellpadding="2" cellspacing="0" rowClasses="jsfcrud_odd_row,jsfcrud_even_row" rules="all" style="border:solid 1px">
                    <h:column>
                        <f:facet name="header">
                            <h:outputText value="Grupo Investigacion"/>
                        </f:facet>
                        <h:outputText value="#{item.nombre}"/>
                    </h:column>
                    <h:column>
                        <f:facet name="header">
                            <h:outputText value="Categoría"/>
                        </f:facet>
                        <h:outputText value="#{item.clasificacion}"/>
                    </h:column>                    
                    <h:column>
                        <f:facet name="header">
                            <h:outputText escape="false"
                                          value="&nbsp;"/>
                        </f:facet>                        
                        <h:commandButton value="Detalle" 
                                         title="Detalle del Grupo de Investigación"
                                         action="#{GrupoInvestigacion.detailSetup}">
                            <f:param name="jsfcrud.currentGrupoInvestigacion"
                                     value="#{jsfcrud_class['beans.util.JsfUtil'].jsfcrud_method['getAsConvertedString'][item][GrupoInvestigacion.converter].jsfcrud_invoke}"/>
                        </h:commandButton>
                        <h:outputText value=" "/>
                        
                        <h:outputText value=" "/>
                        <h:commandButton value="Borrar" 
                                         action="#{GrupoInvestigacion.desvincular}"
                                         image="../delete.png"
                                         title="Deshabilitar el Grupo de Investigación"
                                         onclick="return confirmDelete();">
                            <f:param name="jsfcrud.currentGrupoInvestigacion"
                                     value="#{jsfcrud_class['beans.util.JsfUtil'].jsfcrud_method['getAsConvertedString'][item][GrupoInvestigacion.converter].jsfcrud_invoke}"/>
                        </h:commandButton>
                    </h:column>

                </h:dataTable>
            </h:panelGroup>
            <br />
            <h:commandButton action="#{GrupoInvestigacion.createSetup}" value="Nuevo Grupo"/>

        </h:form>
        </body>
    </html>
</f:view>
