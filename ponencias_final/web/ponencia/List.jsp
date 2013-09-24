<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>

<%@taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@taglib uri="http://richfaces.org/rich" prefix="rich" %>
<%@taglib uri="https://ajax4jsf.dev.java.net/ajax" prefix="ajax" %>

<f:view>
    <html>        
        <head> 
            <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
             <h:form>   
                 
                <h:commandLink title="volver al inicio" action="#{participantes.validaWelcome}">
                <img src = "../Home.png" onfocus="" /><h:outputText value="Inicio"/>
                </h:commandLink> | 
                <h:commandLink  title="cambiar contraseña" action="#{participantes.go_change}">
                    <h:graphicImage value="../clave.png" rendered="true" />           <Strong><h:outputText value="Cambiar contraseña"/>
                </Strong> </h:commandLink> | 
                <h:commandLink  title="Usuario" action="#{participantes.editSetup}">
                    <h:graphicImage value="../user.png" rendered="true" />           <Strong><h:outputText value="#{user.nombre}"/>
                </Strong> </h:commandLink> |
                <h:outputText value=" #{participantes.fecha}"/> 
                <div align="right">
                    <h:commandLink action="#{participantes.logout}" value="(salir)">
                        <h:graphicImage value="../exit.png" rendered="true"/>
                    </h:commandLink>
                </div>
             </h:form>     
                
            <title>Mis Ponencias</title>

            <link rel="stylesheet" type="text/css" href="/ponencias/faces/jsfcrud.css" />
    </head>        
        <body>                        
            <script  language="javascript">
               function confirmDelete()
               {
                   return confirm('Esta seguro que desea Eliminar la ponencia?');
               }
            </script> 
 
            <h:panelGroup id="messagePanel" layout="block">
                <h:messages errorStyle="color: red" infoStyle="color: green" layout="table"/>
            </h:panelGroup>        
            <h:outputText value="HOME > Consultar ponencia - Mis ponencias"/>
        <h1>Mis Ponencias</h1>
        <h:form>
        <%--filtro de busqueda--%>           
                <h:outputText value="FILTRAR PONENCIAS" style="font-weight:bold"/>
                <h:panelGrid columns="2" border="0">
                    <h:outputText value="Participante"
                                  style="font-weight:bold"
                                  rendered="#{participantes.p.rol.idrol != 2}"/>
                    <h:inputText maxlength="11" value="#{ponencia.ponenciab.participantesCedula}"
                                     id="participante"
                                     rendered="#{participantes.p.rol.idrol != 2}"
                                     />
                    
                    <h:outputText value="Desde" style="font-weight:bold"/>
                    <rich:calendar value="#{ponencia.ponenciab.fechaInicial}"
                                   id = "fechai"
                                   label="Fecha de inicio de la búsqueda (d/mm/yyyy)"/>
                    
                    <h:outputText value="Hasta" style="font-weight:bold"/>
                    <rich:calendar value="#{ponencia.ponenciab2.fechaInicial}"
                                   id = "fechaf"
                                   label="Fecha final de la búsqueda (d/mm/yyyy)"/>  
                    
                                                          
                    <%-- en la base de datos crear una nueva tabla para pais, ciudad --%>
                    
                    <h:outputText value="Ciudad" style="font-weight:bold"  />                    
                    <h:selectOneMenu id="ciudad" value="#{ponencia.ponenciab.ciudadidCiudad}">                        
                        <f:selectItems value="#{Ciudad.ciudadItemsAvailableSelectOne}"/>
                    </h:selectOneMenu>                    
                                      
                    
                    <h:outputText value="Tipo" style="font-weight:bold"/>
                    <h:selectOneMenu id ="tipo" value="#{ponencia.ponenciab.tipoIdtipo}">
                        <f:selectItems value="#{TipoPonencia.tipoItemsAvailableSelectOne}"/>
                    </h:selectOneMenu>
                    
                    <h:outputText value="Apoyo" style="font-weight:bold"/>
                    <h:selectOneMenu id ="apoyo" value="#{ponencia.ponenciab.apoyoIdapoyo}">
                        <f:selectItems value="#{apoyo.apoyoItemsAvailableSelectOne}"/>
                    </h:selectOneMenu>
                    
                    <h:outputText value="Proyecto" style="font-weight:bold"/>
                    <h:selectOneMenu id ="proyecto" value="#{ponencia.ponenciab.proyectoIdproyecto}">
                        <f:selectItems value="#{proyecto.proyectoItemsAvailableSelectOne}"/>
                        <%--<f:selectItems value="#{proyecto.proyectoItemsAvailableSelectOneActivos}"/>--%>
                        <%--<f:selectItems value="#{proyecto}"--%>
                    </h:selectOneMenu>
                       
                </h:panelGrid>
                
                <h:commandButton value="Aplicar Filtro" action="#{ponencia.busca_ponencia(participantes.p.rol.idrol,participantes.p.cedula)}">
                    <f:param name="jsfcrud.currentParticipantes" value="#{jsfcrud_class['beans.util.JsfUtil'].jsfcrud_method['getAsConvertedString'][item][participantes.converter].jsfcrud_invoke}"/>
                </h:commandButton>
        
        </h:form>
        
        <h:form styleClass="jsfcrud_list_form">
            <h:outputText escape="false" value="(no existen Ponencias con estas características)<br />" rendered="#{ponencia.pagingInfo.itemCount == 0}" />
            <h:panelGroup rendered="#{ponencia.pagingInfo.itemCount > 0}" >
               <h:commandButton action="#{ponencia.prev}" value="Anterior #{ponencia.pagingInfo.batchSize}" rendered="#{ponencia.pagingInfo.firstItem >= ponencia.pagingInfo.batchSize}" image="../ant.png"/>
                <h:outputText value="Ponencia #{ponencia.pagingInfo.firstItem + 1}-#{ponencia.pagingInfo.lastItem} de #{ponencia.pagingInfo.itemCount}"/>&nbsp;
                
                <h:commandButton action="#{ponencia.next}" value="Sig #{ponencia.pagingInfo.batchSize}" 
                                 rendered="#{ponencia.pagingInfo.lastItem + ponencia.pagingInfo.batchSize <= ponencia.pagingInfo.itemCount}" image="../sig.png"/>
                <h:commandButton action="#{ponencia.next}" value="Siguientes #{ponencia.pagingInfo.itemCount - ponencia.pagingInfo.lastItem} " image="../sig.png"
                               rendered="#{ponencia.pagingInfo.lastItem < ponencia.pagingInfo.itemCount && ponencia.pagingInfo.lastItem + ponencia.pagingInfo.batchSize > ponencia.pagingInfo.itemCount}"/>

                <h:dataTable value="#{ponencia.ponencias_lista}" var="item" border="0" cellpadding="2" cellspacing="0" rowClasses="jsfcrud_odd_row,jsfcrud_even_row" rules="all" style="border:solid 1px">
                    <h:column rendered="#{participantes.p.rol.idrol != 2}">
                        <f:facet name="header">
                            <h:outputText value="Cédula"/>
                        </f:facet>
                        <h:outputText value="#{item.participantesCedula.cedula}"/>
                    </h:column>
                    <h:column>
                        <f:facet name="header">
                            <h:outputText value="Ponencia"/>
                        </f:facet>
                        <h:outputText value="#{item.nombre}"/>
                    </h:column>                    
                    <h:column>
                        <f:facet name="header">
                            <h:outputText value="FechaInicial"/>
                        </f:facet>
                        <h:outputText value="#{item.fechaInicial}">
                            <f:convertDateTime pattern="d/M/yyyy"/>
                        </h:outputText>
                    </h:column>
                    <h:column>
                        <f:facet name="header">
                            <h:outputText value="Proyecto"/>
                        </f:facet>
                        <h:outputText value="#{item.proyectoIdproyecto.nombre}"/>
                    </h:column>                    
                    <h:column>
                        <f:facet name="header">
                            <h:outputText value="Tipo"/>
                        </f:facet>
                        <h:outputText value="#{item.tipoIdtipo.tipo}"/>
                    </h:column> 
                    
                    
                    <h:column>
                        <f:facet name="header">
                            <h:outputText escape="false" value="&nbsp;"/>
                        </f:facet>

                        <h:outputText value=" "/>
                        <h:commandButton value="Detalle"
                                         action="#{ponencia.detailSetup}"
                                         title="Detalles de la ponencia">
                            <f:param name="jsfcrud.currentPonencia"
                                     value="#{jsfcrud_class['beans.util.JsfUtil'].jsfcrud_method['getAsConvertedString'][item][ponencia.converter].jsfcrud_invoke}"/>
                        </h:commandButton>
                        
                        <h:outputText value=" "/>
                        <h:commandButton value="Borrar" onfocus=""
                                         action="#{ponencia.remove(participantes.participantes.cedula, participantes.p.rol.idrol)}"
                                         image="../delete.png" 
                                         title="Borrar ponencia" 
                                         onclick="return confirmDelete();">
                            <f:param name="jsfcrud.currentPonencia" 
                                     value="#{jsfcrud_class['beans.util.JsfUtil'].jsfcrud_method['getAsConvertedString'][item][ponencia.converter].jsfcrud_invoke}"/>
                        </h:commandButton>
                    </h:column>

                </h:dataTable>
                
                <br/>           
            </h:panelGroup>
                <h:commandButton value="Crear nueva Ponencia" 
                                 action="#{ponencia.createSetup}"
                                 title="Crear una nueva ponencia"/>
            <br />
        </h:form>                                                          
        </body>
    </html>
</f:view>