<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<f:view>
    <html>
        <head>
            <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>  <h:form>
                <h:commandLink title="volver al inicio" action="#{participantes.validaWelcome}">
                    <h:graphicImage value="../Home.png"/><h:outputText value="inicio"/>
                </h:commandLink> | <h:commandLink  title="cambiar contraseña" action="#{participantes.go_change}">
                <h:graphicImage value="../clave.png"/>           <Strong><h:outputText value="Cambiar contraseña"/>
                </Strong> </h:commandLink> | <h:commandLink  title="Usuario" action="#{participantes.editSetup}">
                <h:graphicImage value="../user.png"/>           <Strong><h:outputText value="#{user.nombre}"/>
                </Strong> </h:commandLink> |<h:outputText value=" #{participantes.fecha}"/> <div align="right">
                    <h:commandLink action="#{participantes.logout}" value="(salir)">
                        <h:graphicImage value="../exit.png"/>
                    </h:commandLink></div></h:form> <title>Listado de participantes</title>

            <link rel="stylesheet" type="text/css" href="/ponencias/faces/jsfcrud.css" />
        </head>
        <body>
                                              <script  language="javascript">
    function confirmDelete()
    {
        return confirm('¿Esta seguro que desea desvincular el participante?');
    }
    </script>
        <h:panelGroup id="messagePanel" layout="block">
            <h:messages errorStyle="color: red" infoStyle="color: green" layout="table"/>
        </h:panelGroup>
        <h:outputText value="HOME > Gestionar Participantes"/>
        <br/>
        <h1>Gestionar Participantes</h1>
        <h:form >
            <h:outputText value="Filtrar Participantes"/>
            <br>
            <h:panelGrid columns="2" border="0">
                <h:outputText value="Cédula:" style="font-weight:bold"/>
                <h:inputText id="cedula" 
                             maxlength="11"
                             value="#{participantes.participantesb.cedula}" 
                             title="cedula" />
                
                <h:outputText value="Nombre" style="font-weight:bold"/>
                <h:inputText maxlength="45" value="#{participantes.participantesb.nombre}"
                             id="nombre"
                             title="Nombre del participante"
                             size ="11"/>
                
                <h:outputText value="Vinculación:" style="font-weight:bold"/>
                <h:selectOneMenu   id="vinculacion" 
                                   value="#{participantes.participantesb.vinculacion}" 
                                   title="vinculacion"  >
                    <f:selectItems value="#{vinculacion.vinculacionItemsAvailableSelectOne}"/>
               </h:selectOneMenu>
                
                <h:outputText value="Rol:" style="font-weight:bold"/>
                <h:selectOneMenu   id="rol" 
                                   value="#{participantes.participantesb.rol}" 
                                   title="rol"  >
                    <f:selectItems value="#{rol.rolItemsAvailableSelectOneAdmin}"/> 
               </h:selectOneMenu>
                
                <h:outputText value="Grupo de Investigación:" style="font-weight:bold"/>
                <h:selectOneMenu id="grupoinvestigacion" 
                                 value="#{participantes.participantesb.grupoInvestigacionidGrupoInvestigacion}"
                                 title="Grupo Investigacion" >
                    <f:selectItems value="#{GrupoInvestigacion.grupoInvestigacionItemsAvailableSelectOne}"/>
                </h:selectOneMenu>
                <h:outputText value="Estado:" style="font-weight:bold"/>
                <h:selectOneMenu id="estado" 
                                 value="#{participantes.participantesb.estado}"
                                 title="Grupo Investigacion" >
                    <f:selectItem itemValue="---" />
                    <f:selectItem itemValue="Activo" />
                    <f:selectItem itemValue="No_Activo" />                    
                </h:selectOneMenu>
            </h:panelGrid>

                <br />
            
           <h:commandButton action="#{participantes.busca_participantes}" 
                            value="Aplicar Filtro"/>
           <br />
           <br />
        </h:form> 
           
           <h:form styleClass="jsfcrud_list_form">
           <h:outputText escape="false" value="(no hay resultados para la busqueda)<br />" rendered="#{participantes.pagingInfo.itemCount == 0}" />
           <h:panelGroup rendered="#{participantes.pagingInfo.itemCount > 0}">
                <h:commandButton action="#{participantes.prev}" value="Anterior #{participantes.pagingInfo.batchSize}" rendered="#{participantes.pagingInfo.firstItem >= participantes.pagingInfo.batchSize}" image="../ant.png"/>&nbsp;
                <h:outputText value="Participante #{participantes.pagingInfo.firstItem + 1}-#{participantes.pagingInfo.lastItem} de #{participantes.pagingInfo.itemCount}"/>&nbsp;
                
                <h:commandButton action="#{participantes.next}" value="Sig #{participantes.pagingInfo.batchSize}" 
                                 rendered="#{participantes.pagingInfo.lastItem + participantes.pagingInfo.batchSize <= participantes.pagingInfo.itemCount}" image="../sig.png"/>
                <h:commandButton action="#{participantes.next}" value="Siguientes #{participantes.pagingInfo.itemCount - participantes.pagingInfo.lastItem} " image="../sig.png"
                               rendered="#{participantes.pagingInfo.lastItem < participantes.pagingInfo.itemCount && participantes.pagingInfo.lastItem + participantes.pagingInfo.batchSize > participantes.pagingInfo.itemCount}"/>
                
                <h:dataTable value="#{participantes.participantes_lista}" var="item" border="0" cellpadding="2" cellspacing="0" rowClasses="jsfcrud_odd_row,jsfcrud_even_row" rules="all" style="border:solid 1px">
                    <h:column>
                        <f:facet name="header">
                            <h:outputText value="CEDULA"/>
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
                
                    <h:column>
                        <f:facet name="header">
                            <h:outputText value="Vinculación"/>
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
                            <h:outputText value="Grupo Investigacion"/>
                        </f:facet>
                        <h:outputText value="#{item.grupoInvestigacionidGrupoInvestigacion.nombre}"/>
                    </h:column>

                    <h:column>
                        <f:facet name="header">
                            <h:outputText escape="false" value="&nbsp;"/>
                        </f:facet>
                        <h:outputText value=" "/>
                        <h:commandButton value="Detalle" 
                                         action="#{participantes.detailSetup}"  title="Detalle del participante" >
                            <f:param name="jsfcrud.currentParticipantes" 
                                     value="#{jsfcrud_class['beans.util.JsfUtil'].jsfcrud_method['getAsConvertedString'][item][participantes.converter].jsfcrud_invoke}"/>
                        </h:commandButton>
                        
                        <h:outputText value=" "/>
                        <h:commandButton value="Desvincular" 
                                         action="#{participantes.desvincular}"
                                         image="../delete.png"
                                         title="Desvincular el participante"
                                         onclick="return confirmDelete();">
                            <f:param name="jsfcrud.currentParticipantes" 
                                     value="#{jsfcrud_class['beans.util.JsfUtil'].jsfcrud_method['getAsConvertedString'][item][participantes.converter].jsfcrud_invoke}"/>
                        </h:commandButton>
                    </h:column>
                    
                 </h:dataTable>
                
            </h:panelGroup>
                </h:form>
            <br />
            <%-- <h:form styleClass="jsfcrud_list_form">       
                <br />
                <h:commandButton action="#{participantes.createSetupA}" value="Nuevo Participante"/>           
            </h:form>--%>
        </body>
    </html>
</f:view>
